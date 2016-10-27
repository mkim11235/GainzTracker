package com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;
import com.example.mkim11235.gainztracker.tasks.AddExerciseHistoryTask;
import com.example.mkim11235.gainztracker.tasks.FetchMostRecentWeightReps;

/**
 * Created by Michael on 10/23/2016.
 */

public class AddExerciseHistoryEntryFragment extends ExerciseHistoryEntryFragment
        implements FetchMostRecentWeightReps.Callback {

    /**
     * Initialize extra members from bundle
     * @param bundle bundle to get args from
     */
    @Override
    protected void setExtraMembersFromBundle(Bundle bundle) {}

    /**
     * Set default values for edittexts weight, reps, date if not set already
     * Set fragment retainInstance true for asynctask result
     */
    @Override
    protected void setEditTextDefaults() {
        if (mWeightEditText.getText().length() == 0) {
            setRetainInstance(true);
            mDateEditText.setText(Utility.getCurrentDate());
            new FetchMostRecentWeightReps(this).execute(mExerciseId);
        }
    }

    /**
     * Set final button text to "Add Exercise Entry"
     */
    @Override
    protected void setFinalButtonText() {
        mExerciseHistoryFinalButton.setText(getString(R.string.button_add_exercise_history_entry_text_final));
    }

    @Override
    protected void setFinalButtonOnClickListener() {
        mExerciseHistoryFinalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightString = mWeightEditText.getText().toString();
                String repsString = mRepsEditText.getText().toString();
                String dateString = mDateEditText.getText().toString();

                // Validation check. all must be entered
                if (allValidEntries(weightString, repsString, dateString)) {
                    // Convert all to longs for asynctask
                    long weight = Long.parseLong(weightString);
                    long reps = Long.parseLong(repsString);
                    // Format date string into DB format
                    dateString = Utility.formatDateReadableToDB(dateString);
                    long date = Long.parseLong(dateString);

                    Activity activity = getActivity();

                    // Add new exercise history entry to DB
                    new AddExerciseHistoryTask(activity).execute(mExerciseId, weight, reps, date);

                    // Return to MainActivity w/ EHEFragment
                    activity.finish();
                    activity.onBackPressed();
                }
            }
        });
    }

    @Override
    public void onFinishFetchWeightRepsDefaults(String weight, String reps) {
        mWeightEditText.setText(weight);
        mRepsEditText.setText(reps);
    }
}
