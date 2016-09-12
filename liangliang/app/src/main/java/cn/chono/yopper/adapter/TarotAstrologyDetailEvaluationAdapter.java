package cn.chono.yopper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationsListEntity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterIconClickLitener;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;

/**
 * Created by cc on 16/4/27.
 */
public class TarotAstrologyDetailEvaluationAdapter extends BaseAdapter {

    Context mContext;

    EvalutionViewHolder mEvalutionViewHolder;

    List<EvaluationsListEntity> mListEntities = new ArrayList<>();

    CropCircleTransformation mCropCircleTransformation;

    OnAdapterIconClickLitener mOnAdapterIconClickLitener;

    OnAdapterItemClickLitener mOnAdapterItemClickLitener;

    public void setOnAdapterItemClickLitener(OnAdapterItemClickLitener onAdapterItemClickLitener) {
        mOnAdapterItemClickLitener = onAdapterItemClickLitener;
    }

    public void setOnAdapterIconClickLitener(OnAdapterIconClickLitener onAdapterIconClickLitener) {
        mOnAdapterIconClickLitener = onAdapterIconClickLitener;
    }

    public TarotAstrologyDetailEvaluationAdapter(Context context) {

        mContext = context;

        mCropCircleTransformation = new CropCircleTransformation( Glide.get(context).getBitmapPool());
    }

    public void setData(List<EvaluationsListEntity> listEntities) {
        mListEntities = listEntities;
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return mListEntities == null ? 0 : mListEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return mListEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (null == convertView) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology_detail_evaluation, parent, false);

            mEvalutionViewHolder = new EvalutionViewHolder();

            mEvalutionViewHolder.item_tarot_astrology_detail_evalution_iv_user_icon = (ImageView) convertView.findViewById(R.id.item_tarot_astrology_detail_evalution_iv_user_icon);
            mEvalutionViewHolder.item_tarot_astrology_detail_evalution_tv_user_name = (TextView) convertView.findViewById(R.id.item_tarot_astrology_detail_evalution_tv_user_name);
            mEvalutionViewHolder.item_tarot_astrology_detail_evalution_tv_content = (TextView) convertView.findViewById(R.id.item_tarot_astrology_detail_evalution_tv_content);
            convertView.setTag(mEvalutionViewHolder);

        } else mEvalutionViewHolder = (EvalutionViewHolder) convertView.getTag();


        final EvaluationsListEntity evaluationsListEntity = mListEntities.get(position);

        String iconUrl = ImgUtils.DealImageUrl(evaluationsListEntity.user.headImg, YpSettings.IMG_SIZE_150,
                YpSettings.IMG_SIZE_150);

        Glide.with(mContext).load(iconUrl).bitmapTransform(mCropCircleTransformation).into(mEvalutionViewHolder.item_tarot_astrology_detail_evalution_iv_user_icon);


        mEvalutionViewHolder.item_tarot_astrology_detail_evalution_tv_user_name.setText(evaluationsListEntity.user.name);

        mEvalutionViewHolder.item_tarot_astrology_detail_evalution_tv_content.setText(evaluationsListEntity.description);


        if (mOnAdapterIconClickLitener==null){
            return convertView;
        }
        mEvalutionViewHolder.item_tarot_astrology_detail_evalution_iv_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAdapterIconClickLitener.onAdapterIconClick(position,evaluationsListEntity.user.userId);
            }
        });

        if (mOnAdapterItemClickLitener==null)
            return convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAdapterItemClickLitener.onAdapterItemClick(position,null);
            }
        });



        return convertView;
    }

    public class EvalutionViewHolder {


        ImageView item_tarot_astrology_detail_evalution_iv_user_icon;

        TextView item_tarot_astrology_detail_evalution_tv_user_name;

        TextView item_tarot_astrology_detail_evalution_tv_content;


    }
}
