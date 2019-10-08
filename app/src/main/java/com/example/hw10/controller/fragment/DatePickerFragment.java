package com.example.hw10.controller.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.hw10.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {
    private static final String EXTRA_TASK_DATE = "com.example.HW9.taskDate";
    private static final String ARG_TASK_DATE = "taskDate";

    private DatePicker mDatePicker;
    private Date mDate;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_TASK_DATE);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null, false);
        mDatePicker = view.findViewById(R.id.fragment_date_picker);

        initDatePicker();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Date of crime:")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date date = extractDate();
                        sendResult(date);
                    }
                })
                .create();
    }

    private Date extractDate() {
        int year = mDatePicker.getYear();
        int monthOfYear = mDatePicker.getMonth();
        int dayOfMonth = mDatePicker.getDayOfMonth();

        GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        return calendar.getTime();
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, monthOfYear, dayOfMonth, null);
    }

    private void sendResult(Date date) {
        Fragment fragment = getTargetFragment();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TASK_DATE, date);
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
