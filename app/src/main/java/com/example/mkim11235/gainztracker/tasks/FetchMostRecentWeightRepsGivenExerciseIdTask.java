package com.example.mkim11235.gainztracker.tasks;

import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.EditText;

import com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment.ExerciseHistoryEntryFragment;
import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

/**
 * Fetches most recent weight/reps for given exerciseId from db
 * Sets corresponding edittext values from context to those value. "" if none
 */
public class FetchMostRecentWeightRepsGivenExerciseIdTask extends AsyncTask<Long, Void, Void> {
    private ExerciseHistoryEntryFragment mFragment;

    public FetchMostRecentWeightRepsGivenExerciseIdTask(Fragment context) {
        mFragment = (ExerciseHistoryEntryFragment) context;
    }

    @Override
    protected Void doInBackground(Long... longs) {
        long exerciseId = longs[0];

        // Find row in exerciseHistory where id = id
        // Sort by most recent date highest weight
        String orderBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC, " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC";
        Cursor cursor = mFragment.getActivity().getContentResolver().query(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT
                        , DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS},
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ?",
                new String[] {Long.toString(exerciseId)},
                orderBy);

        // Set the edittext for weight and reps to first entry.
        if (cursor.moveToFirst()) {
            String exerciseWeight = Integer.toString(cursor.getInt(0));
            String exerciseReps = Integer.toString(cursor.getInt(1));

            EditText editTextWeight = (EditText) mFragment.getActivity().findViewById(
                    R.id.edittext_exercise_history_entry_weight);
            mFragment.setEditTextText(editTextWeight, exerciseWeight);

            EditText editTextReps = (EditText) mFragment.getActivity().findViewById(
                    R.id.edittext_exercise_history_entry_reps);
            mFragment.setEditTextText(editTextReps, exerciseReps);
        }

        cursor.close();
        return null;
    }
}
