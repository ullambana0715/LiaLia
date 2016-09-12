package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.CounselorList.CounselorsList;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;

/**
 * Created by cc on 16/4/26.
 */
public class TarotOrAstrologyListAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    int mCounselorType;


    Context mContext;

    List<CounselorsList> mCounselorsLists = new ArrayList<>();

    OnAdapterItemClickLitener mOnAdapterItemClickLitener;


    CropCircleTransformation mCropCircleTransformation;

    BitmapPool mPool;


    public List<CounselorsList> getCounselorsLists() {
        return mCounselorsLists;
    }

    public void setData(List<CounselorsList> data) {

        mCounselorsLists = data;
        notifyDataSetChanged();
    }

    public void addData(List<CounselorsList> data) {

        mCounselorsLists.addAll(data);
        notifyDataSetChanged();
    }


    public TarotOrAstrologyListAdapter(Context context, int counselorType) {

        this.mContext = context;

        mCounselorType = counselorType;


        mPool = Glide.get(mContext).getBitmapPool();

        mCropCircleTransformation = new CropCircleTransformation(mPool);
    }

    public List<CounselorsList> getData() {
        return mCounselorsLists;

    }

    public void setOnAdapterItemClickLitener(OnAdapterItemClickLitener onAdapterItemClickLitener) {
        mOnAdapterItemClickLitener = onAdapterItemClickLitener;
    }


    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolders(view, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology, parent, false);
        return new ViewHolders(view, true);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, boolean isItem) {

        if (position == getAdapterItemCount() - 1) {
            ((ViewHolders) holder).item_tarot_astrology_view.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolders) holder).item_tarot_astrology_view.setVisibility(View.GONE);
        }


        final CounselorsList counselors = mCounselorsLists.get(position);
        if (counselors == null) return;


        String iconUrl = ImgUtils.DealImageUrl(counselors.avatar, YpSettings.IMG_SIZE_150,
                YpSettings.IMG_SIZE_150);


        Glide.with(mContext).load(iconUrl).bitmapTransform(mCropCircleTransformation).into(((ViewHolders) holder).item_tarot_astrology_iv_icon);


        ((ViewHolders) holder).item_tarot_astrology_tv_name.setText(counselors.nickName);

        ((ViewHolders) holder).item_tarot_astrology_rbar_score.setRating((float) counselors.totalStarLevel);

        setTarotAstrologrLabel(((ViewHolders) holder).item_tarot_astrology_ll_label, counselors.skillTags);


        ((ViewHolders) holder).item_tarot_astrology_tv_reply.setText(mContext.getResources().getString(R.string.answer));
        if (counselors.totalAnswerCount == 0) {

            ((ViewHolders) holder).item_tarot_astrology_tv_reply.setVisibility(View.GONE);
            ((ViewHolders) holder).item_tarot_astrology_tv_reply_status.setVisibility(View.GONE);


        } else if (counselors.totalAnswerCount > 99999) {
            ((ViewHolders) holder).item_tarot_astrology_tv_reply_status.setText("99999+");
            ((ViewHolders) holder).item_tarot_astrology_tv_reply.setVisibility(View.VISIBLE);
            ((ViewHolders) holder).item_tarot_astrology_tv_reply_status.setVisibility(View.VISIBLE);

        } else {
            ((ViewHolders) holder).item_tarot_astrology_tv_reply_status.setText(counselors.totalAnswerCount + "");
            ((ViewHolders) holder).item_tarot_astrology_tv_reply.setVisibility(View.VISIBLE);
            ((ViewHolders) holder).item_tarot_astrology_tv_reply_status.setVisibility(View.VISIBLE);

        }


        StringBuilder sb = new StringBuilder();

        sb.append((counselors.charge / 100));


        ((ViewHolders) holder).item_tarot_astrology_tv_price.setText(sb.toString());

        if (mCounselorType == Constant.CounselorType_Tarot) {
            ((ViewHolders) holder).item_tarot_astrology_tv_price_unit.setText("/问题");
        } else if (mCounselorType == Constant.CounselorType_Astrology) {
            ((ViewHolders) holder).item_tarot_astrology_tv_price_unit.setText("/星盘");
        } else if (mCounselorType == Constant.CounselorType_Psychological) {//心理咨询师

        }

        ((ViewHolders) holder).item_tarot_diviner_info.setText(counselors.getDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnAdapterItemClickLitener)
                    mOnAdapterItemClickLitener.onAdapterItemClick(position, counselors.userId);
            }
        });
    }

    @Override
    public int getAdapterItemCount() {


        return mCounselorsLists == null ? 0 : mCounselorsLists.size();
    }


    private void setTarotAstrologrLabel(LinearLayout linearLayout, String[] list) {


        linearLayout.removeAllViews();

        if (null == list || list.length < 1) {
            return;
        }
        for (int i = 0; i < list.length; i++) {
            if (i > 4) break;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 0, 12, 0);


            TextView textView = new TextView(mContext);

            textView.setTextSize(12);

            textView.setText(list[i]);


            textView.setMinWidth(45);

            textView.setGravity(Gravity.CENTER);

            textView.setPadding(30, 10, 30, 10);

            textView.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));

            textView.setBackgroundResource(R.color.color_f5f5f5);

            textView.setLayoutParams(params);

            linearLayout.addView(textView);

        }


    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        ImageView item_tarot_astrology_iv_icon;
        TextView item_tarot_astrology_tv_name;
        RatingBar item_tarot_astrology_rbar_score;
        LinearLayout item_tarot_astrology_ll_label;

        TextView item_tarot_astrology_tv_reply_status;
        TextView item_tarot_astrology_tv_reply;

        TextView item_tarot_astrology_tv_price;
        TextView item_tarot_astrology_tv_price_unit;

        View item_tarot_astrology_view;
        TextView item_tarot_diviner_info;


        public ViewHolders(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                item_tarot_astrology_iv_icon = (ImageView) itemView.findViewById(R.id.item_tarot_astrology_iv_icon);
                item_tarot_astrology_tv_name = (TextView) itemView.findViewById(R.id.item_tarot_astrology_tv_name);
                item_tarot_astrology_rbar_score = (RatingBar) itemView.findViewById(R.id.item_tarot_astrology_rbar_score);
                item_tarot_astrology_ll_label = (LinearLayout) itemView.findViewById(R.id.item_tarot_astrology_ll_label);

                item_tarot_astrology_tv_reply_status = (TextView) itemView.findViewById(R.id.item_tarot_astrology_tv_reply_status);
                item_tarot_astrology_tv_reply = (TextView) itemView.findViewById(R.id.item_tarot_astrology_tv_reply);

                item_tarot_astrology_tv_price = (TextView) itemView.findViewById(R.id.item_tarot_astrology_tv_price);
                item_tarot_astrology_tv_price_unit = (TextView) itemView.findViewById(R.id.item_tarot_astrology_tv_price_unit);

                item_tarot_astrology_view = itemView.findViewById(R.id.item_tarot_astrology_view);

                item_tarot_diviner_info = (TextView) itemView.findViewById(R.id.diviner_info);

            }

        }
    }


}
