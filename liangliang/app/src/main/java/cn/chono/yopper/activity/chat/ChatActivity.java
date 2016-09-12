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
import android.os.CountDownTimer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.orhanobut.logger.Logger;
import com.tencent.TIMConversationType;
import com.tencent.TIMImageElem;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BrokenLock.BrokenLockBean;
import cn.chono.yopper.Service.Http.BrokenLock.BrokenLockRespBean;
import cn.chono.yopper.Service.Http.BrokenLock.BrokenLockService;
import cn.chono.yopper.Service.Http.BrokenLock.UnLockRespDto;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportBean;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportRespBean;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportService;
import cn.chono.yopper.Service.Http.ChatReadMsg.ChatReadMsgBean;
import cn.chono.yopper.Service.Http.ChatReadMsg.ChatReadMsgService;
import cn.chono.yopper.Service.Http.DatingsAttemptHandle.DatingsAttempResp;
import cn.chono.yopper.Service.Http.DatingsAttemptHandle.DatingsAttempRespBean;
import cn.chono.yopper.Service.Http.DatingsAttemptHandle.DatingsAttemptBean;
import cn.chono.yopper.Service.Http.DatingsAttemptHandle.DatingsAttemptService;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingInfoStateDto;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingStatusWithTargetBean;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingStatusWithTargetRespBean;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingStatusWithTargetService;
import cn.chono.yopper.Service.Http.GainPFruit.AvailableEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitBean;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitRespEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitService;
import cn.chono.yopper.Service.Http.KeyStatus.KeyStatusBean;
import cn.chono.yopper.Service.Http.KeyStatus.KeyStatusDto;
import cn.chono.yopper.Service.Http.KeyStatus.KeyStatusRespBean;
import cn.chono.yopper.Service.Http.KeyStatus.KeyStatusService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.appointment.DatingDetailActivity;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.activity.order.BuyKeyActivity;
import cn.chono.yopper.activity.order.UserAppleOrderPayActivity;
import cn.chono.yopper.adapter.ChatGiftAdapter;
import cn.chono.yopper.adapter.ChatMessageAdapter;
import cn.chono.yopper.adapter.EmoViewPagerAdapter;
import cn.chono.yopper.adapter.EmoteAdapter;
import cn.chono.yopper.adapter.GiftViewPagerAdapter;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AttributeDto;
import cn.chono.yopper.data.AudioMsg;
import cn.chono.yopper.data.DatingHandleStatusMsg;
import cn.chono.yopper.data.HintMsg;
import cn.chono.yopper.data.ImgMsg;
import cn.chono.yopper.data.KeyTable;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.NotificationMsg;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.UserReceiveGiftTable;
import cn.chono.yopper.data.UserToUserWithDatingTable;
import cn.chono.yopper.entity.chatgift.GiftInfoEntity;
import cn.chono.yopper.entity.chatgift.GiftOrderResp;
import cn.chono.yopper.entity.chatgift.GiftOrdreReq;
import cn.chono.yopper.entity.chatgift.GiftUtil;
import cn.chono.yopper.entity.chatgift.GiveGiftRpBean;
import cn.chono.yopper.entity.chatgift.PresentGiftInfoBean;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.SendMsgStatusEvent;
import cn.chono.yopper.im.imObserver.ChatObserver;
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
import cn.chono.yopper.utils.DatingUtils;
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
public class ChatActivity extends MainFrameActivity implements DragListView.OnRefreshLoadingMoreListener, OnClickListener, ChatMessageAdapter.OnItemSendFailClickLitener, ChatMessageAdapter.OnItemBrokenkeyClickLitener {

    // 根布局
    private ResizeLayout chat_root_layout;

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

    //礼物布局
    private LinearLayout chat_more_gift_layout;

    // 表情页
    private ViewPager chat_more_face_view_pager;

    //礼物页面
    private ViewPager chat_more_gift_viewPage;


    // 更多其他内容布局 如位置 图片等
    private LinearLayout chat_more_others_layout;

    // 照片按钮
    private LinearLayout chat_photo_layout;

    // 相机按钮
    private LinearLayout chat_camera_layout;

    // 赠送礼物
    private LinearLayout chat_send_gift_layout;

    private LinearLayout chat_bottom_hint_layout;

    private LinearLayout chat_goback_layout;
    private TextView chat_title_tv;
    private LinearLayout chat_option_layout;

    private TextView chat_broken_key_tv;

    private TextView chat_dating_agree_tv;

    private TextView chat_dating_refuse_tv;

    private TextView chat_dating_interested_tv;

    private TextView chat_dating_hint_tv;

    private TextView chat_dating_type_content_tv;

    private RelativeLayout chat_dating_handle_layout;

    private LinearLayout chat_dating_layout;

    private static final int BIGGER = 1;

    private static final int SMALLER = 2;

    private static final int MSG_RESIZE = 1;

    private int curTat;// 当前的状态

    private int face_or_others;// 100时点击了表情按钮 200时点击了更多按钮 300时候listview 500时点击了礼物

    private List<ChatDto> recoverlist;

    private ChatMessageAdapter messageAdapter;


    // 一页10条数据
    private int pagesize = 10;
    // 页数
    private static int pagecount = 0;

    private static int cur_pagecount = 1;
    // 页面数据余数
    private int remainder_count = 0;

    private List<ChatDto> chatdtolist;

    private Handler mhandler;

    // 聊友对象信息
    private UserDto userdto;
    // 自己的信息

    private boolean isopenFace = false;
    private boolean isopenmore = false;

    private boolean isopenSound = false;

    private boolean isopenGift = false;


    private NetState receiver;

    private LinearLayout chat_face_indicator;//图片指示器

    private LinearLayout chat_gift_indicator;//礼物的图片指示器

    private String datingId;

    private String publishDate_userId;

    private int datingHandleStatus;

    private String dating_info_str;

    private int meIsActive;

    private int reply;

    private int is_Broken_key = KeyTable.no_broken;

    private int giftItemPostion = 0;

    private GiftUtil giftUtil;

    @Override
    public void onItemSendFailClick(ChatDto dto) {
        showSendFailDialog(dto);
    }

    @Override
    public void onItemBrokenkeyClick() {
        if (meIsActive == UserToUserWithDatingTable.meActive) {

            if (datingHandleStatus != UserToUserWithDatingTable.status_deny) {
                getAccountInfo();
            }
        }

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

    private int targetUserId;// 当前聊友的id

    private String targetUser_nickName = "";

    private int userid;

    private ChatObserver mChatObserver;

    private int meSex;

    private int giftReceiveStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadingDiaog = DialogUtil.LoadingDialog(ChatActivity.this, null);

        RxBus.get().register(this);

        PushAgent.getInstance(this).onAppStart();

        userid = LoginUser.getInstance().getUserId();

        giftUtil = new GiftUtil();

        chatdtolist = new ArrayList<ChatDto>();

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(YpSettings.USERID)) {
                targetUserId = bundle.getInt(YpSettings.USERID);
            }

