package cn.chono.yopper.im.model;

import android.text.TextUtils;

import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;

import java.io.UnsupportedEncodingException;

/**
 * 自定义消息
 */
public class CustomMessage extends ImMessage {

    public CustomMessage(TIMMessage message) {
        this.message = message;
    }

    /**
     * 自定义消息构造函数
     *
     * @param msg
     */
    public CustomMessage(String msg, String desc) {

        message = new TIMMessage();

        try {
            byte[] srtbyte = msg.getBytes("UTF-8");

            TIMCustomElem textElem = new TIMCustomElem();
            textElem.setData(srtbyte);
            textElem.setDesc(desc);
            message.addElement(textElem);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void save() {

    }

    @Override
    public String getSummary() {

        String msg = "";

        if (message != null) {
            for (int i = 0; i < message.getElementCount(); ++i) {
                switch (message.getElement(i).getType()) {


                    case Custom:

                        TIMCustomElem customElem = (TIMCustomElem) message.getElement(i);

                        if (customElem != null) {
                            msg = customElem.getDesc();
                        }

                        break;
                }


            }
        }


        return msg;
    }

}
