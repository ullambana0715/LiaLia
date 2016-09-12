package cn.chono.yopper.activity.find;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.utils.ISO8601;

/**
 * Created by cc on 16/2/25.
 */
public class CampaignsActivity extends MainFrameActivity {

    private WebView campaigns_webview;


    private String webURL;

    private String title;

    private ViewStub error_network_vs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.campaigns_activity);

        PushAgent.getInstance(this).onAppStart();

        title = getIntent().getExtras().getString("TITLE");

        webURL = getIntent().getExtras().getString("URL");

        initView();

        initWebView();

//        DialogUtil.showDisCoverNetToast(this, "请在wifi环境下加载\n" + "  土豪请无视");
    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("活动WEB"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("活动WEB"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }



    private void initView() {

        campaigns_webview = (WebView) findViewById(R.id.campaigns_webview);

        error_network_vs = (ViewStub) findViewById(R.id.error_network_vs);

        getTvTitle().setText(title);

        getBtnOption().setVisibility(View.GONE);

        gettvOption().setVisibility(View.GONE);

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }


    private void handleNetError() {

        error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) this.findViewById(R.id.error_network_layout);

        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) this.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                campaigns_webview.setVisibility(View.VISIBLE);

                campaigns_webview.loadUrl(webURL);

                error_network_vs.setVisibility(View.GONE);
            }
        });
    }


    private void initWebView() {


        campaigns_webview.getSettings().setJavaScriptEnabled(true);// 允许使用js

        campaigns_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式

        campaigns_webview.getSettings().setDomStorageEnabled(true);


        campaigns_webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


//                if (ActivityUtil.isNetWorkAvailable(CampaignsActivity.this)) {
//                    campaigns_webview.setVisibility(View.VISIBLE);
//
//                    error_network_layout.setVisibility(View.GONE);
//                }


                LogUtils.e("ＡＡonPageFinished=" + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);

                LogUtils.e("ＡＡonPageStarted=" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtils.e("ＡＡonReceivedError=" + errorCode);
                LogUtils.e("ＡＡonReceivedErrorfailingUrl=" + failingUrl);
                campaigns_webview.stopLoading();

                campaigns_webview.setVisibility(View.GONE);

                handleNetError();

            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                LogUtils.e("shouldOverrideKeyEvent=");
                return super.shouldOverrideKeyEvent(view, event);


            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                LogUtils.e("onReceivedSslError=");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String valueUrl = ISO8601.decodeURIComponent(url);//decode URL 我们肉眼能看得清楚的URL


                LogUtils.e("ＡＡshouldOverrideUrlLoading_url=" + valueUrl);

                return super.shouldOverrideUrlLoading(view, url);


            }

        });


        campaigns_webview.loadUrl(webURL);
    }


}
