package cn.chono.yopper.event;

import com.tencent.TIMSoundElem;

/**
 * Created by sunquan on 16/7/19.
 */
public class SendMsgStatusEvent {

    private int sendStatus;

    private String msgId;


    private TIMSoundElem soundElem;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public TIMSoundElem getSoundElem() {
        return soundElem;
    }

    public void setSoundElem(TIMSoundElem soundElem) {
        this.soundElem = soundElem;
    }

    public SendMsgStatusEvent(int sendStatus, String msgId, TIMSoundElem soundElem) {
        this.sendStatus = sendStatus;
        this.msgId = msgId;
        this.soundElem = soundElem;
    }
}
