package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.mkim11235.gainztracker.ExerciseFragment.COL_EXERCISE_MUSCLE;
import static com.example.mkim11235.gainztracker.ExerciseFragment.COL_EXERCISE_NAME;

/**
 * Created by Michael on 10/17/2016!
 */

public class ExerciseAdapter extends CursorAdapter {

    public ExerciseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_exercise, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewName = (TextView) view.findViewById(R.id.textview_exercise_name);
        TextView textViewMuscle = (TextView) view.findViewById(R.id.textview_exercise_muscle);

        textViewName.setText(getNameFromCursor(cursor));
        textViewMuscle.setText(getMuscleFromCursor(cursor));
    }

    private String getNameFromCursor(Cursor cursor) {
        return cursor.getString(COL_EXERCISE_NAME);
    }

    private String getMuscleFromCursor(Cursor cursor) {
        return cursor.getString(COL_EXERCISE_MUSCLE);
    }
}
