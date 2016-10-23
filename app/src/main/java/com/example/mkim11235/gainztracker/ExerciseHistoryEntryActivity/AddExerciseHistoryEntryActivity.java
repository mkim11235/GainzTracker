package com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;
import com.example.mkim11235.gainztracker.tasks.AddExerciseHistoryTask;
import com.example.mkim11235.gainztracker.tasks.FetchMostRecentWeightRepsGivenExerciseIdTask;

/**
 * Created by Michael on 10/17/2016.
 */

public class AddExerciseHistoryEntryActivity extends ExerciseHistoryEntryActivity {
    /**
     * Initialize members with any extra args from bundle
     * @param bundle bundle containing args
     */
    @Override
    protected void initExtraArguments(Bundle bundle) {}

    /**
     * Set the default weight and rep texts to appropriate values
     * @param exerciseId id of the exercise this history entry refers to
     */
    @Override
    protected void getAndSetDefaultWeightRepsDate(long exerciseId) {
        mDateEditText.setText(Utility.getCurrentDate());
        new FetchMostRecentWeightRepsGivenExerciseIdTask(this).execute(exerciseId);
    }

    /**
     * Gets final button text string
     * @return final button text string
     */
    @Override
    protected String getFinalButtonText() {
        return getString(R.string.button_add_exercise_history_entry_text_final);
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

                    // Add new exercise history entry to DB
                    new AddExerciseHistoryTask(AddExerciseHistoryEntryActivity.this)
                            .execute(exerciseId, weight, reps, date);

                    // Return to exercise activity
                    // Todo: I want to return to MainActivity with ExerciseHistoryFragment
                    Toast.makeText(AddExerciseHistoryEntryActivity.this, "Not yet implemented", Toast.LENGTH_LONG);
                    /*
                    Intent intent = new Intent(v.getContext(), ExerciseActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, exerciseId);
                    startActivity(intent);
                    */
                }
            }
        };
    }
}
