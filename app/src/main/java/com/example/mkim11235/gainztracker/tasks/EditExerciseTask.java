package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentValues;
import android.content.Context;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/25/2016.
 */

public class EditExerciseTask extends DbTask<String> {

    private static final int INDEX_EXERCISE_ID = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_MUSCLE = 2;

    public EditExerciseTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String exerciseId = strings[INDEX_EXERCISE_ID];
        String exerciseName = strings[INDEX_NAME];
        String exerciseMuscle = strings[INDEX_MUSCLE];

        // Setup new values
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_NAME, exerciseName);
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_MUSCLE, exerciseMuscle);

        mContentResolver.update(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                newValues,
                DatabaseContract.ExerciseEntry._ID + " = ? ",
                new String[]{exerciseId});

        return null;
    }
}
