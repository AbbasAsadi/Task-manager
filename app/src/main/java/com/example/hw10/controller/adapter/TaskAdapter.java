package com.example.hw10.controller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw10.R;
import com.example.hw10.controller.fragment.DialogFragment;
import com.example.hw10.model.State;
import com.example.hw10.utility.StateConverter;
import com.example.hw10.model.Task;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> implements Filterable {
    private static final String TAG = "taskAdapter";
    private List<Task> mTaskListFiltered;
    private int[] mMaterialColors;
    private Context mContext;
    private List<Task> mTaskList;
    private Integer stateValue;
   private int lastPosition;

    public TaskAdapter(Context context, List<Task> tasks , State state) {
        this.mTaskListFiltered = tasks;
        this.mTaskList = tasks;
        this.mContext = context;
        this.mMaterialColors = context.getResources().getIntArray(R.array.colors);
        StateConverter stateConverter = new StateConverter();
        stateValue = stateConverter.convertToDatabaseValue(state);
    }


    public void setTasks(List<Task> tasks) {
        mTaskList = tasks;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_task, parent, false) , mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Log.e(TAG, "position#" + position);
        if (position % 2 == 0)
            holder.itemView.setBackgroundColor(Color.rgb(145, 196, 153));
        else
            holder.itemView.setBackgroundColor(Color.rgb(242, 233, 220));

        handleMaterialLetterIcon(holder, position);
        //holder.bindTasks(mTasks.get(position));
        holder.bindTasks(mTaskListFiltered.get(position));

    }

    private void handleMaterialLetterIcon(@NonNull TaskHolder holder, int position) {
        holder.mMaterialLetterIcon.setInitials(true);
        holder.mMaterialLetterIcon.setInitialsNumber(1);
        holder.mMaterialLetterIcon.setLetterSize(20);
        holder.mMaterialLetterIcon.setShapeColor(mMaterialColors[new Random().nextInt(mMaterialColors.length)]);
        holder.mMaterialLetterIcon.setLetter(mTaskList.get(position).getMName());
    }

    @Override
    public int getItemCount() {
        //return mTasks.size();
        return mTaskListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String tmpString = charSequence.toString();
                if (tmpString.isEmpty()) {
                    mTaskListFiltered = mTaskList;
                } else {
                    List<Task> filteredList = new ArrayList<>();
                    for (Task task : mTaskList) {
                        if (task.getMName().toLowerCase().contains(tmpString.toLowerCase()) ||
                                task.getMDescription().toLowerCase().contains(tmpString.toLowerCase())) {
                            filteredList.add(task);
                        }
                    }
                    mTaskListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mTaskListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTaskListFiltered = (ArrayList<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public class TaskHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {
//        private final AlertDialog editTaskDialog = new AlertDialog.Builder(getActivity()).create();
        private TextView mTextViewName;
        private TextView mTextViewDate;
        private MaterialLetterIcon mMaterialLetterIcon;
        private EditText mEditTextNameDialog;
        private RadioGroup mRadioGroup;
        private EditText mEditTextDescription;
        private MaterialButton setDate;
        private MaterialButton setTime;
        private ImageView deleteIcon;
        // private View tempView;
        private String nameTask;

        public TaskHolder(@NonNull final View itemView , Context context) {
            super(itemView);
            initUi(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.d("lastPosition", lastPosition + "");
                    lastPosition = getAdapterPosition();
                    // Log.d("lastPosition", lastPosition + "");
                    // showEditTaskDialog();
                    //updateUi();


                }
            });

        }

        private void initUi(@NonNull View itemView) {
            // tempView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
            mTextViewName = itemView.findViewById(R.id.name_item_task);
            mTextViewDate = itemView.findViewById(R.id.date_item_task);
            mMaterialLetterIcon = itemView.findViewById(R.id.icon_item_task);
        }

       /* private void showEditTaskDialog() {
            //final AlertDialog editTaskDialog = new AlertDialog(getActivity());
            editTaskDialog.setView(tempView);
            initUiEditTaskDialog();
            initDefaultValue();
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteTaskDialog();
                    editTaskDialog.dismiss();
                }
            });
            editTaskDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Long id = mRepository.getTaskList(userId, State.TODO).get(lastPosition).getMId();
                            Task task = mRepository.getTask(id);
                            initTaskName(task);
                            initTaskState(task);


                          *//*  try {
                                mRepository.updateTask(task);
                            } catch (TaskNotExistException e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }*//*
                            Log.e(TAG, "last position is ##" + lastPosition);
                            mTaskAdapter.notifyItemChanged(lastPosition);
                            mTaskAdapter.setTasks(mRepository.getTaskList(userId, State.TODO));
                            editTaskDialog.dismiss();
                        }
                    });
            editTaskDialog.show();
        }

        private void initUiEditTaskDialog() {
            mEditTextNameDialog = tempView.findViewById(R.id.set_name_edit);
            mRadioGroup = tempView.findViewById(R.id.radio_group_dialog);
            mEditTextDescription = tempView.findViewById(R.id.set_description_edit);
            setDate = tempView.findViewById(R.id.set_date_button);
            setTime = tempView.findViewById(R.id.set_time_button);
            deleteIcon = tempView.findViewById(R.id.delete_icon);
            deleteIcon.setVisibility(View.VISIBLE);
        }

        private void initDefaultValue() {
            Task tempTask = mRepository.getTaskList(userId, State.TODO).get(lastPosition);
            mEditTextNameDialog.setText(tempTask.getMName());
            mEditTextDescription.setText(tempTask.getMDescription());
            setDate.setText(tempTask.getMDate().toString());
            //setTime.setText((int) tempTask.getTime());
            State state = tempTask.getMState();
            if (state == State.TODO) {
                mRadioGroup.check(R.id.todo_radio_button);

            } else if (state == State.DOING) {
                mRadioGroup.check(R.id.doing_radio_button);

            } else if (state == State.DONE) {
                mRadioGroup.check(R.id.done_radio_button);
            }
        }

        private void initTaskName(Task task) {
            nameTask = mEditTextNameDialog.getText().toString();
            if (!nameTask.isEmpty()) {
                try {
                    task.setMName(nameTask);
                    mRepository.updateTask(task);
                } catch (TaskNotExistException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }

        private void initTaskState(Task task) {
            if (mRadioGroup.getCheckedRadioButtonId() != -1) {
                int radioButtonId = mRadioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.todo_radio_button:
                        task.setMState(State.TODO);
                        try {
                            mRepository.updateTask(task);
                        } catch (TaskNotExistException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.doing_radio_button:
                        task.setMState(State.DOING);
                        try {
                            mRepository.updateTask(task);
                        } catch (TaskNotExistException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.done_radio_button:
                        task.setMState(State.DONE);
                        try {
                            mRepository.updateTask(task);
                        } catch (TaskNotExistException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }

        }

        private void showDeleteTaskDialog() {
            final AlertDialog editTaskDialog = new AlertDialog.Builder(getActivity()).create();
            editTaskDialog.setMessage("Do you want to delete this task?");
            editTaskDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteRow();
                    editTaskDialog.dismiss();
                }
            });
            editTaskDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editTaskDialog.dismiss();
                }
            });
            editTaskDialog.show();
        }

        private void deleteRow() {
            Long id = mRepository.getTaskList(userId, State.TODO).get(lastPosition).getMId();
            Task deleteTask = mRepository.getTask(id);
            Log.e("TAG4", deleteTask.getMUser().getMUserName());
            try {
                mRepository.deleteTask(deleteTask);
                mTaskAdapter.setTasks(mRepository.getTaskList(userId, State.TODO));
                mTaskAdapter.notifyDataSetChanged();
                if (mRepository.getTaskList(userId, State.TODO).size() == 0) {
                    emptyListIcon.setVisibility(View.VISIBLE);
                    mRecyclerViewTask.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "this Task does not exist!!!", Toast.LENGTH_SHORT).show();
            }
        }*/

        public void bindTasks(Task task) {
            mTextViewName.setText(task.getMName());
            mTextViewDate.setText(DialogFragment.convertTime(task.getMDate() , 0));
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.todo_radio_button:
                    //mRepository.getTaskList(userId, stateValue).get(lastPosition).setMState(State.TODO);
                    mTaskList.get(lastPosition).setMState(State.TODO);
                    break;
                case R.id.doing_radio_button:
                    //mRepository.getTaskList(userId, stateValue).get(lastPosition).setMState(State.DOING);
                    mTaskList.get(lastPosition).setMState(State.DOING);
                    break;
                case R.id.done_radio_button:
                    //mRepository.getTaskList(userId, stateValue).get(lastPosition).setMState(State.DONE);
                   mTaskList.get(lastPosition).setMState(State.DONE);
                    break;
            }

        }
    }

}
