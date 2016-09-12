package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkTimesEntity;
import cn.chono.yopper.utils.TimeUtils;

/**
 * Created by cc on 16/5/3.
 */
public class TarotAstrologyTimeAdapter extends BaseAdapter {


    List<WorkTimesEntity> mWorkTimesEntityList;

    Integer isSelect;


    Context context;

    OnTimeItemClickLitener mOnTimeItemClickLitener;


    public TarotAstrologyTimeAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<WorkTimesEntity> workTimesEntityList) {
        mWorkTimesEntityList = workTimesEntityList;

    }



    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;

    }

    public void setOnTimeItemClickLitener(OnTimeItemClickLitener onTimeItemClickLitener) {
        mOnTimeItemClickLitener = onTimeItemClickLitener;
    }

    @Override
    public int getCount() {
        return mWorkTimesEntityList == null ? 0 : mWorkTimesEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWorkTimesEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TimeViewHolder mTimeViewHolder;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology_time, parent, false);


            mTimeViewHolder = new TimeViewHolder();

            mTimeViewHolder.item_tarot_astrology_tv_time = (TextView) convertView.findViewById(R.id.item_tarot_astrology_tv_time);


            convertView.setTag(mTimeViewHolder);

        } else {
            mTimeViewHolder = (TimeViewHolder) convertView.getTag();
        }





        int itemHeight = (parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight()) / 5;

        convertView.getLayoutParams().height = itemHeight;
        convertView.getLayoutParams().width = itemHeight;


        final WorkTimesEntity workTimesEntity  = mWorkTimesEntityList.get(position);

        if (workTimesEntity == null) {

            LogUtils.e("workTimesEntity－－－－" + workTimesEntity.isFullReservation);


            return convertView;
        }


        mTimeViewHolder.item_tarot_astrology_tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LogUtils.e("dfsfsfsf有返应。。。。。。");


                if (!workTimesEntity.isFullReservation) {
                    LogUtils.e("有返应。。。。。。");

                    if (mOnTimeItemClickLitener != null) {
                        mOnTimeItemClickLitener.onTimeItemClick(position, workTimesEntity);
                    }

                }


            }
        });




        long time = TimeUtils.getFormat(workTimesEntity.workTime);

        String HHmm = TimeUtils.getHHmm(time);

        if (!TextUtils.isEmpty(HHmm)) {
            mTimeViewHolder.item_tarot_astrology_tv_time.setText(HHmm);
        }


        LogUtils.e("能不能点呢－－－－" + workTimesEntity.isFullReservation);


        if (workTimesEntity.isFullReservation) {
            mTimeViewHolder.item_tarot_astrology_tv_time.setTextColor(context.getResources().getColor(R.color.color_bababa));
            mTimeViewHolder.item_tarot_astrology_tv_time.setBackgroundResource(R.color.color_e0ffffff);


        } else {

            mTimeViewHolder.item_tarot_astrology_tv_time.setTextColor(context.getResources().getColor(R.color.color_666666));
            mTimeViewHolder.item_tarot_astrology_tv_time.setBackgroundResource(R.color.color_ffffff);


            if (isSelect == null) {

                mTimeViewHolder.item_tarot_astrology_tv_time.setTextColor(context.getResources().getColor(R.color.color_666666));
                mTimeViewHolder.item_tarot_astrology_tv_time.setBackgroundResource(R.color.color_ffffff);

            } else if (position == isSelect) {
                mTimeViewHolder.item_tarot_astrology_tv_time.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                mTimeViewHolder.item_tarot_astrology_tv_time.setBackgroundResource(R.color.color_ff735d);
            }



        }

        LogUtils.e("return .....");










        return convertView;
    }

    public class TimeViewHolder {

        TextView item_tarot_astrology_tv_time;


    }

    public interface OnTimeItemClickLitener<T> {
        void onTimeItemClick(int position, T data);

    }

}
