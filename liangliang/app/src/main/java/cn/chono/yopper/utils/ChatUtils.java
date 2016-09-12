package cn.chono.yopper.utils;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.orhanobut.logger.Logger;
import com.tencent.TIMCustomElem;
import com.tencent.TIMImageElem;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AttributeDto;
import cn.chono.yopper.data.AudioMsg;
import cn.chono.yopper.data.DatingHandleStatusMsg;
import cn.chono.yopper.data.GiftMsg;
import cn.chono.yopper.data.HintMsg;
import cn.chono.yopper.data.ImgMsg;
import cn.chono.yopper.data.KeyTable;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageDto;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.NotificationMsg;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.data.UserReceiveGiftTable;
import cn.chono.yopper.data.UserToUserWithDatingTable;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.MessageFactory;
import cn.chono.yopper.smack.entity.ChatDto;


public class ChatUtils {

	/*
     * 保存聊天记录
	 * 
	 */

    public static void SaveOrUpdateChatMsgToDB(String jid, String message, long date, int r_s_type, int rend_status, String msgid, int send_status, String datingId, int counsel, String TIMMessage) {
        //今日星碰消息只有且仅有一条，所以对其我们查找不用根据packetId 因为packetId变化，而星碰jid则不会变化
        // 处理乱码乱码
        String mid = LoginUser.getInstance().getUserId() + "";
        String messages2 = null;
        try {
            messages2 = new String(message.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {

            ChatDto dto = App.getInstance().db.findFirst(Selector.from(ChatDto.class).where("msgId", " =", msgid));


            if (dto != null) {

                Logger.e("更新===" + msgid);

                dto.setDate(date);
                dto.setMid(mid);
                dto.setJid(jid);
                dto.setMessage(messages2);
                dto.setMsgId(msgid);
                dto.setR_s_type(r_s_type);

                if (dto.getSend_status() != ChatDto.readed_status) {
                    dto.setStatus(rend_status);
                }

                dto.setSend_status(send_status);

                dto.setDatingId(datingId);

                App.getInstance().db.update(dto);

            } else {

                Logger.e("保存===" + msgid);

                dto = new ChatDto(mid, jid, messages2, date, r_s_type, rend_status, msgid, send_status, datingId, counsel, TIMMessage);

                App.getInstance().db.save(dto);

            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存消息记录
     *
     * @param chatMessage
     * @param JID
     * @param is_success
     */

    public static void saveMessageRecord(String chatMessage, String JID, int is_success, int s_r_type, long time, int counsel, String datingId, String TIMMessage) {
        String mid = LoginUser.getInstance().getUserId() + "";

        try {
            MessageDto messageDto = null;

            int resource = 0;

            if (counsel == 1) {
                resource = 1;
                messageDto = App.getInstance().db.findFirst(Selector.from(MessageDto.class).where(" mid", " =", mid).and(" jid", " =", JID));
            } else if (!TextUtils.isEmpty(datingId)) {
                messageDto = App.getInstance().db.findFirst(Selector.from(MessageDto.class).where(" mid", " =", mid).and(" jid", " =", JID).and(" datingId", " =", datingId));
                resource = 0;
            } else {
                messageDto = App.getInstance().db.findFirst(Selector.from(MessageDto.class).where(" mid", " =", mid).and(" jid", " =", JID));
                resource = 2;
            }

            if (messageDto == null) {
                messageDto = new MessageDto(chatMessage, time, mid, JID, is_success, resource, 0, s_r_type, datingId, TIMMessage);
                App.getInstance().db.save(messageDto);

            } else {
                messageDto.setLastmessage(chatMessage);
                messageDto.setLasttime(time);
                messageDto.setSend_state(is_success);

                App.getInstance().db.update(messageDto);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    public static ChatDto ReceiveMsgFilter(TIMMessage msg) {

        ChatDto dto;

        ImMessage message = MessageFactory.getMessage(msg);

        if (message == null) {
            return null;
        }


        String TIMMessageStr = JsonUtils.toJson(message.getMessage());

        TextMsg textMsg = null;

        AudioMsg audioMsg = null;

        ImgMsg imgMsg = null;

        AttributeDto attributeDto = null;

        HintMsg hintMsg = null;

        NotificationMsg notificationMsg = null;

        DatingHandleStatusMsg datingHandleStatusMsg = null;

        GiftMsg giftMsg = null;

        for (int i = 0; i < msg.getElementCount(); ++i) {
            switch (msg.getElement(i).getType()) {

                case Text:
                    TIMTextElem textElem = (TIMTextElem) msg.getElement(i);
                    textMsg = new TextMsg();
                    textMsg.setText(textElem.getText());
                    textMsg.setType(MessageType.Text);
                    break;

                case Sound:
                    TIMSoundElem soundElem = (TIMSoundElem) msg.getElement(i);

                    audioMsg = new AudioMsg();
                    audioMsg.setType(MessageType.Audio);
                    audioMsg.setElem(soundElem);
                    audioMsg.setDuration((int) soundElem.getDuration());
                    break;

                case Image:
                    TIMImageElem imageElem = (TIMImageElem) msg.getElement(i);

                    imgMsg = new ImgMsg();
                    imgMsg.setElem(imageElem);
                    imgMsg.setType(MessageType.Img);
                    imgMsg.setH(imageElem.getImageList().get(0).getHeight());
                    imgMsg.setW(imageElem.getImageList().get(0).getWidth());

                    break;

                case Custom:

                    TIMCustomElem customElem = (TIMCustomElem) msg.getElement(i);
                    try {

                        String res = new String(customElem.getData(), "UTF-8");

                        String type = ChatUtils.getMsgType(res);
                        if (TextUtils.equals(type, MessageType.Text) || TextUtils.equals(type, MessageType.Img) || TextUtils.equals(type, MessageType.Audio)) {

                            //把相关属性给加上
                            attributeDto = JsonUtils.fromJson(res, AttributeDto.class);

                        } else if (TextUtils.equals(type, MessageType.Hint)) {

                            hintMsg = JsonUtils.fromJson(res, HintMsg.class);

                        } else if (TextUtils.equals(type, MessageType.Notification)) {

                            notificationMsg = JsonUtils.fromJson(res, NotificationMsg.class);

                        } else if (TextUtils.equals(type, MessageType.DatingHandleResult)) {

                            datingHandleStatusMsg = JsonUtils.fromJson(res, DatingHandleStatusMsg.class);

                        } else if (TextUtils.equals(type, MessageType.Gift)) {

                            giftMsg = JsonUtils.fromJson(res, GiftMsg.class);

                        }


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }

        String datingId = "";
        int counsel = 0;
        String msgType = "";
        String chatMessage = "";

        if (textMsg != null && attributeDto != null) {
            //文字
            textMsg.setDateid(attributeDto.getDateid());
            textMsg.setCounsel(attributeDto.getCounsel());
            textMsg.setMask(attributeDto.getMask());

            if (!TextUtils.isEmpty(attributeDto.getLockText())) {
                textMsg.setText(attributeDto.getLockText());
            }

            datingId = attributeDto.getDateid();
            counsel = attributeDto.getCounsel();
            msgType = MessageType.Text;

            chatMessage = JsonUtils.toJson(textMsg);


        } else if (imgMsg != null && attributeDto != null) {
            //图片
            datingId = attributeDto.getDateid();
            counsel = attributeDto.getCounsel();
            msgType = MessageType.Img;

            imgMsg.setDateid(attributeDto.getDateid());
            imgMsg.setCounsel(attributeDto.getCounsel());
            imgMsg.setMask(attributeDto.getMask());

            chatMessage = JsonUtils.toJson(imgMsg);

        } else if (audioMsg != null && attributeDto != null) {
            //语音

            datingId = attributeDto.getDateid();
            counsel = attributeDto.getCounsel();
            msgType = MessageType.Audio;

            audioMsg.setDateid(attributeDto.getDateid());
            audioMsg.setCounsel(attributeDto.getCounsel());
            audioMsg.setMask(attributeDto.getMask());

            chatMessage = JsonUtils.toJson(audioMsg);

        } else if (hintMsg != null) {
            //提示

            int action = hintMsg.getAction();
            if (!ChatUtils.isSupportMsgAction(action)) {
                return null;
            } else {
                if (action != 1 && action != 11 && action != 12) {
                    counsel = 1;
                } else {
                    datingId = hintMsg.getExtra().get("dateid").toString();
                }
            }

            msgType = MessageType.Hint;
            chatMessage = JsonUtils.toJson(hintMsg);

        } else if (notificationMsg != null) {
            //通知消息
            int notifytype = notificationMsg.getNotifytype();
            if (!ChatUtils.isSupportNotify(notifytype)) {
                return null;
            }

            msgType = MessageType.Notification;

            chatMessage = JsonUtils.toJson(notificationMsg);

        } else if (giftMsg != null) {
            //礼物消息
            datingId = giftMsg.getDateid();
            counsel = giftMsg.getCounsel();
            msgType = MessageType.Gift;

            chatMessage = JsonUtils.toJson(giftMsg);

        } else if (datingHandleStatusMsg != null) {
            //邀约处理结果
            msgType = MessageType.DatingHandleResult;
            datingId = datingHandleStatusMsg.getDateid();
            chatMessage = JsonUtils.toJson(datingHandleStatusMsg);
        }

        if (CheckUtil.isEmpty(chatMessage)) {
            return null;
        }

        String jid = msg.getConversation().getPeer();


        msg.getConversation().setReadMessage();

//        String identifer=msg.getConversation().getIdentifer();

//        Logger.e("peer="+peer);
//
//        Logger.e("identifer="+identifer);
//
//        if (YpSettings.isTest) {
//
//            if (TextUtils.equals(LoginUser.getInstance().getUserId() + "@test", msg.getSender())) {
//                return null;
//            }
//        } else {
//
//            if (TextUtils.equals(LoginUser.getInstance().getUserId() + "", msg.getSender())) {
//                return null;
//            }
//
//        }


        if (jid.contains("@test")) {
            jid = jid.replace("@test", "");
        }


        int r_s_type;

        if (msg.isSelf()) {
            r_s_type = ChatDto.s_type;
        } else {
            r_s_type = ChatDto.r_type;
        }


        if (r_s_type == ChatDto.s_type) {
            if (giftMsg == null) {

                return null;
            }
        }


        dto = ChatUtils.ReceiveMsgDeal(chatMessage, msgType, LoginUser.getInstance().getUserId() + "", jid, r_s_type, msg.getMsgId(), msg.timestamp() * 1000, counsel, datingId, TIMMessageStr);

        return dto;

    }


    public static void saveBlockHint(String userid, String jid, String msgid) {

        try {

            List<MessageDto> user_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", userid).and("jid", " =", jid).and("resource", " =", MessageDto.resource_user));

            if (user_messagedtoList != null && user_messagedtoList.size() > 0) {

                for (int i = 0; i < user_messagedtoList.size(); i++) {
                    MessageDto dto = user_messagedtoList.get(i);
                    String datingid = dto.getDatingId();

                    if (TextUtils.isEmpty(datingid)) {

                        return;
                    }
                    HintMsg hintMsg = new HintMsg();
                    hintMsg.setType(MessageType.Hint);
                    hintMsg.setText("你们存在拉黑关系，对方将无法收到你的消息");

                    String msgStr = JsonUtils.toJson(hintMsg);

                    ChatUtils.SaveOrUpdateChatMsgToDB(jid, msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingid, 0, "");

                    ChatUtils.saveMessageRecord(msgStr, jid, ChatDto.succeed, ChatDto.r_type, System.currentTimeMillis(), 0, datingid, "");

                    ChatDto chatDto = new ChatDto(userid, jid, msgStr, System.currentTimeMillis(), ChatDto.r_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingid, 0, "");

                    RxBus.get().post("onChatReceiveMsg", new CommonEvent(chatDto));
                }
            }


        } catch (DbException e) {
            e.printStackTrace();
        }

        RxBus.get().post("ReceiveMessageUpdateList", new CommonEvent());

    }


    public static void deleteBlockUserMessageList(String userid, String jid) {

        Logger.e("userid=" + userid + "---jid=" + jid);

        try {

            List<MessageDto> user_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", userid).and("jid", " =", jid).and("resource", " =", MessageDto.resource_user));

            if (user_messagedtoList != null && user_messagedtoList.size() > 0) {

                for (int i = 0; i < user_messagedtoList.size(); i++) {
                    MessageDto dto = user_messagedtoList.get(i);

                    App.getInstance().db.delete(dto);
                }
            }


        } catch (DbException e) {
            e.printStackTrace();
        }

        RxBus.get().post("ReceiveMessageUpdateList", new CommonEvent());

    }


    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        if (calendar.before(inputTime)) {
            //当前时间在输入时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日");
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "昨天 " + sdf.format(currenTimeZone);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + "月" + "d" + "日" + " HH:mm");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日" + " HH:mm");
                return sdf.format(currenTimeZone);
            }

        }

    }

    /**
     * 对接受的消息进行处理 不符合消息类型的消息将丢掉 符合条件的则保存至数据库
     */
    public static ChatDto ReceiveMsgDeal(String message, String msgType, String mid, String jid, int r_s_type, String msgid, long date, int counsel, String datingId, String TIMMessage) {

        ChatDto chatDto = null;

        NotificationMsg notificationMsg = JsonUtils.fromJson(message, NotificationMsg.class);

        int notifytype = 0;

        if (notificationMsg != null) {

            notifytype = notificationMsg.getNotifytype();

            if (!TextUtils.isEmpty(notificationMsg.getCategory())) {
                jid = notificationMsg.getCategory();
            }
        }


        if (TextUtils.equals(msgType, MessageType.DatingHandleResult)) {

            chatDto = new ChatDto(mid, jid, message, date, ChatDto.r_type, ChatDto.no_read_status, msgid, ChatDto.succeed, datingId, counsel, TIMMessage);

            DatingHandleStatusMsg datingHandleStatusMsg = JsonUtils.fromJson(message, DatingHandleStatusMsg.class);

            saveOrUpdateDatingDealStatus(jid, datingId, datingHandleStatusMsg.getDatingTheme(), UserToUserWithDatingTable.meActive, 2, datingHandleStatusMsg.getDatingHandleStatus(), datingHandleStatusMsg.getPublishDate_userId());

        }

        try {

            if (TextUtils.equals(msgType, MessageType.Hint)) {

                HintMsg hintMsg = JsonUtils.fromJson(message, HintMsg.class);

                int action = hintMsg.getAction();

                if (action == 1 || action == 11 || action == 12) {

                    chatDto = new ChatDto(mid, jid, message, date, r_s_type, ChatDto.no_read_status, msgid, ChatDto.succeed, datingId, counsel, TIMMessage);

                    ChatUtils.SaveOrUpdateChatMsgToDB(jid, message, date, r_s_type, ChatDto.no_read_status, msgid, ChatDto.succeed, datingId, counsel, TIMMessage);

                    ChatUtils.saveMessageRecord(message, jid, ChatDto.succeed, r_s_type, date, counsel, datingId, TIMMessage);


                } else {

                    //跟产品讨论 需不要存储
                    String orderId = hintMsg.getExtra().get("orderId").toString();
                    int orderStatus = 0;
                    String hint = "";
                    switch (action) {
                        case 20://咨询订单付款成功
                            orderStatus = 1;
                            hint = "";
                            break;

                        case 21://咨询开始前15分钟
                            orderStatus = 1;
                            hint = "";
                            break;

                        case 22://咨询开始
                            orderStatus = 2;
                            hint = "";
                            break;

                        case 23://占卜师端请求结束
                            orderStatus = 3;
                            hint = hintMsg.getText();
                            break;

                        case 24://客户确认结束
                            orderStatus = 4;
                            hint = "";
                            break;

                        case 25://客户确认结束超时
                            orderStatus = 4;
                            hint = "";
                            break;

                    }
                    DbHelperUtils.saveCounselOrderInfo(mid, jid, hint, orderId, orderStatus, 0);

                    if (action != 23) {
                        chatDto = new ChatDto(mid, jid, message, date, r_s_type, ChatDto.no_read_status, msgid, ChatDto.succeed, datingId, counsel, TIMMessage);
                        ChatUtils.SaveOrUpdateChatMsgToDB(jid, message, date, r_s_type, ChatDto.no_read_status, msgid, ChatDto.succeed, datingId, counsel, TIMMessage);

                        ChatUtils.saveMessageRecord(message, jid, ChatDto.succeed, r_s_type, date, counsel, datingId, TIMMessage);

                    }
                }

            } else {

                if (notifytype != 10 && notifytype != 11 && notifytype != 26 && notifytype != 27) {
                    ChatUtils.SaveOrUpdateChatMsgToDB(jid, message, date, r_s_type, ChatDto.no_read_status, msgid, ChatDto.succeed, datingId, counsel, TIMMessage);
                }

                //9文章评论回复通知；10奖品更新；11解锁消息；
                if (notifytype != 9 && notifytype != 10 && notifytype != 11 && notifytype != 26 && notifytype != 27) {
                    // 加过滤判断
                    ChatUtils.saveMessageRecord(message, jid, ChatDto.succeed, r_s_type, date, counsel, datingId, TIMMessage);
                    chatDto = new ChatDto(mid, jid, message, date, r_s_type, ChatDto.no_read_status, msgid, ChatDto.succeed, datingId, counsel, TIMMessage);
                }

                if (notifytype == 11) {
                    String userId = notificationMsg.getExtra().get("userId").toString();
                    saveOrUpdateKeyRecord(mid, userId + "", 1);
                }

                if (notifytype == 10) {
                    //奖品更新
                    SharedprefUtil.saveBoolean(ContextUtil.getContext(), mid + "prize", true);
                    RxBus.get().post("prizeUpdate", new CommonEvent());

                }

                if (notifytype == 26) {
                    //ta喜欢我
                    Logger.e("有人喜欢我了");
                    SharedprefUtil.saveBoolean(ContextUtil.getContext(), mid + "likeMe", true);
                    RxBus.get().post("likeMe", new CommonEvent());

                }

                if (notifytype == 27) {
                    //相互喜欢
                    Logger.e("我喜欢的人也喜欢我了");
                    SharedprefUtil.saveBoolean(ContextUtil.getContext(), mid + "likeEachOther", true);
                    RxBus.get().post("likeEachOther", new CommonEvent());

                }

            }


        } catch (Exception e) {

            e.printStackTrace();
        }
        return chatDto;
    }


    /**
     * 根据消息内容获取消息类型
     *
     * @param msg
     * @return
     */
    public static String getMsgType(String msg) {
        String type = "";
        try {
            JSONObject json = new JSONObject(msg);
            if (json != null) {

                type = json.getString("type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return type;
    }


    public static void msgHintDeal(ChatDto dto) {


//            VibratorUtil.Vibrate(1000);

        if (SharedprefUtil.getBoolean(ContextUtil.getContext(), YpSettings.IS_OPEAN_MESSAGE_VOICE, true)) {
            long pre_time = SharedprefUtil.getLong(ContextUtil.getContext(), YpSettings.message_voice_time, 0);
            if (pre_time == 0) {

                SharedprefUtil.saveLong(ContextUtil.getContext(), YpSettings.message_voice_time, System.currentTimeMillis());

                MediaPlayer.create(ContextUtil.getContext(), R.raw.office).start();
                PlaySoundPool playSoundPool = new PlaySoundPool(ContextUtil.getContext());
                playSoundPool.loadSfx(R.raw.office, 1);
                playSoundPool.play(1, 0);

            } else {
                long cur_time = System.currentTimeMillis();
                if (cur_time - pre_time >= 5000) {
                    SharedprefUtil.saveLong(ContextUtil.getContext(), YpSettings.message_voice_time, cur_time);
                    MediaPlayer.create(ContextUtil.getContext(), R.raw.office).start();
                    PlaySoundPool playSoundPool = new PlaySoundPool(ContextUtil.getContext());
                    playSoundPool.loadSfx(R.raw.office, 1);
                    playSoundPool.play(1, 0);

                }
            }
        }
    }

    /**
     * 根据消息内容获取邀约id
     *
     * @param msg
     * @return
     */
    public static String getMsgDatingId(String msg) {
        String datingId = "";
        try {
            JSONObject json = new JSONObject(msg);
            if (json != null) {
                datingId = json.getString("dateid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datingId;
    }


    public static long getNoReadNum(String userid) {

        long no_read_num = 0;

        try {
            List<ChatDto> list = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("mid", " =", userid).and("status", "=", 0));
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    ChatDto dto = list.get(i);
                    String message = dto.getMessage();
                    String type = ChatUtils.getMsgType(message);
                    if (TextUtils.equals(type, MessageType.Notification)) {
                        NotificationMsg notificationMsg = JsonUtils.fromJson(message, NotificationMsg.class);
                        if (notificationMsg.getNotifytype() != 9 && notificationMsg.getNotifytype() != 10 && notificationMsg.getNotifytype() != 11) {

                            int badge = notificationMsg.getBadge();
                            if (badge >= 0) {
                                if (badge == 0) {
                                    badge = 1;
                                }
                                no_read_num = no_read_num + badge;
                            }

                        }

                    } else {
                        no_read_num = no_read_num + 1;
                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        return no_read_num;
    }


    public static boolean isSupportMsgType(String type) {

        boolean isSupport = false;

        if (TextUtils.equals(type, MessageType.Text)) {
            isSupport = true;
            return isSupport;
        }

        if (TextUtils.equals(type, MessageType.Img)) {
            isSupport = true;
            return isSupport;
        }

        if (TextUtils.equals(type, MessageType.Audio)) {
            isSupport = true;
            return isSupport;
        }

        if (TextUtils.equals(type, MessageType.Gift)) {
            isSupport = true;
            return isSupport;
        }

        if (TextUtils.equals(type, MessageType.DatingHandleResult)) {
            isSupport = true;
            return isSupport;
        }

        if (TextUtils.equals(type, MessageType.Notification)) {
            isSupport = true;
            return isSupport;
        }

        if (TextUtils.equals(type, MessageType.Hint)) {
            isSupport = true;
            return isSupport;
        }

        return isSupport;
    }


    public static boolean isSupportMsgAction(int action) {

        boolean isSupport = false;

        if (action == 1) {//邀约删除
            isSupport = true;
            return isSupport;
        }


        if (action == 11) {//礼物接收提示
            isSupport = true;
            return isSupport;
        }

        if (action == 12) {//礼物拒绝提示
            isSupport = true;
            return isSupport;
        }


        if (action == 20) {//咨询订单付款成功
            isSupport = true;
            return isSupport;
        }

        if (action == 21) {//咨询开始前15分钟
            isSupport = true;
            return isSupport;
        }

        if (action == 22) {//咨询开始
            isSupport = true;
            return isSupport;
        }

        if (action == 23) {//占卜师申请结束
            isSupport = true;
            return isSupport;
        }

        if (action == 24) {//客户确认结束
            isSupport = true;
            return isSupport;
        }

        if (action == 25) {//客户确认结束超时
            isSupport = true;
            return isSupport;
        }

        return isSupport;
    }


    public static boolean isSupportNotify(int notifytype) {
        boolean isSupport = false;
        //0:系统通知类型点击后无动作；1送P果；2颜值打分；3邀请上传图片；4邀请认证视频；5邀请公开视频；6冒泡评论通知；7冒泡点赞通知；8每日星运通知；9文章评论回复通知（作废了）；10奖品更新；11解锁消息；12文章推送（作废）；26的时候表示ta喜欢我；27的时候表示相互喜欢；28表示照片解锁消息
        //20约会消息
        if (notifytype == 0 || notifytype == 1 || notifytype == 3) {
            isSupport = true;
            return isSupport;
        }

        if (notifytype == 4 || notifytype == 5 || notifytype == 6 || notifytype == 7 || notifytype == 10 || notifytype == 11) {
            isSupport = true;
            return isSupport;
        }

        if (notifytype == 20 || notifytype == 21 || notifytype == 22 || notifytype == 23 || notifytype == 24 || notifytype == 25) {
            isSupport = true;
            return isSupport;
        }

        if (notifytype == 26 || notifytype == 27 || notifytype == 28) {
            isSupport = true;
            return isSupport;
        }

        return isSupport;
    }


    /**
     * 保存或者更新解锁记录
     *
     * @param JID       129233
     * @param keyStatus
     */
    public static void saveOrUpdateKeyRecord(String mid, String JID, int keyStatus) {

        try {

            KeyTable keyTable = App.getInstance().db.findFirst(Selector.from(KeyTable.class).where(" userId", " =", mid).and(" targetId", " =", JID));

            if (keyTable == null) {

                keyTable = new KeyTable(JID, mid, keyStatus);
                App.getInstance().db.save(keyTable);

            } else {

                if (keyTable.getIsBrokenKey() == KeyTable.no_broken && keyStatus == KeyTable.had_broken) {

                    keyTable.setIsBrokenKey(keyStatus);
                    App.getInstance().db.update(keyTable);
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 获取解锁记录
     *
     * @param JID 129233
     */
    public static KeyTable getKeyRecord(String mid, String JID) {

        KeyTable keyTable = null;
        try {

            keyTable = App.getInstance().db.findFirst(Selector.from(KeyTable.class).where(" userId", " =", mid).and(" targetId", " =", JID));


        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return keyTable;
    }


    /**
     * 保存或者更新 用户用户基于邀约的邀约表的邀约处理状态
     *
     * @param JID 129233
     */
    public static void saveOrUpdateDatingDealStatus(String JID, String datingId, String datingTheme, int meIsActive, int isReply, int datingDealStatus, String publishDate_userId) {

        String mid = LoginUser.getInstance().getUserId() + "";


        try {

            UserToUserWithDatingTable dto = App.getInstance().db.findFirst(Selector.from(UserToUserWithDatingTable.class).where(" userId", " =", mid).and(" targetId", " =", JID).and(" datingId", " =", datingId));

            if (dto == null) {

                dto = new UserToUserWithDatingTable(JID, mid, datingId, datingTheme, meIsActive, isReply, datingDealStatus, publishDate_userId);
                App.getInstance().db.save(dto);

            } else {

                if (dto.getDatingDealStatus() != datingDealStatus) {
                    dto.setDatingDealStatus(datingDealStatus);
                    App.getInstance().db.update(dto);
                }

            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 获取邀约处理等信息
     *
     * @param JID 129233
     */
    public static UserToUserWithDatingTable getDatingTable(String mid, String JID, String datingId) {


        UserToUserWithDatingTable dto = null;

        try {

            dto = App.getInstance().db.findFirst(Selector.from(UserToUserWithDatingTable.class).where(" userId", " =", mid).and(" targetId", " =", JID).and(" datingId", " =", datingId));


        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return dto;
    }


    /**
     * 保存或者更新 用户用户基于邀约的邀约表的消息回复状态
     *
     * @param JID 129233
     */
    public static void saveOrUpdateIsReply(String JID, String datingId, String datingTheme, int meIsActive, int isReply, int datingDealStatus, String publishDate_userId) {

        String mid = LoginUser.getInstance().getUserId() + "";

        try {

            UserToUserWithDatingTable dto = App.getInstance().db.findFirst(Selector.from(UserToUserWithDatingTable.class).where(" userId", " =", mid).and(" targetId", " =", JID).and(" datingId", " =", datingId));

            if (dto == null) {

                dto = new UserToUserWithDatingTable(JID, mid, datingId, datingTheme, meIsActive, isReply, datingDealStatus, publishDate_userId);
                App.getInstance().db.save(dto);

            } else {

                if (dto.getIsReply() != UserToUserWithDatingTable.replyed) {
                    dto.setIsReply(isReply);
                    App.getInstance().db.update(dto);
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 保存或者更新 用户用户基于邀约的邀约表的邀约处理状态
     *
     * @param JID 129233
     */
    public static void saveOrUpdateDatingStatusTable(String JID, String datingId, String datingTheme, int meIsActive, int isReply, int datingDealStatus, String publishDate_userId) {

        String mid = LoginUser.getInstance().getUserId() + "";

        try {

            UserToUserWithDatingTable dto = App.getInstance().db.findFirst(Selector.from(UserToUserWithDatingTable.class).where(" userId", " =", mid).and(" targetId", " =", JID).and(" datingId", " =", datingId));

            if (dto == null) {

                dto = new UserToUserWithDatingTable(JID, datingId, mid, datingTheme, meIsActive, isReply, datingDealStatus, publishDate_userId);
                App.getInstance().db.save(dto);

            } else {
                dto.setIsReply(isReply);
                dto.setMeIsActive(meIsActive);
                dto.setDatingTheme(datingTheme);
                dto.setDatingDealStatus(datingDealStatus);
                App.getInstance().db.update(dto);

            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    /**
     * @param JID 129233
     */
    public static void DeleteChatRecord(String JID, String datingId, int resource) {

        String mid = LoginUser.getInstance().getUserId() + "";

        try {

            List<MessageDto> strange_messagedtoList = new ArrayList<MessageDto>();
            if (TextUtils.isEmpty(datingId)) {
                if (resource == 1) {
                    strange_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("resource", " =", resource));
                } else {
                    strange_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("jid", " =", JID));
                }

            } else {
                strange_messagedtoList = App.getInstance().db.findAll(Selector.from(MessageDto.class).where("mid", " =", mid).and("jid", " =", JID).and("datingId", " =", datingId));
            }


            if (strange_messagedtoList != null && strange_messagedtoList.size() > 0) {

                for (int i = 0; i < strange_messagedtoList.size(); i++) {
                    String s_jid = strange_messagedtoList.get(i).getJid();
                    String datingid = strange_messagedtoList.get(i).getDatingId();

                    if (!TextUtils.isEmpty(datingid)) {
                        App.getInstance().db.delete(ChatDto.class, WhereBuilder.b("jid", " =", s_jid).and("mid", " =", mid).and("datingId", " =", datingId));
                        App.getInstance().db.delete(MessageDto.class, WhereBuilder.b("jid", " =", s_jid).and("mid", " =", mid).and("datingId", " =", datingid));
                    } else {
                        App.getInstance().db.delete(ChatDto.class, WhereBuilder.b("jid", " =", s_jid).and("mid", " =", mid));
                        App.getInstance().db.delete(MessageDto.class, WhereBuilder.b("jid", " =", s_jid).and("mid", " =", mid));
                    }

                }
            }

        } catch (DbException e) {
            e.printStackTrace();

        }
    }


    public static String getMsgcontent(String message) {

        // 文本：文本
        //
        // 表情：[表情]
        //
        // 图片：[图片]
        //
        // P果：[P果]
        //

        String type = ChatUtils.getMsgType(message);

        String content = "";

        if (TextUtils.equals(type, MessageType.Img)) {

            content = "[图片]";

        } else if (TextUtils.equals(type, MessageType.DatingHandleResult)) {

            DatingHandleStatusMsg datingHandleStatusMsg = JsonUtils.fromJson(message, DatingHandleStatusMsg.class);
            if (datingHandleStatusMsg != null) {
                content = datingHandleStatusMsg.getText();
            }

        } else if (TextUtils.equals(type, MessageType.Gift)) {

            content = "[礼物]";

        } else if (TextUtils.equals(type, MessageType.Audio)) {

            content = "[语音]";

        } else if (TextUtils.equals(type, MessageType.Notification)) {

            content = "收到通知";

        } else if (TextUtils.equals(type, MessageType.Hint)) {

            HintMsg hintMsg = JsonUtils.fromJson(message, HintMsg.class);
            if (hintMsg != null) {
                content = hintMsg.getText();
            }

        } else {
            // 默认文字
            TextMsg textmsg = JsonUtils.fromJson(message, TextMsg.class);
            if (textmsg != null) {
                content = textmsg.getText();
            }

        }

        return content;
    }


    public static String msgTimeFormat(long datetime) {

        String time = "";

        long system_time = System.currentTimeMillis();
        if (TimeUtil.isToday(datetime)) {
            // 是当天

            int min = TimeUtil.getdifMin(datetime, system_time);

            if (min == 0) {
                time = "刚刚";
            } else if (1 <= min && min < 60) {
                time = min + "分钟前";
            } else {
                time = TimeUtil.gethour_minString(datetime);
            }

        } else {
            // 消息时间不是当天
            time = TimeUtil.getDateString(datetime, system_time);
        }

        return time;
    }


    /**
     * 根据消息内容获取mask
     *
     * @param msg
     * @return
     */
    public static int getMask(String msg) {
        int mask = 0;
        try {
            JSONObject json = new JSONObject(msg);
            if (json != null) {
                mask = json.getInt("mask");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mask;
    }


    /**
     * 将用户未读消息忽略置为已读
     */
    public static void setUserChatRecordReaded(String mid, String jid, String datingId) {
        try {
            List<ChatDto> no_read_list = new ArrayList<ChatDto>();
            no_read_list = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).and("status", " =", 0).and("datingId", " =", datingId));
            if (no_read_list != null && no_read_list.size() > 0) {
                for (int i = 0; i < no_read_list.size(); i++) {
                    ChatDto dto = no_read_list.get(i);
                    try {
                        dto.setStatus(ChatDto.readed_status);
                        App.getInstance().db.update(dto);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (DbException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 将用户与系统未读消息忽略置为已读
     */
    public static void setSystemChatRecordReaded(String mid, String jid) {
        try {
            List<ChatDto> no_read_list = new ArrayList<ChatDto>();
            no_read_list = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).and("status", " =", 0));
            if (no_read_list != null && no_read_list.size() > 0) {
                for (int i = 0; i < no_read_list.size(); i++) {
                    ChatDto dto = no_read_list.get(i);
                    try {
                        dto.setStatus(ChatDto.readed_status);
                        App.getInstance().db.update(dto);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (DbException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 将用户与咨询师未读消息忽略置为已读
     */
    public static void setCounselChatRecordReaded(String mid, String jid, String counsel) {
        try {
            List<ChatDto> no_read_list = new ArrayList<ChatDto>();
            no_read_list = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("jid", " =", jid).and("mid", " =", mid).and("status", " =", 0).and("counsel", " =", counsel));
            if (no_read_list != null && no_read_list.size() > 0) {
                for (int i = 0; i < no_read_list.size(); i++) {
                    ChatDto dto = no_read_list.get(i);
                    try {
                        dto.setStatus(ChatDto.readed_status);
                        App.getInstance().db.update(dto);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (DbException e1) {
            e1.printStackTrace();
        }
    }


    public static void changeSendStatus(int send_status, String msgId, TIMSoundElem soundElem) {

        try {
            ChatDto dto = App.getInstance().db.findFirst(Selector.from(ChatDto.class).where("msgId", " =", msgId));
            if (dto != null) {

                Logger.e("changeSendStatus=" + msgId);

                dto.setSend_status(send_status);

                if (soundElem != null) {

                    String msgType = ChatUtils.getMsgType(dto.getMessage());

                    if (TextUtils.equals(msgType, MessageType.Audio)) {

                        AudioMsg audioMsg = JsonUtils.fromJson(dto.getMessage(), AudioMsg.class);
                        audioMsg.setElem(soundElem);

                        String messageStr = JsonUtils.toJson(audioMsg);
                        dto.setMessage(messageStr);
                    }
                }

                App.getInstance().db.update(dto);

            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    /**
     * 根据消息发送时间插入到有序的消息list
     *
     * @param msgDto
     * @param msgDtoList
     * @return
     */
    public static List<MessageDto> insertDto(MessageDto msgDto, List<MessageDto> msgDtoList) {

        boolean haveInserted = false;

        List<MessageDto> sortedAfter = new ArrayList<MessageDto>();
        if (msgDtoList != null && msgDtoList.size() > 0) {

            for (int i = 0, j = 0; i < msgDtoList.size(); ) {
                if (haveInserted || msgDtoList.get(i).getLasttime() > msgDto.getLasttime()) {

                    sortedAfter.add(j++, msgDtoList.get(i++));
                } else {
                    sortedAfter.add(j++, msgDto);
                    haveInserted = true;
                }
            }
            int preSize = msgDtoList.size();
            int CurSize = sortedAfter.size();
            if (preSize == CurSize) {
                sortedAfter.add(msgDto);
            }

        } else {
            sortedAfter.add(msgDto);
        }

        return sortedAfter;

    }


    /**
     * 获取礼物接收状态
     *
     * @param targetId
     * @param userId
     * @param datingId
     * @return
     */
    public static int getGiftReceiveStatus(String targetId, String userId, String datingId) {

        int giftReceiveStatus = 0;

        try {
            UserReceiveGiftTable dto = App.getInstance().db.findFirst(Selector.from(UserReceiveGiftTable.class).where("targetId", " =", targetId).and("userId", " =", userId).and("datingId", " =", datingId));
            if (dto != null) {

                giftReceiveStatus = dto.getIsGiftReceive();

            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return giftReceiveStatus;
    }


    /**
     * 改变礼物接收状态
     *
     * @param targetId
     * @param userId
     * @param datingId
     * @return
     */
    public static void changeGiftReceiveStatus(String targetId, String userId, String datingId, int giftReceiveStatus) {

        try {
            UserReceiveGiftTable dto = App.getInstance().db.findFirst(Selector.from(UserReceiveGiftTable.class).where("targetId", " =", targetId).and("userId", " =", userId).and("datingId", " =", datingId));
            if (dto != null) {
                if (dto.getIsGiftReceive() != giftReceiveStatus) {

                    dto.setIsGiftReceive(giftReceiveStatus);

                    App.getInstance().db.update(dto);
                }

            } else {
                dto = new UserReceiveGiftTable();
                dto.setDatingId(datingId);
                dto.setTargetId(targetId);
                dto.setUserId(userId);
                dto.setIsGiftReceive(giftReceiveStatus);
                App.getInstance().db.save(dto);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

    }


}