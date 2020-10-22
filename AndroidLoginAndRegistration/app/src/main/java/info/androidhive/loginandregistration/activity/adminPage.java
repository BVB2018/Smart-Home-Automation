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

public class adminPage extends Activity {
    //local declaration of variables
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnActivities;
    private Button btnReg;
    private Button btnDel;
    private Button btnChPd;
    private Button btnChHist;
    private Button btnForgotPassword;
    private SQLiteHandler db;
    private SessionManager session;

    //overriding onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        //type casting
        //txtName = (TextView) findViewById(R.id.name);
        //txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnActivities = (Button) findViewById(R.id.btnActivities);
        btnReg = (Button) findViewById(R.id.btnRegDel);
        btnDel = (Button) findViewById(R.id.btnDeleteUser);
        btnChPd = (Button) findViewById(R.id.btnChPd);
        btnChHist = (Button) findViewById(R.id.btnChHist);
      //  btnForgotPassword = (Button) findViewById(R.id.btn_forgot_password);
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

        // Displaying the user details on the screen
        //txtName.setText(name);
        //txtEmail.setText(email);

        //providing intents to redirect to that particular screen on cick buttons

        btnActivities.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminPage.this, adminActivities.class);
                startActivity(intent);
                finish();
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminPage.this, RegisterByAdmin.class);
                startActivity(intent);
                finish();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminPage.this, DeleteActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnChPd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminPage.this, changepassword.class);
                startActivity(intent);
                finish();
            }
        });
        btnChHist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminPage.this, logHistory.class);
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


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(adminPage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(adminPage.this, LoginActivity.class));
        finish();

    }
}
