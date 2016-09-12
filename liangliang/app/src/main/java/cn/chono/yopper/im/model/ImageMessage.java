package cn.chono.yopper.im.model;

import com.tencent.TIMCustomElem;
import com.tencent.TIMImageElem;
import com.tencent.TIMMessage;

import java.io.UnsupportedEncodingException;

/**
 * 图片消息数据
 */
public class ImageMessage extends ImMessage {

    public ImageMessage(TIMMessage message) {
        this.message = message;
    }

    /**
     * 图片消息构造函数
     *
     * @param path  图片路径
     * @param isOri 是否原图发送
     */
    public ImageMessage(String path, boolean isOri, String customStr) {
        message = new TIMMessage();
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(path);
        elem.setLevel(isOri ? 0 : 1);
        message.addElement(elem);


        try {
            byte[] srtbyte = customStr.getBytes("UTF-8");

            TIMCustomElem customElem = new TIMCustomElem();
            customElem.setData(srtbyte);
            customElem.setDesc("图片属性");
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
        return "[图片]";
    }


}
