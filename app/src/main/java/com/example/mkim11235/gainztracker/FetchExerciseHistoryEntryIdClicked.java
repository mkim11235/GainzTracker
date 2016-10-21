package com.example.mkim11235.gainztracker;

import android.os.AsyncTask;

/**
 * Created by Michael on 10/20/2016.
 */

public class FetchExerciseHistoryEntryIdClicked extends AsyncTask {
    private ExerciseHistoryActivity mContext;
    private ExerciseHistoryAdapter mAdapter;

    public FetchExerciseHistoryEntryIdClicked(ExerciseHistoryActivity context, ExerciseHistoryAdapter adapter) {
        this.mContext = mContext;
        this.mAdapter = adapter;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
