package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

public class DeleteExerciseTask extends AsyncTask<String, Void, Void> {
    private final Context mContext;

    public DeleteExerciseTask(Context context) {
        mContext = context;
    }

    // Deletes given exercise (name, muscle) from DB
    @Override
    protected Void doInBackground(String... strings) {
        String exerciseName = strings[0];
        String exerciseMuscle = strings[1];

        int rowsDeleted = mContext.getContentResolver().delete(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                DatabaseContract.ExerciseEntry.COLUMN_NAME + " = ? AND "
                        + DatabaseContract.ExerciseEntry.COLUMN_MUSCLE + " = ?",
                new String[] {exerciseName, exerciseMuscle});

        return null;
    }
}
