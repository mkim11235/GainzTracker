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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkim11235.gainztracker.data.DatabaseContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXERCISE_LOADER = 0;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.title_main_activity));

        // Sets up loader for getting all exercises
        getSupportLoaderManager().initLoader(EXERCISE_LOADER, null, this);

        mExerciseAdapter = new ExerciseAdapter(this, null, 0);
        mAddExerciseButton = (ImageButton) findViewById(R.id.image_button_add_exercise);

        ListView listView = (ListView) findViewById(R.id.listview_exercises);
        listView.setAdapter(mExerciseAdapter);
        registerForContextMenu(listView);
        // On Click goes to exercise history activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                long id = mExerciseAdapter.getItemId(position);
                Intent intent = new Intent(view.getContext(), ExerciseHistoryActivity.class).putExtra(Intent.EXTRA_TEXT, id);
                startActivity(intent);
            }
        });

        // Add Exercise Button onClick starts AddExercise Activity
        mAddExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddExerciseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view
            , ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.listview_exercises) {
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

        RelativeLayout view = (RelativeLayout) info.targetView;
        String exerciseName = ((TextView) view.findViewById(R.id.textview_exercise_name))
                .getText().toString();
        String exerciseMuscle = ((TextView) view.findViewById(R.id.textview_exercise_muscle))
                .getText().toString();

        String[] menuItems = getResources().getStringArray(R.array.exercise_menu);
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];

        switch (menuItemName) {
            // Todo: implement edit stuff
            case "Edit":
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_LONG).show();
                break;
            case "Delete":
                new DeleteExerciseTask(this).execute(exerciseName, exerciseMuscle);
                break;
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                DatabaseContract.ExerciseEntry.CONTENT_URI,
                EXERCISE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mExerciseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mExerciseAdapter.swapCursor(null);
    }

}
