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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/7/22.
 */
public class UIVideoAdapter<T> extends BaseRecyclerAdapter<UIVideoAdapter.ItemViewHolder> {

    Context mContext;

    List<T> list = new ArrayList<>();


    private  int itemHeight;

    public UIVideoAdapter(Context context) {

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




        GeneralVideos generalVideos = (GeneralVideos) list.get(position);


        if (TextUtils.equals(YpSettings.suppose, generalVideos.getCoverImgUrl())) {


            holder.mUIPhotoIcon.setPadding(10, 10, 10, 10);

            Glide.with(mContext).load(R.drawable.ic_upload).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.mUIPhotoIcon);



            holder.mUIPhotoIconLock.setImageResource(0);

            holder.itemView.setOnClickListener(view -> {

                CommonItemEvent commonItemEvent = new CommonItemEvent();

                commonItemEvent.position = position;

                //添加
                RxBus.get().post("AddItemVideoLoindingEvent", commonItemEvent);


            });


        } else {

            holder.mUIPhotoIcon.setPadding(0, 0, 0, 0);


            String coverImgUrl = ImgUtils.DealVideoImageUrl(generalVideos.getCoverImgUrl(), itemHeight, itemHeight);


            Glide.with(mContext).load(coverImgUrl).into(holder.mUIPhotoIcon);


            holder.mUIPhotoIconLock.setImageResource(R.drawable.ic_play);

            holder.itemView.setOnClickListener(view -> {

                CommonItemEvent commonItemEvent = new CommonItemEvent();

                commonItemEvent.event=generalVideos;

                commonItemEvent.position = position;

                //查看
                RxBus.get().post("ItemVideoLoindingEvent", commonItemEvent);


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


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);




        }
    }
}
