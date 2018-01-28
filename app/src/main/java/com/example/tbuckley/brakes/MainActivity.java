package com.example.tbuckley.brakes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Switch mUrlNavigationSwitch;
    private SettingsManager mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(SettingsManager.PREFS, MODE_PRIVATE);
        mSettings = new SettingsManager(prefs);

        // When checked, allows navigating the web by clicking on URLs.
        // When not checked, prevents URLs from loading.
        mUrlNavigationSwitch = findViewById(R.id.url_navigation_switch);
        mUrlNavigationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSettings.setNavigationAllowed(mUrlNavigationSwitch.isChecked());
            }
        });

        mUrlNavigationSwitch.setChecked(mSettings.getNavigationAllowed());
    }
}
