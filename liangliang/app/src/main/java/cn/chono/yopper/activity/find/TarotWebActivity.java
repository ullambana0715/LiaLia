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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ISO8601;

/**
 * Created by cc on 16/4/6.
 */
public class TarotWebActivity extends MainFrameActivity {
    WebView tarotWeb_wv;

    ViewStub tarotWeb_vs;

    ViewStub tarotWeb_localhost_vs;

    LinearLayout error_network_layout;

    TextView error_network_tv;


    String mWebUrl, mTitle;

    LinearLayout tarotWeb_ll_bg;

    Button tarotWeb_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_tarot_web);

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


    private void NoData(int Visibility) {
        if (Visibility == View.GONE) {
            tarotWeb_vs.setVisibility(View.GONE);
        } else {


            tarotWeb_vs.setVisibility(View.VISIBLE);

            error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);
            error_network_layout.setVisibility(View.VISIBLE);
            error_network_tv = (TextView) findViewById(R.id.error_network_tv);

            error_network_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tarotWeb_localhost_vs.setVisibility(View.VISIBLE);
                    tarotWeb_wv.loadUrl(mWebUrl);
                    tarotWeb_wv.setVisibility(View.VISIBLE);

                    NoData(View.GONE);

                }
            });
        }

    }

    private void initView() {


        tarotWeb_wv = (WebView) findViewById(R.id.tarotWeb_wv);

        tarotWeb_vs = (ViewStub) findViewById(R.id.tarotWeb_vs);

        tarotWeb_localhost_vs = (ViewStub) findViewById(R.id.tarotWeb_localhost_vs);

        tarotWeb_ll_bg = (LinearLayout) findViewById(R.id.tarotWeb_ll_bg);
        tarotWeb_btn = (Button) findViewById(R.id.tarotWeb_btn);

        if (TextUtils.equals(YpSettings.BUNDLE_KEY_WEB_TITLE, mTitle)) {

            tarotWeb_ll_bg.setVisibility(View.VISIBLE);
            tarotWeb_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle tarotBd = new Bundle();

                    tarotBd.putInt(YpSettings.COUNSEL_TYPE, Constant.CounselorType_Astrology);
                    ActivityUtil.jump(TarotWebActivity.this, TarotOrAstrologysListActivity.class, tarotBd, 1, 100);

                    finish();
                }
            });

        }


        tarotWeb_wv.getSettings().setJavaScriptEnabled(true);// 允许使用js
        tarotWeb_wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        tarotWeb_wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                tarotWeb_localhost_vs.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                tarotWeb_wv.setVisibility(View.GONE);
                NoData(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String urls = ISO8601.decodeURIComponent(url);
                LogUtils.e(urls);

                String type = eventIntercepting(urls);

                if (TextUtils.equals("tarotDivision", type)) {

                    Bundle tarotBd = new Bundle();

                    tarotBd.putInt(YpSettings.COUNSEL_TYPE, Constant.CounselorType_Tarot);
                    ActivityUtil.jump(TarotWebActivity.this, TarotOrAstrologysListActivity.class, tarotBd, 1, 100);
                    finish();
                    return true;
                } else if (TextUtils.equals("closeWeb", type)) {


                    finish();
                    return true;
                }


                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        tarotWeb_wv.loadUrl(mWebUrl);
    }


    private String eventIntercepting(String url) {
        int url_end = url.length();
        int start_index = url.indexOf("?");

        String dataUrl = (String) url.subSequence(start_index + 1, url_end);

        if (TextUtils.isEmpty(dataUrl)) {
            return dataUrl;
        }

        if (!dataUrl.toLowerCase().startsWith("data")) {
            return dataUrl;
        }


        int dataUrl_end = dataUrl.length();
        int dataUrl_index = dataUrl.indexOf("=");

        String dataJson = (String) dataUrl.subSequence(dataUrl_index + 1, dataUrl_end);

        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            return jsonObject.getString("type");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return "";
    }

    private void receptionData(Bundle bundle) {

        if (bundle == null) {
            DialogUtil.showTopToast(this, "没有可浏览的页面！");
            finish();
            return;
        }


        // 取出要显示的weburl地址
        if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_URL))
            mWebUrl = bundle.getString(YpSettings.BUNDLE_KEY_WEB_URL);
        // 取出要显示的标题
        if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_TITLE))
            mTitle = bundle.getString(YpSettings.BUNDLE_KEY_WEB_TITLE);
        if (TextUtils.isEmpty(mWebUrl)) {
            // DialogUtil.showToast(this, "指定页面未找到！");
            finish();
            return;
        }


        if (YpSettings.isTest) {
            mWebUrl = HttpURL.Test_webURL + mWebUrl;
        } else {
            mWebUrl = HttpURL.webURL + mWebUrl;
        }
    }


}
