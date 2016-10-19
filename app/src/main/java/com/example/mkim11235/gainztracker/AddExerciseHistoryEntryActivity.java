package com.example.mkim11235.gainztracker;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Michael on 10/17/2016.
 */

public class AddExerciseHistoryEntryActivity extends AppCompatActivity {
    // Is there a point for these ot be member variables?
    // mb put inside on create
    private long mExerciseId;
    private String mExerciseName;

    Button mAddExerciseHistoryEntryButton;
    EditText mWeightEditText;
    EditText mRepsEditText;
    EditText mDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_history_entry);

        // actionbar setup
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get extras from bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mExerciseId = extras.getLong(getString(R.string.EXTRA_EXERCISE_ID));
            mExerciseName = extras.getString(getString(R.string.EXTRA_EXERCISE_NAME));
        }

        setTitle("Add " + mExerciseName + " Entry");

        mWeightEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_weight);
        mRepsEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_reps);
        mDateEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_date);

        /*
        // Setup Listeners when text changed
        mWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        */

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
                    // Need to remove the '/' from date string
                    dateString = dateString.replace("/", "");
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

    @Override
    public void setTitle(CharSequence title) {
        TextView textViewTitle = (TextView) findViewById(R.id.textview_action_bar_title);
        textViewTitle.setText(title);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }
}
