package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import info.androidhive.loginandregistration.R;

public class ForgotPD extends Activity {
    private TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pd);
        info=(TextView)findViewById(R.id.TV_Info);
        info.setText("Please contact your admin to retrieve your password..!!");
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ForgotPD.this, LoginActivity.class));
        finish();

    }
}
