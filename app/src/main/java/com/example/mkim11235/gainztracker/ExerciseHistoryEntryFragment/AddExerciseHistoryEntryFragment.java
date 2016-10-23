package com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;
import com.example.mkim11235.gainztracker.tasks.AddExerciseHistoryTask;
import com.example.mkim11235.gainztracker.tasks.FetchMostRecentWeightRepsGivenExerciseIdTask;

/**
 * Created by Michael on 10/23/2016.
 */

public class AddExerciseHistoryEntryFragment extends ExerciseHistoryEntryFragment {
    /**
     * Initialize extra members from bundle
     * @param bundle bundle to get args from
     */
    @Override
    protected void setExtraMembersFromBundle(Bundle bundle) {}

    /**
     * Set default values for edittexts weight, reps, date
     */
    @Override
    protected void setEditTextDefaults() {
        mDateEditText.setText(Utility.getCurrentDate());
        new FetchMostRecentWeightRepsGivenExerciseIdTask(this).execute(mExerciseId);
    }

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
                    // Try converting all to long so asynctask can take params
                    long weight = Integer.parseInt(weightString);
                    long reps = Integer.parseInt(repsString);
                    // Format date string into DB format
                    dateString = Utility.formatDateReadableToDB(dateString);
                    long date = Integer.parseInt(dateString);

                    // Add new exercise history entry to DB
                    new AddExerciseHistoryTask(getActivity())
                            .execute(mExerciseId, weight, reps, date);

                    // Return to exercise activity
                    // Todo: I want to return to MainActivity with ExerciseHistoryFragment
                    Toast.makeText(getActivity(), "Not yet implemented", Toast.LENGTH_LONG);
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
