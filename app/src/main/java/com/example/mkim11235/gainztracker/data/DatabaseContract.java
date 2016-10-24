package com.example.mkim11235.gainztracker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Michael on 10/12/2016.
 */

// Defines table and column names for DB
public class DatabaseContract {

    public static final String CONTENT_AUTHORITY = "com.example.mkim11235.gainztracker";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EXERCISE = "exercise";
    public static final String PATH_EXERCISE_HISTORY = "exercise_history";

    // Exercise Table on Main.
    public static final class ExerciseEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;

        public static final String TABLE_NAME = "exercise";

        public static final String COLUMN_NAME = "column_name";
        public static final String COLUMN_MUSCLE = "column_muscle";

        public static Uri buildExerciseUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ExerciseHistoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE_HISTORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY
                        + "/" + PATH_EXERCISE_HISTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY
                        + "/" + PATH_EXERCISE_HISTORY;

        public static final String TABLE_NAME = "exercise_history";

        // Foreign key into the Exercise Table
        public static final String COLUMN_EXERCISE_ID = "column_exercise_id";
        public static final String COLUMN_WEIGHT = "column_weight";
        public static final String COLUMN_REPS = "column_reps";
        public static final String COLUMN_DATE = "column_date";

        public static Uri buildExerciseHistoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
