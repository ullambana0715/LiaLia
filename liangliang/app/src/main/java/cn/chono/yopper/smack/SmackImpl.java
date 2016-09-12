//package cn.chono.yopper.smack;
//
//import android.text.TextUtils;
//
//import com.lidroid.xutils.util.LogUtils;
//import cn.chono.yopper.YPApplication;
//import cn.chono.yopper.data.AttractionMsg;
//import cn.chono.yopper.data.HintMsg;
//import cn.chono.yopper.data.MessageType;
//import cn.chono.yopper.data.NotificationMsg;
//import cn.chono.yopper.smack.entity.ChatDto;
//import cn.chono.yopper.utils.ChatUtils;
//import cn.chono.yopper.utils.CheckUtil;
//import cn.chono.yopper.utils.JsonUtils;
//import cn.chono.yopper.utils.UUIDGenerator;
//
//public class SmackImpl implements Smack {
//
//    /************
//     * 收到新消息处理
//     ********************/
//    private synchronized void registerReceiveNewMessageListener() {
//        // do not register multiple packet listeners
//        if (receivermPacketListener != null) {
//            // 如果监听存在就删除监听
//            mXMPPConnection.removePacketListener(receivermPacketListener);
//        }
//        // 对消息进行过滤
//        PacketTypeFilter filter = new PacketTypeFilter(Message.class);
//        if (receivermPacketListener == null) {
//            // 消息监听方法
//            receivermPacketListener = new PacketListener() {
//                public void processPacket(Packet packet) {
//
//                    LogUtils.e(packet.toXML());
//
//                    if (packet instanceof Message) {// 如果是消息类型
//
//                        Message msg = (Message) packet;
//
//                        if (CheckUtil.isEmpty(msg.getPacketID())) {
//                            msg.setPacketID(UUIDGenerator.getUUID());
//                        }
//
//                        // 获取消息内容
//                        String chatMessage = msg.getBody();
//
//                        if (CheckUtil.isEmpty(chatMessage)) {
//                            return;// 如果消息为空，直接返回了
//                        }
//
//                        // 监听消息类型是发送还是收到
//                        Carbon cc = CarbonManager.getCarbon(msg);
//
//                        if (cc != null && cc.getDirection() == Carbon.Direction.received) {// 收到的消息
//
//                            msg = (Message) cc.getForwarded().getForwardedPacket();
//                            chatMessage = msg.getBody();
//                        } else if (cc != null && cc.getDirection() == Carbon.Direction.sent) {//自己发送的
//                            return;
//                        }
//
//                        if (msg.getType() == Message.Type.error) {
//                            LogUtils.e("我自己发送的----------=" + msg.getPacketID());
//                            return;
//                        }
//
//                        String jid = getJabberID(msg.getFrom());// 消息来自对象
//
//
//                        if (CheckUtil.isEmpty(jid)) {
//                            return;
//                        }
//
//                        //发现模块文章有更新
//                        if (jid.equals("chono")) {
//
//                            return;
//                        }
//
//
//                        String mid = YPApplication.loginUser.getUserId() + "";
//
//                        if (jid.equals(mid)) {
//                            return;
//                        }
//
//                        String resource = getJabberResource(msg.getFrom());
//
//                        if (CheckUtil.isEmpty(resource)) {
//                            return;
//                        }
//
//                        int postion = jid.indexOf("@");
//
//                        if (postion >= jid.length() || postion == -1) {
//                            return;
//                        }
//
//                        String fromuserid = jid.substring(0, postion);
//
//                        String type = ChatUtils.getMsgType(chatMessage);
//
//                        if (!ChatUtils.isSupportMsgType(type)) {
//                            return;
//                        }
//
//                        if (TextUtils.equals(type, MessageType.Attraction)) {
//
//                            AttractionMsg attractionMsg = JsonUtils.fromJson(chatMessage, AttractionMsg.class);
//                            String dateid = attractionMsg.getDateid();
//                            if (CheckUtil.isEmpty(dateid)) {
//                                return;
//                            }
//                        }
//
//                        if (!CheckUtil.isNumeric(fromuserid)) {
//                            //不是数字
//                            if (!TextUtils.equals(type, MessageType.Notification)) {
//                                return;
//                            } else {
//                                NotificationMsg notificationMsg = JsonUtils.fromJson(chatMessage, NotificationMsg.class);
//                                int notifytype = notificationMsg.getNotifytype();
//                                if (!ChatUtils.isSupportNotify(notifytype)) {
//                                    return;
//                                }
//                            }
//                        }
//
//                        String counsel = "";
//                        String datingId = "";
//                        if (TextUtils.equals(type, MessageType.Hint)) {
//
//                            HintMsg hintMsg = JsonUtils.fromJson(chatMessage, HintMsg.class);
//                            int action = hintMsg.getAction();
//                            if (!ChatUtils.isSupportMsgAction(action)) {
//                                return;
//                            } else {
//                                if (action != 1) {
//                                    counsel = "diviner";
//                                } else {
//                                    datingId = hintMsg.getExtra().get("dateid").toString();
//                                }
//                            }
//
//                        } else {
//                            counsel = ChatUtils.getMsgCounsek(chatMessage);
//
//                            datingId = ChatUtils.getMsgDatingId(chatMessage);
//
//                        }
//
//                        if (resource.equals("phone") && CheckUtil.isEmpty(counsel) && CheckUtil.isEmpty(datingId)) {
//
//                            return;
//                        }
//
//                        long ts;// 消息时间戳
//                        DelayInfo timestamp = (DelayInfo) msg.getExtension("delay", "urn:xmpp:delay");
//
//                        if (timestamp != null) {
//                            ts = timestamp.getStamp().getTime();
//                        } else {
//                            ts = System.currentTimeMillis();
//                        }
//
//
//                        LogUtils.e("收到消息的datingId=" + datingId);
//
//                        ChatDto dto = ChatUtils.ReceiveMsgDeal(chatMessage, type, mid, jid, msg.getPacketID(), ts, counsel, datingId);
//                        if (dto != null) {
//                            mService.newMessage(dto);// 通知service，处理是否需要显示通知栏，
//                        }
//
//                    }
//                }
//            };
//        }
//        mXMPPConnection.addPacketListener(receivermPacketListener, filter);// 这是最关健的了，少了这句，前面的都是白费功夫
//    }
//
//}

