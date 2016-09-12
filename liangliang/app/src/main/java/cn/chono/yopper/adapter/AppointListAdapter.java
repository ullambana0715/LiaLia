package cn.chono.yopper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AppointmentDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.InfoTransformUtils;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.view.ProgressBarView;

public class AppointListAdapter extends BaseRecyclerAdapter<AppointListAdapter.ViewHolder> {

    private List<AppointmentDto> list;


    private Context mContext;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public AppointListAdapter(Context context, List<AppointmentDto> list) {
        this.mContext = context;
        this.list = list;

        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);

    }


    @Override
    public ViewHolder getViewHolder(View view) {


        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType, boolean isItem) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.appoint_list_item_layout, viewGroup, false);
        return new ViewHolder(v, true);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i, boolean isItem) {

        // 给ViewHolder设置元素
        AppointmentDto dto = list.get(i);


        if (!CheckUtil.isEmpty(dto.getOwner().getName())) {
            viewHolder.dating_list_item_name_tv.setText(dto.getOwner().getName());
        }

        //性别：男
        if (dto.getOwner().getSex() == 1) {
            Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_man);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            viewHolder.dating_list_item_age_tv.setCompoundDrawables(sexDrawable, null, null, null);
            viewHolder.dating_list_item_age_tv.setTextColor(mContext.getResources().getColor(R.color.color_8cd2ff));
        } else {
            Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_woman);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            viewHolder.dating_list_item_age_tv.setCompoundDrawables(sexDrawable, null, null, null);
            viewHolder.dating_list_item_age_tv.setTextColor(mContext.getResources().getColor(R.color.color_fe8cd9));
        }

        if (dto.getOwner().isBirthdayPrivacy() || dto.getOwner().getAge() == 0) {
            viewHolder.dating_list_item_age_tv.setText("-");
        } else {
            viewHolder.dating_list_item_age_tv.setText(dto.getOwner().getAge() + "");
        }


        if (!dto.getOwner().isVideoVerification()) {
            viewHolder.dating_list_item_video_lable_iv.setVisibility(View.GONE);
        } else {
            viewHolder.dating_list_item_video_lable_iv.setVisibility(View.VISIBLE);
        }

        if (!dto.getOwner().isHot()) {
            viewHolder.dating_list_item_hot_iv.setVisibility(View.GONE);
        } else {
            viewHolder.dating_list_item_hot_iv.setVisibility(View.VISIBLE);
        }

        if (!dto.getOwner().isActivityExpert()) {
            viewHolder.dating_list_item_activity_talent_iv.setVisibility(View.GONE);
        } else {
            viewHolder.dating_list_item_activity_talent_iv.setVisibility(View.VISIBLE);
        }


        switch (dto.getOwner().getCurrentUserPosition()) {
            case 0:
                //不是VIP
                viewHolder.dating_list_item_vip_iv.setVisibility(View.GONE);
                break;

            case 1:
                //白银VIP
                viewHolder.dating_list_item_vip_iv.setVisibility(View.VISIBLE);
                viewHolder.dating_list_item_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_silver);
                break;

            case 2:
                //黄金VIP
                viewHolder.dating_list_item_vip_iv.setVisibility(View.VISIBLE);
                viewHolder.dating_list_item_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_gold);
                break;

            case 3:
                //铂金VIP
                viewHolder.dating_list_item_vip_iv.setVisibility(View.VISIBLE);
                viewHolder.dating_list_item_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                break;

            case 4:
                //钻石VIP
                viewHolder.dating_list_item_vip_iv.setVisibility(View.VISIBLE);
                viewHolder.dating_list_item_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                break;

        }

        if (!CheckUtil.isEmpty(dto.getOwner().getHeadImg())) {
            String imageurl = ImgUtils.DealImageUrl(dto.getOwner().getHeadImg(), 150, 150);
            Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(viewHolder.dating_list_item_userImg_iv);
        }

        String imageurl = "";

        String addressStr = "";
        String timeStr = "";
        String contentStr = "";

        switch (dto.getActivityType()) {
            case Constant.APPOINT_TYPE_MARRIED:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_married_icon);
                //对他的寄语
                viewHolder.dating_list_item_title_tv.setText(dto.getMarriage().getWish());
                imageurl = dto.getMarriage().getPhotoUrl();


                StringBuilder sbMarriage = new StringBuilder();

                if (!TextUtils.isEmpty(dto.getMarriage().getProfession())) {
                    sbMarriage.append(dto.getMarriage().getProfession());
                }

                if (!TextUtils.isEmpty(InfoTransformUtils.getEducation(dto.getMarriage().getEducation()))) {

                    sbMarriage.append(" ");
                    sbMarriage.append(InfoTransformUtils.getEducation(dto.getMarriage().getEducation()));
                }

                addressStr = sbMarriage.toString();


                timeStr = InfoTransformUtils.getWishMarriageTime(dto.getMarriage().getWishMarriageTime());
                contentStr = dto.getMarriage().getHeight() + "cm" + " " + dto.getMarriage().getWeight() + "kg";

                break;
            case Constant.APPOINT_TYPE_TRAVEL:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_travel_icon);
                //旅行意义
                String meaningStr = "";
                String[] meaningTags = dto.getTravel().getMeaningTags();
                if (meaningTags != null && meaningTags.length > 0) {
                    for (int k = 0; k < meaningTags.length; k++) {
                        if (meaningTags.length == 0) {
                            meaningStr = meaningStr + meaningTags[k];
                        } else {
                            meaningStr = meaningStr + "," + meaningTags[k];
                        }

                    }
                }


                if (!CheckUtil.isEmpty(dto.getTravel().getAddress())) {
                    viewHolder.dating_list_item_title_tv.setText(dto.getTravel().getAddress() + " " + meaningStr);

                } else {

                    viewHolder.dating_list_item_title_tv.setText("约人旅行" + " " + meaningStr);
                }

                imageurl = dto.getTravel().getPhotoUrl();

                addressStr = "";

                if (dto.getTravel().getMeetingTravelTimeType() == Constant.Travel_Time_Type_Specific_Date) {
                    long time = ISO8601.getTime(dto.getTravel().getMeetingTime());
                    timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                } else {
                    timeStr = InfoTransformUtils.getMeetingTravelTimeType(dto.getTravel().getMeetingTravelTimeType());

                }
                contentStr = dto.getTravel().getDescription();

                break;
            case Constant.APPOINT_TYPE_OTHERS:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_others_icon);
                //其他主题
                viewHolder.dating_list_item_title_tv.setText(dto.getOther().getTheme());
                imageurl = dto.getOther().getPhotoUrl();


                StringBuilder sbOther = new StringBuilder();

                if (!TextUtils.isEmpty(dto.getOther().getFirstArea())) {
                    sbOther.append(dto.getOther().getFirstArea());
                }

                if (!TextUtils.isEmpty(dto.getOther().getSecondArea())) {

                    sbOther.append(" ");
                    sbOther.append(dto.getOther().getSecondArea());
                }

                addressStr = sbOther.toString();


                if (dto.getOther().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                    long time = ISO8601.getTime(dto.getOther().getMeetingTime());
                    timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                } else {
                    timeStr = InfoTransformUtils.getMeetingTime(dto.getOther().getMeetingTimeType());

                }
                contentStr = dto.getOther().getDescription();

                break;

            case Constant.APPOINT_TYPE_MOVIE:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_movie_icon);
                //电影名称
                int mysex_movie = dto.getOwner().getSex();

                if (mysex_movie != dto.getMovie().getTargetSex()) {
                    viewHolder.dating_list_item_title_tv.setText("约人看电影 " + dto.getMovie().getName());
                } else {
                    viewHolder.dating_list_item_title_tv.setText("看电影" + " " + dto.getMovie().getName());
                }

                imageurl = dto.getMovie().getPhotoUrl();

                addressStr = dto.getMovie().getAddress();

                if (dto.getMovie().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                    long time = ISO8601.getTime(dto.getMovie().getMeetingTime());
                    timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                } else {
                    timeStr = InfoTransformUtils.getMeetingTime(dto.getMovie().getMeetingTimeType());

                }
                contentStr = dto.getMovie().getDescription();

                break;

            case Constant.APPOINT_TYPE_DOG:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_dog_icon);
                //犬类别
                viewHolder.dating_list_item_title_tv.setText(dto.getWalkTheDog().getDogType());
                imageurl = dto.getWalkTheDog().getPhotoUrl();


                StringBuilder sbWalkTheDog = new StringBuilder();

                if (!TextUtils.isEmpty(dto.getWalkTheDog().getFirstArea())) {
                    sbWalkTheDog.append(dto.getWalkTheDog().getFirstArea());
                }

                if (!TextUtils.isEmpty(dto.getWalkTheDog().getSecondArea())) {

                    sbWalkTheDog.append(" ");
                    sbWalkTheDog.append(dto.getWalkTheDog().getSecondArea());
                }

                addressStr = sbWalkTheDog.toString();


                if (dto.getWalkTheDog().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                    long time = ISO8601.getTime(dto.getWalkTheDog().getMeetingTime());
                    timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                } else {
                    timeStr = InfoTransformUtils.getMeetingTime(dto.getWalkTheDog().getMeetingTimeType());

                }
                contentStr = dto.getWalkTheDog().getDescription();

                break;

            case Constant.APPOINT_TYPE_FITNESS:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_fitness_icon);
                //运动项目
                viewHolder.dating_list_item_title_tv.setText(dto.getSports().getTheme());
                imageurl = dto.getSports().getPhotoUrl();


                StringBuilder sbSports = new StringBuilder();

                if (!TextUtils.isEmpty(dto.getSports().getFirstArea())) {
                    sbSports.append(dto.getSports().getFirstArea());
                }

                if (!TextUtils.isEmpty(dto.getSports().getSecondArea())) {

                    sbSports.append(" ");
                    sbSports.append(dto.getSports().getSecondArea());
                }

                addressStr = sbSports.toString();


                if (dto.getSports().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                    long time = ISO8601.getTime(dto.getSports().getMeetingTime());
                    timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                } else {
                    timeStr = InfoTransformUtils.getMeetingTime(dto.getSports().getMeetingTimeType());

                }
                contentStr = dto.getSports().getDescription();

                break;

            case Constant.APPOINT_TYPE_KTV:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_ktv_icon);
                //约人K歌
                int mysex_ktv = dto.getOwner().getSex();

                if (mysex_ktv != dto.getSinging().getTargetSex()) {
                    viewHolder.dating_list_item_title_tv.setText("约人K歌");
                } else {
                    viewHolder.dating_list_item_title_tv.setText("K歌");
                }

                imageurl = dto.getSinging().getPhotoUrl();


                StringBuilder sbSinging = new StringBuilder();

                if (!TextUtils.isEmpty(dto.getSinging().getFirstArea())) {
                    sbSinging.append(dto.getSinging().getFirstArea());
                }

                if (!TextUtils.isEmpty(dto.getSinging().getSecondArea())) {

                    sbSinging.append(" ");
                    sbSinging.append(dto.getSinging().getSecondArea());
                }

                addressStr = sbSinging.toString();


                if (dto.getSinging().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                    long time = ISO8601.getTime(dto.getSinging().getMeetingTime());
                    timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                } else {
                    timeStr = InfoTransformUtils.getMeetingTime(dto.getSinging().getMeetingTimeType());

                }
                contentStr = dto.getSinging().getDescription();

                break;

            case Constant.APPOINT_TYPE_EAT:
                viewHolder.dating_list_item_type_iv.setBackgroundResource(R.drawable.dating_eat_icon);
                //约人吃饭
                int mysex_eat = dto.getOwner().getSex();

                if (mysex_eat != dto.getDine().getTargetSex()) {

                    viewHolder.dating_list_item_title_tv.setText("约人吃饭");

                } else {

                    viewHolder.dating_list_item_title_tv.setText("吃美食");

                }
                imageurl = dto.getDine().getPhotoUrl();


                StringBuilder sbDine = new StringBuilder();

                if (!TextUtils.isEmpty(dto.getDine().getFirstArea())) {
                    sbDine.append(dto.getDine().getFirstArea());
                }

                if (!TextUtils.isEmpty(dto.getDine().getSecondArea())) {

                    sbDine.append(" ");
                    sbDine.append(dto.getDine().getSecondArea());
                }

                addressStr = sbDine.toString();


                if (dto.getDine().getMeetingTimeType() == Constant.MeetingTime_Type_Select_Time) {
                    long time = ISO8601.getTime(dto.getDine().getMeetingTime());
                    timeStr = TimeUtil.getDateFormatString(time, System.currentTimeMillis());
                } else {
                    timeStr = InfoTransformUtils.getMeetingTime(dto.getDine().getMeetingTimeType());

                }
                contentStr = dto.getDine().getDescription();

                break;
        }


        if (dto.getActivityType() == Constant.APPOINT_TYPE_MARRIED) {

            viewHolder.dating_list_item_address_iv.setBackgroundResource(R.drawable.white_bg_ff_circle);

            viewHolder.dating_list_item_time_iv.setBackgroundResource(R.drawable.white_bg_ff_circle);

        } else if (dto.getActivityType() == Constant.APPOINT_TYPE_TRAVEL) {

            viewHolder.dating_list_item_address_iv.setBackgroundResource(R.drawable.white_bg_ff_circle);

            addressStr = contentStr;
            contentStr = "";

        } else {

            viewHolder.dating_list_item_address_iv.setBackgroundResource(R.drawable.appoint_item_address_icon);

            viewHolder.dating_list_item_time_iv.setBackgroundResource(R.drawable.appoint_item_time_icon);
        }


        if (!CheckUtil.isEmpty(addressStr)) {
            viewHolder.dating_list_item_address_iv.setVisibility(View.VISIBLE);
            viewHolder.dating_list_item_address_tv.setVisibility(View.VISIBLE);
            viewHolder.dating_list_item_address_tv.setText(addressStr);
        } else {
            viewHolder.dating_list_item_address_iv.setVisibility(View.GONE);
            viewHolder.dating_list_item_address_tv.setVisibility(View.GONE);
        }


        if (!CheckUtil.isEmpty(timeStr)) {
            viewHolder.dating_list_item_time_iv.setVisibility(View.VISIBLE);
            viewHolder.dating_list_item_time_tv.setVisibility(View.VISIBLE);
            viewHolder.dating_list_item_time_tv.setText(timeStr);
        } else {
            viewHolder.dating_list_item_time_iv.setVisibility(View.GONE);
            viewHolder.dating_list_item_time_tv.setVisibility(View.GONE);
        }

        if (!CheckUtil.isEmpty(contentStr)) {
            viewHolder.dating_list_item_content_iv.setVisibility(View.VISIBLE);
            viewHolder.dating_list_item_content_tv.setVisibility(View.VISIBLE);
            viewHolder.dating_list_item_content_tv.setText(contentStr);
        } else {
            viewHolder.dating_list_item_content_iv.setVisibility(View.GONE);
            viewHolder.dating_list_item_content_tv.setVisibility(View.GONE);
        }


        long createtime = ISO8601.getTime(dto.getCreateTime());
        String publictimeStr = TimeUtil.getDatingPublishDateString(createtime, System.currentTimeMillis());
        viewHolder.dating_list_item_publish_time_tv.setText(publictimeStr);

        String location_str = CheckUtil.getSpacingTool(dto.getDistance());
        viewHolder.dating_list_item_distance_tv.setText(location_str);


        if (!CheckUtil.isEmpty(imageurl)) {
            viewHolder.dating_list_item_image_iv.setVisibility(View.VISIBLE);
            imageurl = ImgUtils.DealImageUrl(imageurl, 150, 150);
            Glide.with(mContext).load(imageurl).into(viewHolder.dating_list_item_image_iv);
        } else {
            viewHolder.dating_list_item_image_iv.setVisibility(View.GONE);
        }

        viewHolder.dating_list_item_all_pbv.setProgress(dto.getOwner().getSincerity());
        viewHolder.dating_list_item_all_pbv.setProgressBar_max(100);
        viewHolder.dating_list_item_all_pbv.setPromptTextIsDisplayable(true);
        viewHolder.dating_list_item_all_pbv.setPromptTextCrompttext("诚意度");
        viewHolder.dating_list_item_all_pbv.setOutside_round_style(ProgressBarView.STROKE_FILL);

