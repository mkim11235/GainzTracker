package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentValues;
import android.content.Context;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/20/2016.
 */

public class EditExerciseHistoryTask extends DbTask<Long> {

    private static final int INDEX_EXERCISE_ID = 0;
    private static final int INDEX_WEIGHT = 1;
    private static final int INDEX_REPS = 2;
    private static final int INDEX_DATE = 3;
    private static final int INDEX_EXERCISE_HISTORY_ID = 4;

    public EditExerciseHistoryTask(Context context) {
        super(context);
    }

    /**
     * Updates exerciseHistoryEntry with new values
     *
     * @param longs long parameters exercisId, weight, reps, date, exerciseHistoryId
     * @return null
     */
    @Override
    protected Void doInBackground(Long... longs) {
        Long exerciseId = longs[INDEX_EXERCISE_ID];
        Long weight = longs[INDEX_WEIGHT];
        Long reps = longs[INDEX_REPS];
        Long date = longs[INDEX_DATE];
        Long exerciseHistoryId = longs[INDEX_EXERCISE_HISTORY_ID];

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
                    new String[]{Long.toString(exerciseHistoryId)});
        }

        return null;
    }

}
