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
 * Created by cc on 16/3/25.
 */
public class WishesForYouAdapter extends BaseAdapter {

    private String[] data;

    private Context context;

    private String contextStr;

    private OnItemSendClickLitener onItemClickLitener;

    public WishesForYouAdapter(Context context) {
        this.context = context;
    }

    public void setData(String[] data) {
        this.data = data;
    }

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

    private ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wishes_for_you, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.contextView = (TextView) convertView.findViewById(R.id.contextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.contextView.setText(data[position]);

        if (!TextUtils.isEmpty(contextStr) && TextUtils.equals(contextStr, data[position])) {
            viewHolder.contextView.setBackgroundResource(R.drawable.send_message_corners_setect);
            viewHolder.contextView.setTextColor(context.getResources().getColor(R.color.color_ffffff));

        } else {
            viewHolder.contextView.setBackgroundResource(R.drawable.send_message_corners_default);
            viewHolder.contextView.setTextColor(context.getResources().getColor(R.color.color_333333));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickLitener) {
                    onItemClickLitener.onItemClickLitener(position, data[position]);
                }
            }
        });


        return convertView;
    }


    public void setSelectItem(String contextStr) {
        this.contextStr = contextStr;
        notifyDataSetChanged();
    }


    private class ViewHolder {
        private TextView contextView;
    }


    public interface OnItemSendClickLitener {
        void onItemClickLitener(int position, String context);
    }


    public void setOnItemClickLitener(OnItemSendClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }
}
