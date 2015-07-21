package com.stenbergroom.githubreader.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class UserActivity extends ActionBarActivity {

    String account = "Stenbergroom";
    private GitHub gitHub;
    private TextView tv_test;

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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.setUser(null);
        Toast.makeText(UserActivity.this, "on destroy", Toast.LENGTH_SHORT).show();
    }
}
