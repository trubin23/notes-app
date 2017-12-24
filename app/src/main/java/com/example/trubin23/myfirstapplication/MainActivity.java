package com.example.trubin23.myfirstapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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

import com.example.trubin23.database.NoteDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static com.example.trubin23.myfirstapplication.Note.NOTE_UID;

public class MainActivity extends AppCompatActivity
        implements NoteItemActionHandler,
        SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Cursor>
{
    public static final int CURSOR_LOADER_ID = 0;

    public static final String ACTION_CHANGED_DB = "action-changed-db";
    public static final String NOTES = "notes";

    private static final int EDIT_NOTE_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST = 1;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerNoteAdapter mRecyclerNoteAdapter;

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

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(llm);
        } else {
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL);//number or const
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerNoteAdapter = new RecyclerNoteAdapter(this);
        mRecyclerView.setAdapter(mRecyclerNoteAdapter);

        mChangedDbReceiver = new ChangedDbReceiver();

        mSwipeRefreshLayout.setOnRefreshListener(this);

        String[] permissions = {INTERNET, ACCESS_NETWORK_STATE};

        onRequestPermissions(permissions);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    private void onRequestPermissions(String... permissions) {
        List<String> requestPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                //if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                requestPermissions.add(permission);
            }
        }

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    requestPermissions.toArray(new String[0]), MY_PERMISSIONS_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST) {
            return;
        }

        List<String> requestPermission = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                requestPermission.add(permissions[i]);
            }
        }

        onRequestPermissions(requestPermission.toArray(new String[0]));
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
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
    }

    @Override
    public void onEdit(@NonNull String uid) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(NOTE_UID, uid);
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
    }

    @Override
    public void onDelete(@NonNull final String uid, final String noteTitle, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(noteTitle);
        builder.setMessage(R.string.message_about_delete);

        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();
                        SyncWithServer.deleteNote(getApplicationContext(), noteDao, uid);
                    }
                });

        builder.setNegativeButton(getString(android.R.string.cancel), null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_NOTE_REQUEST_CODE) {
            Intent intent = new Intent(this, LoadNoteService.class);
            stopService(intent);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();
        SyncWithServer.notesSync(getApplicationContext(), noteDao);
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mChangedDbReceiver,
                new IntentFilter(ACTION_CHANGED_DB));

        if (mFirstStart) {
            mFirstStart = false;

            NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();
            SyncWithServer.notesSync(getApplicationContext(), noteDao);
        } else {
            getSupportLoaderManager().getLoader(CURSOR_LOADER_ID).forceLoad();
        }

        super.onResume();
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
            getSupportLoaderManager().getLoader(CURSOR_LOADER_ID).forceLoad();
        }
    };
}