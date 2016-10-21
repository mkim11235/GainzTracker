package com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mkim11235.gainztracker.DatePickerFragment;
import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;

/**
 * Created by Michael on 10/17/2016.
 */

public abstract class ExerciseHistoryEntryActivity extends AppCompatActivity {
    private static final int DECREMENT_CHANGE = -1;
    private static final int INCREMENT_CHANGE = 1;

    protected long mExerciseId;
    protected String mExerciseName;

    protected Button mExerciseHistoryFinalButton;
    protected ImageButton mDecrementWeightButton;
    protected ImageButton mIncrementWeightButton;
    protected ImageButton mDecrementRepsButton;
    protected ImageButton mIncrementRepsButton;

    protected EditText mWeightEditText;
    protected EditText mRepsEditText;
    protected EditText mDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get extras from bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mExerciseId = extras.getLong(getString(R.string.EXTRA_EXERCISE_ID));
            mExerciseName = extras.getString(getString(R.string.EXTRA_EXERCISE_NAME));
            initExtraArguments(extras);
        }

        setTitle(mExerciseName + " Entry");

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
        setupChangeButtons(mDecrementWeightButton, mWeightEditText, DECREMENT_CHANGE);
        setupChangeButtons(mDecrementRepsButton, mRepsEditText, DECREMENT_CHANGE);
        setupChangeButtons(mIncrementWeightButton, mWeightEditText, INCREMENT_CHANGE);
        setupChangeButtons(mIncrementRepsButton, mRepsEditText, INCREMENT_CHANGE);

        // When button clicked, create new entry in exercise history table, return to main
        // Todo: add code to check whether all fields entered. cannot be null. or allow null values in table
        mExerciseHistoryFinalButton = (Button) findViewById(R.id.button_exercise_history_entry_final);
        setupFinalButtonText();
        setupFinalButtonOnClick();
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
     * Way for async task to change edittext text
     * @param editText = the edittext to modify
     * @param text = the new text to set edittext to
     */
    public void setEditTextText(EditText editText, String text) {
        editText.setText(text);
    }

    /**
     * Returns true if valid entry for all three edittexts
     * Sets edittext error if not valid
     *
     * @param weightString weight from edittext
     * @param repsString reps from edittext
     * @param dateString date from edittext
     * @return true if all length > 0, false otherwise
     */
    protected boolean allValidEntries(String weightString, String repsString, String dateString) {
        // Validation check. all must be entered
        boolean allValid = true;
        if (weightString.length() == 0) {
            mWeightEditText.setError("Please enter valid weight.");
            allValid = false;
        }
        if (repsString.length() == 0) {
            mRepsEditText.setError("Please enter valid reps.");
            allValid = false;
        }
        if (dateString.length() == 0) {
            mDateEditText.setError("Please enter valid date");
            allValid = false;
        }
        return allValid;
    }

    /**
     * Initialize any extra arguments from bundle
     * @param bundle bundle containinng args
     */
    protected abstract void initExtraArguments(Bundle bundle);

    /**
     * Set the default weight and rep texts to appropriate values
     */
    protected abstract void getAndSetDefaultWeightAndReps();

    /**
     * Setup final button text
     */
    protected abstract void setupFinalButtonText();

    /**
     * Setup the final button OnClick
     */
    protected abstract void setupFinalButtonOnClick();

    /**
     * Sets up button onclicklisteners for increment/decrement
     */
    private void setupChangeButtons(final ImageButton button, final EditText editText, final int change) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = editText.getText().toString();
                if (editTextString.length() > 0) {
                    int curWeight = Integer.parseInt(editTextString);
                    editText.setText(Integer.toString(curWeight + change));
                }
            }
        });
    }
}
