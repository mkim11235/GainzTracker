package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/17/2016.
 */

/**
 * Adds new exerciseHistoryEntry to db
 */
public class AddExerciseHistoryTask extends DbTask<Long> {

    private static final int INDEX_EXERCISE_ID = 0;
    private static final int INDEX_WEIGHT = 1;
    private static final int INDEX_REPS = 2;
    private static final int INDEX_DATE = 3;

    public AddExerciseHistoryTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(Long... longs) {
        long id = longs[INDEX_EXERCISE_ID];
        long weight = longs[INDEX_WEIGHT];
        long reps = longs[INDEX_REPS];
        long date = longs[INDEX_DATE];

        addExerciseHistoryEntry(id, weight, reps, date);
        return null;
    }

    // Inserts exerciseHistoryEntry into DB
    private long addExerciseHistoryEntry(long id, long weight, long reps, long date) {
        long exerciseHistoryEntryId;

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID, id);
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT, weight);
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS, reps);
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE, date);

        Uri insertedUri = mContentResolver.insert(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                values
        );

        exerciseHistoryEntryId = ContentUris.parseId(insertedUri);
        return exerciseHistoryEntryId;
    }
}
