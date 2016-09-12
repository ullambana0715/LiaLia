package cn.chono.yopper.activity.find;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * Created by yangjinyu on 16/4/6.
 */
public class WebActivity extends Activity implements View.OnClickListener {

    private WebView mWebView;

    private RelativeLayout no_net_layout;

    private TextView simple_webview_reload;

    String weburl;

    boolean isNeedhost = true;

    TextView localhost_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.simple_webview);

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

        no_net_layout = (RelativeLayout) findViewById(R.id.no_net_layout);

        simple_webview_reload = (TextView) findViewById(R.id.data_reload_tv);

        mWebView = (WebView) findViewById(R.id.simple_webview);

        localhost_tv = (TextView) findViewById(R.id.localhost_tv);

        simple_webview_reload.setOnClickListener(this);

        mWebView.getSettings().setJavaScriptEnabled(true);// 允许使用js

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                localhost_tv.setVisibility(View.GONE);

                no_net_layout.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                localhost_tv.setVisibility(View.VISIBLE);

                no_net_layout.setVisibility(View.GONE);

                LogUtils.e("onPageStarted=" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                localhost_tv.setVisibility(View.GONE);

                no_net_layout.setVisibility(View.VISIBLE);
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
        mWebView.loadUrl(weburl);
    }

    private void receptionData(Bundle bundle) {

        if (bundle == null) {
            DialogUtil.showTopToast(this, "没有可浏览的页面！");
            this.finish();
            return;
        }
        // 取出要显示的weburl地址
        weburl = bundle.getString(YpSettings.BUNDLE_KEY_WEB_URL);

        LogUtils.e("WEBURL_"+weburl);

        if (TextUtils.isEmpty(weburl)) {
            // DialogUtil.showToast(this, "指定页面未找到！");
            this.finish();
            return;
        }

        if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_NEED_HOST)){

            isNeedhost=bundle.getBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST);
        }

        if (isNeedhost) {

            if (YpSettings.isTest) {

                weburl = HttpURL.Test_webURL + weburl;

            } else {

                weburl = HttpURL.webURL + weburl;
            }
        }
    }
    @Override
    public void onClick(View v) {

        ViewsUtils.preventViewMultipleClick(v, 1000);

        localhost_tv.setVisibility(View.VISIBLE);

        no_net_layout.setVisibility(View.GONE);

        mWebView.loadUrl(weburl);

    }
}
