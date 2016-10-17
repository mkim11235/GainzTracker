package com.example.mkim11235.gainztracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

// Once clicked on specific exercise from main, enter here
// Has Exercise title, history of workouts as list
// button at bottom to add new entry to history
// hopefully later can support holding click to delete or change entry
public class ExerciseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXERCISE_HISTORY_LOADER = 1; // try doing 0 later see wat hapen

    private static final String[] EXERCISE_HISTORY_COLUMNS = {
            DatabaseContract.ExerciseHistoryEntry._ID,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE
    };

    static final int COL_EXERCISE_HISTORY_EXERCISE_ID = 3;
    static final int COL_EXERCISE_HISTORY_WEIGHT = 4;
    static final int COL_EXERCISE_HISTORY_REPS = 5;
    static final int COL_EXERCISE_HISTORY_DATE = 6;

    private ImageButton mAddExerciseHistoryEntryButton;
    private ExerciseHistoryAdapter mExerciseHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Get exercise id from intent
        Intent intent = getIntent();
        long exerciseId = intent.getLongExtra(Intent.EXTRA_TEXT, -1L);

        // Need to somehow pass exerciseId into loader for query
        getSupportLoaderManager().initLoader(EXERCISE_HISTORY_LOADER, null, this);
        mExerciseHistoryAdapter = new ExerciseHistoryAdapter(this, null, 0);

        // Gets and sets exercise title
        final String exerciseTitle = getAndSetExerciseTitleFromId(exerciseId);

        // Setup adaptor to populate listview
        ListView listView = (ListView) findViewById(R.id.listview_exercise_history);
        listView.setAdapter(mExerciseHistoryAdapter);

        /*
        // Add functionality for details maybe later
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                long id = mExerciseHistoryAdapter.getItemId(position);
            }
        });
        */

        mAddExerciseHistoryEntryButton = (ImageButton) findViewById(R.id.image_button_add_exercise_history_entry);
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TO DO: Start new activity for creating new history entry
                // pass in the name of exercise as extra
                Intent intent = new Intent(view.getContext(), AddExerciseHistoryEntryActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, exerciseTitle);
                startActivity(intent);
            }
        });
    }

    // Gets and sets Exercise Title from ID
    // Has to read database so async task
    // TO DO: looks ugly try optimize later
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

    // need to query for only EH entries with matching exercise_id
    // TO DO: Needs work.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                EXERCISE_HISTORY_COLUMNS,
                null,
                null,
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
