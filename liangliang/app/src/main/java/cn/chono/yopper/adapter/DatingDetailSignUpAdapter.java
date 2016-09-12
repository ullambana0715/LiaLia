package cn.chono.yopper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AppointOwner;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.view.ProgressBarView;

public class DatingDetailSignUpAdapter extends BaseAdapter {

    private List<AppointOwner> list;
    ;

    private Context mContext;

    private CropCircleTransformation circletransformation;

    private BitmapPool mPool;


    public DatingDetailSignUpAdapter(Context context, List<AppointOwner> list) {
        this.mContext = context;
        this.list = list;

        mPool = Glide.get(context).getBitmapPool();
        circletransformation = new CropCircleTransformation(mPool);

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
        if (convertView == null) {
            // 获得设置的view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dating_detail_signup_item, null);
            holder = new ViewHolder();
            holder.dating_detail_signup_root_layout = (LinearLayout) convertView.findViewById(R.id.dating_detail_signup_root_layout);

            holder.dating_detail_signup_userImg_iv = (ImageView) convertView.findViewById(R.id.dating_detail_signup_userImg_iv);


            holder.dating_detail_signup_hot_iv = (ImageView) convertView.findViewById(R.id.dating_detail_signup_hot_iv);

            holder.dating_detail_signup_name_tv = (TextView) convertView.findViewById(R.id.dating_detail_signup_name_tv);

            holder.dating_detail_signup_age_tv = (TextView) convertView.findViewById(R.id.dating_detail_signup_age_tv);

            holder.dating_detail_signup_all_pbv = (ProgressBarView) convertView.findViewById(R.id.dating_detail_signup_all_pbv);

            holder.dating_detail_signup_bottom_view = convertView.findViewById(R.id.dating_detail_signup_bottom_view);

            convertView.setTag(holder);

        } else {
            // 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        final AppointOwner dto = list.get(position);

        if (!CheckUtil.isEmpty(dto.getHeadImg())) {
            String imageurl = ImgUtils.DealImageUrl(dto.getHeadImg(), 150, 150);
            Glide.with(mContext).load(imageurl).bitmapTransform(circletransformation).into(holder.dating_detail_signup_userImg_iv);
        }

        holder.dating_detail_signup_name_tv.setText(dto.getName());

        //性别：男
        if (dto.getSex() == 1) {
            Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_man);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            holder.dating_detail_signup_age_tv.setCompoundDrawables(sexDrawable, null, null, null);
            holder.dating_detail_signup_age_tv.setTextColor(mContext.getResources().getColor(R.color.color_8cd2ff));
        } else {
            Drawable sexDrawable = mContext.getResources().getDrawable(R.drawable.ic_sex_woman);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            holder.dating_detail_signup_age_tv.setCompoundDrawables(sexDrawable, null, null, null);
            holder.dating_detail_signup_age_tv.setTextColor(mContext.getResources().getColor(R.color.color_fe8cd9));
        }

        if(dto.isBirthdayPrivacy() || dto.getAge()==0){
            holder.dating_detail_signup_age_tv.setText("-");
        }else{
            holder.dating_detail_signup_age_tv.setText(dto.getAge()+"");
        }

        if (dto.isHot()) {
            holder.dating_detail_signup_hot_iv.setVisibility(View.VISIBLE);
        } else {
            holder.dating_detail_signup_hot_iv.setVisibility(View.GONE);
        }


        holder.dating_detail_signup_all_pbv.setProgress(dto.getSincerity());
        holder.dating_detail_signup_all_pbv.setProgressBar_max(100);
        holder.dating_detail_signup_all_pbv.setPromptTextIsDisplayable(true);
        holder.dating_detail_signup_all_pbv.setPromptTextCrompttext("诚意度");
        holder.dating_detail_signup_all_pbv.setOutside_round_style(ProgressBarView.STROKE_FILL);

        if (position == list.size() - 1) {
            holder.dating_detail_signup_bottom_view.setVisibility(View.GONE);
        } else {
            holder.dating_detail_signup_bottom_view.setVisibility(View.VISIBLE);
        }

        holder.dating_detail_signup_root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.USERID, dto.getUserId());
                ActivityUtil.jump(mContext, UserInfoActivity.class, bundle, 0, 100);
            }
        });


        return convertView;
    }


    // 重写的自定义ViewHolder
    public class ViewHolder {

        public LinearLayout dating_detail_signup_root_layout;

        public ImageView dating_detail_signup_userImg_iv;

        public TextView dating_detail_signup_name_tv;

        public TextView dating_detail_signup_age_tv;

        public ImageView dating_detail_signup_hot_iv;

        public ProgressBarView dating_detail_signup_all_pbv;

        public View dating_detail_signup_bottom_view;

    }

    public void setData(List<AppointOwner> list) {
        this.list = list;
    }

    public List<AppointOwner> getDatas() {
        return list;
    }

}
