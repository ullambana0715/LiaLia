package cn.chono.yopper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.VisitorsDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.TimeUtil;

public class UserVisitorsAdapter extends BaseRecyclerAdapter<UserVisitorsAdapter.NewViewHolder> {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, int userID);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private BitmapPool mPool;
    private CropCircleTransformation transformation;

    private Context context;

    private List<VisitorsDto> list;

    public UserVisitorsAdapter(Context context) {
        this.context = context;

        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public void setData(List<VisitorsDto> list) {
        this.list = list;
    }

    public List<VisitorsDto> getData() {
        return list;
    }

    public class NewViewHolder extends RecyclerView.ViewHolder {

        private ImageView user_visitor_head_img_iv;

        private ImageView user_visitor_isnew_iv;

        private ImageView user_visitor_video_iv;

        private TextView user_visitor_sex_constellation_tv;

        private TextView user_visitor_name_tv;

        private TextView user_visitor_time_tv;

        private ImageView user_visitor_vip_iv;

        public NewViewHolder(View itemView, boolean isItme) {
            super(itemView);

            if (isItme) {
                user_visitor_head_img_iv = (ImageView) itemView.findViewById(R.id.user_visitor_head_img_iv);

                user_visitor_isnew_iv = (ImageView) itemView.findViewById(R.id.user_visitor_isnew_iv);

                user_visitor_video_iv = (ImageView) itemView.findViewById(R.id.user_visitor_video_iv);

                user_visitor_sex_constellation_tv = (TextView) itemView.findViewById(R.id.user_visitor_sex_constellation_tv);

                user_visitor_name_tv = (TextView) itemView.findViewById(R.id.user_visitor_name_tv);

                user_visitor_time_tv = (TextView) itemView.findViewById(R.id.user_visitor_time_tv);

                user_visitor_vip_iv = (ImageView) itemView.findViewById(R.id.user_visitor_vip_iv);
            }


        }
    }


    @Override
    public NewViewHolder getViewHolder(View view) {
        return new NewViewHolder(view, false);
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_visitors_item_layout, parent, false);
        return new NewViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final NewViewHolder holder, final int position, boolean isItem) {


        final VisitorsDto dto = list.get(position);

        String url = dto.getUser().getHeadImg();
        String imageurl = ImgUtils.DealImageUrl(url, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

        Glide.with(context).load(imageurl).bitmapTransform(transformation).dontAnimate().into(holder.user_visitor_head_img_iv);

        if (dto.isNew()) {
            holder.user_visitor_isnew_iv.setVisibility(View.VISIBLE);
        } else {
            holder.user_visitor_isnew_iv.setVisibility(View.GONE);
        }


        String name = dto.getUser().getName();

        String nameStr = "";


        if (!TextUtils.isEmpty(name)) {
            int length = name.length();
            if (length > 9) {
                nameStr = name.substring(0, 8) + "...";
            } else {
                nameStr = name;
            }


        }

        holder.user_visitor_name_tv.setText(nameStr);


        if (dto.getUser().getSex()  == 1) {
            Drawable sexDrawable = context.getResources().getDrawable(R.drawable.ic_sex_man);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            holder.user_visitor_sex_constellation_tv.setCompoundDrawables(sexDrawable, null, null, null);
            holder.user_visitor_sex_constellation_tv.setTextColor(context.getResources().getColor(R.color.color_8cd2ff));
        } else if (dto.getUser().getSex() == 2) {
            Drawable sexDrawable = context.getResources().getDrawable(R.drawable.ic_sex_woman);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            holder.user_visitor_sex_constellation_tv.setCompoundDrawables(sexDrawable, null, null, null);
            holder.user_visitor_sex_constellation_tv.setTextColor(context.getResources().getColor(R.color.color_fe8cd9));
        }

        if (((dto.getUser().getStatus() >> 1) & 1) == 0) {
            //视频不通过
            holder.user_visitor_video_iv.setVisibility(View.GONE);
        } else {
            holder.user_visitor_video_iv.setVisibility(View.VISIBLE);
        }


        holder.user_visitor_sex_constellation_tv.setText(CheckUtil.ConstellationMatching(dto.getUser().getHoroscope()));

        long time = ISO8601.getTime(dto.getLastVisitTime());
        String timeStr = TimeUtil.normalTimeFormat(time);
        holder.user_visitor_time_tv.setText(timeStr + "看过你");


        switch (dto.getCurrentUserPosition()) {
            case 0:
                //不是VIP
                holder.user_visitor_vip_iv.setVisibility(View.GONE);
                break;

            case 1:
                //白银VIP
                holder.user_visitor_vip_iv.setVisibility(View.VISIBLE);
                holder.user_visitor_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_silver);
                break;

            case 2:
                //黄金VIP
                holder.user_visitor_vip_iv.setVisibility(View.VISIBLE);
                holder.user_visitor_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_gold);
                break;

            case 3:
                //铂金VIP
                holder.user_visitor_vip_iv.setVisibility(View.VISIBLE);
                holder.user_visitor_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                break;

            case 4:
                //钻石VIP
                holder.user_visitor_vip_iv.setVisibility(View.VISIBLE);
                holder.user_visitor_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                break;

        }


        if (mOnItemClickLitener != null) {
            holder.user_visitor_head_img_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.user_visitor_head_img_iv, position, dto.getUser().getId());
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getAdapterItemCount() {
        return list == null ? 0 : list.size();
    }

}
