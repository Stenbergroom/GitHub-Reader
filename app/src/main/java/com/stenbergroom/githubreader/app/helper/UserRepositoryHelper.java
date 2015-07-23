package com.stenbergroom.githubreader.app.helper;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import com.stenbergroom.githubreader.app.R;
import com.stenbergroom.githubreader.app.UserActivity;
import com.stenbergroom.githubreader.app.adapter.RepositoryAdapter;
import com.stenbergroom.githubreader.app.animator.CustomAnimator;
import com.stenbergroom.githubreader.app.entity.Repository;
import com.stenbergroom.githubreader.app.entity.User;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserRepositoryHelper {

    private List<Repository> repositoryList = new ArrayList<Repository>();
    private RepositoryAdapter repositoryAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public UserRepositoryHelper(RepositoryAdapter repositoryAdapter, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, ProgressBar progressBar) {
        this.repositoryAdapter = repositoryAdapter;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.progressBar = progressBar;
    }

    public void runTask() {
        UserRepositoryHelperTask userRepositoryHelperTask = new UserRepositoryHelperTask();
        userRepositoryHelperTask.execute();
    }

    public class UserRepositoryHelperTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);
//            swipeRefreshLayout = (SwipeRefreshLayout)activity.findViewById(R.id.swipe_container);
//            recyclerView = (RecyclerView)activity.findViewById(R.id.list);
//            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
//            recyclerView.setItemAnimator(new CustomAnimator());
//            repositoryAdapter = new RepositoryAdapter(new ArrayList<Repository>(), R.layout.row_repository, activity);
//            recyclerView.setAdapter(repositoryAdapter);
            repositoryAdapter.clearRepositories();
        }

        @Override
        protected Void doInBackground(Void... params) {
            repositoryList.clear();

            try {
                Map<String, GHRepository> repositories = User.getGhUser().getRepositories();
                for(String repoName : repositories.keySet()) {
                    GHRepository ghRepository = repositories.get(repoName);
                    repositoryList.add(new Repository(ghRepository.getName(), ghRepository.getLanguage(), ghRepository.getForks(), ghRepository.getWatchers()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Collections.sort(repositoryList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            repositoryAdapter.addRepositories(repositoryList);

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
