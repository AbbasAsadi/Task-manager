package com.example.hw10.controller.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.hw10.R;
import com.example.hw10.exception.TaskNotExistException;
import com.example.hw10.model.Repository;
import com.example.hw10.model.Task;
import com.example.hw10.utility.MyDialogCloseListener;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailDialogFragment extends DialogFragment {
    public static final String TAG = "DetailDialogFragment";
    public static final String EXTRA_TASK_ID = "taskId";
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private TextView mTextViewState;
    private ImageView mImageViewTask;
    private MaterialButton mButtonEdit;
    private MaterialButton mButtonDelete;
    private MaterialButton mButtonShare;
    private MaterialButton mButtonDone;
    private Long mTaskId;
    private Repository mRepository;
    private Task mTask;

    public DetailDialogFragment() {
        // Required empty public constructor
    }

    public static DetailDialogFragment newInstance(Long taskId) {

        Bundle args = new Bundle();
        args.putLong(EXTRA_TASK_ID, taskId);
        DetailDialogFragment fragment = new DetailDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = Repository.getInstance();
        mTaskId = getArguments().getLong(EXTRA_TASK_ID);
        mTask = mRepository.getTask(mTaskId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initUi(view);
        SetValue();

        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to delete this task? ")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    mRepository.deleteTask(mTask);
                                    Toast.makeText(getActivity(), "delete task was successful", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                } catch (TaskNotExistException e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("NO", null)
                        .create();
                alertDialog.show();
            }
        });
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "task name: " + mTask.getMName() +
                "\ntask description: " + mTask.getMDescription() +
                "\nin Date: " + DialogAddEditFragment.convertTime(mTask.getMDate() , 0) +
                "\nin time: " + DialogAddEditFragment.convertTime(null , mTask.getMTime()) +
                "\n in state :" + mTask.getMState().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.setType("text/plain");
                Intent intentFilter = Intent.createChooser(intent, getString(R.string.chooser));
                startActivity(intentFilter);
            }
        });
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddEditFragment fragment = DialogAddEditFragment.newInstance(mTaskId, false);
                fragment.show(getFragmentManager(), "editDialog");
                dismiss();
            }
        });
        return view;
    }

    private void SetValue() {
        mTextViewTitle.setText(mTask.getMName());
        mTextViewDescription.setText(mTask.getMDescription());
        mTextViewDate.setText(DialogAddEditFragment.convertTime(mTask.getMDate(), 0));
        mTextViewTime.setText(DialogAddEditFragment.convertTime(null, mTask.getMTime()));
        mTextViewState.setText(mTask.getMState().toString());

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
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

    private void initUi(View view) {
        mTextViewTitle = view.findViewById(R.id.title_detail);
        mTextViewDescription = view.findViewById(R.id.description_detail);
        mTextViewDate = view.findViewById(R.id.date_detail);
        mTextViewTime = view.findViewById(R.id.time_detail);
        mTextViewState = view.findViewById(R.id.state_detail);
        mButtonEdit = view.findViewById(R.id.edit_button_detail);
        mButtonDelete = view.findViewById(R.id.delete_button_detail);
        mButtonDone = view.findViewById(R.id.done_button_detail);
        mButtonShare = view.findViewById(R.id.share_button_detail);
    }
}
