package com.stenbergroom.githubreader.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class UserActivity extends Activity {

    String account = "Stenbergroom";
    private GitHub gitHub;
    private TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tv_test = (TextView)findViewById(R.id.tv_test);

        try {
            gitHub = GitHub.connectUsingOAuth(System.getProperty("github.oauth"));
        } catch (IOException e) {
            Toast.makeText(UserActivity.this, "Error Connect", Toast.LENGTH_SHORT).show();
        }

        new MyTask().execute();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        GHUser user;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                user = gitHub.getUser(account);
            } catch (IOException e) {
                Toast.makeText(UserActivity.this, "Error doInBackground", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tv_test.setText(user.getLogin());
        }
    }
}
