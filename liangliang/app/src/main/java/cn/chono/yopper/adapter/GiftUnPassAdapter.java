package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.entity.chatgift.GiftInfoEntity;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.glide.CropCircleTransformation;

/**
 * Created by yangjinyu on 16/8/2.
 */
public class GiftUnPassAdapter extends BaseRecyclerAdapter<GiftUnPassAdapter.NewHolderView> {


    private List<GiftInfoEntity> list;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    private Context context;

    public GiftUnPassAdapter(Context c) {
        context = c;

        mPool = Glide.get(context).getBitmapPool();

        transformation = new CropCircleTransformation(mPool);
    }


    @Override
    public NewHolderView getViewHolder(View view) {
        return new NewHolderView(view);
    }

    public void setData(List<GiftInfoEntity> l) {
        this.list = l;
    }

    public void addAll(List<GiftInfoEntity> l) {
        if (null == list) {
            list = new ArrayList<>();
        }

        list.addAll(l);
    }

    @Override
    public NewHolderView onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gift, parent, false);
        return new NewHolderView(view);
    }

    @Override
    public void onBindViewHolder(NewHolderView holder, int position, boolean isItem) {

        GiftInfoEntity giftDto = list.get(position);

        if (!TextUtils.isEmpty(giftDto.getGiftName())) {

            holder.itemGiftName.setText(giftDto.getGiftName());
        }


        holder.itemGiftAttract.setText("魅力值+" + giftDto.getCharm());

        holder.itemGiftApple.setText(giftDto.getAppleCount() + "个苹果");


        if (!TextUtils.isEmpty(giftDto.getImageUrl())) {

            Glide.with(context).load(giftDto.getImageUrl()).bitmapTransform(transformation).into(holder.itemGiftIcon);
        }

        holder.itemGiftLy.setOnClickListener(v->{

                RxBus.get().post("onClickNoPassGift", new CommonEvent<>(giftDto));

        });
    }

    @Override
    public int getAdapterItemCount() {
        return null != list ? list.size() : 0;
    }

    class NewHolderView extends RecyclerView.ViewHolder {

        @BindView(R.id.item_gift_icon)
        ImageView itemGiftIcon;

        @BindView(R.id.item_gift_name)
        TextView itemGiftName;

        @BindView(R.id.item_gift_attract)
        TextView itemGiftAttract;

        @BindView(R.id.item_gift_apple)
        TextView itemGiftApple;

        @BindView(R.id.item_gift_ly)
        RelativeLayout itemGiftLy;

        public NewHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
