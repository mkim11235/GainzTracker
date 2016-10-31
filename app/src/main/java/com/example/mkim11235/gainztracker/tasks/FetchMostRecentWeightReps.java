package com.example.mkim11235.gainztracker.tasks;

import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment.ExerciseHistoryEntryFragment;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/19/2016.
 */

/**
 * Fetches most recent weight/reps for given exerciseId from db
 * Callback to UI thread to update EditText defaults
 */
public class FetchMostRecentWeightReps extends AsyncTask<Long, Void, String[]> {

    private static final int INDEX_EXERCISE_ID = 0;
    private static final int INDEX_WEIGHT = 0;
    private static final int INDEX_REPS = 1;

    private ExerciseHistoryEntryFragment mFragment;

    public FetchMostRecentWeightReps(Fragment fragment) {
        mFragment = (ExerciseHistoryEntryFragment) fragment;
    }

    @Override
    protected String[] doInBackground(Long... longs) {
        long exerciseId = longs[INDEX_EXERCISE_ID];

        // Find row in exerciseHistory where id = id
        // Sort by most recent date highest weight highest reps
        String orderBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC, " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC";
        Cursor cursor = mFragment.getActivity().getContentResolver().query(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                new String[]{DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT
                        , DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS},
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ?",
                new String[]{Long.toString(exerciseId)},
                orderBy);

        // Prepare result for postExecute
        String[] result = null;
        if (cursor.moveToFirst()) {
            String weight = Long.toString(cursor.getInt(INDEX_WEIGHT));
            String reps = Long.toString(cursor.getInt(INDEX_REPS));

            result = new String[]{weight, reps};
        }

        cursor.close();
        return result;
    }

    /**
     * Notify UI thread to set default weight and reps edittexts
     * If no previous exercise history entries for default, do nothing
     *
     * @param result result from doInBackground
     */
    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        if (result != null) {
            String weight = result[INDEX_WEIGHT];
            String reps = result[INDEX_REPS];

            if (mFragment instanceof Callback) {
                ((Callback) mFragment).onFinishFetchWeightRepsDefaults(weight, reps);
            }
        }
    }

    public interface Callback {
        void onFinishFetchWeightRepsDefaults(String weight, String reps);
    }
}