            if (bundle.containsKey(YpSettings.DATINGS_ID)) {
                datingId = bundle.getString(YpSettings.DATINGS_ID);
            }

        }

        initComponent();

        mhandler = new Handler();

        initData();

        initChatData();

        ChatUtils.setUserChatRecordReaded(userid + "", targetUserId + "", datingId);

        meSex = DbHelperUtils.getDbUserSex(userid);

        mChatObserver = new ChatObserver(targetUserId + "", TIMConversationType.C2C);

        mChatObserver.readMessages();

        if (meIsActive == UserToUserWithDatingTable.meActive) {

            handleGiftReceive();

        }

    }


    private void handleGiftReceive() {

        if (meSex == 2) {

            if (giftReceiveStatus == 0) {
                giftReceiveStatus = ChatUtils.getGiftReceiveStatus(targetUserId + "", userid + "", datingId);
            }

            if (giftReceiveStatus != UserReceiveGiftTable.Received) {

                giftUtil.receiveGift(datingId, targetUserId, userid);
            }
        }
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

        if (TextUtils.equals(msgId, dto.getMsgId())) {

            return;
        }

        msgId = dto.getMsgId();

        String datingsID = dto.getDatingId();

        if (!CheckUtil.isEmpty(datingsID) && TextUtils.equals(datingsID, datingId)) {


            String type = ChatUtils.getMsgType(dto.getMessage());

            if (TextUtils.equals(type, MessageType.Notification)) {

                NotificationMsg notificationMsg = JsonUtils.fromJson(dto.getMessage(), NotificationMsg.class);

                int notifytype = 0;

                if (notificationMsg != null) {
                    notifytype = notificationMsg.getNotifytype();
                }

                if (notifytype == 11) {
                    //破冰解锁消息
                    is_Broken_key = KeyTable.had_broken;

                    //刷新list
                    messageAdapter.setKeyBroken(is_Broken_key);
                    messageAdapter.notifyDataSetChanged();

                }
            }

            if (chatdtolist == null) {
                chatdtolist = new ArrayList<ChatDto>();
            }


            chatdtolist.add(dto);
            if (messageAdapter == null) {
                messageAdapter = new ChatMessageAdapter(this, chatdtolist, userid, targetUserId, meIsActive, is_Broken_key);
                messageAdapter.setOnItemSendFailClickLitener(this);
                messageAdapter.setOnItemBrokenkeyClickLitener(this);
                messageListView.setAdapter(messageAdapter);
                messageListView.setSelection(messageAdapter.getCount() - 1);

            }

            messageAdapter.setKeyBroken(is_Broken_key);
            messageAdapter.setList(chatdtolist);


            if (type.equals(MessageType.DatingHandleResult)) {

                DatingHandleStatusMsg datingHandleStatusMsg = JsonUtils.fromJson(dto.getMessage(), DatingHandleStatusMsg.class);

                datingHandleStatus = datingHandleStatusMsg.getDatingHandleStatus();

                setDatingViewData(publishDate_userId, dating_info_str, meIsActive, reply, datingHandleStatus, true);

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

        String msgId = event.getMsgId();

        int sendStatus = event.getSendStatus();

        Logger.e("sendStatus===" + sendStatus + "---onSendMessageStatus=msgId=" + msgId);

        TIMSoundElem soundElem = event.getSoundElem();

        changeSendStatus(sendStatus, msgId, soundElem);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideSoftInputView();
            MediaUtil.getInstance().stop();
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
        chat_option_layout = (LinearLayout) this.findViewById(R.id.chat_option_layout);


        chat_option_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);
                hideSoftInputView();
                chat_bottom_hint_layout.setVisibility(View.GONE);
                MediaUtil.getInstance().stop();
                showOptionsDialog();

            }
        });

        chat_goback_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                hideSoftInputView();
                MediaUtil.getInstance().stop();
                chat_bottom_hint_layout.setVisibility(View.GONE);
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

                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (new File(chat_sound_record_btn.getSavePath()).length() == 0) {

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

        chat_more_gift_layout = (LinearLayout) findViewById(R.id.chat_more_gift_layout);

        chat_more_gift_viewPage = (ViewPager) findViewById(R.id.chat_more_gift_view_pager);

        chat_gift_indicator = (LinearLayout) findViewById(R.id.chat_more_gift_indicator);


        chat_more_others_layout = (LinearLayout) this.findViewById(R.id.chat_more_others_layout);

        chat_photo_layout = (LinearLayout) this.findViewById(R.id.chat_photo_layout);
        chat_photo_layout.setOnClickListener(this);

        chat_camera_layout = (LinearLayout) this.findViewById(R.id.chat_camera_layout);
        chat_camera_layout.setOnClickListener(this);


        chat_send_gift_layout = (LinearLayout) this.findViewById(R.id.chat_send_gift_layout);
        chat_send_gift_layout.setOnClickListener(this);

        messageListView = (DragListView) this.findViewById(R.id.chat_msg_listView);

        chat_bottom_hint_layout = (LinearLayout) this.findViewById(R.id.chat_bottom_hint_layout);


        chat_broken_key_tv = (TextView) this.findViewById(R.id.chat_broken_key_tv);
        chat_broken_key_tv.setOnClickListener(this);

        chat_dating_agree_tv = (TextView) this.findViewById(R.id.chat_dating_agree_tv);
        chat_dating_agree_tv.setOnClickListener(this);

        chat_dating_refuse_tv = (TextView) this.findViewById(R.id.chat_dating_refuse_tv);
        chat_dating_refuse_tv.setOnClickListener(this);

        chat_dating_interested_tv = (TextView) this.findViewById(R.id.chat_dating_interested_tv);
        chat_dating_interested_tv.setOnClickListener(this);

        chat_dating_hint_tv = (TextView) this.findViewById(R.id.chat_dating_hint_tv);

        chat_dating_type_content_tv = (TextView) this.findViewById(R.id.chat_dating_type_content_tv);

        chat_dating_handle_layout = (RelativeLayout) this.findViewById(R.id.chat_dating_handle_layout);

        chat_dating_layout = (LinearLayout) this.findViewById(R.id.chat_dating_layout);


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
                chat_bottom_hint_layout.setVisibility(View.GONE);


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
                chat_bottom_hint_layout.setVisibility(View.GONE);

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
                chat_bottom_hint_layout.setVisibility(View.GONE);

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
                chat_bottom_hint_layout.setVisibility(View.GONE);
                chat_more_layout.setVisibility(View.GONE);
                chat_input_et.requestFocus();
                chat_input_et.setCursorVisible(true);
                isopenSound = false;
                isopenFace = false;
                isopenmore = false;
                isopenGift = false;
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

                chat_bottom_hint_layout.setVisibility(View.GONE);

                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);

                return false;
            }
        });


        initEmoView();

        initGiftView();
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

    /**
     * 初始化礼物布局
     */
    private List<GiftInfoEntity> passHotsList = new ArrayList<>();
    HashMap<String, Object> map = new HashMap<>();
    private int accountPCount = 0;

    private void initGiftView() {
        map = giftUtil.getGiftData(LoginUser.getInstance().getUserId());

        passHotsList.addAll((List<GiftInfoEntity>) map.get("listdata"));

        accountPCount = (int) map.get("appcount");

        List<View> views = new ArrayList<>();


        //计算view页数
        int pageCount;

        if (passHotsList.size() % 8 != 0) {

            pageCount = (passHotsList.size() / 8 + 1);

        } else {

            pageCount = (passHotsList.size() / 8);

        }
        Logger.e("礼物条数：：：：" + passHotsList.size() + "获取礼物页数：：：" + (passHotsList.size() / 8));
        for (int i = 0; i < pageCount; i++) {
            views.add(getGiftGridView(i));
        }
        initGrftLayout();
        chat_more_gift_viewPage.setAdapter(new GiftViewPagerAdapter(views));
        chat_more_gift_viewPage.addOnPageChangeListener(new GiftPageChageListener());
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

    class GiftPageChageListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            // 更改指示器图片
            for (int i = 0; i < giftIndicators.length; i++) {
                giftIndicators[arg0].setBackgroundResource(R.drawable.near_detail_img_selected);
                if (arg0 != i) {
                    giftIndicators[i].setBackgroundResource(R.drawable.near_detail_img_no_selected);
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

    ImageView[] giftIndicators;

    private void initGrftLayout() {
        //计算view页数
        int pageCount = 0;
        if (passHotsList.size() % 8 != 0) {
            pageCount = passHotsList.size() / 8 + 1;
        } else {
            pageCount = (passHotsList.size() / 8);
        }
        giftIndicators = new ImageView[pageCount];// 定义指示器数组大小

        for (int i = 0; i < pageCount; i++) {
            int imagePadding = 10;
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(imagePadding, imagePadding, imagePadding, imagePadding);

            indicator_view = new ImageView(this);
            indicator_view.setBackgroundResource(R.drawable.near_detail_img_no_selected);

            giftIndicators[i] = indicator_view;
            if (i == 0) {
                giftIndicators[i].setBackgroundResource(R.drawable.near_detail_img_selected);
            }
            chat_gift_indicator.addView(giftIndicators[i], params);
        }
        chat_gift_indicator.setVisibility(View.VISIBLE);
    }

    private Dialog giveDailog, buyPDailog;
    private ChatGiftAdapter giftAdapter;

    private View getGiftGridView(final int i) {
        View view = View.inflate(this, R.layout.include_emo_gridview, null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setNumColumns(4);
        List<GiftInfoEntity> list = new ArrayList<>();

        int start;
        int end;

        Logger.e("礼物列表：：" + passHotsList.size() + "当前初始化：" + i);
        if (passHotsList.size() > (i * 8 + 8)) {
            start = i * 8;
            end = i * 8 + 8;
        } else {
            start = i * 8;
            end = passHotsList.size();
        }

        Logger.e("礼物列表：： 开始" + start + " 结束： " + end);
        list.addAll(passHotsList.subList(start, end));

        giftAdapter = new ChatGiftAdapter(this, list);
        gridview.setAdapter(giftAdapter);
        gridview.setOnItemClickListener((parent, view1, position1, id1) -> {
            if (i == 0) {
                giftItemPostion = position1;
            } else {
                giftItemPostion = i * 8 + position1;
            }
            String url = passHotsList.get(giftItemPostion).getImageUrl();
            String content = passHotsList.get(giftItemPostion).getGiftName();
            String content2 = " 需" + passHotsList.get(giftItemPostion).getAppleCount() + "个苹果";
            giveDailog = giftUtil.GiftClickDialog(ChatActivity.this, url, content, content2, "赠送", "取消", true);
            giveDailog.show();
        });
        return view;
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

        final EmoteAdapter gridAdapter = new EmoteAdapter(ChatActivity.this, list);
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
        SpannableString spannableString = FaceTextUtils.toSpannableString(ChatActivity.this, content.toString());
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
                    // String conent = input_str.substring(0, start - 5)
                    // + input_str.substring(start + 1);
                    chat_input_et.getEditableText().delete(start - 4, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ChatActivity.this, chat_input_et.getText().toString());
                    chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    chat_input_et.setSelection(start - 4);
                    no_face = false;
                    break;
                }

                if (emo_str_two.equals(face_str)) {
                    // String conent = input_str.substring(0, start - 4)
                    // + input_str.substring(start + 1);
                    chat_input_et.getEditableText().delete(start - 3, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ChatActivity.this, chat_input_et.getText().toString());
                    chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    chat_input_et.setSelection(start - 3);
                    no_face = false;
                    break;
                }
                if (emo_str_three.equals(face_str)) {
                    // String conent = input_str.substring(0, start - 4)
                    // + input_str.substring(start + 1);
                    chat_input_et.getEditableText().delete(start - 5, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(ChatActivity.this, chat_input_et.getText().toString());
                    chat_input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    chat_input_et.setSelection(start - 5);
                    no_face = false;
                    break;
                }
            }

            if (no_face) {
                chat_input_et.getEditableText().delete(start - 1, start);
                SpannableString spannableString = FaceTextUtils.toSpannableString(ChatActivity.this, chat_input_et.getText().toString());
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
                    chat_more_gift_layout.setVisibility(View.GONE);
                    chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);

                    chat_input_et.setVisibility(View.VISIBLE);
                    chat_sound_record_btn.setVisibility(View.GONE);

                    chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

                    isopenFace = false;
                    isopenmore = true;
                    isopenSound = false;
                    isopenGift = false;

                } else if (face_or_others == 100) {
                    chat_more_layout.setVisibility(View.VISIBLE);
                    chat_more_face_layout.setVisibility(View.VISIBLE);
                    chat_more_others_layout.setVisibility(View.GONE);
                    chat_more_gift_layout.setVisibility(View.GONE);
                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                    chat_input_et.setVisibility(View.VISIBLE);
                    chat_sound_record_btn.setVisibility(View.GONE);
                    chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

                    isopenFace = true;
                    isopenmore = false;
                    isopenSound = false;
                    isopenGift = false;

                } else if (face_or_others == 300) {
                    chat_more_layout.setVisibility(View.GONE);
                    chat_more_gift_layout.setVisibility(View.GONE);
                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);

                    chat_input_et.setVisibility(View.GONE);
                    chat_sound_record_btn.setVisibility(View.VISIBLE);
                    chat_sound_iv.setBackgroundResource(R.drawable.chat_input_btn_bg);

                    isopenSound = true;
                    isopenFace = false;
                    isopenmore = false;
                    isopenGift = false;

                } else if (face_or_others == 500) {
                    chat_more_btn.setBackgroundResource(R.drawable.chat_more_add_off);
                    chat_more_layout.setVisibility(View.VISIBLE);
                    chat_more_face_layout.setVisibility(View.GONE);
                    chat_more_others_layout.setVisibility(View.GONE);
                    chat_more_gift_layout.setVisibility(View.VISIBLE);

                    chat_input_et.setVisibility(View.VISIBLE);
                    chat_sound_record_btn.setVisibility(View.GONE);

                    chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

                    isopenFace = false;
                    isopenmore = false;
                    isopenSound = false;
                    isopenGift = true;
//                    giftAdapter.setData();
//                    giftAdapter.notifyDataSetChanged();
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
                    isopenGift = false;
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
                isopenGift = false;
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


                    int mask = 1;
                    if (is_Broken_key == KeyTable.had_broken) {
                        mask = 0;
                    }

                    TextMsg textmsg = new TextMsg(MessageType.Text, str, 0, datingId, mask);


                    long date = System.currentTimeMillis();

                    String textmsg_str = JsonUtils.toJson(textmsg);

                    AttributeDto attributeDto = new AttributeDto();
                    attributeDto.setMask(mask);
                    attributeDto.setDateid(datingId);
                    attributeDto.setCounsel(0);

                    if (mask == 1) {
                        attributeDto.setLockText(str);
                    }

                    attributeDto.setType(MessageType.Text);

                    String attributeDto_str = JsonUtils.toJson(attributeDto);


                    if (mask == 1) {
                        str = "您有一条未解锁的消息";
                    }

                    ImMessage message = new TextMessage(str, attributeDto_str);

                    String TIMMessageStr = JsonUtils.toJson(message.getMessage());

                    String msgid = message.getMessage().getMsgId();


                    ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", textmsg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
                    setChatdtolist(chatDto);
                    ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", textmsg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
                    ChatUtils.saveMessageRecord(textmsg_str, targetUserId + "", ChatDto.sending, ChatDto.s_type, date, 0, datingId, TIMMessageStr);

                    // 发送消息
                    mChatObserver.sendMessage(message.getMessage(), null);


                    reply = UserToUserWithDatingTable.replyed;
                    ChatUtils.saveOrUpdateIsReply(targetUserId + "", datingId, dating_info_str, meIsActive, reply, datingHandleStatus, publishDate_userId + "");

                    handleGiftReceive();

                }

                break;


            // 发送邀约处理 信息
            case 3:

                int mask = 1;
                if (is_Broken_key == KeyTable.had_broken) {
                    mask = 0;
                }

                if (datingHandleStatus == UserToUserWithDatingTable.status_deny) {
                    mask = 0;
                }


                String desc = "[约会结果]";

                DatingHandleStatusMsg msg = new DatingHandleStatusMsg();
                msg.setDateid(datingId);
                msg.setMask(mask);
                msg.setPublishDate_userId(publishDate_userId);
                switch (datingHandleStatus) {
                    case UserToUserWithDatingTable.status_agree:
                        msg.setText("我同意了你的约会请求");
                        if (mask == 1) {
                            str = "您有一条未解锁的消息";
                        }
                        break;

                    case UserToUserWithDatingTable.status_deny:
                        msg.setText("我拒绝了你的约会请求");

                        break;

                    case UserToUserWithDatingTable.status_delay:
                        msg.setText("我再考虑一下");
                        if (mask == 1) {
                            str = "您有一条未解锁的消息";
                        }
                        break;
                }

                msg.setDatingHandleStatus(datingHandleStatus);
                msg.setDatingTheme(dating_info_str);
                msg.setType(MessageType.DatingHandleResult);
                long date = System.currentTimeMillis();
                String msg_str = JsonUtils.toJson(msg);


                ImMessage message = new CustomMessage(msg_str, desc);
                String TIMMessageStr = JsonUtils.toJson(message.getMessage());

                String msgid = message.getMessage().getMsgId();

                ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
                setChatdtolist(chatDto);
                ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
                ChatUtils.saveMessageRecord(msg_str, targetUserId + "", ChatDto.sending, ChatDto.s_type, date, 0, datingId, TIMMessageStr);


                mChatObserver.sendMessage(message.getMessage(), null);

                handleGiftReceive();

                reply = UserToUserWithDatingTable.replyed;
                ChatUtils.saveOrUpdateIsReply(targetUserId + "", datingId, dating_info_str, meIsActive, reply, datingHandleStatus, publishDate_userId + "");

                break;


        }

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
            messageAdapter = new ChatMessageAdapter(this, chatdtolist, userid, targetUserId, meIsActive, is_Broken_key);
            messageListView.setAdapter(messageAdapter);
            messageAdapter.setOnItemSendFailClickLitener(this);
            messageAdapter.setOnItemBrokenkeyClickLitener(this);
            messageAdapter.notifyDataSetChanged();
        }

    }


    private void setUriBitmap(String file_Path) {

        if (CheckUtil.isEmpty(file_Path)) {
            DialogUtil.showDisCoverNetToast(ChatActivity.this, "选取失败，请重新选择！");
            return;
        }

        Bitmap bm = ImgUtils.resizesBitmap(file_Path);

        if (null != bm) {
            // 保存在自己定义文件的路径
            String filePath = ImgUtils.saveImgFile(ChatActivity.this, bm);
            if (CheckUtil.isEmpty(filePath)) {
                DialogUtil.showDisCoverNetToast(ChatActivity.this, "选取失败，请重新选择！");
                return;
            }


            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {
                if (file.length() > 1024 * 1024 * 10) {
                    DialogUtil.showDisCoverNetToast(ChatActivity.this, "图片过大，发送失败！");
                } else {

                    int mask = 1;
                    if (is_Broken_key == KeyTable.had_broken) {
                        mask = 0;
                    }

                    long date = System.currentTimeMillis();

                    ImgMsg msg = new ImgMsg();
                    msg.setType(MessageType.Img);
                    msg.setW(bm.getWidth());
                    msg.setH(bm.getHeight());
                    msg.setFilePath(filePath);
                    msg.setMask(mask);
                    msg.setDateid(datingId);
                    msg.setCounsel(0);

                    TIMImageElem elem = new TIMImageElem();
                    elem.setPath(filePath);
                    elem.setLevel(0);
                    msg.setElem(elem);


                    String img_Str = JsonUtils.toJson(msg);

                    AttributeDto attributeDto = new AttributeDto();
                    attributeDto.setMask(mask);
                    attributeDto.setDateid(datingId);
                    attributeDto.setCounsel(0);
                    attributeDto.setType(MessageType.Img);

                    String attributeDto_str = JsonUtils.toJson(attributeDto);

                    ImMessage message = new ImageMessage(filePath, true, attributeDto_str);

                    String TIMMessageStr = JsonUtils.toJson(message.getMessage());


                    String msgid = message.getMessage().getMsgId();

                    ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", img_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
                    // 加入消息列表
                    setChatdtolist(chatDto);

                    ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", img_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
                    ChatUtils.saveMessageRecord(img_Str, targetUserId + "", ChatDto.sending, ChatDto.s_type, date, 0, datingId, TIMMessageStr);

                    reply = UserToUserWithDatingTable.replyed;
                    ChatUtils.saveOrUpdateIsReply(targetUserId + "", datingId, dating_info_str, meIsActive, reply, datingHandleStatus, publishDate_userId + "");

                    mChatObserver.sendMessage(message.getMessage(), null);
                    handleGiftReceive();

                }

            } else {
                DialogUtil.showDisCoverNetToast(ChatActivity.this, "图片不存在！");
            }


        } else {
            DialogUtil.showDisCoverNetToast(ChatActivity.this, "选取失败，请重新选择！");
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
                        String messageStr = JsonUtils.toJson(audioMsg);
                        dto.setMessage(messageStr);
                    }
                }

                ChatUtils.changeSendStatus(send_status, msgId, soundElem);

                messageAdapter.notifyDataSetChanged();

                chat_root_layout.postInvalidate();


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
                    if (userdto != null) {
                        chat_title_tv.setText(targetUser_nickName);
                    }

                }
            }
        }
    }

    /**
     * 获取聊友的信息 系统账号则不用获取
     *
     * @param id
     */
    private void getUserInfoWithID(final int id) {

        UserInfo userInfo = DbHelperUtils.getUserInfo(id);
        if (userInfo != null) {
            userdto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
            if (userdto != null) {
                targetUser_nickName = userdto.getProfile().getName();
                chat_title_tv.setText(targetUser_nickName);
                if (userdto.getProfile().getSex() == 2) {
                    //对方是女性
                    chat_send_gift_layout.setVisibility(View.VISIBLE);
                } else {
                    chat_send_gift_layout.setVisibility(View.GONE);
                }
            }
        }

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

        ChatUtils.setUserChatRecordReaded(userid + "", targetUserId + "", datingId);

        RxBus.get().post("refreshMessageList", new CommonEvent<>());

        RxBus.get().unregister(this);
        giftUtil.unSubscribe();
        mChatObserver.stop();

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

                MediaUtil.getInstance().stop();


                chat_more_layout.setVisibility(View.GONE);
                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                isopenmore = false;

                MultiImageSelector.create()
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                        .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                        .start(ChatActivity.this, YpSettings.PHOTO_SELECT);


                break;
            case R.id.chat_camera_layout:// 拍照


                chat_more_layout.setVisibility(View.GONE);
                chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
                isopenmore = false;


                MultiImageSelector.create()
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                        .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                        .start(ChatActivity.this, YpSettings.PHOTO_SELECT);

                break;

            case R.id.chat_send_gift_layout:

                chat_send_gift_layout.setVisibility(View.VISIBLE);
                chat_more_layout.setTag(1000);

                face_or_others = 500;

                if (curTat == SMALLER) {// 软键盘显示了
                    changeInput();
                } else {
                    setMoreLayoutVisible(true);
                }
                break;

            case R.id.chat_dating_agree_tv:// 同意
                HandleDatings(UserToUserWithDatingTable.status_agree);
                break;

            case R.id.chat_dating_refuse_tv:// 拒绝
                HandleDatings(UserToUserWithDatingTable.status_deny);
                break;

            case R.id.chat_dating_interested_tv:// 再考虑
                HandleDatings(UserToUserWithDatingTable.status_delay);
                break;

            case R.id.chat_broken_key_tv:// 遮挡面板
                //提示解锁
                if (meIsActive == UserToUserWithDatingTable.meActive) {

                    if (datingHandleStatus != UserToUserWithDatingTable.status_deny) {
                        getAccountInfo();
                    }
                }

            default:
                break;
        }

    }


    //获取苹果与keys数量
    private void getAccountInfo() {

        loadingDiaog = DialogUtil.LoadingDialog(ChatActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        GainPFruitBean fruitBean = new GainPFruitBean();
        fruitBean.setUserId(userid);

        GainPFruitService pFruitService = new GainPFruitService(this);

        pFruitService.parameter(fruitBean);
        pFruitService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                GainPFruitRespEntity fruitRespBean = (GainPFruitRespEntity) respBean;
                AvailableEntity available = fruitRespBean.resp;
                int key_count = available.keyCount;

                if (key_count >= 1) {
                    brokenKeyDialog = DialogUtil.createChatBrokenKeyDialog(ChatActivity.this, key_count + "", doBrokenKeyBackCallListener);
                    if (!isFinishing()) {
                        brokenKeyDialog.show();
                    }

                } else {
                    underKeyDialog = DialogUtil.createChatUnderKeyDialog(ChatActivity.this, underKeyBackCallListener);
                    if (!isFinishing()) {
                        underKeyDialog.show();
                    }
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(ChatActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChatActivity.this, msg);
            }
        });

        pFruitService.enqueue();
    }


    private Dialog brokenKeyDialog, underKeyDialog;
    private Dialog optionsDialog, loadingDiaog, hintdialog, reportDialog;

    public void showOptionsDialog() {
        // 初始化一个自定义的Dialog
        optionsDialog = new MyDialog(ChatActivity.this, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);

                select_operate_dialog_title_tv.setText("操作");
                select_operate_dialog_one_tv.setText("邀约详情");
                select_operate_dialog_two_tv.setText("举报");


                select_operate_dialog_one_layout.setVisibility(View.VISIBLE);
                select_operate_dialog_two_layout.setVisibility(View.VISIBLE);
                select_operate_dialog_three_layout.setVisibility(View.GONE);

                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);
                        optionsDialog.dismiss();
                        Bundle bundle = new Bundle();

                        bundle.putInt(YpSettings.USERID, Integer.valueOf(publishDate_userId));

                        bundle.putString(YpSettings.DATINGS_ID, datingId);

                        ActivityUtil.jump(ChatActivity.this, DatingDetailActivity.class, bundle, 0, 100);

                    }

                });

                select_operate_dialog_two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        optionsDialog.dismiss();
                        showRePortDialog();
                    }

                });


            }
        });
        optionsDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        optionsDialog.show();

    }


    private BackCallListener underKeyBackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                underKeyDialog.dismiss();
            }
            //去快速获取Key
            Bundle bundle = new Bundle();

            bundle.putInt("apple_count", 100);
            bundle.putInt("key_count", 5);
            bundle.putInt("keyPrice", 20);

            ActivityUtil.jump(ChatActivity.this, BuyKeyActivity.class, bundle, 0, 100);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                underKeyDialog.dismiss();
            }

        }
    };


    private BackCallListener doBrokenKeyBackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                brokenKeyDialog.dismiss();
            }
            doBrokenKey();
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                brokenKeyDialog.dismiss();
            }

        }
    };


    public void showRePortDialog() {
        // 初始化一个自定义的Dialog
        reportDialog = new MyDialog(ChatActivity.this, R.style.MyDialog, R.layout.select_operate_post_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                TextView select_operate_post_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_title_tv);
                LinearLayout select_operate_post_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_one_layout);
                LinearLayout select_operate_post_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_two_layout);
                LinearLayout select_operate_post_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_three_layout);
                LinearLayout select_operate_post_dialog_four_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_four_layout);
                LinearLayout select_operate_post_dialog_five_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_five_layout);

                TextView select_operate_post_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_one_tv);

                TextView select_operate_post_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_two_tv);

                TextView select_operate_post_dialog_three_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_three_tv);

                TextView select_operate_post_dialog_four_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_four_tv);

                TextView select_operate_post_dialog_five_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_five_tv);

                select_operate_post_dialog_title_tv.setText("举报原因");
                select_operate_post_dialog_one_tv.setText("诽谤谩骂");
                select_operate_post_dialog_two_tv.setText("色情骚扰");
                select_operate_post_dialog_three_tv.setText("垃圾广告");
                select_operate_post_dialog_four_tv.setText("欺诈(酒托、饭托等)");
                select_operate_post_dialog_five_tv.setText("违法(涉毒、暴恐等)");

                select_operate_post_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        reportDialog.dismiss();
                        doReportRequest("诽谤谩骂");

                    }

                });

                select_operate_post_dialog_two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        reportDialog.dismiss();
                        doReportRequest("色情骚扰");

                    }

                });

                select_operate_post_dialog_three_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        reportDialog.dismiss();

                        doReportRequest("垃圾广告");

                    }

                });

                select_operate_post_dialog_four_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        reportDialog.dismiss();

                        doReportRequest("欺诈(酒托、饭托等)");

                    }

                });

                select_operate_post_dialog_five_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        reportDialog.dismiss();
                        doReportRequest("违法(涉毒、暴恐等)");
                    }
                });


            }
        });
        reportDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        reportDialog.show();

    }

    /**
     * 举报
     */
    private void doReportRequest(String content) {

//		String url = "Type=2&Id=" + userID + "&Content=涉黄";

        loadingDiaog = DialogUtil.LoadingDialog(ChatActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        BubblingReportBean reportBean = new BubblingReportBean();
        reportBean.setId(targetUserId + "");
        reportBean.setType("2");
        reportBean.setContent(content);

        BubblingReportService reportService = new BubblingReportService(this);
        reportService.parameter(reportBean);
        reportService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                BubblingReportRespBean reportRespBean = (BubblingReportRespBean) respBean;

                loadingDiaog.dismiss();

                hintdialog = DialogUtil.createSuccessHintDialog(
                        ChatActivity.this, "举报成功!");
                if (!ChatActivity.this.isFinishing()) {
                    hintdialog.show();
                    successtimer = new SuccessTimer(2000, 1000);
                    successtimer.start();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ChatActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChatActivity.this, msg);
            }
        });
        reportService.enqueue();

    }


    private SuccessTimer successtimer;

    private class SuccessTimer extends CountDownTimer {

        public SuccessTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            if (hintdialog != null) {
                hintdialog.dismiss();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


    /**
     * 未读消息
     */
    private void postChatReadMsg(String fromuser, String targetUserId) {

        ChatReadMsgBean attamptBean = new ChatReadMsgBean();
        attamptBean.setTargetUserId(targetUserId);
        attamptBean.setFromUserId(fromuser);
        attamptBean.setDatingId(datingId);

        ChatReadMsgService chatReadMsgService = new ChatReadMsgService(this);
        chatReadMsgService.parameter(attamptBean);
        chatReadMsgService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

            }
        });
        chatReadMsgService.enqueue();
    }


    /**
     * 解锁
     */
    private void doBrokenKey() {

        loadingDiaog = DialogUtil.LoadingDialog(ChatActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        BrokenLockBean bean = new BrokenLockBean();
        bean.setOtherUserId(targetUserId + "");


        BrokenLockService brokenLockService = new BrokenLockService(this);
        brokenLockService.parameter(bean);
        brokenLockService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                BrokenLockRespBean brokenLockRespBean = (BrokenLockRespBean) respBean;

                loadingDiaog.dismiss();

                UnLockRespDto dto = brokenLockRespBean.getResp();

                if (dto.isSuccess()) {
                    //解锁成功
                    is_Broken_key = KeyTable.had_broken;
                    ChatUtils.saveOrUpdateKeyRecord(userid + "", targetUserId + "", KeyTable.had_broken);

                    if (datingHandleStatus == UserToUserWithDatingTable.status_deny) {
                        chat_broken_key_tv.setVisibility(View.VISIBLE);
                        if (meIsActive == UserToUserWithDatingTable.meActive) {
                            chat_broken_key_tv.setText("对方已拒绝您，本次活动您不能再联系他");
                        } else {
                            chat_broken_key_tv.setText("您已拒绝对方，本次活动中您不能再联系他");
                        }
                    } else {
                        chat_broken_key_tv.setVisibility(View.GONE);
                    }

                    //刷新list
                    messageAdapter.setKeyBroken(is_Broken_key);
                    messageAdapter.notifyDataSetChanged();

                } else {
                    DialogUtil.showDisCoverNetToast(ChatActivity.this, dto.getErrorMsg());
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ChatActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChatActivity.this, msg);
            }
        });
        brokenLockService.enqueue();

    }


    /**
     * 邀约处理：同意 拒绝 再考虑
     */
    private void HandleDatings(final int type) {

        loadingDiaog = DialogUtil.LoadingDialog(ChatActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        DatingsAttemptBean bean = new DatingsAttemptBean();
        bean.setTargetUserId(targetUserId + "");
        bean.setDatingId(datingId);
        bean.setType(type);


        DatingsAttemptService datingsAttemptService = new DatingsAttemptService(this);
        datingsAttemptService.parameter(bean);
        datingsAttemptService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                DatingsAttempRespBean dtos = (DatingsAttempRespBean) respBean;

                if (dtos != null) {

                    DatingsAttempResp datingsAttempResp = dtos.getResp();

                    if (datingsAttempResp.getResult() == 0) {
                        //处理成功 更新界面
                        datingHandleStatus = type;

                        setDatingViewData(publishDate_userId, dating_info_str, meIsActive, reply, datingHandleStatus, true);

                        //自己给对方发一条消息
                        sendMessageIfNotNull("", 3);

                    } else if (datingsAttempResp.getResult() == 1) {

                        if (!TextUtils.isEmpty(datingsAttempResp.getMsg())) {

                            HintMsg hintMsg = new HintMsg();
                            hintMsg.setType(MessageType.Hint);
                            hintMsg.setText(datingsAttempResp.getMsg());

                            String msgStr = JsonUtils.toJson(hintMsg);

                            ImMessage message = new CustomMessage(datingsAttempResp.getMsg(), "");

                            String msgid = message.getMessage().getMsgId();

                            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingId, 0, "");

                            ChatUtils.saveMessageRecord(msgStr, targetUserId + "", ChatDto.succeed, ChatDto.r_type, System.currentTimeMillis(), 0, datingId, "");

                            ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingId, 0, "");

                            setChatdtolist(chatDto);
                        }

                    }
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ChatActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChatActivity.this, msg);
            }
        });
        datingsAttemptService.enqueue();

    }


    /**
     * 获取解锁状态
     */
    private void getBrokenKeyStatus() {


        if (loadingDiaog == null) {
            if (!isFinishing()) {
                loadingDiaog.show();
            }
        }

        KeyStatusBean bean = new KeyStatusBean();
        bean.setTargetUserId(targetUserId + "");


        KeyStatusService keyStatusService = new KeyStatusService(this);
        keyStatusService.parameter(bean);
        keyStatusService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                KeyStatusRespBean keyStatusRespBean = (KeyStatusRespBean) respBean;

                loadingDiaog.dismiss();

                KeyStatusDto dto = keyStatusRespBean.getResp();
                String state = dto.getState();
                if (TextUtils.equals(state, "Unlock")) {
                    is_Broken_key = KeyTable.had_broken;
                    ChatUtils.saveOrUpdateKeyRecord(userid + "", targetUserId + "", KeyTable.had_broken);
                } else {
                    is_Broken_key = KeyTable.no_broken;
                }


                HandleInputKeyView();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ChatActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChatActivity.this, msg);
            }
        });
        keyStatusService.enqueue();

    }


    private void initData() {

        postChatReadMsg(userid + "", targetUserId + "");

        getUserInfoWithID(targetUserId);


        UserToUserWithDatingTable dto = ChatUtils.getDatingTable(userid + "", targetUserId + "", datingId);

        if (dto != null) {

            publishDate_userId = dto.getPublishDate_userId();

            datingHandleStatus = dto.getDatingDealStatus();

            dating_info_str = dto.getDatingTheme();

            meIsActive = dto.getMeIsActive();

            reply = dto.getIsReply();

            setDatingViewData(publishDate_userId, dating_info_str, meIsActive, reply, datingHandleStatus, false);


        } else {
            getDatingHandleStatusInfo();
        }

        KeyTable keyTable = ChatUtils.getKeyRecord(userid + "", targetUserId + "");

        if (keyTable != null) {
            is_Broken_key = keyTable.getIsBrokenKey();

            if (is_Broken_key != KeyTable.had_broken) {
                getBrokenKeyStatus();
            } else {
                HandleInputKeyView();
            }

        } else {
            getBrokenKeyStatus();
        }

    }


    private void HandleInputKeyView() {


        if (meIsActive == UserToUserWithDatingTable.meActive) {
            if (is_Broken_key == KeyTable.had_broken) {

                if (datingHandleStatus == UserToUserWithDatingTable.status_deny) {
                    chat_broken_key_tv.setVisibility(View.VISIBLE);
                    chat_broken_key_tv.setText("对方已拒绝您，本次活动您不能再联系他");
                } else {
                    chat_broken_key_tv.setVisibility(View.GONE);
                }

            } else {

                chat_broken_key_tv.setVisibility(View.VISIBLE);
                if (datingHandleStatus == UserToUserWithDatingTable.status_deny) {
                    chat_broken_key_tv.setText("对方已拒绝您，本次活动您不能再联系他");
                } else {
                    chat_broken_key_tv.setText("解锁后，聊天将不受任何限制");
                }
            }

        } else {

            if (datingHandleStatus == UserToUserWithDatingTable.status_deny) {
                chat_broken_key_tv.setVisibility(View.VISIBLE);
                chat_broken_key_tv.setText("您已拒绝对方，本次活动中您不能再联系他");
            } else {
                chat_broken_key_tv.setVisibility(View.GONE);
            }

        }

        if (messageAdapter != null) {
            messageAdapter.setMeIsActive(meIsActive);
        }

    }

    /**
     * 获取邀约处理信息以及是否聊天发起者
     */
    private void getDatingHandleStatusInfo() {

        loadingDiaog = DialogUtil.LoadingDialog(ChatActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        DatingStatusWithTargetBean bean = new DatingStatusWithTargetBean();
        bean.setDatingId(datingId);
        bean.setOtherUserId(targetUserId);
        DatingStatusWithTargetService datingStatusWithTargetService = new DatingStatusWithTargetService(ChatActivity.this);

        datingStatusWithTargetService.parameter(bean);

        datingStatusWithTargetService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                DatingStatusWithTargetRespBean idRespBean = (DatingStatusWithTargetRespBean) respBean;

                DatingInfoStateDto datingInfoStateDto = idRespBean.getResp();

                publishDate_userId = datingInfoStateDto.getDating().getOwner().getUserId() + "";

                datingHandleStatus = datingInfoStateDto.getChatState();

                dating_info_str = DatingUtils.getChatDatingTitle(datingInfoStateDto.getDating());


                if (datingInfoStateDto.isCurrentUserLauncher()) {

                    meIsActive = UserToUserWithDatingTable.meActive;
                    reply = UserToUserWithDatingTable.replyed;

                } else {

                    meIsActive = UserToUserWithDatingTable.me_no_Active;
                    reply = UserToUserWithDatingTable.no_reply;

                }


                setDatingViewData(publishDate_userId, dating_info_str, meIsActive, reply, datingHandleStatus, true);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ChatActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChatActivity.this, msg);


            }
        });

        datingStatusWithTargetService.enqueue();
    }

    /**
     * 邀约信息 以及处理按钮 设置数据
     * 同时根据需要是否讲邀约一些信息缓存到数据库
     */
    private void setDatingViewData(String publishDate_userId, String dating_info_str, int meIsActive, int reply, int datingHandleStatus, boolean isSave) {


        chat_dating_layout.setVisibility(View.VISIBLE);

        chat_dating_type_content_tv.setText(dating_info_str);


        if (meIsActive == UserToUserWithDatingTable.meActive) {
            //主动
            switch (datingHandleStatus) {

                case 0:


                case 1://未处理

                    if (TextUtils.equals(publishDate_userId, userid + "")) {
                        chat_dating_hint_tv.setText("已邀请" + targetUser_nickName + "参加您的邀约，等待对方回应");
                    } else {
                        chat_dating_hint_tv.setText("已告知" + targetUser_nickName + "您感兴趣，等待对方回应");
                    }

                    break;
                case UserToUserWithDatingTable.status_agree://同意
                    chat_dating_hint_tv.setText("已和" + targetUser_nickName + "达成活动意向");
                    break;

                case UserToUserWithDatingTable.status_deny://拒绝
                    chat_dating_hint_tv.setText(targetUser_nickName + "拒绝了您的约会请求");
                    break;

                case UserToUserWithDatingTable.status_delay://再考虑
                    if (TextUtils.equals(publishDate_userId, userid + "")) {
                        chat_dating_hint_tv.setText("已邀请" + targetUser_nickName + "参加您的邀约，等待对方回应");
                    } else {
                        chat_dating_hint_tv.setText("已告知" + targetUser_nickName + "您感兴趣，等待对方回应");
                    }
                    break;

            }
            chat_dating_handle_layout.setVisibility(View.GONE);

        } else {
            //被动

            switch (datingHandleStatus) {
                case 0:

                case 1://未处理

                    if (TextUtils.equals(publishDate_userId, userid + "")) {
                        chat_dating_hint_tv.setText(targetUser_nickName + "想参加您的活动");
                    } else {
                        chat_dating_hint_tv.setText(targetUser_nickName + "想邀请您参加他的活动");
                    }
                    chat_dating_handle_layout.setVisibility(View.VISIBLE);

                    chat_dating_interested_tv.setVisibility(View.VISIBLE);
                    chat_dating_refuse_tv.setVisibility(View.VISIBLE);
                    chat_dating_agree_tv.setVisibility(View.VISIBLE);

                    break;
                case UserToUserWithDatingTable.status_agree://同意
                    chat_dating_hint_tv.setText("已和" + targetUser_nickName + "达成活动意向");
                    chat_dating_handle_layout.setVisibility(View.GONE);

                    reply = UserToUserWithDatingTable.replyed;

                    break;

                case UserToUserWithDatingTable.status_deny://拒绝
                    if (TextUtils.equals(publishDate_userId, userid + "")) {
                        chat_dating_hint_tv.setText("您已拒绝了" + targetUser_nickName + "的约会请求");
                    } else {
                        chat_dating_hint_tv.setText(targetUser_nickName + "拒绝了您的约会请求");
                    }
                    chat_dating_handle_layout.setVisibility(View.GONE);

                    reply = UserToUserWithDatingTable.replyed;
                    break;

                case UserToUserWithDatingTable.status_delay://再考虑
                    if (TextUtils.equals(publishDate_userId, userid + "")) {
                        chat_dating_hint_tv.setText("已邀请" + targetUser_nickName + "参加您的邀约，等待对方回应");
                    } else {
                        chat_dating_hint_tv.setText(targetUser_nickName + "想邀请您参加他的邀约");
                    }
                    chat_dating_handle_layout.setVisibility(View.VISIBLE);
                    chat_dating_interested_tv.setVisibility(View.GONE);
                    chat_dating_refuse_tv.setVisibility(View.VISIBLE);
                    chat_dating_agree_tv.setVisibility(View.VISIBLE);

                    reply = UserToUserWithDatingTable.replyed;
                    break;

            }
        }

        if (isSave) {
            ChatUtils.saveOrUpdateDatingStatusTable(targetUserId + "", datingId, dating_info_str, meIsActive, reply, datingHandleStatus, publishDate_userId + "");
        }

        HandleInputKeyView();
    }


    private void initChatData() {
        try {
            cur_pagecount = 1;
            recoverlist = new ArrayList<ChatDto>();

            recoverlist = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("jid", " =", targetUserId + "").and("mid", " =", userid + "").and("datingId", " =", datingId).orderBy("date"));

            if (recoverlist != null && recoverlist.size() > 0) {

                // private int pagesize=10;
                // //页数
                // private static int pagecount=0;
                // //页面数据余数
                // private int remainder_count=0;

                chat_bottom_hint_layout.setVisibility(View.GONE);

                remainder_count = recoverlist.size() % pagesize;
                if (remainder_count > 0) {
                    pagecount = recoverlist.size() / pagesize + 1;
                } else {
                    pagecount = recoverlist.size() / pagesize;
                }

                if (pagecount > 1) {
                    chatdtolist = recoverlist.subList(pagecount * pagesize - (cur_pagecount + 1) * pagesize + remainder_count, recoverlist.size());
                    // 允许下拉
                    messageAdapter = new ChatMessageAdapter(this, chatdtolist, userid, targetUserId, meIsActive, is_Broken_key);
                    messageAdapter.setOnItemSendFailClickLitener(this);
                    messageAdapter.setOnItemBrokenkeyClickLitener(this);
                    messageListView.setAdapter(messageAdapter);
                    messageListView.setSelection(chatdtolist.size() - 1);

                } else {
                    chatdtolist = recoverlist;
                    // bu允许下拉
                    // messageListView.R

                    messageAdapter = new ChatMessageAdapter(this, chatdtolist, userid, targetUserId, meIsActive, is_Broken_key);
                    messageListView.setAdapter(messageAdapter);
                    messageAdapter.setOnItemSendFailClickLitener(this);
                    messageAdapter.setOnItemBrokenkeyClickLitener(this);
                    messageListView.setSelection(chatdtolist.size() - 1);
                }

            } else {

                chat_bottom_hint_layout.setVisibility(View.VISIBLE);
                messageAdapter = new ChatMessageAdapter(this, chatdtolist, userid, targetUserId, meIsActive, is_Broken_key);
                messageListView.setAdapter(messageAdapter);
                messageAdapter.setOnItemSendFailClickLitener(this);
                messageAdapter.setOnItemBrokenkeyClickLitener(this);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private void saveAudio(String filepath, int time) {

        int mask = 1;
        if (is_Broken_key == KeyTable.had_broken) {
            mask = 0;
        }

        long date = System.currentTimeMillis();
        AudioMsg msg = new AudioMsg();
        msg.setType(MessageType.Audio);
        msg.setCounsel(0);
        msg.setMask(mask);
        msg.setDateid(datingId);
        msg.setDuration(time);
        msg.setFilepath(filepath);

        TIMSoundElem elem = new TIMSoundElem();
        elem.setPath(filepath);
        elem.setDuration(time);//填写语音时长

        msg.setElem(elem);


        String msg_str = JsonUtils.toJson(msg);


        AttributeDto attributeDto = new AttributeDto();
        attributeDto.setMask(mask);
        attributeDto.setDateid(datingId);
        attributeDto.setCounsel(0);
        attributeDto.setType(MessageType.Audio);

        String attributeDto_str = JsonUtils.toJson(attributeDto);

        ImMessage message = new VoiceMessage(elem, attributeDto_str);


        String msgid = message.getMessage().getMsgId();

        String TIMMessageStr = JsonUtils.toJson(message.getMessage());

        ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);

        //加入消息列表
        setChatdtolist(chatDto);

        ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
        ChatUtils.saveMessageRecord(msg_str, targetUserId + "", ChatDto.sending, ChatDto.s_type, date, 0, datingId, TIMMessageStr);

        mChatObserver.sendMessage(message.getMessage(), null);
        handleGiftReceive();

    }


    /**
     * 清理缓存提示框 可自行设置标题 提示内容 以及按钮文本
     */

    private Dialog sendFailDialog;

    public void showSendFailDialog(final ChatDto dto) {

        // 初始化一个自定义的Dialog
        sendFailDialog = new MyDialog(ChatActivity.this, R.style.MyDialog, R.layout.my_hint_operate_dialog_layout, new MyDialog.DialogEventListener() {

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


                        TIMMessage tIMMessage = JsonUtils.fromJson(dto.getTIMMessage(), TIMMessage.class);

                        String msgid = dto.getMsgId();


                        if (TextUtils.equals(type, MessageType.Text)) {


                            TextMsg textmsg = JsonUtils.fromJson(message, TextMsg.class);

                            long date = System.currentTimeMillis();

                            String textmsg_str = JsonUtils.toJson(textmsg);

                            AttributeDto attributeDto = new AttributeDto();
                            attributeDto.setMask(textmsg.getMask());
                            attributeDto.setDateid(textmsg.getDateid());
                            attributeDto.setCounsel(0);
                            attributeDto.setType(MessageType.Text);

                            if (textmsg.getMask() == 1) {
                                attributeDto.setLockText(textmsg.getText());
                            }

                            String attributeDto_str = JsonUtils.toJson(attributeDto);

                            ImMessage immessage = new TextMessage(textmsg.getText(), attributeDto_str);

                            String TIMMessageStr = JsonUtils.toJson(immessage.getMessage());


                            Logger.e("showSendFailDialog=msgId=" + msgid);

                            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", textmsg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, dto.getSend_status(), datingId, 0, TIMMessageStr);

                            // 发送消息
                            mChatObserver.sendMessage(immessage.getMessage(), msgid);


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
                            attributeDto.setDateid(datingId);
                            attributeDto.setCounsel(0);
                            attributeDto.setType(MessageType.Img);

                            String attributeDto_str = JsonUtils.toJson(attributeDto);

                            ImMessage immessage = new ImageMessage(msg.getFilePath(), true, attributeDto_str);

                            String TIMMessageStr = JsonUtils.toJson(immessage.getMessage());

                            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", img_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, dto.getSend_status(), datingId, 0, TIMMessageStr);


                            mChatObserver.sendMessage(immessage.getMessage(), msgid);


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
                            attributeDto.setCounsel(0);
                            attributeDto.setType(MessageType.Audio);

                            String attributeDto_str = JsonUtils.toJson(attributeDto);

                            ImMessage immessage = new VoiceMessage(elem, attributeDto_str);

                            String TIMMessageStr = JsonUtils.toJson(immessage.getMessage());


                            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", message, date, ChatDto.s_type, ChatDto.readed_status, msgid, dto.getSend_status(), datingId, 0, TIMMessageStr);

                            mChatObserver.sendMessage(immessage.getMessage(), msgid);

                        }

                        dto.setSend_status(ChatDto.sending);

                        handleGiftReceive();

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


    //取消按钮
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("cancelbtn")
            }
    )
    public void cancel(Object iscancel) {

        if (null != giveDailog && giveDailog.isShowing()) {

            giveDailog.dismiss();
        }

        if (null != buyPDailog && buyPDailog.isShowing()) {

            buyPDailog.dismiss();
        }
    }

    //确定按钮
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("sureBtn")
            }
    )
    public void sure(Object sureBtn) {
        Logger.e("点击了确定送礼物按钮 ；；；；" + sureBtn);
        if ((int) sureBtn == 1) {
            //赠送
            giftUtil.giveGift(ChatActivity.this, passHotsList.get(giftItemPostion).getGiftId(), datingId, targetUserId, false, giftItemPostion);
            if (!loadingDiaog.isShowing()) {
                loadingDiaog.show();
            }
////            GiveGiftBody body = new GiveGiftBody();
////            body.setDatingId(datingId);
////            body.setIsFirstCall(true);
////            body.setToUserId(targetUserId);
////            Subscription subscription = HttpFactory.getHttpApi().giveGift(passHotsList.get(giftItemPostion).getGiftId(), body)
////                    .compose(SchedulersCompat.applyIoSchedulers())
////                    .compose(RxResultHelper.handleResult())
////                    .subscribe(givegift -> {
////                        Logger.e("赠送礼物的返回成功");
////                        // 赠送结果（0：成功 1：对方Hot 2：苹果数量不足）\r\n            当对方Hot时，app端组织提示文案
////                        if (givegift.getResult() == 0) {
////                            RxBus.get().post("giveSuccess", position);
////                        } else if (givegift.getResult() == 1) {
////                            RxBus.get().post("giveHotFiled", position);
////                        } else if (givegift.getResult() == 2) {
////                            RxBus.get().post("giveFiled", position);
////                        }
////                    }, throwable -> {
////                        Logger.e("赠送礼物的请求已经出现异常");
////                        DialogUtil.showDisCoverNetToast(context, "赠送出现异常了");
////                    });
//
            giveDailog.dismiss();
        } else {
            GiftInfoEntity passHot = passHotsList.get(giftItemPostion);

            PresentGiftInfoBean giftInfoBean = new PresentGiftInfoBean();
            giftInfoBean.setToUserId(targetUserId);
            giftInfoBean.setDatingId(datingId);
            giftInfoBean.setFirstCall(false);
            giftInfoBean.setFromUserId(LoginUser.getInstance().getUserId());
            giftInfoBean.setGiftId(passHot.getGiftId());

            GiftOrdreReq req = new GiftOrdreReq();
            req.setNeedAppleCount(passHot.getAppleCount());
            req.setPresentGiftInfo(giftInfoBean);

            giftUtil.giveGiftOrderInfo(req);
            if (!loadingDiaog.isShowing()) {
                loadingDiaog.show();
            }

        }

    }

    //赠送成功
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giveSuccess")
            }
    )
    public void giveSuccess(Object position) {
        loadingDiaog.dismiss();
        if (null != giveDailog && giveDailog.isShowing()) {
            giveDailog.dismiss();
        }

//        chat_bottom_hint_layout.setVisibility(View.GONE);
//        chat_more_layout.setVisibility(View.GONE);
//        chat_input_et.requestFocus();
//        chat_input_et.setCursorVisible(true);
//        isopenSound = false;
//        isopenFace = false;
//        isopenmore = false;
//        isopenGift = false;
//        chat_more_btn.setBackgroundResource(R.drawable.chat_more_bg);
//        chat_expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
//        chat_sound_iv.setBackgroundResource(R.drawable.chat_sound_btn_bg);

//        sendGiftMsg();

    }

    //hot用户
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giveHotFiled")
            }
    )
    public void giveHotFiled(Object position) {
        loadingDiaog.dismiss();
        DialogUtil.showDisCoverNetToast(ChatActivity.this, "对方是HOT用户");
        if (null != giveDailog && giveDailog.isShowing()) {
            giveDailog.dismiss();
        }
    }

    //苹果不足
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giveFiled")
            }
    )
    public void giveFiled(CommonEvent commonEvent) {
        loadingDiaog.dismiss();
        if (null != giveDailog && giveDailog.isShowing()) {
            giveDailog.dismiss();
        }

        GiveGiftRpBean dto = (GiveGiftRpBean) commonEvent.getEvent();
        String content = dto.getMsg();//"您的苹果数量不足，无法赠送礼物，点击购买苹果，购买成功后，直接送出礼物，剩余苹果不消耗";
        buyPDailog = giftUtil.GiftClickDialog(ChatActivity.this, "", content, "", "马上获取苹果", "取消", false);
        buyPDailog.show();
    }


    //送礼物存在拉黑关系
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giveFiledWithBlock")
            }
    )
    public void giveFiledWithBlock(CommonEvent commonEvent) {

        loadingDiaog.dismiss();
        if (null != giveDailog && giveDailog.isShowing()) {
            giveDailog.dismiss();
        }

        GiveGiftRpBean dto = (GiveGiftRpBean) commonEvent.getEvent();

        if (dto != null) {

            HintMsg hintMsg = new HintMsg();
            hintMsg.setType(MessageType.Hint);
            hintMsg.setText(dto.getMsg());

            String msgStr = JsonUtils.toJson(hintMsg);

            ImMessage message = new CustomMessage(dto.getMsg(), "");

            String msgid = message.getMessage().getMsgId();

            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingId, 0, "");

            ChatUtils.saveMessageRecord(msgStr, targetUserId + "", ChatDto.succeed, ChatDto.r_type, System.currentTimeMillis(), 0, datingId, "");

            ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingId, 0, "");

            setChatdtolist(chatDto);

        }

    }


    //对方拒绝接受礼物
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giveFiledWithRefused")
            }
    )
    public void giveFiledWithRefused(CommonEvent commonEvent) {

        loadingDiaog.dismiss();
        if (null != giveDailog && giveDailog.isShowing()) {
            giveDailog.dismiss();
        }
        GiveGiftRpBean dto = (GiveGiftRpBean) commonEvent.getEvent();
        if (dto != null) {
            DialogUtil.showDisCoverNetToast(this, dto.getMsg());
        }

    }

    //赠送礼物异常

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giveError")
            }
    )
    public void giveError(CommonEvent commonEvent) {

        loadingDiaog.dismiss();
        if (null != giveDailog && giveDailog.isShowing()) {
            giveDailog.dismiss();
        }

    }


    //购买苹果下单成功
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giftOrderInfo")
            }
    )
    public void giftOrderInfo(GiftOrderResp orderinfo) {
        if (null != buyPDailog) {

            buyPDailog.dismiss();

        }

        loadingDiaog.dismiss();

        if (null != orderinfo.getOrder()) {

            Bundle bundle = new Bundle();

            bundle.putString(YpSettings.ORDER_ID, orderinfo.getOrder().getOrderId());

            bundle.putString(YpSettings.ProductName, orderinfo.getOrder().getProduct().getProductName());

            bundle.putLong(YpSettings.PAY_COST, (orderinfo.getOrder().getTotalFee() / 100));

            bundle.putInt(YpSettings.PAY_TYPE, 4);

            ActivityUtil.jump(ChatActivity.this, UserAppleOrderPayActivity.class, bundle, 0, 100);
        } else {
            DialogUtil.showDisCoverNetToast(this, "购买出现异常，请稍后重试");
        }
    }


    //送礼物购买苹果快速下单失败--无此苹果商品
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giftOrderInfoWithNoApple")
            }
    )
    public void giftOrderInfoWithNoApple(GiftOrderResp oj) {

        if (null != buyPDailog) {

            buyPDailog.dismiss();
        }
        loadingDiaog.dismiss();

        Bundle appleBundle = new Bundle();

        appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);

        ActivityUtil.jump(ChatActivity.this, AppleListActivity.class, appleBundle, 0, 100);

    }

    //送礼物购买苹果快速下单失败
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("giftOrderInfoFiled")
            }
    )
    public void giftOrderInfoFiled(Object oj) {
        if (null != buyPDailog) {

            buyPDailog.dismiss();

        }
        loadingDiaog.dismiss();
        DialogUtil.showDisCoverNetToast(ChatActivity.this, "购买苹果出现异常");
    }

    //快速获取苹果支付成功
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("quicklyPaySuccessForChat")

            }
    )
    public void quicklyPaySuccessForChat(CommonEvent event) {

//        sendGiftMsg();

    }


    //送礼物存在拉黑关系
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("gifteReceiveWithBlock")
            }
    )
    public void gifteReceiveWithBlock(CommonEvent commonEvent) {


        String hintstr = (String) commonEvent.getEvent();

        if (!TextUtils.isEmpty(hintstr)) {

            HintMsg hintMsg = new HintMsg();
            hintMsg.setType(MessageType.Hint);
            hintMsg.setText(hintstr);

            String msgStr = JsonUtils.toJson(hintMsg);

            ImMessage message = new CustomMessage(hintstr, "");

            String msgid = message.getMessage().getMsgId();

            ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingId, 0, "");

            ChatUtils.saveMessageRecord(msgStr, targetUserId + "", ChatDto.succeed, ChatDto.r_type, System.currentTimeMillis(), 0, datingId, "");

            ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingId, 0, "");

            setChatdtolist(chatDto);
        }
    }


