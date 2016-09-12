package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andbase.tractor.utils.LogUtils;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListRespBean;

/**
 * Created by jianghua on 2016/3/24.
 */
public class BonusAdapter extends BaseRecyclerAdapter<BonusAdapter.NewViewHolder> {

    private Context context;

    private List<MyBonusListRespBean.MyBonusListRespContent.BonusItem> listData;

    private OnItemListener onItemListener;

    public BonusAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemListener {
        void onReceive(View view, int position, MyBonusListRespBean.MyBonusListRespContent.BonusItem itemData);

    }

    public OnItemListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public List<MyBonusListRespBean.MyBonusListRespContent.BonusItem> getListData() {
        return listData;
    }

    public void setListData(List<MyBonusListRespBean.MyBonusListRespContent.BonusItem> listData) {
        this.listData = listData;
    }

    public void addData(List<MyBonusListRespBean.MyBonusListRespContent.BonusItem> list) {

        if (null == listData) {
            listData = new ArrayList<>();
        }
        listData.addAll(list);
    }

    @Override
    public NewViewHolder getViewHolder(View view) {
        return new NewViewHolder(view, false);
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mybonus, parent, false);
        return new NewViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final NewViewHolder holder, final int position, boolean isItem) {

        Glide.with(context).load(listData.get(position).getPrize().getImageUrl()).into(holder.mBonusImg);
        holder.mBonusName.setText(listData.get(position).getPrize().getName() + " x" + new Double(listData.get(position).getCount()).intValue());
        holder.mBonusValue.setText("价值：" + String.valueOf(new Double(listData.get(position).getPrize().getPrice()).intValue()) + "RMB");
        holder.mBonusTimeout.setText("有效期 ：" + getExpiryDate(listData.get(position).getExpiryDate()) + "天");

        int status = listData.get(position).getStatus();
        int type = listData.get(position).getPrize().getType();

        if (type == 2) {//抽奖
            if (status == 1) {
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("已领取");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));
                holder.mBonusGet.setBackgroundResource(R.drawable.bonus_rectangle_btn);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            } else if (status == 0) {
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("抽奖");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                holder.mBonusGet.setBackgroundResource(R.drawable.shape_defalut_corner);

                holder.mBonusTimeout.setVisibility(View.VISIBLE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            } else if (2 == status) {//已过期
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_50333333));
                holder.mBonusGet.setVisibility(View.GONE);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner_cover);

                holder.mBonusExpired.setVisibility(View.VISIBLE);
            } else if (3 == status) {//已兑换
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("已兑换");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                holder.mBonusGet.setBackgroundResource(R.drawable.shape_corner_grey);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            }
        } else if (type == 1) {

            if (0 == status) {
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("领取");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                holder.mBonusGet.setBackgroundResource(R.drawable.shape_defalut_corner);

                holder.mBonusTimeout.setVisibility(View.VISIBLE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            } else if (1 == status) {
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("已领取");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));
                holder.mBonusGet.setBackgroundResource(R.drawable.bonus_rectangle_btn);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            } else if (2 == status) {//已过期
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_50333333));
                holder.mBonusGet.setVisibility(View.GONE);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner_cover);

                holder.mBonusExpired.setVisibility(View.VISIBLE);
            } else if (3 == status) {//已兑换
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("已兑换");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                holder.mBonusGet.setBackgroundResource(R.drawable.shape_corner_grey);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            }
        } else if (type == 3) {//苹果
            if (0 == status) {
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("领取");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                holder.mBonusGet.setBackgroundResource(R.drawable.shape_defalut_corner);

                holder.mBonusTimeout.setVisibility(View.VISIBLE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            } else if (1 == status) {
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("已领取");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));
                holder.mBonusGet.setBackgroundResource(R.drawable.bonus_rectangle_btn);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            } else if (2 == status) {//已过期
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_50333333));
                holder.mBonusGet.setVisibility(View.GONE);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner_cover);

                holder.mBonusExpired.setVisibility(View.VISIBLE);
            } else if (3 == status) {//已兑换
                holder.mBonusName.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.mBonusGet.setVisibility(View.VISIBLE);
                holder.mBonusGet.setText("已兑换");
                holder.mBonusGet.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                holder.mBonusGet.setBackgroundResource(R.drawable.shape_corner_grey);

                holder.mBonusTimeout.setVisibility(View.GONE);

                holder.mFragmeLayout.setBackgroundResource(R.drawable.shape_corner);

                holder.mBonusExpired.setVisibility(View.GONE);
            }
        }

        if (listData.get(position).getPrize().getType() == 2) {//抽奖券
            holder.mBonusValue.setVisibility(View.GONE);
        } else {
//            holder.mBonusValue.setVisibility(View.VISIBLE);
        }

        holder.mBonusGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemListener.onReceive(holder.mBonusGet, position, listData.get(position));
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public class NewViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout mFragmeLayout;
        public ImageView mBonusImg;
        public TextView mBonusName;
        public TextView mBonusValue;
        public TextView mBonusTimeout;
        public TextView mBonusGet;
        public ImageView mBonusExpired;

        public NewViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                mFragmeLayout = (FrameLayout) itemView.findViewById(R.id.bonus_layout);
                mBonusGet = (TextView) itemView.findViewById(R.id.get_bonus);
                mBonusImg = (ImageView) itemView.findViewById(R.id.bonus_img);
                mBonusName = (TextView) itemView.findViewById(R.id.bonus_name);
                mBonusValue = (TextView) itemView.findViewById(R.id.bonus_value);
                mBonusTimeout = (TextView) itemView.findViewById(R.id.bonus_timeout);
                mBonusExpired = (ImageView) itemView.findViewById(R.id.bonus_expired);
            }
        }
    }

    /**
     * 获取倒计时的时间
     *
     * @param expiryDate
     * @return
     */
    private String getExpiryDate(String expiryDate) {
        String strTime = expiryDate.substring(0, expiryDate.indexOf("+"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");

        Date nowTime = new Date(System.currentTimeMillis());

        Date targetTime = null;

        try {
            targetTime = format.parse(strTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (targetTime != null) {
            long ms = (targetTime.getTime() - nowTime.getTime());

            long days = ms / (1000 * 60 * 60 * 24);
            long hours = (ms - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (ms - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (ms - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / 1000;

            if (hours > 0 && days > 0) {//得到整天
                return String.valueOf(new Long(days).intValue() + 1);
            }

            if (days > 0 && minutes > 0) {//得到整小时
                return String.valueOf(new Long(hours).intValue() + 1);
            }

            if (minutes > 0 && seconds > 0) {//得到整分钟
                return String.valueOf(new Long(minutes).intValue() + 1);
            }
        }

        return "0";
    }
}
