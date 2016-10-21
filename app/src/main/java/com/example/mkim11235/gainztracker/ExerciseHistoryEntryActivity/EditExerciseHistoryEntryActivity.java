package com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mkim11235.gainztracker.ExerciseHistoryActivity;
import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.UpdateExerciseHistoryTask;
import com.example.mkim11235.gainztracker.Utility;

/**
 * Created by Michael on 10/20/2016.
 */

public class EditExerciseHistoryEntryActivity extends ExerciseHistoryEntryActivity {
    private long mOldExerciseWeight;
    private long mOldExerciseReps;
    private long mOldExerciseDate;

    /**
     * Initialize weight,reps,date from bundle
     * @param bundle bundle containing args
     */
    @Override
    protected void initExtraArguments(Bundle bundle) {
        mOldExerciseWeight = Long.parseLong(bundle.getString(getString(R.string.EXTRA_EXERCISE_WEIGHT)));
        mOldExerciseReps = Long.parseLong(bundle.getString(getString(R.string.EXTRA_EXERCISE_REPS)));
        mOldExerciseDate = Long.parseLong(bundle.getString(getString(R.string.EXTRA_EXERCISE_DATE)));
    }

    /**
     * Set default weight and reps to what they were before click edit
     */
    @Override
    protected void getAndSetDefaultWeightAndReps(long exerciseId) {
        mWeightEditText.setText(Long.toString(mOldExerciseWeight));
        mRepsEditText.setText(Long.toString(mOldExerciseReps));
        String formatDate = Utility.formatDateDBToReadable(Long.toString(mOldExerciseDate));
        mDateEditText.setText(formatDate);
    }

    /**
     * Gets final button text string
     * @return final button text string
     */
    @Override
    protected String getFinalButtonText() {
        return getString(R.string.button_edit_exercise_history_entry_text_final);
    }

    /**
     * Gets final button onclicklistener
     * @param exerciseId exercise id for the history entry
     * @return final button onclicklistener
     */
    @Override
    protected View.OnClickListener getFinalButtonOnClickListener(final long exerciseId) {
        return new View.OnClickListener() {
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
                    new UpdateExerciseHistoryTask(EditExerciseHistoryEntryActivity.this)
                            .execute(exerciseId, weight, reps, date, mOldExerciseWeight,
                                    mOldExerciseReps, mOldExerciseDate);

                    // Return to exercise activity
                    Intent intent = new Intent(v.getContext(), ExerciseHistoryActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, exerciseId);
                    startActivity(intent);
                }
            }
        };
    }
}
