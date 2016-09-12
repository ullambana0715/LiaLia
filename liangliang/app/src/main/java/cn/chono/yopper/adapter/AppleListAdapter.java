package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.Products.AppleInfoEntity;
import cn.chono.yopper.Service.Http.Products.ProductsListEntity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;

/**
 * Created by cc on 16/5/5.
 */
public class AppleListAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    int VIEW_APPLE = 1;

    int VIEW_NUMBER = 0;

    Context context;

    OnAdapterItemClickLitener mOnAdapterItemClickLitener;

    List<ProductsListEntity> mProductsListEntities = new ArrayList<>();

    AppleInfoEntity mAppleInfoEntity;

    public AppleInfoEntity getAppleInfoEntity() {
        return mAppleInfoEntity;
    }

    public void setPrivacyList(List<ProductsListEntity> privacyList) {
        mProductsListEntities = privacyList;
        notifyDataSetChanged();
    }

    public List<ProductsListEntity> getProductsListEntities() {
        return mProductsListEntities;
    }

    public void addPrivacyList(List<ProductsListEntity> privacyList) {
        mProductsListEntities.addAll(privacyList);
        notifyDataSetChanged();
    }

    public void setAppleInfoEntity(AppleInfoEntity appleInfoEntity) {
        mAppleInfoEntity = appleInfoEntity;
        notifyDataSetChanged();
    }

    public AppleListAdapter(Context context, OnAdapterItemClickLitener onAdapterItemClickLitener) {
        this.context = context;
        mOnAdapterItemClickLitener = onAdapterItemClickLitener;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new AppLeViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        if (viewType == VIEW_NUMBER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apple_number, parent, false);
            return new NumberViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apple_list, parent, false);

        return new AppLeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, boolean isItem) {


        if (holder instanceof NumberViewHolder) {

            ((NumberViewHolder) holder).item_apple_list_tv_number.setText(mAppleInfoEntity.availableBalance + "");


            return;
        }


        if (holder instanceof AppLeViewHolder) {

            final ProductsListEntity productsListEntity = mProductsListEntities.get(getPosition(position));

            String imaUrl = ImgUtils.DealImageUrl(productsListEntity.imageUrl, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

            Glide.with(context).load(imaUrl).centerCrop().into(((AppLeViewHolder) holder).item_apple_list_iv_icon);

            ((AppLeViewHolder) holder).item_apple_list_tv_apple_number.setText(productsListEntity.productName);

            ((AppLeViewHolder) holder).item_apple_list_tv_apple_hint.setText(productsListEntity.description);

            if (productsListEntity.isHighlight) {
                ((AppLeViewHolder) holder).item_apple_list_tv_apple_hint.setTextColor(context.getResources().getColor(R.color.color_ff7462));
            } else {
                ((AppLeViewHolder) holder).item_apple_list_tv_apple_hint.setTextColor(context.getResources().getColor(R.color.color_b2b2b2));
            }


            StringBuilder sb = new StringBuilder("￥");

            sb.append((productsListEntity.price / 100));

            ((AppLeViewHolder) holder).item_apple_list_tv_apple_pay.setText(sb.toString());


            if (mOnAdapterItemClickLitener == null) {
                return;
            }


            ((AppLeViewHolder) holder).item_apple_list_tv_apple_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAdapterItemClickLitener.onAdapterItemClick(getPosition(position), productsListEntity);
                }
            });


            return;
        }


    }

    private int getPosition(int position) {

        if (mAppleInfoEntity == null) {
            return position;
        }
        return position - 1;
    }

    @Override
    public int getAdapterItemCount() {
        int count;

        if (mAppleInfoEntity == null) {
            count = mProductsListEntities.size();
        } else {
            count = mProductsListEntities.size() + 1;
        }

        return count;
    }

    @Override
    public int getAdapterItemViewType(int position) {


        if (mAppleInfoEntity == null) {
            return VIEW_APPLE;
        }

        if (position == 0) {
            return VIEW_NUMBER;
        }
        return VIEW_APPLE;


    }

    public class AppLeViewHolder extends RecyclerView.ViewHolder {

        ImageView item_apple_list_iv_icon;

        TextView item_apple_list_tv_apple_number;
        TextView item_apple_list_tv_apple_hint;
        TextView item_apple_list_tv_apple_pay;


        public AppLeViewHolder(View itemView) {
            super(itemView);

            item_apple_list_iv_icon = (ImageView) itemView.findViewById(R.id.item_apple_list_iv_icon);
            item_apple_list_tv_apple_number = (TextView) itemView.findViewById(R.id.item_apple_list_tv_apple_number);
            item_apple_list_tv_apple_hint = (TextView) itemView.findViewById(R.id.item_apple_list_tv_apple_hint);
            item_apple_list_tv_apple_pay = (TextView) itemView.findViewById(R.id.item_apple_list_tv_apple_pay);
        }
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {

        TextView item_apple_list_tv_number;

        public NumberViewHolder(View itemView) {
            super(itemView);

            item_apple_list_tv_number = (TextView) itemView.findViewById(R.id.item_apple_list_tv_number);
        }
    }
}
