package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ArticlePraise.ArticlePraiseBean;
import cn.chono.yopper.Service.Http.ArticlePraise.ArticlePraiseRespBean;
import cn.chono.yopper.Service.Http.ArticlePraise.ArticlePraiseSericve;
import cn.chono.yopper.Service.Http.ArticlePraiseCancel.ArticlePraiseCancelBean;
import cn.chono.yopper.Service.Http.ArticlePraiseCancel.ArticlePraiseCancelSericve;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.near.ZoomViewerActivity;
import cn.chono.yopper.data.ZoomViewerDto;
import cn.chono.yopper.tencent.TencentShareUtil;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCall;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DealImgUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.weibo.SinaWeiBoUtil;
import cn.chono.yopper.wxapi.WeixinUtils;


public class ArticleContentDetailActivity extends MainFrameActivity {


    private WebView article_content_detail_webview;

    private String count;

    private String weburl;

    private LinearLayout error_network_layout;

    private TextView error_network_tv;


    private boolean istouchContentImg = false;

    private String isType;



    @Override
    public void onResume() {

        super.onResume();

        MobclickAgent.onPageStart("文章详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        MobclickAgent.onResume(this); // 统计时长

        istouchContentImg = false;

    }

    @Override
    public void onPause() {

        super.onPause();

        MobclickAgent.onPageEnd("文章详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        MobclickAgent.onPause(this); // 统计时长

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.article_content_detail_activity);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {

            weburl = bundle.getString(YpSettings.BUNDLE_KEY_WEB_URL);

            if (bundle.containsKey(YpSettings.BUNDLE_KEY_WEB_URL_TYPE)) {

                isType = bundle.getString(YpSettings.BUNDLE_KEY_WEB_URL_TYPE);
            }


        }

        if (TextUtils.isEmpty(isType) || !TextUtils.equals("Article", isType)) {

            if (YpSettings.isTest) {

                weburl = HttpURL.Test_webURL + weburl;

            } else {

                weburl = HttpURL.webURL + weburl;
            }

        }


        initView();

    }



    private void initView() {
        this.getTvTitle().setText("文章详情");

        this.getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        article_content_detail_webview = (WebView) this.findViewById(R.id.article_content_detail_webview);

        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);

        error_network_tv = (TextView) findViewById(R.id.error_network_tv);


        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article_content_detail_webview.setVisibility(View.VISIBLE);
                error_network_layout.setVisibility(View.GONE);
                article_content_detail_webview.loadUrl(weburl);

            }
        });

        article_content_detail_webview.getSettings().setJavaScriptEnabled(true);// 允许使用js
        article_content_detail_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        article_content_detail_webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


                LogUtils.e("onPageFinished=" + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);

                LogUtils.e("onPageStarted=" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                article_content_detail_webview.setVisibility(View.GONE);
                error_network_layout.setVisibility(View.VISIBLE);

            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                LogUtils.e("onReceivedError=");
                return super.shouldOverrideKeyEvent(view, event);


            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String valueUrl = ISO8601.decodeURIComponent(url);//decode URL 我们肉眼能看得清楚的URL
                try {


                    if (valueUrl.toLowerCase().startsWith("http://localhost/return?data=")) {
                        valueUrl = valueUrl.replace("http://localhost/return?data=", "");

                        LogUtils.e("shouldOverrideUrlLoading=" + valueUrl);

                        JSONObject obj = new JSONObject(valueUrl);
                        String type = obj.getString("type");
                        if (!CheckUtil.isEmpty(type) && type.equals("praise")) {//点赞
                            JSONObject dataobj = obj.getJSONObject("data");
                            String articleid = dataobj.getString("articleid");
                            boolean isCancel = dataobj.getBoolean("isCancel");

                            praiseCancel(articleid, isCancel);


                            return true;
                        } else if (!CheckUtil.isEmpty(type) && type.equals("share")) {//分享

                            JSONObject dataobj = obj.getJSONObject("data");

                            String link = dataobj.getString("link");
                            String title = dataobj.getString("title");
                            String content = dataobj.getString("content");
                            String img = dataobj.getString("img");

                            if(CheckUtil.isEmpty(content)){
                                content="这里真的很有料，不要错过哦";
                            }

                            if(content.equals("null")){
                                content="这里真的很有料，不要错过哦";
                            }

                            if (YpSettings.isTest) {
                                link = HttpURL.Test_webURL + link;
                            } else {
                                link = HttpURL.webURL + link;
                            }


                            share(link, title, content, img);

                            return true;

                        } else if (!CheckUtil.isEmpty(type) && type.equals("link")) {//评论


                            JSONObject dataobj = obj.getJSONObject("data");
                            String link = dataobj.getString("link");
                            String count = dataobj.getString("count");

                            Bundle bd = new Bundle();
                            bd.putString(YpSettings.BUNDLE_KEY_WEB_URL, link);
                            bd.putString("count", count);
                            bd.putString(YpSettings.FROM_PAGE, "ArticleContentDetailActivity");

                            ActivityUtil.jump(ArticleContentDetailActivity.this, ArticleCommentListActivity.class, bd, 0, 100);
                            return true;
                        } else if (!CheckUtil.isEmpty(type) && type.equals("getarticle")) {


                            String articleid = obj.getJSONObject("data").getString("articleid");
                            String injected = obj.getJSONObject("data").getString("injected");


                            getPraise(articleid, injected);


                            return true;
                        } else if (!CheckUtil.isEmail(type) && type.equals("touchContentImg")) {


                            if (istouchContentImg) {
                                return true;
                            }

                            istouchContentImg = true;
                            JSONObject dataobj = obj.getJSONObject("data");

                            JSONArray jsonArray = dataobj.getJSONArray("imgList");
                            int index = dataobj.getInt("index");
                            double x = dataobj.getDouble("x");
                            double y = dataobj.getDouble("y");


                            List<String> imgList = new ArrayList<String>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                imgList.add(jsonArray.getString(i));
                            }


                            ZoomViewerDto sq = new ZoomViewerDto();

                            sq.list=imgList;

                            sq.position=index;

                            sq.type="ArticleContentDetailActivity";

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(YpSettings.ZOOM_LIST_DTO, sq);
                            ActivityUtil.jump(ArticleContentDetailActivity.this, ZoomViewerActivity.class, bundle, 0, 200);


                            return true;
                        }


                    }
                } catch (JSONException e) {

                }

                return super.shouldOverrideUrlLoading(view, url);


            }

        });

        article_content_detail_webview.loadUrl(weburl);
    }

    /**
     * 获取自己的点赞状态
     *
     * @param articleid
     */
    private void getPraise(String articleid, final String injected) {

        ArticlePraiseBean praiseBean = new ArticlePraiseBean();
        praiseBean.setId(articleid);

        ArticlePraiseSericve praiseSericve = new ArticlePraiseSericve(this);

        praiseSericve.parameter(praiseBean);

        praiseSericve.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                ArticlePraiseRespBean praiseRespBean = (ArticlePraiseRespBean) respBean;

                boolean isCancel = false;

                if (null != praiseRespBean) {
                    isCancel = praiseRespBean.getResp().isResp();
                }

                isCanclJS(isCancel, injected);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                isCanclJS(false, injected);
            }
        });

        praiseSericve.enqueue();

    }

    /**
     * 注入js,提交点赞状态
     *
     * @param isCancl
     */
    private void isCanclJS(boolean isCancl, String injected) {
        String js = injected + "(" + isCancl + ");";

        LogUtils.e("js=" + js);


        article_content_detail_webview.loadUrl("javascript:" + js);//注入js

    }

    /**
     * 提交点赞状态
     *
     * @param articleid
     * @param isCancel
     */
    private void praiseCancel(String articleid, boolean isCancel) {

        ArticlePraiseCancelBean praiseCancelBean = new ArticlePraiseCancelBean();

        praiseCancelBean.setId(articleid);

        praiseCancelBean.setCancel(isCancel);

        ArticlePraiseCancelSericve praiseCancelSericve = new ArticlePraiseCancelSericve(this);

        praiseCancelSericve.parameter(praiseCancelBean);

        praiseCancelSericve.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);


            }
        }, new OnCallBackFailListener() {
        });

        praiseCancelSericve.enqueue();
    }

    /**
     * 分享
     */
    private void share(final String link, final String title, final String content, final String img) {

        // 分享
        Dialog dialog = DialogUtil.createShareDialog(ArticleContentDetailActivity.this, new BackCall() {

            @Override
            public void deal(int which, Object... obj) {

                switch (which) {
                    case R.id.setting_share_to_sina_weibo:

                        SinaWeiBoUtil.sendMessage(ArticleContentDetailActivity.this,true, false, title, content, img, link);

                        break;

                    case R.id.setting_share_to_qq:

                        TencentShareUtil.shareToQQ(ArticleContentDetailActivity.this, title, content, link, img, getString(R.string.app_name));
                        break;

                    case R.id.setting_share_to_weixin_friend:
                        // 微信朋友圈分享

                        if (WeixinUtils.isWeixinAvailable()) {

                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();

                                   final Bitmap bmp = DealImgUtils.getHtmlByteArray(img);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(null==bmp){
                                                Bitmap  bmpp=BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);
                                                WeixinUtils.sendFriendTextAndPicture(title, content, link, bmpp);
                                            }else{
                                                WeixinUtils.sendFriendTextAndPicture(title, content, link, bmp);
                                            }
                                        }
                                    });

                                }
                            }.start();

                        } else {
                            DialogUtil.showTopToast(ArticleContentDetailActivity.this, "您的手机没有安装微信!");
                        }
                        break;

                    case R.id.setting_share_to_weixin:

                        if (WeixinUtils.isWeixinAvailable()) {

                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();

                                    final Bitmap bmp = DealImgUtils.getHtmlByteArray(img);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(null==bmp){
                                                Bitmap  bmpp=BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);
                                                WeixinUtils.sendTextAndPicture(title, content, link, bmpp);
                                            }else{
                                                WeixinUtils.sendTextAndPicture(title, content, link, bmp);
                                            }
                                        }
                                    });

                                }
                            }.start();
                        } else {
                            DialogUtil.showTopToast(ArticleContentDetailActivity.this, "您的手机没有安装微信!");
                        }

                        break;

                    case R.id.setting_share_to_qq_zone:

                        // http://cdn.duitang.com/uploads/item/201209/03/20120903120924_TSsvE.jpeg
                        ArrayList<String> img_url_list = new ArrayList<String>();
                        img_url_list.add(img);
                        TencentShareUtil.shareToQzone(ArticleContentDetailActivity.this, title, content, link, img_url_list, getString(R.string.app_name));

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
        if (null != article_content_detail_webview) {
//            mWebView.stopLoading();此方法有问题。在android 4.4.4上lenovo上，连续onDestroy()时app会异常停止

            article_content_detail_webview.clearFocus();


            article_content_detail_webview.clearCache(true);
            article_content_detail_webview.clearHistory();
            article_content_detail_webview.destroy();


        }
    }

}
