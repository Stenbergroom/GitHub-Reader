package com.stenbergroom.githubreader.app;

import android.os.Bundle;
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
import com.stenbergroom.githubreader.app.helper.UserHelper;
import com.stenbergroom.githubreader.app.util.UsernameField;


public class MainActivity extends ActionBarActivity {

    private UsernameField usernameField;
    private EditText etUsername;

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
        Button btnTellMeMore = (Button) findViewById(R.id.btn_tell_me_more);


        btnTellMeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Network.isAvailable(MainActivity.this) && !etUsername.getText().toString().equals("")) {
                    UserHelper userHelper = new UserHelper(MainActivity.this, etUsername.getText().toString());
                    userHelper.runTask();
                }
                if (!Network.isAvailable(MainActivity.this)) {
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
}
