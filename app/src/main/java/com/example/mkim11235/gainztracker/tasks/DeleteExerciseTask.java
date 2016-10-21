package com.example.mkim11235.gainztracker.tasks;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.ExerciseAdapter;
import com.example.mkim11235.gainztracker.MainActivity;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

/**
 * Deletes entry w/ name,muscle from exercises table
 * Also deletes all entries in exercisehistory table with matching exerciseId
 */
public class DeleteExerciseTask extends AsyncTask<String, Void, Void> {
    private MainActivity mContext;
    private ExerciseAdapter mAdapter;

    public DeleteExerciseTask(MainActivity context, ExerciseAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    // Deletes given exercise (name, muscle) from DB
    @Override
    protected Void doInBackground(String... strings) {
        String exerciseName = strings[0];
        String exerciseMuscle = strings[1];

        int position = Integer.parseInt(strings[2]);
        String exerciseId = Long.toString(getExerciseIdGivenPosition(position));

        int rowsDeleted = mContext.getContentResolver().delete(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                DatabaseContract.ExerciseEntry.COLUMN_NAME + " = ? AND "
                        + DatabaseContract.ExerciseEntry.COLUMN_MUSCLE + " = ?",
                new String[] {exerciseName, exerciseMuscle});

        int exerciseHistoryRowsDeleted = mContext.getContentResolver().delete(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ? ",
                new String[] {exerciseId});

        return null;
    }

    private Long getExerciseIdGivenPosition(int position) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(0);
    }
}
