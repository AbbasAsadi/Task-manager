package com.example.hw10.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw10.R;
import com.example.hw10.controller.fragment.DetailDialogFragment;
import com.example.hw10.controller.fragment.DialogAddEditFragment;
import com.example.hw10.model.State;
import com.example.hw10.model.Task;
import com.example.hw10.utility.StateConverter;
import com.github.ivbaranov.mli.MaterialLetterIcon;

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
    private FragmentManager mFragmentManager;

    public TaskAdapter(Context context, List<Task> tasks, FragmentManager supportFragmentManager, State state) {
        this.mTaskListFiltered = tasks;
        this.mTaskList = tasks;
        this.mContext = context;
        this.mMaterialColors = context.getResources().getIntArray(R.array.colors);
        StateConverter stateConverter = new StateConverter();
        stateValue = stateConverter.convertToDatabaseValue(state);
        mFragmentManager = supportFragmentManager;
    }


    public void setTasks(List<Task> tasks) {
        mTaskList = tasks;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_task, parent, false), mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Log.e(TAG, "position#" + position);
        if (position % 2 == 0)
            holder.itemView.setBackgroundColor(Color.rgb(216, 178, 88));
        else
            holder.itemView.setBackgroundColor(Color.rgb(113, 183, 135));

        handleMaterialLetterIcon(holder, position);
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
        private TextView mTextViewName;
        private TextView mTextViewDate;
        private MaterialLetterIcon mMaterialLetterIcon;
        private ImageView mImageViewShare;
        private Context mContext;

        public TaskHolder(@NonNull final View itemView, Context context) {
            super(itemView);
            initUi(itemView);
            mContext = context;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("lastPosition", lastPosition + "");
                    lastPosition = getAdapterPosition();
                    Log.d("lastPosition", lastPosition + "");
                    Long taskId = mTaskListFiltered.get(lastPosition).getMId();
                    showEditTaskDialog(taskId);

                    mImageViewShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, mTaskListFiltered.get(lastPosition).toString());
                            intent.setType("text/plain");
                            Intent intentFilter = Intent.createChooser(intent, mContext.getString(R.string.chooser));
                            mContext.startActivity(intentFilter);
                        }
                    });


                }
            });

        }

        private void initUi(@NonNull View itemView) {
            mTextViewName = itemView.findViewById(R.id.name_item_task);
            mTextViewDate = itemView.findViewById(R.id.date_item_task);
            mMaterialLetterIcon = itemView.findViewById(R.id.icon_item_task);
            mImageViewShare = itemView.findViewById(R.id.share_item_task);
        }

        private void showEditTaskDialog(Long taskId) {
            DetailDialogFragment fragment = DetailDialogFragment.newInstance(taskId);
            fragment.show(mFragmentManager, "editDialog");
        }

        public void bindTasks(Task task) {
            mTextViewName.setText(task.getMName());
            mTextViewDate.setText(DialogAddEditFragment.convertTime(task.getMDate(), 0));
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.todo_radio_button:
                    mTaskList.get(lastPosition).setMState(State.TODO);
                    break;
                case R.id.doing_radio_button:
                    mTaskList.get(lastPosition).setMState(State.DOING);
                    break;
                case R.id.done_radio_button:
                    mTaskList.get(lastPosition).setMState(State.DONE);
                    break;
            }
        }
    }

}
