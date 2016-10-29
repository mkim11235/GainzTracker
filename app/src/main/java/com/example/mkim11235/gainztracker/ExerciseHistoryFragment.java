package com.example.mkim11235.gainztracker;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mkim11235.gainztracker.data.DatabaseContract;
import com.example.mkim11235.gainztracker.tasks.DeleteExerciseHistoryTask;

import java.util.Arrays;

/**
 * Created by Michael on 10/22/2016.
 */

public class ExerciseHistoryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXERCISE_HISTORY_ADAPTER_FLAGS = 0;
    private static final int SHARED_PREF_SORT_BY_DEFAULT_POS = 0;
    private static final String PREF_KEY_SORT_BY_EXERCISE_HISTORY = "PREF_SORT_BY_EXERCISE_HISTORY";

    private static final String[] EXERCISE_HISTORY_COLUMNS = {
            DatabaseContract.ExerciseHistoryEntry._ID,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS,
            DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE
    };

    static final int COL_EXERCISE_HISTORY_ID = 0;
    static final int COL_EXERCISE_HISTORY_EXERCISE_ID = 1;
    static final int COL_EXERCISE_HISTORY_WEIGHT = 2;
    static final int COL_EXERCISE_HISTORY_REPS = 3;
    static final int COL_EXERCISE_HISTORY_DATE = 4;

    private long mExerciseId;
    private String mExerciseName;
    private String[] mSortByArray;

    private Bundle mBaseBundle;
    private TextView mTextViewTitle;
    private ImageButton mAddExerciseHistoryEntryButton;
    private ExerciseHistoryAdapter mExerciseHistoryAdapter;
    private SharedPreferences mSharedPref;

    public ExerciseHistoryFragment() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity_main, menu);

        Spinner spinner = (Spinner) menu.findItem(R.id.menu_item_sort_by).getActionView();
        setupSpinner(spinner, PREF_KEY_SORT_BY_EXERCISE_HISTORY, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_exercise_history_fragment));
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_exercise_history, container, false);

        // Get exerciseId and exerciseName from bundle
        // Set the baseBundle to received bundle
        mBaseBundle = getArguments();
        mExerciseId = mBaseBundle.getLong(getString(R.string.EXTRA_EXERCISE_ID));
        mExerciseName = mBaseBundle.getString(getString(R.string.EXTRA_EXERCISE_NAME));

        // Initialize member vars
        mSortByArray = getResources().getStringArray(R.array.sort_by_exercise_history);
        mTextViewTitle = (TextView) rootView.findViewById(R.id.textview_title_exercise_history);
        mAddExerciseHistoryEntryButton = (ImageButton)
                rootView.findViewById(R.id.image_button_exercise_history_add);
        mExerciseHistoryAdapter = new ExerciseHistoryAdapter(getActivity(), null,
                EXERCISE_HISTORY_ADAPTER_FLAGS);

        // Setup title
        mTextViewTitle.setText(mExerciseName);

        // Implement listview functionality
        ListView exerciseHistoryListView = (ListView)
                rootView.findViewById(R.id.listview_exercise_history);
        exerciseHistoryListView.setAdapter(mExerciseHistoryAdapter);
        registerForContextMenu(exerciseHistoryListView);

        // Add ExerciseHistory Button onClick starts EntryActivity w/ ExerciseHistoryEntryFrag
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EntryActivity.class);
                intent.putExtra(getString(R.string.EXTRA_FRAGMENT_TAG), getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_HISTORY_ENTRY));
                intent.putExtras(mBaseBundle);
                startActivity(intent);

                //Todo: optimization. if sorted by date, can pass in default weight, reps intent
                /*Cursor c = (Cursor) mExerciseHistoryAdapter.getItem(0);
                String w = String.valueOf(c.getLong(COL_EXERCISE_HISTORY_WEIGHT));
                String r = String.valueOf(c.getLong(COL_EXERCISE_HISTORY_REPS));*/
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Get the sortby from shardPref. set it to default 0 if null
        mSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String sharedPrefSortBy = mSharedPref.getString(PREF_KEY_SORT_BY_EXERCISE_HISTORY, null);
        if (sharedPrefSortBy == null) {
            sharedPrefSortBy = mSortByArray[SHARED_PREF_SORT_BY_DEFAULT_POS];
            mSharedPref.edit().putString(PREF_KEY_SORT_BY_EXERCISE_HISTORY, sharedPrefSortBy).apply();
        }

        // init loader to sort based on sharedPref sortby
        int sharedPrefPosition = Arrays.asList(mSortByArray).indexOf(sharedPrefSortBy);
        getLoaderManager().initLoader(sharedPrefPosition, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view
            , ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.listview_exercise_history) {
            String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        Cursor selectedItem = (Cursor) mExerciseHistoryAdapter.getItem(info.position);
        Long exerciseHistoryId = selectedItem.getLong(COL_EXERCISE_HISTORY_ID);

        String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];

        switch (menuItemName) {
            case "Edit":
                Long exerciseWeight = selectedItem.getLong(COL_EXERCISE_HISTORY_WEIGHT);
                Long exerciseReps = selectedItem.getLong(COL_EXERCISE_HISTORY_REPS);
                Long exerciseDate = selectedItem.getLong(COL_EXERCISE_HISTORY_DATE);

                Intent intent = new Intent(getActivity(), EntryActivity.class);
                intent.putExtra(getString(R.string.EXTRA_FRAGMENT_TAG),
                        getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_HISTORY_ENTRY));
                intent.putExtras(mBaseBundle);
                intent.putExtras(buildEditEntryBundle(exerciseWeight, exerciseReps, exerciseDate,
                        exerciseHistoryId));
                startActivity(intent);
                break;
            case "Delete":
                new DeleteExerciseHistoryTask(getActivity()).execute(exerciseHistoryId);
                break;
        }
        return true;
    }

    /**
     * Creates loader for displaying exercise history entries sorted based on spinner selection
     * @param i index of spinner selection
     * @param args null
     * @return CursorLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args) {
        String exerciseIdString = Long.toString(mExerciseId);
        String sortBy;
        switch (i) {
            case 0:
                sortBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC, " +
                        DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC, " +
                        DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " DESC";
                break;
            case 1:
                sortBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC, " +
                        DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " DESC, " +
                        DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC";
                break;
            case 2:
                sortBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " DESC, " +
                        DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC, " +
                        DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC";
                break;
            default:
                sortBy = null;
        }

        return new CursorLoader(getActivity(),
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                EXERCISE_HISTORY_COLUMNS,
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ? ",
                new String[] {exerciseIdString},
                sortBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mExerciseHistoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mExerciseHistoryAdapter.swapCursor(null);
    }

    private Bundle buildEditEntryBundle(long weight, long reps, long date, long id) {
        Bundle bundle = new Bundle();
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_WEIGHT), weight);
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_REPS), reps);
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_DATE), date);
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_HISTORY_ID), id);
        return bundle;
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
