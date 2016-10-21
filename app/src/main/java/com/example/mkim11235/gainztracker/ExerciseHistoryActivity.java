package com.example.mkim11235.gainztracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity.AddExerciseHistoryEntryActivity;
import com.example.mkim11235.gainztracker.ExerciseHistoryEntryActivity.EditExerciseHistoryEntryActivity;
import com.example.mkim11235.gainztracker.data.DatabaseContract;

// Once clicked on specific exercise from main, enter here
// Has Exercise title, history of workouts as list
// button at bottom to add new entry to history
// hopefully later can support holding click to delete or change entry
public class ExerciseHistoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXERCISE_HISTORY_LOADER = 1; // try doing 0 later see wat hapen

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get exercise id from intent
        Intent intent = getIntent();
        mExerciseId = intent.getLongExtra(Intent.EXTRA_TEXT, -1L);

        // Need to somehow pass exerciseId into loader for query
        getSupportLoaderManager().initLoader(EXERCISE_HISTORY_LOADER, null, this);
        mExerciseHistoryAdapter = new ExerciseHistoryAdapter(this, null, 0);

        // Gets and sets exercise title
        new FetchExerciseTitleTask(this).execute(mExerciseId);

        // Setup adaptor to populate listview
        ListView listView = (ListView) findViewById(R.id.listview_exercise_history);
        listView.setAdapter(mExerciseHistoryAdapter);
        registerForContextMenu(listView);

        /*
        // Add functionality for details maybe later
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                long id = mExerciseHistoryAdapter.getItemId(position);
            }
        });
        */

        mAddExerciseHistoryEntryButton = (ImageButton)
                findViewById(R.id.image_button_add_exercise_history_entry);
        mAddExerciseHistoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pass in the exerciseID and name of exercise via bundle
                Intent intent = new Intent(view.getContext(), AddExerciseHistoryEntryActivity.class)
                        .putExtras(mExerciseHistoryEntryBundle);
                startActivity(intent);
            }
        });
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

        LinearLayout view = (LinearLayout) info.targetView;
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
                Intent intent = new Intent(view.getContext(), EditExerciseHistoryEntryActivity.class);
                intent.putExtras(mExerciseHistoryEntryBundle);
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_WEIGHT), exerciseWeight);
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_REPS), exerciseReps);
                intent.putExtra(getString(R.string.EXTRA_EXERCISE_DATE), exerciseDate);
                startActivity(intent);
                break;
            case "Delete":
                new DeleteExerciseHistoryTask(this).execute(exerciseWeight, exerciseReps, exerciseDate);
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
                DatabaseContract.ExerciseHistoryEntry.COLUMN_WEIGHT + " DESC";
        return new CursorLoader(this,
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
