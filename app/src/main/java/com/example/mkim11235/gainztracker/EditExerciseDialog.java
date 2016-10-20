package com.example.mkim11235.gainztracker;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Michael on 10/20/2016.
 */

public class EditExerciseDialog extends DialogFragment {
    private String mExerciseName;
    private String mExerciseMuscle;
    private Integer mItemPosition;

    private EditText mEditTextName;
    private EditText mEditTextMuscle;
    private Button mButtonDone;

    public EditExerciseDialog() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get arguments from bundle
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            mExerciseName = arguments.getString(getString(R.string.EXTRA_EXERCISE_NAME));
            mExerciseMuscle = arguments.getString(getString(R.string.EXTRA_EXERCISE_MUSCLE));
            mItemPosition = arguments.getInt(getString(R.string.EXTRA_LIST_ITEM_POSITION));
        }

        // Setup member variables
        View view = inflater.inflate(R.layout.fragment_edit_exercise, container);
        mEditTextName = (EditText) view.findViewById(R.id.edittext_edit_exercise_name);
        mEditTextMuscle = (EditText) view.findViewById(R.id.edittext_edit_exercise_muscle);
        mButtonDone = (Button) view.findViewById(R.id.button_edit_exercise);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEditTextName.getText().toString();
                String muscle = mEditTextMuscle.getText().toString();

                if (allValidEntries(name, muscle)) {
                    ((MainActivity) getActivity()).onFinishEditExerciseDialog(name, muscle,
                            mItemPosition);
                    dismiss();
                }
            }
        });

        getDialog().setTitle(buildTitle());
        return view;
    }

    private String buildTitle() {
        return String.format("Change %s | %s", mExerciseName, mExerciseMuscle);
    }

    // returns true if name and muscle are not empty.
    // returns false otherwise. also sets error message for empty edittext
    private boolean allValidEntries(String name, String muscle) {
        boolean allValid = true;
        if (name.length() == 0) {
            mEditTextName.setError("Please enter a valid name");
            allValid = false;
        }

        if (muscle.length() == 0) {
            mEditTextMuscle.setError("Please enter a valid muscle");
            allValid = false;
        }
        return allValid;
    }
}
