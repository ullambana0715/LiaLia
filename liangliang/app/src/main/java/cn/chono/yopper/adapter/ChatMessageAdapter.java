package cn.chono.yopper.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andbase.tractor.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.orhanobut.logger.Logger;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.tencent.TIMValueCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.activity.chat.ChatImageShowActivity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AudioMsg;
import cn.chono.yopper.data.DatingHandleStatusMsg;
import cn.chono.yopper.data.GiftMsg;
import cn.chono.yopper.data.HintMsg;
import cn.chono.yopper.data.ImgMsg;
import cn.chono.yopper.data.KeyTable;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserToUserWithDatingTable;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.MessageFactory;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.FaceTextUtils;
import cn.chono.yopper.utils.FileUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.MediaUtil;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.XCRoundImageViewByXfermode;

/**
 * 聊天适配器
 *
 * @author SQ
 * @ClassName: ChatMessageAdapter
 */

public class ChatMessageAdapter extends BaseListAdapter<ChatDto> {


    public interface OnItemSendFailClickLitener {

        /**
         * 发送失败 重发点击监听
         */
        void onItemSendFailClick(ChatDto dto);

    }

    private OnItemSendFailClickLitener mOnItemSendFailClickLitener;

    public void setOnItemSendFailClickLitener(
            OnItemSendFailClickLitener mOnItemClickLitener) {
        this.mOnItemSendFailClickLitener = mOnItemClickLitener;
    }


    public interface OnItemBrokenkeyClickLitener {

        /**
         * 破冰
         */
        void onItemBrokenkeyClick();

    }

    private OnItemBrokenkeyClickLitener mOnItemBrokenkeyClickLitener;

    public void setOnItemBrokenkeyClickLitener(OnItemBrokenkeyClickLitener mOnItemClickLitener) {
        this.mOnItemBrokenkeyClickLitener = mOnItemClickLitener;
    }


    // Item的类型

    private final int Type_Send_Txt = 0;
    private final int Type_Receiver_Txt = 1;

    // 图片
    private final int Type_Send_Image = 2;
    private final int Type_Receiver_Image = 3;

    // 语音
    private final int Type_Send_Audio = 4;
    private final int Type_Receiver_Audio = 5;

    private final int Type_Hint = 6;

    // 礼物
    private final int Type_Send_Gift = 7;
    private final int Type_Receiver_Gift = 8;


    private Context mContext;


    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    private int userid;

    private int targetUserid;

    //是否解锁破冰
    private int keyBroken;

    //是否主动方
    private int isActive;


    public ChatMessageAdapter(Context context, List<ChatDto> msgList, int userId, int targetUserId, int isactive, int keybroken) {
        super(context, msgList);
        this.mContext = context;
        this.list = msgList;
        mPool = Glide.get(context).getBitmapPool();

        this.userid = userId;

        this.targetUserid = targetUserId;
        this.isActive = isactive;
        this.keyBroken = keybroken;
        transformation = new CropCircleTransformation(mPool);
    }


    @Override
    public int getItemViewType(int position) {
        ChatDto chatdto = list.get(position);
        String message = chatdto.getMessage();
        String type = ChatUtils.getMsgType(message);
        if (TextUtils.equals(type, MessageType.Img)) {
            return chatdto.getR_s_type() == ChatDto.s_type ? Type_Send_Image : Type_Receiver_Image;

        } else if (TextUtils.equals(type, MessageType.Gift)) {

            return chatdto.getR_s_type() == ChatDto.s_type ? Type_Send_Gift : Type_Receiver_Gift;

        } else if (TextUtils.equals(type, MessageType.DatingHandleResult)) {

            return chatdto.getR_s_type() == ChatDto.s_type ? Type_Send_Txt : Type_Receiver_Txt;

        } else if (TextUtils.equals(type, MessageType.Audio)) {

            return chatdto.getR_s_type() == ChatDto.s_type ? Type_Send_Audio : Type_Receiver_Audio;

        } else if (TextUtils.equals(type, MessageType.Hint)) {

            return chatdto.getR_s_type() == ChatDto.s_type ? Type_Hint : Type_Hint;

        } else {
            // 默认文字
            return chatdto.getR_s_type() == ChatDto.s_type ? Type_Send_Txt : Type_Receiver_Txt;
        }

    }


    @Override
    public int getViewTypeCount() {
        return 9;
    }

