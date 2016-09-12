package cn.chono.yopper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LikeDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterIconClickLitener;


public class LikeAdapter<T> extends BaseRecyclerAdapter<LikeAdapter.ViewHolder> {

    private List<T> list;

    private Context mContext;


    private OnAdapterIconClickLitener mOnAdapterIconClickLitener;

    public void setOnAdapterIconClickLitener(OnAdapterIconClickLitener onAdapterIconClickLitener) {
        mOnAdapterIconClickLitener = onAdapterIconClickLitener;
    }

    private OnLikeItemClickLitener mOnLikeItemClickLitener;

    public void setOnLikeItemClickLitener(OnLikeItemClickLitener onLikeItemClickLitener) {
        mOnLikeItemClickLitener = onLikeItemClickLitener;
    }


    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    public LikeAdapter(Context context) {
        this.mContext = context;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public void setData(List<T> list) {
        this.list = list;
    }

    public void addData(List<T> morelist) {
        if (null == list) {
            list = new ArrayList<T>();
        }
        list.addAll(morelist);

    }

    public List<T> getDatas() {
        return list;
    }


    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        // 给ViewHolder设置布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_like, parent, false);
        return new ViewHolder(v, true);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i, boolean isItem) {
        // 给ViewHolder设置元素

        final LikeDto dto = (LikeDto) list.get(i);


        String imageurl = ImgUtils.DealImageUrl(dto.getUserInfo().getHeadImg(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

        Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(viewHolder.item_like_icon_img_iv);

        viewHolder.item_like_name_tv.setText(dto.getUserInfo().getName() + "");


        if (i == list.size() - 1) {
            viewHolder.item_like_line.setVisibility(View.GONE);
        } else {
            viewHolder.item_like_line.setVisibility(View.VISIBLE);
        }


        if (dto.getUserInfo().getSex()  == 1) {
            Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_man);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            viewHolder.item_like_sex_constellation_tv.setCompoundDrawables(sexDrawable, null, null, null);
            viewHolder.item_like_sex_constellation_tv.setTextColor(mContext.getResources().getColor(R.color.color_8cd2ff));

        } else if (dto.getUserInfo().getSex() == 2) {
            Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_woman);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            viewHolder.item_like_sex_constellation_tv.setCompoundDrawables(sexDrawable, null, null, null);
            viewHolder.item_like_sex_constellation_tv.setTextColor(mContext.getResources().getColor(R.color.color_fe8cd9));
        }


        if (((dto.getUserInfo().getStatus() >> 1) & 1) == 0) {
            //视频不通过
            viewHolder.item_like_video_level_iv.setVisibility(View.GONE);
        } else {
            viewHolder.item_like_video_level_iv.setVisibility(View.VISIBLE);
        }

        if (dto.isActivityExpert()) {
            //是活动达人
            viewHolder.item_like_activity_level_iv.setVisibility(View.VISIBLE);

        } else {
            viewHolder.item_like_activity_level_iv.setVisibility(View.GONE);
        }


        switch (dto.getCurrentUserPosition()) {
            case 0:
                //不是VIP
                viewHolder.item_like_vip_level_iv.setVisibility(View.GONE);
                break;

            case 1:
                //白银VIP
                viewHolder.item_like_vip_level_iv.setVisibility(View.VISIBLE);
                viewHolder.item_like_vip_level_iv.setBackgroundResource(R.drawable.ic_small_vip_silver);
                break;

            case 2:
                //黄金VIP
                viewHolder.item_like_vip_level_iv.setVisibility(View.VISIBLE);
                viewHolder.item_like_vip_level_iv.setBackgroundResource(R.drawable.ic_small_vip_gold);
                break;

            case 3:
                //铂金VIP
                viewHolder.item_like_vip_level_iv.setVisibility(View.VISIBLE);
                viewHolder.item_like_vip_level_iv.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                break;

            case 4:
                //钻石VIP
                viewHolder.item_like_vip_level_iv.setVisibility(View.VISIBLE);
                viewHolder.item_like_vip_level_iv.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                break;

        }


        if (dto.getActivity() != null && dto.getActivity().getActivityId()!=null) {
            viewHolder.item_like_join_activity_tv.setText(dto.getActivity().getActivityName());
        } else {
            viewHolder.item_like_join_activity_tv.setText("Ta最近没有参加活动");
        }

        if (dto.getDating() != null && dto.getDating().getDatingId() != null) {
            viewHolder.item_like_join_datings_tv.setText(dto.getDating().getDatingName());
        } else {
            viewHolder.item_like_join_datings_tv.setText("Ta最近没有发布约会");
        }

        viewHolder.item_like_sex_constellation_tv.setText(CheckUtil.ConstellationMatching(dto.getUserInfo().getHoroscope()));


        // 如果设置了回调，则设置点击事件
        if (mOnAdapterIconClickLitener != null) {
            viewHolder.item_like_icon_img_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnAdapterIconClickLitener.onAdapterIconClick(i, dto);
                }
            });

        }

        if (mOnLikeItemClickLitener == null) {
            return;
        }

        viewHolder.item_like_join_activity_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityId = "";
                if (dto.getActivity() != null) {
                    activityId = dto.getActivity().getActivityId();
                }
                mOnLikeItemClickLitener.OnTakePartEvent(i, activityId);

            }

        });

        viewHolder.item_like_join_datings_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String datingId = "";
                if (dto.getDating() != null) {
                    datingId = dto.getDating().getDatingId();
                }

                mOnLikeItemClickLitener.OnDatingsEvent(i, dto.getUserInfo().getId(), datingId);

            }

        });


    }


    @Override
    public int getAdapterItemCount() {
        // 返回数据总数
        return list == null ? 0 : list.size();
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_like_icon_img_iv;

        private ImageView item_like_vip_level_iv;

        private TextView item_like_name_tv;

        private TextView item_like_sex_constellation_tv;

        private ImageView item_like_video_level_iv;

        private ImageView item_like_activity_level_iv;

        private LinearLayout item_like_join_activity_layout;

        private TextView item_like_join_activity_tv;

        private LinearLayout item_like_join_datings_layout;

        private TextView item_like_join_datings_tv;

        private View item_like_line;

        public ViewHolder(View v, boolean isItme) {
            super(v);

            if (isItme) {

                item_like_icon_img_iv = (ImageView) v.findViewById(R.id.item_like_icon_img_iv);

                item_like_vip_level_iv = (ImageView) v.findViewById(R.id.item_like_vip_level_iv);

                item_like_name_tv = (TextView) v.findViewById(R.id.item_like_name_tv);

                item_like_sex_constellation_tv = (TextView) v.findViewById(R.id.item_like_sex_constellation_tv);


                item_like_video_level_iv = (ImageView) v.findViewById(R.id.item_like_video_level_iv);

                item_like_activity_level_iv = (ImageView) v.findViewById(R.id.item_like_activity_level_iv);

                item_like_join_activity_layout = (LinearLayout) v.findViewById(R.id.item_like_join_activity_layout);

                item_like_join_activity_tv = (TextView) v.findViewById(R.id.item_like_join_activity_tv);

                item_like_join_datings_layout = (LinearLayout) v.findViewById(R.id.item_like_join_datings_layout);

                item_like_join_datings_tv = (TextView) v.findViewById(R.id.item_like_join_datings_tv);

                item_like_line = v.findViewById(R.id.item_like_line);

            }


        }
    }

    public interface OnLikeItemClickLitener {

        //点击活动
        void OnTakePartEvent(int position, String actionId);

        //点击邀约
        void OnDatingsEvent(int position, int userId, String datingId);


    }


}
