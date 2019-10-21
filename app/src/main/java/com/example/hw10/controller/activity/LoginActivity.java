package com.example.hw10.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.hw10.R;
import com.example.hw10.controller.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        boolean status = preferences.getBoolean(LoginFragment.ALREADY_SIGN_IN, false);
        if (status) {
            Long id = preferences.getLong(LoginFragment.SIGN_IN_USER_ID, 0);
            startActivity(ListActivity.newIntent(this, id));
        } else {
            startFragment();
        }
    }

    private void startFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_main);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_main, new LoginFragment())
                    .commit();
        }
    }
}
