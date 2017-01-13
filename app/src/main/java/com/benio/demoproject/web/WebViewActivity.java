package com.benio.demoproject.web;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.benio.demoproject.R;
import com.benio.demoproject.web.urlrooter.UrlInterceptor;
import com.benio.demoproject.web.urlrooter.UrlRouterInterceptor;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";

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
        private boolean mReceivedSslError;
        private boolean loadingFinished = true;
        private boolean redirect = false;
        private Dialog mDialog;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted() called with: view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
            mReceivedError = false;
            loadingFinished = false;
            showLoading();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished() called with: view = [" + view + "], url = [" + url + "]");
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                if (!mReceivedError) {
                    hideLoading();
                    // 完全加载完页面再显示Title
                    setTitle(view.getTitle());
                } else {
                    showNetError();
                }
            } else {
                redirect = false;
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading() called with: view = [" + view + "], url = [" + url + "]");
            if (mUrlInterceptor.intercept(url)) {
                return true;
            }
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, "onReceivedError() called with: view = [" + view + "], errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
            mReceivedError = true;
            view.stopLoading();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            showSslError(handler, error);
        }

        private void showSslError(final SslErrorHandler handler, SslError error) {
            if (mReceivedSslError) {
                handler.proceed();
                return;
            }

            if (mDialog == null) {
                Context context = WebViewActivity.this;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mReceivedSslError = true;
                        handler.proceed();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mReceivedSslError = false;
                        handler.cancel();
                        dialog.dismiss();
                    }
                });
                builder.setMessage(R.string.error_ssl_cert_invalid);
                builder.setTitle(R.string.label_warning);
                mDialog = builder.show();
            } else if (!mDialog.isShowing()) {
                mDialog.show();
            }
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
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(mWebChromeClient);
        WebSettings settings = webView.getSettings();
        //支持 Javascript
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);

        // For API level below 18 (This method was deprecated in API level 18)
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Application Cache 存储
        settings.setAppCacheEnabled(true);
        final String cachePath = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(cachePath);
        settings.setAppCacheMaxSize(5 * 1024 * 1024);

        // Web SQL Database 存储
        settings.setDatabaseEnabled(true);
        final String dbPath = getApplicationContext().getDir("db", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(dbPath);

        //Dom Storage（Web Storage）存储
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.setDomStorageEnabled(true);
    }

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

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
