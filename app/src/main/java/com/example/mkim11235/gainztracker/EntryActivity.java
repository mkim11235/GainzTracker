package com.example.mkim11235.gainztracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mkim11235.gainztracker.ExerciseEntryFragment.AddExerciseEntryFragment;
import com.example.mkim11235.gainztracker.ExerciseEntryFragment.EditExerciseEntryFragment;
import com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment.AddExerciseHistoryEntryFragment;
import com.example.mkim11235.gainztracker.ExerciseHistoryEntryFragment.EditExerciseHistoryEntryFragment;

/**
 * Created by Michael on 10/23/2016.
 */

/**
 * Activity for entering.
 * Enter exercises or exercise histories
 */
public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        // Get fragmentTag from intent
        Bundle extras = getIntent().getExtras();
        String fragmentTag = null;
        if (extras != null) {
            fragmentTag = extras.getString(getString(R.string.EXTRA_FRAGMENT_TAG));
        }

        // Attach the fragment based on the received fragmentTag
        if (savedInstanceState == null) {
            int containerId = R.id.container_activity_exercise_entry;
            Fragment fragment;

            // Cannot use switch statement because R.string values are not static
            if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_ENTRY))) {
                fragment = new AddExerciseEntryFragment();
            } else if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_ENTRY))) {
                fragment = new EditExerciseEntryFragment();
            } else if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_HISTORY_ENTRY))) {
                fragment = new AddExerciseHistoryEntryFragment();
            } else if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_HISTORY_ENTRY))) {
                fragment = new EditExerciseHistoryEntryFragment();
            } else {
                throw new IllegalArgumentException(
                        String.format("Unrecognized fragment tag: %s", fragmentTag));
            }

            fragment.setArguments(extras);
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(containerId, fragment, fragmentTag);
            ft.commit();
        }
    }
}
