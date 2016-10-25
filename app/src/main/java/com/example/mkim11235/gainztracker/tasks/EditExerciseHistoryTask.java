package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentValues;
import android.content.Context;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/20/2016.
 */

public class EditExerciseHistoryTask extends DbTask<Long> {

    public EditExerciseHistoryTask(Context context) {
        super(context);
    }

    /**
     * Updates exerciseHistoryEntry with new values
     * @param longs long parameters exercisId, weight, reps, date, exerciseHistoryId
     * @return null
     */
    @Override
    protected Void doInBackground(Long... longs) {
        Long exerciseId = longs[0];
        Long weight = longs[1];
        Long reps = longs[2];
        Long date = longs[3];
        Long exerciseHistoryId = longs[4];

        // Update entry with id with new values
        if (exerciseHistoryId != null) {
            // setup content values for update
            ContentValues newValues = new ContentValues();
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID, exerciseId);
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT, weight);
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS, reps);
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE, date);

            mContentResolver.update(
                    DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                    newValues,
                    DatabaseContract.ExerciseHistoryEntry._ID + " = ? ",
                    new String[] {Long.toString(exerciseHistoryId)});
        }

        return null;
    }

}
