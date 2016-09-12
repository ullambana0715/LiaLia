package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.entity.PrivacyAlbum;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.glide.BlurTransformation;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/7/22.
 */
public class UIPrivatePhotoAdapter<T> extends BaseRecyclerAdapter<UIPrivatePhotoAdapter.ItemViewHolder> {

    Context mContext;

    List<T> list = new ArrayList<>();

    boolean isUnlockPrivacyAlbum;

    private int itemHeight;

    BitmapPool mPool;

    BlurTransformation mBlurTransformation;

    public UIPrivatePhotoAdapter(Context context) {

        mContext = context;

        itemHeight = (UnitUtil.getScreenWidthPixels(mContext) - UnitUtil.dip2px(20, mContext)) / 6;

        mPool = Glide.get(mContext).getBitmapPool();

        mBlurTransformation = new BlurTransformation(mContext, mPool, 8, 8);
    }

    public void setList(List<T> list, boolean isUnlockPrivacyAlbum) {
        this.list = list;
        this.isUnlockPrivacyAlbum = isUnlockPrivacyAlbum;
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public ItemViewHolder getViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_photo, parent, false);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);


        param.height = itemHeight;

        param.width = itemHeight;


        view.setLayoutParams(param);


        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position, boolean isItem) {

        PrivacyAlbum privacyAlbum = (PrivacyAlbum) list.get(position);

        String pri = privacyAlbum.getImageUrl();


        if (TextUtils.equals(YpSettings.suppose, pri)) {

            holder.mUIPhotoIcon.setPadding(10, 10, 10, 10);

            Glide.with(mContext).load(R.drawable.ic_upload).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.mUIPhotoIcon);

            holder.mUIPhotoIconLock.setBackgroundResource(0);

            holder.itemView.setOnClickListener(view -> {


                CommonItemEvent commonItemEvent = new CommonItemEvent();

                commonItemEvent.position = position;

                RxBus.get().post("AddPrivatePhotoEvent", commonItemEvent);


            });


        } else {

            holder.mUIPhotoIcon.setPadding(0, 0, 0, 0);


            if (isUnlockPrivacyAlbum) {

                holder.mUIPhotoIconLock.setBackgroundResource(0);


                holder.itemView.setOnClickListener(view -> {

                    //可查看


                    CommonItemEvent commonItemEvent = new CommonItemEvent();

                    commonItemEvent.position = position;

                    RxBus.get().post("ItemPrivatePhotoEvent", commonItemEvent);


                });

                String imgUrl = ImgUtils.DealImageUrl(pri, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

                Glide.with(mContext).load(imgUrl).error(R.drawable.error_user_icon).into(holder.mUIPhotoIcon);


            } else {


                //不可查看


                holder.mUIPhotoIconLock.setBackgroundResource(R.drawable.ic_private);


                holder.itemView.setOnClickListener(view -> {


                    CommonItemEvent commonItemEvent = new CommonItemEvent();

                    commonItemEvent.position = position;

                    RxBus.get().post("ItemVerificationPrivatePhotoEvent", commonItemEvent);


                });

                String imgUrl = ImgUtils.DealImageUrl(pri, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

                Glide.with(mContext).load(imgUrl).error(R.drawable.error_user_icon).bitmapTransform(mBlurTransformation).into(holder.mUIPhotoIcon);


            }


        }

    }

    @Override
    public int getAdapterItemCount() {

        return list == null ? 0 : list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.UIPhoto_icon)
        ImageView mUIPhotoIcon;

        @BindView(R.id.UIPhoto_icon_lock)
        ImageView mUIPhotoIconLock;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
