package com.example.mkim11235.gainztracker;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mkim11235.gainztracker.data.DatabaseContract;
import com.example.mkim11235.gainztracker.tasks.DeleteExerciseHistoryTask;

/**
 * Created by Michael on 10/22/2016.
 */

public class ExerciseHistoryFragment extends AbstractListViewWithAddButtonFragment {

    private static final int MOST_RECENT_ENTRY_INDEX = 0;
    private static final String PREF_KEY_SORT_BY = "PREF_SORT_BY_EXERCISE_HISTORY";
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

    private Bundle mBaseBundle;
    private TextView mTextViewTitle;
    private ImageButton mAddExerciseHistoryEntryButton;

    public ExerciseHistoryFragment() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_exercise_history, menu);

        Spinner spinner = (Spinner) menu.findItem(R.id.menu_item_sort_by_exercise_history).getActionView();
        setupSpinner(spinner, R.array.sort_by_exercise_history, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_exercise_history_fragment));
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_exercise_history, container, false);

        // Get arguments and set members
        mBaseBundle = getArguments();
        mExerciseId = mBaseBundle.getLong(getString(R.string.EXTRA_EXERCISE_ID));
        mExerciseName = mBaseBundle.getString(getString(R.string.EXTRA_EXERCISE_NAME));

        // Initialize member vars
        mTextViewTitle = (TextView) rootView.findViewById(R.id.textview_title_exercise_history);
        mAddExerciseHistoryEntryButton = (ImageButton)
                rootView.findViewById(R.id.image_button_exercise_history_add);
        mCursorAdapter = new ExerciseHistoryAdapter(getActivity(), null,
                CURSOR_ADAPTER_FLAGS);
        mPrefKeySortBy = PREF_KEY_SORT_BY;

        // Setup title
        mTextViewTitle.setText(mExerciseName);

        // Implement listview functionality
        ListView exerciseHistoryListView = (ListView)
                rootView.findViewById(R.id.listview_exercise_history);
        exerciseHistoryListView.setAdapter(mCursorAdapter);
        registerForContextMenu(exerciseHistoryListView);

        // Add ExerciseHistory Button onClick starts EntryActivity w/ ExerciseHistoryEntryFrag
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EntryActivity.class);
                intent.putExtra(getString(R.string.EXTRA_FRAGMENT_TAG), getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_HISTORY_ENTRY));
                intent.putExtras(mBaseBundle);

                // If list sorted by Date, pass in most recent entry through intent
                String[] sortByArray = getResources().getStringArray(R.array.sort_by_exercise_history);
                int sortByIndex = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(PREF_KEY_SORT_BY, UNDEFINED_INDEX);
                String selectedItem = sortByArray[sortByIndex];
                if (selectedItem.equals("Date")) {
                    Cursor cursor = (Cursor) mCursorAdapter.getItem(MOST_RECENT_ENTRY_INDEX);
                    String weightString = String.valueOf(cursor.getLong(COL_EXERCISE_HISTORY_WEIGHT));
                    String repsString = String.valueOf(cursor.getLong(COL_EXERCISE_HISTORY_REPS));

                    intent.putExtra(getString(R.string.EXTRA_EXERCISE_WEIGHT), weightString);
                    intent.putExtra(getString(R.string.EXTRA_EXERCISE_REPS), repsString);
                }

                startActivity(intent);
            }
        });

        return rootView;
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

        Cursor selectedItem = (Cursor) mCursorAdapter.getItem(info.position);
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

    private Bundle buildEditEntryBundle(long weight, long reps, long date, long id) {
        Bundle bundle = new Bundle();
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_WEIGHT), weight);
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_REPS), reps);
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_DATE), date);
        bundle.putLong(getString(R.string.EXTRA_EXERCISE_HISTORY_ID), id);
        return bundle;
    }
}
