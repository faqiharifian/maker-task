package com.google.developer.taskmaker;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.taskmaker.data.DatabaseContract;
import com.google.developer.taskmaker.data.TaskAdapter;

public class MainActivity extends AppCompatActivity implements
        TaskAdapter.OnItemClickListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID_MESSAGES = 0;

    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new TaskAdapter(null);
        mAdapter.setOnItemClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ID_MESSAGES, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    /* Click events in RecyclerView items */
    @Override
    public void onItemClick(View v, int position) {
        Intent i = new Intent(this, TaskDetailActivity.class);
        i.setData(ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, mAdapter.getItemId(position)));
        startActivity(i);
    }

    /* Click events on RecyclerView item checkboxes */
    @Override
    public void onItemToggled(boolean active, int position) {
        //TODO: Handle task item checkbox event
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DatabaseContract.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("count ", data.getCount()+"");
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
