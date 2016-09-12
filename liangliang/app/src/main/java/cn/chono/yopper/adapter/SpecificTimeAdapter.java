package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.data.TimeDataBean;

/**
 * Created by cc on 16/3/28.
 */
public class SpecificTimeAdapter extends RecyclerView.Adapter<SpecificTimeAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemListener(int position, Object object);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private int days;

    private int month;

    private int Date;

    private Context context;

    private List<TimeDataBean> initData;

    public SpecificTimeAdapter(Context context, int days, int month, int Date) {
        this.context = context;

        this.days = days;
        this.month = month;
        this.Date = Date;

    }

    public void setData(List<TimeDataBean> initData) {
        this.initData = initData;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.specific_time_layout, parent, false);

        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TimeDataBean dataBean = initData.get(position);

        if (!TextUtils.isEmpty(dataBean.getDaysMonth())) {
            holder.DaysMonth.setText(dataBean.getDaysMonth());
        }

        holder.dataAdapter = new SpecificTimeDataAdapter(context, days, month, Date);

        holder.dataAdapter.setData(dataBean.getTimeDatas());

        holder.dataAdapter.setOnItemClickListener(onItemClickListener);

        holder.gridView.setAdapter(holder.dataAdapter);
    }

    @Override
    public int getItemCount() {
        return initData == null ? 0 : initData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView DaysMonth;

        private GridView gridView;

        private SpecificTimeDataAdapter dataAdapter;

        public ViewHolder(View itemView) {
            super(itemView);

            DaysMonth = (TextView) itemView.findViewById(R.id.DaysMonth);

            gridView = (GridView) itemView.findViewById(R.id.gridView);


        }


    }
}
