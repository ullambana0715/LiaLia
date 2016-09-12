package cn.chono.yopper.activity.chat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.TIMConversationType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ChatReadMsg.ChatReadMsgBean;
import cn.chono.yopper.Service.Http.ChatReadMsg.ChatReadMsgService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.adapter.MessageAdapter;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AttributeDto;
import cn.chono.yopper.data.KeyTable;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageDto;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.NotificationMsg;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserToUserWithDatingTable;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.im.imObserver.ChatObserver;
import cn.chono.yopper.im.imObserver.MessageListObserver;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.TextMessage;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;

/**
 * 消息列表
 *
 * @author sam.sun
 */
public class MessageListActivity extends MainFrameActivity {


    private LayoutInflater mInflater;

    private TextView message_dating_a_key_deal_tv;//一键处理按钮

    private LinearLayout message_dating_a_key_deal_layout;//一键处理布局

    private ListView message_listview;// 消息列表

    private ViewStub error_no_message_vs;

    private List<MessageDto> system_messagedtoList = new ArrayList<MessageDto>();

    private List<MessageDto> user_messagedtoList = new ArrayList<MessageDto>();

    private List<MessageDto> no_handle_messagedtoList = new ArrayList<MessageDto>();

    private List<MessageDto> counsel_messagedtoList = new ArrayList<MessageDto>();

    private MessageAdapter messageAdapter;

    private String mid;

    // 删除
    private Dialog deleteDialog;

    private TextView title_tv;

    private ChatObserver mChatObserver;

    private MessageListObserver mMessageListObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        PushAgent.getInstance(this).onAppStart();

        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setContentLayout(R.layout.message_activity);

        RxBus.get().register(this);

        mid = LoginUser.getInstance().getUserId() + "";


        initComponent();

