package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentValues;
import android.content.Context;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/25/2016.
 */

public class UpdateExerciseTask extends DbTask<String> {

    public UpdateExerciseTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String exerciseId = strings[0];
        String exerciseName = strings[1];
        String exerciseMuscle = strings[2];

        // Setup new values
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_NAME, exerciseName);
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_MUSCLE, exerciseMuscle);

        int rowsUpdated = mContentResolver.update(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                newValues,
                DatabaseContract.ExerciseEntry._ID + " = ? ",
                new String[] {exerciseId});

        return null;
    }
}
