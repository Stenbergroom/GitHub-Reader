package com.stenbergroom.githubreader.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.IconicsTextView;
import android.view.View;
import android.widget.ProgressBar;
import com.nispok.snackbar.Snackbar;
import com.pkmmte.view.CircularImageView;
import com.stenbergroom.githubreader.app.adapter.RepositoryAdapter;
import com.stenbergroom.githubreader.app.animator.CustomAnimator;
import com.stenbergroom.githubreader.app.entity.Repository;
import com.stenbergroom.githubreader.app.entity.User;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserActivity extends ActionBarActivity {

    private Bitmap bitmap;
    private CircularImageView imageAvatar;
    private String usernameCompany, followersCounts, followingCounts;
    private IconicsTextView tvUsernameCompany, tvFollowersCounts, tvFollowingCounts;
    private ProgressBar progressBar;
    private List<Repository> repositoryList = new ArrayList<Repository>();
    private RepositoryAdapter repositoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        imageAvatar = (CircularImageView)findViewById(R.id.image_avatar);
        tvUsernameCompany = (IconicsTextView)findViewById(R.id.tv_username_company);
        tvFollowersCounts = (IconicsTextView)findViewById(R.id.tv_followers_counts);
        tvFollowingCounts = (IconicsTextView)findViewById(R.id.tv_following_counts);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new CustomAnimator());
        repositoryAdapter = new RepositoryAdapter(new ArrayList<Repository>(), R.layout.row_repository, UserActivity.this);
        recyclerView.setAdapter(repositoryAdapter);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //
            }
        });

        new InitializeUserInfoTask().execute();

    }

    public void onClickImage(View view) {
        switch (view.getId()) {
            case R.id.image_global:
                Snackbar.with(UserActivity.this)
                        .text("Global")
                        .show(UserActivity.this);
                break;
            case R.id.image_share:
                Snackbar.with(UserActivity.this)
                        .text("Share")
                        .show(UserActivity.this);
                break;
            case R.id.image_save:
                Snackbar.with(UserActivity.this)
                        .text("Save")
                        .show(UserActivity.this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User.setGhUser(null);
    }

    class InitializeUserInfoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL avatarUrl = new URL(User.getGhUser().getAvatarUrl());
                bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream());
                if (!(User.getGhUser().getCompany() == null) && !User.getGhUser().getCompany().equals("")) {
                    usernameCompany = User.getGhUser().getLogin() + ", " + User.getGhUser().getCompany();
                } else {
                    usernameCompany = User.getGhUser().getLogin();
                }
                followingCounts = String.valueOf(User.getGhUser().getFollowingCount());
                followersCounts = String.valueOf(User.getGhUser().getFollowersCount());

                Map<String, GHRepository> repositories = User.getGhUser().getRepositories();
                for(String repoName : repositories.keySet()) {
                    GHRepository ghRepository = repositories.get(repoName);
                    repositoryList.add(new Repository(ghRepository.getName(), ghRepository.getLanguage(), ghRepository.getForks(), ghRepository.getWatchers()));
                }
                Collections.sort(repositoryList);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageAvatar.setImageBitmap(bitmap);
            tvUsernameCompany.setText(usernameCompany);
            tvFollowersCounts.setText(followersCounts);
            tvFollowingCounts.setText(followingCounts);

            repositoryAdapter.addRepositories(repositoryList);

            progressBar.setVisibility(View.GONE);
        }
    }
}
