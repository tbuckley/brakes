package com.example.tbuckley.brakes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    WebView mWebView;
    TextView mTextView;
    boolean mClearHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);
        mTextView = (TextView) findViewById(R.id.textview);

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
                super.onPageFinished(view, url);
            }

            @Override
            @TargetApi(24)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, request.getUrl().toString());
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

        if(Intent.ACTION_VIEW.equals(action)) {
            mWebView.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.GONE);
            Uri data = intent.getData();

            mClearHistory = true;
            Log.d(TAG, "loadUrl: " + data.toString());
            mWebView.loadUrl(data.toString());
            mWebView.clearHistory();
        } else {
            mWebView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
