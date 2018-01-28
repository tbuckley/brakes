package com.example.tbuckley.brakes;

import android.content.SharedPreferences;

/**
 * Created by tbuckley on 1/28/18.
 */

public class SettingsManager {
    private static final String NAVIGATION_ALLOWED =
            "com.example.tbuckley.brakes.NAVIGATION_ALLOWED";
    private static final boolean DEFAULT_NAVIGATION_ALLOWED = false;

    public static final String PREFS = "com.example.tbuckley.brakes.PREFS";

    private SharedPreferences mPrefs;

    SettingsManager(SharedPreferences prefs) {
        mPrefs = prefs;
    }

    public boolean getNavigationAllowed() {
        return mPrefs.getBoolean(NAVIGATION_ALLOWED, DEFAULT_NAVIGATION_ALLOWED);
    }
    public void setNavigationAllowed(boolean allowed) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(NAVIGATION_ALLOWED, allowed);
        editor.apply();
    }
}
