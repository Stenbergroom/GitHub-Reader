package com.stenbergroom.githubreader.app.helper;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import com.stenbergroom.githubreader.app.MainActivity;
import com.stenbergroom.githubreader.app.R;
import com.stenbergroom.githubreader.app.UserActivity;
import com.stenbergroom.githubreader.app.entity.User;
import com.stenbergroom.githubreader.app.util.UsernameField;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class UserHelper {

    private static final String OAUTH_TOKEN = System.getProperty("github.oauth");
    private GHUser user;
    private MainActivity activity;
    private String username;

    public UserHelper(MainActivity activity, String username) {
        this.activity = activity;
        this.username = username;
    }

    public void runTask() {
        UserHelperTask userHelperTask = new UserHelperTask();
        userHelperTask.execute();
    }

    public class UserHelperTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //GitHub gitHub = GitHub.connectUsingOAuth(OAUTH_TOKEN);
                GitHub gitHub = GitHub.connectAnonymously();
                user = gitHub.getUser(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (user != null) {
                new User(user);
                Intent intent = new Intent(activity, UserActivity.class);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                activity.startActivity(intent, activityOptionsCompat.toBundle());
            } else {
                SmoothProgressBar progressBarMain = (SmoothProgressBar)activity.findViewById(R.id.progress_bar_main);
                UsernameField usernameField = (UsernameField) activity.findViewById(R.id.username_layout);
                usernameField.setError("User " + username + " not found");
                progressBarMain.setVisibility(View.INVISIBLE);
            }
        }
    }
}
