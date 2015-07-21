package com.stenbergroom.githubreader.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.nispok.snackbar.Snackbar;
import com.stenbergroom.githubreader.app.util.Network;
import com.stenbergroom.githubreader.app.util.UsernameField;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private static final String OAUTH_TOKEN = System.getProperty("github.oauth");
    private static GitHub gitHub;
    private static GHUser user;
    private UsernameField usernameField;
    private EditText etUsername;
    private Button btnTellMeMore;

    public static GitHub getGitHub() {
        return gitHub;
    }

    public static GHUser getUser() {
        return user;
    }

    public static void setUser(GHUser user) {
        MainActivity.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        usernameField = (UsernameField)findViewById(R.id.username_layout);
        etUsername = (EditText)findViewById(R.id.et_username);
        btnTellMeMore = (Button)findViewById(R.id.btn_tell_me_more);


        btnTellMeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Network.isAvailable(MainActivity.this) && !etUsername.getText().toString().equals("")){
                    try {
                        gitHub = GitHub.connectUsingOAuth(OAUTH_TOKEN);
                        if (gitHub != null) {
                             new ProcessingUserTask().execute();
                        } else {
                            Snackbar.with(MainActivity.this)
                                    .text(MainActivity.this.getString(R.string.failed_connection_to_gh))
                                    .show(MainActivity.this);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Catch error connect", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!Network.isAvailable(MainActivity.this)){
                    Snackbar.with(MainActivity.this)
                            .text(MainActivity.this.getString(R.string.no_connection))
                            .show(MainActivity.this);
                }
            }
        });

        etUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        usernameField.clearError();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ProcessingUserTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                user = gitHub.getUser(etUsername.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (user != null) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("username", user.getLogin());
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(intent, activityOptionsCompat.toBundle());
            } else {
                usernameField.setError("User "+etUsername.getText().toString()+" not found");
            }
        }
    }
}
