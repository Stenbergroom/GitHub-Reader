package com.stenbergroom.githubreader.app.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.nispok.snackbar.Snackbar;
import com.stenbergroom.githubreader.app.R;
import com.stenbergroom.githubreader.app.helper.UserHelper;
import com.stenbergroom.githubreader.app.util.Network;
import com.stenbergroom.githubreader.app.util.UsernameField;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    private static final String CONST_LANG = "language";
    private UsernameField usernameField;
    private EditText etUsername;
    private SmoothProgressBar progressBarMain;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init language
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String languageToLoad = sharedPreferences.getString(CONST_LANG, "");
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setIcon(R.drawable.icon_main);

        usernameField = (UsernameField)findViewById(R.id.username_layout);
        etUsername = (EditText)findViewById(R.id.et_username);
        progressBarMain = (SmoothProgressBar)findViewById(R.id.progress_bar_main);

        progressBarMain.setVisibility(View.INVISIBLE);

        Button btnTellMeMore = (Button) findViewById(R.id.btn_tell_me_more);
        btnTellMeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Network.isAvailable(MainActivity.this) && !etUsername.getText().toString().equals("")) {
                    UserHelper userHelper = new UserHelper(MainActivity.this, etUsername.getText().toString());
                    userHelper.runTask();
                    progressBarMain.setVisibility(View.VISIBLE);
                }
                if (!Network.isAvailable(MainActivity.this)) {
                    Snackbar.with(MainActivity.this)
                            .text(MainActivity.this.getString(R.string.no_connection))
                            .show(MainActivity.this);
                    progressBarMain.setVisibility(View.INVISIBLE);
                }
            }
        });

        etUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        usernameField.clearError();
                        progressBarMain.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    private void saveLocale(String key, String value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBarMain.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_english:
                saveLocale(CONST_LANG, "en");
                Snackbar.with(MainActivity.this)
                        .text(MainActivity.this.getString(R.string.restart_app))
                        .show(MainActivity.this);
                break;
            case R.id.action_russian:
                saveLocale(CONST_LANG, "ru");
                Snackbar.with(MainActivity.this)
                        .text(MainActivity.this.getString(R.string.restart_app))
                        .show(MainActivity.this);
                break;
            case R.id.action_about:
                // TODO about
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
