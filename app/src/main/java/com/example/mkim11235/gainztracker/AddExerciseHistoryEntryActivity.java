package com.example.mkim11235.gainztracker;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Michael on 10/17/2016.
 */

public class AddExerciseHistoryEntryActivity extends AppCompatActivity {
    // Is there a point for these ot be member variables?
    // mb put inside on create
    private long mExerciseId;
    private String mExerciseName;

    Button mAddExerciseHistoryEntryButton;
    ImageButton mDecrementWeightButton;
    ImageButton mIncrementWeightButton;
    ImageButton mDecrementRepsButton;
    ImageButton mIncrementRepsButton;

    EditText mWeightEditText;
    EditText mRepsEditText;
    EditText mDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_history_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get extras from bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mExerciseId = extras.getLong(getString(R.string.EXTRA_EXERCISE_ID));
            mExerciseName = extras.getString(getString(R.string.EXTRA_EXERCISE_NAME));
        }

        setTitle("Add " + mExerciseName + " Entry");

        // initialize member variables
        mDecrementWeightButton = (ImageButton) findViewById(R.id.image_button_exercise_history_decrement_weight);
        mIncrementWeightButton = (ImageButton) findViewById(R.id.image_button_exercise_history_increment_weight);
        mDecrementRepsButton = (ImageButton) findViewById(R.id.image_button_exercise_history_decrement_reps);
        mIncrementRepsButton = (ImageButton) findViewById(R.id.image_button_exercise_history_increment_reps);

        mWeightEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_weight);
        mRepsEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_reps);
        mDateEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_date);

        // Set weight/reps default to most recent weight/reps. empty if none.
        getAndSetDefaultWeightAndReps();

        // Set date default to current date
        mDateEditText.setText(Utility.getCurrentDate());

        // Decrement/Increment button setup
        mDecrementWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = mWeightEditText.getText().toString();
                if (editTextString.length() > 0) {
                    int curWeight = Integer.parseInt(editTextString);
                    mWeightEditText.setText(Integer.toString(curWeight - 1));
                }
            }
        });

        mIncrementWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = mWeightEditText.getText().toString();
                if (editTextString.length() > 0) {
                    int curWeight = Integer.parseInt(editTextString);
                    mWeightEditText.setText(Integer.toString(curWeight + 1));
                }
            }
        });

        mDecrementRepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = mRepsEditText.getText().toString();
                if (editTextString.length() > 0) {
                    int curReps = Integer.parseInt(editTextString);
                    mRepsEditText.setText(Integer.toString(curReps - 1));
                }
            }
        });

        mIncrementRepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = mRepsEditText.getText().toString();
                if (editTextString.length() > 0) {
                    int curReps = Integer.parseInt(mRepsEditText.getText().toString());
                    mRepsEditText.setText(Integer.toString(curReps + 1));
                }
            }
        });

        // When button clicked, create new entry in exercise history table, return to main
        // Todo: add code to check whether all fields entered. cannot be null. or allow null values in table
        mAddExerciseHistoryEntryButton = (Button) findViewById(R.id.button_add_exercise_history_entry_final);
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightString = String.valueOf(mWeightEditText.getText());
                String repsString = String.valueOf(mRepsEditText.getText());
                String dateString = String.valueOf(mDateEditText.getText());

                // Validation check. all must be entered
                boolean allValidEntries = true;
                if (weightString.length() == 0) {
                    mWeightEditText.setError("Please enter valid weight.");
                    allValidEntries = false;
                }
                if (repsString.length() == 0) {
                    mRepsEditText.setError("Please enter valid reps.");
                    allValidEntries = false;
                }
                if (dateString.length() == 0) {
                    mDateEditText.setError("Please enter valid date");
                    allValidEntries = false;
                }

                if (allValidEntries) {
                    // Try converting all to long so asynctask can take params
                    long weight = Integer.parseInt(weightString);
                    long reps = Integer.parseInt(repsString);
                    // Format date string into DB format
                    dateString = Utility.formatDateReadableToDB(dateString);
                    long date = Integer.parseInt(dateString);

                    // Add new exercise history entry to DB
                    AddExerciseHistoryDBTask dbTask = new AddExerciseHistoryDBTask(AddExerciseHistoryEntryActivity.this);
                    dbTask.execute(mExerciseId, weight, reps, date);

                    // Return to exercise activity
                    Intent intent = new Intent(v.getContext(), ExerciseHistoryActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, mExerciseId);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Way for async task to edit text text
     * @param editText = the edittext to modify
     * @param text = the new text to set edittext to
     */
    public void setEditTextText(EditText editText, String text) {
        editText.setText(text);
    }

    private void getAndSetDefaultWeightAndReps() {
        FetchMostRecentWeightRepsGivenExerciseIdTask task = new FetchMostRecentWeightRepsGivenExerciseIdTask(this);
        task.execute(mExerciseId);
    }
}