    @SuppressLint("InflateParams")
    private View createViewByType(String type, int position) {

        if (TextUtils.equals(type, MessageType.Img)) {// 图片类型
            return getItemViewType(position) == Type_Receiver_Image ? mInflater.inflate(R.layout.item_chat_received_mask_image, null) : mInflater.inflate(R.layout.item_chat_sent_image, null);

        } else if (TextUtils.equals(type, MessageType.Gift)) {
            // 发送P果 跟文字布局一样 所以就使用文字的布局,收到苹果跟hint一样
            return getItemViewType(position) == Type_Receiver_Gift ? mInflater.inflate(R.layout.item_chat_received_gift, null) : mInflater.inflate(R.layout.item_chat_sent_gift, null);

        } else if (TextUtils.equals(type, MessageType.Audio)) {

            return getItemViewType(position) == Type_Receiver_Audio ? mInflater.inflate(R.layout.item_chat_received_mask_audio, null) : mInflater.inflate(R.layout.item_chat_send_audio, null);

        } else if (TextUtils.equals(type, MessageType.Hint)) {
            // 中间提示信息
            return mInflater.inflate(R.layout.item_chat_other_message_hint, null);

        } else if (TextUtils.equals(type, MessageType.DatingHandleResult)) {
            // 邀约处理结果跟文本显示item一致
            return getItemViewType(position) == Type_Receiver_Txt ? mInflater.inflate(R.layout.item_chat_received_mask_message, null) : mInflater.inflate(R.layout.item_chat_sent_message, null);

        } else {// 默认文本
            return getItemViewType(position) == Type_Receiver_Txt ? mInflater.inflate(R.layout.item_chat_received_mask_message, null) : mInflater.inflate(R.layout.item_chat_sent_message, null);
        }
    }

    private static class ViewHolder {
        // 文本类型
        TextView chat_time_tv;

        // 用户头像 根据消息收到还是发送的类型 设置自己的头像或者对方的头像
        ImageView iv_avatar;

        ImageView send_fail_iv;

        ProgressBar pb_send_status;

        TextView text_message_tv;

        // 图片
        XCRoundImageViewByXfermode image_iv;
        // 发送图片百分比
        TextView image_progress_tv;
        // 图片布局
        RelativeLayout chat_item_image_layout;

        XCRoundImageViewByXfermode image_bg_iv;

        // 提示(都是收到消息)
        TextView others_message_hint_tv;

        ImageView audio_iv;

        TextView audio_timelenth_tv;
        RelativeLayout audio_layout;

        LinearLayout no_mask_layout;

        ViewStub mask_vs;

        ImageView giftmsg_iv;

        TextView gift_charm_tv;

    }

