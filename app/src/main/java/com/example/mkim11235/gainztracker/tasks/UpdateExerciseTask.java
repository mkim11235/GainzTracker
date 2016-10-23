package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.ExerciseEntryActivity;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/20/2016.
 */

/**
 * Updates the db with new name and muscle
 */
public class UpdateExerciseTask extends AsyncTask<String, Void, Void> {
    private ExerciseEntryActivity mContext;

    public UpdateExerciseTask(Context context) {
        mContext = (ExerciseEntryActivity) context;
    }

    /**
     * First finds the _ID of the selected item.
     * Updates the exercise table entry with that ID with new values
     */
    @Override
    protected Void doInBackground(String... strings) {
        String newExerciseName = strings[0];
        String newExerciseMuscle = strings[1];
        String oldExerciseName = strings[2];
        String oldExerciseMuscle = strings[3];

        // I need to get id cuz unique
        // use id for selection & args
        long id = getExerciseIdWithNameMuscle(oldExerciseName, oldExerciseMuscle);

        // setup content values
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_NAME, newExerciseName);
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_MUSCLE, newExerciseMuscle);

        int rowsUpdated = mContext.getContentResolver().update(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                newValues,
                DatabaseContract.ExerciseEntry._ID + " = ? ",
                new String[] {Long.toString(id)});

        return null;
    }

    /**
     * returns exercise _ID from position of list item clicked
     */
    private long getExerciseIdWithNameMuscle(String name, String muscle) {
        String selection = DatabaseContract.ExerciseEntry.COLUMN_NAME + " = ? AND " +
                DatabaseContract.ExerciseEntry.COLUMN_MUSCLE + " = ?";

        String[] selectionArgs = new String[] {name, muscle};

        Cursor cursor = mContext.getContentResolver().query(DatabaseContract.ExerciseEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseEntry._ID},
                selection,
                selectionArgs,
                null);

        Long exerciseId = null;
        if (cursor.moveToFirst()) {
            exerciseId = cursor.getLong(0);
        }

        cursor.close();
        return exerciseId;
    }
}
