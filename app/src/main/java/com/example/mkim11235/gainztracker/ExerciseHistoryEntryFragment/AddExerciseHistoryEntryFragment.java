package com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;
import com.example.mkim11235.gainztracker.tasks.AddExerciseHistoryTask;
import com.example.mkim11235.gainztracker.tasks.FetchMostRecentWeightReps;

/**
 * Created by Michael on 10/23/2016.
 */

public class AddExerciseHistoryEntryFragment extends ExerciseHistoryEntryFragment
        implements FetchMostRecentWeightReps.Callback {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        setEditTextDefaults(getArguments());

        mExerciseHistoryFinalButton.setText(getString(R.string.button_add_exercise_history_entry_text_final));
        setFinalButtonOnClickListener();

        return rootView;
    }

    /*
     * Set default values for edittexts weight, reps, date if not set already
     * Set fragment retainInstance true for asynctask result
     */
    private void setEditTextDefaults(Bundle args) {
        String weightString = args.getString(getString(R.string.EXTRA_EXERCISE_WEIGHT));
        String repsString = args.getString(getString(R.string.EXTRA_EXERCISE_REPS));

        if (weightString != null) {
            mWeightEditText.setText(weightString);
        }
        if (repsString != null) {
            mRepsEditText.setText(repsString);
        }

        if (mWeightEditText.getText().length() == 0) {
            setRetainInstance(true);
            mDateEditText.setText(Utility.getCurrentDate());
            new FetchMostRecentWeightReps(this).execute(mExerciseId);
        }
    }

    /*
     * Sets finalButton onclick to add entry to database if entry is valid
     * Navigates back
     */
    private void setFinalButtonOnClickListener() {
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

    /**
     * Sets the edittext texts to passed values.
     * Called when FetchMostRecentWeightReps finishes
     * @param weight weight to set edittext to
     * @param reps reps to set edittext to
     */
    @Override
    public void onFinishFetchWeightRepsDefaults(String weight, String reps) {
        mWeightEditText.setText(weight);
        mRepsEditText.setText(reps);
    }
}
