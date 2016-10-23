package com.example.mkim11235.gainztracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ExerciseFragment.OnExerciseSelectedListener {

    private final String EXERCISE_FRAGMENT_TAG = "EFTAG";
    private final String EXERCISE_HISTORY_FRAGMENT_TAG = "EHFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container_activity_main,
                    new ExerciseFragment(), EXERCISE_FRAGMENT_TAG).commit();
        }

        // Dunno about setting title here
        //setTitle(getString(R.string.title_main_activity));
    }

    @Override
    public void onExerciseSelected(long exerciseId) {
        ExerciseHistoryFragment exerciseHistoryFragment =
                ExerciseHistoryFragment.newInstance(exerciseId);

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_activity_main, exerciseHistoryFragment,
                EXERCISE_HISTORY_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }
}
