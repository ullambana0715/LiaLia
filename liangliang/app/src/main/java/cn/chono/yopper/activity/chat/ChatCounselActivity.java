package cn.chono.yopper.activity.chat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.andbase.tractor.utils.LogUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.orhanobut.logger.Logger;
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMConversationType;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileEntity;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileRespEntity;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileService;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorsProfileBean;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderCompletion.OrderCompletionBean;
import cn.chono.yopper.Service.Http.OrderCompletion.OrderCompletionDto;
import cn.chono.yopper.Service.Http.OrderCompletion.OrderCompletionRespBean;
import cn.chono.yopper.Service.Http.OrderCompletion.OrderCompletionService;
import cn.chono.yopper.Service.Http.OrderLastest.OrderLastestBean;
import cn.chono.yopper.Service.Http.OrderLastest.OrderLastestEntity;
import cn.chono.yopper.Service.Http.OrderLastest.OrderLastestRespEntity;
import cn.chono.yopper.Service.Http.OrderLastest.OrderLastestService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.base.SelectEntryActivity;
import cn.chono.yopper.activity.order.UserOrderEvaluationActivity;
import cn.chono.yopper.activity.order.UserOrderFeedBackActivity;
import cn.chono.yopper.adapter.ChatCounselAdapter;
import cn.chono.yopper.adapter.EmoViewPagerAdapter;
import cn.chono.yopper.adapter.EmoteAdapter;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AttributeDto;
import cn.chono.yopper.data.AudioMsg;
import cn.chono.yopper.data.CounselOrderStatusTable;
import cn.chono.yopper.data.HintMsg;
import cn.chono.yopper.data.ImgMsg;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.OrderListEvent;
import cn.chono.yopper.event.SendMsgStatusEvent;
import cn.chono.yopper.im.imObserver.ChatObserver;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.im.model.CustomMessage;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.ImageMessage;
import cn.chono.yopper.im.model.TextMessage;
import cn.chono.yopper.im.model.VoiceMessage;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.FaceTextUtils;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.MediaUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.DragListView;
import cn.chono.yopper.view.MyDialog;
import cn.chono.yopper.view.RecordButton;
import cn.chono.yopper.view.ResizeLayout;
import me.nereo.multi_image_selector.MultiImageSelector;

@SuppressLint("HandlerLeak")
public class ChatCounselActivity extends MainFrameActivity implements DragListView.OnRefreshLoadingMoreListener, OnClickListener, ChatCounselAdapter.OnItemSendFailClickLitener {

    // 根布局
    private static ResizeLayout chat_root_layout;

    // 消息列表
    private DragListView messageListView;

    // 网络提示
    private LinearLayout net_hint_layout;

    private ImageView chat_more_btn;

    private RecordButton chat_sound_record_btn;

    // 输入框
    private EditText chat_input_et;

    // 表情按钮
    private ImageView chat_expresion_btn;

    // 语音
    private ImageView chat_sound_iv;

    // 发送按钮
    private Button chat_send_btn;

    // 更多内容布局
    private LinearLayout chat_more_layout;

    // 表情布局
    private LinearLayout chat_more_face_layout;

    // 表情页
    private ViewPager chat_more_face_view_pager;


    // 更多其他内容布局 如位置 图片等
    private LinearLayout chat_more_others_layout;

    // 照片按钮
    private LinearLayout chat_photo_layout;

    // 相机按钮
    private LinearLayout chat_camera_layout;

    private LinearLayout chat_goback_layout;

    private TextView chat_title_tv;

    private LinearLayout chat_counsel_layout;

    private TextView chat_counsel_agree_tv;

    private TextView chat_counsel_deny_tv;

    private TextView chat_counsel_content_tv;

    private TextView chat_counsel_end_tv;

    private LinearLayout chat_counsel_input_layout;

    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;

    private int curTat;// 当前的状态

    private int face_or_others;// 100时点击了表情按钮 200时点击了更多按钮 300时候listview

    private List<ChatDto> recoverlist;

    private ChatCounselAdapter messageAdapter;

    // 一页10条数据
    private int pagesize = 10;
    // 页数
    private int pagecount = 0;

    private int cur_pagecount = 1;
    // 页面数据余数
    private int remainder_count = 0;

    private List<ChatDto> chatdtolist;
    private Handler mhandler;

    // 自己的信息

    private boolean isopenFace = false;
    private boolean isopenmore = false;

    private boolean isopenSound = false;

    private NetState receiver;

    private LinearLayout chat_face_indicator;//图片指示器

    private boolean isHandleEnd = false;

    @Override
    public void onItemSendFailClick(ChatDto dto) {
        showSendFailDialog(dto);
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

    private InputHandler inputHandler = new InputHandler();

    private String mid;

    private int targetUserId;// 当前聊友的id

    private String jid = "";

    private String targetUser_nickName = "";

    private int userid;

    private String orderId = "";

    private int orderStatus = 1;

    private int mCounselorType = 0;

    private String hint = "咨询师请求结束";

    //0代表从消息列表过来 1代表从订单过来
    private int fromPage = 0;

    private ChatObserver mChatObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_counsel);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        RxBus.get().register(this);

        PushAgent.getInstance(this).onAppStart();
        userid = LoginUser.getInstance().getUserId();
        mid = userid + "";

