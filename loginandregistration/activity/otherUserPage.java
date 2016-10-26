package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class otherUserPage extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnActivities;
    private Button btnChPd;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_page);

        txtName = (TextView) findViewById(R.id.nameET);
        txtEmail = (TextView) findViewById(R.id.emailET);
        btnActivities = (Button) findViewById(R.id.btnuserActivities);
        btnChPd = (Button) findViewById(R.id.btnuserChPd);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        //Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        btnActivities.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(otherUserPage.this, adminActivities.class);
                startActivity(intent);
                finish();
            }
        });
        btnChPd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(otherUserPage.this, changepassword.class);
                startActivity(intent);
                finish();
            }
        });
        // Logout button click event
         btnLogout.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
                logoutUser();
            }
        });
    }

    //disabling back button
    @Override
    public void onBackPressed() {
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(otherUserPage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
