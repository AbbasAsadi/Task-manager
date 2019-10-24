package com.example.hw10.controller.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.hw10.R;
import com.example.hw10.controller.adapter.ViewPagerAdapter;
import com.example.hw10.controller.fragment.DialogAddEditFragment;
import com.example.hw10.controller.fragment.DoingFragment;
import com.example.hw10.controller.fragment.DoneFragment;
import com.example.hw10.controller.fragment.TodoFragment;
import com.example.hw10.utility.MyDialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ListActivity extends AppCompatActivity implements MyDialogCloseListener {
    public static final String EXTRA_USER_ID = "userId";
    private static final String TAG = "ListActivity";
    private Long userId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton mFloatingActionButton;

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
        userId = (Long) getIntent().getSerializableExtra(EXTRA_USER_ID);
        Log.e("TAG1", "in listActivity userId:#" + userId);
        viewPagerJob(-2);

        initFloatingButtonAddTask();
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
                DialogAddEditFragment fragment = DialogAddEditFragment.newInstance(userId);
                fragment.show(getSupportFragmentManager(), "dialogFragment");
            }
        });

    }

    private void initUi() {
        mFloatingActionButton = findViewById(R.id.floating_button_add);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
    }


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
        Log.e(TAG, "in addTabs#" + lastItem);
        viewPager.setCurrentItem(lastItem);
        viewPager.setOffscreenPageLimit(lastItem);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        Log.e(TAG, "handleDialogClose");
        Log.e(TAG, viewPager.getCurrentItem() + "");

        this.viewPagerJob(viewPager.getCurrentItem());
    }
}
