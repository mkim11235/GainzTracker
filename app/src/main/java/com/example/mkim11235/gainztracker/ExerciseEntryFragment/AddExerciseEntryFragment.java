package com.example.mkim11235.gainztracker.ExerciseEntryFragment;

import android.content.Intent;
import android.view.View;

import com.example.mkim11235.gainztracker.MainActivity;
import com.example.mkim11235.gainztracker.tasks.AddExerciseTask;

/**
 * Created by Michael on 10/22/2016.
 */

public class AddExerciseEntryFragment extends ExerciseEntryFragment {

    /**
     * Sets up ExerciseEntryButton to add exerciseEntry to DB
     */
    @Override
    protected void setExerciseEntryButtonOnClickListener() {
        mExerciseEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exerciseName = mExerciseName.getText().toString();
                String exerciseMuscle = mExerciseMuscle.getText().toString();

                // Validation check
                if (allValidEntries(exerciseName, exerciseMuscle)) {
                    AddExerciseTask dbTask = new AddExerciseTask(getActivity());
                    dbTask.execute(exerciseName, exerciseMuscle);

                    // Return to main activity
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /*
     * Checks if entered name and muscle are entered
     * Returns true if so, otherwise sets error and returns false
     */
    private boolean allValidEntries(String name, String muscle) {
        boolean allValid = true;
        if (name.length() == 0) {
            mExerciseName.setError("Please enter a valid name");
            allValid = false;
        }

        if (muscle.length() == 0) {
            mExerciseMuscle.setError("Please enter a valid muscle");
            allValid = false;
        }

        return allValid;
    }
}
