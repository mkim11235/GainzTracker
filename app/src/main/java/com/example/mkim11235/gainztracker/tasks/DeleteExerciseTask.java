package com.example.mkim11235.gainztracker.tasks;

import android.content.Context;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/24/2016.
 */

public class DeleteExerciseTask extends DbTask<String> {

    public DeleteExerciseTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String exerciseId = strings[0];

        mContentResolver.delete(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                DatabaseContract.ExerciseEntry._ID + " = ?",
                new String[] {exerciseId});

        mContentResolver.delete(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ? ",
                new String[] {exerciseId});

        return null;
    }
}
