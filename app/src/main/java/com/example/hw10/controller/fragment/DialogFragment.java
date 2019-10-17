package com.example.hw10.controller.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw10.MyDialogCloseListener;
import com.example.hw10.R;
import com.example.hw10.model.Repository;
import com.example.hw10.model.State;
import com.example.hw10.model.Task;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends androidx.fragment.app.DialogFragment {
    public static final String EXTRA_USER_ID = "userId";
    private EditText mEditTextName;
    private EditText mEditTextDescription;
    private MaterialButton mButtonSetDate;
    private MaterialButton mButtonSetTime;
    private MaterialButton mButtonDone;
    private RadioGroup mRadioGroupState;
    private Repository mRepository;

    private Long userId;

    public DialogFragment() {
        // Required empty public constructor
    }

    public static DialogFragment newInstance(Long userId) {

        Bundle args = new Bundle();
        args.putLong(EXTRA_USER_ID, userId);
        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
//        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener) activity).handleDialogClose(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        // setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        userId = getArguments().getLong(EXTRA_USER_ID);
        mRepository = Repository.getInstance();
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

        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task();
                String taskName = mEditTextName.getText().toString().trim();
                String taskDescription = mEditTextDescription.getText().toString().trim();

                if (!taskName.isEmpty() || mRadioGroupState.getCheckedRadioButtonId() != -1) {
                    task.setMName(taskName);
                    task.setMDescription(taskDescription);
                    task.setUserId(userId);
                    int radioButtonId = mRadioGroupState.getCheckedRadioButtonId();
                    switch (radioButtonId) {
                        case R.id.todo_radio_button:
                            task.setMState(State.TODO);
                            break;
                        case R.id.doing_radio_button:
                            task.setMState(State.DOING);
                            break;
                        case R.id.done_radio_button:
                            task.setMState(State.DONE);
                            break;
                    }
                    mRepository.insertTask(task);
                    dismiss();
                }
            }
        });

        return view;
    }

    private void initUi(View view) {
        mEditTextName = view.findViewById(R.id.set_name_edit);
        mEditTextDescription = view.findViewById(R.id.set_description_edit);
        mButtonSetDate = view.findViewById(R.id.set_date_button);
        mButtonSetTime = view.findViewById(R.id.set_time_button);
        mButtonDone = view.findViewById(R.id.done_button);
        mRadioGroupState = view.findViewById(R.id.radio_group_dialog);
    }

}
