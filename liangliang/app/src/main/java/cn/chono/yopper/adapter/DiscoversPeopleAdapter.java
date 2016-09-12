package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.NearPeopleDto;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/3/16.
 */
public class DiscoversPeopleAdapter extends BaseRecyclerAdapter<DiscoversPeopleAdapter.NewViewHolder> {


    public interface OnItemClickLitener {
        void onItemClick(View view, int position, int userID);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private Context context;

    private List<NearPeopleDto> list;


    public DiscoversPeopleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<NearPeopleDto> list) {
        this.list = list;

    }

    public List<NearPeopleDto> getData() {
        return list;
    }


    public void addData(List<NearPeopleDto> addlist) {
        if (list == null) {
            list = new ArrayList<NearPeopleDto>();
        }
        list.addAll(addlist);

    }


    @Override
    public NewViewHolder getViewHolder(View view) {
        return new NewViewHolder(view, false);
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View view = LayoutInflater.from(context).inflate(R.layout.discover_people_item_layout, parent, false);
//
//        int itemHeight = (parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight()) / 3;
////
////
//        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
//
//        params.width = itemHeight;
//
//        params.height = itemHeight;
////
//        view.setLayoutParams(params);

        return new NewViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final NewViewHolder holder, final int position, boolean isItem) {



//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.relativeLayout.getLayoutParams();
//
//        params.width = itemHeight;
//
//        params.height = itemHeight;
////
//        holder.relativeLayout.setLayoutParams(params);


        final NearPeopleDto dto = list.get(position);

        if (dto.isHasVideo())
            holder.video_icon.setVisibility(View.VISIBLE);
        else
            holder.video_icon.setVisibility(View.GONE);


        String url = dto.getHeadImg();

        String imageurl = ImgUtils.DealImageUrl(url, YpSettings.IMG_SIZE_360, YpSettings.IMG_SIZE_360);


        Logger.e("imageurl=" + imageurl);

        Glide.with(context).load(imageurl).error(R.drawable.error_default_icon).into(holder.userIcon);

//        Glide.with(context).load(imageurl).into(holder.userIcon);

//        boolean hasDating = dto.isHasDating();
//        if (hasDating) {
//            holder.hasDating_view.setVisibility(View.VISIBLE);
//        } else {
//            holder.hasDating_view.setVisibility(View.GONE);
//        }

        long time = ISO8601.getTime(dto.getUpdateTime());

        String timeStr = TimeUtil.LivelyTimeFormat(time);

        String location_str = CheckUtil.getSpacingTool(dto.getDistance());

        String location_time_tv = timeStr + " | " + location_str;

        holder.locationNumberTv.setText(location_time_tv);

        if (dto.isHot()) {
            holder.hot_icon.setVisibility(View.VISIBLE);
        } else {
            holder.hot_icon.setVisibility(View.GONE);
        }


        if (dto.isActivityExpert()) {
            holder.activity_talent_icon.setVisibility(View.VISIBLE);
        } else {
            holder.activity_talent_icon.setVisibility(View.GONE);
        }


        switch (dto.getCurrentUserPosition()) {
            case 0:
                //不是VIP
                holder.vip_iv.setVisibility(View.GONE);
                break;

            case 1:
                //白银VIP
                holder.vip_iv.setVisibility(View.VISIBLE);
                holder.vip_iv.setBackgroundResource(R.drawable.ic_people_vip_silver);
                break;

            case 2:
                //黄金VIP
                holder.vip_iv.setVisibility(View.VISIBLE);
                holder.vip_iv.setBackgroundResource(R.drawable.ic_people_vip_gold);
                break;

            case 3:
                //铂金VIP
                holder.vip_iv.setVisibility(View.VISIBLE);
                holder.vip_iv.setBackgroundResource(R.drawable.ic_people_vip_platinum);
                break;

            case 4:
                //钻石VIP
                holder.vip_iv.setVisibility(View.VISIBLE);
                holder.vip_iv.setBackgroundResource(R.drawable.ic_people_vip_diamond);
                break;

        }


        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.relativeLayout, position, dto.getId());
                }
            });
        }


    }


    @Override
    public int getAdapterItemCount() {
        return list == null ? 0 : list.size();
    }


    class NewViewHolder extends RecyclerView.ViewHolder {


        public ImageView userIcon;

        /**
         * 矩离
         */
        public TextView locationNumberTv;
        public RelativeLayout relativeLayout;
        //        public ImageView hasDating_view;
        public ImageView video_icon;
        public ImageView hot_icon;

        public ImageView activity_talent_icon;
        public ImageView vip_iv;

        public NewViewHolder(View itemView, boolean isItem) {
            super(itemView);


            if (isItem) {


                locationNumberTv = (TextView) itemView.findViewById(R.id.locationNumberTv);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);

                userIcon = (ImageView) itemView.findViewById(R.id.userIcon);
//                hasDating_view = (ImageView) itemView.findViewById(R.id.hasDating_view);
                video_icon = (ImageView) itemView.findViewById(R.id.video_icon);
                hot_icon = (ImageView) itemView.findViewById(R.id.hot_icon);

                activity_talent_icon = (ImageView) itemView.findViewById(R.id.activity_talent_icon);
                vip_iv = (ImageView) itemView.findViewById(R.id.vip_iv);

                int width = UnitUtil.getScreenWidthPixels(context);


            }
        }
    }
}
