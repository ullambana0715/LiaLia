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
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationTagsEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationsAggregationEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationsListEntity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.view.FlowLeftLayout;

/**
 * Created by cc on 16/4/28.
 */
public class TarotOrAstrologyEvaluationAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {


     int TYPE_LABEL = 0;

     int TYPE_EVALUATION = 1;

     Context mContext;

     List<EvaluationsListEntity> mListEntities = new ArrayList<>();

     EvaluationsAggregationEntity mEvaluationsAggregationEntity;

    OnAdapterItemClickLitener mOnAdapterItemClickLitener;



    private BitmapPool mPool;
    private CropCircleTransformation transformation;

    public TarotOrAstrologyEvaluationAdapter(Context context) {

        mContext = context;

        mPool=Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation( mPool);
    }

    public void setOnAdapterItemClickLitener(OnAdapterItemClickLitener onAdapterItemClickLitener) {
        mOnAdapterItemClickLitener = onAdapterItemClickLitener;
    }

    public void setDataAggregation(EvaluationsAggregationEntity evaluationsAggregationEntity) {

        mEvaluationsAggregationEntity = evaluationsAggregationEntity;
        notifyDataSetChanged();
    }


    public void setData(List<EvaluationsListEntity> listEntities) {
        mListEntities = listEntities;
        notifyDataSetChanged();

    }

    public void addData(List<EvaluationsListEntity> listEntities) {
        mListEntities.addAll(listEntities);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new EvauationView(view, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {


        if (viewType == TYPE_LABEL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology_label, parent, false);

            return new LabelView(view, true);


        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology_evaluation, parent, false);

        return new EvauationView(view, true);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, boolean isItem) {


        //标签
        if (holder instanceof LabelView) {

            if (mEvaluationsAggregationEntity == null) return;

            ((LabelView) holder).item_tarot_astrology_label_rbar.setRating((float) mEvaluationsAggregationEntity.avgStars);

            ((LabelView) holder).item_tarot_astrology_label_number.setText(mEvaluationsAggregationEntity.avgStars + "");

            setTagsView(mContext, ((LabelView) holder).item_tarot_astrology_label_fll, mEvaluationsAggregationEntity.tags);


            return;
        }
        //评论列表



        if (holder instanceof EvauationView) {

            int positions = position;

            if (mEvaluationsAggregationEntity != null) {
                positions = positions - 1;
            }


            final EvaluationsListEntity evaluationsListEntity = mListEntities.get(positions);

            String iconUrl = ImgUtils.DealImageUrl(evaluationsListEntity.user.headImg, YpSettings.IMG_SIZE_150,
                    YpSettings.IMG_SIZE_150);

            Glide.with(mContext).load(iconUrl).bitmapTransform(transformation).into(((EvauationView) holder).item_tarot_astrology_evalution_iv_user_icon);


            ((EvauationView) holder).item_tarot_astrology_evalution_tv_user_name.setText(evaluationsListEntity.user.name);

            ((EvauationView) holder).item_tarot_astrology_evalution_tv_content.setText(evaluationsListEntity.description);


            long createtime = ISO8601.getTime(evaluationsListEntity.createTime);
            String publictimeStr = TimeUtil.getDatingPublishDateString(createtime, System.currentTimeMillis());
            ((EvauationView) holder).item_tarot_astrology_evalution_tv_time.setText(publictimeStr);

            ((EvauationView) holder).item_tarot_astrology_evalution_iv_user_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAdapterItemClickLitener !=null){

                        int positions = position;

                        if (mEvaluationsAggregationEntity != null) {
                            positions = positions - 1;
                        }


                        mOnAdapterItemClickLitener.onAdapterItemClick(positions,evaluationsListEntity.user.userId);
                    }
                }
            });


            return;
        }


    }


    @Override
    public int getAdapterItemViewType(int position) {


        if (mEvaluationsAggregationEntity == null) {

            return TYPE_EVALUATION;
        }


        if (position > 0) {

            return TYPE_EVALUATION;

        }

        return TYPE_LABEL;


    }

    @Override
    public int getAdapterItemCount() {

        if (mEvaluationsAggregationEntity == null) {

            return mListEntities == null ? 0 : mListEntities.size();
        }


        return mListEntities == null ? 0 : mListEntities.size() + 1;
    }

    public class EvauationView extends RecyclerView.ViewHolder {

        ImageView item_tarot_astrology_evalution_iv_user_icon;

        TextView item_tarot_astrology_evalution_tv_user_name;

        TextView item_tarot_astrology_evalution_tv_time;

        TextView item_tarot_astrology_evalution_tv_content;

        View item_tarot_astrology_evalution_view;


        public EvauationView(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {

                item_tarot_astrology_evalution_iv_user_icon = (ImageView) itemView.findViewById(R.id.item_tarot_astrology_evalution_iv_user_icon);
                item_tarot_astrology_evalution_tv_user_name = (TextView) itemView.findViewById(R.id.item_tarot_astrology_evalution_tv_user_name);
                item_tarot_astrology_evalution_tv_time = (TextView) itemView.findViewById(R.id.item_tarot_astrology_evalution_tv_time);
                item_tarot_astrology_evalution_tv_content = (TextView) itemView.findViewById(R.id.item_tarot_astrology_evalution_tv_content);
                item_tarot_astrology_evalution_view = itemView.findViewById(R.id.item_tarot_astrology_evalution_view);


            }
        }
    }

    public class LabelView extends RecyclerView.ViewHolder {

        RatingBar item_tarot_astrology_label_rbar;

        TextView item_tarot_astrology_label_number;

        FlowLeftLayout item_tarot_astrology_label_fll;


        public LabelView(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {

                item_tarot_astrology_label_rbar = (RatingBar) itemView.findViewById(R.id.item_tarot_astrology_label_rbar);
                item_tarot_astrology_label_number = (TextView) itemView.findViewById(R.id.item_tarot_astrology_label_number);
                item_tarot_astrology_label_fll = (FlowLeftLayout) itemView.findViewById(R.id.item_tarot_astrology_label_fll);
            }
        }
    }


    private void setTagsView(Context context, FlowLeftLayout flowLeftLayout, List<EvaluationTagsEntity> tags) {
        flowLeftLayout.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 0;
        lp.rightMargin = 15;
        lp.topMargin = 15;
        lp.bottomMargin = 15;

        if (tags != null && tags.size() > 0) {

            for (int i = 0; i < tags.size(); i++) {

                String key = tags.get(i).key;

                int vaule = tags.get(i).value;

                StringBuilder sb = new StringBuilder();

                sb.append(key);
                sb.append("(");
                sb.append(vaule);
                sb.append(")");


                TextView view = new TextView(context);
                view.setText(sb.toString());
                view.setTextSize(14);
                view.setPadding(40, 10, 40, 10);

                view.setGravity(Gravity.CENTER_HORIZONTAL);

                view.setBackgroundResource(R.color.color_20b2b2b2);

                view.setTextColor(context.getResources().getColor(R.color.color_b2b2b2));


                flowLeftLayout.addView(view, lp);
            }
        }
    }


}
