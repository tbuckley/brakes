package com.example.tbuckley.brakes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "MainActivity";

    WebView mWebView;
    ProgressBar mProgressBar;

    boolean mClearHistory;
    boolean mPreventUrlLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.progress_bar);

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
                    Toast.makeText(WebViewActivity.this,
                            getString(R.string.url_blocked),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        // Enable Javascript & Zoom
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        String action = intent.getAction();
        assert Intent.ACTION_VIEW.equals(action);

        mWebView.setVisibility(View.VISIBLE);
        Uri data = intent.getData();

        // Allow loading a URL from a VIEW Intent.
        mPreventUrlLoading = false;
        mClearHistory = true;
        Log.d(TAG, "loadUrl: " + data.toString());
        mWebView.loadUrl(data.toString());
        mWebView.clearHistory();
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
        SharedPreferences prefs = getSharedPreferences(SettingsManager.PREFS, MODE_PRIVATE);
        SettingsManager settings = new SettingsManager(prefs);

        if (!settings.getNavigationAllowed()) {
            // Block URL loading when a user touches something.
            mPreventUrlLoading = true;
        }
        return false; // Let system handle touch event.
    }

}
