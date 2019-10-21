package com.example.hw10.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.hw10.R;
import com.example.hw10.controller.fragment.DialogFragment;
import com.example.hw10.controller.fragment.DoingFragment;
import com.example.hw10.controller.fragment.DoneFragment;
import com.example.hw10.controller.fragment.TodoFragment;
import com.example.hw10.controller.adapter.ViewPagerAdapter;
import com.example.hw10.model.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ListActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "userId";
    public static final String DATE_PICKER_FRAGMENT_TAG = "DatePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    private Long userId;
    //private ListFragment listFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton mFloatingActionButton;
    private Repository mRepository;
    private ImageView emptyListIcon;
    private RecyclerView mRecyclerViewTask;

    public static Intent newIntent(Context context, Long userId) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initUi();
        mRepository = Repository.getInstance();
        userId = (Long) getIntent().getSerializableExtra(EXTRA_USER_ID);
        Log.e("TAG1" , "in listActivity userId:#" + userId);
        //listFragment = new ListFragment();
        //manageOutputIntent();
        addTabs(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabTitles();

        initFloatingButtonAddTask();


        //startFragment();
    }

    private void initFloatingButtonAddTask() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = DialogFragment.newInstance(userId);
                fragment.show(getSupportFragmentManager() , "dialogFragment");
              /*  FragmentManager fragmentManager = getSupportFragmentManager();

                //DialogFragment fragment = fragmentManager.findFragmentById(R.id.fragment_dialog);
               // if (fragment == null) {
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.activity_list, DialogFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
               /// }*/
            }
        });

       //addTaskDialog();
    }

   /* private void addTaskDialog() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final View tempView = LayoutInflater.inflate(R.layout.dialog_layout, null);
                final View tempView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_layout , null);

                final AlertDialog addTaskDialog = new AlertDialog.Builder(getApplicationContext()).create();
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
*//*
                        setDateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(getActivity(), "heyyyyyyy", Toast.LENGTH_SHORT).show();
                                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
                                datePickerFragment.setTargetFragment(ListFragment.newInstance(userId), REQUEST_CODE_DATE_PICKER);
                                //Log.d(TAG, "I'm here");
                                datePickerFragment.show(getSupportFragmentManager() , DATE_PICKER_FRAGMENT_TAG);
                            }
                        });*//*

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
                                //Log.e(TAG, "last position is ##" + lastPosition);
                               *//* mTaskAdapter.setTasks(mRepository.getTaskList(userId));
                                mTaskAdapter.notifyDataSetChanged();*//*
                            }
                            addTaskDialog.dismiss();
                        }
                    }
                });
                addTaskDialog.show();
            }
        });
    }*/

    private void initUi() {
        emptyListIcon = findViewById(R.id.empty_list_icon);
        mRecyclerViewTask = findViewById(R.id.recycler_view);
        mFloatingActionButton = findViewById(R.id.floating_button_add);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
    }

    private void setupTabTitles() {
        tabLayout.getTabAt(0).setText("TODO");
        tabLayout.getTabAt(1).setText("DOING");
        tabLayout.getTabAt(2).setText("DONE");
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TodoFragment.newInstance(userId) , "TodoFragment");
        adapter.addFragment(DoingFragment.newInstance(userId), "DoingFragment");
        adapter.addFragment(DoneFragment.newInstance(userId) , "DoneFragment");
        viewPager.setAdapter(adapter);

      /*  adapter.addFragment(new TodoFragment(), "TODO");
        adapter.addFragment(new DoingFragment(), "DOING");
        adapter.addFragment(new DoneFragment(), "DONE");*/
        //viewPager.setCurrentItem(0);
        //viewPager.setOffscreenPageLimit(3);
    }

/*    private void startFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_list);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_list, listFragment)
                    .commit();
        }
    }*/


    /*private void manageOutputIntent() {
        Bundle args = new Bundle();
        args.putLong(EXTRA_USER_ID, userId);
        listFragment.setArguments(args);
    }*/
}
