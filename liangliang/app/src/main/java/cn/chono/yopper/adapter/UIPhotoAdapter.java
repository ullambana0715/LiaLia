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
import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.UserPhoto;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/7/22.
 */
public class UIPhotoAdapter<T> extends BaseRecyclerAdapter<UIPhotoAdapter.ItemViewHolder> {

    Context mContext;

    List<T> list = new ArrayList<>();

    private int itemHeight;


    public UIPhotoAdapter(Context context) {

        mContext = context;

        itemHeight = (UnitUtil.getScreenWidthPixels(mContext) - UnitUtil.dip2px(20, mContext)) / 6;

    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();


    }

    public void addList(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public ItemViewHolder getViewHolder(View view) {
        return new ItemViewHolder(view, false);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_photo, parent, false);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);


        param.height = itemHeight;

        param.width = itemHeight;


        view.setLayoutParams(param);


        return new ItemViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position, boolean isItem) {


        UserPhoto userPhoto = (UserPhoto) list.get(position);

        String photo = userPhoto.getImageUrl();


        if (TextUtils.equals(YpSettings.suppose, photo)) {

            Logger.e("进来了=" + photo);

            holder.mUIPhotoIcon.setPadding(10, 10, 10, 10);

            Glide.with(mContext).load(R.drawable.ic_upload).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.mUIPhotoIcon);

//            holder.mUIPhotoIcon.setImageResource(R.drawable.ic_upload);


            holder.mUIPhotoIconLock.setBackgroundResource(0);


            holder.mUIPhotoIcon.setOnClickListener(v -> {


                CommonItemEvent commonItemEvent = new CommonItemEvent();

                commonItemEvent.position = position;


                RxBus.get().post("AddPhotoEvent", commonItemEvent);


            });


        } else if (TextUtils.equals(YpSettings.album_invite, photo)) {

            Logger.e("你也要进来了=" + photo);

            holder.mUIPhotoIcon.setPadding(10, 10, 10, 10);


//            holder.mUIPhotoIcon.setImageResource(R.drawable.ic_invite_upload);

            Glide.with(mContext).load(R.drawable.ic_anvite_upload).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.mUIPhotoIcon);

            holder.mUIPhotoIconLock.setBackgroundResource(0);

            holder.mUIPhotoIcon.setOnClickListener(v -> {

                CommonItemEvent commonItemEvent = new CommonItemEvent();

                commonItemEvent.position = position;


                RxBus.get().post("InviteAddPhotoEvent", commonItemEvent);


            });


        } else {


            Logger.e("你是不是傻=" + photo);

            holder.mUIPhotoIcon.setPadding(0, 0, 0, 0);

            String imgUrl = ImgUtils.DealImageUrl(photo, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);


            Glide.with(mContext).load(imgUrl).into(holder.mUIPhotoIcon);

            holder.mUIPhotoIconLock.setBackgroundResource(0);

            holder.mUIPhotoIcon.setOnClickListener(v -> {

                CommonItemEvent commonItemEvent = new CommonItemEvent();

                commonItemEvent.position = position;


                RxBus.get().post("ItmePhotoEvent", commonItemEvent);


            });


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


        public ItemViewHolder(View itemView, boolean isItem) {
            super(itemView);

            if (isItem)
                ButterKnife.bind(this, itemView);


        }
    }
}
