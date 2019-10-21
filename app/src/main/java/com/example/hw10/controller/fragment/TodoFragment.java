package com.example.hw10.controller.fragment;


import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw10.R;
import com.example.hw10.controller.activity.LoginActivity;
import com.example.hw10.controller.adapter.TaskAdapter;
import com.example.hw10.model.Repository;
import com.example.hw10.model.State;
import com.example.hw10.model.StateConverter;
import com.example.hw10.model.Task;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {
    public static final String USER_ID = "userId";
    private static final String TAG = "todoFragment";
    private static int stateValue;
    private Long userId;
   // private int lastPosition;

    private ImageView emptyListIcon;
    private RecyclerView mRecyclerViewTask;
    private TaskAdapter mTaskAdapter;
    private List<Task> mTaskList;
    private Repository mRepository;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    private StateConverter mStateConverter;

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstance(Long userId) {

        Bundle args = new Bundle();
        args.putLong(USER_ID, userId);
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRepository = Repository.getInstance();
        userId = getArguments().getLong(USER_ID);
        Log.e("TAG1", "in listFragment userId:#" + userId);

        showSubtitle();
        mStateConverter = new StateConverter();
        stateValue = mStateConverter.convertToDatabaseValue(State.TODO);
        mTaskList = mRepository.getTaskList(userId, stateValue);
    }

    private void showSubtitle() {
        String subtitleMessage = "Hi " + mRepository.getUser(userId).getMUserName();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitleMessage);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        initUi(view);
        Log.e("where am I", "onCreateView");
        updateUi();
        return view;
    }

    private void initUi(View view) {
        emptyListIcon = view.findViewById(R.id.empty_list_icon);
        mRecyclerViewTask = view.findViewById(R.id.recycler_view);
        mRecyclerViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("where am I", "onResume");
        updateUi();
        mTaskAdapter.setTasks(mRepository.getTaskList(userId, stateValue));
        mRecyclerViewTask.setAdapter(mTaskAdapter);
        mTaskAdapter.notifyDataSetChanged();
    }

    private void updateUi() {
        // mTaskList = mRepository.getTaskList(userId, State.TODO);
        mTaskList = mRepository.getTaskList(userId, stateValue);
        if (mTaskAdapter == null) {
            Log.e(TAG, "Task Adapter is null");
            mTaskAdapter = new TaskAdapter(getActivity(), mTaskList , State.TODO);
            mRecyclerViewTask.setAdapter(mTaskAdapter);

        } else {
            Log.e(TAG, "Task Adapter exist");
            mTaskAdapter.setTasks(mRepository.getTaskList(userId, stateValue));
            mRecyclerViewTask.setAdapter(mTaskAdapter);
            mTaskAdapter.notifyDataSetChanged();
        }
        if (mTaskList.size() != 0) {
            emptyListIcon.setVisibility(View.GONE);
            mRecyclerViewTask.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_fragment, menu);

        MenuItem searchItem = menu.findItem(R.id.search_task_menu_item);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    mTaskAdapter.getFilter().filter(newText);

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    mTaskAdapter.getFilter().filter(query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu_item:
                AlertDialog exitDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("are ypu sure you want to log out of your account?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences.Editor editor = getActivity().getApplicationContext()
                                        .getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                                editor.putBoolean(LoginFragment.ALREADY_SIGN_IN, false);
                                editor.commit();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        })
                        .setNegativeButton("NO" , null)
                        .create();
                exitDialog.show();                break;
            case R.id.delete_all_task_menu_item:
                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setMessage("Do you want to delete all Tasks?");
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Repository.getInstance().deleteAllTask(userId);
                        mTaskAdapter = null;
                        Log.e("where am I", "delete_all_task_menu_item");
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
                break;

            case R.id.search_task_menu_item:
                return true;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
}
