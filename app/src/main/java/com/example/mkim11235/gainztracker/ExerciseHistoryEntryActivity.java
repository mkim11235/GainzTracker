package com.example.mkim11235.gainztracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment.AddExerciseHistoryEntryFragment;
import com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment.EditExerciseHistoryEntryFragment;

/**
 * Created by Michael on 10/17/2016.
 */

public class ExerciseHistoryEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history_entry);

        // Get fragmentTag from intent
        Bundle extras = getIntent().getExtras();
        String fragmentTag = null;
        if (extras != null) {
            fragmentTag = extras.getString(getString(R.string.EXTRA_FRAGMENT_TAG));
        }

        if (savedInstanceState == null) {
            int containerId = R.id.container_activity_exercise_history_entry;
            Fragment fragment;
            String tag;

            if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_HISTORY_ENTRY))) {
                fragment = new AddExerciseHistoryEntryFragment();
                tag = getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_HISTORY_ENTRY);
            } else if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_HISTORY_ENTRY))){
                fragment = new EditExerciseHistoryEntryFragment();
                fragment.setArguments(extras);
                tag = getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_HISTORY_ENTRY);
            } else {
                throw new IllegalArgumentException(
                        String.format("Unrecognized fragment tag: %s", fragmentTag));
            }

            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(containerId, fragment, tag);
            ft.commit();
        }
    }

}
