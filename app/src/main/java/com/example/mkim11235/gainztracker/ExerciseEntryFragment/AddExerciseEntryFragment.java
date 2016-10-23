package com.example.mkim11235.gainztracker.ExerciseEntryFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mkim11235.gainztracker.MainActivity;
import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.tasks.AddExerciseTask;

/**
 * Created by Michael on 10/22/2016.
 */

public class AddExerciseEntryFragment extends ExerciseEntryFragment {

    /**
     * No extra arguments needed from bundle
     * @param bundle bundle to get args from
     */
    @Override
    protected void getExtraArguments(Bundle bundle) {}

    /**
     * Default is empty for creating new exercise
     */
    @Override
    protected void setEditTextDefaults() {}

    @Override
    protected void setExerciseEntryButtonText() {
        mExerciseEntryButton.setText(getString(R.string.button_add_exercise_entry_text));
    }

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
}
