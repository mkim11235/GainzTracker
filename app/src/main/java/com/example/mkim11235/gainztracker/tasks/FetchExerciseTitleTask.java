package com.example.mkim11235.gainztracker.tasks;

import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.ExerciseHistoryFragment;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/17/2016.
 */

// Given exercise id, reads exercise table and gets the name
// Try to set exerciseActivity title to name + "History"
public class FetchExerciseTitleTask extends AsyncTask<Long, Void, Void> {
    private final ExerciseHistoryFragment mFragment;

    public FetchExerciseTitleTask(Fragment fragment) {
        mFragment = (ExerciseHistoryFragment) fragment;
    }

    /**
     * Gets the name of exercise with given exerciseId
     * Sets title of ExerciseActivity to name
     * Sets member variable exerciseName to name
     * Sets bundle in the context
     * @param longs 0 is the exerciseId to query
     */
    @Override
    protected Void doInBackground(Long... longs) {
        long exerciseId = longs[0];

        // Find row in exercises where id = id
        Cursor cursor = mFragment.getActivity().getContentResolver().query(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseEntry.COLUMN_NAME},
                DatabaseContract.ExerciseEntry._ID + " = ?",
                new String[] {Long.toString(exerciseId)},
                null);

        if (cursor.moveToFirst()) {
            String exerciseName = cursor.getString(0);

            mFragment.setExerciseName(exerciseName);
            mFragment.setBundle();
        }

        cursor.close();
        return null;
    }

}
