package com.example.mkim11235.gainztracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        String workout = intent.getStringExtra(Intent.EXTRA_TEXT);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(workout);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_workout);
        layout.addView(textView);
    }
}
