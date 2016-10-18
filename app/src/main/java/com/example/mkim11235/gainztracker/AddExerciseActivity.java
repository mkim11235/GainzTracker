package com.example.mkim11235.gainztracker;

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
 * Created by Michael on 10/16/2016.
 */
/*
    form: name, muscle
    add button at bottom

    needs to add changes to exercise table
 */
public class AddExerciseActivity extends AppCompatActivity {

    Button mAddExerciseButton;
    EditText mExerciseName;
    EditText mExerciseMuscle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Actionbar setup
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_add_exercise_activity));

        mExerciseName = (EditText) findViewById(R.id.edittext_exercise_name);
        mExerciseMuscle = (EditText) findViewById(R.id.edittext_exercise_muscle);

        // When button clicked, create new entry in exercise table, return to main
        mAddExerciseButton = (Button) findViewById(R.id.button_add_exercise);
        mAddExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exerciseName = mExerciseName.getText().toString();
                String exerciseMuscle = mExerciseMuscle.getText().toString();

                // Add new exercise to DB
                AddExerciseDBTask dbTask = new AddExerciseDBTask(AddExerciseActivity.this);
                dbTask.execute(exerciseName, exerciseMuscle);

                // Return to main activity
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
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
}
