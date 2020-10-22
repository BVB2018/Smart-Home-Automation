package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
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

public class otherUserActivity extends AppCompatActivity {

    private static final String TAG = otherUserActivity.class.getSimpleName();
    private Switch swLight;
    private Switch swFan;
    private Switch swDoor;
    private String light="0";
    private String fan="0";
    private String door="0";
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    //overriding function to be executed onCreate of that screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        //type casting
        swLight = (Switch) findViewById(R.id.light);
        swFan = (Switch) findViewById(R.id.Fan);
        swDoor = (Switch) findViewById(R.id.Door);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //get values of the switches if already set by other user
        getData();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        //txtName.setText(name);
        //txtEmail.setText(email);

        //monitoring switch light
        swLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    light="1";
                }
                else {
                    light="0";
                }
                //changing data in database
                changeData(light,fan,door);
            }
        });

        //monitoring switch fan
        swFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    fan="1";
                }
                else {
                    fan="0";
                }
                //changing data in database
                changeData(light,fan,door);
            }
        });

        //monitoring switch door
        swDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    door="1";
                }
                else {
                    door="0";
                }
                //changing data in database
                changeData(light,fan,door);
            }
        });
    }
    //function to get the data set to the switches from the database, if it's already set by other users
    private void getData(){
        //request tag
        String tag_string_req = "get_data_activity";

        //set and show progress dialogue
        pDialog.setMessage("working ...");
        showDialog();

        //make request to get data
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DATA, new Response.Listener<String>() {

            //get response from php
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get Data Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONObject user = jObj.getJSONObject("user");
                        String light = user.getString("light");
                        String fan = user.getString("fan");
                        String door=user.getString("door");

                        //setting switch values in xml file
                        if(light.equals("1")){
                            swLight.setChecked(true);
                        }
                        else{
                            swLight.setChecked(false);
                        }
                        if(fan.equals("1")){
                            swFan.setChecked(true);
                        }
                        else{
                            swFan.setChecked(false);
                        }
                        if(door.equals("1")){
                            swDoor.setChecked(true);
                        }
                        else{
                            swDoor.setChecked(false);
                        }
                        // Inserting row in users table
                        //db.addUser(name, email,phone,category, uid, created_at);

                        //Toast.makeText(getApplicationContext(), "Success..!", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
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
                Log.e(TAG, "Error in getting data: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void changeData(final String light, final String fan,
                            final String door){
        // Tag used to cancel the request
        String tag_string_req = "req_activity";

        pDialog.setMessage("working ...");
        showDialog();
        Log.d("hi",light+fan+door);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ACTIVITY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Updation Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONObject user = jObj.getJSONObject("user");
                        String light = user.getString("light");
                        String fan = user.getString("fan");
                        String door=user.getString("door");

                        // Inserting row in users table
                        //db.addUser(name, email,phone,category, uid, created_at);

                        //Toast.makeText(getApplicationContext(), "Success..!", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
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
                params.put("light", light);
                params.put("fan", fan);
                params.put("door", door);
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
        startActivity(new Intent(otherUserActivity.this, otherUserPage.class));
        finish();

    }
}

