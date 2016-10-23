package com.example.mkim11235.gainztracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mkim11235.gainztracker.ExerciseEntryFragment.AddExerciseEntryFragment;
import com.example.mkim11235.gainztracker.ExerciseEntryFragment.EditExerciseEntryFragment;

/**
 * Created by Michael on 10/16/2016.
 */

public class ExerciseEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_entry);

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
            String tag;

            if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_ENTRY))) {
                fragment = new AddExerciseEntryFragment();
                tag = getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_ENTRY);
            } else if (fragmentTag.equals(getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_ENTRY))){
                fragment = new EditExerciseEntryFragment();
                tag = getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_ENTRY);
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
