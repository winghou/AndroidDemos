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
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.benio.demoproject.R;
import com.benio.demoproject.web.urlrooter.UrlInterceptor;
import com.benio.demoproject.web.urlrooter.UrlRouterInterceptor;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private View mBadNetView;
    private UrlInterceptor mUrlInterceptor;

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
            Log.d(TAG, "onPageStarted() called with: view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
            mReceivedError = false;
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
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldInterceptRequest() called with: view = [" + view + "], request = [" + request + "]");
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading() called with: view = [" + view + "], url = [" + url + "]");
            if (mUrlInterceptor.intercept(url)) {
                return true;
            }
            view.loadUrl(url);
            return false;
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

        mUrlInterceptor = new UrlRouterInterceptor(this);
        initWebView(mWebView);
        String html = "<html>\n" +
                "\t<body>\n" +
                "\t<a href=\"http://www.baidu.com\">baidu</a>\n" +
                "\t<a href=\"magapp://salestarget/viewOrgSalesTarget?orgId=100&orgName=name&orgType=2&date=2016-12-01\">伪协议</a>\n" +
                "\t</body>\n" +
                "</html>";
        mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
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
