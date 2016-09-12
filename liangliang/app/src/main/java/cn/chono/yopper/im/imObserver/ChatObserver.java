package cn.chono.yopper.im.imObserver;

import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import java.util.Observable;
import java.util.Observer;

import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AudioMsg;
import cn.chono.yopper.data.ImgMsg;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.SendMsgStatusEvent;
import cn.chono.yopper.im.imEvent.MessageEvent;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.MessageFactory;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SharedprefUtil;

/**
 * 聊天界面逻辑
 */
public class ChatObserver implements Observer {

    private TIMConversation conversation;

    public ChatObserver(String identify, TIMConversationType type) {

        if (YpSettings.isTest) {
            conversation = TIMManager.getInstance().getConversation(type, identify + "@test");
        } else {
            conversation = TIMManager.getInstance().getConversation(type, identify);
        }

        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
    }

    /**
     * 中止页面逻辑
     */
    public void stop() {
        //注销消息监听
        MessageEvent.getInstance().deleteObserver(this);
    }

    /**
     * 获取聊天TIM会话
     */
    public TIMConversation getConversation() {
        return conversation;
    }

    /**
     * 中止页面逻辑
     *
     * @param message 发送的消息
     */
    public void sendMessage(final TIMMessage message, String msgid) {

        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                if (!TextUtils.isEmpty(msgid)) {
                    RxBus.get().post("onSendMessageStatus", new SendMsgStatusEvent(ChatDto.no_succeed, msgid, null));
                } else {
                    RxBus.get().post("onSendMessageStatus", new SendMsgStatusEvent(ChatDto.no_succeed, message.getMsgId(), null));
                }

            }

            @Override
            public void onSuccess(TIMMessage msg) {
                //发送消息成功,消息状态已在sdk中修改，此时只需更新界面

                TIMSoundElem soundElem = null;

                switch (msg.getElement(0).getType()) {

                    case Sound:

                        soundElem = (TIMSoundElem) msg.getElement(0);

                        Logger.e("uuid=" + soundElem.getUuid());

                        Logger.e("adadadad");

                        break;
                }


                if (!TextUtils.isEmpty(msgid)) {
                    RxBus.get().post("onSendMessageStatus", new SendMsgStatusEvent(ChatDto.succeed, msgid, soundElem));
                } else {
                    RxBus.get().post("onSendMessageStatus", new SendMsgStatusEvent(ChatDto.succeed, message.getMsgId(), soundElem));
                }


            }
        });

//        //message对象为发送中状态
//        MessageEvent.getInstance().onNewMessage(message);

    }


    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {

            //当前聊天界面已读上报，用于多终端登录时未读消息数同步
            readMessages();

            TIMMessage msg = (TIMMessage) data;
            Logger.e("消息接收到＝" + msg.toString());
            if (msg == null || msg.getConversation().getPeer().equals(conversation.getPeer()) && msg.getConversation().getType() == conversation.getType()) {

                ChatDto dto = ChatUtils.ReceiveMsgFilter(msg);
                if (dto != null) {

                    ChatUtils.msgHintDeal(dto);

                    RxBus.get().post("onChatReceiveMsg", new CommonEvent(dto));


                }

            }
        }
    }


    /**
     * 设置会话为已读
     */
    public void readMessages() {
        conversation.setReadMessage();
    }


}
