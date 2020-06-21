package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.im.R;


public class IMWebActivity extends IMBaseActivity implements View.OnClickListener {
    private String url;
    private RelativeLayout mRlfinish;
    private WebView mwebView;
    private View mline;
    private ProgressBar mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_webview);
        mRlfinish=findViewById(R.id.re_top_finish);
        mline=findViewById(R.id.line);
        mProgress=findViewById(R.id.progress_bar);
        mRlfinish.setOnClickListener(this);
        mwebView=findViewById(R.id.web);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mwebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if(TextUtils.isEmpty(url)){
            Toast.makeText(this, "内容链接无效,请检查链接地址", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        data();
    }
    private void data() {
        WebSettings settings = mwebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mwebView.setWebChromeClient(new WebChromeClient());
        mwebView.setWebViewClient(new WebViewClient());
        mwebView.getSettings().setJavaScriptEnabled(true);
        //支持JS
        mwebView.loadUrl(url);
        mwebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgress.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == mProgress.getVisibility()) {
                        mProgress.setVisibility(View.VISIBLE);
                    }
                    mProgress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.re_top_finish){
            finish();
        }
    }

    //使用Webview的时候，返回键没有重写的时候会直接关闭程序，这时候其实我们要其执行的知识回退到上一步的操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if(keyCode==KeyEvent.KEYCODE_BACK&&mwebView.canGoBack()){
            mwebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
