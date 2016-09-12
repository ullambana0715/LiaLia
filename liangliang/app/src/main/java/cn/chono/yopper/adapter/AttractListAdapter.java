package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.entity.charm.ReceiveGiftInfoEntity;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.TimeUtils;

/**
 * Created by yangjinyu on 16/7/29.
 */
public class AttractListAdapter<T> extends BaseRecyclerAdapter<AttractListAdapter.AttractHolderView> {


    private Context context;

    private List<T> list;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    public AttractListAdapter(Context c) {

        context = c;

        mPool = Glide.get(context).getBitmapPool();

        transformation = new CropCircleTransformation(mPool);
    }

    @Override
    public AttractHolderView getViewHolder(View view) {
        return new AttractHolderView(view, false);
    }

    public void addList(List<T> l) {

        if (null == list) {

            list = new ArrayList<>();
        }

        list.addAll(l);
    }

    public void setList(List<T> l) {
        list = l;
    }

    @Override
    public AttractHolderView onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_attractlist, parent, false);

        return new AttractHolderView(view, true);
    }

    @Override
    public void onBindViewHolder(AttractHolderView holder, int position, boolean isItem) {

        if (position == list.size() - 1) {

            holder.mItemCharmGiftTopLine.setVisibility(View.INVISIBLE);

            holder.mItemCharmGiftBottomLine.setVisibility(View.INVISIBLE);

        } else {

            holder.mItemCharmGiftTopLine.setVisibility(View.VISIBLE);

            holder.mItemCharmGiftBottomLine.setVisibility(View.VISIBLE);

        }

        ReceiveGiftInfoEntity dto = (ReceiveGiftInfoEntity) list.get(position);

        long time = ISO8601.getTime(dto.getSignForTime());

        String timestr = TimeUtil.getTime(time, "yy-MM-dd HH:mm:ss");

        holder.mItemCharmGiftReceiveTimeTv.setText(timestr);


        holder.mItemCharmUsernameTv.setText(dto.getGiver().getName());

        String userImg = ImgUtils.DealImageUrl(dto.getGiver().getHeadImg(), 150, 150);

        Glide.with(context).load(userImg)
                .bitmapTransform(transformation)
                .into(holder.mItemCharmUsericonIv);

        String giftImg = ImgUtils.DealImageUrl(dto.getGift().getImageUrl(), 50, 50);

        Glide.with(context).load(giftImg).into(holder.mItemCharmGiftIcon);

        holder.mItemCharmAddTv.setText("+" + dto.getIncreasedCharm());


        holder.mItemCharmSexAgeHorsecopeTv.setCompoundDrawablesWithIntrinsicBounds(dto.getGiver().getSex() == 1 ? R.drawable.ic_attract_man : R.drawable.ic_attract_woman, 0, 0, 0);


        String targetHorsecope = CheckUtil.ConstellationMatching(dto.getGiver().getHoroscope());


        if (dto.getGiver().isBirthdayPrivacy()) {
            holder.mItemCharmSexAgeHorsecopeTv.setText("-" + " " + targetHorsecope);
        } else {
            holder.mItemCharmSexAgeHorsecopeTv.setText(dto.getGiver().getAge() + " " + targetHorsecope);
        }


        holder.mItemCharmGiftName.setText(dto.getGift().getGiftName());
    }

    @Override
    public int getAdapterItemCount() {
        return list == null ? 0 : list.size();
    }

    static class AttractHolderView extends RecyclerView.ViewHolder {


        @BindView(R.id.item_charm_gift_top_line)
        View mItemCharmGiftTopLine;

        @BindView(R.id.item_charm_add_tv)
        TextView mItemCharmAddTv;

        @BindView(R.id.item_charm_gift_receive_time_tv)
        TextView mItemCharmGiftReceiveTimeTv;

        @BindView(R.id.item_charm_gift_bottom_line)
        RelativeLayout mItemCharmGiftBottomLine;

        @BindView(R.id.item_charm_usericon_iv)
        ImageView mItemCharmUsericonIv;

        @BindView(R.id.item_charm_username_tv)
        TextView mItemCharmUsernameTv;

        @BindView(R.id.item_charm_sex_age_horsecope_tv)
        TextView mItemCharmSexAgeHorsecopeTv;

        @BindView(R.id.item_charm_gift_name)
        TextView mItemCharmGiftName;

        @BindView(R.id.item_charm_gift_icon)
        ImageView mItemCharmGiftIcon;

        public AttractHolderView(View view, boolean isitme) {

            super(view);

            if (isitme)

                ButterKnife.bind(this, view);
        }
    }


}
