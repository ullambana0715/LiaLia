package cn.chono.yopper.activity.base;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andbase.tractor.listener.LoadListener;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ActivitySignStatus.ActivitySignReqBean;
import cn.chono.yopper.Service.Http.ActivitySignStatus.ActivitySignRespBean;
import cn.chono.yopper.Service.Http.ActivitySignStatus.ActivitySignService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.SignUpActivity.SighUpRespBean;
import cn.chono.yopper.Service.Http.SignUpActivity.SighUpService;
import cn.chono.yopper.Service.Http.SignUpActivity.SignUpReqBean;
import cn.chono.yopper.Service.Http.WebHttpService.WebHttpService;
import cn.chono.yopper.activity.appointment.ReleaseAppointmentActivity;
import cn.chono.yopper.activity.order.UserOrderPayActivity;
import cn.chono.yopper.activity.usercenter.SettingPhoneActivity;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.tencent.TencentShareUtil;
import cn.chono.yopper.ui.UserInfoEditActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtils;
import cn.chono.yopper.utils.BackCall;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DealImgUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.URLExecutor;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.weibo.SinaWeiBoUtil;
import cn.chono.yopper.wxapi.WeixinUtils;

/**
 * 简单网页载入，浏览页面
 *
 * @author zxb
 */
public class SimpleWebViewActivity extends MainFrameActivity implements
        OnClickListener {
    private String titleStr;
    private String weburl;

    // 需要隐藏本身的title，使用web页里面的导航和标题
    private boolean needHideTitle;

    private boolean isRewardList = false;

    private boolean isRewardDetail = false;

    private LinearLayout backBut;

    private WebView mWebView;
    private RelativeLayout no_net_layout;
    private TextView simple_webview_reload;

    private boolean isNeedhost = true;


    private TextView localhost_tv;

    /**
     * 约会类型
     */
    private int APPOINTMENT_YTPE;
    /**
     * 标记来源。区分是不是由发布约会与约会详情过来的。默认为0,表示不是
     */
    private int SOURCE_YTPE = 0;

    private boolean isFree;
    String activityId;
    int signUpStatus;
    int activityStatus;

    private String category;


    private List<String> source_url_type = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.simple_webview);
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

    /**
     * 接收界面传递的值
     *
     * @param bundle
     */
    private void receptionData(Bundle bundle) {

        if (bundle == null) {
            DialogUtil.showTopToast(this, "没有可浏览的页面！");
            this.finish();
            return;
        }


        // 取出要显示的weburl地址
        weburl = bundle.getString(YpSettings.BUNDLE_KEY_WEB_URL);
        // weburl="https://www.baidu.com/";

        titleStr = bundle.getString(YpSettings.BUNDLE_KEY_WEB_TITLE);
        // title="百度";
        needHideTitle = bundle.getBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

        if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_NEED_HOST)) {
            isNeedhost = bundle.getBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST, true);
        }

        //发布约会时传过来的约会类型
        APPOINTMENT_YTPE = bundle.getInt(YpSettings.APPOINTMENT_INTENT_YTPE);
        //标记来源。区分是不是由发布约会与约会详情过来的。默认为0,表示不是
        SOURCE_YTPE = bundle.getInt(YpSettings.SOURCE_YTPE_KEY);


        if (TextUtils.isEmpty(weburl)) {
            // DialogUtil.showToast(this, "指定页面未找到！");
            this.finish();
            return;
        }

        if (isNeedhost) {
            if (YpSettings.isTest) {
                weburl = HttpURL.Test_webURL + weburl;
            } else {
                weburl = HttpURL.webURL + weburl;
            }
        }

        if (100 == SOURCE_YTPE) {//区分是不是发布约会
            if (weburl.toLowerCase().endsWith("app/loader.html")) {//发布约会时，需要拼接 tmep字段，用于防止web页面的缓存。temp是一个随机数

                weburl = weburl + "?temp=" + System.currentTimeMillis();
            }


        } else if (300 == SOURCE_YTPE) {//、约会详情

            if (weburl.contains("?")) {//需要拼接 tmep字段和 HiddenBtn，用于防止web页面的缓存和告诉web没有点击确认的BUTTON。temp是一个随机数
                weburl = weburl + "&HiddenBtn=TRUE&temp=" + System.currentTimeMillis();
            } else {
                weburl = weburl + "?HiddenBtn=TRUE&temp=" + System.currentTimeMillis();
            }


        }

        if (600 == SOURCE_YTPE) {
            mSignUp = (Button) findViewById(R.id.signup);
            mSignUp.setOnClickListener(this);
            gettvOption().setText("分享");
            gettvOption().setTextColor(getResources().getColor(android.R.color.black));
            getOptionLayout().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (YpSettings.isTest) {
                        share(HttpURL.Test_webURL + shareUrl, shareTitle, shareContent, shareImgUrl);
                    } else {
                        share(HttpURL.webURL + shareUrl, shareTitle, shareContent, shareImgUrl);
                    }

                }
            });
