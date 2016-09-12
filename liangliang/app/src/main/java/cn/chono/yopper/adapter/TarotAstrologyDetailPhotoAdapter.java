package cn.chono.yopper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/4/27.
 */
public class TarotAstrologyDetailPhotoAdapter extends BaseAdapter {

    Context mContext;

    String[] mAlbumImages;

    OnAdapterItemClickLitener mOnAdapterItemClickLitener;

    public void setOnAdapterItemClickLitener(OnAdapterItemClickLitener onAdapterItemClickLitener) {
        mOnAdapterItemClickLitener = onAdapterItemClickLitener;
    }

    public TarotAstrologyDetailPhotoAdapter(Context context) {

        mContext = context;


    }


    public void setData(String[] albumImages) {
        mAlbumImages = albumImages;
        notifyDataSetChanged();
    }


    PhotoViewHolder mPhotoViewHolder;

    @Override
    public int getCount() {

        int count = mAlbumImages == null ? 0 : mAlbumImages.length;
        if (count > 4) {
            count = 4;
        }

        return count;
    }

    @Override
    public Object getItem(int position) {
        return mAlbumImages[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology_detail_photo, parent, false);
            mPhotoViewHolder = new PhotoViewHolder();
            mPhotoViewHolder.item_tarot_astrology_detail_photo_iv_icon = (ImageView) convertView.findViewById(R.id.item_tarot_astrology_detail_photo_iv_icon);

            convertView.setTag(mPhotoViewHolder);


            int w = UnitUtil.dip2px(24, mContext);


            GridView.LayoutParams params = (GridView.LayoutParams) convertView.getLayoutParams();

            params.width = (UnitUtil.getScreenWidthPixels(mContext) - w) / 4;
            params.height = (UnitUtil.getScreenWidthPixels(mContext) - w) / 4;


            convertView.setLayoutParams(params);


        } else {
            mPhotoViewHolder = (PhotoViewHolder) convertView.getTag();
        }

        String iconUrl = ImgUtils.DealImageUrl(mAlbumImages[position], YpSettings.IMG_SIZE_150,
                YpSettings.IMG_SIZE_150);

        Glide.with(mContext).load(iconUrl).centerCrop().into(mPhotoViewHolder.item_tarot_astrology_detail_photo_iv_icon);

        mPhotoViewHolder.item_tarot_astrology_detail_photo_iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAdapterItemClickLitener != null) {
                    mOnAdapterItemClickLitener.onAdapterItemClick(position, mAlbumImages);
                }
            }
        });


        return convertView;
    }

    public class PhotoViewHolder {

        private ImageView item_tarot_astrology_detail_photo_iv_icon;


    }
}
