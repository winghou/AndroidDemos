package com.benio.demoproject.web;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.benio.demoproject.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private View mBadNetView;

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d(TAG, "onProgressChanged() called with: view = [" + view + "], newProgress = [" + newProgress + "]");
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            Log.d(TAG, "onReceivedTitle() called with: view = [" + view + "], title = [" + title + "]");
            setTitle(title);
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        private boolean mReceivedError;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mReceivedError = false;
            Log.d(TAG, "onPageStarted() called with: view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
            showLoading();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished() called with: view = [" + view + "], url = [" + url + "]");
            if (!mReceivedError) {
                hideLoading();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading() called with: view = [" + view + "], url = [" + url + "]");
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, "onReceivedError() called with: view = [" + view + "], errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
            mReceivedError = true;
            showNetError();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            showNetError();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);
        mBadNetView = findViewById(R.id.tv_web_error);
        mWebView = (WebView) findViewById(R.id.web);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_web);

        initWebView(mWebView);
        String url = "http://www.baidu.com1";
        mWebView.loadUrl(url);
    }

    private void initWebView(WebView webView) {
        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(mWebChromeClient);
        WebSettings settings = webView.getSettings();
        //支持 Javascript
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);
    }

    private static final String TAG = "WebViewActivity";

    public void showLoading() {
        mBadNetView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
    }

    public void hideLoading() {
        mBadNetView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }

    public void showNetError() {
        mBadNetView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mWebView.reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 点击back，后退网页
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
