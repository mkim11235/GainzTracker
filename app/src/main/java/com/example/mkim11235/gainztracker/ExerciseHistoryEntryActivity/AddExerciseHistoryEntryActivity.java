package com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mkim11235.gainztracker.AddExerciseHistoryDBTask;
import com.example.mkim11235.gainztracker.ExerciseHistoryActivity;
import com.example.mkim11235.gainztracker.FetchMostRecentWeightRepsGivenExerciseIdTask;
import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;

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
    protected void getAndSetDefaultWeightAndReps(long exerciseId) {
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
                String weightString = getWeightEditTextString();
                String repsString = getRepsEditTextString();
                String dateString = getDateEditTextString();

                // Validation check. all must be entered
                if (allValidEntries(weightString, repsString, dateString)) {
                    // Try converting all to long so asynctask can take params
                    long weight = Integer.parseInt(weightString);
                    long reps = Integer.parseInt(repsString);
                    // Format date string into DB format
                    dateString = Utility.formatDateReadableToDB(dateString);
                    long date = Integer.parseInt(dateString);

                    // Add new exercise history entry to DB
                    new AddExerciseHistoryDBTask(AddExerciseHistoryEntryActivity.this)
                            .execute(exerciseId, weight, reps, date);

                    // Return to exercise activity
                    Intent intent = new Intent(v.getContext(), ExerciseHistoryActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, exerciseId);
                    startActivity(intent);
                }
            }
        };
    }
}
