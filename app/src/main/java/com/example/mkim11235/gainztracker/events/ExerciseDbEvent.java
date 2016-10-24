package com.example.mkim11235.gainztracker.events;

/**
 * Created by Michael on 10/24/2016.
 */

public class ExerciseDbEvent extends DbEvent {
    private static final long ADD_EXERCISE_ID_DEFAULT = -1;

    private String mExerciseName;
    private String mExerciseMuscle;
    private long mExerciseId;

    public ExerciseDbEvent(String exerciseName, String exerciseMuscle, long exerciseId,
                           DbOperationType dbOperationType) {
        mExerciseName = exerciseName;
        mExerciseMuscle = exerciseMuscle;
        mExerciseId = exerciseId;
        mDbOperationType = dbOperationType;
    }

    public ExerciseDbEvent(String exerciseName, String exerciseMuscle,
                           DbOperationType dbOperationType) {
        this(exerciseName, exerciseMuscle, ADD_EXERCISE_ID_DEFAULT, dbOperationType);
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public String getExerciseMuscle() {
        return mExerciseMuscle;
    }

    public long getExerciseId() {
        return mExerciseId;
    }

    public DbOperationType getDbOperationType() {
        return mDbOperationType;
    }
}
