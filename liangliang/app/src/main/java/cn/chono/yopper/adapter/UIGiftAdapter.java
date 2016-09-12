package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.data.MyGiftSum;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.StringUtils;

/**
 * Created by cc on 16/7/22.
 */
public class UIGiftAdapter<T> extends BaseRecyclerAdapter<UIGiftAdapter.ItemViewHolder> {

    Context mContext;

    List<T> list = new ArrayList<>();


    public UIGiftAdapter(Context context) {

        mContext = context;


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


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_gift, parent, false);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position, boolean isItem) {

        if (position == list.size() - 1)
            holder.mUIGiftRight.setVisibility(View.VISIBLE);
        else {
            holder.mUIGiftRight.setVisibility(View.GONE);
        }

        MyGiftSum myGiftSum = (MyGiftSum) list.get(position);

        String imageurl = ImgUtils.DealImageUrl(myGiftSum.getGift().getImageUrl(), 150, 150);


        Glide.with(mContext).load(imageurl).into(holder.mUIGiftIcon);


        holder.mUIGiftTvName.setText(StringUtils.isEmpty(myGiftSum.getGift().getGiftName()));


        if (myGiftSum.getSum() == 0) {

            holder.mUIGiftTvNumber.setText("");
        } else {
            holder.mUIGiftTvNumber.setText("x" + myGiftSum.getSum());
        }


    }

    @Override
    public int getAdapterItemCount() {

        return list == null ? 0 : list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.UIGift_lift)
        Space mUIGiftLift;
        @BindView(R.id.UIGift_icon)
        ImageView mUIGiftIcon;
        @BindView(R.id.UIGift_tv_name)
        TextView mUIGiftTvName;
        @BindView(R.id.UIGift_tv_number)
        TextView mUIGiftTvNumber;
        @BindView(R.id.UIGift_right)
        Space mUIGiftRight;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
