package cn.chono.yopper.activity.chat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.base.SelectEntryActivity;
import cn.chono.yopper.adapter.ChatSystemAdapter;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.im.imObserver.ChatObserver;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.MediaUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.DragListView;

@SuppressLint("HandlerLeak")
public class ChatSystemActivity extends MainFrameActivity implements DragListView.OnRefreshLoadingMoreListener {


    // 消息列表
    private DragListView messageListView;


    private LinearLayout chat_goback_layout;

    private TextView chat_title_tv;


    private List<ChatDto> recoverlist;

    private ChatSystemAdapter messageAdapter;

    // 一页10条数据
    private int pagesize = 10;
    // 页数
    private static int pagecount = 0;

    private static int cur_pagecount = 1;
    // 页面数据余数
    private int remainder_count = 0;

    private static List<ChatDto> chatdtolist;
    private Handler mhandler;

    private String mid;

    private int targetUserId;// 当前聊友的id

    private String jid = "";

    private int userid;

    private ChatObserver mChatObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_system);
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

        }

        initComponent();

        jid = targetUserId + "";
        mhandler = new Handler();

        ChatUtils.setSystemChatRecordReaded(mid, jid);

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
        chat_title_tv.setText("俩俩");

        chat_goback_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                hideSoftInputView();
                finish();
            }
        });

        messageListView = (DragListView) this.findViewById(R.id.chat_msg_listView);

        messageListView.setOnRefreshListener(this);
        messageListView.setPullLoadEnable(false);

        messageListView.setDividerHeight(0);

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


    @Override
    protected void onDestroy() {
        if (mhandler != null) {
            mhandler.removeCallbacksAndMessages(null);
        }
        mChatObserver.stop();
        ChatUtils.setSystemChatRecordReaded(mid, jid);
        RxBus.get().post("refreshMessageList", new CommonEvent<>());
        RxBus.get().unregister(this);
        super.onDestroy();
    }


    private void initChatData() {
        try {
            cur_pagecount = 1;
            recoverlist = new ArrayList<ChatDto>();
            recoverlist = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).orderBy("date"));
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
                    messageAdapter = new ChatSystemAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
                    messageListView.setAdapter(messageAdapter);
                    messageListView.setSelection(chatdtolist.size() - 1);

                } else {
                    chatdtolist = recoverlist;
                    // bu允许下拉
                    // messageListView.R

                    messageAdapter = new ChatSystemAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
                    messageListView.setAdapter(messageAdapter);
                    messageListView.setSelection(chatdtolist.size() - 1);
                }

            } else {
                messageAdapter = new ChatSystemAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
                messageListView.setAdapter(messageAdapter);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("onChatReceiveMsg")

            }
    )
    public void onChatReceiveMsg(CommonEvent event) {

        ChatDto dto = (ChatDto) event.getEvent();

        if (dto == null) {

            return;
        }

        if (!dto.getJid().equals(jid)) {

            return;
        }

        if (chatdtolist == null) {
            chatdtolist = new ArrayList<ChatDto>();
        }

        try {
            dto.setStatus(ChatDto.readed_status);
            App.getInstance().db.update(dto);

        } catch (DbException e) {
            e.printStackTrace();
        }

        chatdtolist.add(dto);

        if (messageAdapter == null) {
            messageAdapter = new ChatSystemAdapter(this, chatdtolist, mid, userid, jid, targetUserId);
            messageListView.setAdapter(messageAdapter);
            messageListView.setSelection(messageAdapter.getCount() - 1);
        }
        messageAdapter.setList(chatdtolist);

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
            ChatSystemActivity.this.getTvTitle().setText("未连接");
        } else {
            ChatSystemActivity.this.getTvTitle().setText("俩俩");
        }

    }

}
