package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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

public class childactivities extends Activity {
    //local declarations
    private static final String TAG = childactivities.class.getSimpleName();
    private Switch swLight;
    private Switch swFan;
    private String light="0";
    private String fan="0";
    private String door="0";
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    //overriding oncreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childactivities);

        //type casting
        swLight = (Switch) findViewById(R.id.switchLight);
        swFan = (Switch) findViewById(R.id.switchFan);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getData();
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        //monitoring switch toggles from the user
        swLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    light="1";
                }
                else {
                    light="0";
                }
                changeData(light,fan,door);
            }
        });
        swFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    fan="1";
                }
                else {
                    fan="0";
                }
                changeData(light,fan,door);
            }
        });
    }

    //function to get data from t edatabase and set them on xml if already set by other users
    private void getData(){
        //tag to cancel request
        String tag_string_req = "getDataActivity";

        //set and show progress dialogue
        pDialog.setMessage("working ...");
        showDialog();

        //make request to php server
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DATA, new Response.Listener<String>() {

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
                    } else {
                        // Error occurred in getting data. Get the error
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
        String tag_string_req = "require_activity";

        pDialog.setMessage("working ...");
        showDialog();

        //make request to change activity data
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
}
