package com.example.michaelbettis_term_scheduler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle setMinDate = this.getArguments();
        long startDate = Objects.requireNonNull(setMinDate).getLong("setStartDate");

        Bundle setMaxDate = this.getArguments();
        long endDate = setMaxDate.getLong("setEndDate");

        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(requireActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);

        if (endDate == -1) {
            //sets the minDate
            datePicker.getDatePicker().setMinDate(startDate);

        } else if (startDate == -1) {
            //sets the maxDate
            datePicker.getDatePicker().setMaxDate(endDate);

        } else {

            //sets the minDate
            datePicker.getDatePicker().setMinDate(startDate);

            //sets the maxDate
            datePicker.getDatePicker().setMaxDate(endDate);

        }

        return datePicker;
    }
}
