package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/20/2016.
 */

public class UpdateExerciseHistoryTask extends AsyncTask<Long, Void, Void> {
    private ExerciseHistoryEntryActivity mContext;

    public UpdateExerciseHistoryTask(Context context) {
        mContext = (ExerciseHistoryEntryActivity) context;
    }

    /**
     * Finds entry in exercisehistory table with old values
     * Updates the entry with new values
     * @param longs
     * @return
     */
    @Override
    protected Void doInBackground(Long... longs) {
        Long exerciseId = longs[0];
        Long weight = longs[1];
        Long reps = longs[2];
        Long date = longs[3];

        Long oldWeight = longs[4];
        Long oldReps = longs[5];
        Long oldDate = longs[6];

        // Query db for entry with old values
        Long exerciseHistoryId = getExerciseHistoryIdWithOldValues(exerciseId, oldWeight, oldReps,
                oldDate);

        // Update entry with id with new values
        if (exerciseHistoryId != null) {
            // setup content values for update
            ContentValues newValues = new ContentValues();
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID, exerciseId);
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT, weight);
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS, reps);
            newValues.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE, date);

            int rowsUpdated = mContext.getContentResolver().update(
                    DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                    newValues,
                    DatabaseContract.ExerciseHistoryEntry._ID + " = ? ",
                    new String[] {Long.toString(exerciseHistoryId)});
        }

        return null;
    }

    private Long getExerciseHistoryIdWithOldValues(long exerciseId, long oldWeight, long oldReps, long oldDate) {
        String selection = DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ? AND " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " = ? AND " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " = ? AND " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " = ? ";
        String[] selectionArgs = new String[] {Long.toString(exerciseId), Long.toString(oldWeight),
                Long.toString(oldReps), Long.toString(oldDate)};

        Cursor cursor = mContext.getContentResolver().query(DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseHistoryEntry._ID},
                selection,
                selectionArgs,
                null);

        Long exerciseHistoryId = null;
        if (cursor.moveToFirst()) {
            exerciseHistoryId = cursor.getLong(0);
        }

        cursor.close();
        return exerciseHistoryId;
    }
}
