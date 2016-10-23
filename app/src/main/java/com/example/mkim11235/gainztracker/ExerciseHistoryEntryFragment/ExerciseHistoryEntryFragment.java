package com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mkim11235.gainztracker.ContinuousLongClickListener;
import com.example.mkim11235.gainztracker.DatePickerFragment;
import com.example.mkim11235.gainztracker.ExerciseEntryBaseFragment;
import com.example.mkim11235.gainztracker.R;
import com.example.mkim11235.gainztracker.Utility;

/**
 * Created by Michael on 10/23/2016.
 */

public abstract class ExerciseHistoryEntryFragment extends ExerciseEntryBaseFragment {
    private static final int DECREMENT_CHANGE = -1;
    private static final int INCREMENT_CHANGE = 1;

    private static final int DECREMENT_CHANGE_LONG = -5;
    private static final int INCREMENT_CHANGE_LONG = 5;

    private Handler mIncrementHandler;

    private ImageButton mDecrementWeightButton;
    private ImageButton mIncrementWeightButton;
    private ImageButton mDecrementRepsButton;
    private ImageButton mIncrementRepsButton;

    // Decided make these protected cuz subclasses often need this
    protected long mExerciseId;
    protected String mExerciseName;

    protected Button mExerciseHistoryFinalButton;
    protected EditText mWeightEditText;
    protected EditText mRepsEditText;
    protected EditText mDateEditText;

    //Todo: if implement options menu, uncomment line below
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_history_entry, container, false);

        // Initialize member variables
        Bundle args = getArguments();
        if (args != null) {
            mExerciseId = args.getLong(getString(R.string.EXTRA_EXERCISE_ID));
            mExerciseName = args.getString(getString(R.string.EXTRA_EXERCISE_NAME));
            setExtraMembersFromBundle(args);
        }

        mExerciseHistoryFinalButton = (Button)
                rootView.findViewById(R.id.button_exercise_history_entry_final);
        mDecrementWeightButton = (ImageButton)
                rootView.findViewById(R.id.image_button_exercise_history_decrement_weight);
        mIncrementWeightButton = (ImageButton)
                rootView.findViewById(R.id.image_button_exercise_history_increment_weight);
        mDecrementRepsButton = (ImageButton)
                rootView.findViewById(R.id.image_button_exercise_history_decrement_reps);
        mIncrementRepsButton = (ImageButton)
                rootView.findViewById(R.id.image_button_exercise_history_increment_reps);

        mWeightEditText = (EditText) rootView.findViewById(R.id.edittext_exercise_history_entry_weight);
        mRepsEditText = (EditText) rootView.findViewById(R.id.edittext_exercise_history_entry_reps);
        mDateEditText = (EditText) rootView.findViewById(R.id.edittext_exercise_history_entry_date);
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        mIncrementHandler = new Handler();

        // Decrement/Increment button setup
        mDecrementWeightButton.setOnClickListener(
                getChangeButtonOnClickListener(mWeightEditText, DECREMENT_CHANGE));
        mDecrementRepsButton.setOnClickListener(
                getChangeButtonOnClickListener(mRepsEditText, DECREMENT_CHANGE));
        mIncrementWeightButton.setOnClickListener(
                getChangeButtonOnClickListener(mWeightEditText, INCREMENT_CHANGE));
        mIncrementRepsButton.setOnClickListener(
                getChangeButtonOnClickListener(mRepsEditText, INCREMENT_CHANGE));

        // Setup Dec/Increment button OnLongClick
        new ContinuousLongClickListener(mDecrementWeightButton, mIncrementHandler,
                getChangeButtonOnLongClickListener(mWeightEditText, DECREMENT_CHANGE_LONG));
        new ContinuousLongClickListener(mIncrementWeightButton, mIncrementHandler,
                getChangeButtonOnLongClickListener(mWeightEditText, INCREMENT_CHANGE_LONG));
        new ContinuousLongClickListener(mDecrementRepsButton, mIncrementHandler,
                getChangeButtonOnLongClickListener(mRepsEditText, DECREMENT_CHANGE));
        new ContinuousLongClickListener(mIncrementRepsButton, mIncrementHandler,
                getChangeButtonOnLongClickListener(mRepsEditText, INCREMENT_CHANGE));

        setEditTextDefaults();
        setFinalButtonText();
        setFinalButtonOnClickListener();

        return rootView;
    }

    /**
     * Shows the datepickerdialog. Called from layout fragment_exercise_history_entry onClick
     * @param v view from which called
     */
    public void showDatePickerDialog(View v) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.setArguments(buildDatePickerArgsBundle());
        fragment.show(getFragmentManager(), getString(R.string.FRAGMENT_TAG_DATE_PICKER));
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
     * gets button onclicklisteners for increment/decrement
     */
    private View.OnClickListener getChangeButtonOnClickListener(final EditText editText, final int change) {
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
     * Gets up button onlongclick listener for inc/dec
     * @param editText edittext to modify
     * @param change change amount
     * @return OnClickListenerObject
     */
    private View.OnLongClickListener getChangeButtonOnLongClickListener(final EditText editText, final int change) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int curValue = Integer.parseInt(editText.getText().toString());
                editText.setText(Integer.toString(curValue + change));
                return false;
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