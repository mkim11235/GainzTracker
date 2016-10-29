package com.example.mkim11235.gainztracker.tasks;

import android.content.Context;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

/**
 * Deletes given exercise history entry from DB
 */
public class DeleteExerciseHistoryTask extends DbTask<Long> {

    private static final int INDEX_EXERCISE_ID = 0;

    public DeleteExerciseHistoryTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(Long... longs) {
        Long exerciseId = longs[INDEX_EXERCISE_ID];

        mContentResolver.delete(DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                DatabaseContract.ExerciseHistoryEntry._ID + " = ?",
                new String[] {Long.toString(exerciseId)});

        return null;
    }

}
