package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Michael on 10/17/2016.
 */

public class ExerciseAdapter extends CursorAdapter {
    public ExerciseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_exercise, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewName = (TextView) view.findViewById(R.id.textview_exercise_name);
        TextView textViewMuscle = (TextView) view.findViewById(R.id.textview_exercise_muscle);

        textViewName.setText(getNameFromCursor(cursor));
        textViewMuscle.setText(getMuscleFromCursor(cursor));
    }

    // TO DO
    // Dont use hardcoded values.
    private String getNameFromCursor(Cursor cursor) {
       return cursor.getString(1);
    }

    private String getMuscleFromCursor(Cursor cursor) {
        return cursor.getString(2);
    }
}
