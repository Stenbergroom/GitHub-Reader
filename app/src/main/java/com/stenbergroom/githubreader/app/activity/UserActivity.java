package com.stenbergroom.githubreader.app.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.cocosw.bottomsheet.BottomSheet;
import com.stenbergroom.githubreader.app.R;
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
import java.util.List;

public class UserActivity extends ActionBarActivity {

    private static final String GH_LINK = "https://github.com/";

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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GH_LINK + User.getGhUser().getLogin()));
                startActivity(browserIntent);
                break;
            case R.id.image_save:
                showDialogSave();
                break;
            case R.id.image_share:
                BottomSheet sheet = getShareActions(new BottomSheet.Builder(UserActivity.this)
                        .grid()
                        .title(getString(R.string.share_title)), GH_LINK + User.getGhUser().getLogin())
                        .show();
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
                .setTitle(R.string.dialog_save_title)
                .setMessage(R.string.dialog_save_message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database database = new Database(UserActivity.this);
                        try {
                            database.savingDataToDatabase();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private BottomSheet.Builder getShareActions(BottomSheet.Builder builder, String text) {
        PackageManager pm = this.getPackageManager();

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        final List<ResolveInfo> list = pm.queryIntentActivities(shareIntent, 0);

        for (int i = 0; i < list.size(); i++) {
            builder.sheet(i, list.get(i).loadIcon(pm), list.get(i).loadLabel(pm));
        }

        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityInfo activity = list.get(which).activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                Intent newIntent = (Intent) shareIntent.clone();
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                newIntent.setComponent(name);
                startActivity(newIntent);
            }
        });
        return builder;
    }
}
