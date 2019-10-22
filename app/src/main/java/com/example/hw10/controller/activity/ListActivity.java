package com.example.hw10.controller.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.hw10.R;
import com.example.hw10.controller.adapter.ViewPagerAdapter;
import com.example.hw10.controller.fragment.DialogFragment;
import com.example.hw10.controller.fragment.DoingFragment;
import com.example.hw10.controller.fragment.DoneFragment;
import com.example.hw10.controller.fragment.TodoFragment;
import com.example.hw10.model.Repository;
import com.example.hw10.utility.MyDialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ListActivity extends AppCompatActivity implements MyDialogCloseListener {
    public static final String EXTRA_USER_ID = "userId";
    public static final String DATE_PICKER_FRAGMENT_TAG = "DatePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    private static final String TAG = "ListActivity";
    private Long userId;
    //private ListFragment listFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton mFloatingActionButton;
    private Repository mRepository;
    private ImageView emptyListIcon;
    // private FragmentStatePagerAdapter mViewPagerAdapter;
    /*private TaskAdapter mTaskAdapter;
    private RecyclerView mRecyclerViewTask;*/

    public static Intent newIntent(Context context, Long userId) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

/*    public TaskAdapter getTaskAdapter() {
        return mTaskAdapter;
    }

    public void setTaskAdapter(TaskAdapter taskAdapter) {
        mTaskAdapter = taskAdapter;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initUi();
        mRepository = Repository.getInstance();
        userId = (Long) getIntent().getSerializableExtra(EXTRA_USER_ID);
        Log.e("TAG1", "in listActivity userId:#" + userId);
        //listFragment = new ListFragment();
        //manageOutputIntent();
        viewPagerJob(-2);

        initFloatingButtonAddTask();


        //startFragment();
    }

    private void viewPagerJob(int lastItem) {
        addTabs(viewPager, lastItem);
        tabLayout.setupWithViewPager(viewPager);
        setupTabTitles();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void initFloatingButtonAddTask() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = DialogFragment.newInstance(userId);
                fragment.show(getSupportFragmentManager(), "dialogFragment");
            }
        });

    }

    private void initUi() {
      /*  mRecyclerViewTask = findViewById(R.id.recycler_view);
        mRecyclerViewTask.setLayoutManager(new LinearLayoutManager(this));*/
        emptyListIcon = findViewById(R.id.empty_list_icon);
        mFloatingActionButton = findViewById(R.id.floating_button_add);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
    }

    /* public void updateUi(Integer stateValue) {
         List<Task> mTaskList = mRepository.getTaskList(userId, stateValue);
         if (mTaskAdapter == null) {
             Log.e(TAG, "Task Adapter is null");
             mTaskAdapter = new TaskAdapter(this, mTaskList, State.DOING);
             mRecyclerViewTask.setAdapter(mTaskAdapter);

         } else {
             Log.e(TAG, "Task Adapter exist");
             mTaskAdapter.setTasks(mTaskList);
             mRecyclerViewTask.setAdapter(mTaskAdapter);
             mTaskAdapter.notifyDataSetChanged();
         }
         if (mTaskList.size() != 0) {
             emptyListIcon.setVisibility(View.GONE);
             mRecyclerViewTask.setVisibility(View.VISIBLE);
         }
     }
 */
    private void setupTabTitles() {
        tabLayout.getTabAt(0).setText("TODO");
        tabLayout.getTabAt(1).setText("DOING");
        tabLayout.getTabAt(2).setText("DONE");
    }

    private void addTabs(ViewPager viewPager, int lastItem) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TodoFragment.newInstance(userId), "TodoFragment");
        adapter.addFragment(DoingFragment.newInstance(userId), "DoingFragment");
        adapter.addFragment(DoneFragment.newInstance(userId), "DoneFragment");
        //if (lastItem > -1 && lastItem < 3)
        Log.e(TAG , "in addTabs#" + lastItem);
        viewPager.setCurrentItem(lastItem);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        Log.e(TAG, "handleDialogClose");
        Log.e(TAG, viewPager.getCurrentItem() + "");

        this.viewPagerJob(viewPager.getCurrentItem());
//mTaskAdapter.notifyDataSetChanged();
        //mViewPagerAdapter.notifyDataSetChanged();
    }
}
