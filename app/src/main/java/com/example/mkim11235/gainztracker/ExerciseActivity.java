package com.example.mkim11235.gainztracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

// Has Exercise title, history of workouts as list
// button at bottom to add new entry to history
// hopefully later can support holding click to delete or change entry
public class ExerciseActivity extends AppCompatActivity {
    private ImageButton mAddExerciseHistoryEntryButton;
    //private ExerciseHistoryAdapter mExerciseHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Get exercise id from intent
        Intent intent = getIntent();
        long exerciseId = intent.getLongExtra(Intent.EXTRA_TEXT, -1L);

        // Gets and sets exercise title
        getAndSetExerciseTitleFromId(exerciseId);

        // Setup adaptor to populate listview

        mAddExerciseHistoryEntryButton = (ImageButton) findViewById(R.id.image_button_add_exercise_history_entry);
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TO DO: Start new activity for creating new history entry
                // Need to create new activity and xml and shiet
            }
        });
    }

    // Gets and sets Exercise Title from ID
    // Has to read database so async task
    private void getAndSetExerciseTitleFromId(long id) {
        FetchExerciseTitleTask exerciseTitleTask = new FetchExerciseTitleTask(this);
        exerciseTitleTask.execute(id);
    }
}
