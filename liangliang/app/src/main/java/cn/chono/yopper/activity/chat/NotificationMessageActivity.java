package cn.chono.yopper.activity.chat;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.orhanobut.logger.Logger;
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.appointment.DatingDetailActivity;
import cn.chono.yopper.activity.base.SelectEntryActivity;
import cn.chono.yopper.activity.near.BubblingInfoActivity;
import cn.chono.yopper.activity.video.VideoDetailGetActivity;
import cn.chono.yopper.adapter.NotificationMessageAdapter;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageDto;
import cn.chono.yopper.data.NotificationMsg;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.im.imObserver.MessageListObserver;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.ui.UserInfoEditActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;


/**
 * 通知消息列表
 *
 * @author sam.sun
 */
public class NotificationMessageActivity extends MainFrameActivity {

    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private ListView notification_message_listview;// 消息列表

    private ViewStub notification_error_no_message_vs;

    private List<ChatDto> notification_messagedtoList = new ArrayList<ChatDto>();

    private NotificationMessageAdapter notificationMessageAdapter;

    private String mid;

    // 删除
    private Dialog deleteDialog;

    private MessageListObserver mMessageListObserver;

    private String notification_title;
    private String jid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        RxBus.get().register(this);
        mid = LoginUser.getInstance().getUserId() + "";

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            notification_title = bundle.getString(YpSettings.NOTIFICATION_TITLE);
            jid = bundle.getString(YpSettings.MESSAGE_OTHER_JID);
        }

        initComponent();

        mMessageListObserver = new MessageListObserver();


    }


    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText(notification_title + "");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.VISIBLE);
        this.getBtnOption().setVisibility(View.GONE);
        this.gettvOption().setVisibility(View.VISIBLE);
        this.gettvOption().setText("忽略未读");

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 1000);
                finish();
            }
        });

        this.getOptionLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 1000);

                try {
                    List<ChatDto> chatdtoList = App.getInstance().db.findAll((Selector.from(ChatDto.class).where("jid", " =", jid)).and("mid", " =", mid));
                    if (chatdtoList != null && chatdtoList.size() > 0) {
                        for (int j = 0; j < chatdtoList.size(); j++) {
                            ChatDto chatdto = chatdtoList.get(j);
                            if (chatdto != null) {
                                String message = chatdto.getMessage();
                                NotificationMsg notificationMsg = JsonUtils.fromJson(message, NotificationMsg.class);
                                notificationMsg.setBadge(-1);
                                String chatmsg_str = JsonUtils.toJson(notificationMsg);
                                chatdto.setMessage(chatmsg_str);
                                chatdto.setStatus(ChatDto.readed_status);
                                App.getInstance().db.update(chatdto);
                            }
                        }
                    }

                } catch (DbException e1) {
                    e1.printStackTrace();
                }
                getMessageDto();

            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.act_notification_message, null);
        notification_message_listview = (ListView) contextView.findViewById(R.id.strange_message_listview);

        notification_error_no_message_vs = (ViewStub) contextView.findViewById(R.id.notification_error_no_message_vs);

        notificationMessageAdapter = new NotificationMessageAdapter(this);
        notification_message_listview.setAdapter(notificationMessageAdapter);


        notification_message_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                ChatDto dto = notification_messagedtoList.get(position);
