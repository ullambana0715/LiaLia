package cn.chono.yopper.im.model;

import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;

import java.io.UnsupportedEncodingException;

import cn.chono.yopper.utils.CheckUtil;

/**
 * 文本消息数据
 */
public class TextMessage extends ImMessage {

    public TextMessage(TIMMessage message) {
        this.message = message;
    }


    public TextMessage(String s, String msg) {
        message = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText(s);
        message.addElement(elem);


        if (!CheckUtil.isEmpty(msg)) {

            try {
                byte[] srtbyte = msg.getBytes("UTF-8");

                TIMCustomElem customElem = new TIMCustomElem();
                customElem.setData(srtbyte);
                customElem.setDesc("");
                message.addElement(customElem);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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

        String msg = "收到一条消息";

        if (message != null) {
            for (int i = 0; i < message.getElementCount(); ++i) {
                switch (message.getElement(i).getType()) {

                    case Text:

                        TIMTextElem textElem = (TIMTextElem) message.getElement(i);
                        if (textElem != null) {
                            msg = textElem.getText();
                        }
                        break;
                }


            }
        }
        return msg;
    }

}
