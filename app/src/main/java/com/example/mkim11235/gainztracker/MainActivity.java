package com.example.mkim11235.gainztracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ExerciseFragment.OnExerciseSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_container);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container_empty_activity,
                    new ExerciseFragment(), getString(R.string.FRAGMENT_TAG_EXERCISE)).commit();
        }

        // Dunno about setting title here
        //setTitle(getString(R.string.title_main_activity));
    }

    @Override
    public void onExerciseSelected(long exerciseId) {
        ExerciseHistoryFragment exerciseHistoryFragment =
                ExerciseHistoryFragment.newInstance(exerciseId);

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_empty_activity, exerciseHistoryFragment,
                getString(R.string.FRAGMENT_TAG_EXERCISE_HISTORY));
        ft.addToBackStack(null);
        ft.commit();
    }
}
