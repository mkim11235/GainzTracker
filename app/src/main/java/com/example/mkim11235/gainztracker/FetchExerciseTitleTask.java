package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/17/2016.
 */

// Given exercise id, reads exercise table and gets the name
// Try to set exerciseActivity title to name + "History"
public class FetchExerciseTitleTask extends AsyncTask<Long, Void, String> {
    private final Context mContext;

    public FetchExerciseTitleTask(Context context) {
        mContext = context;
    }

    // Gets the name of exercise with given exerciseId
    // Sets the title to exerciseName
    // Returns the name
    @Override
    protected String doInBackground(Long... longs) {
        long exerciseId = longs[0];

        // Find row in exercises where id = id
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseEntry.COLUMN_NAME},
                DatabaseContract.ExerciseEntry._ID + " = ?",
                new String[] {Long.toString(exerciseId)},
                null);

        String exerciseName = null;
        if (cursor.moveToFirst()) {
            // set this to 1, see what happens@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            exerciseName = cursor.getString(0);
            ((ExerciseHistoryActivity)mContext).setTitle(exerciseName);
        }

        cursor.close();
        return exerciseName;
    }

}