//                String msgId = dto.getMsgId();
//                showDeleteDialog(msgId);
                return true;
            }
        });


        notification_message_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ChatDto dto = notification_messagedtoList.get(position);

                NotificationMsg notificationMsg = JsonUtils.fromJson(dto.getMessage(), NotificationMsg.class);

                if (notificationMsg != null) {
                    int type = notificationMsg.getNotifytype();
                    Bundle bundle = new Bundle();
                    if (type == 1 || type == 2 || type == 28) {
                        //去他人个人资料页
                        try {
                            String message = dto.getMessage();
                            notificationMsg.setBadge(-1);
                            String chatmsg_str = JsonUtils.toJson(notificationMsg);
                            dto.setMessage(chatmsg_str);
                            dto.setStatus(ChatDto.readed_status);
                            App.getInstance().db.update(dto);
                        } catch (DbException e1) {
                            e1.printStackTrace();
                        }
                        String testData = notificationMsg.getExtra().get("userId").toString();
                        double userid = Double.parseDouble(testData);
                        bundle.putInt(YpSettings.USERID, (int) userid);
                        ActivityUtil.jump(NotificationMessageActivity.this, UserInfoActivity.class, bundle, 0, 100);

                    } else if (type == 4 || type == 5) {

                        try {
                            String message = dto.getMessage();
                            notificationMsg.setBadge(-1);
                            String chatmsg_str = JsonUtils.toJson(notificationMsg);
                            dto.setMessage(chatmsg_str);
                            dto.setStatus(ChatDto.readed_status);
                            App.getInstance().db.update(dto);
                        } catch (DbException e1) {
                            e1.printStackTrace();
                        }

                        bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
                        //去视频过度页面
                        ActivityUtil.jump(NotificationMessageActivity.this, VideoDetailGetActivity.class, bundle, 0, 100);
                    } else if (type == 6 || type == 7) {
                        try {
                            String message = dto.getMessage();
                            notificationMsg.setBadge(-1);
                            String chatmsg_str = JsonUtils.toJson(notificationMsg);
                            dto.setMessage(chatmsg_str);
                            dto.setStatus(ChatDto.readed_status);
                            App.getInstance().db.update(dto);
                        } catch (DbException e1) {
                            e1.printStackTrace();
                        }

                        //去冒泡详情
                        String str = notificationMsg.getExtra().get("bubbleId").toString();
                        double bubbleId = Double.parseDouble(str);
                        int b_id = (int) bubbleId;
                        bundle.putString(YpSettings.BUBBLING_LIST_ID, b_id + "");
                        ActivityUtil.jump(NotificationMessageActivity.this, BubblingInfoActivity.class, bundle, 0, 100);
                    } else if (type == 3) {
                        try {
                            String message = dto.getMessage();
                            notificationMsg.setBadge(-1);
                            String chatmsg_str = JsonUtils.toJson(notificationMsg);
                            dto.setMessage(chatmsg_str);
                            dto.setStatus(ChatDto.readed_status);
                            App.getInstance().db.update(dto);
                        } catch (DbException e1) {
                            e1.printStackTrace();
                        }

                        //去个人资料编辑页
                        bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());

                        ActivityUtil.jump(NotificationMessageActivity.this, UserInfoActivity.class, bundle, 0, 100);

                    } else if (type == 24 || type == 20 || type == 21 || type == 22 || type == 23 || type == 25) {
                        try {
                            String message = dto.getMessage();
                            notificationMsg.setBadge(-1);
                            String chatmsg_str = JsonUtils.toJson(notificationMsg);
                            dto.setMessage(chatmsg_str);
                            dto.setStatus(ChatDto.readed_status);
                            App.getInstance().db.update(dto);
                        } catch (DbException e1) {
                            e1.printStackTrace();
                        }
                        String str = notificationMsg.getExtra().get("appointmentId").toString();

                        bundle.putString(YpSettings.DATINGS_ID, str);
                        ActivityUtil.jump(NotificationMessageActivity.this, DatingDetailActivity.class, bundle, 0, 100);
                    }
                }
            }
        });


        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


    }


    /**
     *
     *
     */

    public void showDeleteDialog(final String msgId) {

        // 初始化一个自定义的Dialog
        deleteDialog = new MyDialog(NotificationMessageActivity.this, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

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

                // 点击升级按钮
                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        NotificationMessageActivity.this.deleteDialog.dismiss();

                        try {

                            App.getInstance().db.delete(ChatDto.class, WhereBuilder.b("msgId", " =", msgId).and("mid", " =", mid));

                            App.getInstance().db.delete(MessageDto.class, WhereBuilder.b("msgId", " =", msgId).and("mid", " =", mid));

                            getMessageDto();

                        } catch (DbException e) {
                            e.printStackTrace();

                        }
                    }

                });
            }
        });
        deleteDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
        deleteDialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("消息通知列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        getMessageDto();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("消息通知列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    private void getMessageDto() {
        try {

            notification_message_listview.setVisibility(View.VISIBLE);


            notification_messagedtoList = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("mid", " =", mid).and("jid", " =", jid).orderBy("date", true));

            if (notification_messagedtoList != null && notification_messagedtoList.size() > 0) {

                if (notificationMessageAdapter != null) {

                    notificationMessageAdapter.setList(notification_messagedtoList);
                    //去掉重复刷新适配器
//                    notificationMessageAdapter.notifyDataSetChanged();
                } else {
                    notificationMessageAdapter = new NotificationMessageAdapter(this);
                    notificationMessageAdapter.setList(notification_messagedtoList);
                    notification_message_listview.setAdapter(notificationMessageAdapter);
                }

                notification_error_no_message_vs.setVisibility(View.GONE);

            } else {

                notification_message_listview.setVisibility(View.GONE);

                notification_error_no_message_vs.setVisibility(View.VISIBLE);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
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


    @Override
    protected void onDestroy() {

        RxBus.get().post("refreshMessageList", new CommonEvent<>());

        RxBus.get().unregister(this);
        mMessageListObserver.stop();
        super.onDestroy();
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
            this.getTvTitle().setText(notification_title + "(未连接)");
        } else {
            this.getTvTitle().setText(notification_title);
        }

    }

}
