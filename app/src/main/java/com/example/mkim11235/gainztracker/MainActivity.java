package com.example.mkim11235.gainztracker;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener,
        ExerciseFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_container);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container_empty_activity,
                    new ExerciseFragment(), getString(R.string.FRAGMENT_TAG_EXERCISE)).commit();
        }

        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onExerciseSelected(long exerciseId, String exerciseName) {
        Bundle args = new Bundle();
        args.putLong(getString(R.string.EXTRA_EXERCISE_ID), exerciseId);
        args.putString(getString(R.string.EXTRA_EXERCISE_NAME), exerciseName);

        ExerciseHistoryFragment exerciseHistoryFragment = new ExerciseHistoryFragment();
        exerciseHistoryFragment.setArguments(args);

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_empty_activity, exerciseHistoryFragment,
                getString(R.string.FRAGMENT_TAG_EXERCISE_HISTORY));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackStackChanged() {
        boolean canBack = getFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getFragmentManager().popBackStack();
        return super.onSupportNavigateUp();
    }
}
