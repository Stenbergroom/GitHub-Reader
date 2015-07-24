package com.stenbergroom.githubreader.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.nispok.snackbar.Snackbar;
import com.stenbergroom.githubreader.app.adapter.RepositoryAdapter;
import com.stenbergroom.githubreader.app.animator.CustomAnimator;
import com.stenbergroom.githubreader.app.database.Database;
import com.stenbergroom.githubreader.app.entity.Repository;
import com.stenbergroom.githubreader.app.entity.User;
import com.stenbergroom.githubreader.app.helper.UserContentHelper;
import com.stenbergroom.githubreader.app.helper.UserRepositoryHelper;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import java.io.IOException;
import java.util.ArrayList;

public class UserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        CircularProgressBar progressBar = (CircularProgressBar)findViewById(R.id.progress_bar_user);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new CustomAnimator());
        RepositoryAdapter repositoryAdapter = new RepositoryAdapter(new ArrayList<Repository>(), R.layout.row_repository);
        recyclerView.setAdapter(repositoryAdapter);

        final UserContentHelper userContentHelper = new UserContentHelper(UserActivity.this);
        final UserRepositoryHelper userRepositoryHelper = new UserRepositoryHelper(repositoryAdapter, recyclerView, swipeRefreshLayout, progressBar);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userRepositoryHelper.runTask();
            }
        });

        userRepositoryHelper.runTask();
        userContentHelper.runTask();

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void onClickImage(View view) {
        switch (view.getId()) {
            case R.id.image_global:
                Snackbar.with(UserActivity.this)
                        .text("Global")
                        .show(UserActivity.this);
            break;
            case R.id.image_save:
                showDialogSave();
                break;
            case R.id.image_share:
            Snackbar.with(UserActivity.this)
                        .text("Share")
                        .show(UserActivity.this);
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User.setGhUser(null);
    }

    private void showDialogSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(R.string.dialog_save_title)
                .setMessage(R.string.dialog_save_message)
                .setCancelable(false)
                .setPositiveButton(R.id.dialog_save_btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database database = new Database(UserActivity.this);
                        try {
                            database.pushInfo();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.id.dialog_save_btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
