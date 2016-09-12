package cn.chono.yopper.im.model;

import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;

import java.io.UnsupportedEncodingException;

/**
 * 语音消息数据
 */
public class VoiceMessage extends ImMessage {

    public VoiceMessage(TIMMessage message){
        this.message = message;
    }


    /**
     * 语音消息构造方法
     */
    public VoiceMessage(TIMSoundElem elem,String customStr){
        message = new TIMMessage();
        message.addElement(elem);

        try {
            byte[] srtbyte = customStr.getBytes("UTF-8");

            TIMCustomElem customElem = new TIMCustomElem();
            customElem.setData(srtbyte);
            customElem.setDesc("");
            message.addElement(customElem);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    @Override
    public String getSummary() {
        return "[语音]";
    }

}
