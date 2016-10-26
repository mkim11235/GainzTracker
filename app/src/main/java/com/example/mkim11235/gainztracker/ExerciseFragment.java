package com.example.mkim11235.gainztracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
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

import com.example.mkim11235.gainztracker.data.DatabaseContract;
import com.example.mkim11235.gainztracker.tasks.DeleteExerciseTask;

/**
 * Created by Michael on 10/22/2016.
 */

public class ExerciseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXERCISE_LOADER = 0;
    private static final int EXERCISE_ADAPTER_FLAGS = 0;

    private static final String[] EXERCISE_COLUMNS = {
            DatabaseContract.ExerciseEntry._ID,
            DatabaseContract.ExerciseEntry.COLUMN_NAME,
            DatabaseContract.ExerciseEntry.COLUMN_MUSCLE,
    };

    static final int COL_EXERCISE_ID = 0;
    static final int COL_EXERCISE_NAME = 1;
    static final int COL_EXERCISE_MUSCLE = 2;

    private ImageButton mAddExerciseButton;
    private ExerciseAdapter mExerciseAdapter;
    private OnExerciseSelectedListener mCallBack;

    public ExerciseFragment() {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sort_by:
                //Todo: implement dialog or something. maybe need preferences
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_exercise, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_exercise_fragment));
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        // Initialize member variables
        mExerciseAdapter = new ExerciseAdapter(getActivity(), null, EXERCISE_ADAPTER_FLAGS);
        mAddExerciseButton = (ImageButton) rootView.findViewById(R.id.image_button_exercise_add);

        // Implement listview functionality
        ListView exerciseListView = (ListView) rootView.findViewById(R.id.listview_exercise);
        exerciseListView.setAdapter(mExerciseAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) mExerciseAdapter.getItem(position);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(EXERCISE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Sets up callback to parent activity
     * Need to override deprecated method because supporting older devices
     * @param activity activity this fragment is attaching to
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnExerciseSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnExerciseSelectedListener");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view
            , ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.listview_exercise) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

            String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }

            Cursor selectedItem = (Cursor) mExerciseAdapter.getItem(info.position);
            String exerciseName = selectedItem.getString(COL_EXERCISE_NAME);
            menu.setHeaderTitle(exerciseName);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        Cursor selectedItem = (Cursor) mExerciseAdapter.getItem(info.position);
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                EXERCISE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mExerciseAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mExerciseAdapter.swapCursor(null);
    }

    public interface OnExerciseSelectedListener {
        void onExerciseSelected(long exerciseId, String exerciseName);
    }
}
