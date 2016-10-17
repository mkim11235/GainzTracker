package com.example.mkim11235.gainztracker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by Michael on 10/16/2016.
 */

public class AddExerciseDBTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = AddExerciseDBTask.class.getSimpleName();

    private final Context mContext;

    public AddExerciseDBTask(Context context) {
        mContext = context;
    }

    // Inserts exercise into DB
    private long addExercise(String name, String muscle) {
        long exerciseId;

        Cursor exerciseCursor = mContext.getContentResolver().query(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                new String[]{DatabaseContract.ExerciseEntry._ID},
                DatabaseContract.ExerciseEntry.COLUMN_NAME + " = ?",
                new String[] {name},
                null);

        // If database already contains exercise with the given name
        if (exerciseCursor.moveToFirst()) {
            int exerciseIdIndex = exerciseCursor.getColumnIndex(DatabaseContract.ExerciseEntry._ID);
            exerciseId = exerciseCursor.getLong(exerciseIdIndex);
        } else {
            ContentValues values = new ContentValues();

            values.put(DatabaseContract.ExerciseEntry.COLUMN_NAME, name);
            values.put(DatabaseContract.ExerciseEntry.COLUMN_MUSCLE, muscle);

            Uri insertedUri = mContext.getContentResolver().insert(
                    DatabaseContract.ExerciseEntry.CONTENT_URI,
                    values
            );

            exerciseId = ContentUris.parseId(insertedUri);
        }

        exerciseCursor.close();
        return exerciseId;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String name = strings[0];
        String muscle = strings[1];

        long exerciseId = addExercise(name, muscle);
        return null;
    }
}
