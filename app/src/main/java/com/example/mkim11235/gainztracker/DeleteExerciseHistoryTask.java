package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

public class DeleteExerciseHistoryTask extends AsyncTask<String, Void, Void> {
    private final Context mContext;

    public DeleteExerciseHistoryTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String exerciseWeight = strings[0];
        String exerciseReps = strings[1];
        String exerciseDate = strings[2];

        mContext.getContentResolver().delete(DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " = ? AND "
                        + DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " = ? AND "
                        + DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " = ?",
                new String[] {exerciseWeight, exerciseReps, exerciseDate});
        return null;
    }
}
