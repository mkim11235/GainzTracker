package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/24/2016.
 */

public class AddExerciseTask extends DbTask<String> {

    public AddExerciseTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String name = strings[0];
        String muscle = strings[1];

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ExerciseEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.ExerciseEntry.COLUMN_MUSCLE, muscle);

        Uri insertedUri = mContentResolver.insert(
                DatabaseContract.ExerciseEntry.CONTENT_URI, values);

        long exerciseId = ContentUris.parseId(insertedUri);

        return null;
    }
}
