package com.example.mkim11235.gainztracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mkim11235.gainztracker.events.ExerciseClickedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container_activity_main,
                    new ExerciseFragment(), getString(R.string.FRAGMENT_TAG_EXERCISE)).commit();
        }

        // Dunno about setting title here
        //setTitle(getString(R.string.title_main_activity));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ExerciseClickedEvent event) {
        ExerciseHistoryFragment exerciseHistoryFragment =
                ExerciseHistoryFragment.newInstance(event.getExerciseId());

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_activity_main, exerciseHistoryFragment,
                getString(R.string.FRAGMENT_TAG_EXERCISE_HISTORY));
        ft.addToBackStack(null);
        ft.commit();
    }
}
