package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class change_pwd_child extends Activity {
    //local declarations
    private static final String TAG = change_pwd_child.class.getSimpleName();
    private Button btnchangePassword;
    private String curr_password;
    private String new_password;
    private String email;
    private EditText curr_pd;
    private EditText new_pd;
    private EditText emailid;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd_child);

        //type casting
        curr_pd=(EditText)findViewById(R.id.currPd_ET);
        new_pd=(EditText)findViewById(R.id.newPd_ET);
        emailid=(EditText)findViewById(R.id.ETEMAIL);
        btnchangePassword = (Button) findViewById(R.id.btn_changepassword);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from SQLite
        // HashMap<String, String> user = db.getUserDetails();

        //String name = user.get("name");
        //String email = user.get("email");

        // Displaying the user details on the screen
        //txtName.setText(name);
        //txtEmail.setText(email);

        // change password button click event
        btnchangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailid.getText().toString().trim();
                curr_password=curr_pd.getText().toString().trim();
                new_password=new_pd.getText().toString().trim();
                changeData(email,curr_password,new_password);
            }
        });
    }

    //function to change password
    private void changeData(final String email, final String curr_password,
                            final String new_password) {
        // Tag used to cancel the request
        String tag_string_req = "req_activity";

        pDialog.setMessage("working ...");
        showDialog();

        //making request to php
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHPD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Changing Password Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Success..!", Toast.LENGTH_LONG).show();
                    } else {
                        // Error occurred in changing password. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Updation Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("curr_password", curr_password);
                params.put("new_password", new_password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(change_pwd_child.this, childPage.class));
        finish();

    }
}
