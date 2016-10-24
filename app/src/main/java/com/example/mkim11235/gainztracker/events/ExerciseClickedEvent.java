package com.example.mkim11235.gainztracker.events;

/**
 * Created by Michael on 10/24/2016.
 */

public class ExerciseClickedEvent {
    private long mExerciseId;

    public ExerciseClickedEvent(long exerciseId) {
        mExerciseId = exerciseId;
    }

    public long getExerciseId() {
        return mExerciseId;
    }
}
