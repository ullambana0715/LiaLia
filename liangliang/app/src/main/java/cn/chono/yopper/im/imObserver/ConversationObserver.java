package cn.chono.yopper.im.imObserver;

import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;

import java.util.Observable;
import java.util.Observer;

import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.im.imEvent.MessageEvent;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.SharedprefUtil;

/**
 * 会话界面逻辑
 */
public class ConversationObserver implements Observer {


    public ConversationObserver() {
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {

            TIMMessage msg = (TIMMessage) data;
            if (msg != null) {

                ChatDto dto = ChatUtils.ReceiveMsgFilter(msg);

                if (dto != null) {

                    TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C,dto.getJid());
                    conversation.setReadMessage();

                    ChatUtils.msgHintDeal(dto);


                    long pre_time = SharedprefUtil.getLong(ContextUtil.getContext(), YpSettings.chat_message_conversation_time, 0);
                    if (pre_time == 0) {

                        SharedprefUtil.saveLong(ContextUtil.getContext(), YpSettings.chat_message_conversation_time, System.currentTimeMillis());

                        RxBus.get().post("ReceiveNewMessage", new CommonEvent(dto));

                    } else {
                        long cur_time = System.currentTimeMillis();
                        if (cur_time - pre_time >= 5000) {
                            SharedprefUtil.saveLong(ContextUtil.getContext(), YpSettings.chat_message_conversation_time, cur_time);

                            RxBus.get().post("ReceiveNewMessage", new CommonEvent(dto));

                        }
                    }

                }
            }
        }
    }

    public void stop() {
        //注销消息监听
        MessageEvent.getInstance().deleteObserver(this);
    }
}
