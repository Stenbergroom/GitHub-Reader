package com.stenbergroom.githubreader.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import com.nispok.snackbar.Snackbar;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.net.URL;

public class UserActivity extends ActionBarActivity {

    private URL avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //gitHub = GitHub.connectUsingOAuth(System.getProperty("github.oauth"));
        //user = gitHub.getUser(account);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        if (MainActivity.getGitHub() == null && MainActivity.getUser() == null) {
            Toast.makeText(UserActivity.this, "gh or user null", Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.with(UserActivity.this)
                    .text(MainActivity.getUser().getLogin())
                    .show(UserActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.setUser(null);
    }
}
