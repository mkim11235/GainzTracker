package com.example.mkim11235.gainztracker;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;

/**
 * Created by Michael on 10/28/2016!
 */

public abstract class AbstractListViewWithAddButtonFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SHARED_PREF_SORT_BY_DEFAULT_POS = 0;
    protected static final int CURSOR_ADAPTER_FLAGS = 0;

    private SharedPreferences mSharedPref;
    protected String mPrefKeySortBy;
    protected String[] mSortByArray;
    protected CursorAdapter mCursorAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity_main, menu);

        Spinner spinner = (Spinner) menu.findItem(R.id.menu_item_sort_by).getActionView();
        setupSpinner(spinner, mPrefKeySortBy, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Get the sortby from shardPref. set it to default 0 if null
        mSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String sharedPrefSortBy = mSharedPref.getString(mPrefKeySortBy, null);
        if (sharedPrefSortBy == null) {
            sharedPrefSortBy = mSortByArray[SHARED_PREF_SORT_BY_DEFAULT_POS];
            mSharedPref.edit().putString(mPrefKeySortBy, sharedPrefSortBy).apply();
        }

        // init loader to sort based on sharedPref sortby
        int sharedPrefPosition = Arrays.asList(mSortByArray).indexOf(sharedPrefSortBy);
        getLoaderManager().initLoader(sharedPrefPosition, null, this);
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

    private void setupSpinner(Spinner spinner, final String prefKey, final LoaderManager.LoaderCallbacks context) {
        String sharedPrefSortBy = mSharedPref.getString(prefKey, mSortByArray[SHARED_PREF_SORT_BY_DEFAULT_POS]);
        int sharedPrefPosition = Arrays.asList(mSortByArray).indexOf(sharedPrefSortBy);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mSortByArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(sharedPrefPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                mSharedPref.edit().putString(prefKey, selectedItem).apply();
                getLoaderManager().restartLoader(i, null, context);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
