package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ArticleCommentOtherComment.ArticleCommentOtherCommentBean;
import cn.chono.yopper.Service.Http.ArticleCommentOtherComment.ArticleCommentOtherCommentService;
import cn.chono.yopper.Service.Http.ArticleReportComment.ArticleReportCommentBean;
import cn.chono.yopper.Service.Http.ArticleReportComment.ArticleReportCommentService;
import cn.chono.yopper.Service.Http.ArticleTalkinjectedService.ArticleTalkinjectedBean;
import cn.chono.yopper.Service.Http.ArticleTalkinjectedService.ArticleTalkinjectedService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.adapter.EmoViewPagerAdapter;
import cn.chono.yopper.adapter.EmoteAdapter;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.ArticleDataBean;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.FaceTextUtils;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.ResizeLayout;

public class ArticleCommentListActivity extends MainFrameActivity implements OnClickListener, OnTouchListener, ResizeLayout.OnResizeListener {


    private RelativeLayout input_et_layout;

    //输入框
    private EditText input_et;


    private Button expresion_btn;

    private TextView send_btn;

    private WebView article_comment_webview;

    /**
     * 表情图片 布局
     */
    private LinearLayout more_face_layout;
    /**
     * 表情容器
     */
    private ViewPager more_face_view_pager;
    /**
     * 表情容器下面的布局
     */
    private LinearLayout more_face_type_layout;
    /**
     * 发送
     */
    private TextView more_face_send_tv;

    private boolean isopenFace = false;

    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;

    // 根布局
    private ResizeLayout root_layout;

    private InputHandler inputHandler = new InputHandler();

    private int curTat;// 当前的状态

    private int face_or_others;// 100时点击了表情按钮 200时点击了更多按钮 300时候listview


    private String countStr;

    private int count;

    private String weburl;

    private String name;

    private String type;

    private ArticleDataBean articledtaBean_talkinjected;
    private ArticleDataBean articledtaBean_commentOtherComment;
    private ArticleDataBean articledtaBean_reportComment;
    private ArticleDataBean articledtaBean_touchUserIcon;


    private Dialog loadingDiaog;


    private LinearLayout error_network_layout;

    private TextView error_network_tv;

    private LinearLayout more_face_indicator;

    private String fromPage = "";

