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
 * Notifies UI thread to update edittext defaults
 */
public class FetchMostRecentWeightReps extends AsyncTask<Long, Void, String[]> {
    private ExerciseHistoryEntryFragment mFragment;

    public FetchMostRecentWeightReps(Fragment fragment) {
        mFragment = (ExerciseHistoryEntryFragment) fragment;
    }

    @Override
    protected String[] doInBackground(Long... longs) {
        long exerciseId = longs[0];

        // Find row in exerciseHistory where id = id
        // Sort by most recent date highest weight highest reps
        String orderBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC, " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC";
        Cursor cursor = mFragment.getActivity().getContentResolver().query(
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                new String[] {DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT
                        , DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS},
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ?",
                new String[] {Long.toString(exerciseId)},
                orderBy);

        // Prepare result for postExecute
        String[] result = null;
        if (cursor.moveToFirst()) {
            String weight = Long.toString(cursor.getInt(0));
            String reps = Long.toString(cursor.getInt(1));

            result = new String[] {weight, reps};
        }

        cursor.close();
        return result;
    }

    /**
     * Notify UI thread to set default weight and reps edittexts
     * @param result result from doInBackground
     */
    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        String weight = result[0];
        String reps = result[1];

        if (mFragment instanceof OnFinishFetchWeightRepsDefaultsListener) {
            ((OnFinishFetchWeightRepsDefaultsListener) mFragment)
                    .onFinishFetchWeightRepsDefaults(weight, reps);
        }
    }

    public interface OnFinishFetchWeightRepsDefaultsListener {
        void onFinishFetchWeightRepsDefaults(String weight, String reps);
    }
}
