package com.example.mkim11235.gainztracker.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Adapter;

import com.example.mkim11235.gainztracker.ExerciseAdapter;
import com.example.mkim11235.gainztracker.MainActivity;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

/**
 * Created by Michael on 10/20/2016.
 */

/**
 * Updates the db with new name and muscle
 */
public class UpdateExerciseTask extends AsyncTask<String, Void, Void> {
    private MainActivity mContext;
    private ExerciseAdapter mAdapter;

    public UpdateExerciseTask(Context context, Adapter adapter) {
        mContext = (MainActivity) context;
        mAdapter = (ExerciseAdapter) adapter;
    }

    /**
     * First finds the _ID of the selected item.
     * Updates the exercise table entry with that ID with new values
     */
    @Override
    protected Void doInBackground(String... strings) {
        String newExerciseName = strings[0];
        String newExerciseMuscle = strings[1];
        String selectedItemPosition = strings[2];

        // I need to get id cuz unique
        // use id for selection & args
        long id = getExerciseIdFromPosition(Integer.parseInt(selectedItemPosition));

        // setup content values
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_NAME, newExerciseName);
        newValues.put(DatabaseContract.ExerciseEntry.COLUMN_MUSCLE, newExerciseMuscle);

        int rowsUpdated = mContext.getContentResolver().update(
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                newValues,
                DatabaseContract.ExerciseEntry._ID + " = ? ",
                new String[] {Long.toString(id)});

        return null;
    }

    /**
     * returns exercise _ID from position of list item clicked
     */
    private long getExerciseIdFromPosition(int position) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(0);
    }
}
