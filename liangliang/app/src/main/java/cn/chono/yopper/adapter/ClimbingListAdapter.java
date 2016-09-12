package cn.chono.yopper.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andbase.tractor.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ClimbList.ClimbListRespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ImgUtils;

/**
 * Created by yangjinyu on 16/2/29.
 */
public class ClimbingListAdapter extends BaseAdapter {

    Context mContext;
    private BitmapPool mPool;
    private CropCircleTransformation transformation;

    private List<ClimbListRespBean.ClimbListRespBeanTemp.Rank.RankItem> rankItems = new ArrayList<>();

    public List<ClimbListRespBean.ClimbListRespBeanTemp.Rank.RankItem> getRankItems() {
        return rankItems;
    }

    public void setRankItems(List<ClimbListRespBean.ClimbListRespBeanTemp.Rank.RankItem> rankItems) {
        this.rankItems = rankItems;
    }

    public ClimbingListAdapter(Context c) {
        mContext = c;
        mPool = Glide.get(c).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public void setData(List<ClimbListRespBean.ClimbListRespBeanTemp.Rank.RankItem> listData) {
        if (rankItems == null) {
            rankItems = new ArrayList<>();
        }

        rankItems.addAll(listData);
    }

    @Override
    public int getCount() {
        return rankItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rankItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_climbing_list, null);
            holder.mBonusView = (ImageView) convertView.findViewById(R.id.bonus);
            holder.mPlaceView = (TextView) convertView.findViewById(R.id.placeicon);
            holder.mUserConstellationView = (TextView) convertView.findViewById(R.id.userconstellation);
            holder.mUserNameView = (TextView) convertView.findViewById(R.id.username);
            holder.mUserEnergyView = (TextView) convertView.findViewById(R.id.energy_no);
            holder.mUserPortraitView = (ImageView) convertView.findViewById(R.id.portraiticon);
            holder.mEnergyBar = (SeekBar) convertView.findViewById(R.id.energy_bar);
            holder.mVipView = (ImageView) convertView.findViewById(R.id.climbing_vip_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int sex = 0;
        if (rankItems.get(position).getUser() != null) {
            sex = rankItems.get(position).getUser().getSex();
            holder.mUserNameView.setText(rankItems.get(position).getUser().getName());

            LogUtils.e("ClimbingAdapter url :" + rankItems.get(position).getUser().getHeadImg());
            String imageurl = ImgUtils.DealImageUrl(rankItems.get(position).getUser().getHeadImg(), 150, 150);
            LogUtils.e("ClimbingAdapter new url :" + imageurl);
            Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(holder.mUserPortraitView);

            if (sex == 1) {
                Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_man);
                sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
                holder.mUserConstellationView.setCompoundDrawables(sexDrawable, null, null, null);
                holder.mUserConstellationView.setTextColor(mContext.getResources().getColor(R.color.color_8cd2ff));
            } else if (sex == 2) {
                Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_woman);
                sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
                holder.mUserConstellationView.setCompoundDrawables(sexDrawable, null, null, null);
                holder.mUserConstellationView.setTextColor(mContext.getResources().getColor(R.color.color_fe8cd9));
            }

            holder.mUserConstellationView.setText(CheckUtil.ConstellationMatching(rankItems.get(position).getUser().getHoroscope()));

        }

        switch (rankItems.get(position).getUser().getCurrentUserPosition()) {
            case 0:
                //不是VIP
                holder.mVipView.setVisibility(View.GONE);
                break;

            case 1:
                //白银VIP
                holder.mVipView.setVisibility(View.VISIBLE);
                holder.mVipView.setBackgroundResource(R.drawable.ic_small_vip_silver);
                break;

            case 2:
                //黄金VIP
                holder.mVipView.setVisibility(View.VISIBLE);
                holder.mVipView.setBackgroundResource(R.drawable.ic_small_vip_gold);
                break;

            case 3:
                //铂金VIP
                holder.mVipView.setVisibility(View.VISIBLE);
                holder.mVipView.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                break;

            case 4:
                //钻石VIP
                holder.mVipView.setVisibility(View.VISIBLE);
                holder.mVipView.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                break;
        }


        if (rankItems.get(position).getPower() != null) {
            double pro = (rankItems.get(position).getPower().getTotalValue() + 0.0)
                    / rankItems.get(0).getPower().getTotalValue();
            System.out.println("pro : " + pro);
            holder.mEnergyBar.setProgress((int) (pro * 100));
            holder.mUserEnergyView.setText(String.valueOf(rankItems.get(position).getPower().getTotalValue()));
        }
        holder.mEnergyBar.setEnabled(false);

        Drawable drawable;
        if (position == 0) {
            drawable = mContext.getResources().getDrawable(R.drawable.no1);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mPlaceView.setCompoundDrawables(null, null, null, drawable);
            holder.mPlaceView.setText("");
        } else if (position == 1) {
            drawable = mContext.getResources().getDrawable(R.drawable.no2);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mPlaceView.setCompoundDrawables(null, null, null, drawable);
            holder.mPlaceView.setText("");
        } else if (position == 2) {
            drawable = mContext.getResources().getDrawable(R.drawable.no3);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mPlaceView.setCompoundDrawables(null, null, null, drawable);
            holder.mPlaceView.setText("");
        } else {
            holder.mPlaceView.setCompoundDrawables(null, null, null, null);
            holder.mPlaceView.setText(position + 1 + "");
        }

        holder.mUserPortraitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rankItems.get(position).getUser() != null) {
                    Bundle userbundle = new Bundle();
                    userbundle.putInt(YpSettings.USERID, Integer.parseInt(rankItems.get(position).getUser().getUserId()));
                    ActivityUtil.jump(mContext, UserInfoActivity.class, userbundle, 0, 100);
                }
            }
        });
        return convertView;
    }

    public void updateSpecificItemByPosition(ListView lv, int position, int data) {
        int firstVisiblePosition = lv.getFirstVisiblePosition();
        int lastVisiblePosition = lv.getLastVisiblePosition();

        if ((position >= firstVisiblePosition) && (position <= lastVisiblePosition)) {
            View v = lv.getChildAt(position - firstVisiblePosition);
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.mEnergyBar.setProgress(data);
        }
    }

    class ViewHolder {
        public TextView mUserNameView;
        public TextView mUserConstellationView;
        public TextView mUserEnergyView;
        public ImageView mUserPortraitView;
        public ImageView mBonusView;
        public TextView mPlaceView;
        public SeekBar mEnergyBar;
        public ImageView mVipView;
    }
}
