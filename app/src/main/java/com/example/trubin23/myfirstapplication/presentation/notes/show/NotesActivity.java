package com.example.trubin23.myfirstapplication.presentation.notes.show;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.trubin23.myfirstapplication.MyCustomApplication;
import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.DeleteNoteUseCase;
import com.example.trubin23.myfirstapplication.presentation.common.BaseActivity;
import com.example.trubin23.myfirstapplication.presentation.notes.add.EditNoteActivity;
import com.example.trubin23.myfirstapplication.presentation.notes.show.notelist.NoteItemActionHandler;
import com.example.trubin23.myfirstapplication.presentation.notes.show.notelist.RecyclerNoteAdapter;
import com.example.trubin23.myfirstapplication.presentation.notes.utils.ThemeChanger;
import com.example.trubin23.myfirstapplication.storage.database.DatabaseHelper;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;
import com.example.trubin23.myfirstapplication.storage.database.NoteDaoImpl;
import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;
import com.example.trubin23.myfirstapplication.storage.network.ResponseProcessing;
import com.example.trubin23.myfirstapplication.storage.network.RestError;
import com.example.trubin23.myfirstapplication.storage.network.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.example.trubin23.myfirstapplication.storage.model.NoteStorage.NOTE_UID;

public class NotesActivity extends BaseActivity implements
        NotesContract.View,
        NoteItemActionHandler,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String ACTION_CHANGED_DB = "action-changed-db";

    private static final String TAG = "NotesActivity";

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ChangedDbReceiver mChangedDbReceiver;

    private boolean mFirstStart;

    private NotesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        createPresenter();

        mFirstStart = savedInstanceState == null;

        mRecyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(llm);
        } else {
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerNoteAdapter mRecyclerNoteAdapter = new RecyclerNoteAdapter(this);
        mRecyclerView.setAdapter(mRecyclerNoteAdapter);

        mChangedDbReceiver = new ChangedDbReceiver();

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void createPresenter() {
        DeleteNoteUseCase deleteNoteUseCase =  new DeleteNoteUseCase();
        mPresenter = new NotesPresenter(mUseCaseHandler, deleteNoteUseCase);
        bindPresenterToView(mPresenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_choicer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.theme_choicer == item.getItemId()) {
            new ThemeChanger(this).showDialog();
        }

        return true;
    }

    @OnClick(R.id.button_create_note)
    public void onClickCreateNote(View view) {
        Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEdit(@NonNull String uid) {
        Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
        intent.putExtra(NOTE_UID, uid);
        startActivity(intent);
    }

    @Override
    public void onDelete(@NonNull final String uid, @NonNull String noteTitle, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);

        builder.setTitle(noteTitle);
        builder.setMessage(R.string.message_about_delete);

        builder.setPositiveButton(getString(android.R.string.ok),
                (dialogInterface, i) -> mPresenter.deleteNote(uid));

        builder.setNegativeButton(getString(android.R.string.cancel), null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    private ResponseProcessing<List<NoteStorage>> refreshProcessing() {
        return new ResponseProcessing<List<NoteStorage>>() {
            Resources res = getApplicationContext().getResources();

            @Override
            public void success(List<NoteStorage> noteStorages) {
                NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();
                Completable.fromAction(() -> noteDao.notesSync(noteStorages))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    reloadNotesFromDb();
                                    Resources res = getResources();
                                    Toast.makeText(getApplicationContext(), res.getString(R.string.notes_sync)
                                            + "\n" + res.getString(R.string.success), Toast.LENGTH_SHORT).show();
                                },
                                throwable -> Log.e(TAG, "noteStorages.forEach(noteDao::addNote)", throwable));
            }

            @Override
            public void successWithoutBody() {
                super.successWithoutBody();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void error(RestError restError) {
                super.error(restError);
                Toast.makeText(getApplicationContext(), res.getString(R.string.notes_sync) + "\n" +
                                res.getString(R.string.error) + restError.getCode(),
                        Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<NoteStorage>> call, Throwable t) {
                super.onFailure(call, t);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        RetrofitClient.getNotes(refreshProcessing());
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mChangedDbReceiver,
                new IntentFilter(ACTION_CHANGED_DB));

        if (mFirstStart) {
            mFirstStart = false;
            onRefresh();
        } else {
            reloadNotesFromDb();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mChangedDbReceiver);
        super.onPause();
    }

    private void reloadNotesFromDb() {
        mSwipeRefreshLayout.setRefreshing(true);
        Single.create((SingleOnSubscribe<Cursor>) emitter -> {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
            if (databaseHelper != null) {
                NoteDao noteDao = new NoteDaoImpl(databaseHelper);
                emitter.onSuccess(noteDao.getCursorAllData());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cursor -> {
                            ((RecyclerNoteAdapter) mRecyclerView.getAdapter()).swapCursor(cursor);
                            mSwipeRefreshLayout.setRefreshing(false);
                        },
                        throwable -> mSwipeRefreshLayout.setRefreshing(false)
                );
    }

    @Override
    public void showSuccessToast(int eventId) {
        Resources res = getResources();
        Toast.makeText(this, res.getString(eventId) + "\n"
                + res.getString(R.string.success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorToast(int eventId) {
        Resources res = getResources();
        Toast.makeText(this, res.getString(eventId) + "\n"
                + res.getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    private class ChangedDbReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            reloadNotesFromDb();
        }
    }
}