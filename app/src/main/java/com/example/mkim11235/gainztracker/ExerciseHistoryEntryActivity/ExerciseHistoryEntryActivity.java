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

import com.example.mkim11235.gainztracker.ContinuousLongClickListener;
import com.example.mkim11235.gainztracker.DatePickerFragment;
import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;

/**
 * Created by Michael on 10/17/2016.
 */

public abstract class ExerciseHistoryEntryActivity extends AppCompatActivity {
    private static final int DECREMENT_CHANGE = -1;
    private static final int INCREMENT_CHANGE = 1;

    private long mExerciseId;
    private String mExerciseName;

    private Button mExerciseHistoryFinalButton;
    private ImageButton mDecrementWeightButton;
    private ImageButton mIncrementWeightButton;
    private ImageButton mDecrementRepsButton;
    private ImageButton mIncrementRepsButton;

    // Decided make these protected cuz subclasses often need this
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
        mDecrementWeightButton = (ImageButton)
                findViewById(R.id.image_button_exercise_history_decrement_weight);
        mIncrementWeightButton = (ImageButton)
                findViewById(R.id.image_button_exercise_history_increment_weight);
        mDecrementRepsButton = (ImageButton)
                findViewById(R.id.image_button_exercise_history_decrement_reps);
        mIncrementRepsButton = (ImageButton)
                findViewById(R.id.image_button_exercise_history_increment_reps);

        mWeightEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_weight);
        mRepsEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_reps);
        mDateEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_date);

        // Set weight/reps default to most recent weight/reps. empty if none.
        getAndSetDefaultWeightRepsDate(mExerciseId);

        // Decrement/Increment button setup
        mDecrementWeightButton.setOnClickListener(
                setChangeButtonOnClickListener(mWeightEditText, DECREMENT_CHANGE));
        mDecrementRepsButton.setOnClickListener(
                setChangeButtonOnClickListener(mRepsEditText, DECREMENT_CHANGE));
        mIncrementWeightButton.setOnClickListener(
                setChangeButtonOnClickListener(mWeightEditText, INCREMENT_CHANGE));
        mIncrementRepsButton.setOnClickListener(
                setChangeButtonOnClickListener(mRepsEditText, INCREMENT_CHANGE));

        // Trying to setup  onLongClick with CLCL class
        ContinuousLongClickListener c = new ContinuousLongClickListener(mIncrementWeightButton, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int curWeight = Integer.parseInt(mWeightEditText.getText().toString());
                mWeightEditText.setText(Integer.toString(curWeight + 5));
                return false;
            }
        });

        // When button clicked, create new entry in exercise history table, return to main
        mExerciseHistoryFinalButton = (Button) findViewById(R.id.button_exercise_history_entry_final);
        mExerciseHistoryFinalButton.setText(getFinalButtonText());
        mExerciseHistoryFinalButton.setOnClickListener(getFinalButtonOnClickListener(mExerciseId));
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
        fragment.setArguments(buildDatePickerArgsBundle());
        fragment.show(getFragmentManager(), getString(R.string.fragment_date_picker_tag));
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
     * @param exerciseId the id of exercise that history entry refers to
     */
    protected abstract void getAndSetDefaultWeightRepsDate(long exerciseId);

    /**
     * Gets the final button's text
     * @return the value to set final button text
     */
    protected abstract String getFinalButtonText();

    /**
     * Gets the final button's onclicklistener
     * @param exerciseId exercise id for the history entry
     * @return final button onclicklistener
     */
    protected abstract View.OnClickListener getFinalButtonOnClickListener(long exerciseId);

    /**
     * Sets up button onclicklisteners for increment/decrement
     */
    private View.OnClickListener setChangeButtonOnClickListener(final EditText editText, final int change) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = editText.getText().toString();
                if (editTextString.length() > 0) {
                    int curWeight = Integer.parseInt(editTextString);
                    editText.setText(Integer.toString(curWeight + change));
                }
            }
        };
    }

    /**
     * Builds bundle with year, month, day arguments
     * @return bundle containing args year, month, day
     */
    private Bundle buildDatePickerArgsBundle() {
        String selectedDate = mDateEditText.getText().toString();
        int year = Utility.getYearFromReadableDate(selectedDate);
        int month = Utility.getMonthFromReadableDate(selectedDate);
        int day = Utility.getDayFromReadableDate(selectedDate);

        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.EXTRA_YEAR), year);
        bundle.putInt(getString(R.string.EXTRA_MONTH), month);
        bundle.putInt(getString(R.string.EXTRA_DAY), day);
        return bundle;
    }
}
