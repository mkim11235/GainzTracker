package com.example.mkim11235.gainztracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Michael on 10/18/2016.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    //do smt with chosen date
    // maybe send intent back to history entry activity of date to add
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        int actualMonth = month + 1;
        String formattedDate = Utility.formatDateReadable(year, actualMonth, day);

        TextView textViewDate = (TextView) getActivity()
                .findViewById(R.id.edittext_exercise_history_entry_date);

        textViewDate.setText(formattedDate);
    }
}
