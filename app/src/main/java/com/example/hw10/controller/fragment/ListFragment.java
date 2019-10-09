package com.example.hw10.controller.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw10.R;
import com.example.hw10.controller.activity.MainActivity;
import com.example.hw10.exception.TaskNotExistException;
import com.example.hw10.model.Repository;
import com.example.hw10.model.State;
import com.example.hw10.model.Task;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    public static final String USER_ID = "userId";
    public static final String TAG = "ListFragment";
    public static final String DATE_PICKER_FRAGMENT_TAG = "DatePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    private Long userId;
    private int lastPosition;

    private ImageView emptyListIcon;
    private RecyclerView mRecyclerViewTask;
    private TaskAdapter mTaskAdapter;
    private FloatingActionButton mFloatingActionButton;
    private List<Task> mTaskList;
    private Repository mRepository;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRepository = Repository.getInstance();
        getExtras();
        showSubtitle();
        mTaskList = mRepository.getTaskList(userId);
    }

    private void showSubtitle() {
        String subtitleMessage = "Hi " + mRepository.getUser(userId).getMUserName();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitleMessage);

    }

    private void getExtras() {
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getLong(USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initUi(view);
        updateUi();
        initFloatingButtonAddTask(view);
        return view;
    }

    private void initUi(View view) {
        mFloatingActionButton = view.findViewById(R.id.floating_button_add);
        emptyListIcon = view.findViewById(R.id.empty_list_icon);
        mRecyclerViewTask = view.findViewById(R.id.recycler_view);
        mRecyclerViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mTabLayout = view.findViewById(R.id.tab_layout);
        //mViewPager = view.findViewById(R.id.view_pager);

    }

    private void initFloatingButtonAddTask(View view) {
        addTaskDialog(view);
    }

    private void addTaskDialog(View view) {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View tempView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);

                final AlertDialog addTaskDialog = new AlertDialog.Builder(getActivity()).create();
                addTaskDialog.setView(tempView);
                addTaskDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText taskNameEditText = tempView.findViewById(R.id.set_name_edit);
                        EditText descriptionEditText = tempView.findViewById(R.id.set_description_edit);
                        MaterialButton setDateButton = tempView.findViewById(R.id.set_date_button);
                        Button setTimeButton = tempView.findViewById(R.id.set_time_button);
                        //ImageView deleteIcon = tempView.findViewById(R.id.delete_icon);
                        //deleteIcon.setVisibility(View.INVISIBLE);
                        String nameTask = taskNameEditText.getText().toString().trim();
                        String description = descriptionEditText.getText().toString().trim();

                        setDateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getActivity(), "heyyyyyyy", Toast.LENGTH_SHORT).show();
                                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
                                datePickerFragment.setTargetFragment(ListFragment.this, REQUEST_CODE_DATE_PICKER);
                                Log.d(TAG, "I'm here");
                                datePickerFragment.show(getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
                            }
                        });

                        RadioGroup mRadioGroup = tempView.findViewById(R.id.radio_group_dialog);
                        if (mRadioGroup.getCheckedRadioButtonId() != -1) {
                            int radioButtonId = mRadioGroup.getCheckedRadioButtonId();
                            State state = null;
                            switch (radioButtonId) {
                                case R.id.todo_radio_button:
                                    state = State.TODO;
                                    break;
                                case R.id.doing_radio_button:
                                    state = State.DOING;
                                    break;
                                case R.id.done_radio_button:
                                    state = State.DONE;
                                    break;
                            }
                            if (!nameTask.isEmpty()) {
                                emptyListIcon.setVisibility(View.GONE);
                                mRecyclerViewTask.setVisibility(View.VISIBLE);
                                Task task = new Task();
                                task.setUserId(userId);
                                task.setMName(nameTask);
                                task.setMState(state);
                                task.setMDescription(description);
                                task.setMDate(new Date());
                                task.setMTime(new Date().getTime());
                                mRepository.insertTask(task);
                                Log.e(TAG , "last position is ##" + lastPosition);
                                mTaskAdapter.setTasks(mRepository.getTaskList(userId));
                                mTaskAdapter.notifyDataSetChanged();
                            }
                            addTaskDialog.dismiss();
                        }
                    }
                });
                addTaskDialog.show();
            }
        });
    }


  /*  private void startFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_add_task);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.dialog_layout, new AddTaskFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }*/





    @Override
    public void onResume() {
        super.onResume();
        mTaskAdapter.setTasks(mRepository.getTaskList(userId));
        mTaskAdapter.notifyDataSetChanged();
    }

    private void updateUi() {
        mTaskList = mRepository.getTaskList(userId);
        if (mTaskAdapter == null) {
            Log.e(TAG , "Task Adapter is null");
            mTaskAdapter = new TaskAdapter(getActivity() , mTaskList);
            mRecyclerViewTask.setAdapter(mTaskAdapter);
            if (mTaskList.size() != 0){
                emptyListIcon.setVisibility(View.GONE);
                mRecyclerViewTask.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e(TAG , "Task Adapter exist");
            mTaskAdapter.setTasks(mTaskList);
            mTaskAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu_item:
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
            case R.id.delete_all_task_menu_item:
                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setMessage("Do you want to delete all Tasks?");
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Repository.getInstance().deleteAllTask(userId);
                        mTaskAdapter = null;
                        updateUi();
                        mRecyclerViewTask.setVisibility(View.GONE);
                        emptyListIcon.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    public class TaskHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {
        private TextView mTextViewName;
        private TextView mTextViewDate;
        private MaterialLetterIcon mMaterialLetterIcon;

        private EditText mEditTextNameDialog;
        private RadioGroup mRadioGroup;
        private EditText mEditTextDescription;
        private MaterialButton setDate;
        private MaterialButton setTime;
        private ImageView deleteIcon;
        private View tempView;
        private String nameTask;
        private final AlertDialog editTaskDialog = new AlertDialog.Builder(getActivity()).create();

        public TaskHolder(@NonNull final View itemView) {
            super(itemView);
            initUi(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.d("lastPosition", lastPosition + "");
                    lastPosition = getAdapterPosition();
                    // Log.d("lastPosition", lastPosition + "");
                    showEditTaskDialog();
                    //updateUi();



                }
            });

        }

        private void initUi(@NonNull View itemView) {
            tempView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
            mTextViewName = itemView.findViewById(R.id.name_item_task);
            mTextViewDate = itemView.findViewById(R.id.date_item_task);
            mMaterialLetterIcon = itemView.findViewById(R.id.icon_item_task);
        }

        private void showEditTaskDialog() {
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
                            Long id = mRepository.getTaskList(userId).get(lastPosition).getMId();
                            Task task = mRepository.getTask(id);
                            initTaskName(task);
                            initTaskState(task);


                          /*  try {
                                mRepository.updateTask(task);
                            } catch (TaskNotExistException e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }*/
                            Log.e(TAG , "last position is ##" + lastPosition);
                            mTaskAdapter.notifyItemChanged(lastPosition);
                            mTaskAdapter.setTasks(mRepository.getTaskList(userId));
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
            Task tempTask = mRepository.getTaskList(userId).get(lastPosition);
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
            if (!nameTask.isEmpty()){
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
            Long id = mRepository.getTaskList(userId).get(lastPosition).getMId();
            Task deleteTask = mRepository.getTask(id);
            Log.e("TAG4" , deleteTask.getMUser().getMUserName());
            try {
                mRepository.deleteTask(deleteTask);
                mTaskAdapter.setTasks(mRepository.getTaskList(userId));
                mTaskAdapter.notifyDataSetChanged();
                if (mRepository.getTaskList(userId).size() == 0) {
                    emptyListIcon.setVisibility(View.VISIBLE);
                    mRecyclerViewTask.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "this Task does not exist!!!", Toast.LENGTH_SHORT).show();
            }
        }

        public void bindTasks(Task task) {
            mTextViewName.setText(task.getMName());
            mTextViewDate.setText(task.getMDate().toString());
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.todo_radio_button:
                    mRepository.getTaskList(userId).get(lastPosition).setMState(State.TODO);
                    break;
                case R.id.doing_radio_button:
                    mRepository.getTaskList(userId).get(lastPosition).setMState(State.DOING);
                    break;
                case R.id.done_radio_button:
                    mRepository.getTaskList(userId).get(lastPosition).setMState(State.DONE);
                    break;
            }

        }
    }


    public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> mTasks;
        private int[] mMaterialColors;
        private Context mContext;

        public TaskAdapter(Context context , List<Task> tasks) {
            this.mTasks = tasks;
            this.mContext = context;
            this.mMaterialColors = getContext().getResources().getIntArray(R.array.colors);
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_task, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Log.e(TAG, "position#" + position);
            if (position % 2 == 0)
                holder.itemView.setBackgroundColor(Color.rgb(145, 196, 153));
            else
                holder.itemView.setBackgroundColor(Color.rgb(242, 233, 220));

            handleMaterialLetterIcon(holder, position);
            holder.bindTasks(mTasks.get(position));
        }

        private void handleMaterialLetterIcon(@NonNull TaskHolder holder, int position) {
            holder.mMaterialLetterIcon.setInitials(true);
            holder.mMaterialLetterIcon.setInitialsNumber(1);
            holder.mMaterialLetterIcon.setLetterSize(20);
            holder.mMaterialLetterIcon.setShapeColor(mMaterialColors[new Random().nextInt(mMaterialColors.length)]);
            holder.mMaterialLetterIcon.setLetter(mTasks.get(position).getMName());
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }
}//end class