    private String commcentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_find_articlecomment);
        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            countStr = bundle.getString("count");
            weburl = bundle.getString(YpSettings.BUNDLE_KEY_WEB_URL);
            fromPage = bundle.getString(YpSettings.FROM_PAGE);

            if (bundle.containsKey("commcentId")) {
                commcentId = bundle.getString("commcentId");
            }
        }

        if (!TextUtils.isEmpty(countStr)) {
            count = Integer.valueOf(countStr);
        }

        if (YpSettings.isTest) {
            weburl = HttpURL.Test_webURL + weburl;
        } else {
            weburl = HttpURL.webURL + weburl;
        }


        initView();
        initEmoView();

        initWebView();

    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("评论列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("评论列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    @SuppressWarnings("deprecation")
    private void initView() {


        root_layout = (ResizeLayout) findViewById(R.id.root_layout);

        input_et = (EditText) findViewById(R.id.input_et);

        expresion_btn = (Button) findViewById(R.id.expresion_btn);
        send_btn = (TextView) findViewById(R.id.send_btn);

        more_face_layout = (LinearLayout) findViewById(R.id.more_face_layout);
        more_face_view_pager = (ViewPager) findViewById(R.id.more_face_view_pager);
        more_face_indicator = (LinearLayout) this.findViewById(R.id.more_face_indicator);

        more_face_type_layout = (LinearLayout) findViewById(R.id.more_face_type_layout);
        more_face_send_tv = (TextView) findViewById(R.id.more_face_send_tv);

        input_et_layout = (RelativeLayout) findViewById(R.id.input_et_layout);

        article_comment_webview = (WebView) findViewById(R.id.article_comment_webview);


        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);

        error_network_tv = (TextView) findViewById(R.id.error_network_tv);


        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article_comment_webview.loadUrl(weburl);
                article_comment_webview.setVisibility(View.VISIBLE);
                input_et_layout.setVisibility(View.VISIBLE);
                error_network_layout.setVisibility(View.GONE);

            }
        });


        more_face_layout.setVisibility(View.GONE);
        more_face_layout.setTag(100);
        curTat = BIGGER;// 界面控制在了刚进入什么都是隐藏的状态，故这个值可以这么给初始化

        setTvTetle(count);


        getBtnOption().setVisibility(View.GONE);
        gettvOption().setVisibility(View.GONE);

        getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftInputViewLayout();
                finish();

            }
        });


        expresion_btn.setOnClickListener(this);

        send_btn.setOnClickListener(this);
        more_face_send_tv.setOnClickListener(this);


        input_et.setOnTouchListener(this);

        input_et_layout.setOnClickListener(this);
        root_layout.setOnResizeListener(this);

        loadingDiaog = DialogUtil.LoadingDialog(this, null);

    }

    /**
     * 设置标题
     *
     * @param count
     */
    private void setTvTetle(int count) {
        if (0 == count) {
            getTvTitle().setText("评论");
            return;
        }
        getTvTitle().setText("评论" + count);
    }


    private void initWebView() {


        article_comment_webview.getSettings().setJavaScriptEnabled(true);// 允许使用js
        article_comment_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        article_comment_webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


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
                LogUtils.e("ＡＡonReceivedError=");
                article_comment_webview.setVisibility(View.GONE);
                input_et_layout.setVisibility(View.GONE);
                error_network_layout.setVisibility(View.VISIBLE);

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

                if (valueUrl.startsWith("http://localhost/return")) {

                    int index = valueUrl.indexOf("=");
                    int urlSize = valueUrl.length();

                    String data = "";

                    if (-1 != index) {//载取data内容
                        data = valueUrl.substring(index + 1, urlSize);

                        try {
                            JSONObject jsonObject = new JSONObject(data);

                            type = jsonObject.getString("type");

                            if (TextUtils.equals(type, "talkinjected")) {//评论文章

                                articledtaBean_talkinjected = JsonUtils.fromJson(data, ArticleDataBean.class);

                                input_et.setText("");
                                if (fromPage.equals("FindMessageActivity")) {
                                    String userNmae = "输入回复内容";
                                    input_et.setHint(userNmae);
                                } else {
                                    String userNmae = "输入评论内容";
                                    input_et.setHint(userNmae);
                                }
                                input_et.setCursorVisible(true);

                            } else if (TextUtils.equals(type, "commentOtherComment")) {//回复评论
                                articledtaBean_commentOtherComment = JsonUtils.fromJson(data, ArticleDataBean.class);


                                String username = articledtaBean_commentOtherComment.getData().getUsername();

                                if (!TextUtils.isEmpty(username)) {
                                    input_et.setText("");
                                    String userNmae = "回复" + username;
                                    input_et.setHint(userNmae);
                                    input_et.setCursorVisible(true);
                                }


                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                                input_et.requestFocus();


                            } else if (TextUtils.equals(type, "reportComment")) {//举报
                                articledtaBean_reportComment = JsonUtils.fromJson(data, ArticleDataBean.class);
                                input_et.setText("");
                                String userNmae = "输入评论内容";
                                input_et.setHint(userNmae);
                                input_et.setCursorVisible(true);
                                webHttp("");

                            } else if (TextUtils.equals(type, "touchUserIcon")) {

                                articledtaBean_touchUserIcon = JsonUtils.fromJson(data, ArticleDataBean.class);

                                String userID = articledtaBean_touchUserIcon.getData().getUserid();

                                if (TextUtils.isEmpty(userID)) {
                                    return true;
                                }


                                Bundle bundle = new Bundle();
                                bundle.putInt(YpSettings.USERID, Integer.valueOf(userID));
                                ActivityUtil.jump(ArticleCommentListActivity.this,
                                        UserInfoActivity.class, bundle, 0, 200);

                                return true;

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    LogUtils.e("ＡＡshouldOverrideUrlLoading_data=" + data);


                    return true;
                }


                LogUtils.e("ＡＡshouldOverrideUrlLoading=" + valueUrl);
                return super.shouldOverrideUrlLoading(view, url);


            }

        });


        article_comment_webview.loadUrl(weburl);
    }

    /**
     * 设置初始化状态。比如，回复评论时，成功与否都要设置初化状态，评论文章
     */
    private void reductionType() {
        type = "talkinjected";

        input_et.setText("");
        String userNmae = "输入评论内容";
        input_et.setHint(userNmae);
    }

    /**
     * 评论文章
     *
     * @param id
     * @param context
     */
    private void talkinjectedHttp(String id, String context) {

        loadingDiaog.show();

        ArticleTalkinjectedBean articleBean = new ArticleTalkinjectedBean();
        articleBean.setId(id);
        articleBean.setContent(context);

        ArticleTalkinjectedService httpService = new ArticleTalkinjectedService(this);
        httpService.parameter(articleBean);
        httpService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                reductionType();

                loadingDiaog.dismiss();

                DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this, "评论成功");

                article_comment_webview.loadUrl("javascript:" + articledtaBean_talkinjected.getData().getInjected());//注入js
                count = count + 1;
                setTvTetle(count);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();


                String msg = respBean.getMsg();

                if (!TextUtils.isEmpty(msg)) {

                    DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this, msg);

                } else {
                    DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this);
                }
            }
        });
        httpService.enqueue();
    }

    /**
     * 回复评论
     *
     * @param commentId
     * @param context
     */
    private void commentOtherCommentHttp(final String commentId, final String context) {

        loadingDiaog.show();

        ArticleCommentOtherCommentBean commentOtherCommentBean = new ArticleCommentOtherCommentBean();
        commentOtherCommentBean.setCommentId(commentId);
        commentOtherCommentBean.setContent(context);
        ArticleCommentOtherCommentService commentOtherCommentService = new ArticleCommentOtherCommentService(this);
        commentOtherCommentService.parameter(commentOtherCommentBean);
        commentOtherCommentService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                reductionType();


                loadingDiaog.dismiss();
                DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this, "回复成功");


                UserDto userDto = localUserInfo(LoginUser.getInstance().getUserId());
                String userName = "";
                if (null != userDto) {
                    userName = userDto.getProfile().getName();
                }

                String js = articledtaBean_talkinjected.getData().getOtherCommentInjected() + "(" + "\"" + commentId + "\"" + "," + "\"" + LoginUser.getInstance().getUserId() + "\"" + "," + "\"" + userName + "\"" + "," + "\"" + context + "\"" + ");";


                article_comment_webview.loadUrl("javascript:" + js);//注入js

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                loadingDiaog.dismiss();

                String msg = respBean.getMsg();

                if (!TextUtils.isEmpty(msg)) {

                    DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this, msg);

                } else {
                    DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this);
                }
            }
        });
        commentOtherCommentService.enqueue();
    }

    /**
     * 举报
     *
     * @param commentId
     */
    private void reportCommentHttp(String commentId) {

        loadingDiaog.show();

        ArticleReportCommentBean reportCommentBean = new ArticleReportCommentBean();
        reportCommentBean.setCommentId(commentId);

        ArticleReportCommentService reportCommentService = new ArticleReportCommentService(this);

        reportCommentService.parameter(reportCommentBean);

        reportCommentService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);


                loadingDiaog.dismiss();

                reductionType();

                DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this, "举报成功");

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();


                String msg = respBean.getMsg();

                if (!TextUtils.isEmpty(msg)) {

                    DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this, msg);

                } else {
                    DialogUtil.showDisCoverNetToast(ArticleCommentListActivity.this);
                }
            }
        });
        reportCommentService.enqueue();
    }

    /**
     * 获取当前登录用户的信息。
     *
     * @param userid
     * @return
     */
    private UserDto localUserInfo(int userid) {
        UserInfo userInfo = DbHelperUtils.getUserInfo(userid);
        if (userInfo != null) {

            UserDto dto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
            return dto;
        }
        return null;
    }

    /**
     * 点发送时。要做提交的类型的判断
     *
     * @param context
     */


    private void webHttp(String context) {

        if (TextUtils.equals(type, "talkinjected") && null != articledtaBean_talkinjected) {

            if (fromPage.equals("FindMessageActivity")) {
                commentOtherCommentHttp(commcentId, context);
            } else {
                talkinjectedHttp(articledtaBean_talkinjected.getData().getId(), context);
            }

            return;
        }

        if (TextUtils.equals(type, "commentOtherComment") && null != articledtaBean_commentOtherComment) {

            commentOtherCommentHttp(articledtaBean_commentOtherComment.getData().getCommentid(), context);
            return;
        }

        if (TextUtils.equals(type, "reportComment") && null != articledtaBean_reportComment) {
            reportCommentHttp(articledtaBean_reportComment.getData().getCommentid());
            return;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expresion_btn:// 表情切换 but

                input_et.requestFocus();

                if (isopenFace) {
                    more_face_layout.setVisibility(View.GONE);
                    isopenFace = false;
                    changeInput();
                    expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                    return;
                }

                isopenFace = false;

                more_face_layout.setTag(1000);
                face_or_others = 100;

                if (curTat == SMALLER) {// 软键盘显示了
                    changeInput();
                } else {
                    setMoreLayoutVisible(true);
                }

                break;
            case R.id.send_btn:// 发送but

                ViewsUtils.preventViewMultipleClick(v, 1000);

                String cmt = input_et.getText().toString().trim();


                if (!TextUtils.isEmpty(cmt)) {
                    webHttp(cmt);
                    isopenFace = false;
                    hideSoftInputViewLayout();
                    more_face_layout.setTag(2000);
                    face_or_others = 200;
                    setMoreLayoutVisible(false);

                }
                break;

            case R.id.more_face_send_layout:// 发送but

                ViewsUtils.preventViewMultipleClick(v, 1000);
                String comment = input_et.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)) {
                    webHttp(comment);
                    hideSoftInputViewLayout();
                    more_face_layout.setTag(2000);
                    face_or_others = 200;
                    setMoreLayoutVisible(false);
                }

                break;

            default:
                break;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.input_et:
                more_face_layout.setVisibility(View.GONE);
                input_et.setCursorVisible(true);
                break;
            case R.id.input_et_layout:
                more_face_layout.setVisibility(View.GONE);
                input_et.setCursorVisible(true);
                break;

            case R.id.article_comment_webview:
                hideSoftInputViewLayout();
                break;

            default:
                break;
        }

        return false;
    }

    private void hideSoftInputViewLayout() {
        hideSoftInputView();
        more_face_layout.setVisibility(View.GONE);

    }

    @Override
    public void OnResize(int w, int h, int oldw, int oldh) {
        int change = BIGGER;
        if (h < oldh) {
            change = SMALLER;
        }
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = change;
        inputHandler.sendMessage(msg);

    }

    private class InputHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_RESIZE: {

                    if (msg.arg1 == BIGGER) {
                        curTat = BIGGER;
                        setMoreLayoutVisible(true);
                    } else {
                        curTat = SMALLER;
                        setMoreLayoutVisible(false);

                    }
                }
                break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 反复切换软键盘
     */
    private void changeInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        input_et.setCursorVisible(true);
    }

    List<String> emos;

    /**
     * 初始化表情布局
     *
     * @param
     * @return void
     * @throws
     * @Title: initEmoView
     */
    private void initEmoView() {

        emos = FaceTextUtils.emoList;

        List<View> views = new ArrayList<View>();
        for (int i = 0; i < 3; ++i) {
            views.add(getGridView(i));
        }
        initLayout();
        more_face_view_pager.setAdapter(new EmoViewPagerAdapter(views));
        more_face_view_pager.addOnPageChangeListener(new PageChageListener());
    }


    class PageChageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            // 更改指示器图片
            for (int i = 0; i < indicators.length; i++) {
                indicators[arg0].setBackgroundResource(R.drawable.near_detail_img_selected);
                if (arg0 != i) {
                    indicators[i].setBackgroundResource(R.drawable.near_detail_img_no_selected);
                }
            }

        }

    }

    private ImageView[] indicators = null;

    private LinearLayout.LayoutParams params;

    /**
     * 手机密度
     */
    private ImageView indicator_view;

    private void initLayout() {
        indicators = new ImageView[3]; // 定义指示器数组大小

        for (int i = 0; i < 3; i++) {

            int imagePadding = 10;
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(imagePadding, imagePadding, imagePadding, imagePadding);

            // 循环加入指示器
            indicator_view = new ImageView(this);
            indicator_view.setBackgroundResource(R.drawable.near_detail_img_no_selected);

            indicators[i] = indicator_view;
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.near_detail_img_selected);
            }
            more_face_indicator.addView(indicators[i], params);

        }
        more_face_indicator.setVisibility(View.VISIBLE);
    }


    private View getGridView(final int i) {
        View view = View.inflate(this, R.layout.include_emo_gridview, null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 0) {
            list.addAll(emos.subList(0, 18));
        } else if (i == 1) {
            list.addAll(emos.subList(18, 36));
        } else if (i == 2) {
            list.addAll(emos.subList(36, emos.size()));
        }
        final EmoteAdapter gridAdapter = new EmoteAdapter(ArticleCommentListActivity.this, list);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                if (i == 0 || i == 1) {
                    if (position == 17) {
                        faceDeletefun();
                    } else {
                        inputFaceFun(position + i * 18);
                    }
                } else {
                    if (position == 5) {
                        faceDeletefun();
                    } else {
                        inputFaceFun(position + 36);
                    }
                }

            }
        });
        return view;
    }

    private void faceDeletefun() {

        int start = input_et.getSelectionStart();
        if (start > 0) {

            boolean no_face = true;
            String emo_str_one = "";
            String emo_str_two = "";
            String emo_str_three = "";
            String input_str = input_et.getText().toString();
            if (input_str.length() >= 4) {
                emo_str_one = input_str.substring(start - 4, start);
            }
            if (input_str.length() >= 3) {
                emo_str_two = input_str.substring(start - 3, start);
            }
            if (input_str.length() >= 5) {
                emo_str_three = input_str.substring(start - 5, start);
            }

            String face_str = "";
            for (int i = 0; i < FaceTextUtils.expression_text.length - 1; i++) {
                face_str = "[" + FaceTextUtils.expression_text[i].toString() + "]";
                if (emo_str_one.equals(face_str)) {

                    input_et.getEditableText().delete(start - 4, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ArticleCommentListActivity.this, input_et.getText().toString());
                    input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    input_et.setSelection(start - 4);
                    no_face = false;
                    break;
                }

                if (emo_str_two.equals(face_str)) {

                    input_et.getEditableText().delete(start - 3, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ArticleCommentListActivity.this, input_et.getText().toString());
                    input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    input_et.setSelection(start - 3);
                    no_face = false;
                    break;
                }
                if (emo_str_three.equals(face_str)) {

                    input_et.getEditableText().delete(start - 5, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ArticleCommentListActivity.this, input_et.getText().toString());
                    input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    input_et.setSelection(start - 5);
                    no_face = false;
                    break;
                }
            }

            if (no_face) {
                input_et.getEditableText().delete(start - 1, start);
                SpannableString spannableString = FaceTextUtils.toSpannableString(ArticleCommentListActivity.this, input_et.getText().toString());
                input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                input_et.setSelection(start - 1);
            }

        }
    }

    private void inputFaceFun(int position) {
        String emo_Str = "[" + FaceTextUtils.expression_text[position] + "]";
        int start = input_et.getSelectionStart();
        int contentStrLength = input_et.getText().toString().length();
        int emo_StrLength = emo_Str.length();
        CharSequence content = null;
        if (contentStrLength + emo_StrLength >= 100) {
            return;
        } else {
            content = input_et.getText().insert(start, emo_Str);
        }
        SpannableString spannableString = FaceTextUtils.toSpannableString(
                ArticleCommentListActivity.this, content.toString());
        input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
        // chat_input_et.setText(content);
        // 定位光标位置
        CharSequence info = input_et.getText();
        int inputStrSize = input_et.getText().toString().length();
        if (info instanceof Spannable) {
            Spannable spanText = (Spannable) info;
            if (inputStrSize < 100) {
                Selection.setSelection(spanText, start + emo_Str.length());
            }

        }
    }

    private void setMoreLayoutVisible(boolean show) {
        if (null != more_face_layout) {
            int id = (Integer) more_face_layout.getTag();
            if (show && id == 1000) {
                if (face_or_others == 100) {
                    more_face_layout.setVisibility(View.VISIBLE);
                    expresion_btn.setBackgroundResource(R.drawable.chat_input_btn_bg);
                    isopenFace = true;
                    LogUtils.e("来了啊呀呀呀");

                } else {
                    more_face_layout.setVisibility(View.GONE);
                    expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                    isopenFace = false;
                    LogUtils.e("来了啊呀呀呀－＝＝＝＝＝＝");
                }

            } else {
                more_face_layout.setVisibility(View.GONE);
                expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                isopenFace = false;
                LogUtils.e("来了啊呀呀呀9000000000");
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != article_comment_webview) {
//            mWebView.stopLoading();此方法有问题。在android 4.4.4上lenovo上，连续onDestroy()时app会异常停止

            article_comment_webview.clearFocus();


            article_comment_webview.clearCache(true);
            article_comment_webview.clearHistory();
            article_comment_webview.destroy();


        }
    }


}