//        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.dating_list_item_root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.dating_list_item_root_layout, pos);
                }
            });
        }


        viewHolder.dating_list_item_userImg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                Bundle bundle = new Bundle();
                AppointmentDto dto = getDatas().get(position);
                bundle.putInt(YpSettings.USERID, dto.getOwner().getUserId());
                ActivityUtil.jump(mContext, UserInfoActivity.class, bundle, 0, 100);
            }
        });
    }


    @Override
    public int getAdapterItemCount() {
        return list == null ? 0 : list.size();
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout dating_list_item_root_layout;

        public TextView dating_list_item_name_tv;

        public TextView dating_list_item_age_tv;

        public ImageView dating_list_item_userImg_iv;


        public ImageView dating_list_item_video_lable_iv;

        public ImageView dating_list_item_hot_iv;

        public ImageView dating_list_item_activity_talent_iv;

        public ImageView dating_list_item_vip_iv;

        public ImageView dating_list_item_type_iv;

        public TextView dating_list_item_title_tv;

        public ImageView dating_list_item_content_iv;

        public TextView dating_list_item_content_tv;


        public ImageView dating_list_item_time_iv;

        public TextView dating_list_item_time_tv;

        public ImageView dating_list_item_address_iv;

        public TextView dating_list_item_address_tv;


        public TextView dating_list_item_publish_time_tv;


        public TextView dating_list_item_distance_tv;

        public ImageView dating_list_item_image_iv;

        public ProgressBarView dating_list_item_all_pbv;


        public ViewHolder(View v, boolean isItme) {
            super(v);

            if (isItme) {

                dating_list_item_root_layout = (RelativeLayout) v.findViewById(R.id.dating_list_item_root_layout);

                dating_list_item_userImg_iv = (ImageView) v.findViewById(R.id.dating_list_item_userImg_iv);


                dating_list_item_name_tv = (TextView) v.findViewById(R.id.dating_list_item_name_tv);


                dating_list_item_age_tv = (TextView) v.findViewById(R.id.dating_list_item_age_tv);

                dating_list_item_video_lable_iv = (ImageView) v.findViewById(R.id.dating_list_item_video_lable_iv);

                dating_list_item_hot_iv = (ImageView) v.findViewById(R.id.dating_list_item_hot_iv);

                dating_list_item_activity_talent_iv = (ImageView) v.findViewById(R.id.dating_list_item_activity_talent_iv);

                dating_list_item_vip_iv = (ImageView) v.findViewById(R.id.dating_list_item_vip_iv);


                dating_list_item_type_iv = (ImageView) v.findViewById(R.id.dating_list_item_type_iv);

                dating_list_item_title_tv = (TextView) v.findViewById(R.id.dating_list_item_title_tv);

                dating_list_item_content_iv = (ImageView) v.findViewById(R.id.dating_list_item_content_iv);

                dating_list_item_content_tv = (TextView) v.findViewById(R.id.dating_list_item_content_tv);


                dating_list_item_time_iv = (ImageView) v.findViewById(R.id.dating_list_item_time_iv);

                dating_list_item_time_tv = (TextView) v.findViewById(R.id.dating_list_item_time_tv);

                dating_list_item_address_iv = (ImageView) v.findViewById(R.id.dating_list_item_address_iv);

                dating_list_item_address_tv = (TextView) v.findViewById(R.id.dating_list_item_address_tv);


                dating_list_item_publish_time_tv = (TextView) v.findViewById(R.id.dating_list_item_publish_time_tv);


                dating_list_item_distance_tv = (TextView) v.findViewById(R.id.dating_list_item_distance_tv);

                dating_list_item_image_iv = (ImageView) v.findViewById(R.id.dating_list_item_image_iv);

                dating_list_item_all_pbv = (ProgressBarView) v.findViewById(R.id.dating_list_item_all_pbv);


            }

        }
    }

    public void setData(List<AppointmentDto> list) {
        this.list = list;
    }

    public List<AppointmentDto> getDatas() {
        return list;
    }

}
