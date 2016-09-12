package cn.chono.yopper.activity.find;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
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
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/4/6.
 */
public class FindWebActivity extends MainFrameActivity {

    WebView Web_wv;

    ViewStub Web_vs;

    ViewStub Web_localhost_vs;

    LinearLayout error_network_layout;

    TextView error_network_tv;



    String weburl;

    boolean isNeedhost = true;



    boolean isTitle = false;

    String titleName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_find_webview);
        PushAgent.getInstance(this).onAppStart();

        // 获得传入参数
        Bundle bundle = this.getIntent().getExtras();
        receptionData(bundle);
        initView();// 此方法必须在receptionData(bundle)之后，因为要先接收值
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WebView界面"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WebView界面"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    private void initView() {


        if (isTitle) {

            getTitleLayout().setVisibility(View.VISIBLE);

            getGoBackLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            getTvTitle().setText(titleName);
        }else
            getTitleLayout().setVisibility(View.GONE);


        Web_wv = (WebView) findViewById(R.id.Web_wv);

        Web_vs = (ViewStub) findViewById(R.id.Web_vs);

        Web_localhost_vs = (ViewStub) findViewById(R.id.Web_localhost_vs);

        Web_wv.getSettings().setJavaScriptEnabled(true);// 允许使用js
        Web_wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        Web_wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.e("onPageFinished=" + url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Web_localhost_vs.setVisibility(View.GONE);

                LogUtils.e("onPageStarted=" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Web_wv.setVisibility(View.GONE);
                NoData(View.VISIBLE);
                LogUtils.e("onPageFinished=" + failingUrl);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                finish();

                return true;
            }
        });
        Web_wv.loadUrl(weburl);
    }

    private void receptionData(Bundle bundle) {

        if (bundle == null) {
            DialogUtil.showTopToast(this, "没有可浏览的页面！");
            this.finish();
            return;
        }
        // 取出要显示的weburl地址
        weburl = bundle.getString(YpSettings.BUNDLE_KEY_WEB_URL);

        LogUtils.e("WEBURL_" + weburl);

        if (TextUtils.isEmpty(weburl)) {
            // DialogUtil.showToast(this, "指定页面未找到！");
            this.finish();
            return;
        }

        if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_NEED_HOST)) {

            isNeedhost = bundle.getBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST);
        }

        if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE)) {

            isTitle = bundle.getBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

        }

        if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_TITLE)) {

            titleName = bundle.getString(YpSettings.BUNDLE_KEY_WEB_TITLE);
        }

        if (isNeedhost) {
            if (YpSettings.isTest) {
                weburl = HttpURL.Test_webURL + weburl;
            } else {
                weburl = HttpURL.webURL + weburl;
            }
        }
    }



    private void NoData(int Visibility) {
        if (Visibility == View.GONE) {
            Web_vs.setVisibility(View.GONE);
        } else {


            Web_vs.setVisibility(View.VISIBLE);

            error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);
            error_network_layout.setVisibility(View.VISIBLE);
            error_network_tv = (TextView) findViewById(R.id.error_network_tv);

            error_network_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Web_localhost_vs.setVisibility(View.VISIBLE);
                    Web_wv.loadUrl(weburl);
                    Web_wv.setVisibility(View.VISIBLE);

                    NoData(View.GONE);

                }
            });
        }

    }


}
