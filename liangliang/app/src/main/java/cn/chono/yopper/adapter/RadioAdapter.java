package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.chono.yopper.R;

/**
 * Created by cc on 16/3/22.
 */
public class RadioAdapter extends BaseAdapter {


    @Override
    public int getCount() {
        return data == null ? 0 : data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.travel_pay_item_layout, null);
            holder = new ViewHolder();

            holder.view = (TextView) convertView.findViewById(R.id.contextView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.view.setText(data[position]);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickLitener) {
                    onItemClickLitener.onItemClickLitener(position, data[position]);
                }
            }
        });


        if (!TextUtils.isEmpty(contextStr) && TextUtils.equals(contextStr, data[position])) {
            holder.view.setBackgroundResource(R.drawable.travel_label_lable_corners_setect);
            holder.view.setTextColor(context.getResources().getColor(R.color.color_ffffff));

        } else {
            holder.view.setBackgroundResource(R.drawable.travel_label_lable_corners_default);
            holder.view.setTextColor(context.getResources().getColor(R.color.color_333333));
        }


        return convertView;
    }

    public interface OnItemTravelClickLitener {
        void onItemClickLitener(int position, String context);
    }


    public void setOnItemClickLitener(OnItemTravelClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }


    private String[] data;

    private String contextStr;

    private OnItemTravelClickLitener onItemClickLitener;

    private Context context;


    public RadioAdapter(Context context) {
        this.context = context;
    }

    public void setData(String[] data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void setSelectItem(String contextStr) {

        this.contextStr = contextStr;

        notifyDataSetChanged();
    }


    public class ViewHolder {

        private TextView view;


    }
}