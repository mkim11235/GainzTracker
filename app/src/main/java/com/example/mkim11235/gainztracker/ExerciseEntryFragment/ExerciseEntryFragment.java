package com.example.mkim11235.gainztracker.ExerciseEntryFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mkim11235.gainztracker.R;

/**
 * Created by Michael on 10/22/2016.
 */

public abstract class ExerciseEntryFragment extends Fragment {
    protected Button mExerciseEntryButton;
    protected EditText mExerciseName;
    protected EditText mExerciseMuscle;

    //Todo: if implement options menu, uncomment line below
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_entry, container, false);

        // Initialize member variables
        mExerciseName = (EditText) rootView.findViewById(R.id.edittext_exercise_name);
        mExerciseMuscle = (EditText) rootView.findViewById(R.id.edittext_exercise_muscle);
        mExerciseEntryButton = (Button) rootView.findViewById(R.id.button_exercise_entry);

        setExerciseEntryButtonOnClickListener();

        return rootView;
    }

    /**
     * Sets ExerciseEntryButton onclicklistener
     */
    protected abstract void setExerciseEntryButtonOnClickListener();
}
