package cn.chono.yopper.adapter;

import android.content.Context;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.VipConfigs.VipConfigsListEntity;
import cn.chono.yopper.data.ApplesEntity;
import cn.chono.yopper.data.VipRenewalsPrivilegeEntity;

/**
 * Created by cc on 16/6/14.
 */
public class VipOpenedAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    static int sVIPTITLE = 0;
    static int sVIP = 1;
    static int sVIPTV = 2;
    static int sPrivilege = 3;

    int apple_count;

    int userPosition;

    Context mContext;


    List<VipConfigsListEntity> mRenewalsEntities = new ArrayList<>();


    public VipOpenedAdapter(Context context) {
        mContext = context;
    }


    public void setData(List<VipConfigsListEntity> renewalsEntities) {
        mRenewalsEntities = renewalsEntities;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {


        if (sVIPTITLE == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_renewals_vip_title, parent, false);

            return new RenewalsTitleViewHolder(view);


        }

        if (sVIP == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vip_renewals, parent, false);

            return new RenewalsViewHolder(view);
        }

        if (sVIPTV == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vip_renewals_tv, parent, false);

            return new RenewalsTvViewHolder(view);

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vip_privilege, parent, false);

        return new PrivilegeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, boolean isItem) {

        LogUtils.e("onBindViewHolder=" + position);

        if (holder instanceof RenewalsTitleViewHolder) {

            final ApplesEntity applesEntity = mRenewalsEntities.get(position).mApplesEntity;

            apple_count = applesEntity.apple_count;

            userPosition = applesEntity.userPosition;


            ((RenewalsTitleViewHolder) holder).item_renewals_vip_tv_status_nint.setVisibility(View.VISIBLE);

            ((RenewalsTitleViewHolder) holder).item_renewals_vip_tv_more_apple_number.setText(apple_count + "");

            ((RenewalsTitleViewHolder) holder).item_renewals_vip_tv_status.setText(getUserPosition(userPosition));

            ((RenewalsTitleViewHolder) holder).item_renewals_vip_tv_status_title.setText("升级");

            ((RenewalsTitleViewHolder) holder).item_renewals_vip_tv_status_hint.setText("会员");


            ((RenewalsTitleViewHolder) holder).item_renewals_vip_tv_obtain_more_apple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.get().post("more_apple", apple_count);
                }
            });


            return;
        }


        if (holder instanceof RenewalsViewHolder) {


            final VipConfigsListEntity vipConfigsListEntity = mRenewalsEntities.get(position);

            String userPositionName = vipConfigsListEntity.vipName == null ? "" : vipConfigsListEntity.vipName;
            // V2.5.4 vip身份类型 ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP


            ((RenewalsViewHolder) holder).item_vip_renewals_tv_title.setText(userPositionName);

            StringBuffer dateSb = new StringBuffer("时长");

            dateSb.append(vipConfigsListEntity.dayCount);

            dateSb.append("天");

            ((RenewalsViewHolder) holder).item_vip_renewals_tv_date.setText(dateSb.toString());


            StringBuffer appleSb = new StringBuffer();

            appleSb.append(vipConfigsListEntity.appleCount);

            appleSb.append("苹果");

            ((RenewalsViewHolder) holder).item_vip_renewals_tv_but.setText(appleSb.toString());


            ((RenewalsViewHolder) holder).item_vip_renewals_tv_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    RxBus.get().post("pay_apple", vipConfigsListEntity);





                }
            });


            Integer imgUrl = getUserPositionIcon(vipConfigsListEntity.userPosition);

            if (imgUrl != null) {
                Glide.with(mContext).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(((RenewalsViewHolder) holder).item_vip_renewals_iv_icon);
            }


            ((RenewalsViewHolder) holder).item_vip_renewals_ll_privilege.removeAllViews();

            if (vipConfigsListEntity.vipRight == null || vipConfigsListEntity.vipRight.size() == 0) {
                return;
            }


            for (int i = 0; i < vipConfigsListEntity.vipRight.size(); i++) {

                String content = vipConfigsListEntity.vipRight.get(i);

                if (TextUtils.isEmpty(content)) {

                    continue;
                }

                View view = LayoutInflater.from(mContext).inflate(R.layout.item_vip_renewals_content, null);

                TextView textView = (TextView) view.findViewById(R.id.item_vip_renewals_tv_content);

                textView.setText(content);

                ((RenewalsViewHolder) holder).item_vip_renewals_ll_privilege.addView(view);


            }


            return;
        }


        if (holder instanceof RenewalsTvViewHolder) {

            ((RenewalsTvViewHolder) holder).item_vip_renewals_tv_hint.setText(mRenewalsEntities.get(position).mVIPTITLE == null ? "" : mRenewalsEntities.get(position).mVIPTITLE);


            return;
        }


        if (holder instanceof PrivilegeViewHolder) {


            VipRenewalsPrivilegeEntity vipRenewalsPrivilegeEntity = mRenewalsEntities.get(position).mVipRenewalsPrivilegeEntity;

            ((PrivilegeViewHolder) holder).item_vip_privilege_name.setText(vipRenewalsPrivilegeEntity.title == null ? "" : vipRenewalsPrivilegeEntity.title);

            Glide.with(mContext).load(vipRenewalsPrivilegeEntity.imageId).into(((PrivilegeViewHolder) holder).item_vip_privilege_icon);


            return;
        }


    }


    private String getUserPosition(int userPosition) {


        if (1 == userPosition) {

            return mContext.getResources().getString(R.string.silver_vip);

        } else if (2 == userPosition) {

            return mContext.getResources().getString(R.string.gole_vip);

        } else if (3 == userPosition) {

            return mContext.getResources().getString(R.string.platinum_vip);

        } else if (4 == userPosition) {

            return mContext.getResources().getString(R.string.diamond_vip);

        }


        return "VIP";


    }

    private Integer getUserPositionIcon(int userPosition) {


        if (1 == userPosition) {

            return R.drawable.vip_silver_icon;

        } else if (2 == userPosition) {

            return R.drawable.vip_gole_icon;

        } else if (3 == userPosition) {

            return R.drawable.vip_platinum_icon;

        } else if (4 == userPosition) {

            return R.drawable.vip_diamond_icon;

        }


        return null;
    }


    @Override
    public int getAdapterItemCount() {


        return mRenewalsEntities == null ? 0 : mRenewalsEntities.size();
    }


    @Override
    public int getAdapterItemViewType(int position) {


        if (mRenewalsEntities.get(position).mApplesEntity != null) {
            return sVIPTITLE;

        }

        if (mRenewalsEntities.get(position).mVipRenewalsPrivilegeEntity != null) {

            return sPrivilege;
        }

        if (!TextUtils.isEmpty(mRenewalsEntities.get(position).mVIPTITLE)) {

            return sVIPTV;
        }

        return sVIP;
    }

    public class RenewalsTitleViewHolder extends RecyclerView.ViewHolder {


        TextView item_renewals_vip_tv_status;
        TextView item_renewals_vip_tv_status_title;
        TextView item_renewals_vip_tv_status_hint;

        TextView item_renewals_vip_tv_more_apple_number;

        TextView item_renewals_vip_tv_obtain_more_apple;

        TextView item_renewals_vip_tv_status_nint;

        public RenewalsTitleViewHolder(View itemView) {
            super(itemView);

            item_renewals_vip_tv_status_nint= (TextView) itemView.findViewById(R.id.item_renewals_vip_tv_status_nint);

            item_renewals_vip_tv_status = (TextView) itemView.findViewById(R.id.item_renewals_vip_tv_status);

            item_renewals_vip_tv_status_title = (TextView) itemView.findViewById(R.id.item_renewals_vip_tv_status_title);

            item_renewals_vip_tv_status_hint = (TextView) itemView.findViewById(R.id.item_renewals_vip_tv_status_hint);

            item_renewals_vip_tv_more_apple_number = (TextView) itemView.findViewById(R.id.item_renewals_vip_tv_more_apple_number);

            item_renewals_vip_tv_obtain_more_apple = (TextView) itemView.findViewById(R.id.item_renewals_vip_tv_obtain_more_apple);
        }
    }


    public class RenewalsTvViewHolder extends RecyclerView.ViewHolder {

        TextView item_vip_renewals_tv_hint;


        public RenewalsTvViewHolder(View itemView) {
            super(itemView);

            item_vip_renewals_tv_hint = (TextView) itemView.findViewById(R.id.item_vip_renewals_tv_hint);
        }
    }


    public class RenewalsViewHolder extends RecyclerView.ViewHolder {

        TextView item_vip_renewals_tv_but;

        TextView item_vip_renewals_tv_title;

        TextView item_vip_renewals_tv_date;

        ImageView item_vip_renewals_iv_icon;

        LinearLayout item_vip_renewals_ll_privilege;


        public RenewalsViewHolder(View itemView) {
            super(itemView);

            item_vip_renewals_tv_but = (TextView) itemView.findViewById(R.id.item_vip_renewals_tv_but);

            item_vip_renewals_tv_title = (TextView) itemView.findViewById(R.id.item_vip_renewals_tv_title);

            item_vip_renewals_tv_date = (TextView) itemView.findViewById(R.id.item_vip_renewals_tv_date);

            item_vip_renewals_iv_icon = (ImageView) itemView.findViewById(R.id.item_vip_renewals_iv_icon);

            item_vip_renewals_ll_privilege = (LinearLayout) itemView.findViewById(R.id.item_vip_renewals_ll_privilege);

        }
    }


    public class PrivilegeViewHolder extends RecyclerView.ViewHolder {

        TextView item_vip_privilege_name;

        ImageView item_vip_privilege_icon;

        public PrivilegeViewHolder(View itemView) {
            super(itemView);


            item_vip_privilege_name = (TextView) itemView.findViewById(R.id.item_vip_privilege_name);

            item_vip_privilege_icon = (ImageView) itemView.findViewById(R.id.item_vip_privilege_icon);
        }
    }
}
