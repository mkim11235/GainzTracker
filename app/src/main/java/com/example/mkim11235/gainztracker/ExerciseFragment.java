package com.example.mkim11235.gainztracker;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mkim11235.gainztracker.data.DatabaseContract;
import com.example.mkim11235.gainztracker.tasks.DeleteExerciseTask;

/**
 * Created by Michael on 10/22/2016.
 */

public class ExerciseFragment extends AbstractListViewWithAddButtonFragment {

    static final int COL_EXERCISE_ID = 0;
    static final int COL_EXERCISE_NAME = 1;
    static final int COL_EXERCISE_MUSCLE = 2;
    private static final String[] EXERCISE_COLUMNS = {
            DatabaseContract.ExerciseEntry._ID,
            DatabaseContract.ExerciseEntry.COLUMN_NAME,
            DatabaseContract.ExerciseEntry.COLUMN_MUSCLE,
    };
    private static final String PREF_KEY_SORT_BY = "PREF_SORT_BY_EXERCISE";

    private ImageButton mAddExerciseButton;
    private Callback mCallBack;

    public ExerciseFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_exercise, menu);

        Spinner spinner = (Spinner) menu.findItem(R.id.menu_item_sort_by_exercise).getActionView();
        setupSpinner(spinner, R.array.sort_by_exercise, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_exercise_fragment));
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        // Initialize member variables
        mCursorAdapter = new ExerciseAdapter(getActivity(), null, CURSOR_ADAPTER_FLAGS);
        mAddExerciseButton = (ImageButton) rootView.findViewById(R.id.image_button_exercise_add);
        mPrefKeySortBy = PREF_KEY_SORT_BY;

        // Implement listview functionality
        ListView exerciseListView = (ListView) rootView.findViewById(R.id.listview_exercise);
        exerciseListView.setAdapter(mCursorAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) mCursorAdapter.getItem(position);
                long exerciseId = cursor.getLong(COL_EXERCISE_ID);
                String exerciseName = cursor.getString(COL_EXERCISE_NAME);
                mCallBack.onExerciseSelected(exerciseId, exerciseName);
            }
        });
        registerForContextMenu(exerciseListView);

        // Add Exercise Button onClick starts entryActivity
        // Need to pass in indicator to attach AddExerciseEntryActivity
        mAddExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EntryActivity.class);
                intent.putExtra(getString(R.string.EXTRA_FRAGMENT_TAG),
                        getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_ENTRY));
                startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * Sets up callback to parent activity
     * Need to override deprecated method because supporting older devices
     *
     * @param activity activity this fragment is attaching to
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view
            , ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.listview_exercise) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }

            Cursor selectedItem = (Cursor) mCursorAdapter.getItem(info.position);
            String exerciseName = selectedItem.getString(COL_EXERCISE_NAME);
            menu.setHeaderTitle(exerciseName);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        Cursor selectedItem = (Cursor) mCursorAdapter.getItem(info.position);
        String exerciseId = Long.toString(selectedItem.getLong(COL_EXERCISE_ID));
        String exerciseName = selectedItem.getString(COL_EXERCISE_NAME);
        String exerciseMuscle = selectedItem.getString(COL_EXERCISE_MUSCLE);

        String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];

        switch (menuItemName) {
            case "Edit":
                // start entryActivity w/ fragment tag for UpdateExerciseEntry
                // pass in old values of exercise clicked
                Intent intent = new Intent(getActivity(), EntryActivity.class);
                intent.putExtra(getString(R.string.EXTRA_FRAGMENT_TAG),
                        getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_ENTRY));
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_ID), exerciseId);
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_NAME), exerciseName);
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_MUSCLE), exerciseMuscle);
                startActivity(intent);
                break;
            case "Delete":
                new DeleteExerciseTask(getActivity()).execute(exerciseId);
                break;
        }
        return true;
    }

    /**
     * Creates loader for displaying exercises sorted based on spinner selection
     *
     * @param i      index of selected spinner in mSortByArray
     * @param bundle null
     * @return CursorLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortBy;
        switch (i) {
            case 0:
                sortBy = DatabaseContract.ExerciseEntry.COLUMN_NAME + " ASC";
                break;
            case 1:
                sortBy = DatabaseContract.ExerciseEntry.COLUMN_MUSCLE + " ASC";
                break;
            default:
                sortBy = null;
        }

        return new CursorLoader(getActivity(),
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                EXERCISE_COLUMNS,
                null,
                null,
                sortBy);
    }

    public interface Callback {
        void onExerciseSelected(long exerciseId, String exerciseName);
    }
}
