package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDateEntity;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDatesEntity;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkTimesEntity;
import cn.chono.yopper.utils.TimeUtils;
import cn.chono.yopper.view.MyGridView;

/**
 * Created by cc on 16/5/23.
 */
public class TarotAstrologyTimeTabAdapter extends BaseRecyclerAdapter<TarotAstrologyTimeTabAdapter.TimeViewHolder> {


    WorkDateEntity resp;


    LayoutInflater mLayoutInflater;


    Context mContext;


    int selectItme = 0;

    Integer isSelect = null;


    TarotAstrologyTimeAdapter mTarotAstrologyTimeAdapter;


    public TarotAstrologyTimeTabAdapter(Context context, TarotAstrologyTimeAdapter tarotAstrologyTimeAdapter) {
        mContext = context;
        mTarotAstrologyTimeAdapter = tarotAstrologyTimeAdapter;
        mLayoutInflater = LayoutInflater.from(mContext);

    }


    public void setData(WorkDateEntity resp) {
        this.resp = resp;

        notifyDataSetChanged();


        LogUtils.e("resp--------------------------------");
    }


    public void setIsSelect(Integer isSelect) {

        this.isSelect = isSelect;

        notifyDataSetChanged();


    }

    @Override
    public TimeViewHolder getViewHolder(View view) {
        return new TimeViewHolder(view);
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarot_astrology_time_tab_content, parent, false);


        return new TimeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position, boolean isItem) {


        List<WorkTimesEntity> workTimesEntityList = new ArrayList<>();

        holder.tarotAstrologyTime_gv_time.setNumColumns(5);


        final List<WorkDatesEntity> workDatetimes = resp.workDatetimes;

        if (workDatetimes == null || workDatetimes.size() == 0) {

            mTarotAstrologyTimeAdapter.setData(workTimesEntityList);
            holder.tarotAstrologyTime_gv_time.invalidateViews();

            return;
        }

        holder.itme_time_content.removeAllViews();

        for (int i = 0; i < workDatetimes.size(); i++) {


            View view = mLayoutInflater.inflate(R.layout.item_tarot_astrology_time_tab, null);

            TextView item_tarot_astrology_time_tab_tv_week = (TextView) view.findViewById(R.id.item_tarot_astrology_time_tab_tv_week);
            TextView item_tarot_astrology_time_tab_tv_date = (TextView) view.findViewById(R.id.item_tarot_astrology_time_tab_tv_date);
            View item_tarot_astrology_time_tab_view = view.findViewById(R.id.item_tarot_astrology_time_tab_view);


            long time = TimeUtils.getFormat(workDatetimes.get(i).workDate);

            String mmdd = TimeUtils.getMMdd(time);


            String date = TimeUtils.getDesignationDateWeek(time);

            int apartDay = TimeUtils.getApartDateDay(time, TimeUtils.getCurrentTime());

            if (apartDay == 0) {
                date = "今天";

            } else if (apartDay == 1) {
                date = "明天";

            } else if (apartDay == 2) {
                date = "后天";

            }

            item_tarot_astrology_time_tab_tv_week.setText(date);


            item_tarot_astrology_time_tab_tv_date.setText(mmdd);


            view.setTag(i);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectItme = (int) v.getTag();

                    notifyDataSetChanged();


                }
            });


            if (selectItme == i) {

                workTimesEntityList = workDatetimes.get(selectItme).workTimes;

                item_tarot_astrology_time_tab_tv_week.setTextColor(mContext.getResources().getColor(R.color.color_ff735d));
                item_tarot_astrology_time_tab_tv_date.setTextColor(mContext.getResources().getColor(R.color.color_ff735d));
                item_tarot_astrology_time_tab_view.setVisibility(View.VISIBLE);

            } else {

                item_tarot_astrology_time_tab_tv_week.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
                item_tarot_astrology_time_tab_tv_date.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
                item_tarot_astrology_time_tab_view.setVisibility(View.GONE);

            }


            holder.itme_time_content.addView(view);


        }

        mTarotAstrologyTimeAdapter.setData(workTimesEntityList);
        mTarotAstrologyTimeAdapter.setIsSelect(isSelect);
        holder.tarotAstrologyTime_gv_time.invalidateViews();

    }


    @Override
    public int getAdapterItemCount() {
        return 1;
    }


    class TimeViewHolder extends RecyclerView.ViewHolder {

        MyGridView tarotAstrologyTime_gv_time;

        LinearLayout itme_time_content;

        public TimeViewHolder(View itemView) {
            super(itemView);

            tarotAstrologyTime_gv_time = (MyGridView) itemView.findViewById(R.id.tarotAstrologyTime_gv_time);

            itme_time_content = (LinearLayout) itemView.findViewById(R.id.itme_time_content);

            if (mTarotAstrologyTimeAdapter != null)
                tarotAstrologyTime_gv_time.setAdapter(mTarotAstrologyTimeAdapter);

        }
    }


}