//            isFree = bundle.getInt(YpSettings.ACTIVITY_FEE) == 0 ? true:false;
            activityId = bundle.getString(YpSettings.ACTIVITY_ID);
//            activityStatus = bundle.getInt(YpSettings.ACTIVITY_STATUS);
//            signUpStatus = bundle.getInt(YpSettings.SIGHUP_STATUS);
            getSignStatus();
        }


        if (weburl.startsWith(HttpURL.webURL + "reward/awardlist")) {
            isRewardList = true;
        } else {
            isRewardList = false;
        }
    }

    String shareTitle;
    String shareContent;
    String shareImgUrl;
    String shareUrl;

    void getSignStatus() {
        final ActivitySignReqBean reqBean = new ActivitySignReqBean();
        reqBean.setActivityId(activityId);
        ActivitySignService service = new ActivitySignService(this);
        service.parameter(reqBean);
        service.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                ActivitySignRespBean bean = (ActivitySignRespBean) respBean;
                activityStatus = bean.getResp().getStatus();
                setButton();
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
            }
        });
        service.enqueue();
    }

    void setButton() {
        switch (activityStatus) {// 活动状态（0：正常 1：人数已满 2：报名已截止 3：已取消 4：已结束）
            case 0:
                mSignUp.setText("报名");
                mSignUp.setBackgroundResource(R.drawable.corners_ff7462_top_style);
                mSignUp.setClickable(true);
                break;
            case 1:
                mSignUp.setText("报名已满");
                mSignUp.setBackgroundResource(R.drawable.shape_corner_grey);
                mSignUp.setClickable(false);
                break;
            case 2:
                mSignUp.setText("报名时间已截止");
                mSignUp.setBackgroundResource(R.drawable.shape_corner_grey);
                mSignUp.setClickable(false);
                break;
            case 3:
                mSignUp.setText("活动已取消");
                mSignUp.setBackgroundResource(R.drawable.shape_corner_grey);
                mSignUp.setClickable(false);
                break;
            case 4:
                mSignUp.setText("已结束");
                mSignUp.setBackgroundResource(R.drawable.shape_corner_grey);
                mSignUp.setClickable(false);
                break;
        }
    }

    Dialog confirmDialog;
    Dialog okDialog;
    Button mSignUp;

    Dialog minfoDialog, mPhoneDialog;

    /**
     * 组件初始化和数据绑定
     */
    private void initView() {

        no_net_layout = (RelativeLayout) findViewById(R.id.no_net_layout);
        no_net_layout.setOnClickListener(this);
        simple_webview_reload = (TextView) findViewById(R.id.data_reload_tv);
        mWebView = (WebView) findViewById(R.id.simple_webview);
        localhost_tv = (TextView) findViewById(R.id.localhost_tv);
        simple_webview_reload.setOnClickListener(this);

        if (SOURCE_YTPE == 600) {
            mSignUp.setVisibility(View.VISIBLE);
            mSignUp.setClickable(true);
        }

        // 设置标题栏
        getTvTitle().setText(titleStr);
        getBtnGoBack().setVisibility(View.VISIBLE);
        if (SOURCE_YTPE == 600) {
            getOptionLayout().setVisibility(View.VISIBLE);
        } else {
            getOptionLayout().setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(titleStr)) {
            this.getTvTitle().setText(titleStr);
        }
        if (needHideTitle) {
            this.getTitleLayout().setVisibility(View.GONE);
        }


        backBut = getGoBackLayout();
        backBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);
                if (isRewardDetail) {
                    mWebView.goBack();
                    isRewardDetail = false;
                } else {
                    finish();
                }

            }
        });

        okDialog = DialogUtil.createHintOperateDialog(this, "", "报名成功", "", "确定", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                okDialog.dismiss();
            }

            @Override
            public void onEnsure(View view, Object... obj) {

                okDialog.dismiss();

                finish();
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);// 允许使用js
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


                if (100 == SOURCE_YTPE) {//发布约会过来的


                    url = ISO8601.decodeURIComponent(url);//先把地址 decode我们所认识的地址
                    LogUtils.e("onPageFinished=" + url);

                    if (url.toLowerCase().startsWith("http://m.dianping.com/shop/")) {
                        isRewardDetail = true;
                    } else if (url.toLowerCase().startsWith("http://m.gewara.com/movie/m/choicecinema.xhtml")) {
                        LogUtils.e("isRewardDetail=" + "直=");
                        isRewardDetail = true;
                    }

                    LogUtils.e("isRewardDetail=" + isRewardDetail);

                    //获取到需要的webSite,用于js的注入
                    String webSite = "";
                    if (YpSettings.isTest) {

                        webSite = HttpURL.Test_webURL;
                    } else {
                        webSite = HttpURL.webURL;
                    }


                    try {

                        if (url.toLowerCase().startsWith("intent://")) {//这个地址不做处理

                        } else if (url.toLowerCase().startsWith("http://localhost/return_to_native")) {//选择确认后的处理。拿到的地址中返回来的data数据。需要自己去截取

                            int index = url.indexOf("?");
                            int urlSize = url.length();
                            String dataStr = null;
                            if (-1 != index) {
                                dataStr = (String) url.subSequence(index + 1, urlSize);
                            }

                            LogUtils.e("dataStr=" + dataStr);

                            String lodeData = null;
                            if (!TextUtils.isEmpty(dataStr)) {
                                int data_index = dataStr.indexOf("=");
                                int data_size = dataStr.length();
                                lodeData = (String) dataStr.subSequence(data_index + 1, data_size);
                            }

                            Intent webIntent = new Intent();
                            if (APPOINTMENT_YTPE == Constant.APPOINT_TYPE_MOVIE) {//判断从那个界面过来的。用于返回到具体的界面，
                                webIntent.setClass(SimpleWebViewActivity.this, ReleaseAppointmentActivity.class);
                            }

                            Bundle webBundle = new Bundle();
                            webBundle.putString(YpSettings.WEB_DATA_KEY, lodeData);

                            webIntent.putExtras(webBundle);

                            setResult(YpSettings.WEB_CODE, webIntent);
                            LogUtils.e("lodeData=" + lodeData);

                            finish();


                        } else if (url.toLowerCase().startsWith(webSite + "app/loader.html")) {//判断是webSite+app/loader.html

                            if (APPOINTMENT_YTPE == Constant.APPOINT_TYPE_MOVIE) {//是不是电影的
//                                typeStr = "movie_list";

                                String js = URLExecutor.getMovieJS(webSite, "new_movie_list");//获取js
                                LogUtils.e("jsUrl=" + js);
                                view.loadUrl("javascript:" + js);//注入js

                            } else {//不是电影类型的。
                                JSONObject jsonObject = new JSONObject();//需要生成一个json字符串。

                                jsonObject.put("category", titleStr);//category相对应的是，吃饭，喝酒，咖啡等。标题中就是这些文字，所以直接拿来用

                                String category = jsonObject.toString();

                                String js = URLExecutor.getLoaderJS(webSite, "shop_list", category);//获取js
                                LogUtils.e("jsUrl=" + js);
                                view.loadUrl("javascript:" + js);//注入js
                            }


                        } else {//判断不是app/loader.html类型结尾的。所以再判断是不是电影的。来区分注入js的类型

                            if (APPOINTMENT_YTPE == Constant.APPOINT_TYPE_MOVIE) {//电影的。
//                                typeStr = "movie_list";
                                String host = URLExecutor.getHttpHost(url);//获取地址中的域名
                                String js = URLExecutor.getMovieJS(webSite, host);//获取js
                                LogUtils.e("jsUrl=" + js);
                                view.loadUrl("javascript:" + js);

                            } else {//不是电影的

                                String host = URLExecutor.getHttpHost(url);//获取地址中的域名
                                String js = URLExecutor.getLoaderJS(webSite, host, category);//category 是在shouldOverrideUrlLoading（）方法中 http://localhost/redirect类型中截取出来的。也是json字符串

                                LogUtils.e("jsUrl=" + js);

                                view.loadUrl("javascript:" + js);//获取js
                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (300 == SOURCE_YTPE) {//这个是约会详情过来的。也需要 webSite ，地址中的域名。然后注入js

                    String webSite = "";
                    if (YpSettings.isTest) {

                        webSite = HttpURL.Test_webURL;
                    } else {
                        webSite = HttpURL.webURL;
                    }


                    url = ISO8601.decodeURIComponent(url);
                    LogUtils.e("onPageFinished=" + url);

                    String host = URLExecutor.getHttpHost(url);
                    String js = URLExecutor.getMovieJS(webSite, host);
                    LogUtils.e("jsUrl=" + js);
                    view.loadUrl("javascript:" + js);//获取js

                } else if (600 == SOURCE_YTPE) {
                    if (mSignUp.getVisibility() == View.VISIBLE) {
                        localhost_tv.setVisibility(View.GONE);
                        no_net_layout.setVisibility(View.GONE);
                    } else {
                        localhost_tv.setVisibility(View.GONE);
                        no_net_layout.setVisibility(View.VISIBLE);
                    }

                } else {
                    localhost_tv.setVisibility(View.GONE);
                    no_net_layout.setVisibility(View.GONE);
                }


            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);

                localhost_tv.setVisibility(View.VISIBLE);
                no_net_layout.setVisibility(View.GONE);

                LogUtils.e("onPageStarted=" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // DialogUtil.showAlert(SimpleWebViewActivity.this, "载入时发生错误:" +
                // errorCode, description);

                if (SOURCE_YTPE == 0) {
                    localhost_tv.setVisibility(View.GONE);
                    no_net_layout.setVisibility(View.VISIBLE);

                } else if (SOURCE_YTPE == 600) {
                    localhost_tv.setVisibility(View.GONE);
                    no_net_layout.setVisibility(View.VISIBLE);
                    mSignUp.setVisibility(View.GONE);
                }


                LogUtils.e("onReceivedError=" + failingUrl);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                LogUtils.e("onReceivedError=");
                return super.shouldOverrideKeyEvent(view, event);


            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                super.onReceivedSslError(view, handler, error);

                if (SOURCE_YTPE == 0) {
                    localhost_tv.setVisibility(View.GONE);
                    no_net_layout.setVisibility(View.VISIBLE);

                } else if (SOURCE_YTPE == 600) {
                    localhost_tv.setVisibility(View.GONE);
                    no_net_layout.setVisibility(View.VISIBLE);
                    mSignUp.setVisibility(View.GONE);
                }

                LogUtils.e("onReceivedError=" + "");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e("shouldOverrideUrlLoading=" + url);

                if (SOURCE_YTPE == 600) {
                    mSignUp.setVisibility(View.VISIBLE);
                    String valueUrl = ISO8601.decodeURIComponent(url);//decode URL 我们肉眼能看得清楚的URL
                    if (valueUrl.toLowerCase().startsWith("http://localhost/return?data=")) {
                        valueUrl = valueUrl.replace("http://localhost/return?data=", "");

                        LogUtils.e("shouldOverrideUrlLoading=" + valueUrl);

                        try {
                            JSONObject obj = new JSONObject(valueUrl);
                            String type = obj.getString("type");
                            if (!CheckUtil.isEmpty(type) && type.equals("detail")) {//点赞
                                JSONObject dataobj = obj.getJSONObject("data");
                                shareUrl = dataobj.getString("link");
                                shareTitle = dataobj.getString("title");
                                shareContent = dataobj.getString("content");
                                shareImgUrl = dataobj.getString("img");
//                                activityStatus = Integer.parseInt(dataobj.getString("status"));
                                setButton();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    return true;
                }

                if (SOURCE_YTPE != 0) {//发布约会与约会详情过来的。为了简单，就不单独对发布与约会详情做判断。因为约会详情刚开始访问时，加入了HiddenBtn=TRUE。已经隐藏掉了选 择的按钮。所以约会详情不会走到这里。
                    String valueUrl = ISO8601.decodeURIComponent(url);//decode URL 我们肉眼能看得清楚的URL

                    LogUtils.e("valueUrl=" + valueUrl);


                    if (valueUrl.toLowerCase().startsWith("http://localhost/redirect")) {//判断是不是以http://localhost/redirect 开头的。如果 是。则要截取URL字符串

                        category = URLExecutor.getCategory(valueUrl);//获取 字符串category字段的值 。用于访问时，做为js的参数
                        String access = URLExecutor.getAccessHttpURL(valueUrl, APPOINTMENT_YTPE);//截取访问 地址。然后重新访问 ，访问成功。以category做为js的参数注入js
                        LogUtils.e("access=" + access);


                        view.loadUrl(access);

                        return true;

                    } else if (valueUrl.toLowerCase().startsWith("http://localhost/event/page_injected")) {//这个类型。是注入js最终成功后，要显示的列表信息了。

                        LogUtils.e("page_injected=");

                        _handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);//延迟500ms，为了在视觉上js注入的时间差。
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                                localhost_tv.setVisibility(View.GONE);
                                no_net_layout.setVisibility(View.GONE);
                            }

                        });
                        return true;
                    } else if (valueUrl.toLowerCase().startsWith("http://localhost/httprequest")) {//这个类型。。也需要截取地址。然后以这个地址访问我们自己的web 服务 ，然后再进行处理

                        String access = URLExecutor.getHttprequestURL(valueUrl);//获取访问地址 ，访问自己的服务器
                        String callback = URLExecutor.getCallback(valueUrl);//同时要获取地址中callback中的值 ，用于访问自己服务器后作为js 的参数

                        webHttp(access, callback);//访问自己的服务器

                        return true;
                    } else if (valueUrl.toLowerCase().startsWith("intent://shopinfo") || valueUrl.toLowerCase().startsWith("dianping://shopinfo")) {
                        return true;
                    } else {

                        return false;
                    }


                } else {//不是约会详情，也不是发布约会的


                    try {
                        if (URLExecutor.execute(url, SimpleWebViewActivity.this, 0)) {
                            return true;
                        }
                        if (url.toLowerCase().endsWith(".apk")) {
                            ActivityUtil.jumpToWeb(SimpleWebViewActivity.this, url);
                            return true;
                        }

                        if (isRewardList) {

                            if (url != null
                                    && url.startsWith(HttpURL.webURL + "Reward/")) {
                                isRewardDetail = true;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    return super.shouldOverrideUrlLoading(view, url);
                }


            }

        });


        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             * 页面关闭
             */
            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                finish();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {


                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {

                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {

                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(SimpleWebViewActivity.this.titleStr)) {
                    getTvTitle().setText(title);
                }

            }

        });


        mWebView.loadUrl(weburl);


    }

    private Handler _handler = new Handler();


    private void webHttp(String url, final String callback) {

        LogUtils.e("url=" + url);

        if (TextUtils.isEmpty(url)) {

            return;

        }

        WebHttpService webHttpService = new WebHttpService();

        webHttpService.enqueue(url, new LoadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(Object result) {

            }

            @Override
            public void onFail(Object result) {


                //失败后。需要拼接json 作为js 的参数
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("code", -1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String js = callback + "(" + jsonObject.toString() + ");";

                LogUtils.e("webHttp=" + js);
                mWebView.loadUrl("javascript:" + js);//注入js
            }

            @Override
            public void onSuccess(Object result) {
                String resultStr = result.toString();
                //成功后。需要拼接json 作为js 的参数
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("code", 200);
                    jsonObject.put("data", resultStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String js = callback + "(" + jsonObject.toString() + ");";

                LogUtils.e("webHttp=" + js);
                mWebView.loadUrl("javascript:" + js);//注入js
            }

            @Override
            public void onCancel(Object result) {

            }

            @Override
            public void onTimeout(Object result) {

            }

            @Override
            public void onCancelClick() {

            }
        });


    }


    @Override
    public void onClick(View v) {
        if (SOURCE_YTPE == 600) {
            switch (v.getId()) {
                case R.id.no_net_layout:
                case R.id.data_reload_tv:
                    ViewsUtils.preventViewMultipleClick(v, 1000);

                    localhost_tv.setVisibility(View.VISIBLE);
                    no_net_layout.setVisibility(View.GONE);
                    mWebView.loadUrl(weburl);
                    break;

                case R.id.signup:
                    confirmDialog = DialogUtil.createHintOperateDialog(SimpleWebViewActivity.this, "",
                            "请确定填写信息的真实性，否则线下无法参加活动", "取消", "确定", new BackCallListener() {
                                @Override
                                public void onCancel(View view, Object... obj) {
                                    confirmDialog.dismiss();
                                }

                                @Override
                                public void onEnsure(View view, Object... obj) {
                                    confirmDialog.dismiss();
                                    final SignUpReqBean reqBean = new SignUpReqBean();
                                    reqBean.setId(activityId);
                                    reqBean.setUseFee(false);
                                    SighUpService service = new SighUpService(SimpleWebViewActivity.this);
                                    service.parameter(reqBean);
                                    service.callBack(new OnCallBackSuccessListener() {
                                        @Override
                                        public void onSuccess(RespBean respBean) {
                                            super.onSuccess(respBean);
                                            SighUpRespBean bean = (SighUpRespBean) respBean;
                                            // 报名结果（0：成功 1：手机号未认证 2：资料不完善 3：重复报名 4：报名人数已满 5：超过报名截止时间）
                                            switch (bean.getResp().getResult()) {

                                                case 0:

                                                    if (TextUtils.isEmpty(bean.getResp().getOrderId())) {

                                                        okDialog.show();

                                                    } else {

                                                        Bundle bundle = new Bundle();

                                                        bundle.putString(YpSettings.ORDER_ID, bean.getResp().getOrderId());

                                                        bundle.putString(YpSettings.ACTIVITY_ID, activityId);

                                                        ActivityUtil.jump(SimpleWebViewActivity.this, UserOrderPayActivity.class, bundle, 0, 0);

                                                        finish();
                                                    }
                                                    break;

                                                case 1:
                                                    mPhoneDialog = DialogUtil.createHintOperateDialog(SimpleWebViewActivity.this, "认证", bean.getResp().getMsg(), "", "确定", new BackCallListener() {
                                                        @Override
                                                        public void onCancel(View view, Object... obj) {

                                                        }

                                                        @Override
                                                        public void onEnsure(View view, Object... obj) {

                                                            mPhoneDialog.dismiss();

                                                            Bundle bundle = new Bundle();

                                                            bundle.putInt(YpSettings.FROM_PAGE, 1000);

                                                            ActivityUtil.jump(SimpleWebViewActivity.this, SettingPhoneActivity.class, bundle, 0, 0);
                                                        }
                                                    });
                                                    mPhoneDialog.show();

                                                    break;
                                                case 2:

                                                    minfoDialog = DialogUtil.createHintOperateDialog(SimpleWebViewActivity.this, "认证", bean.getResp().getMsg(), "", "确定", new BackCallListener() {
                                                        @Override
                                                        public void onCancel(View view, Object... obj) {

                                                        }

                                                        @Override
                                                        public void onEnsure(View view, Object... obj) {

                                                            minfoDialog.dismiss();

                                                            Bundle bundle = new Bundle();

                                                            bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());

                                                            AppUtils.jump(SimpleWebViewActivity.this, UserInfoEditActivity.class, bundle);

                                                        }
                                                    });
                                                    minfoDialog.show();
                                                    break;
                                                case 3:
                                                case 4:
                                                case 5:
                                                    Toast.makeText(SimpleWebViewActivity.this, bean.getResp().getMsg(), Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    }, new OnCallBackFailListener() {
                                        @Override
                                        public void onFail(RespBean respBean) {
                                            super.onFail(respBean);


                                            if (TextUtils.isEmpty(respBean.getMsg())) {

                                                DialogUtil.showDisCoverNetToast(SimpleWebViewActivity.this);

                                                return;

                                            }

                                            DialogUtil.showDisCoverNetToast(SimpleWebViewActivity.this, respBean.getMsg());
                                        }
                                    });

                                    service.enqueue();
                                }
                            });
                    confirmDialog.show();
                    break;
            }
        } else {

            ViewsUtils.preventViewMultipleClick(v, 1000);

            localhost_tv.setVisibility(View.VISIBLE);

            no_net_layout.setVisibility(View.GONE);

            mWebView.loadUrl(weburl);
        }
    }

    private void share(final String link, final String title, final String content, final String img) {

        // 分享
        Dialog dialog = DialogUtil.createShareDialog(SimpleWebViewActivity.this, new BackCall() {

            @Override
            public void deal(int which, Object... obj) {

                switch (which) {
                    case R.id.setting_share_to_sina_weibo:

                        SinaWeiBoUtil.sendMessage(SimpleWebViewActivity.this, true, false, title, content, img, link);

                        break;

                    case R.id.setting_share_to_qq:

                        TencentShareUtil.shareToQQ(SimpleWebViewActivity.this, title, content, link, img, getString(R.string.app_name));
                        break;

                    case R.id.setting_share_to_weixin_friend:
                        // 微信朋友圈分享

                        if (WeixinUtils.isWeixinAvailable()) {

                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    final Bitmap bmp = DealImgUtils.getHtmlByteArray(img);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null == bmp) {
                                                Bitmap bmpp = BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);
                                                WeixinUtils.sendFriendTextAndPicture(title, content, link, bmpp);
                                            } else {
                                                WeixinUtils.sendFriendTextAndPicture(title, content, link, bmp);
                                            }
                                        }
                                    });

                                }
                            }.start();

                        } else {
                            DialogUtil.showTopToast(SimpleWebViewActivity.this, "您的手机没有安装微信!");
                        }
                        break;

                    case R.id.setting_share_to_weixin:

                        if (WeixinUtils.isWeixinAvailable()) {

                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    final Bitmap bmp = DealImgUtils.getHtmlByteArray(img);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null == bmp) {
                                                Bitmap bmpp = BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);
                                                WeixinUtils.sendTextAndPicture(title, content, link, bmpp);
                                            } else {
                                                WeixinUtils.sendTextAndPicture(title, content, link, bmp);
                                            }
                                        }
                                    });

                                }
                            }.start();
                        } else {
                            DialogUtil.showTopToast(SimpleWebViewActivity.this, "您的手机没有安装微信!");
                        }

                        break;

                    case R.id.setting_share_to_qq_zone:

                        // http://cdn.duitang.com/uploads/item/201209/03/20120903120924_TSsvE.jpeg
                        ArrayList<String> img_url_list = new ArrayList<String>();
                        img_url_list.add(img);
                        TencentShareUtil.shareToQzone(SimpleWebViewActivity.this, title, content, link, img_url_list, getString(R.string.app_name));

                        break;

                    default:
                        break;
                }

                super.deal(which, obj);
            }

        });
        if (!isFinishing()) {
            dialog.show();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWebView) {
//            mWebView.stopLoading();此方法有问题。在android 4.4.4上lenovo上，连续onDestroy()时app会异常停止

            mWebView.clearFocus();


            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.destroy();


        }


    }


}