        chatdtolist = new ArrayList<ChatDto>();

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(YpSettings.USERID)) {
                targetUserId = bundle.getInt(YpSettings.USERID);
            }

            if (bundle.containsKey(YpSettings.ORDER_ID)) {
                orderId = bundle.getString(YpSettings.ORDER_ID);
            }

            if (bundle.containsKey(YpSettings.COUNSEL_TYPE)) {
                mCounselorType = bundle.getInt(YpSettings.COUNSEL_TYPE);
            }

            if (bundle.containsKey(YpSettings.COUNSEL_STATUS)) {
                orderStatus = bundle.getInt(YpSettings.COUNSEL_STATUS);
            }

            if (bundle.containsKey(YpSettings.FROM_PAGE)) {
                fromPage = bundle.getInt(YpSettings.FROM_PAGE);
            }

        }


        jid = targetUserId + "";


        initComponent();


        CounselOrderStatusTable dto = DbHelperUtils.getCounselOrderInfo(mid, jid);

        if (dto != null) {

            hint = dto.getHint();

            if (fromPage == 0) {
                orderId = dto.getOrderId();
                orderStatus = dto.getCounselOrderStatus();
                mCounselorType = dto.getCounselorType();

            } else {
                DbHelperUtils.saveCounselOrderInfo(mid, jid, hint, orderId, orderStatus, mCounselorType);
            }


        } else {

            getOrderLastest(userid, targetUserId);
        }


        mhandler = new Handler();

        getCounselInfoWithID(targetUserId);

        setOrderStatusView(orderStatus, hint);

        ChatUtils.setCounselChatRecordReaded(mid, jid, "1");

        initChatData();

        mChatObserver = new ChatObserver(targetUserId + "", TIMConversationType.C2C);

        mChatObserver.readMessages();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("聊天"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

        MediaUtil.getInstance().stop();

    }

    @Override
    protected void onResume() {
        super.onResume();


        MobclickAgent.onPageStart("聊天"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            hideSoftInputView();

            if (fromPage == 1 && isHandleEnd) {

                //手动同意结束 如果是订单列表过来返回需要刷新订单列表
                RxBus.get().post("OrderListEvent", new OrderListEvent());
            }

            finish();
        }
        return true;
    }


    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏 标题栏设置为聊天对象的昵称
        chat_goback_layout = (LinearLayout) this.findViewById(R.id.chat_goback_layout);
        chat_title_tv = (TextView) this.findViewById(R.id.chat_title_tv);

        chat_goback_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                hideSoftInputView();

                if (fromPage == 1 && isHandleEnd) {

                    //手动同意结束 如果是订单列表过来返回需要刷新订单列表
                    RxBus.get().post("OrderListEvent", new OrderListEvent());
                }

                finish();
            }
        });

        // 内容部分

        chat_root_layout = (ResizeLayout) this.findViewById(R.id.chat_root_layout);


        net_hint_layout = (LinearLayout) this.findViewById(R.id.net_hint_layout);


        chat_sound_iv = (ImageView) this.findViewById(R.id.chat_sound_iv);


        chat_more_btn = (ImageView) this.findViewById(R.id.chat_more_btn);

        chat_input_et = (EditText) this.findViewById(R.id.chat_input_et);

        chat_expresion_btn = (ImageView) this.findViewById(R.id.chat_expresion_btn);

        chat_sound_record_btn = (RecordButton) this.findViewById(R.id.chat_sound_record_btn);

        chat_sound_record_btn.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {

            @Override
            public void onFinishedRecord(final String audioPath, final int time) {
                LogUtils.e("finished!!!!!!!!!! save to "
                        + audioPath + "     time ==" + time);
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (new File(chat_sound_record_btn.getSavePath()).length() == 0) {
                            LogUtils.e(" 录入文件大小为0，录入失败 ");
                            return;
                        }
                        saveAudio(chat_sound_record_btn.getSavePath(), time);
                    }
                }, 1000);
            }
        });

        chat_send_btn = (Button) this.findViewById(R.id.chat_send_btn);
        chat_send_btn.setOnClickListener(this);

        chat_more_layout = (LinearLayout) this.findViewById(R.id.chat_more_layout);

        chat_more_face_layout = (LinearLayout) this.findViewById(R.id.chat_more_face_layout);

        chat_more_face_view_pager = (ViewPager) this.findViewById(R.id.chat_more_face_view_pager);

        chat_face_indicator = (LinearLayout) this.findViewById(R.id.chat_more_face_indicator);


        chat_more_others_layout = (LinearLayout) this.findViewById(R.id.chat_more_others_layout);

        chat_photo_layout = (LinearLayout) this.findViewById(R.id.chat_photo_layout);
        chat_photo_layout.setOnClickListener(this);

        chat_camera_layout = (LinearLayout) this.findViewById(R.id.chat_camera_layout);
        chat_camera_layout.setOnClickListener(this);


        messageListView = (DragListView) this.findViewById(R.id.chat_msg_listView);

        chat_counsel_layout = (LinearLayout) this.findViewById(R.id.chat_counsel_layout);

        chat_counsel_agree_tv = (TextView) this.findViewById(R.id.chat_counsel_agree_tv);
        chat_counsel_agree_tv.setOnClickListener(this);

        chat_counsel_deny_tv = (TextView) this.findViewById(R.id.chat_counsel_deny_tv);
        chat_counsel_deny_tv.setOnClickListener(this);

        chat_counsel_content_tv = (TextView) this.findViewById(R.id.chat_counsel_content_tv);


        chat_counsel_end_tv = (TextView) this.findViewById(R.id.chat_counsel_end_tv);


        chat_counsel_input_layout = (LinearLayout) this.findViewById(R.id.chat_counsel_input_layout);


        receiver = new NetState();
        IntentFilter filter = new IntentFilter();

        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);
        receiver.onReceive(this, null);

        messageListView.setOnRefreshListener(this);
        messageListView.setPullLoadEnable(false);

        messageListView.setDividerHeight(0);

        chat_root_layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {
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
        });
        chat_more_layout.setTag(100);
        chat_more_layout.setVisibility(View.GONE);

        curTat = BIGGER;// 界面控制在了刚进入什么都是隐藏的状态，故这个值可以这么给初始化

        // 更多按钮
        chat_more_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);

                if (isopenmore) {
                    chat_more_layout.setVisibility(View.GONE);
                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);

                    isopenmore = false;
                    return;
                }

                chat_more_layout.setTag(1000);

                face_or_others = 200;

                if (curTat == SMALLER) {// 软键盘显示了
                    changeInput();
                } else {
                    setMoreLayoutVisible(true);
                }

            }
        });

        chat_sound_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 500);

                chat_input_et.requestFocus();
                chat_input_et.setCursorVisible(true);

                if (isopenSound) {
                    chat_more_layout.setVisibility(View.GONE);
                    isopenSound = false;
                    changeInput();
                    chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);
                    return;
                }


                isopenFace = false;
                isopenmore = true;
                isopenSound = false;

                chat_more_layout.setTag(1000);
                face_or_others = 300;
                // 弹出照片 拍照 位置 语音 赠送P果等功能面板
                if (curTat == SMALLER) {// 软键盘显示了
                    changeInput();
                } else {
                    setMoreLayoutVisible(true);
                }

            }
        });

        // 表情按钮
        chat_expresion_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);

                chat_input_et.requestFocus();
                chat_input_et.setCursorVisible(true);

                if (isopenFace) {
                    chat_more_layout.setVisibility(View.GONE);
                    isopenFace = false;
                    changeInput();
                    chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                    return;
                }

                isopenSound = false;
                isopenFace = false;
                isopenmore = true;

                chat_more_layout.setTag(1000);
                face_or_others = 100;
                // 弹出照片 拍照 位置 语音 赠送P果等功能面板
                if (curTat == SMALLER) {// 软键盘显示了
                    changeInput();
                } else {
                    setMoreLayoutVisible(true);
                }

            }
        });

        // 文本输入框监控 点击隐藏约会信息 有文字输入则显示发送按钮 隐藏约会按钮
        chat_input_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!CheckUtil.isEmpty(arg0.toString().trim())) {
                    // 有文字输入则显示发送按钮 隐藏约会按钮
                    chat_more_btn.setVisibility(View.GONE);
                    chat_send_btn.setVisibility(View.VISIBLE);
                } else {
                    chat_send_btn.setVisibility(View.GONE);
                    chat_more_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        chat_input_et.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                chat_more_layout.setVisibility(View.GONE);
                chat_input_et.requestFocus();
                chat_input_et.setCursorVisible(true);
                isopenSound = false;
                isopenFace = false;
                isopenmore = false;
                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);
                return false;
            }
        });


        messageListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                face_or_others = 400;
                //
                hideSoftInputView();

                chat_more_layout.setVisibility(View.GONE);

                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);

                return false;
            }
        });


        initEmoView();

    }

    private List<String> emos;

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
        chat_more_face_view_pager.setAdapter(new EmoViewPagerAdapter(views));
        chat_more_face_view_pager.addOnPageChangeListener(new PageChageListener());
    }

    class PageChageListener implements OnPageChangeListener {

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
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(imagePadding, imagePadding, imagePadding, imagePadding);

            // 循环加入指示器
            indicator_view = new ImageView(this);
            indicator_view.setBackgroundResource(R.drawable.near_detail_img_no_selected);

            indicators[i] = indicator_view;
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.near_detail_img_selected);
            }
            chat_face_indicator.addView(indicators[i], params);

        }
        chat_face_indicator.setVisibility(View.VISIBLE);
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

        final EmoteAdapter gridAdapter = new EmoteAdapter(ChatCounselActivity.this, list);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

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

    private void inputFaceFun(int position) {
        String emo_Str = "[" + FaceTextUtils.expression_text[position] + "]";

        int start = chat_input_et.getSelectionStart();
        CharSequence content = chat_input_et.getText().insert(start, emo_Str);
        SpannableString spannableString = FaceTextUtils.toSpannableString(ChatCounselActivity.this, content.toString());
        chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
        // chat_input_et.setText(content);
        // 定位光标位置
        CharSequence info = chat_input_et.getText();
        if (info instanceof Spannable) {
            Spannable spanText = (Spannable) info;
            Selection.setSelection(spanText, start + emo_Str.length());
        }
    }

    private void faceDeletefun() {

        int start = chat_input_et.getSelectionStart();
        if (start > 0) {

            boolean no_face = true;
            String emo_str_one = "";
            String emo_str_two = "";
            String emo_str_three = "";
            String input_str = chat_input_et.getText().toString();
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
                face_str = "[" + FaceTextUtils.expression_text[i].toString()
                        + "]";
                if (emo_str_one.equals(face_str)) {

                    chat_input_et.getEditableText().delete(start - 4, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ChatCounselActivity.this, chat_input_et.getText().toString());
                    chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    chat_input_et.setSelection(start - 4);
                    no_face = false;
                    break;
                }

                if (emo_str_two.equals(face_str)) {

                    chat_input_et.getEditableText().delete(start - 3, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ChatCounselActivity.this, chat_input_et.getText().toString());
                    chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    chat_input_et.setSelection(start - 3);
                    no_face = false;
                    break;
                }
                if (emo_str_three.equals(face_str)) {

                    chat_input_et.getEditableText().delete(start - 5, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ChatCounselActivity.this, chat_input_et.getText().toString());
                    chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    chat_input_et.setSelection(start - 5);
                    no_face = false;
                    break;
                }
            }

            if (no_face) {
                chat_input_et.getEditableText().delete(start - 1, start);
                SpannableString spannableString = FaceTextUtils.toSpannableString(ChatCounselActivity.this, chat_input_et.getText().toString());
                chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                chat_input_et.setSelection(start - 1);
            }

        }
    }

    private void setMoreLayoutVisible(boolean show) {
        if (null != chat_more_layout) {
            int id = (Integer) chat_more_layout.getTag();
            if (show && id == 1000) {
                if (face_or_others == 200) {
                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_add_off);
                    chat_more_layout.setVisibility(View.VISIBLE);
                    chat_more_face_layout.setVisibility(View.GONE);
                    chat_more_others_layout.setVisibility(View.VISIBLE);
                    chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);

                    chat_input_et.setVisibility(View.VISIBLE);
                    chat_sound_record_btn.setVisibility(View.GONE);

                    chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

                    isopenFace = false;
                    isopenmore = true;
                    isopenSound = false;

                } else if (face_or_others == 100) {
                    chat_more_layout.setVisibility(View.VISIBLE);
                    chat_more_face_layout.setVisibility(View.VISIBLE);
                    chat_more_others_layout.setVisibility(View.GONE);
                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                    chat_input_et.setVisibility(View.VISIBLE);
                    chat_sound_record_btn.setVisibility(View.GONE);
                    chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

                    isopenFace = true;
                    isopenmore = false;
                    isopenSound = false;

                } else if (face_or_others == 300) {
                    chat_more_layout.setVisibility(View.GONE);

                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);

                    chat_input_et.setVisibility(View.GONE);
                    chat_sound_record_btn.setVisibility(View.VISIBLE);
                    chat_sound_iv.setBackgroundResource(R.drawable.chat_input_btn_bg);

                    isopenSound = true;
                    isopenFace = false;
                    isopenmore = false;

                } else {
                    chat_more_layout.setVisibility(View.GONE);
                    chat_more_face_layout.setVisibility(View.GONE);
                    chat_more_others_layout.setVisibility(View.GONE);
                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                    chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);

                    chat_input_et.setVisibility(View.VISIBLE);
                    chat_sound_record_btn.setVisibility(View.GONE);
                    chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

                    isopenFace = false;
                    isopenmore = false;
                    isopenSound = false;
                }

            } else {
                chat_more_layout.setVisibility(View.GONE);

                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);

                chat_input_et.setVisibility(View.VISIBLE);
                chat_sound_record_btn.setVisibility(View.GONE);
                chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

                isopenFace = false;
                isopenmore = false;
                isopenSound = false;
            }
        }
    }

    /**
     * 反复切换软键盘
     */
    private void changeInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }


    @Override
    public void onRefresh() {
        // 下拉加载更多
        mhandler.postDelayed(new Runnable() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {

                cur_pagecount = cur_pagecount + 1;
                if (cur_pagecount - pagecount == 0) {
                    messageListView.setPullRefreshEnable(false);
                    // messageListView.removeHeadView(ChatActivity.this);
                    List<ChatDto> pageList = recoverlist.subList(0, remainder_count);
                    List<ChatDto> allList = new ArrayList<ChatDto>();
                    for (int i = 0; i < pageList.size(); i++) {
                        allList.add(pageList.get(i));
                    }
                    for (int i = 0; i < chatdtolist.size(); i++) {
                        allList.add(chatdtolist.get(i));
                    }
                    chatdtolist = allList;
                    messageAdapter.setList(chatdtolist);
                    messageAdapter.notifyDataSetChanged();
                } else if (cur_pagecount < pagecount) {
                    int start = pagecount * pagesize - (cur_pagecount + 1) * pagesize + remainder_count;
                    int end = (pagecount - cur_pagecount) * pagesize + remainder_count;
                    List<ChatDto> pageList = recoverlist.subList(start, end);
                    List<ChatDto> allList = new ArrayList<ChatDto>();
                    // 允许下拉
                    messageListView.setPullRefreshEnable(true);

                    for (int i = 0; i < pageList.size(); i++) {
                        allList.add(pageList.get(i));
                    }
                    for (int i = 0; i < chatdtolist.size(); i++) {
                        allList.add(chatdtolist.get(i));
                    }
                    chatdtolist = allList;
                    messageAdapter.setList(chatdtolist);
                    messageAdapter.notifyDataSetChanged();
                    messageListView.setSelectionFromTop(12, 20);

                } else {
                    messageListView.setPullRefreshEnable(false);
                }

                messageListView.onRefreshComplete();
            }

        }, 2000);

    }

    // 发送消息
    private void sendMessageIfNotNull(String str, int contenttype) {

        switch (contenttype) {
            // 发送文本消息
            case 1:
                if (!TextUtils.isEmpty(str)) {

                    TextMsg textmsg = new TextMsg(MessageType.Text, str, 1, "", 0);

                    long date = System.currentTimeMillis();
                    String msg_Str = JsonUtils.toJson(textmsg);

                    AttributeDto attributeDto = new AttributeDto();
                    attributeDto.setMask(0);
                    attributeDto.setDateid("");
                    attributeDto.setCounsel(1);
                    attributeDto.setType(MessageType.Text);

                    String attributeDto_str = JsonUtils.toJson(attributeDto);

                    ImMessage message = new TextMessage(str, attributeDto_str);

                    String TIMMessageStr = JsonUtils.toJson(message.getMessage());

                    String msgid = message.getMessage().getMsgId();

                    Logger.e("msgid="+msgid);

                    ChatDto chatDto = new ChatDto(mid, jid, msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, "", 1, TIMMessageStr);
                    setChatdtolist(chatDto);
                    ChatUtils.SaveOrUpdateChatMsgToDB(jid, msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, "", 1, TIMMessageStr);
                    ChatUtils.saveMessageRecord(msg_Str, jid, ChatDto.sending, ChatDto.s_type, date, 1, "", TIMMessageStr);

                    // 发送消息
                    mChatObserver.sendMessage(message.getMessage(),null);

                }

                break;

        }

    }


    private String msgId;

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("onChatReceiveMsg")

            }
    )
    public void onChatReceiveMsg(CommonEvent event) {
        //接收到消息
        ChatDto dto = (ChatDto) event.getEvent();

        if (dto == null) {
            return;
        }

        if (TextUtils.equals(msgId, dto.getMsgId())) {
            return;
        }

        msgId = dto.getMsgId();

        if (!dto.getJid().equals(jid)) {
            return;
        }

        if (dto.getJid().equals(jid)) {
            try {
                dto.setStatus(ChatDto.readed_status);
                App.getInstance().db.update(dto);

            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        if (chatdtolist != null && chatdtolist.size() > 0) {
            chatdtolist.add(dto);
            messageAdapter.setList(chatdtolist);
        } else {
            chatdtolist = new ArrayList<ChatDto>();
            chatdtolist.add(dto);
            messageAdapter = new ChatCounselAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
            messageListView.setAdapter(messageAdapter);
            messageListView.setSelection(messageAdapter.getCount() - 1);
            messageAdapter.setList(chatdtolist);
            messageAdapter.setOnItemSendFailClickLitener(this);
        }


        //
        String msg = dto.getMessage();
        String msg_type = ChatUtils.getMsgType(msg);

        if (TextUtils.equals(msg_type, MessageType.Hint)) {

            HintMsg hintdto = JsonUtils.fromJson(msg, HintMsg.class);

            if (hintdto != null) {
                int action = hintdto.getAction();
                orderId = hintdto.getExtra().get("orderId").toString();
                switch (action) {
                    case 20://咨询订单付款成功
                        orderStatus = 1;

                        setOrderStatusView(orderStatus, hint);

                        break;

                    case 21://咨询开始前15分钟
                        orderStatus = 1;

                        setOrderStatusView(orderStatus, hint);
                        break;

                    case 22://咨询开始
                        orderStatus = 2;

                        setOrderStatusView(orderStatus, hint);
                        break;

                    case 23://占卜师端请求结束
                        orderStatus = 3;
                        hint = hintdto.getText();
                        setOrderStatusView(orderStatus, hint);
                        break;

                    case 24://客户确认结束
                        orderStatus = 4;
                        isHandleEnd = true;
                        setOrderStatusView(orderStatus, hint);
                        break;

                    case 25://客户确认结束超时
                        orderStatus = 4;
                        isHandleEnd = true;
                        setOrderStatusView(orderStatus, hint);
                        break;

                }


            }
        }

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("onSendMessageStatus")

            }
    )
    public void onSendMessageStatus(SendMsgStatusEvent event) {
        //消息发送失败
        String msgId = event.getMsgId();
        int sendStatus = event.getSendStatus();
        TIMSoundElem soundElem = event.getSoundElem();
        changeSendStatus(sendStatus, msgId, soundElem);
    }


    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case YpSettings.PHOTO_SELECT:
                if (resultCode == -1) {

                    List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                    for (String p : mSelectPath) {

                        setUriBitmap(p);
                    }

                }

                break;

            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * @throws
     * @Title: setChatdtolist
     * @Description: 将消息显示listview
     * @return: void
     */

    private void setChatdtolist(ChatDto chatDto) {

        if (chatdtolist != null && chatdtolist.size() > 0) {
            chatdtolist.add(chatDto);
        } else {
            chatdtolist = new ArrayList<ChatDto>();
            chatdtolist.add(chatDto);
        }

        if (messageAdapter != null) {
            messageAdapter.setList(chatdtolist);
            messageListView.setAdapter(messageAdapter);
            messageAdapter.notifyDataSetChanged();
            if (chatdtolist.size() > 1) {
                messageListView.setSelection(messageListView.FOCUS_DOWN);
            }

        } else {
            messageAdapter = new ChatCounselAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
            messageListView.setAdapter(messageAdapter);
            messageAdapter.notifyDataSetChanged();
            messageAdapter.setOnItemSendFailClickLitener(this);
        }

    }


    private void setUriBitmap(String file_Path) {

        if (CheckUtil.isEmpty(file_Path)) {
            DialogUtil.showDisCoverNetToast(ChatCounselActivity.this, "选取失败，请重新选择！");
            return;
        }
        Bitmap bm = ImgUtils.resizesBitmap(file_Path);

        if (null != bm) {
            // 保存在自己定义文件的路径
            String filePath = ImgUtils.saveImgFile(ChatCounselActivity.this, bm);
            if (CheckUtil.isEmpty(filePath)) {
                DialogUtil.showDisCoverNetToast(ChatCounselActivity.this, "选取失败，请重新选择！");
                return;
            }

            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {
                if (file.length() > 1024 * 1024 * 10) {
                    DialogUtil.showDisCoverNetToast(ChatCounselActivity.this, "图片过大，发送失败！");
                } else {

                    long date = System.currentTimeMillis();

                    ImgMsg msg = new ImgMsg();
                    msg.setType(MessageType.Img);
                    msg.setW(bm.getWidth());
                    msg.setH(bm.getHeight());

                    msg.setMask(0);
                    msg.setDateid("");
                    msg.setCounsel(1);

                    TIMImageElem elem = new TIMImageElem();
                    elem.setPath(filePath);
                    elem.setLevel(0);
                    msg.setElem(elem);

                    String msg_Str = JsonUtils.toJson(msg);


                    AttributeDto attributeDto = new AttributeDto();
                    attributeDto.setMask(0);
                    attributeDto.setDateid("");
                    attributeDto.setCounsel(1);
                    attributeDto.setType(MessageType.Img);

                    String attributeDto_str = JsonUtils.toJson(attributeDto);

                    ImMessage message = new ImageMessage(filePath, true, attributeDto_str);

                    String TIMMessageStr = JsonUtils.toJson(message.getMessage());

                    String msgid = message.getMessage().getMsgId();


                    ChatDto chatDto = new ChatDto(mid, jid, msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, "", 1, TIMMessageStr);
                    // 加入消息列表
                    setChatdtolist(chatDto);

                    ChatUtils.SaveOrUpdateChatMsgToDB(jid, msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, "", 1, TIMMessageStr);
                    ChatUtils.saveMessageRecord(msg_Str, jid, ChatDto.sending, ChatDto.s_type, date, 1, "", TIMMessageStr);

                    mChatObserver.sendMessage(message.getMessage(),null);
                }

            } else {
                DialogUtil.showDisCoverNetToast(ChatCounselActivity.this, "图片不存在！");
            }
        } else {
            DialogUtil.showDisCoverNetToast(ChatCounselActivity.this, "选取失败，请重新选择！");
        }

    }


    /**
     * 改变发送消息的发送状态
     *
     * @param send_status
     */

    private void changeSendStatus(int send_status, String msgId, TIMSoundElem soundElem) {

        int size = chatdtolist.size();
        for (int i = 0; i < size; i++) {
            ChatDto dto = chatdtolist.get(i);
            if (TextUtils.equals(dto.getMsgId(), msgId)) {
                dto.setSend_status(send_status);
                if (soundElem != null) {

                    String msgType = ChatUtils.getMsgType(dto.getMessage());

                    if (TextUtils.equals(msgType, MessageType.Audio)) {

                        AudioMsg audioMsg = JsonUtils.fromJson(dto.getMessage(), AudioMsg.class);
                        audioMsg.setElem(soundElem);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                chat_root_layout.postInvalidate();
                ChatUtils.changeSendStatus(send_status, msgId, soundElem);
                break;
            }
        }
    }

    /**
     * 网络状态广播接收者
     *
     * @author SQ
     */
    class NetState extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent arg1) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (gprs != null && wifi != null) {

                if (!gprs.isConnected() && !wifi.isConnected()) {
                    // 网络连接断开，请检查网络
                    net_hint_layout.setVisibility(View.VISIBLE);
                    chat_title_tv.setText("未连接");
                } else {
                    net_hint_layout.setVisibility(View.GONE);

                    chat_title_tv.setText(targetUser_nickName);


                }
            }
        }
    }

    /**
     * 获取聊友的信息 系统账号则不用获取
     *
     * @param id
     */
    private void getCounselInfoWithID(final int id) {

        CounselorProfileEntity dto = DbHelperUtils.getCounselInfo(id);
        if (dto != null) {
            targetUser_nickName = dto.getNickName();
            chat_title_tv.setText(targetUser_nickName);
        }
        getCounselInfo();
    }


    /**
     * 连网获取塔罗师信息
     */
    private void getCounselInfo() {


        CounselorsProfileBean counselorsProfileBean = new CounselorsProfileBean();

        counselorsProfileBean.userId = targetUserId;

        counselorsProfileBean.counselorType = mCounselorType;

        CounselorProfileService counselorProfileService = new CounselorProfileService(this);

        counselorProfileService.parameter(counselorsProfileBean);

        counselorProfileService.callBack(new OnCallBackSuccessListener() {
                                             @Override
                                             public void onSuccess(RespBean respBean) {
                                                 super.onSuccess(respBean);


                                                 CounselorProfileRespEntity counselorProfileRespEntity = (CounselorProfileRespEntity) respBean;

                                                 CounselorProfileEntity counselorProfileEntity = counselorProfileRespEntity.resp;

                                                 if (counselorProfileEntity != null) {
                                                     targetUser_nickName = counselorProfileEntity.getNickName();
                                                     chat_title_tv.setText(targetUser_nickName);
                                                     String info = JsonUtils.toJson(counselorProfileEntity);
                                                     DbHelperUtils.saveCounselInfo(targetUserId, info);
                                                 }

                                             }


                                         }, new OnCallBackFailListener() {
                                             @Override
                                             public void onFail(RespBean respBean) {
                                                 super.onFail(respBean);

                                             }
                                         }

        );

        counselorProfileService.enqueue();
    }

    @Override
    protected void onDestroy() {
        if (mhandler != null) {
            mhandler.removeCallbacksAndMessages(null);
        }
        if (inputHandler != null) {
            inputHandler.removeCallbacksAndMessages(null);
        }
        unregisterReceiver(receiver);

        ChatUtils.setCounselChatRecordReaded(mid, jid, "1");

        RxBus.get().post("refreshCounselMessageList", new CommonEvent<>());

        RxBus.get().unregister(this);

        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_btn:// 发送
                String str = chat_input_et.getText().toString();
                chat_input_et.setText("");
                sendMessageIfNotNull(str, 1);

                break;
            case R.id.chat_photo_layout:// 图片发送


                chat_more_layout.setVisibility(View.GONE);
                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                isopenmore = false;

                MultiImageSelector.create()
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                        .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                        .start(ChatCounselActivity.this, YpSettings.PHOTO_SELECT);


                break;
            case R.id.chat_camera_layout:// 拍照

                MultiImageSelector.create()
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                        .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                        .start(ChatCounselActivity.this, YpSettings.PHOTO_SELECT);

                chat_more_layout.setVisibility(View.GONE);
                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                isopenmore = false;


                break;


            case R.id.chat_counsel_agree_tv:// 同意
                completionOrder();
                break;

            case R.id.chat_counsel_deny_tv:// 我不同意
                counselEndDiaog = DialogUtil.createHintOperateDialog(ChatCounselActivity.this, "", "是否去投诉", "继续对话", "去投诉", counselEndNotAgreeCallListener);
                if (!isFinishing()) {
                    counselEndDiaog.show();
                }
                break;

            default:
                break;
        }

    }


    private BackCallListener counselEndNotAgreeCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                counselEndDiaog.dismiss();
            }
            //去投诉反馈

            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.ORDER_ID, orderId);

            ActivityUtil.jump(ChatCounselActivity.this, UserOrderFeedBackActivity.class, bundle, 0, 100);

        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                counselEndDiaog.dismiss();
            }

        }
    };


    private BackCallListener counselEvalutionCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                counselEvalutionDialog.dismiss();
            }
            //去评价

            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.ORDER_ID, orderId);
            ActivityUtil.jump(ChatCounselActivity.this, UserOrderEvaluationActivity.class, bundle, 0, 100);

        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                counselEvalutionDialog.dismiss();
            }

        }
    };


    private Dialog loadingDiaog, counselEndDiaog, counselEvalutionDialog;

    /**
     * 结束咨询：同意
     */
    private void completionOrder() {

        loadingDiaog = DialogUtil.LoadingDialog(ChatCounselActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        OrderCompletionBean bean = new OrderCompletionBean();
        bean.setId(orderId);


        OrderCompletionService orderCompletionService = new OrderCompletionService(this);
        orderCompletionService.parameter(bean);
        orderCompletionService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                OrderCompletionRespBean orderCompletionRespBean = (OrderCompletionRespBean) respBean;

                OrderCompletionDto dto = orderCompletionRespBean.getResp();

                if (dto.isResp()) {

                    //更新数据库  将与该咨询师本次订单的状态改为结束

                    setOrderStatusView(CounselOrderStatusTable.counselEnd, hint);
                    DbHelperUtils.saveCounselOrderInfo(mid, jid, "", orderId, CounselOrderStatusTable.counselEnd, mCounselorType);

                    HintMsg hintMsg = new HintMsg();
                    hintMsg.setAction(25);
                    hintMsg.setText("咨询已经结束");
                    hintMsg.setType(MessageType.Hint);

                    String msg_Str = JsonUtils.toJson(hintMsg);

                    long date = System.currentTimeMillis();

                    ImMessage message = new CustomMessage(msg_Str, "收到一条咨询消息");
                    String TIMMessageStr = JsonUtils.toJson(message.getMessage());

                    String msgid = message.getMessage().getMsgId();


                    ChatDto chatDto = new ChatDto(mid, jid, msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.succeed, "", 1, TIMMessageStr);
                    setChatdtolist(chatDto);
                    ChatUtils.SaveOrUpdateChatMsgToDB(jid, msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.succeed, "", 1, TIMMessageStr);
                    ChatUtils.saveMessageRecord(msg_Str, jid, ChatDto.sending, ChatDto.s_type, date, 1, "", TIMMessageStr);

                    mChatObserver.sendMessage(message.getMessage(),null);

                    isHandleEnd = true;

                    counselEvalutionDialog = DialogUtil.createHintOperateDialog(ChatCounselActivity.this, "", "是否现在评价", "暂不", "去评价", counselEvalutionCallListener);
                    if (!isFinishing()) {
                        counselEvalutionDialog.show();
                    }

                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                isHandleEnd = false;

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ChatCounselActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChatCounselActivity.this, msg);
            }
        });

        orderCompletionService.enqueue();

    }

    /**
     * 根据用户Id和咨询师Id获取最近一条有效订单
     *
     * @param bookingUserId 预约用户Id
     * @param receiveUserId 接单用户Id
     */
    private void getOrderLastest(int bookingUserId, int receiveUserId) {

        OrderLastestService orderLastestService = new OrderLastestService(this);

        OrderLastestBean orderLastestBean = new OrderLastestBean();

        orderLastestBean.bookingUserId = bookingUserId;

        orderLastestBean.receiveUserId = receiveUserId;

        orderLastestService.parameter(orderLastestBean);

        orderLastestService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                OrderLastestRespEntity orderLastestRespEntity = (OrderLastestRespEntity) respBean;

                OrderLastestEntity orderLastestEntity = orderLastestRespEntity.resp;

                if (orderLastestEntity == null) {
                    return;
                }

                mCounselorType = orderLastestEntity.getCounselType();


                orderId = orderLastestEntity.getOrderId();


                if (orderLastestEntity.getOrderStatus() == 5) {
                    orderStatus = CounselOrderStatusTable.request_end;
                    hint = "咨询师请求结束";
                } else if (orderLastestEntity.getOrderStatus() == 3 || orderLastestEntity.getOrderStatus() == 4) {
                    orderStatus = CounselOrderStatusTable.counselEnd;
                }

                setOrderStatusView(orderStatus, hint);

                DbHelperUtils.saveCounselOrderInfo(mid, jid, "", orderId, orderStatus, mCounselorType);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
            }
        });

        orderLastestService.enqueue();

    }


    private void setOrderStatusView(int orderStatus, String content) {

        switch (orderStatus) {
            case CounselOrderStatusTable.no_start:

                chat_counsel_layout.setVisibility(View.GONE);


                chat_counsel_end_tv.setVisibility(View.GONE);

                chat_counsel_input_layout.setVisibility(View.VISIBLE);

                break;

            case CounselOrderStatusTable.counseling:

                chat_counsel_layout.setVisibility(View.GONE);


                chat_counsel_end_tv.setVisibility(View.GONE);

                chat_counsel_input_layout.setVisibility(View.VISIBLE);

                break;

            case CounselOrderStatusTable.request_end:

                chat_counsel_layout.setVisibility(View.VISIBLE);

                chat_counsel_end_tv.setVisibility(View.GONE);

                chat_counsel_content_tv.setText(content);

                chat_counsel_input_layout.setVisibility(View.VISIBLE);

                break;


            case CounselOrderStatusTable.counselEnd:

                chat_counsel_layout.setVisibility(View.GONE);


                chat_counsel_end_tv.setVisibility(View.VISIBLE);

                chat_counsel_input_layout.setVisibility(View.GONE);

                break;

        }
    }


    private void initChatData() {

        try {
            cur_pagecount = 1;
            recoverlist = new ArrayList<ChatDto>();
            recoverlist = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).and("counsel", " =", "1").orderBy("date"));
            if (recoverlist != null && recoverlist.size() > 0) {

                // private int pagesize=10;
                // //页数
                // private static int pagecount=0;
                // //页面数据余数
                // private int remainder_count=0;

                remainder_count = recoverlist.size() % pagesize;
                if (remainder_count > 0) {
                    pagecount = recoverlist.size() / pagesize + 1;
                } else {
                    pagecount = recoverlist.size() / pagesize;
                }

                if (pagecount > 1) {
                    chatdtolist = recoverlist.subList(pagecount * pagesize - (cur_pagecount + 1) * pagesize + remainder_count, recoverlist.size());
                    // 允许下拉
                    messageAdapter = new ChatCounselAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
                    messageListView.setAdapter(messageAdapter);
                    messageListView.setSelection(chatdtolist.size() - 1);
                    messageAdapter.setOnItemSendFailClickLitener(this);

                } else {
                    chatdtolist = recoverlist;
                    // bu允许下拉
                    // messageListView.R

                    messageAdapter = new ChatCounselAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
                    messageAdapter.setOnItemSendFailClickLitener(this);
                    messageListView.setAdapter(messageAdapter);
                    messageListView.setSelection(chatdtolist.size() - 1);
                }

            } else {

                messageAdapter = new ChatCounselAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
                messageListView.setAdapter(messageAdapter);
                messageAdapter.setOnItemSendFailClickLitener(this);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private void saveAudio(String filepath, int time) {


        long date = System.currentTimeMillis();
        AudioMsg msg = new AudioMsg();
        msg.setDuration(time);
        msg.setType(MessageType.Audio);
        msg.setCounsel(1);
        msg.setMask(0);
        msg.setDateid("");


        TIMSoundElem elem = new TIMSoundElem();
        elem.setPath(filepath);
        elem.setDuration(time);//填写语音时长

        msg.setElem(elem);

        String msg_str = JsonUtils.toJson(msg);


        AttributeDto attributeDto = new AttributeDto();
        attributeDto.setMask(0);
        attributeDto.setDateid("");
        attributeDto.setCounsel(1);
        attributeDto.setType(MessageType.Audio);


        String attributeDto_str = JsonUtils.toJson(attributeDto);
        ImMessage message = new VoiceMessage(elem, attributeDto_str);

        String msgid = message.getMessage().getMsgId();

        String TIMMessageStr = JsonUtils.toJson(message.getMessage());


        ChatDto chatDto = new ChatDto(mid, jid, msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, "", 1, TIMMessageStr);

        //加入消息列表
        setChatdtolist(chatDto);

        ChatUtils.SaveOrUpdateChatMsgToDB(jid, msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, "", 1, TIMMessageStr);

        ChatUtils.saveMessageRecord(msg_str, jid, ChatDto.sending, ChatDto.s_type, date, 1, "", TIMMessageStr);

        mChatObserver.sendMessage(message.getMessage(),null);
    }


    /**
     * 清理缓存提示框 可自行设置标题 提示内容 以及按钮文本
     */

    private Dialog sendFailDialog;

    public void showSendFailDialog(final ChatDto dto) {

        // 初始化一个自定义的Dialog
        sendFailDialog = new MyDialog(ChatCounselActivity.this, R.style.MyDialog, R.layout.my_hint_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView my_dialog_hint_title = (TextView) contentView.findViewById(R.id.my_dialog_hint_title);
                TextView my_dialog_hint_content = (TextView) contentView.findViewById(R.id.my_dialog_hint_content);
                TextView my_dialog_hint_ensure = (TextView) contentView.findViewById(R.id.my_dialog_hint_ensure);
                TextView my_dialog_hint_cancel = (TextView) contentView.findViewById(R.id.my_dialog_hint_cancel);

                my_dialog_hint_title.setText("提示");
                my_dialog_hint_title.setVisibility(View.GONE);
                my_dialog_hint_content.setText("是否要重新发送此条消息？");
                my_dialog_hint_ensure.setText("重发");
                my_dialog_hint_cancel.setText("取消");

                // 点击保存按钮
                my_dialog_hint_ensure.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);


                        String message = dto.getMessage();

                        final String type = ChatUtils.getMsgType(message);


                        String msgid = dto.getMsgId();

                        if (TextUtils.equals(type, MessageType.Text)) {


                            TextMsg textmsg = JsonUtils.fromJson(message, TextMsg.class);

                            long date = System.currentTimeMillis();

                            String textmsg_str = JsonUtils.toJson(textmsg);

                            AttributeDto attributeDto = new AttributeDto();
                            attributeDto.setMask(textmsg.getMask());
                            attributeDto.setDateid(textmsg.getDateid());
                            attributeDto.setCounsel(1);
                            attributeDto.setType(MessageType.Text);

                            String attributeDto_str = JsonUtils.toJson(attributeDto);

                            ImMessage immessage = new TextMessage(textmsg.getText(), attributeDto_str);

                            String TIMMessageStr = JsonUtils.toJson(immessage.getMessage());


                            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", textmsg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, dto.getSend_status(), "", 1, TIMMessageStr);

                            // 发送消息
                            mChatObserver.sendMessage(immessage.getMessage(),msgid);


                        } else if (TextUtils.equals(type, MessageType.Img)) {


                            long date = System.currentTimeMillis();

                            ImgMsg msg = JsonUtils.fromJson(message, ImgMsg.class);

                            TIMImageElem elem = new TIMImageElem();
                            elem.setPath(msg.getFilePath());
                            elem.setLevel(0);
                            msg.setElem(elem);


                            String img_Str = JsonUtils.toJson(msg);

                            AttributeDto attributeDto = new AttributeDto();
                            attributeDto.setMask(msg.getMask());
                            attributeDto.setDateid("");
                            attributeDto.setCounsel(1);
                            attributeDto.setType(MessageType.Img);

                            String attributeDto_str = JsonUtils.toJson(attributeDto);

                            ImMessage immessage = new ImageMessage(msg.getFilePath(), true, attributeDto_str);

                            String TIMMessageStr = JsonUtils.toJson(immessage.getMessage());

                            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", img_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, dto.getSend_status(), "", 1, TIMMessageStr);


                            mChatObserver.sendMessage(immessage.getMessage(),msgid);


                        } else if (TextUtils.equals(type, MessageType.Audio)) {

                            AudioMsg audioMsg = JsonUtils.fromJson(message, AudioMsg.class);

                            long date = System.currentTimeMillis();

                            TIMSoundElem elem = new TIMSoundElem();
                            elem.setPath(audioMsg.getFilepath());
                            elem.setDuration(audioMsg.getDuration());//填写语音时长

                            audioMsg.setElem(elem);

                            AttributeDto attributeDto = new AttributeDto();
                            attributeDto.setMask(audioMsg.getMask());
                            attributeDto.setDateid(audioMsg.getDateid());
                            attributeDto.setCounsel(1);
                            attributeDto.setType(MessageType.Audio);

                            String attributeDto_str = JsonUtils.toJson(attributeDto);

                            ImMessage immessage = new VoiceMessage(elem, attributeDto_str);

                            String TIMMessageStr = JsonUtils.toJson(immessage.getMessage());


                            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", message, date, ChatDto.s_type, ChatDto.readed_status, msgid, dto.getSend_status(), "", 1, TIMMessageStr);


                            mChatObserver.sendMessage(immessage.getMessage(),msgid);

                        }

                        dto.setSend_status(ChatDto.sending);

                        messageAdapter.notifyDataSetChanged();

                        sendFailDialog.dismiss();
                    }

                });
                // 点击取消按钮
                my_dialog_hint_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        sendFailDialog.dismiss();

                    }
                });

            }
        });

        sendFailDialog.show();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("onConnectionListenerTitle")

            }
    )
    public void onConnectionListenerTitle(CommonEvent event) {

        int status = event.getPostion();

        if (status == 0) {
            chat_title_tv.setText("未连接");
        } else {
            chat_title_tv.setText(targetUser_nickName);
        }

    }


}
