package com.example.mkim11235.gainztracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> mWorkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DUMMY WORKOUTS
        String[] workouts = {
                "Chest Workout",
                "Back Workout",
                "Shoulder Workout",
                "Tricep Workout",
                "Bicep Workout",
        };

        List<String> workoutList = new ArrayList<String>(Arrays.asList(workouts));

        mWorkoutAdapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_workout,
                R.id.list_item_workout_textview,
                workoutList);

        ListView listView = (ListView) this.findViewById(R.id.listview_workouts);
        listView.setAdapter(mWorkoutAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String workout = mWorkoutAdapter.getItem(position);
                Intent intent = new Intent(view.getContext(), WorkoutActivity.class).putExtra(Intent.EXTRA_TEXT, workout);
                startActivity(intent);
            }
        });
    }

    public void startWorkout(View view) {
        Intent intent = new Intent(this, WorkoutActivity.class);
        startActivity(intent);
    }
}
