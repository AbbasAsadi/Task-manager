package com.example.hw10.controller.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw10.R;
import com.example.hw10.model.Repository;
import com.example.hw10.model.User;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
    private MaterialButton mButtonSignUp;
    private Repository mRepository;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = Repository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initUi(view);
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mEditTextUserName.getText().toString();
                String password = mEditTextPassword.getText().toString();
                String confirmPassword = mEditTextConfirmPassword.getText().toString();
                if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_animation);
                    mEditTextUserName.startAnimation(shake);
                    mEditTextPassword.startAnimation(shake);
                    mEditTextConfirmPassword.startAnimation(shake);
                    Toast.makeText(getActivity(),
                            "user name and password and confirm Password should be filled",
                            Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_animation);
                    mEditTextPassword.startAnimation(shake);
                    mEditTextConfirmPassword.startAnimation(shake);
                    Toast.makeText(getActivity(),
                            "both password should be equal",
                            Toast.LENGTH_LONG).show();
                } else if (mRepository.getUser(userName) != null) {
                    Toast.makeText(getActivity(),
                            "this userName is exist , try another ._.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();
                    user.setMUserName(userName);
                    user.setMPassword(password);

                    Toast.makeText(getActivity(), "add user was successful", Toast.LENGTH_SHORT).show();
                    mRepository.insertUser(user);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    private void initUi(View view) {
        mEditTextUserName = view.findViewById(R.id.user_name_edit_text_sign_up);
        mEditTextPassword = view.findViewById(R.id.password_edit_text_sign_up);
        mEditTextConfirmPassword = view.findViewById(R.id.confirm_password_edit_text_sign_up);
        mButtonSignUp = view.findViewById(R.id.sign_up_button);
    }
}
