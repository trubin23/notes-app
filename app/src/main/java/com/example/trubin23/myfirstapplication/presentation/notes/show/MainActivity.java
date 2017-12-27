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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.MyCustomApplication;
import com.example.trubin23.myfirstapplication.presentation.notes.add.EditNoteActivity;
import com.example.trubin23.myfirstapplication.presentation.notes.show.notelist.NoteItemActionHandler;
import com.example.trubin23.myfirstapplication.presentation.notes.show.notelist.NotesCursorLoader;
import com.example.trubin23.myfirstapplication.presentation.notes.show.notelist.RecyclerNoteAdapter;
import com.example.trubin23.myfirstapplication.presentation.notes.utils.ThemeChanger;
import com.example.trubin23.myfirstapplication.storage.database.Note;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;
import com.example.trubin23.myfirstapplication.storage.network.ResponseProcessing;
import com.example.trubin23.myfirstapplication.storage.network.RestError;
import com.example.trubin23.myfirstapplication.storage.network.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static com.example.trubin23.myfirstapplication.storage.database.Note.NOTE_UID;

public class MainActivity extends AppCompatActivity
        implements NoteItemActionHandler,
        SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final int CURSOR_LOADER_ID = 0;

    public static final String ACTION_CHANGED_DB = "action-changed-db";

    private static final String TAG = "MainActivity";

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ChangedDbReceiver mChangedDbReceiver;

    private boolean mFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

        String[] permissions = {INTERNET, ACCESS_NETWORK_STATE};

        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
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
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEdit(@NonNull String uid) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(NOTE_UID, uid);
        startActivity(intent);
    }

    @Override
    public void onDelete(@NonNull final String uid, @NonNull String noteTitle, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(noteTitle);
        builder.setMessage(R.string.message_about_delete);

        builder.setPositiveButton(getString(android.R.string.ok),
                (dialogInterface, i) -> RetrofitClient.deleteNote(uid, deleteProcessing()));

        builder.setNegativeButton(getString(android.R.string.cancel), null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    private ResponseProcessing<Note> deleteProcessing() {
        return new ResponseProcessing<Note>() {
            Resources res = getApplicationContext().getResources();

            @Override
            public void success(Note note) {
                NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();
                Completable.fromAction(() -> noteDao.deleteNote(note.getUid()))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    forceLoad();
                                    Resources res = getResources();
                                    Toast.makeText(getApplicationContext(), res.getString(R.string.note_deleted)
                                            + "\n" + res.getString(R.string.success), Toast.LENGTH_SHORT).show();
                                },
                                throwable -> Log.e(TAG, "noteDao.deleteNote", throwable));
            }

            @Override
            public void error(RestError restError) {
                super.error(restError);
                Toast.makeText(getApplicationContext(),
                        res.getString(R.string.note_deleted) + "\n" +
                                res.getString(R.string.error_code) + restError.getCode(),
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    private ResponseProcessing<List<Note>> refreshProcessing() {
        return new ResponseProcessing<List<Note>>() {
            Resources res = getApplicationContext().getResources();

            @Override
            public void success(List<Note> notes) {
                NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();
                Completable.fromAction(() -> noteDao.notesSync(notes))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    forceLoad();
                                    Resources res = getResources();
                                    Toast.makeText(getApplicationContext(), res.getString(R.string.notes_sync)
                                            + "\n" + res.getString(R.string.success), Toast.LENGTH_SHORT).show();
                                },
                                throwable -> Log.e(TAG, "notes.forEach(noteDao::addNote)", throwable));
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
                                res.getString(R.string.error_code) + restError.getCode(),
                        Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mChangedDbReceiver,
                new IntentFilter(ACTION_CHANGED_DB));

        if (mFirstStart) {
            mFirstStart = false;
            onRefresh();
        } else {
            forceLoad();
        }

        super.onResume();
    }

    private void forceLoad() {
        getSupportLoaderManager().getLoader(CURSOR_LOADER_ID).forceLoad();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mChangedDbReceiver);
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new NotesCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ((RecyclerNoteAdapter) mRecyclerView.getAdapter()).swapCursor(cursor);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(CURSOR_LOADER_ID);
    }

    private class ChangedDbReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    }
}