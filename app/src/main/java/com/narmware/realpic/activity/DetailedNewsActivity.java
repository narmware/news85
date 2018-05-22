package com.narmware.realpic.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.narmware.realpic.R;
import com.narmware.realpic.support.Support;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedNewsActivity extends AppCompatActivity {

    @BindView(R.id.webview)protected WebView mWebView;
    protected ProgressDialog mProgress;
    String news_name,news_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        Intent intent=getIntent();
        news_link=intent.getStringExtra(Support.NEWS_URL);
        getSupportActionBar().hide();
        init();
    }

    private void init() {
        ButterKnife.bind(this);

        setWebView();
        mWebView.loadUrl(news_link);

    }

    public void setWebView(){
        mProgress = new ProgressDialog(DetailedNewsActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);
     //   mProgress.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings=mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        //   webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }

    }
    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("Page loaded : ", url);

         //   mProgress.dismiss();

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //     mt(errorCode + " " + description);
            try {
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            //mHorizontalProgress.setProgress(progress);


        }
    }

}
