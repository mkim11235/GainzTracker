package com.example.mkim11235.gainztracker;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mkim11235.gainztracker.data.DatabaseContract;
import com.example.mkim11235.gainztracker.tasks.DeleteExerciseHistoryTask;
import com.example.mkim11235.gainztracker.tasks.FetchExerciseTitleTask;

/**
 * Created by Michael on 10/22/2016.
 */

public class ExerciseHistoryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXERCISE_HISTORY_LOADER = 1;
    private static final int EXERCISE_HISTORY_ADAPTER_FLAGS = 0;
    private static final String EXTRA_EXERCISE_ID =
            "com.example.mkim11235.gainztracker.extra.EXERCISE_ID";

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

    private ImageButton mAddExerciseHistoryEntryButton;
    private ExerciseHistoryAdapter mExerciseHistoryAdapter;
    private long mExerciseId;
    private String mExerciseName;
    private Bundle mExerciseHistoryEntryBundle;

    public ExerciseHistoryFragment() {}

    public static ExerciseHistoryFragment newInstance(long exerciseId) {
        Bundle args = new Bundle();
        args.putLong(EXTRA_EXERCISE_ID, exerciseId);

        ExerciseHistoryFragment fragment = new ExerciseHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Todo: if implement options menu, uncomment line below
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_history, container, false);
        // Todo: check if this is correct when reviewing actionbar
        // Dunno if do it here or in the activity
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get exerciseId from intent
        mExerciseId = getArguments().getLong(getString(R.string.EXTRA_EXERCISE_ID));

        // Initialize member vars
        mExerciseHistoryAdapter = new ExerciseHistoryAdapter(getActivity(), null,
                EXERCISE_HISTORY_ADAPTER_FLAGS);
        mAddExerciseHistoryEntryButton = (ImageButton)
                rootView.findViewById(R.id.image_button_add_exercise_history_entry);

        // Implement listview functionality
        ListView exerciseHistoryListView = (ListView)
                rootView.findViewById(R.id.listview_exercise_history);
        exerciseHistoryListView.setAdapter(mExerciseHistoryAdapter);
        registerForContextMenu(exerciseHistoryListView);

        //Todo: rethink title scheme. maybe have title below actionbar in xml
        //Todo: this thing doesnt set up bundle correctly figure out bug
        // Gets and sets exercise title
        new FetchExerciseTitleTask(this).execute(mExerciseId);

        // Add ExerciseHistory Button onClick starts ExerciseHistoryEntryFrag
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EntryActivity.class);
                intent.putExtras(mExerciseHistoryEntryBundle);
                intent.putExtra(getString(R.string.EXTRA_FRAGMENT_TAG), getString(R.string.FRAGMENT_TAG_ADD_EXERCISE_HISTORY_ENTRY));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(EXERCISE_HISTORY_LOADER, null, this);
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

        RelativeLayout view = (RelativeLayout) info.targetView;
        String exerciseWeight = ((TextView)view.findViewById(R.id.list_item_exercise_history_weight))
                .getText().toString();
        String exerciseReps = ((TextView)view.findViewById(R.id.list_item_exercise_history_reps))
                .getText().toString();
        String exerciseDate = ((TextView)view.findViewById(R.id.list_item_exercise_history_date))
                .getText().toString();
        exerciseDate = Utility.formatDateReadableToDB(exerciseDate);

        String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];

        switch (menuItemName) {
            case "Edit":
                Intent intent = new Intent(view.getContext(), EntryActivity.class);
                intent.putExtras(mExerciseHistoryEntryBundle);
                intent.putExtra(getString(R.string.EXTRA_FRAGMENT_TAG), getString(R.string.FRAGMENT_TAG_EDIT_EXERCISE_HISTORY_ENTRY));
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_WEIGHT), exerciseWeight);
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_REPS), exerciseReps);
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_DATE), exerciseDate);
                startActivity(intent);
                break;
            case "Delete":
                new DeleteExerciseHistoryTask(getActivity()).execute(exerciseWeight, exerciseReps, exerciseDate);
                break;
        }
        return true;
    }

    /**
     * Called from FetchExerciseTitleTask to set member exerciseName
     * @param name name to set exerciseName to
     */
    public void setExerciseName(String name) {
        mExerciseName = name;
    }

    /**
     * Called from FetchExerciseTitleTask when name is computed
     * Puts id and name in bundle
     */
    public void setBundle() {
        mExerciseHistoryEntryBundle = new Bundle();
        mExerciseHistoryEntryBundle.putLong(getString(R.string.EXTRA_EXERCISE_ID), mExerciseId);
        mExerciseHistoryEntryBundle.putString(getString(R.string.EXTRA_EXERCISE_NAME), mExerciseName);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String exerciseIdString = Long.toString(mExerciseId);
        String orderBy = DatabaseContract.ExerciseHistoryEntry.COLUMN_DATE + " DESC, " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC, " +
                DatabaseContract.ExerciseHistoryEntry.COLUMN_REPS + " DESC";

        return new CursorLoader(getActivity(),
                DatabaseContract.ExerciseHistoryEntry.CONTENT_URI,
                EXERCISE_HISTORY_COLUMNS,
                DatabaseContract.ExerciseHistoryEntry.COLUMN_EXERCISE_ID + " = ? ",
                new String[] {exerciseIdString},
                orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mExerciseHistoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mExerciseHistoryAdapter.swapCursor(null);
    }
}
