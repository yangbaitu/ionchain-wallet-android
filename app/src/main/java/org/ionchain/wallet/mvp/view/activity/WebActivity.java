package org.ionchain.wallet.mvp.view.activity;

import android.view.View;
import android.webkit.WebView;

import org.ionchain.wallet.R;
import org.ionchain.wallet.mvp.view.base.AbsBaseActivity;

public class WebActivity extends AbsBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        getMImmersionBar().titleView(R.id.toolbarlayout)
                .statusBarDarkFont(true)
                .execute();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebView webView = findViewById(R.id.web_view);
        webView.loadUrl("https://www.ionchain.org/download/wallet-agreement.html");
    }
}