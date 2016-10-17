package com.example.mkim11235.gainztracker;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ImageButton mAddExerciseButton;
    private ExerciseAdapter mExerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TO DO: Need to change after learning Loaders
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(DatabaseContract.ExerciseEntry.CONTENT_URI,
                null, null, null, null);
        mExerciseAdapter = new ExerciseAdapter(this, cursor, 0);

        ListView listView = (ListView) findViewById(R.id.listview_exercises);
        listView.setAdapter(mExerciseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                long id = mExerciseAdapter.getItemId(position);
                Intent intent = new Intent(view.getContext(), ExerciseActivity.class).putExtra(Intent.EXTRA_TEXT, id);
                startActivity(intent);
            }
        });

        // Add Exercise Button onClick starts AddExercise Activity
        mAddExerciseButton = (ImageButton) findViewById(R.id.image_button_add_exercise);
        mAddExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddExerciseActivity.class);
                startActivity(intent);
            }
        });
    }
}
