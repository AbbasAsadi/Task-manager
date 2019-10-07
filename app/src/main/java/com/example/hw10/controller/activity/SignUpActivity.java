package com.example.hw10.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.hw10.R;
import com.example.hw10.controller.fragment.SignUpFragment;

public class SignUpActivity extends AppCompatActivity {
    public static Intent newIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        startFragment();
    }
    private void startFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_sign_up);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_sign_up, new SignUpFragment())
                    .commit();
        }
    }
}
