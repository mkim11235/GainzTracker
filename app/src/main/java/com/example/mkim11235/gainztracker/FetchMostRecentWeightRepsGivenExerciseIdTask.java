package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.EditText;

import com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity.AddExerciseHistoryEntryActivity;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

/**
 * Fetches most recent weight/reps for given exerciseId from db
 * Sets corresponding edittext values from context to those value. "" if none
 */
public class FetchMostRecentWeightRepsGivenExerciseIdTask extends AsyncTask<Long, Void, Void> {
    private AddExerciseHistoryEntryActivity mContext;

    public FetchMostRecentWeightRepsGivenExerciseIdTask(Context context) {
        mContext = (AddExerciseHistoryEntryActivity) context;
    }

    @Override
    protected Void doInBackground(Long... longs) {
        long exerciseId = longs[0];

        // Find row in exerciseHistory where id = id
        // Sort by most recent date highest weight
        String orderBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC, " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC";
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT
                        , DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS},
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ?",
                new String[] {Long.toString(exerciseId)},
                orderBy);

        // Set the edittext for weight and reps to first entry.
        if (cursor.moveToFirst()) {
            String exerciseWeight = Integer.toString(cursor.getInt(0));
            EditText editTextWeight = (EditText) mContext.findViewById(
                    R.id.edittext_exercise_history_entry_weight);
            mContext.setEditTextText(editTextWeight, exerciseWeight);

            String exerciseReps = Integer.toString(cursor.getInt(1));
            EditText editTextReps = (EditText) mContext.findViewById(
                    R.id.edittext_exercise_history_entry_reps);
            mContext.setEditTextText(editTextReps, exerciseReps);
        }

        cursor.close();
        return null;
    }
}
