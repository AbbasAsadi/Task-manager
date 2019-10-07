package com.example.hw10.controller.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw10.R;
import com.example.hw10.controller.activity.ListActivity;
import com.example.hw10.controller.activity.SignUpActivity;
import com.example.hw10.model.Repository;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    public static final String USER_ID = "userId";
    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    private MaterialButton logInButton;
    private Repository mRepository;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = Repository.getInstance();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initUi(view);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mEditTextUserName.getText().toString();
                String password = mEditTextPassword.getText().toString();

                if (userName.isEmpty() || password.isEmpty()) {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_animation);
                    mEditTextUserName.startAnimation(shake);
                    mEditTextPassword.startAnimation(shake);
                    Toast.makeText(getActivity(), "UserName and Password should be fill", Toast.LENGTH_SHORT).show();
                } else if (mRepository.getUser(userName) == null) {
                    Toast.makeText(getActivity(), "this user not found", Toast.LENGTH_SHORT).show();
                } else if (!(mRepository.getUser(userName).getMUserName().equals(userName) &&
                        mRepository.getUser(userName).getMPassword().equals(password))) {
                    Toast.makeText(getActivity(), "user name and password not match", Toast.LENGTH_LONG).show();
                } else if (mRepository.getUser(userName).getMUserName().equals(userName) &&
                        mRepository.getUser(userName).getMPassword().equals(password)) {
                    startActivity(ListActivity.newIntent(getActivity(), mRepository.getUser(userName).getMId()));
                }else {
                    Toast.makeText(getActivity(), "something's wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void initUi(@NonNull View view) {
        mEditTextUserName = view.findViewById(R.id.user_name_edit_text);
        mEditTextPassword = view.findViewById(R.id.password_edit_text);
        logInButton = view.findViewById(R.id.logIn_button);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(SignUpActivity.newIntent(getActivity()));
        return super.onOptionsItemSelected(item);
    }

}
