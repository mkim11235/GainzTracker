package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.os.AsyncTask;

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

    @Override
    protected String doInBackground(Long... longs) {
        return null;
    }
}