    @SuppressWarnings({"static-access", "unchecked"})
    @Override
    public View bindView(final int position, View convertView, ViewGroup parent) {
        final ChatDto chatdto = list.get(position);
        String message = chatdto.getMessage();
        final String type = ChatUtils.getMsgType(message);

        ViewHolder viewholder = null;

        if (convertView == null) {
            convertView = createViewByType(type, position);
            viewholder = new ViewHolder();

            viewholder.chat_time_tv = (TextView) convertView.findViewById(R.id.chat_item_time_iv);

            viewholder.iv_avatar = (ImageView) convertView.findViewById(R.id.chat_item_user_img_iv);

            viewholder.send_fail_iv = (ImageView) convertView.findViewById(R.id.chat_item_send_fail_iv);

            viewholder.pb_send_status = (ProgressBar) convertView.findViewById(R.id.chat_item_send_progressbar);

            viewholder.text_message_tv = (TextView) convertView.findViewById(R.id.chat_item_text_message_tv);

            viewholder.image_iv = (XCRoundImageViewByXfermode) convertView.findViewById(R.id.chat_item_image_iv);

            viewholder.image_progress_tv = (TextView) convertView.findViewById(R.id.chat_item_image_send_image_progress_tv);

            viewholder.chat_item_image_layout = (RelativeLayout) convertView.findViewById(R.id.chat_item_image_layout);

            viewholder.image_bg_iv = (XCRoundImageViewByXfermode) convertView.findViewById(R.id.chat_item_image_bg_iv);

            viewholder.others_message_hint_tv = (TextView) convertView.findViewById(R.id.chat_item_others_message_hint_tv);

            viewholder.audio_iv = (ImageView) convertView.findViewById(R.id.chat_item_audio_iv);
            viewholder.audio_layout = (RelativeLayout) convertView.findViewById(R.id.chat_item_audio_layout);

            viewholder.audio_timelenth_tv = (TextView) convertView.findViewById(R.id.chat_audio_timelenth_tv);


            viewholder.no_mask_layout = (LinearLayout) convertView.findViewById(R.id.chat_item_no_mask_layout);

            viewholder.mask_vs = (ViewStub) convertView.findViewById(R.id.chat_item_mask_vs);


            viewholder.giftmsg_iv = (ImageView) convertView.findViewById(R.id.chat_item_giftmsg_iv);

            viewholder.gift_charm_tv = (TextView) convertView.findViewById(R.id.chat_item_gift_charm_tv);

            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }


        // iv_avatar
        if (viewholder.iv_avatar != null) {

            viewholder.iv_avatar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    ViewsUtils.preventViewMultipleClick(v, 1000);

                    hideSoftInputView((Activity) mContext);
                    Bundle bundle = new Bundle();
                    if (chatdto.getR_s_type() == 0) {
                        bundle.putInt(YpSettings.USERID, userid);
                    } else {
                        bundle.putInt(YpSettings.USERID, targetUserid);
                    }
                    ActivityUtil.jump(mContext, UserInfoActivity.class, bundle, 0, 100);
                }
            });

            UserDto userdto = null;
            if (chatdto.getR_s_type() == 0) {
                userdto = DbHelperUtils.getDbUserInfo(userid);
            } else {
                userdto = DbHelperUtils.getDbUserInfo(targetUserid);
            }

            if (userdto != null) {
                String imageurl = ImgUtils.DealImageUrl(userdto.getProfile().getHeadImg(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

                if (!((Activity) mContext).isFinishing()) {
                    Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(viewholder.iv_avatar);
                }
            }
        }


        if (TextUtils.equals(type, MessageType.Img)) {// 图片类型

            final ImgMsg imgMsg = JsonUtils.fromJson(message, ImgMsg.class);

            if (imgMsg != null) {

                int isMask = 0;

                if (chatdto.getR_s_type() == 1) {

                    if (isActive == UserToUserWithDatingTable.meActive) {

                        if (keyBroken != KeyTable.had_broken) {
                            isMask = imgMsg.getMask();
                        }
                    }
                }

                if (isMask == 1) {

                    if (viewholder.mask_vs != null) {
                        viewholder.mask_vs.setVisibility(View.VISIBLE);
                    }

                    if (viewholder.no_mask_layout != null) {
                        viewholder.no_mask_layout.setVisibility(View.GONE);
                    }

                    LinearLayout chat_item_mask_layout = (LinearLayout) convertView.findViewById(R.id.chat_item_mask_layout);

                    chat_item_mask_layout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ViewsUtils.preventViewMultipleClick(v, 1000);
                            mOnItemBrokenkeyClickLitener.onItemBrokenkeyClick();
                        }
                    });


                } else {

                    if (viewholder.mask_vs != null) {
                        viewholder.mask_vs.setVisibility(View.GONE);
                    }

                    if (viewholder.no_mask_layout != null) {
                        viewholder.no_mask_layout.setVisibility(View.VISIBLE);
                    }

                    double w = imgMsg.getW();
                    double h = imgMsg.getH();
                    float dip = UnitUtil.getScreenDIP(mContext);

                    if (w == 0 || h == 0) {
                        w = 640;
                        h = 960;
                    }
                    if (w > h) {
                        double mulriple = w / 100;
                        h = h / mulriple;
                        w = 100;
                    } else {
                        double mulriple = h / 100;
                        w = w / mulriple;
                        h = 100;
                    }

                    LayoutParams para = viewholder.chat_item_image_layout.getLayoutParams();
                    para.height = (int) (h * dip);
                    para.width = (int) (w * dip);

                    viewholder.chat_item_image_layout.setLayoutParams(para);

                    viewholder.image_iv.setType(XCRoundImageViewByXfermode.TYPE_ROUND);
                    viewholder.image_iv.setRoundBorderRadius(60);

                    if (chatdto.getR_s_type() == 0) {
                        viewholder.image_bg_iv.setType(XCRoundImageViewByXfermode.TYPE_ROUND);
                        viewholder.image_bg_iv.setRoundBorderRadius(60);
                    }

                    String filePath = imgMsg.getElem().getPath();
                    String imgurl = "";

                    if (imgMsg.getElem().getImageList() != null && imgMsg.getElem().getImageList().size() > 0) {

                        imgurl = imgMsg.getElem().getImageList().get(0).getUrl();
                    }

                    final String finalimgurl = imgurl;

                    // 判断数据库里是否有本地图片路径
                    if (!TextUtils.isEmpty(filePath) && ImgUtils.fileIsExists(filePath)) {// 不为空时，存在

                        // 加载本地图片(路径以/开头， 绝对路径)
                        if (!((Activity) mContext).isFinishing()) {
                            Glide.with(mContext).load("file://" + filePath).into(viewholder.image_iv);
                        }

                    } else {// 不存在

                        if (!((Activity) mContext).isFinishing()) {
                            Glide.with(mContext).load(finalimgurl).into(viewholder.image_iv);
                        }
                    }

                    viewholder.chat_item_image_layout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            ViewsUtils.preventViewMultipleClick(v, 1000);
                            navToImageview(filePath, finalimgurl);
                        }
                    });

                }
            }
        }


        //
        if (chatdto.getR_s_type() == 0) {

            if (chatdto.getSend_status() == 0) {
                // 发送失败
                if (TextUtils.equals(type, MessageType.Img)) {// 图片类型
                    viewholder.image_progress_tv.setVisibility(View.GONE);
                    viewholder.image_bg_iv.setVisibility(View.GONE);
                }

                if (viewholder.pb_send_status != null && viewholder.send_fail_iv != null) {
                    viewholder.pb_send_status.setVisibility(View.GONE);
                    viewholder.send_fail_iv.setVisibility(View.VISIBLE);
                }

            } else if (chatdto.getSend_status() == 2) {
                // 发送中
                if (TextUtils.equals(type, MessageType.Img)) {// 图片类型

                    viewholder.image_bg_iv.setVisibility(View.VISIBLE);

                    viewholder.image_progress_tv.setVisibility(View.GONE);

                }
                if (viewholder.pb_send_status != null && viewholder.send_fail_iv != null) {
                    viewholder.pb_send_status.setVisibility(View.VISIBLE);

                    viewholder.send_fail_iv.setVisibility(View.GONE);
                }

            } else {
                // 发送成功
                if (TextUtils.equals(type, MessageType.Img)) {
                    viewholder.image_progress_tv.setVisibility(View.GONE);
                    viewholder.image_bg_iv.setVisibility(View.GONE);
                }

                if (viewholder.pb_send_status != null && viewholder.send_fail_iv != null) {
                    viewholder.pb_send_status.setVisibility(View.GONE);
                    viewholder.send_fail_iv.setVisibility(View.GONE);
                }

            }

        }


        if (TextUtils.equals(type, MessageType.Gift)) {


            GiftMsg giftMsg = JsonUtils.fromJson(message, GiftMsg.class);

            if (giftMsg != null) {

                Glide.with(mContext).load(giftMsg.getGiftImg()).into(viewholder.giftmsg_iv);

                viewholder.gift_charm_tv.setText("+" + giftMsg.getCharmValue());
            }

        }

        if (TextUtils.equals(type, MessageType.Hint)) {
            if (viewholder.others_message_hint_tv != null) {
                HintMsg hintMsg = JsonUtils.fromJson(message, HintMsg.class);
                viewholder.others_message_hint_tv.setText(hintMsg.getText());
            }
        }


        // 消息时间的显示和设置
        long current_msg_time = chatdto.getDate();

        if (viewholder.chat_time_tv != null) {

            if (position == 0) {
                Date date = new Date(current_msg_time);
                if (!TimeUtil.isToday(current_msg_time)) {
                    // 消息时间不是当天
                    viewholder.chat_time_tv.setText(TimeUtil.getDateTimeString(current_msg_time));
                    viewholder.chat_time_tv.setVisibility(View.VISIBLE);

                } else {
                    viewholder.chat_time_tv.setText(TimeUtil.gethour_minString(current_msg_time));
                    viewholder.chat_time_tv.setVisibility(View.VISIBLE);
                }

            } else {

                Date date = new Date(current_msg_time);
                long pre_msg_time = list.get(position - 1).getDate();
                if (!TimeUtil.isToday(current_msg_time)) {
                    // 消息时间不是当天
                    // 判断跟上一条消息的时间差 一分钟则显示
                    if (TimeUtil.getIntervalDays(current_msg_time, pre_msg_time)) {
                        viewholder.chat_time_tv.setText(TimeUtil.getDateTimeString(current_msg_time));
                        viewholder.chat_time_tv.setVisibility(View.VISIBLE);
                    } else {
                        viewholder.chat_time_tv.setVisibility(View.GONE);
                    }
                } else {
                    // 是当天 判断与上一条消息时间差 超过60秒就显示

                    if (TimeUtil.getIntervalDays(pre_msg_time, current_msg_time)) {
                        viewholder.chat_time_tv.setText(TimeUtil.gethour_minString(current_msg_time));
                        viewholder.chat_time_tv.setVisibility(View.VISIBLE);
                    } else {
                        viewholder.chat_time_tv.setVisibility(View.GONE);
                    }

                }

            }
        }


        // 文字表情消息
        if (type.equals(MessageType.Text) && viewholder.text_message_tv != null) {


            TextMsg chatMsg = JsonUtils.fromJson(message, TextMsg.class);


            int isMask = 0;

            if (chatdto.getR_s_type() == 1) {

                if (isActive == UserToUserWithDatingTable.meActive) {

                    if (keyBroken != KeyTable.had_broken) {
                        isMask = chatMsg.getMask();

                    }
                }

            }


            if (isMask == 1) {

                //遮挡
                if (viewholder.mask_vs != null) {
                    viewholder.mask_vs.setVisibility(View.VISIBLE);
                }

                if (viewholder.no_mask_layout != null) {
                    viewholder.no_mask_layout.setVisibility(View.GONE);
                }

                LinearLayout chat_item_mask_layout = (LinearLayout) convertView.findViewById(R.id.chat_item_mask_layout);

                chat_item_mask_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        mOnItemBrokenkeyClickLitener.onItemBrokenkeyClick();
                    }
                });


            } else {

                if (viewholder.mask_vs != null) {
                    viewholder.mask_vs.setVisibility(View.GONE);
                }

                if (viewholder.no_mask_layout != null) {
                    viewholder.no_mask_layout.setVisibility(View.VISIBLE);
                }

                if (!CheckUtil.isEmpty(chatMsg.getText())) {
                    SpannableString spannableString = FaceTextUtils.toSpannableString(mContext, chatMsg.getText());
                    viewholder.text_message_tv.setText(spannableString, TextView.BufferType.SPANNABLE);
                }

            }
        }


        // 文字表情消息
        if (type.equals(MessageType.DatingHandleResult) && viewholder.text_message_tv != null) {

            DatingHandleStatusMsg chatMsg = JsonUtils.fromJson(message, DatingHandleStatusMsg.class);


            int isMask = 0;

            if (chatdto.getR_s_type() == 1) {

                if (isActive == UserToUserWithDatingTable.meActive) {

                    if (keyBroken != KeyTable.had_broken) {
                        isMask = chatMsg.getMask();
                    }
                }
            }

            if (isMask == 1) {

                if (viewholder.mask_vs != null) {
                    viewholder.mask_vs.setVisibility(View.VISIBLE);
                }

                if (viewholder.no_mask_layout != null) {
                    viewholder.no_mask_layout.setVisibility(View.GONE);
                }

                LinearLayout chat_item_mask_layout = (LinearLayout) convertView.findViewById(R.id.chat_item_mask_layout);

                chat_item_mask_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        mOnItemBrokenkeyClickLitener.onItemBrokenkeyClick();
                    }
                });


            } else {

                if (viewholder.mask_vs != null) {
                    viewholder.mask_vs.setVisibility(View.GONE);
                }

                if (viewholder.no_mask_layout != null) {
                    viewholder.no_mask_layout.setVisibility(View.VISIBLE);
                }

                if (!CheckUtil.isEmpty(chatMsg.getText())) {
                    SpannableString spannableString = FaceTextUtils.toSpannableString(mContext, chatMsg.getText());
                    viewholder.text_message_tv.setText(spannableString, TextView.BufferType.SPANNABLE);
                }

            }
        }

        if (TextUtils.equals(type, MessageType.Audio)) {

            final AudioMsg audioMsg = JsonUtils.fromJson(chatdto.getMessage(), AudioMsg.class);

            int isMask = 0;

            if (chatdto.getR_s_type() == 1) {

                if (isActive == UserToUserWithDatingTable.meActive) {

                    if (keyBroken != KeyTable.had_broken) {
                        isMask = audioMsg.getMask();
                    }
                }
            }


            if (chatdto.getR_s_type() == 0) {
                viewholder.audio_iv.setBackgroundResource(R.drawable.send_audio_anim);
            } else {
                viewholder.audio_iv.setBackgroundResource(R.drawable.received_audio_anim);
            }

            final AnimationDrawable frameAnimatio = (AnimationDrawable) viewholder.audio_iv.getBackground();


            if (isMask == 1) {

                if (viewholder.mask_vs != null) {
                    viewholder.mask_vs.setVisibility(View.VISIBLE);
                }

                if (viewholder.no_mask_layout != null) {
                    viewholder.no_mask_layout.setVisibility(View.GONE);
                }

                LinearLayout chat_item_mask_layout = (LinearLayout) convertView.findViewById(R.id.chat_item_mask_layout);

                chat_item_mask_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        mOnItemBrokenkeyClickLitener.onItemBrokenkeyClick();
                    }
                });


            } else {

                if (viewholder.mask_vs != null) {
                    viewholder.mask_vs.setVisibility(View.GONE);
                }

                if (viewholder.no_mask_layout != null) {
                    viewholder.no_mask_layout.setVisibility(View.VISIBLE);
                }


                viewholder.audio_timelenth_tv.setText(audioMsg.getDuration() + "");

                int width = DensityUtil.dip2px(mContext, 100) * audioMsg.getDuration() / 60 + 200;

                LayoutParams para = viewholder.audio_layout.getLayoutParams();

                para.width = width;

                viewholder.audio_layout.setLayoutParams(para);

                viewholder.audio_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        playMedia(frameAnimatio, audioMsg.getElem());

                    }
                });
            }
        }
        if (viewholder.send_fail_iv != null) {
            viewholder.send_fail_iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    ViewsUtils.preventViewMultipleClick(arg0, 1000);
                    mOnItemSendFailClickLitener.onItemSendFailClick(chatdto);
                }
            });
        }

        return convertView;

    }

    private void navToImageview(final String path, final String url) {

        Intent intent = new Intent(mContext, ChatImageShowActivity.class);
        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(path) && ImgUtils.fileIsExists(path)) {// 不为空时，存在

            bundle.putString(YpSettings.CHAT_IMAGE_URL, "file://" + path);

        } else {// 不存在

            bundle.putString(YpSettings.CHAT_IMAGE_URL, url);
        }

        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }


    public void hideSoftInputView(Activity activity) {
        InputMethodManager manager = ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setData(List<ChatDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<ChatDto> getDatas() {
        return list;
    }


    public void setKeyBroken(int key_Broken) {
        this.keyBroken = key_Broken;
    }

    public void setMeIsActive(int meIsActive) {
        this.isActive = meIsActive;
    }


    void playMedia(final AnimationDrawable frameAnimatio, TIMSoundElem elem) {

        File tempAudio = new File(elem.getPath());

        if (tempAudio.exists()) {

            try {
                FileInputStream fis = new FileInputStream(tempAudio);
                MediaUtil.getInstance().play(fis);
                frameAnimatio.start();
                MediaUtil.getInstance().setEventListener(new MediaUtil.EventListener() {
                    @Override
                    public void onStop() {
                        frameAnimatio.stop();
                        frameAnimatio.selectDrawable(0);
                    }
                });
            } catch (IOException e) {

            }

            return;
        }


        elem.getSound(new TIMValueCallBack<byte[]>() {
            @Override
            public void onError(int i, String s) {
                Logger.e("一场了==＝" + s);
            }

            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    File tempAudio = FileUtil.getTempFile(FileUtil.FileType.AUDIO);
                    FileOutputStream fos = new FileOutputStream(tempAudio);
                    fos.write(bytes);
                    fos.close();
                    FileInputStream fis = new FileInputStream(tempAudio);
                    MediaUtil.getInstance().play(fis);
                    frameAnimatio.start();
                    MediaUtil.getInstance().setEventListener(new MediaUtil.EventListener() {
                        @Override
                        public void onStop() {
                            frameAnimatio.stop();
                            frameAnimatio.selectDrawable(0);
                        }
                    });
                } catch (IOException e) {

                    Logger.e("一场了＝" + e.getMessage().toString());
                }
            }
        });

    }

}