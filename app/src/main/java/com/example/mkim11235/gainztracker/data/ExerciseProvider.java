package com.example.mkim11235.gainztracker.data;

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
    static final int EXERCISE_HISTORY = 200;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // May not need this
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

    // May not need this
    private static final String sExerciseHistoryByExerciseId =
            DatabaseContract.ExerciseHistoryEntry.TABLE_NAME +
                    "." + DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ? ";

    // Optimization mb later: use sExercisehistoryId instead of passing in through query cuz
    // always just want weight, reps, date
    /*
    private Cursor getExerciseHistoryByExerciseId(Uri uri, String[] projection, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.ExerciseHistoryEntry.TABLE_NAME);

        return builder.query(mDBHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }
    */

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DatabaseContract.PATH_EXERCISE, EXERCISE);
        matcher.addURI(authority, DatabaseContract.PATH_EXERCISE_HISTORY, EXERCISE_HISTORY);

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

            case EXERCISE_HISTORY: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        DatabaseContract.ExerciseHistoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
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
            case EXERCISE_HISTORY:
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

        switch (match) {
            case EXERCISE: {
                long _id = db.insert(DatabaseContract.ExerciseEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.ExerciseEntry.buildExerciseUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case EXERCISE_HISTORY: {
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
            case EXERCISE_HISTORY:
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
            case EXERCISE_HISTORY:
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
            case EXERCISE_HISTORY:
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
