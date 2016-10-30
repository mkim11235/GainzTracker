package com.example.mkim11235.gainztracker;

import android.os.Handler;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Michael on 10/21/2016.
 * Borrows from https://github.com/anagri/AndroidLearnings/blob/master/app/src/main/java/me/creativei/ContinuousLongClickListenerActivity.java
 */

/**
 * Continuous onclicklistener for increment/decrement buttons
 */
public class ContinuousLongClickListener implements View.OnTouchListener, View.OnLongClickListener {

    private static final int DELAY = 333;

    private View.OnLongClickListener mOnLongClickListener;
    private Handler mHandler;
    private Runnable mRunnable;

    public ContinuousLongClickListener(final View view, Handler handler, View.OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
        mHandler = handler;

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mOnLongClickListener.onLongClick(view);
                mHandler.postDelayed(this, DELAY);
            }
        };

        view.setOnLongClickListener(this);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onLongClick(final View view) {
        mHandler.postDelayed(mRunnable, DELAY);
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            mHandler.removeCallbacks(mRunnable);
        }
        return false;
    }
}
