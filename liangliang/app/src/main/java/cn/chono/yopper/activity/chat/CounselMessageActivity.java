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
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.base.SelectEntryActivity;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.CounselMessageAdapter;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageDto;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.im.imObserver.MessageListObserver;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;


/**
 * 咨询消息列表
 *
 * @author sam.sun
 */
public class CounselMessageActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;

    private View contextView;

    private ListView counsel_message_listview;// 消息列表

    private List<MessageDto> counsel_messagedtoList = new ArrayList<MessageDto>();

    private CounselMessageAdapter findMessageAdapter;

    private String mid;

    // 删除
    private Dialog deleteDialog;

    private ViewStub error_no_message_vs;

    private MessageListObserver mMessageListObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PushAgent.getInstance(this).onAppStart();

        mid = LoginUser.getInstance().getUserId() + "";
        RxBus.get().register(this);

        initComponent();
        mMessageListObserver = new MessageListObserver();
        getMessageDto();
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("咨询消息");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);


        this.getGoBackLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 1000);
                finish();
            }
        });


        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.act_notification_message, null);
        counsel_message_listview = (ListView) contextView.findViewById(R.id.strange_message_listview);

        findMessageAdapter = new CounselMessageAdapter(this);
        counsel_message_listview.setAdapter(findMessageAdapter);

        error_no_message_vs = (ViewStub) contextView.findViewById(R.id.notification_error_no_message_vs);


        counsel_message_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDto dto = counsel_messagedtoList.get(position);
                String jid = dto.getJid();
                showDeleteDialog(jid);
                return true;
            }
        });


        counsel_message_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MessageDto dto = counsel_messagedtoList.get(position);

                //进去咨询聊天室内：需要参数 咨询类型 订单id 咨询师id
                String jid = dto.getJid();

                int userID = Integer.valueOf(jid);

                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.USERID, userID);

                ActivityUtil.jump(CounselMessageActivity.this, ChatCounselActivity.class, bundle, 0, 100);


            }
        });


        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("咨询消息");
        MobclickAgent.onResume(this); // 统计时长
        getMessageDto();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("咨询消息");
        MobclickAgent.onPause(this); // 统计时长
    }


    private void getMessageDto() {
        try {

            counsel_message_listview.setVisibility(View.VISIBLE);
            error_no_message_vs.setVisibility(View.GONE);

            counsel_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("resource", " =", MessageDto.resource_counsel).orderBy("lasttime", true));


            if (counsel_messagedtoList != null && counsel_messagedtoList.size() > 0) {

                for (int i = 0; i < counsel_messagedtoList.size(); i++) {
                    MessageDto dto = counsel_messagedtoList.get(i);
                    String jid = dto.getJid();

                    long no_read_num = App.getInstance().db.count(Selector.from(ChatDto.class).where("mid", " =", mid).and("jid", " =", jid).and("status", "=", 0));

                    counsel_messagedtoList.get(i).setNo_read_num((int) no_read_num);
                }
            }


            if (counsel_messagedtoList != null && counsel_messagedtoList.size() > 0) {

                if (findMessageAdapter != null) {

                    findMessageAdapter.setList(counsel_messagedtoList);
                    //注释掉重复刷新
//                    findMessageAdapter.notifyDataSetChanged();
                } else {
                    findMessageAdapter = new CounselMessageAdapter(this);
                    findMessageAdapter.setList(counsel_messagedtoList);
                    counsel_message_listview.setAdapter(findMessageAdapter);
                }

            } else {

                error_no_message_vs.setVisibility(View.VISIBLE);

                counsel_message_listview.setVisibility(View.GONE);

                TextView error_no_message_tv = (TextView) this.findViewById(R.id.error_no_message_tv);
                error_no_message_tv.setText("暂时没有咨询消息");

            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    /**
     *
     *
     */

    public void showDeleteDialog(final String jid) {

        // 初始化一个自定义的Dialog
        deleteDialog = new MyDialog(CounselMessageActivity.this, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

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

                        CounselMessageActivity.this.deleteDialog.dismiss();

                        try {

                            App.getInstance().db.delete(MessageDto.class, WhereBuilder.b("jid", " =", jid).and("mid", " =", mid));

                            App.getInstance().db.delete(ChatDto.class, WhereBuilder.b("jid", " =", jid).and("mid", " =", mid));
                            //刷新
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
    public void onDestroy() {

        RxBus.get().post("refreshMessageList", new CommonEvent<>());

        RxBus.get().unregister(this);

        mMessageListObserver.stop();

        super.onDestroy();

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
                    @Tag("refreshCounselMessageList")
            }
    )

    public void refreshCounselMessageList(CommonEvent event) {

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
            CounselMessageActivity.this.getTvTitle().setText("咨询消息(未连接)");
        } else {
            CounselMessageActivity.this.getTvTitle().setText("咨询消息");
        }

    }


}
