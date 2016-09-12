package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.InfoTransformUtils;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/7/22.
 */
public class UIAppointmentAdapter<T> extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    Context mContext;

    List<T> list = new ArrayList<>();

    final int TYPENOAPP = 0;

    final int TYPE = 1;


    int loginUserId;

    int userID;

    int sex;


    public void setList(List<T> list, int loginUserId, int userID, int sex) {
        this.list = list;

        this.loginUserId = loginUserId;

        this.userID = userID;


        this.sex = sex;

        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        mContext = parent.getContext();

        int w = UnitUtil.getScreenWidthPixels(mContext);

        if (TYPENOAPP == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_no_datings, parent, false);

            view.getLayoutParams().width = w;

            return new NoViewHolder(view);

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_datings, parent, false);

        view.getLayoutParams().width = w - 150;

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, boolean isItem) {

        Logger.e("l=" + loginUserId);

        Logger.e("u=" + userID);


        if (holder instanceof NoViewHolder) {


            if (loginUserId != userID) {

                ((NoViewHolder) holder).mItemUserInfoNoDatingHintTv.setText("他还没有发布任何邀约");

                ((NoViewHolder) holder).mItemUserInfoNoDatingInvitePublishTv.setText("邀请Ta参加我的邀约");

            } else {

                ((NoViewHolder) holder).mItemUserInfoNoDatingHintTv.setText("您还没有发布任何邀约");

                ((NoViewHolder) holder).mItemUserInfoNoDatingInvitePublishTv.setText("马上发起");

            }

            ((NoViewHolder) holder).mItemUserInfoNoDatingInvitePublishTv.setOnClickListener(view -> {

                CommonItemEvent commonItemEvent = new CommonItemEvent();

                if (userID == loginUserId) {

                    //发布邀约

                    RxBus.get().post("ItemUserInfoDatingCanPublishDatingLogin", commonItemEvent);

                } else {


                    //邀请他参加我的邀约

                    RxBus.get().post("ItemUserInfoDatingCanPublishDating", commonItemEvent);


                }


            });
            return;
        }


        if (holder instanceof ItemViewHolder) {

            AppointDetailDto appointDetailDto = (AppointDetailDto) list.get(position);

            if (position == list.size() - 1) {

                ((ItemViewHolder) holder).mItemUserInfoSpaceRight.setVisibility(View.VISIBLE);

            } else {

                ((ItemViewHolder) holder).mItemUserInfoSpaceRight.setVisibility(View.GONE);

            }


            if (loginUserId != userID) {


                ((ItemViewHolder) holder).mItemUserInfoDatingTvInterestedTalk.setVisibility(View.VISIBLE);

                final boolean isExist = DbHelperUtils.isExistChatRecordWithDating(loginUserId + "", userID + "", appointDetailDto.getDatingId());

                if (isExist) {

                    ((ItemViewHolder) holder).mItemUserInfoDatingTvInterestedTalk.setText("打开聊天");

                } else {

                    ((ItemViewHolder) holder).mItemUserInfoDatingTvInterestedTalk.setText("感兴趣，先聊聊");
                }

                //感兴趣，先聊聊 btn
                ((ItemViewHolder) holder).mItemUserInfoDatingTvInterestedTalk.setOnClickListener(v -> {

                    CommonItemEvent commonItemEvent = new CommonItemEvent();


                    commonItemEvent.event = appointDetailDto.getDatingId();

                    commonItemEvent.type = appointDetailDto.getActivityType();

                    commonItemEvent.position = isExist;

                    RxBus.get().post("ItemUserInfoDatingInterested", commonItemEvent);

//                    gotoChatOrDetail(appointDetailDto.getDatingId(), appointDetailDto.getActivityType(), isExist);


                });


            } else {


                ((ItemViewHolder) holder).mItemUserInfoDatingTvInterestedTalk.setVisibility(View.GONE);


            }
            ((ItemViewHolder) holder).mItemUserInfoDatingLayout.setOnClickListener(v -> {


                CommonItemEvent commonItemEvent = new CommonItemEvent();


                commonItemEvent.event = appointDetailDto.getDatingId();

                commonItemEvent.type = appointDetailDto.getActivityType();

                RxBus.get().post("ItemUserInfoDatingInterestedLogin", commonItemEvent);

            });


            String imageurl = "";

            String addressStr = "";

            String timeStr = "";

            String contentStr = "";

            int datingicon = 0;

            switch (appointDetailDto.getActivityType()) {
                case Constant.APPOINT_TYPE_MARRIED:

                    datingicon = R.drawable.dating_married_icon;

                    //对他的寄语
                    ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText(appointDetailDto.getMarriage().getWish());

                    imageurl = appointDetailDto.getMarriage().getPhotoUrl();

                    addressStr = appointDetailDto.getMarriage().getProfession() + " " + InfoTransformUtils.getEducation(appointDetailDto.getMarriage().getEducation());
                    timeStr = InfoTransformUtils.getWishMarriageTime(appointDetailDto.getMarriage().getWishMarriageTime());
                    contentStr = appointDetailDto.getMarriage().getHeight() + "cm" + " " + appointDetailDto.getMarriage().getWeight() + "kg";

                    break;
                case Constant.APPOINT_TYPE_TRAVEL:
                    datingicon = R.drawable.dating_travel_icon;

                    //旅行意义
                    String meaningStr = "";
                    String[] meaningTags = appointDetailDto.getTravel().getMeaningTags();
                    if (meaningTags != null && meaningTags.length > 0) {
                        for (int k = 0; k < meaningTags.length; k++) {
                            if (meaningTags.length == 0) {
                                meaningStr = meaningStr + meaningTags[k];
                            } else {
                                meaningStr = meaningStr + "," + meaningTags[k];
                            }

                        }
                    }


                    if (!CheckUtil.isEmpty(appointDetailDto.getTravel().getAddress())) {

                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText(appointDetailDto.getTravel().getAddress() + " " + meaningStr);

                    } else {

                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText("约人旅行" + " " + meaningStr);
                    }

                    imageurl = appointDetailDto.getTravel().getPhotoUrl();

                    addressStr = "";

                    if (appointDetailDto.getTravel().getMeetingTravelTimeType() == Constant.Travel_Time_Type_Specific_Date) {
                        long time = ISO8601.getTime(appointDetailDto.getTravel().getMeetingTime());
                        timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                    } else {
                        timeStr = InfoTransformUtils.getMeetingTravelTimeType(appointDetailDto.getTravel().getMeetingTravelTimeType());

                    }
                    contentStr = appointDetailDto.getTravel().getDescription();

                    break;
                case Constant.APPOINT_TYPE_OTHERS:

                    datingicon = R.drawable.dating_others_icon;


                    //其他主题
                    ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText(appointDetailDto.getOther().getTheme());
                    imageurl = appointDetailDto.getOther().getPhotoUrl();

                    if (!CheckUtil.isEmpty(appointDetailDto.getOther().getSecondArea())) {
                        addressStr = appointDetailDto.getOther().getFirstArea() + " " + appointDetailDto.getOther().getSecondArea();
                    } else {
                        addressStr = appointDetailDto.getOther().getFirstArea();
                    }

                    if (appointDetailDto.getOther().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                        long time = ISO8601.getTime(appointDetailDto.getOther().getMeetingTime());
                        timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                    } else {
                        timeStr = InfoTransformUtils.getMeetingTime(appointDetailDto.getOther().getMeetingTimeType());

                    }
                    contentStr = appointDetailDto.getOther().getDescription();

                    break;

                case Constant.APPOINT_TYPE_MOVIE:

                    datingicon = R.drawable.dating_movie_icon;


                    //电影名称


                    if (sex != appointDetailDto.getMovie().getTargetSex()) {
                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText("约人看电影 " + appointDetailDto.getMovie().getName());
                    } else {
                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText("看电影" + " " + appointDetailDto.getMovie().getName());
                    }

                    imageurl = appointDetailDto.getMovie().getPhotoUrl();

                    addressStr = appointDetailDto.getMovie().getAddress();

                    if (appointDetailDto.getMovie().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                        long time = ISO8601.getTime(appointDetailDto.getMovie().getMeetingTime());
                        timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                    } else {
                        timeStr = InfoTransformUtils.getMeetingTime(appointDetailDto.getMovie().getMeetingTimeType());

                    }
                    contentStr = appointDetailDto.getMovie().getDescription();

                    break;

                case Constant.APPOINT_TYPE_DOG:

                    datingicon = R.drawable.dating_dog_icon;

                    //犬类别
                    ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText(appointDetailDto.getWalkTheDog().getDogType());
                    imageurl = appointDetailDto.getWalkTheDog().getPhotoUrl();

                    if (!CheckUtil.isEmpty(appointDetailDto.getWalkTheDog().getSecondArea())) {
                        addressStr = appointDetailDto.getWalkTheDog().getFirstArea() + " " + appointDetailDto.getWalkTheDog().getSecondArea();
                    } else {
                        addressStr = appointDetailDto.getWalkTheDog().getFirstArea();
                    }

                    if (appointDetailDto.getWalkTheDog().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                        long time = ISO8601.getTime(appointDetailDto.getWalkTheDog().getMeetingTime());
                        timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                    } else {
                        timeStr = InfoTransformUtils.getMeetingTime(appointDetailDto.getWalkTheDog().getMeetingTimeType());

                    }
                    contentStr = appointDetailDto.getWalkTheDog().getDescription();

                    break;

                case Constant.APPOINT_TYPE_FITNESS:

                    datingicon = R.drawable.dating_fitness_icon;

                    //运动项目
                    ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText(appointDetailDto.getSports().getTheme());
                    imageurl = appointDetailDto.getSports().getPhotoUrl();

                    if (!CheckUtil.isEmpty(appointDetailDto.getSports().getSecondArea())) {
                        addressStr = appointDetailDto.getSports().getFirstArea() + " " + appointDetailDto.getSports().getSecondArea();
                    } else {
                        addressStr = appointDetailDto.getSports().getFirstArea();
                    }

                    if (appointDetailDto.getSports().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                        long time = ISO8601.getTime(appointDetailDto.getSports().getMeetingTime());
                        timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                    } else {
                        timeStr = InfoTransformUtils.getMeetingTime(appointDetailDto.getSports().getMeetingTimeType());

                    }
                    contentStr = appointDetailDto.getSports().getDescription();

                    break;

                case Constant.APPOINT_TYPE_KTV:

                    datingicon = R.drawable.dating_ktv_icon;


                    //约人K歌

                    if (sex != appointDetailDto.getSinging().getTargetSex()) {
                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText("约人K歌");
                    } else {
                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText("K歌");
                    }

                    imageurl = appointDetailDto.getSinging().getPhotoUrl();

                    if (!CheckUtil.isEmpty(appointDetailDto.getSinging().getSecondArea())) {
                        addressStr = appointDetailDto.getSinging().getFirstArea() + " " + appointDetailDto.getSinging().getSecondArea();
                    } else {
                        addressStr = appointDetailDto.getSinging().getFirstArea();
                    }

                    if (appointDetailDto.getSinging().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                        long time = ISO8601.getTime(appointDetailDto.getSinging().getMeetingTime());
                        timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                    } else {
                        timeStr = InfoTransformUtils.getMeetingTime(appointDetailDto.getSinging().getMeetingTimeType());

                    }
                    contentStr = appointDetailDto.getSinging().getDescription();

                    break;

                case Constant.APPOINT_TYPE_EAT:

                    datingicon = R.drawable.dating_eat_icon;


                    //约人吃饭

                    if (sex != appointDetailDto.getDine().getTargetSex()) {

                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText("约人吃饭");

                    } else {

                        ((ItemViewHolder) holder).mItemUserInfoDatingTitleTv.setText("吃美食");

                    }
                    imageurl = appointDetailDto.getDine().getPhotoUrl();

                    Logger.e("约人吃饭＝" + imageurl);


                    if (!CheckUtil.isEmpty(appointDetailDto.getDine().getSecondArea())) {

                        addressStr = appointDetailDto.getDine().getFirstArea() + " " + appointDetailDto.getDine().getSecondArea();
                    } else {
                        addressStr = appointDetailDto.getDine().getFirstArea();
                    }


                    if (appointDetailDto.getDine().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {

                        long time = ISO8601.getTime(appointDetailDto.getDine().getMeetingTime());

                        timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());

                    } else {

                        timeStr = InfoTransformUtils.getMeetingTime(appointDetailDto.getDine().getMeetingTimeType());

                    }
                    contentStr = appointDetailDto.getDine().getDescription();

                    break;
            }

            ((ItemViewHolder) holder).mItemUserInfoDatingTypeIv.setBackgroundResource(datingicon);


            if (appointDetailDto.getActivityType() == Constant.APPOINT_TYPE_MARRIED) {

                ((ItemViewHolder) holder).mItemUserInfoDatingAddressIv.setBackgroundResource(R.drawable.white_bg_ff_circle);

                ((ItemViewHolder) holder).mItemUserInfoDatingTimeIv.setBackgroundResource(R.drawable.white_bg_ff_circle);

            } else if (appointDetailDto.getActivityType() == Constant.APPOINT_TYPE_TRAVEL) {

                ((ItemViewHolder) holder).mItemUserInfoDatingAddressIv.setBackgroundResource(R.drawable.white_bg_ff_circle);

                addressStr = contentStr;

                contentStr = "";

            } else {

                ((ItemViewHolder) holder).mItemUserInfoDatingAddressIv.setBackgroundResource(R.drawable.appoint_item_address_icon);

                ((ItemViewHolder) holder).mItemUserInfoDatingTimeIv.setBackgroundResource(R.drawable.appoint_item_time_icon);
            }


            if (!CheckUtil.isEmpty(addressStr)) {
                ((ItemViewHolder) holder).mItemUserInfoDatingAddressIv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).mItemUserInfoDatingAddressTv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).mItemUserInfoDatingAddressTv.setText(addressStr);
            } else {
                ((ItemViewHolder) holder).mItemUserInfoDatingAddressIv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).mItemUserInfoDatingAddressTv.setVisibility(View.GONE);
            }


            if (!CheckUtil.isEmpty(timeStr)) {
                ((ItemViewHolder) holder).mItemUserInfoDatingTimeIv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).mItemUserInfoDatingTimeTv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).mItemUserInfoDatingTimeTv.setText(timeStr);
            } else {
                ((ItemViewHolder) holder).mItemUserInfoDatingTimeIv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).mItemUserInfoDatingTimeTv.setVisibility(View.GONE);
            }

            if (!CheckUtil.isEmpty(contentStr)) {
                ((ItemViewHolder) holder).mItemUserInfoDatingContentIv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).mItemUserInfoDatingContentTv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).mItemUserInfoDatingContentTv.setText(contentStr);
            } else {
                ((ItemViewHolder) holder).mItemUserInfoDatingContentIv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).mItemUserInfoDatingContentTv.setVisibility(View.GONE);
            }


            if (!CheckUtil.isEmpty(imageurl)) {

                ((ItemViewHolder) holder).mItemUserInfoDatingImageIv.setVisibility(View.VISIBLE);

                imageurl = ImgUtils.DealImageUrl(imageurl, 150, 150);

                Glide.with(mContext).load(imageurl).into(((ItemViewHolder) holder).mItemUserInfoDatingImageIv);

            } else {
                ((ItemViewHolder) holder).mItemUserInfoDatingImageIv.setVisibility(View.GONE);
            }


            return;
        }


    }

    @Override
    public int getAdapterItemCount() {

        return (list == null || list.size() == 0) ? 1 : list.size();
    }


    @Override
    public int getAdapterItemViewType(int position) {

        if (list == null || list.size() == 0)

            return TYPENOAPP;

        else
            return TYPE;

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_user_info_space)
        Space mItemUserInfoSpace;

        @BindView(R.id.item_user_info_space_right)
        Space mItemUserInfoSpaceRight;

        @BindView(R.id.item_user_info_dating_type_iv)
        ImageView mItemUserInfoDatingTypeIv;

        @BindView(R.id.item_user_info_dating_title_tv)
        TextView mItemUserInfoDatingTitleTv;

        @BindView(R.id.item_user_info_dating_address_iv)
        ImageView mItemUserInfoDatingAddressIv;

        @BindView(R.id.item_user_info_dating_address_tv)
        TextView mItemUserInfoDatingAddressTv;

        @BindView(R.id.item_user_info_dating_time_iv)
        ImageView mItemUserInfoDatingTimeIv;

        @BindView(R.id.item_user_info_dating_time_tv)
        TextView mItemUserInfoDatingTimeTv;

        @BindView(R.id.item_user_info_dating_content_iv)
        ImageView mItemUserInfoDatingContentIv;

        @BindView(R.id.item_user_info_dating_content_tv)
        TextView mItemUserInfoDatingContentTv;

        @BindView(R.id.item_user_info_dating_image_iv)
        ImageView mItemUserInfoDatingImageIv;


        @BindView(R.id.item_user_info_dating_tv_interested_talk)
        TextView mItemUserInfoDatingTvInterestedTalk;

        @BindView(R.id.item_user_info_dating_layout)
        LinearLayout mItemUserInfoDatingLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class NoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_user_info_no_dating_hint_tv)
        TextView mItemUserInfoNoDatingHintTv;
        @BindView(R.id.item_user_info_no_dating_invite_publish_tv)
        TextView mItemUserInfoNoDatingInvitePublishTv;


        public NoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
