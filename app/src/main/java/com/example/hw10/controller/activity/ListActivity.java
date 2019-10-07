package com.example.hw10.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.hw10.R;
import com.example.hw10.controller.fragment.ListFragment;

public class ListActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "userId";
    private Long userId;
    private ListFragment listFragment;

    public static Intent newIntent(Context context, Long id) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        userId = (Long) getIntent().getSerializableExtra(EXTRA_USER_ID);
        manageOutputIntent();
        startFragment();
    }
    private void startFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_list);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_list, listFragment)
                    .commit();
        }
    }



    private void manageOutputIntent() {
        listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_USER_ID, userId);
        listFragment.setArguments(args);
    }
}
