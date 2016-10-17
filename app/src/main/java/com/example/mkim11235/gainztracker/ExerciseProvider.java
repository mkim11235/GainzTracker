package com.example.mkim11235.gainztracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Michael on 10/16/2016.
 */

public class ExerciseProvider extends ContentProvider {
    private ExerciseDBHelper mDBHelper;

    static final int EXERCISE = 100;
    static final int EXERCISE_WITH_MUSCLE = 101;
    static final int EXERCISE_HISTORY_WITH_WEIGHT_REPS = 200;
    static final int EXERCISE_HISTORY_WITH_WEIGHT_REPS_DATE = 201;

    // MB delete later
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sExerciseHistoryByExerciseQueryBuilder;

    static {
        sExerciseHistoryByExerciseQueryBuilder = new SQLiteQueryBuilder();
        // Inner join Exercise and ExerciseHistory Tables on Exercise_ID
        sExerciseHistoryByExerciseQueryBuilder.setTables(
                DatabaseContract.ExerciseHistoryEntry.TABLE_NAME + " INNER JOIN " +
                        DatabaseContract.ExerciseEntry.TABLE_NAME +
                        " ON " + DatabaseContract.ExerciseHistoryEntry.TABLE_NAME +
                        "." + DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID +
                        " = " + DatabaseContract.ExerciseEntry.TABLE_NAME +
                        "." + DatabaseContract.ExerciseEntry._ID);
    }

    // location.setting = ? AND 23456 >= ?
    // ? are variables mb
    // location.setting = kirkland and date=2/3/14

    // muscle = ?
    private static final String sExerciseWithMuscle =
            DatabaseContract.ExerciseEntry.TABLE_NAME +
                    "." + DatabaseContract.ExerciseEntry.COLUMN_MUSCLE + " = ? ";

    // Weight = ? and Reps = ?
    private static final String sExerciseHistoryWithWeightReps =
            DatabaseContract.ExerciseHistoryEntry.TABLE_NAME +
                    "." + DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " = ? AND " +
                    DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " = ? ";

    // Weight = ? and Reps = ? and date = ?
    private static final String sExerciseHistoryWithWeightRepsDate =
            DatabaseContract.ExerciseHistoryEntry.TABLE_NAME +
                    "." + DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " = ? AND " +
                    DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " = ? AND " +
                    DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " = ? ";

    private Cursor getExerciseWithMuscle(Uri uri, String[] projection, String sortOrder) {
        String muscle = DatabaseContract.ExerciseEntry.getMuscleFromUri(uri);

        return sExerciseHistoryByExerciseQueryBuilder.query(mDBHelper.getReadableDatabase(),
                projection,
                sExerciseWithMuscle,
                new String[]{muscle},
                null,
                null,
                sortOrder);
    }

    private Cursor getExerciseHistoryWithWeightReps(Uri uri, String[] projection, String sortOrder) {
        String weight = DatabaseContract.ExerciseHistoryEntry.getWeightFromUri(uri);
        String reps = DatabaseContract.ExerciseHistoryEntry.getRepsFromUri(uri);

        return sExerciseHistoryByExerciseQueryBuilder.query(mDBHelper.getReadableDatabase(),
                projection,
                sExerciseHistoryWithWeightReps,
                new String[]{weight, reps},
                null,
                null,
                sortOrder);
    }

    private Cursor getExerciseHistoryWithWeightRepsDate(Uri uri, String[] projection, String sortOrder) {
        String weight = DatabaseContract.ExerciseHistoryEntry.getWeightFromUri(uri);
        String reps = DatabaseContract.ExerciseHistoryEntry.getRepsFromUri(uri);
        String date = DatabaseContract.ExerciseHistoryEntry.getDateFromUri(uri);

        return sExerciseHistoryByExerciseQueryBuilder.query(mDBHelper.getReadableDatabase(),
                projection,
                sExerciseHistoryWithWeightRepsDate,
                new String[]{weight, reps, date},
                null,
                null,
                sortOrder);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DatabaseContract.PATH_EXERCISE, EXERCISE);
        matcher.addURI(authority, DatabaseContract.PATH_EXERCISE + "/*", EXERCISE_WITH_MUSCLE);

        // Not sure. I think name, weight,  reps * # #. might have to add * to start
        matcher.addURI(authority, DatabaseContract.PATH_EXERCISE_HISTORY + "/#/#", EXERCISE_HISTORY_WITH_WEIGHT_REPS);
        matcher.addURI(authority, DatabaseContract.PATH_EXERCISE_HISTORY + "/#/#/#", EXERCISE_HISTORY_WITH_WEIGHT_REPS_DATE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
       mDBHelper = new ExerciseDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case EXERCISE: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        DatabaseContract.ExerciseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case EXERCISE_WITH_MUSCLE: {
                retCursor = getExerciseWithMuscle(uri, projection, sortOrder);
                break;
            }

            case EXERCISE_HISTORY_WITH_WEIGHT_REPS: {
                retCursor = getExerciseHistoryWithWeightReps(uri, projection, sortOrder);
                break;
            }

            case EXERCISE_HISTORY_WITH_WEIGHT_REPS_DATE: {
                retCursor = getExerciseHistoryWithWeightRepsDate(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Makes observer watches URI for any changes
        // Lets CP tell UI when changes made and update
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            // Only returns single row bcuz exercises are unique. so Item_TYpe
            case EXERCISE:
                return DatabaseContract.ExerciseEntry.CONTENT_ITEM_TYPE;
            case EXERCISE_WITH_MUSCLE:
                return  DatabaseContract.ExerciseEntry.CONTENT_ITEM_TYPE;
            case EXERCISE_HISTORY_WITH_WEIGHT_REPS:
                return DatabaseContract.ExerciseHistoryEntry.CONTENT_TYPE;
            case EXERCISE_HISTORY_WITH_WEIGHT_REPS_DATE:
                return DatabaseContract.ExerciseHistoryEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        // TO DO
        // Probably gonna have to add insert for Exercise w/ name and muscle
        // history with weight reps date
        switch (match) {
            case EXERCISE: {
                long _id = db.insert(DatabaseContract.ExerciseEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.ExerciseEntry.buildExerciseUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case EXERCISE_HISTORY_WITH_WEIGHT_REPS: {
                long _id = db.insert(DatabaseContract.ExerciseHistoryEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.ExerciseHistoryEntry.buildExerciseHistoryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (selection == null) selection = "1";
        switch(match) {
            case EXERCISE:
                rowsDeleted = db.delete(DatabaseContract.ExerciseEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case EXERCISE_HISTORY_WITH_WEIGHT_REPS:
                rowsDeleted = db.delete(DatabaseContract.ExerciseHistoryEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        if (selection == null) selection = "1";
        switch(match) {
            case EXERCISE:
                rowsUpdated = db.update(DatabaseContract.ExerciseEntry.TABLE_NAME, values,
                        selection,
                        selectionArgs);
                break;
            case EXERCISE_HISTORY_WITH_WEIGHT_REPS:
                rowsUpdated = db.update(DatabaseContract.ExerciseHistoryEntry.TABLE_NAME, values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EXERCISE_HISTORY_WITH_WEIGHT_REPS:
                db.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.ExerciseHistoryEntry.TABLE_NAME,
                                null, value);
                        if (_id != -1) {
                            count++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
