package cn.chono.yopper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.entity.likeBean.ILike;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * Created by jianghua on 2016/7/18.
 */
public class ILikeAdapter extends BaseRecyclerAdapter<ILikeAdapter.NewViewHolder> {

    private List<ILike> listData = new ArrayList<>();
    private Context mContext;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    private int adapterTYpe = YpSettings.LIKE_TYPE_ILIKE;

    public ILikeAdapter(Context context, int type) {
        this.mContext = context;
        adapterTYpe = type;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public void setData(List<ILike> list) {
        listData = list;
        notifyDataSetChanged();
    }

    public void addData(List<ILike> list) {
        if (null == list) {
            listData = new ArrayList<>();
        }
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public List<ILike> getData() {
        return listData;
    }

    @Override
    public NewViewHolder getViewHolder(View view) {
        return new NewViewHolder(view, false);
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ilike_layout, parent, false);
        return new NewViewHolder(view, true);
    }


    @Override
    public void onBindViewHolder(NewViewHolder holder, int position, boolean isItem) {

        ILike iLike = listData.get(position);
        ILike.UserInfoBean userinfo = listData.get(position).getUserInfo();
        ILike.ActivityBean activityinfo = listData.get(position).getActivity();
        ILike.DatingBean datinginfo = listData.get(position).getDating();
        LogUtils.e("设置的数据 ：：：" + userinfo.getName());


        if (null != userinfo) {
            String imageurl = ImgUtils.DealImageUrl(userinfo.getHeadImg(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
            holder.ilikeNameTv.setText(userinfo.getName());
            Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(holder.ilikeIconImgIv);
            //星座
            holder.ilikeSexConstellationTv.setText(CheckUtil.ConstellationMatching(userinfo.getHoroscope()));


            if (userinfo.getSex() == 1) {
                Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_man);
                sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
                holder.ilikeSexConstellationTv.setCompoundDrawables(sexDrawable, null, null, null);

                holder.ilikeSexConstellationTv.setTextColor(mContext.getResources().getColor(R.color.color_8cd2ff));
            } else if (userinfo.getSex() == 2) {
                Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_woman);
                sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
                holder.ilikeSexConstellationTv.setCompoundDrawables(sexDrawable, null, null, null);
                holder.ilikeSexConstellationTv.setTextColor(mContext.getResources().getColor(R.color.color_fe8cd9));
            }

            //视频
            if (((userinfo.getStatus() >> 1) & 1) == 0) {
                //视频不通过
                holder.ilikeVideoLevelIv.setVisibility(View.GONE);
            } else {
                holder.ilikeVideoLevelIv.setVisibility(View.VISIBLE);
            }
        }

        if (null != iLike) {

            //是否解锁
            if (iLike.isIsUnlock()) {//false 需要解锁，true不需要解锁
                holder.ilikeLockIv.setVisibility(View.GONE);
            } else {
                holder.ilikeLockIv.setVisibility(View.VISIBLE);
            }

            //活动
            if (iLike.isIsActivityExpert()) {
                //是活动达人
                holder.ilikeActivityLevelIv.setVisibility(View.VISIBLE);

            } else {
                holder.ilikeActivityLevelIv.setVisibility(View.GONE);
            }

            //VIP
            switch (iLike.getCurrentUserPosition()) {
                case 0:
                    //不是VIP
                    holder.ilikeVipLevelIv.setVisibility(View.GONE);
                    break;

                case 1:
                    //白银VIP
                    holder.ilikeVipLevelIv.setVisibility(View.VISIBLE);
                    holder.ilikeVipLevelIv.setBackgroundResource(R.drawable.ic_small_vip_silver);
                    break;

                case 2:
                    //黄金VIP
                    holder.ilikeVipLevelIv.setVisibility(View.VISIBLE);
                    holder.ilikeVipLevelIv.setBackgroundResource(R.drawable.ic_small_vip_gold);
                    break;

                case 3:
                    //铂金VIP
                    holder.ilikeVipLevelIv.setVisibility(View.VISIBLE);
                    holder.ilikeVipLevelIv.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                    break;

                case 4:
                    //钻石VIP
                    holder.ilikeVipLevelIv.setVisibility(View.VISIBLE);
                    holder.ilikeVipLevelIv.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                    break;
            }
        }

        if (activityinfo != null && activityinfo.getActivityId() != null) {
            holder.ilikeJoinActivityTv.setText(activityinfo.getActivityName());
        } else {
            holder.ilikeJoinActivityTv.setText("Ta最近没有参加活动");
        }

        if (datinginfo != null && datinginfo.getDatingId() != null) {
            holder.ilikeJoinDatingsTv.setText(datinginfo.getDatingName());
        } else {
            holder.ilikeJoinDatingsTv.setText("Ta最近没有发布约会");
        }

        //头像
        holder.ilikeIconImgIv.setOnClickListener(v1 -> {
            if (adapterTYpe == YpSettings.LIKE_TYPE_ILIKE) {
                RxBus.get().post("ilikeHeadClick", listData.get(position).getUserInfo());
            } else if (adapterTYpe == YpSettings.LIKE_TYPE_LIKEME) {
                RxBus.get().post("likeMeHeadClick", listData.get(position).getUserInfo());
            } else {
                RxBus.get().post("likeEachHeadClick", listData.get(position).getUserInfo());
            }
        });

        //加入的活动
        holder.ilikeJoinActivityLayout.setOnClickListener(v1 -> {

            ViewsUtils.preventViewMultipleClick(v1, 1000);

            String activityId = "";

            if (listData.get(position).getActivity() != null) {

                Logger.e("IlikeAdapter activity =====" + listData.get(position).getActivity().toString());
                activityId = listData.get(position).getActivity().getActivityId();

                if (!TextUtils.isEmpty(activityId)) {

                    if (adapterTYpe == YpSettings.LIKE_TYPE_ILIKE) {

                        RxBus.get().post("ilikeActClick", activityId);

                    } else if (adapterTYpe == YpSettings.LIKE_TYPE_LIKEME) {

                        RxBus.get().post("likeMeActClick", activityId);

                    } else {

                        RxBus.get().post("likeEachActClick", activityId);

                    }

                }
            }
        });

        //约会
        holder.ilikeJoinDatingsLayout.setOnClickListener(v1 -> {
            ILike like = null;
            if (listData.get(position).getDating() != null && listData.get(position) != null) {
                like = listData.get(position);
                if (adapterTYpe == YpSettings.LIKE_TYPE_ILIKE) {
                    RxBus.get().post("ilikeDatingClick", like);
                } else if (adapterTYpe == YpSettings.LIKE_TYPE_LIKEME) {
                    RxBus.get().post("likeMeDatingClick", like);
                } else {
                    RxBus.get().post("likeEachDatingClick", like);
                }
            }
        });

        holder.ilikeItemLayout.setOnLongClickListener(v -> {

            ViewsUtils.preventViewMultipleClick(v, 1000);

            if (adapterTYpe == YpSettings.LIKE_TYPE_ILIKE) {

                RxBus.get().post("ilikelongClick", position);

            } else if (adapterTYpe == YpSettings.LIKE_TYPE_LIKEME) {

                RxBus.get().post("likeMelongClick", position);

            } else {

                RxBus.get().post("likeEachlongClick", position);

            }

            return true;
        });

        holder.ilikeLockIv.setOnClickListener(v -> {

            ViewsUtils.preventViewMultipleClick(v, 1000);

            int userid = listData.get(position).getUserInfo().getId();

            Logger.e("点击了解锁层 === type ==" + adapterTYpe);
            CommonEvent commonEvent = new CommonEvent();
            commonEvent.setPostion(position);
            commonEvent.setEvent(userid);
            if (adapterTYpe == YpSettings.LIKE_TYPE_LIKEME) {
                RxBus.get().post("likeMeunlock", commonEvent);
            } else {
                RxBus.get().post("likeEachunlock", commonEvent);
            }

        });

    }



    @Override
    public int getAdapterItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public class NewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ilike_item_layout)
        LinearLayout ilikeItemLayout;
        @BindView(R.id.ilike_icon_img_iv)
        ImageView ilikeIconImgIv;
        @BindView(R.id.ilike_vip_level_iv)
        ImageView ilikeVipLevelIv;
        @BindView(R.id.ilike_name_tv)
        TextView ilikeNameTv;
        @BindView(R.id.ilike_sex_constellation_tv)
        TextView ilikeSexConstellationTv;
        @BindView(R.id.ilike_video_level_iv)
        ImageView ilikeVideoLevelIv;
        @BindView(R.id.ilike_activity_level_iv)
        ImageView ilikeActivityLevelIv;
        @BindView(R.id.ilike_join_activity_tv)
        TextView ilikeJoinActivityTv;
        @BindView(R.id.ilike_join_activity_layout)
        LinearLayout ilikeJoinActivityLayout;
        @BindView(R.id.ilike_join_datings_tv)
        TextView ilikeJoinDatingsTv;
        @BindView(R.id.ilike_join_datings_layout)
        LinearLayout ilikeJoinDatingsLayout;
        @BindView(R.id.ilike_lock_iv)
        ImageView ilikeLockIv;

        public NewViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
//            ilikeItemLayout = (LinearLayout) itemView.findViewById(R.id.ilike_item_layout);
//            ilikeNameTv = (TextView) itemView.findViewById(R.id.ilike_name_tv);
        }
    }
}
