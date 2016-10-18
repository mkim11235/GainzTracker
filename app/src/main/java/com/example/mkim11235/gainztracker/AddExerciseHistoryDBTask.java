package com.example.mkim11235.gainztracker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/17/2016.
 */

// Adds new entry (exerciseID as a string, weight, reps, date) into exercise history table
public class AddExerciseHistoryDBTask extends AsyncTask<Long, Void, Void> {
    private final Context mContext;

    public AddExerciseHistoryDBTask(Context context) {
        mContext = context;
    }

    // Inserts exercise into DB
    private long addExerciseHistoryEntry(long id, long weight, long reps, long date) {
        long exerciseHistoryEntryId;

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID, id);
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT, weight);
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS, reps);
        values.put(DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE, date);

        Uri insertedUri = mContext.getContentResolver().insert(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                values
        );

        exerciseHistoryEntryId = ContentUris.parseId(insertedUri);
        return exerciseHistoryEntryId;
    }

    @Override
    protected Void doInBackground(Long... longs) {
        long id = longs[0];
        long weight = longs[1];
        long reps = longs[2];
        long date = longs[3];

        long exerciseHistoryId = addExerciseHistoryEntry(id, weight, reps, date);
        return null;
    }
}
