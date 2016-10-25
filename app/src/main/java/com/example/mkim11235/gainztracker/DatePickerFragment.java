package com.example.mkim11235.gainztracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by Michael on 10/18/2016.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /**
     * Create new datepicker with date selected from arg bundle
     * @param savedInstanceState saved instance state
     * @return new datepickerdialog with date selected from bundle
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        int year = args.getInt(getString(R.string.EXTRA_YEAR));
        int month = args.getInt(getString(R.string.EXTRA_MONTH));
        int day = args.getInt(getString(R.string.EXTRA_DAY));

        return new DatePickerDialog(getActivity(), this, year, month - 1, day);
    }

    /**
     * Updated the date on the ExerciseHistoryEntryFrag edittext to selected date
     * @param datePicker datepicker object
     * @param year year to set edittext to
     * @param month month to set edittext to
     * @param day day to set edittext to
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        int actualMonth = month + 1;
        String formattedDate = Utility.formatDateReadable(year, actualMonth, day);

        TextView textViewDate = (TextView) getActivity()
                .findViewById(R.id.edittext_exercise_history_entry_date);

        textViewDate.setText(formattedDate);
    }
}
