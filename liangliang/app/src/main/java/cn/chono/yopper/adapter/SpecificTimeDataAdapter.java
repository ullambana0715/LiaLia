package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.data.TimeDates;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/3/28.
 */
public class SpecificTimeDataAdapter extends BaseAdapter {

    private int days;

    private int month;

    private int Date;

    private SpecificTimeAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(SpecificTimeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private Context context;

    private ViewHolder viewHolder;

    private List<TimeDates> inittimeDatas;


    public SpecificTimeDataAdapter(Context context, int days, int month, int Date) {
        this.context = context;
        this.days = days;
        this.month = month;
        this.Date = Date;


        LogUtils.e("days===d="+days);
        LogUtils.e("month=d==="+month);
        LogUtils.e("Date===d="+Date);
    }


    public void setData(List<TimeDates> inittimeDatas) {
        this.inittimeDatas = inittimeDatas;
    }

    @Override
    public int getCount() {
        return inittimeDatas == null ? 0 : inittimeDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return inittimeDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.specific_time_item_layout, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.tiem_reservations_latout = (LinearLayout) convertView.findViewById(R.id.tiem_reservations_latout);
            viewHolder.tiem_reservations_day_tv = (TextView) convertView.findViewById(R.id.tiem_reservations_day_tv);
            viewHolder.tiem_reservations_tiem_tv = (TextView) convertView.findViewById(R.id.tiem_reservations_tiem_tv);
            convertView.setTag(viewHolder);

            int weight = (UnitUtil.getScreenWidthPixels(context) - 24) / 7;


            GridView.LayoutParams params = (GridView.LayoutParams) convertView.getLayoutParams();

            params.width = weight;
            params.height = weight;

            convertView.setLayoutParams(params);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final TimeDates timeDates = inittimeDatas.get(position);


        if (0 != timeDates.getDate()) {


            if (!TextUtils.isEmpty(timeDates.getType())) {
                viewHolder.tiem_reservations_latout.setVisibility(View.VISIBLE);
                viewHolder.time_tv.setVisibility(View.GONE);

                viewHolder.tiem_reservations_day_tv.setText(timeDates.getType());


                if (days == timeDates.getDays() && month == timeDates.getMonth() && Date == timeDates.getDate()) {

                    LogUtils.e("days===="+days);
                    LogUtils.e("month===="+month);
                    LogUtils.e("Date===="+Date);

                    convertView.setBackgroundResource(R.color.color_ff7462);

                    viewHolder.tiem_reservations_day_tv.setTextColor(context.getResources().getColor(R.color.color_ffffff));

                    viewHolder.tiem_reservations_tiem_tv.setTextColor(context.getResources().getColor(R.color.color_ffffff));

                } else {

                    convertView.setBackgroundResource(R.color.color_ffffff);

                    viewHolder.tiem_reservations_day_tv.setTextColor(context.getResources().getColor(R.color.color_333333));

                    viewHolder.tiem_reservations_tiem_tv.setTextColor(context.getResources().getColor(R.color.color_b2b2b2));
                }


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (timeDates.isData() && null != onItemClickListener) {
                            onItemClickListener.onItemListener(position, timeDates);
                        }
                    }
                });


            } else {
                viewHolder.tiem_reservations_latout.setVisibility(View.GONE);
                viewHolder.time_tv.setVisibility(View.VISIBLE);


                if (timeDates.isData()) {


                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (timeDates.isData() && null != onItemClickListener) {
                                onItemClickListener.onItemListener(position, timeDates);
                            }
                        }
                    });



                    if (days == timeDates.getDays() && month == timeDates.getMonth() && Date == timeDates.getDate()) {


                        LogUtils.e("days====--"+days);
                        LogUtils.e("month====---"+month);
                        LogUtils.e("Date====--"+Date);

                        convertView.setBackgroundResource(R.color.color_ff7462);
                        viewHolder.time_tv.setTextColor(context.getResources().getColor(R.color.color_ffffff));


                    } else {

                        convertView.setBackgroundResource(R.color.color_ffffff);
                        viewHolder.time_tv.setTextColor(context.getResources().getColor(R.color.color_333333));

                    }


                } else {




                    viewHolder.time_tv.setTextColor(context.getResources().getColor(R.color.color_b2b2b2));
                }


            }


            viewHolder.tiem_reservations_tiem_tv.setText(timeDates.getDate() + "");

            viewHolder.time_tv.setText(timeDates.getDate() + "");





        } else {
            viewHolder.time_tv.setText("");
            viewHolder.tiem_reservations_latout.setVisibility(View.GONE);
            viewHolder.time_tv.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    private class ViewHolder {

        private TextView time_tv;

        private LinearLayout tiem_reservations_latout;


        private TextView tiem_reservations_day_tv;

        private TextView tiem_reservations_tiem_tv;
    }
}
