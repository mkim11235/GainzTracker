package com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment;

import android.os.Bundle;
import android.view.View;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;
import com.example.mkim11235.gainztracker.tasks.UpdateExerciseHistoryTask;

/**
 * Created by Michael on 10/23/2016.
 */
public class EditExerciseHistoryEntryFragment extends ExerciseHistoryEntryFragment {
    private long mOldExerciseWeight;
    private long mOldExerciseReps;
    private long mOldExerciseDate;

    @Override
    protected void setExtraMembersFromBundle(Bundle bundle) {
        mOldExerciseWeight = Long.parseLong(bundle.getString(getString(R.string.EXTRA_EXERCISE_WEIGHT)));
        mOldExerciseReps = Long.parseLong(bundle.getString(getString(R.string.EXTRA_EXERCISE_REPS)));
        mOldExerciseDate = Long.parseLong(bundle.getString(getString(R.string.EXTRA_EXERCISE_DATE)));
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
                String weightString = mWeightEditText.getText().toString();;
                String repsString = mRepsEditText.getText().toString();;
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
                    new UpdateExerciseHistoryTask(getActivity())
                            .execute(mExerciseId, weight, reps, date, mOldExerciseWeight,
                                    mOldExerciseReps, mOldExerciseDate);

                    // Return to exercise activity
                    // Todo: I want to return to MainActivity with ExerciseHistoryFragment
                    getActivity().onBackPressed();
                    /*
                    Intent intent = new Intent(v.getContext(), ExerciseActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, exerciseId);
                    startActivity(intent);
                    */
                }
            }
        });
    }
}
