package com.example.hw10.controller.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw10.R;
import com.example.hw10.exception.TaskNotExistException;
import com.example.hw10.model.Repository;
import com.example.hw10.model.State;
import com.example.hw10.model.Task;
import com.example.hw10.utility.MyDialogCloseListener;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogAddEditFragment extends androidx.fragment.app.DialogFragment {
    public static final String TAG = "DialogAddEditFragment";
    public static final String EXTRA_USER_ID = "userId";
    public static final String DATE_PICKER_FRAGMENT_TAG = "DatePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    public static final String TASK_ID = "taskId";
    private static final String TIME_PICKER_FRAGMENT_TAG = "TimePicker";
    private EditText mEditTextName;
    private EditText mEditTextDescription;
    private MaterialButton mButtonSetDate;
    private MaterialButton mButtonSetTime;
    private MaterialButton mButtonDone;
    private MaterialButton mButtonCancel;
    private RadioGroup mRadioGroupState;
    private Repository mRepository;
    private Task mCurrentTask;
    private Long userId = 0L;
    private Long taskId = 0L;

    public DialogAddEditFragment() {
        // Required empty public constructor
    }

    public static DialogAddEditFragment newInstance(Long userId) {

        Bundle args = new Bundle();
        args.putLong(EXTRA_USER_ID, userId);
        DialogAddEditFragment fragment = new DialogAddEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogAddEditFragment newInstance(Long taskId, boolean empty) {

        Bundle args = new Bundle();
        args.putLong(TASK_ID, taskId);
        DialogAddEditFragment fragment = new DialogAddEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String convertTime(Date date, long time) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR) +
                    " / " +
                    calendar.get(Calendar.MONTH) +
                    " / " +
                    calendar.get(Calendar.DAY_OF_MONTH);
        } else if (time != 0) {
            calendar.setTimeInMillis(time);
            return calendar.get(Calendar.HOUR_OF_DAY) +
                    " : " +
                    calendar.get(Calendar.MINUTE);
        }
        return "not Match";
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss");
        Activity activity = getActivity();
        if (activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener) activity).handleDialogClose(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setStyle(DialogAddEditFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        Bundle bundle = getArguments();
        if (bundle.containsKey(EXTRA_USER_ID))
            userId = bundle.getLong(EXTRA_USER_ID);
        if (bundle.containsKey(TASK_ID))
            taskId = bundle.getLong(TASK_ID);
        mRepository = Repository.getInstance();
        mCurrentTask = new Task();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        initUi(view);
        if (taskId != 0) {
            initDefaultValue();
        }
        mButtonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
                timePickerFragment.setTargetFragment(DialogAddEditFragment.this, REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getFragmentManager(), TIME_PICKER_FRAGMENT_TAG);
            }
        });


        mButtonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
                //create parent-child between CrimeDetailFragment and DatePickerFragment
                datePickerFragment.setTargetFragment(DialogAddEditFragment.this, REQUEST_CODE_DATE_PICKER);

                datePickerFragment.show(getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = mEditTextName.getText().toString().trim();
                String taskDescription = mEditTextDescription.getText().toString().trim();
                if (taskId != 0){
                    mCurrentTask = mRepository.getTask(taskId);
                }
                if (!taskName.isEmpty() || mRadioGroupState.getCheckedRadioButtonId() != -1) {
                    mCurrentTask.setMName(taskName);
                    mCurrentTask.setMDescription(taskDescription);
                    mCurrentTask.setUserId(userId);
                    int radioButtonId = mRadioGroupState.getCheckedRadioButtonId();
                    switch (radioButtonId) {
                        case R.id.todo_radio_button:
                            mCurrentTask.setMState(State.TODO);
                            break;
                        case R.id.doing_radio_button:
                            mCurrentTask.setMState(State.DOING);
                            break;
                        case R.id.done_radio_button:
                            mCurrentTask.setMState(State.DONE);
                            break;
                    }
                    if (userId != 0) {
                        mRepository.insertTask(mCurrentTask);
                    } else if (taskId != 0) {
                        try {
                            mRepository.updateTask(mCurrentTask);
                        } catch (TaskNotExistException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    dismiss();
                }
            }
        });

        return view;
    }

    private void initDefaultValue() {
        Task task = mRepository.getTask(taskId);
        mEditTextName.setText(task.getMName());
        mEditTextDescription.setText(task.getMDescription());
        mButtonSetDate.setText(convertTime(task.getMDate(), 0));
        mButtonSetTime.setText(convertTime(null, task.getMTime()));
        switch (task.getMState().getI()) {
            case 0:
                mRadioGroupState.check(R.id.todo_radio_button);
                break;
            case 1:
                mRadioGroupState.check(R.id.doing_radio_button);
                break;
            case 2:
                mRadioGroupState.check(R.id.done_radio_button);
                break;
        }
    }

    private void initUi(View view) {
        mEditTextName = view.findViewById(R.id.set_name_edit);
        mEditTextDescription = view.findViewById(R.id.set_description_edit);
        mButtonSetDate = view.findViewById(R.id.set_date_button);
        mButtonSetTime = view.findViewById(R.id.set_time_button);
        mButtonDone = view.findViewById(R.id.done_button);
        mButtonCancel = view.findViewById(R.id.cancel_button);
        mRadioGroupState = view.findViewById(R.id.radio_group_dialog);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date date = (Date) data.getSerializableExtra(
                    DatePickerFragment.getExtraTaskDate());
            mCurrentTask.setMDate(date);
            mButtonSetDate.setText(convertTime(date, 0));
        }
        if (requestCode == REQUEST_CODE_TIME_PICKER) {
            long time = data.getLongExtra(
                    TimePickerFragment.getExtraTaskTIME(), 0);
            mCurrentTask.setMTime(time);
            mButtonSetTime.setText(convertTime(null, time));
        }
    }
}
