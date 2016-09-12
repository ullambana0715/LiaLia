package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingInfoStateDto;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingStatusWithTargetBean;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingStatusWithTargetRespBean;
import cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser.DatingStatusWithTargetService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserInfoID.UserInfoIDBean;
import cn.chono.yopper.Service.Http.UserInfoID.UserInfoIDRespBean;
import cn.chono.yopper.Service.Http.UserInfoID.UserInfoIDService;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BaseUser;
import cn.chono.yopper.data.KeyTable;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.MessageDto;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.NotificationMsg;
import cn.chono.yopper.data.Profile;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserToUserWithDatingTable;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DatingUtils;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;

public class MessageAdapter extends BaseAdapter {


    /**
     * 这个用来填充list
     */
    private List<MessageDto> list;

    private Context mContext;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    public MessageAdapter(Context context) {
        // 初始化
        this.mContext = context;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public MessageAdapter(Context context, List<MessageDto> list) {
        // 初始化
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list == null ? 0 : list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        MessageDto dto = list.get(position);

        String message = dto.getLastmessage();
        // 如果没有设置过,初始化convertView

        if (convertView == null) {
            // 获得设置的view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, null);

            // 初始化holder
            holder = new ViewHolder();


            holder.icon_img_iv = (ImageView) convertView.findViewById(R.id.message_item_icon_img_iv);

            holder.name_tv = (TextView) convertView.findViewById(R.id.message_item_name_tv);

            holder.time_tv = (TextView) convertView.findViewById(R.id.message_item_time_tv);

            holder.content_tv = (TextView) convertView.findViewById(R.id.message_item_content_tv);

            holder.num_tv = (TextView) convertView.findViewById(R.id.message_item_num_tv);

            holder.dating_info_tv = (TextView) convertView.findViewById(R.id.message_item_dating_info_tv);

            holder.dating_deal_layout = (LinearLayout) convertView.findViewById(R.id.message_item_dating_deal_layout);

            holder.dating_deal_msg_layout = (LinearLayout) convertView.findViewById(R.id.message_item_dating_deal_msg_layout);

            convertView.setTag(holder);
        } else {
            // 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        if (dto.getNo_read_num() > 0) {
            if (dto.getNo_read_num() < 10) {
                holder.num_tv.setBackgroundResource(R.drawable.circle_messaga_num_bg);
            } else {
                holder.num_tv.setBackgroundResource(R.drawable.center_messaga_num_bg);
            }
            holder.num_tv.setVisibility(View.VISIBLE);
            holder.num_tv.setText(dto.getNo_read_num() + "");
        } else {
            holder.num_tv.setVisibility(View.GONE);
        }


        String type = ChatUtils.getMsgType(message);

        String jid = dto.getJid();

        if (TextUtils.equals(type, MessageType.Notification)) {

            holder.dating_deal_msg_layout.setVisibility(View.GONE);

            NotificationMsg notificationMsg = JsonUtils.fromJson(message, NotificationMsg.class);

            String from = notificationMsg.getFrom();
            String text = notificationMsg.getText();
            String content = notificationMsg.getContent();
            String title = notificationMsg.getTitle();

            if (!CheckUtil.isEmpty(from)) {
                holder.name_tv.setText(from);
            }


            if (!CheckUtil.isEmpty(text)) {

                text = title + ":" + text;

                if (!TextUtils.isEmpty(title) && text.contains(title)) {

                    int fstart = text.indexOf(title);

                    int fend = fstart + title.length();

                    SpannableStringBuilder style = new SpannableStringBuilder(text);

                    style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_999999)), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                    holder.dating_info_tv.setText(style);

                } else {

                    holder.dating_info_tv.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));

                    holder.dating_info_tv.setText(text);
                }


            } else {

                if (!CheckUtil.isEmpty(content)) {

                    content = title + ":" + content;

                    if (!TextUtils.isEmpty(title) && content.contains(title)) {

                        int fstart = text.indexOf(title);

                        int fend = fstart + title.length();

                        SpannableStringBuilder style = new SpannableStringBuilder(content);

                        style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_999999)), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                        holder.dating_info_tv.setText(style);

                    } else {

                        holder.dating_info_tv.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));

                        holder.dating_info_tv.setText(content);
                    }

                }

            }

            String imageurl = "http://www.yopper.cn/static/message/icons/" + jid + ".png";

            Logger.e("imageurl=" + imageurl);

            Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(holder.icon_img_iv);

        } else {

            int resource = dto.getResource();

            if (resource == MessageDto.resource_counsel) {

                holder.dating_deal_msg_layout.setVisibility(View.GONE);


                String content = ChatUtils.getMsgcontent(message);

                holder.dating_info_tv.setText(content + "");

                holder.name_tv.setText("咨询消息");

                Glide.with(mContext).load(R.drawable.ic_message_counsel).bitmapTransform(transformation).into(holder.icon_img_iv);

            } else {

                if (resource == MessageDto.resource_system) {

                    holder.dating_deal_msg_layout.setVisibility(View.GONE);

                    String content = ChatUtils.getMsgcontent(message);

                    holder.dating_info_tv.setText(content + "");


                } else {


                    holder.content_tv.setTextColor(mContext.getResources().getColor(R.color.color_9a9a9a));

                    holder.dating_deal_msg_layout.setVisibility(View.VISIBLE);

                    String datingId = dto.getDatingId();
                    String mid = dto.getMid();
                    int datingHandleStatus = 0;

                    UserToUserWithDatingTable datingTable = ChatUtils.getDatingTable(mid, jid, datingId);

                    int mask = ChatUtils.getMask(dto.getLastmessage());

                    KeyTable keyTable = ChatUtils.getKeyRecord(mid, jid);

                    int isBrokenKey = KeyTable.no_broken;
                    if (keyTable != null) {
                        isBrokenKey = keyTable.getIsBrokenKey();
                    }

                    if (datingTable != null) {
                        datingHandleStatus = datingTable.getDatingDealStatus();

                        String dating_info_str = datingTable.getDatingTheme();

                        holder.dating_info_tv.setText(dating_info_str);

                        int meIsActive = datingTable.getMeIsActive();

                        if (meIsActive == UserToUserWithDatingTable.meActive) {

                            holder.dating_deal_layout.setVisibility(View.GONE);
                            holder.content_tv.setVisibility(View.VISIBLE);

                            if (isBrokenKey == 1) {

                                String content = ChatUtils.getMsgcontent(message);
                                holder.content_tv.setText(content);

                            } else {
                                if (mask == 1) {
                                    holder.content_tv.setText("给您发来了一条消息  点击查看>>");
                                    holder.content_tv.setTextColor(mContext.getResources().getColor(R.color.color_9cbbbb));
                                } else {
                                    String content = ChatUtils.getMsgcontent(message);
                                    holder.content_tv.setText(content);
                                }
                            }

                        } else {

                            if (datingHandleStatus == UserToUserWithDatingTable.status_delay || datingHandleStatus == 0 || datingHandleStatus == 1) {
                                holder.dating_deal_layout.setVisibility(View.VISIBLE);
                                holder.content_tv.setVisibility(View.GONE);
                            } else {
                                holder.dating_deal_layout.setVisibility(View.GONE);
                                holder.content_tv.setVisibility(View.VISIBLE);
                            }

                            String content = ChatUtils.getMsgcontent(message);
                            holder.content_tv.setText(content);

                        }


                    } else {
                        getDatingHandleStatusInfo(Integer.valueOf(jid), datingId, message, holder.dating_deal_layout, holder.dating_info_tv, holder.content_tv, mask, isBrokenKey);
                    }

                }

                try {
                    UserInfo userInfodto = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", jid));


                    if (userInfodto != null) {
                        UserDto userdto = JsonUtils.fromJson(userInfodto.getResp(), UserDto.class);
                        holder.name_tv.setText(userdto.getProfile().getName());

                        String imageurl = ImgUtils.DealImageUrl(userdto.getProfile().getHeadImg(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

                        Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(holder.icon_img_iv);

                    } else {

                        if (!TextUtils.equals(jid, "chatadmin")) {
                            getUserWithID(jid, holder.icon_img_iv, holder.name_tv, resource);
                        }


                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

            }

        }


        // 时间设置
        holder.time_tv.setText(ChatUtils.msgTimeFormat(dto.getLasttime()) + "");

        return convertView;
    }


    /**
     * ViewHolder
     *
     * @Title:
     * @Description:主要是避免了不断的view获取初始化.
     * @Author:justlcw
     * @Since:2013-11-22
     * @Version:
     */
    class ViewHolder {

        public ImageView icon_img_iv;
        public TextView name_tv;
        public TextView time_tv;
        public TextView content_tv;
        public TextView num_tv;

        public TextView dating_info_tv;
        public LinearLayout dating_deal_layout;
        public LinearLayout dating_deal_msg_layout;

    }


    public List<MessageDto> getList() {
        return list;
    }

    public void setList(List<MessageDto> addlist) {
        list = new ArrayList<MessageDto>();
        list.addAll(addlist);
        super.notifyDataSetChanged();
    }


    private void getDatingHandleStatusInfo(final int userid, final String datingId, final String message, final LinearLayout dating_deal_layout, final TextView dating_info_tv, final TextView content_tv, final int mask, final int isBrokenKey) {


        DatingStatusWithTargetBean bean = new DatingStatusWithTargetBean();
        bean.setDatingId(datingId);
        bean.setOtherUserId(userid);
        DatingStatusWithTargetService infoIDService = new DatingStatusWithTargetService(mContext);

        infoIDService.parameter(bean);

        infoIDService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DatingStatusWithTargetRespBean idRespBean = (DatingStatusWithTargetRespBean) respBean;

                DatingInfoStateDto datingInfoStateDto = idRespBean.getResp();

                int datingHandleStatus = datingInfoStateDto.getChatState();

                String publishDate_userId = datingInfoStateDto.getDating().getOwner().getUserId() + "";

                String dating_info_str = DatingUtils.getChatDatingTitle(datingInfoStateDto.getDating());

                dating_info_tv.setText(dating_info_str);
                int reply = 0;

                int meIsActive = 0;
                if (datingInfoStateDto.isCurrentUserLauncher()) {
                    meIsActive = UserToUserWithDatingTable.meActive;
                    reply = UserToUserWithDatingTable.replyed;
                } else {
                    meIsActive = UserToUserWithDatingTable.me_no_Active;
                    reply = UserToUserWithDatingTable.no_reply;
                }

                if (meIsActive == UserToUserWithDatingTable.meActive) {

                    dating_deal_layout.setVisibility(View.GONE);
                    content_tv.setVisibility(View.VISIBLE);

                    if (isBrokenKey == 1) {

                        String content = ChatUtils.getMsgcontent(message);
                        content_tv.setText(content);

                    } else {
                        if (mask == 1) {
                            content_tv.setText("给您发来了一条消息  点击查看>>");
                            content_tv.setTextColor(mContext.getResources().getColor(R.color.color_9cbbbb));
                        } else {
                            String content = ChatUtils.getMsgcontent(message);
                            content_tv.setText(content);
                        }
                    }

                } else {

                    if (datingHandleStatus == UserToUserWithDatingTable.status_delay || datingHandleStatus == 0 || datingHandleStatus == 1) {
                        dating_deal_layout.setVisibility(View.VISIBLE);
                        content_tv.setVisibility(View.GONE);
                    } else {
                        dating_deal_layout.setVisibility(View.GONE);
                        content_tv.setVisibility(View.VISIBLE);
                    }

                    if (datingHandleStatus != 0 && datingHandleStatus != 1) {
                        reply = UserToUserWithDatingTable.replyed;
                    } else {
                        reply = UserToUserWithDatingTable.no_reply;
                    }

                    String content = ChatUtils.getMsgcontent(message);
                    content_tv.setText(content);

                }


                ChatUtils.saveOrUpdateDatingStatusTable(userid + "", datingId, dating_info_str, meIsActive, reply, datingHandleStatus, publishDate_userId);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                dating_info_tv.setText("邀约");

                dating_deal_layout.setVisibility(View.GONE);
                content_tv.setVisibility(View.VISIBLE);

                String content = ChatUtils.getMsgcontent(message);
                content_tv.setText(content);


            }
        });

        infoIDService.enqueue();
    }


    private void getUserWithID(final String id, final ImageView head_img, final TextView name_tv, final int resource) {


        UserInfoIDBean infoIDBean = new UserInfoIDBean();

        infoIDBean.setUserId(Integer.valueOf(id));

        UserInfoIDService infoIDService = new UserInfoIDService(mContext);

        infoIDService.parameter(infoIDBean);

        infoIDService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UserInfoIDRespBean idRespBean = (UserInfoIDRespBean) respBean;

                BaseUser baseUser = idRespBean.getResp();
                try {
                    if (resource != MessageDto.resource_counsel) {
                        name_tv.setText(baseUser.getName());
                        String imageurl = ImgUtils.DealImageUrl(baseUser.getHeadImg(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
                        Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(head_img);
                    } else {

                        head_img.setBackgroundResource(R.drawable.icon_strange_date);
                        name_tv.setText("咨询");
                    }


                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(baseUser.getId());
                    UserDto userDto = new UserDto();
                    Profile profile = new Profile();
                    profile.setName(baseUser.getName());
                    profile.setId(baseUser.getId());
                    profile.setHeadImg(baseUser.getHeadImg());
                    profile.setHoroscope(baseUser.getHoroscope());
                    profile.setSex(baseUser.getSex());
                    userDto.setProfile(profile);
                    String str = JsonUtils.toJson(userDto);
                    userInfo.setResp(str);

                    App.getInstance().db.save(userInfo);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                if (resource != MessageDto.resource_counsel) {
                    name_tv.setText("未知");
                } else {
                    head_img.setBackgroundResource(R.drawable.ic_message_counsel);
                    name_tv.setText("咨询");
                }


            }
        });

        infoIDService.enqueue();
    }

}
