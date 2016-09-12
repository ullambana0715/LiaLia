package cn.chono.yopper.im.model;


import com.tencent.TIMMessage;
import com.tencent.TIMSNSChangeInfo;
import com.tencent.TIMSNSSystemElem;
import com.tencent.TIMSNSSystemType;

import java.util.List;

import cn.chono.yopper.utils.ChatUtils;

/**
 * 消息工厂
 */
public class MessageFactory {

    private MessageFactory() {
    }


    /**
     * 消息工厂方法
     */
    public static ImMessage getMessage(TIMMessage message) {

//        TIMSNSSystemElem


        switch (message.getElement(0).getType()) {

            case Text:
            case Face:
                return new TextMessage(message);
            case Image:
                return new ImageMessage(message);
            case Sound:
                return new VoiceMessage(message);
            case Custom:
                return new CustomMessage(message);

            case SNSTips:

                TIMSNSSystemElem elem = (TIMSNSSystemElem) message.getElement(0);
                if (elem == null) {
                    return null;
                }
                TIMSNSSystemType type = elem.getSubType();
                if (type == null) {
                    return null;
                }

                List<TIMSNSChangeInfo> list = elem.getChangeInfoList();

                if (list != null && list.size() > 0) {

                    TIMSNSChangeInfo info = list.get(0);

                    int tiptype = type.getValue();

                    if (tiptype == 5) {

                        String userid = message.getSender();

                        if (userid.contains("@test")) {
                            userid = userid.replace("@test", "");
                        }

                        String jid = info.getIdentifier();

                        if (jid.contains("@test")) {
                            jid = jid.replace("@test", "");
                        }

                        ChatUtils.saveBlockHint(userid, jid,message.getMsgId());
                    }

                }
                return null;

            default:
                return null;
        }
    }


}
