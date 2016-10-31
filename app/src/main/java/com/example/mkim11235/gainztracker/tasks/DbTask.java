package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Michael on 10/24/2016.
 */

/**
 * Abstract base class for DB access (read/write)
 *
 * @param <T> parameter type for AsyncTask
 */
public abstract class DbTask<T> extends AsyncTask<T, Void, Void> {

    protected ContentResolver mContentResolver;

    public DbTask(Context context) {
        this.mContentResolver = context.getContentResolver();
    }
}
