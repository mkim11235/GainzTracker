package com.example.mkim11235.gainztracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Michael on 10/17/2016.
 */

public class AddExerciseHistoryEntryActivity extends AppCompatActivity {
    Button mAddExerciseHistoryEntryButton;
    EditText mWeightEditText;
    EditText mRepsEditText;
    EditText mDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        setTitle("Add Exercise Entry");

        mWeightEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_weight);
        mRepsEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_reps);
        mDateEditText = (EditText) findViewById(R.id.edittext_exercise_history_entry_date);

        // When button clicked, create new entry in exercise history table, return to main
        mAddExerciseHistoryEntryButton = (Button) findViewById(R.id.button_add_exercise);
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = mWeightEditText.getText().toString();
                String reps = mRepsEditText.getText().toString();
                String date = mDateEditText.getText().toString();

                // Add new exercise history entry to DB
                AddExerciseHistoryDBTask dbTask = new AddExerciseHistoryDBTask(AddExerciseHistoryEntryActivity.this);
                dbTask.execute(weight, reps, date);

                // Return to exercise activity
                Intent intent = new Intent(v.getContext(), AddExerciseActivity.class);
                startActivity(intent);
            }
        });
    }
}
