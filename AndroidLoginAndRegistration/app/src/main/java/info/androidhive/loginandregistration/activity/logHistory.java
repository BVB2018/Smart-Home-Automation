package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;


public class logHistory extends Activity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_history);
        webView = (WebView)findViewById(R.id.webLog);
        webView.loadUrl(AppConfig.URL_HISTORY);


    }
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(logHistory.this, adminPage.class));
        finish();

    }
}
