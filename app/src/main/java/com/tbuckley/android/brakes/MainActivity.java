package com.tbuckley.android.brakes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

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
