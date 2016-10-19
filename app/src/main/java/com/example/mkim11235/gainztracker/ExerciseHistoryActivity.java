package com.example.mkim11235.gainztracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

import java.util.concurrent.ExecutionException;

// Once clicked on specific exercise from main, enter here
// Has Exercise title, history of workouts as list
// button at bottom to add new entry to history
// hopefully later can support holding click to delete or change entry
public class ExerciseHistoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXERCISE_HISTORY_LOADER = 1; // try doing 0 later see wat hapen

    private static final String[] EXERCISE_HISTORY_COLUMNS = {
            DatabaseContract.ExerciseHistoryEntry._ID,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE
    };

    static final int COL_EXERCISE_HISTORY_ID = 0;
    static final int COL_EXERCISE_HISTORY_EXERCISE_ID = 1;
    static final int COL_EXERCISE_HISTORY_WEIGHT = 2;
    static final int COL_EXERCISE_HISTORY_REPS = 3;
    static final int COL_EXERCISE_HISTORY_DATE = 4;

    private ImageButton mAddExerciseHistoryEntryButton;
    private ExerciseHistoryAdapter mExerciseHistoryAdapter;
    private long mExerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get exercise id from intent
        Intent intent = getIntent();
        mExerciseId = intent.getLongExtra(Intent.EXTRA_TEXT, -1L);

        // Need to somehow pass exerciseId into loader for query
        getSupportLoaderManager().initLoader(EXERCISE_HISTORY_LOADER, null, this);
        mExerciseHistoryAdapter = new ExerciseHistoryAdapter(this, null, 0);

        // Gets and sets exercise title
        // This might not be async
        // may need to learn about listeners and shiet
        // Todo: check is async or blocking
        final String exerciseTitle = getAndSetExerciseTitleFromId(mExerciseId);

        // Setup adaptor to populate listview
        ListView listView = (ListView) findViewById(R.id.listview_exercise_history);
        listView.setAdapter(mExerciseHistoryAdapter);
        registerForContextMenu(listView);

        /*
        // Add functionality for details maybe later
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                long id = mExerciseHistoryAdapter.getItemId(position);
            }
        });
        */

        mAddExerciseHistoryEntryButton = (ImageButton)
                findViewById(R.id.image_button_add_exercise_history_entry);
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pass in the exerciseID and name of exercise in bundle
                Bundle bundle = new Bundle();
                bundle.putLong(getString(R.string.EXTRA_EXERCISE_ID), mExerciseId);
                bundle.putString(getString(R.string.EXTRA_EXERCISE_NAME), exerciseTitle);

                Intent intent = new Intent(view.getContext(), AddExerciseHistoryEntryActivity.class)
                        .putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    // Gets and sets Exercise Title from ID
    // Has to read database so async task
    // MB DO: looks ugly try optimize later
    // mb needs to use loader
    private String getAndSetExerciseTitleFromId(long id) {
        FetchExerciseTitleTask exerciseTitleTask = new FetchExerciseTitleTask(this);
        try {
            return exerciseTitleTask.execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view
            , ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.listview_exercise_history) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

            String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        Cursor cursor = (Cursor) mExerciseHistoryAdapter.getItem(info.position);
        String exerciseWeight = cursor.getString(COL_EXERCISE_HISTORY_WEIGHT);
        String exerciseReps = cursor.getString(COL_EXERCISE_HISTORY_REPS);
        String exerciseDate = cursor.getString(COL_EXERCISE_HISTORY_DATE);

        String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];

        switch (menuItemName) {
            case "Edit":
                // Todo: implement edit stuff
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_LONG);
                break;
            case "Delete":
                getContentResolver().delete(DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                        DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " = ? AND "
                                + DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " = ? AND "
                                + DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " = ?",
                        new String[] {exerciseWeight, exerciseReps, exerciseDate});
                break;
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // May have to pass in exerciseID through bundle or member variable
        String exerciseIdString = Long.toString(mExerciseId);
        return new CursorLoader(this,
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                EXERCISE_HISTORY_COLUMNS,
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ? ",
                new String[] {exerciseIdString},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mExerciseHistoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mExerciseHistoryAdapter.swapCursor(null);
    }

}
