package com.example.mkim11235.gainztracker;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Michael on 10/23/2016.
 */

public abstract class AbstractExerciseEntryFragment extends Fragment {

    /**
     * Gets and Initializes extra args from bundle
     * @param bundle bundle to get args from
     */
    protected abstract void setExtraMembersFromBundle(Bundle bundle);

    /**
     * Sets default values for edittexts
     */
    protected abstract void setEditTextDefaults();

    /**
     * Sets the exerciseEntryButton's text
     */
    protected abstract void setFinalButtonText();

    /**
     * Sets ExerciseEntryButton onclicklistener
     */
    protected abstract void setFinalButtonOnClickListener();
}
