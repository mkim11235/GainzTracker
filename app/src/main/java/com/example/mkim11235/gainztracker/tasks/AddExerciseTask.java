package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.media.browse.MediaBrowser;
import android.net.Uri;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/24/2016.
 */

public class AddExerciseTask extends DbTask<String> {

    private static final int INDEX_NAME = 0;
    private static final int INDEX_MUSCLE = 1;

    public AddExerciseTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String name = strings[INDEX_NAME];
        String muscle = strings[INDEX_MUSCLE];

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ExerciseEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.ExerciseEntry.COLUMN_MUSCLE, muscle);

        Uri insertedUri = mContentResolver.insert(
                DatabaseContract.ExerciseEntry.CONTENT_URI, values);

        ContentUris.parseId(insertedUri);

        return null;
    }
}
