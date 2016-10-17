package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Michael on 10/17/2016.
 */

// Given exercise id, reads exercise table and gets the name
public class FetchExerciseTitleTask extends AsyncTask<Long, Void, Void> {
    private final Context mContext;

    public FetchExerciseTitleTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Long... longs) {
        return null;
    }
}
