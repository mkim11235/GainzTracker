package com.example.mkim11235.gainztracker.ExerciseEntryFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.tasks.EditExerciseTask;

/**
 * Created by Michael on 10/23/2016.
 */

public class EditExerciseEntryFragment extends ExerciseEntryFragment {

    private String mExerciseId;
    private String mOldExerciseName;
    private String mOldExerciseMuscle;

    /**
     * Gets and sets old exercise name and weight
     * @param bundle bundle to get args from
     */
    @Override
    protected void setExtraMembersFromBundle(Bundle bundle) {
        mExerciseId = bundle.getString(getString(R.string.EXTRA_EXERCISE_ID));
        mOldExerciseName = bundle.getString(getString(R.string.EXTRA_EXERCISE_NAME));
        mOldExerciseMuscle = bundle.getString(getString(R.string.EXTRA_EXERCISE_MUSCLE));
    }

    /**
     * Sets default values to old values
     */
    @Override
    protected void setEditTextDefaults() {
        mExerciseName.setText(mOldExerciseName);
        mExerciseMuscle.setText(mOldExerciseMuscle);
    }

    /**
     * Sets exerciseEntryButtonText
     */
    @Override
    protected void setFinalButtonText() {
        mExerciseEntryButton.setText(getString(R.string.button_edit_exercise_entry_text));
    }

    /**
     * Sets exerciseEntryButton onclick to edit db with new values
     */
    @Override
    protected void setFinalButtonOnClickListener() {
        mExerciseEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exerciseName = mExerciseName.getText().toString();
                String exerciseMuscle = mExerciseMuscle.getText().toString();

                // Validation check
                if (allValidEntries(exerciseName, exerciseMuscle)) {
                    Activity activity = getActivity();
                    new EditExerciseTask(activity).execute(
                            mExerciseId, exerciseName, exerciseMuscle);

                    // Return to main activity
                    activity.finish();
                    activity.onBackPressed();
                }
            }
        });
    }
}
