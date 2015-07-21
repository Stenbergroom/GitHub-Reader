package com.stenbergroom.githubreader.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

    private GitHub gitHub;
    private GHUser user;
    private UsernameField usernameField;
    private EditText etUsername;
    private Button btnTellMeMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (UsernameField)findViewById(R.id.username_layout);
        etUsername = (EditText)findViewById(R.id.et_username);
        btnTellMeMore = (Button)findViewById(R.id.btn_tell_me_more);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        btnTellMeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Network.isAvailable(MainActivity.this) && !etUsername.getText().toString().equals("")){
                    try {
                        gitHub = GitHub.connectUsingOAuth(OAUTH_TOKEN);
                        if (gitHub != null) {
                            checkGitHubUser(etUsername.getText().toString());
                        } else {
                            Snackbar.with(MainActivity.this)
                                    .text(MainActivity.this.getString(R.string.failed_connection_to_gh))
                                    .show(MainActivity.this);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Catch error", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!Network.isAvailable(MainActivity.this)){
                    Snackbar.with(MainActivity.this)
                            .text(MainActivity.this.getString(R.string.no_connection))
                            .show(MainActivity.this);
                }
            }
        });
    }

    private void checkGitHubUser(String username) {

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
}
