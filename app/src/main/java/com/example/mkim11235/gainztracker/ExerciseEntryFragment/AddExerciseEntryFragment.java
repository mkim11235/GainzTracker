package com.example.mkim11235.gainztracker.ExerciseEntryFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.tasks.AddExerciseTask;

/**
 * Created by Michael on 10/22/2016!
 */

public class AddExerciseEntryFragment extends ExerciseEntryFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mExerciseEntryButton.setText(getString(R.string.button_add_exercise_entry_text));
        setFinalButtonOnClickListener();

        return rootView;
    }

     // Sets up ExerciseEntryButton onClick to add exerciseEntry to DB
    private void setFinalButtonOnClickListener() {
        mExerciseEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exerciseName = mExerciseName.getText().toString();
                String exerciseMuscle = mExerciseMuscle.getText().toString();

                // Validation check
                if (allValidEntries(exerciseName, exerciseMuscle)) {
                    Activity activity = getActivity();
                    new AddExerciseTask(activity).execute(exerciseName, exerciseMuscle);

                    // Return to main activity
                    activity.finish();
                    activity.onBackPressed();
                }
            }
        });
    }
}
