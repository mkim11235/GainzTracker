package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Michael on 10/17/2016.
 */

// Adds new entry (weight, reps, date) into exercise history table
public class AddExerciseHistoryDBTask extends AsyncTask<String, Void, Void> {
    private final Context mContext;

    public AddExerciseHistoryDBTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        return null;
    }
}