        getMessageDto();
        mMessageListObserver = new MessageListObserver();
    }


    private void initComponent() {

        title_tv = this.getTvTitle();

        title_tv.setText("消息");

        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getBtnGoBack().setVisibility(View.VISIBLE);


        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });

        message_dating_a_key_deal_layout = (LinearLayout) this.findViewById(R.id.message_dating_a_key_deal_layout);
        message_dating_a_key_deal_tv = (TextView) this.findViewById(R.id.message_dating_a_key_deal_tv);

        //邀约一键处理
        message_dating_a_key_deal_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);

                if (no_handle_messagedtoList != null && no_handle_messagedtoList.size() >= 5) {
                    List<String> list = new ArrayList<String>();
                    list.add("我再考虑一下吧");

                    list.add("黑凤梨，黑凤梨");

                    list.add("你是什么星座呀");

                    list.add("你喜欢咖啡、奶茶还是果汁");

                    list.add("等一等吧，我太忙啦");

                    list.add("你喜欢登山、滑翔、蹦极吗");

                    list.add("你是不是等得花都谢了");
                    list.add("你喜欢幽默搞笑还是惊悚恐怖的电影呀");

                    repondDialog = createRepondDialog(MessageListActivity.this, list);

                    if (!isFinishing()) {
                        repondDialog.show();
                    }
                }

            }
        });

        message_listview = (ListView) this.findViewById(R.id.message_listview);

        error_no_message_vs = (ViewStub) this.findViewById(R.id.error_no_message_vs);

        messageAdapter = new MessageAdapter(this);
        message_listview.setAdapter(messageAdapter);


        message_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDto dto = user_messagedtoList.get(position);
                showDeleteDialog(dto);
                return true;
            }
        });


        message_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MessageDto dto = user_messagedtoList.get(position);
                String type = ChatUtils.getMsgType(dto.getLastmessage());
                if (TextUtils.equals(type, MessageType.Notification)) {
                    //邀请通知
                    Bundle bundle = new Bundle();
                    // 跳转到邀请通知列表界面
                    NotificationMsg notificationMsg = JsonUtils.fromJson(dto.getLastmessage(), NotificationMsg.class);
                    if (notificationMsg.getNotifytype() == 8) {
                        //跳每日星运 跳转到web页面

                        String jid = dto.getJid();

                        try {

                            ChatDto chatDto = App.getInstance().db.findFirst((Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid)));
                            notificationMsg.setBadge(-1);
                            String chatmsg_str = JsonUtils.toJson(notificationMsg);
                            chatDto.setMessage(chatmsg_str);
                            chatDto.setStatus(ChatDto.readed_status);
                            App.getInstance().db.update(chatDto);

                        } catch (DbException e1) {
                            e1.printStackTrace();
                        }

                        UserDto mydto = DbHelperUtils.getDbUserInfo(LoginUser.getInstance().getUserId());
                        int myhor = mydto.getProfile().getHoroscope();

                        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Constellation/Luck?id=" + myhor + "&userid=" + LoginUser.getInstance().getUserId() + "&AuthToken=" + LoginUser.getInstance().getAuthToken());
                        bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);
                        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "每日星运");
                        ActivityUtil.jump(MessageListActivity.this, SimpleWebViewActivity.class, bundle, 0, 100);


                    } else {
                        String from = notificationMsg.getFrom();

                        bundle.putString(YpSettings.NOTIFICATION_TITLE, from);
                        bundle.putString(YpSettings.MESSAGE_OTHER_JID, dto.getJid());

                        ActivityUtil.jump(MessageListActivity.this, NotificationMessageActivity.class, bundle, 0, 100);
                    }


                } else {

                    if (dto.getResource() == MessageDto.resource_counsel) {
                        //咨询师消息列表

                        ActivityUtil.jump(MessageListActivity.this, CounselMessageActivity.class, null, 0, 100);

                    } else {
                        Bundle bundle = new Bundle();
                        String jid = dto.getJid();

                        String datingId = dto.getDatingId();

                        int userID = Integer.valueOf(jid);

                        bundle.putInt(YpSettings.USERID, userID);

                        if (YpSettings.isTest) {
                            if (userID == 129319) {
                                ActivityUtil.jump(MessageListActivity.this, ChatSystemActivity.class, bundle, 0, 100);
                            } else {
                                bundle.putString(YpSettings.DATINGS_ID, datingId);
                                ActivityUtil.jump(MessageListActivity.this, ChatActivity.class, bundle, 0, 100);
                            }
                        } else {
                            if (userID == 103835) {
                                ActivityUtil.jump(MessageListActivity.this, ChatSystemActivity.class, bundle, 0, 100);
                            } else {
                                bundle.putString(YpSettings.DATINGS_ID, datingId);
                                ActivityUtil.jump(MessageListActivity.this, ChatActivity.class, bundle, 0, 100);
                            }
                        }

                    }


                }


            }
        });


    }


    @Override
    public void onDestroy() {

        RxBus.get().post("refreshMessageNum", new CommonEvent<>());

        RxBus.get().unregister(this);

        if (mChatObserver != null) {
            mChatObserver.stop();
        }

        if (mMessageListObserver != null) {
            mMessageListObserver.stop();
        }

        super.onDestroy();

    }

    /**
     *
     *
     */

    public void showDeleteDialog(final MessageDto dto) {

        // 初始化一个自定义的Dialog
        deleteDialog = new MyDialog(this, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                select_operate_dialog_title_tv.setText("操作");
                select_operate_dialog_one_tv.setText("删除对话");

                select_operate_dialog_one_layout.setVisibility(View.VISIBLE);

                select_operate_dialog_two_layout.setVisibility(View.GONE);

                select_operate_dialog_three_layout.setVisibility(View.GONE);

                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        deleteDialog.dismiss();
                        String jid = dto.getJid();
                        LogUtils.e("jid=" + jid);
                        LogUtils.e("getResource=" + dto.getResource());
                        ChatUtils.DeleteChatRecord(jid, dto.getDatingId(), dto.getResource());

                        getMessageDto();

                        RxBus.get().post("ReceiveNewMessage", new CommonEvent());

                        RxBus.get().post("refreshMessageList", new CommonEvent());

                    }

                });
            }
        });
        deleteDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        deleteDialog.show();

    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("消息列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("消息列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长


    }


    private void getMessageDto() {

        try {

            user_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("resource", " =", MessageDto.resource_user).orderBy("lasttime", true));

            system_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("resource", " =", MessageDto.resource_system).orderBy("lasttime", true));

            counsel_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("resource", " =", MessageDto.resource_counsel).orderBy("lasttime"));

            if (user_messagedtoList != null && user_messagedtoList.size() > 0) {

                for (int i = 0; i < user_messagedtoList.size(); i++) {
                    MessageDto dto = user_messagedtoList.get(i);
                    String jid = dto.getJid();
                    String datingId = dto.getDatingId();

                    String message = dto.getLastmessage();
                    String type = ChatUtils.getMsgType(message);
                    long no_read_num = 0;
                    if (TextUtils.equals(type, MessageType.Notification)) {
                        List<ChatDto> chatdtolist = App.getInstance().db.findAll((Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).and("datingId", " =", datingId).and("status", " =", 0)));
                        if (chatdtolist != null && chatdtolist.size() > 0) {
                            for (int j = 0; j < chatdtolist.size(); j++) {
                                ChatDto chatDto = chatdtolist.get(j);
                                NotificationMsg notificationMsg = JsonUtils.fromJson(chatDto.getMessage(), NotificationMsg.class);
                                int badge = notificationMsg.getBadge();
                                if (badge >= 0) {
                                    if (badge == 0) {
                                        badge = 1;
                                    }
                                    no_read_num = no_read_num + badge;
                                }
                            }
                        }

                    } else {
                        no_read_num = App.getInstance().db.count(Selector.from(ChatDto.class).where("mid", " =", mid).and("jid", " =", jid).and("datingId", " =", datingId).and("status", "=", 0));
                    }
                    user_messagedtoList.get(i).setNo_read_num((int) no_read_num);
                }
            }


            if (system_messagedtoList != null && system_messagedtoList.size() > 0) {

                for (int i = 0; i < system_messagedtoList.size(); i++) {
                    MessageDto dto = system_messagedtoList.get(i);
                    String jid = dto.getJid();
                    String datingId = dto.getDatingId();

                    String message = dto.getLastmessage();
                    String type = ChatUtils.getMsgType(message);
                    long no_read_num = 0;
                    if (TextUtils.equals(type, MessageType.Notification)) {
                        List<ChatDto> chatdtolist = App.getInstance().db.findAll((Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).and("status", " =", 0)));
                        if (chatdtolist != null && chatdtolist.size() > 0) {
                            for (int j = 0; j < chatdtolist.size(); j++) {
                                ChatDto chatDto = chatdtolist.get(j);
                                NotificationMsg notificationMsg = JsonUtils.fromJson(chatDto.getMessage(), NotificationMsg.class);
                                int badge = notificationMsg.getBadge();
                                if (badge >= 0) {
                                    if (badge == 0) {
                                        badge = 1;
                                    }
                                    no_read_num = no_read_num + badge;
                                }
                            }
                        }

                    } else {
                        no_read_num = App.getInstance().db.count(Selector.from(ChatDto.class).where("mid", " =", mid).and("jid", " =", jid).and("status", "=", 0));
                    }
                    system_messagedtoList.get(i).setNo_read_num((int) no_read_num);

                    user_messagedtoList = ChatUtils.insertDto(dto, user_messagedtoList);
                }

            }


            if (counsel_messagedtoList != null && counsel_messagedtoList.size() > 0) {

                MessageDto dto = counsel_messagedtoList.get(counsel_messagedtoList.size() - 1);
                long no_read_num = 0;
                for (int i = 0; i < counsel_messagedtoList.size(); i++) {
                    MessageDto dtos = counsel_messagedtoList.get(i);
                    String jid = dtos.getJid();

                    String message = dtos.getLastmessage();
                    String type = ChatUtils.getMsgType(message);

                    if (TextUtils.equals(type, MessageType.Notification)) {
                        List<ChatDto> chatdtolist = App.getInstance().db.findAll((Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).and("counsel", " =", "1").and("status", " =", 0)));
                        if (chatdtolist != null && chatdtolist.size() > 0) {
                            for (int j = 0; j < chatdtolist.size(); j++) {
                                ChatDto chatDto = chatdtolist.get(j);
                                NotificationMsg notificationMsg = JsonUtils.fromJson(chatDto.getMessage(), NotificationMsg.class);
                                int badge = notificationMsg.getBadge();
                                if (badge >= 0) {
                                    if (badge == 0) {
                                        badge = 1;
                                    }
                                    no_read_num = no_read_num + badge;
                                }
                            }
                        }

                    } else {
                        no_read_num = App.getInstance().db.count(Selector.from(ChatDto.class).where("mid", " =", mid).and("jid", " =", jid).and("counsel", " =", "1").and("status", "=", 0));
                    }
                }

                dto.setNo_read_num((int) no_read_num);
                user_messagedtoList = ChatUtils.insertDto(dto, user_messagedtoList);
            }


            if (user_messagedtoList != null && user_messagedtoList.size() > 0) {

                // 给其塞未读消息条数

                message_listview.setVisibility(View.VISIBLE);
                error_no_message_vs.setVisibility(View.GONE);


                if (messageAdapter != null) {
                    messageAdapter.setList(user_messagedtoList);

                } else {

                    messageAdapter = new MessageAdapter(MessageListActivity.this, user_messagedtoList);
                    message_listview.setAdapter(messageAdapter);
                    messageAdapter.setList(user_messagedtoList);

                }

            } else {
                message_listview.setVisibility(View.GONE);
                error_no_message_vs.setVisibility(View.VISIBLE);

            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        no_handle_messagedtoList=new ArrayList<MessageDto>();

        no_handle_messagedtoList = getNoHanleList();

        if (no_handle_messagedtoList != null && no_handle_messagedtoList.size() >= 5) {
            message_dating_a_key_deal_layout.setVisibility(View.VISIBLE);
        } else {
            message_dating_a_key_deal_layout.setVisibility(View.GONE);
        }

    }


    private List<MessageDto> getNoHanleList() {

        List<MessageDto> onelist = new ArrayList<MessageDto>();

        List<MessageDto> twolist = new ArrayList<MessageDto>();

        try {

            List<MessageDto> no_deal_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("resource", " =", MessageDto.resource_user).orderBy("lasttime", true));


            if (no_deal_messagedtoList != null && no_deal_messagedtoList.size() > 0) {


                for (int i = 0; i < no_deal_messagedtoList.size(); i++) {
                    MessageDto dto = no_deal_messagedtoList.get(i);
                    String jid = dto.getJid();
                    KeyTable keyTable = ChatUtils.getKeyRecord(mid, jid);
                    if (keyTable != null && keyTable.getIsBrokenKey() == KeyTable.no_broken) {
                        onelist.add(dto);
                    } else {
                        if (keyTable == null) {
                            onelist.add(dto);
                        }
                    }
                }
            }


            if (onelist != null && onelist.size() > 0) {

                for (int i = 0; i < onelist.size(); i++) {
                    MessageDto dto = onelist.get(i);
                    String jid = dto.getJid();
                    UserToUserWithDatingTable datingdto = ChatUtils.getDatingTable(mid, jid, dto.getDatingId());
                    if (datingdto != null && datingdto.getIsReply() == UserToUserWithDatingTable.no_reply) {
                        twolist.add(dto);
                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        return twolist;
    }


    /**
     * 未读消息
     */
    private void postChatReadMsg(String fromuser, String targetUserId, String datingId) {

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


    private Dialog repondDialog;

    /**
     * 一键处理 回复用语选择
     *
     * @param context
     * @return
     */
    public Dialog createRepondDialog(Context context, List<String> list) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.dlg_key_process_respond, null);

        final Dialog dialog = new Dialog(context, R.style.dialog_BOT_style);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);

        // 获得按钮
        LinearLayout key_deal_respond_layout = (LinearLayout) view.findViewById(R.id.key_deal_respond_layout);


        if (list != null && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                View datingView = initInviteView(list.get(i), i, list.size());
                key_deal_respond_layout.addView(datingView);
            }
        }

        return dialog;

    }

    private View initInviteView(final String respondStr, int position, int length) {

        View view = mInflater.inflate(R.layout.item_respond_text, null);

        View respond_line = view.findViewById(R.id.respond_line);

        TextView respond_tv = (TextView) view.findViewById(R.id.respond_tv);


        if (position + 1 == length) {
            respond_line.setVisibility(View.GONE);
        }


        respond_tv.setText(respondStr);


        respond_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                repondDialog.dismiss();

                for (int i = 0; i < no_handle_messagedtoList.size(); i++) {


                    MessageDto dto = no_handle_messagedtoList.get(i);
                    String jid = dto.getJid();
                    String mid = dto.getMid();


                    mChatObserver = new ChatObserver(jid, TIMConversationType.C2C);

                    postChatReadMsg(mid, jid, dto.getDatingId());

                    ChatUtils.setUserChatRecordReaded(mid, jid, dto.getDatingId());

                    TextMsg textmsg = new TextMsg(MessageType.Text, respondStr, 0, dto.getDatingId(), 1);

                    long date = System.currentTimeMillis();

                    String msg_Str = JsonUtils.toJson(textmsg);


                    AttributeDto attributeDto = new AttributeDto();
                    attributeDto.setMask(1);
                    attributeDto.setDateid(dto.getDatingId());
                    attributeDto.setCounsel(0);
                    attributeDto.setType(MessageType.Text);
                    attributeDto.setLockText(respondStr);

                    String attributeDto_str = JsonUtils.toJson(attributeDto);


                    ImMessage message = new TextMessage("您有一条未解锁的消息", attributeDto_str);

                    String TIMMessageStr = JsonUtils.toJson(message.getMessage());

                    String msgid = message.getMessage().getMsgId();


                    ChatUtils.SaveOrUpdateChatMsgToDB(jid, msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.succeed, dto.getDatingId(), 0, TIMMessageStr);

                    ChatUtils.saveMessageRecord(msg_Str, jid, ChatDto.sending, ChatDto.s_type, date, 0, dto.getDatingId(), TIMMessageStr);

                    UserToUserWithDatingTable datingTable = ChatUtils.getDatingTable(mid, jid, dto.getDatingId());

                    if (datingTable != null) {
                        ChatUtils.saveOrUpdateIsReply(jid, dto.getDatingId(), datingTable.getDatingTheme(), UserToUserWithDatingTable.meActive, UserToUserWithDatingTable.replyed, datingTable.getDatingDealStatus(), datingTable.getPublishDate_userId());
                    }

                    // 发送消息
                    mChatObserver.sendMessage(message.getMessage(),null);

                }

                no_handle_messagedtoList.clear();

                message_dating_a_key_deal_layout.setVisibility(View.GONE);

                getMessageDto();

            }
        });

        return view;
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("ReceiveMessageUpdateList")
            }
    )

    public void ReceiveMessageUpdateList(CommonEvent event) {

        getMessageDto();

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("refreshMessageList")
            }
    )

    public void refreshMessageList(CommonEvent event) {

        getMessageDto();

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
            this.getTvTitle().setText("消息(未连接)");
        } else {
            this.getTvTitle().setText("消息");
        }

    }

}
