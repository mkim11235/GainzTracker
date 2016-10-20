package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

/**
 * Fetches most recent weight/reps for given exerciseId from db
 * Sets corresponding edittext values from context to those value. "" if none
 */
public class FetchMostRecentWeightRepsGivenExerciseIdTask extends AsyncTask<Long, Void, Void> {
    private Context mContext;

    public FetchMostRecentWeightRepsGivenExerciseIdTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Long... longs) {
        long exerciseId = longs[0];

        // Find row in exerciseHistory where id = id
        // Want to sort by date to get most recent date
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT
                        , DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS},
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ?",
                new String[] {Long.toString(exerciseId)},
                null);

        String exerciseWeight = null;
        String exerciseReps = null;

        // MOst recent date is highest formatsortable date so sort desc
        // if multipel match, take highest weight and its corresponding reps
        if (cursor.moveToFirst()) {
            /*
            exerciseName = cursor.getString(0);
            ((ExerciseHistoryActivity)mContext).setTitle(exerciseName);
            */
        }

        cursor.close();
        return null;
    }
}
