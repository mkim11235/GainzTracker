package com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;
import com.example.mkim11235.gainztracker.tasks.EditExerciseHistoryTask;

/**
 * Created by Michael on 10/23/2016.
 */
public class EditExerciseHistoryEntryFragment extends ExerciseHistoryEntryFragment {

    private long mOldExerciseWeight;
    private long mOldExerciseReps;
    private long mOldExerciseDate;
    private long mExerciseHistoryId;

    @Override
    protected void setExtraMembersFromBundle(Bundle bundle) {
        mOldExerciseWeight = bundle.getLong(getString(R.string.EXTRA_EXERCISE_WEIGHT));
        mOldExerciseReps = bundle.getLong(getString(R.string.EXTRA_EXERCISE_REPS));
        mOldExerciseDate = bundle.getLong(getString(R.string.EXTRA_EXERCISE_DATE));
        mExerciseHistoryId = bundle.getLong(getString(R.string.EXTRA_EXERCISE_HISTORY_ID));
    }

    @Override
    protected void setEditTextDefaults() {
        mWeightEditText.setText(Long.toString(mOldExerciseWeight));
        mRepsEditText.setText(Long.toString(mOldExerciseReps));
        String formatDate = Utility.formatDateDBToReadable(Long.toString(mOldExerciseDate));
        mDateEditText.setText(formatDate);
    }

    @Override
    protected void setFinalButtonText() {
        mExerciseHistoryFinalButton.setText(
                getString(R.string.button_edit_exercise_history_entry_text_final));
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
                    // Try converting all to long so asynctask can take params
                    long weight = Integer.parseInt(weightString);
                    long reps = Integer.parseInt(repsString);
                    // Format date string into DB format
                    dateString = Utility.formatDateReadableToDB(dateString);
                    long date = Integer.parseInt(dateString);

                    // Update exercise history entry in DB
                    Activity activity = getActivity();
                    new EditExerciseHistoryTask(activity)
                            .execute(mExerciseId, weight, reps, date, mExerciseHistoryId);

                    // Return to MainActivity w/ EHEFrag
                    activity.finish();
                    activity.onBackPressed();
                }
            }
        });
    }
}
