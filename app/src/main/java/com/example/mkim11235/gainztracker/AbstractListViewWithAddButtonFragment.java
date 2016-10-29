package com.example.mkim11235.gainztracker;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;

/**
 * Created by Michael on 10/28/2016!
 */

public abstract class AbstractListViewWithAddButtonFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int UNDEFINED_INDEX = -1;
    private static final int SHARED_PREF_SORT_BY_DEFAULT_INDEX = 0;
    protected static final int CURSOR_ADAPTER_FLAGS = 0;

    private SharedPreferences mSharedPref;
    protected String mPrefKeySortBy;
    protected CursorAdapter mCursorAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Get the sortby from pref. set it to default 0 if not defined
        mSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int prefSortByIndex = mSharedPref.getInt(mPrefKeySortBy, UNDEFINED_INDEX);
        if (prefSortByIndex == UNDEFINED_INDEX) {
            prefSortByIndex = SHARED_PREF_SORT_BY_DEFAULT_INDEX;
            mSharedPref.edit().putInt(mPrefKeySortBy, prefSortByIndex).apply();
        }

        // init loader to sort based on sharedPref sortby
        getLoaderManager().initLoader(prefSortByIndex, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    protected void setupSpinner(Spinner spinner, int arrayResourceId, final LoaderManager.LoaderCallbacks context) {
        int prefSortByIndex = mSharedPref.getInt(mPrefKeySortBy, SHARED_PREF_SORT_BY_DEFAULT_INDEX);

        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(getActivity(), arrayResourceId, android.R.layout.simple_list_item_1);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(prefSortByIndex);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSharedPref.edit().putInt(mPrefKeySortBy, i).apply();
                getLoaderManager().restartLoader(i, null, context);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
