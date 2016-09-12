package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;

/**
 * Created by cc on 16/3/16.
 */
public class TarotAstrologyPhotoAdapter extends BaseRecyclerAdapter<TarotAstrologyPhotoAdapter.NewViewHolder> {


    Context context;

    String[] mAlbumImages;

    OnAdapterItemClickLitener mOnAdapterItemClickLitener;


    public TarotAstrologyPhotoAdapter(Context context) {
        this.context = context;
    }

    public void setData(String[] albumImages) {
        mAlbumImages = albumImages;



    }

    public void setOnAdapterItemClickLitener(OnAdapterItemClickLitener onAdapterItemClickLitener) {
        mOnAdapterItemClickLitener = onAdapterItemClickLitener;
    }

    @Override
    public NewViewHolder getViewHolder(View view) {
        return new NewViewHolder(view, false);
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology_photo, parent, false);
//
        int itemHeight = (parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight()) / 3;

        view.getLayoutParams().height = itemHeight;

        return new NewViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final NewViewHolder holder, final int position, boolean isItem) {

        String imageurl = ImgUtils.DealImageUrl(mAlbumImages[position], YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);



        Glide.with(context).load(imageurl).into(holder.item_tarot_astrology_photo_iv_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAdapterItemClickLitener != null) {
                    mOnAdapterItemClickLitener.onAdapterItemClick(position, mAlbumImages);
                }
            }
        });

    }


    @Override
    public int getAdapterItemCount() {
        return mAlbumImages == null ? 0 : mAlbumImages.length;
    }


    public class NewViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_tarot_astrology_photo_iv_img;
        public ImageView item_tarot_astrology_photo_iv_bg;


        public NewViewHolder(View itemView, boolean isItem) {
            super(itemView);


            if (isItem) {


                item_tarot_astrology_photo_iv_img = (ImageView) itemView.findViewById(R.id.item_tarot_astrology_photo_iv_img);
                item_tarot_astrology_photo_iv_bg = (ImageView) itemView.findViewById(R.id.item_tarot_astrology_photo_iv_bg);

            }
        }
    }
}
