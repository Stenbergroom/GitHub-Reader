package com.stenbergroom.githubreader.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nispok.snackbar.Snackbar;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class UserActivity extends ActionBarActivity {

    private URL avatarUrl;
    private Bitmap bitmap;
    private ImageView imageAvatar;

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

        imageAvatar = (ImageView)findViewById(R.id.image_avatar);
        new InitializeUserInfoTask().execute();

        if (MainActivity.getGitHub() == null && MainActivity.getUser() == null) {
            Toast.makeText(UserActivity.this, "gh or user null", Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.with(UserActivity.this)
                    .text(MainActivity.getUser().getLogin())
                    .show(UserActivity.this);
        }
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
        MainActivity.setUser(null);
    }

    class InitializeUserInfoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                avatarUrl = new URL(MainActivity.getUser().getAvatarUrl());
                bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream());
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
        }
    }
}
