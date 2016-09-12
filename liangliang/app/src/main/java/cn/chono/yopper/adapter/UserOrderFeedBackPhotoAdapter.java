package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;

/**
 * Created by cc on 16/5/5.
 */
public class UserOrderFeedBackPhotoAdapter extends BaseAdapter {


    ArrayList<String> photoData = new ArrayList<>();

    Context mContext;

    OnPhotoAdapterListener mOnPhotoAdapterListener;


    public UserOrderFeedBackPhotoAdapter(OnPhotoAdapterListener onPhotoAdapterListener, Context context) {
        mOnPhotoAdapterListener = onPhotoAdapterListener;
        mContext = context;
        photoData.add(YpSettings.ORDER_FEEDBACK_PHOTO);
    }


    public void addData(String photo) {
        photoData.remove(YpSettings.ORDER_FEEDBACK_PHOTO);
        photoData.add(photo);

        if (photoData.size() < 5)
            photoData.add(YpSettings.ORDER_FEEDBACK_PHOTO);

        notifyDataSetChanged();

    }

    public void deleteData(int position) {

        photoData.remove(position);

        notifyDataSetChanged();
    }

    public ArrayList<String> getPhotoData() {

        photoData.remove(YpSettings.ORDER_FEEDBACK_PHOTO);


        return photoData;

    }


    @Override
    public int getCount() {
        return photoData == null ? 0 : photoData.size();
    }

    @Override
    public Object getItem(int position) {
        return photoData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    FeedBackViewHolder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new FeedBackViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_feedback_photo, parent, false);
            holder.item_user_order_feekback_photo_iv_img = (ImageView) convertView.findViewById(R.id.item_user_order_feekback_photo_iv_img);
            holder.item_user_order_feekback_photo_iv_delete = (ImageView) convertView.findViewById(R.id.item_user_order_feekback_photo_iv_delete);
            convertView.setTag(holder);

            int itemHeight = (parent.getWidth()-16 - parent.getPaddingLeft() - parent.getPaddingRight()) / 5;

            convertView.getLayoutParams().height = itemHeight;

        } else {

            holder = (FeedBackViewHolder) convertView.getTag();
        }

        final String photo = photoData.get(position);


        if (TextUtils.equals(YpSettings.ORDER_FEEDBACK_PHOTO, photo)) {

            Glide.with(mContext).load(R.drawable.feedback_add_icon).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.item_user_order_feekback_photo_iv_img);

            holder.item_user_order_feekback_photo_iv_delete.setVisibility(View.GONE);

            if (mOnPhotoAdapterListener == null) {
                return convertView;
            }

            holder.item_user_order_feekback_photo_iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPhotoAdapterListener.OnAddListener();
                }
            });

            return convertView;
        }
        Glide.with(mContext).load(photo).centerCrop().into(holder.item_user_order_feekback_photo_iv_img);

        holder.item_user_order_feekback_photo_iv_delete.setVisibility(View.VISIBLE);

        if (mOnPhotoAdapterListener == null) {
            return convertView;
        }

        holder.item_user_order_feekback_photo_iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPhotoAdapterListener.OnDeleteListener(position, photo);
            }
        });

        holder.item_user_order_feekback_photo_iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPhotoAdapterListener.OnItemListener(position, photo, getPhotoData());
            }
        });


        return convertView;
    }

    public class FeedBackViewHolder {

        ImageView item_user_order_feekback_photo_iv_delete;
        ImageView item_user_order_feekback_photo_iv_img;


    }


    public interface OnPhotoAdapterListener {

        void OnDeleteListener(int position, String photo);

        void OnItemListener(int position, String photo, ArrayList<String> photoData);

        void OnAddListener();
    }
}
