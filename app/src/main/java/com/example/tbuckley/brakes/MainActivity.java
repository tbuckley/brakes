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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "MainActivity";
    private static final String PREFS = "com.example.tbuckley.brakes.PREFS";
    private static final String NAVIGATION_ALLOWED =
            "com.example.tbuckley.brakes.NAVIGATION_ALLOWED";
    private static final boolean DEFAULT_NAVIGATION_ALLOWED = false;

    WebView mWebView;
    TextView mTextView;
    boolean mClearHistory;
    boolean mPreventUrlLoading;
    private ProgressBar mProgressBar;
    private ToggleButton mUrlNavigationToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webview);
        mTextView = findViewById(R.id.textview);
        mProgressBar = findViewById(R.id.progress_bar);

        // When checked, allows navigating the web by clicking on URLs.
        // When not checked, prevents URLs from loading.
        mUrlNavigationToggle = findViewById(R.id.url_navigation_toggle);
        mUrlNavigationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                editor.putBoolean(NAVIGATION_ALLOWED, mUrlNavigationToggle.isChecked());
                editor.apply();
            }
        });
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (prefs.contains(NAVIGATION_ALLOWED)) {
            mUrlNavigationToggle.setChecked(prefs.getBoolean(NAVIGATION_ALLOWED,
                    DEFAULT_NAVIGATION_ALLOWED));
        }

        mWebView.setOnTouchListener(this);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted: " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(mClearHistory) {
                    mClearHistory = false;
                    mWebView.clearHistory();
                }
                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            @TargetApi(24)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, request.getUrl().toString());
                if (mPreventUrlLoading) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.url_blocked),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            mWebView.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.GONE);
            mUrlNavigationToggle.setVisibility(View.GONE);
            Uri data = intent.getData();

            // Allow loading a URL from a VIEW Intent.
            mPreventUrlLoading = false;
            mClearHistory = true;
            Log.d(TAG, "loadUrl: " + data.toString());
            mWebView.loadUrl(data.toString());
            mWebView.clearHistory();
        } else {
            mWebView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            mUrlNavigationToggle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!mUrlNavigationToggle.isChecked()) {
            // Block URL loading when a user touches something.
            mPreventUrlLoading = true;
        }
        return false; // Let system handle touch event.
    }

}
