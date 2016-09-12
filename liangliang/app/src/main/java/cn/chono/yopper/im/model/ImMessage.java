package cn.chono.yopper.im.model;

import com.tencent.TIMMessage;

/**
 * 消息数据基类
 */
public abstract class ImMessage {

    TIMMessage message;

    public TIMMessage getMessage() {
        return message;
    }

    /**
     * 保存消息或消息文件
     *
     */
    public abstract void save();


    /**
     * 删除消息
     *
     */
    public void remove(){
        if (message != null){
            message.remove();
        }
    }

    /**
     * 获取消息摘要
     *
     */
    public abstract String getSummary();

    /**
     * 获取发送者
     *
     */
    public String getSender(){
        if (message.getSender() == null) return "";
        return message.getSender();
    }




}
