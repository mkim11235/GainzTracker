package com.example.mkim11235.gainztracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mkim11235.gainztracker.data.DatabaseContract.ExerciseEntry;
import com.example.mkim11235.gainztracker.data.DatabaseContract.ExerciseHistoryEntry;

/**
 * Created by Michael on 10/16/2016.
 */

public class ExerciseDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "exercise.db";

    public ExerciseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExerciseEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                ExerciseEntry.COLUMN_MUSCLE + " TEXT NOT NULL);";

        final String SQL_CREATE_EXERCISE_HISTORY_TABLE = "CREATE TABLE " + ExerciseHistoryEntry.TABLE_NAME + " (" +
                ExerciseHistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " INTEGER NOT NULL, " +
                ExerciseHistoryEntry.COLUMN_WEIGHT + " INTEGER NOT NULL, " +
                ExerciseHistoryEntry.COLUMN_REPS + " INTEGER NOT NULL, " +
                ExerciseHistoryEntry.COLUMN_DATE + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_EXERCISE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EXERCISE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExerciseHistoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