//    private void sendGiftMsg() {
//
//        GiftMsg msg = new GiftMsg();
//
//        msg.setDateid(datingId);
//        msg.setCounsel(0);
//        msg.setMask(0);
//        msg.setType(MessageType.Gift);
//
//        GiftInfoEntity dto = passHotsList.get(giftItemPostion);
//        msg.setGiftImg(dto.getImageUrl());
//        msg.setCharmValue(dto.getCharm());
//
//        long date = System.currentTimeMillis();
//        String msg_str = JsonUtils.toJson(msg);
//
//
//        ImMessage message = new CustomMessage(msg_str, "[礼物]");
//
//        String TIMMessageStr = JsonUtils.toJson(message.getMessage());
//
//        String msgid = message.getMessage().getMsgId();
//
//        ChatDto chatDto = new ChatDto(userid + "", targetUserId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
//        setChatdtolist(chatDto);
//        ChatUtils.SaveOrUpdateChatMsgToDB(targetUserId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
//        ChatUtils.saveMessageRecord(msg_str, targetUserId + "", ChatDto.sending, ChatDto.s_type, date, 0, datingId, TIMMessageStr);
//
//        mChatObserver.sendMessage(message.getMessage(), null);
//
//        reply = UserToUserWithDatingTable.replyed;
//
//        ChatUtils.saveOrUpdateIsReply(targetUserId + "", datingId, dating_info_str, meIsActive, reply, datingHandleStatus, publishDate_userId + "");
//        handleGiftReceive();
//
//    }


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
