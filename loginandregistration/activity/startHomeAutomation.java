package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class startHomeAutomation extends Activity {

    private Button btnstart;
    private WebView webView;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_home_automation);
        btnstart = (Button) findViewById(R.id.btnStart);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new  SessionManager(getApplicationContext());
        // Logout button click event
        btnstart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    private void start() {
        boolean mboolean = false;
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            // do the thing for the first time
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();

            Intent intent = new Intent(startHomeAutomation.this, RegisterActivity.class);
            startActivity(intent);
            finish();

        } else {
        // other time your app loads
            Intent intent = new Intent(startHomeAutomation.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
