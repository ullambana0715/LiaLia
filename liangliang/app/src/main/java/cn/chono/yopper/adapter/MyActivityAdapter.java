package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.data.Activities;
import cn.chono.yopper.utils.ImgUtils;

/**
 * Created by jianghua on 2016/6/12.
 */
public class MyActivityAdapter extends BaseRecyclerAdapter<MyActivityAdapter.NewHolderView> {

    private Context context;
    private List<Activities> dtoList;
    private OnItemClickListener onItemClickListener;

    public MyActivityAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public void setData(List<Activities> list) {
        this.dtoList = list;
    }

    public void addAll(List<Activities> list) {
        if (null == dtoList) {
            dtoList = new ArrayList<>();
        }

        dtoList.addAll(list);
    }

    @Override
    public NewHolderView getViewHolder(View view) {
        return new NewHolderView(view, false);
    }

    @Override
    public NewHolderView onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activities_view, parent, false);
        return new NewHolderView(view, true);
    }

    @Override
    public void onBindViewHolder(NewHolderView holder, final int position, boolean isItem) {
        Activities dto = dtoList.get(position);

        if (position == 0) {
            holder.activityCutLine.setVisibility(View.GONE);
        }

        int status = dto.getJoinStatus();
        if (status == 0) {// 报名状态（0：未报名 1：报名成功 2：报名已退款 3：已完成）
            holder.activityStatusTv.setText("未报名");
        } else if (status == 1) {
            holder.activityStatusTv.setText("报名成功");
        } else if (status == 2) {
            holder.activityStatusTv.setText("已退款");
        } else if (status == 3) {
            holder.activityStatusTv.setText("报名成功");
        }
        holder.activityTitleTv.setText(dto.getTitle());
        holder.activityTimeTv.setText("活动时间：" + formatTime(dto.getActivityStartTime()));
        holder.activityAddressTv.setText("活动地址：" + dto.getAddress());
        holder.activityOvertimeTv.setText("报名截止：" + formatTime(dto.getJoinEndTime()));
        String imageUrl = ImgUtils.DealImageUrl(dto.getTitleImageUrl(), 150, 150);
        Glide.with(context).load(imageUrl)
                .placeholder(R.drawable.activities_default_icon)
                .error(R.drawable.activities_default_icon)
                .into(holder.activityPictureIv);
        holder.activityItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position, dtoList.get(position));
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return null != dtoList ? dtoList.size() : 0;
    }

    class NewHolderView extends RecyclerView.ViewHolder {
        private LinearLayout activityItemLayout;
        private TextView activityStatusTv;
        private TextView activityTitleTv;
        private TextView activityTimeTv;
        private TextView activityAddressTv;
        private TextView activityOvertimeTv;
        private ImageView activityPictureIv;
        private View activityCutLine;

        public NewHolderView(View itemView, boolean isItme) {
            super(itemView);
            if (isItme) {
                activityItemLayout = (LinearLayout) itemView.findViewById(R.id.activity_item_layout);
                activityCutLine = itemView.findViewById(R.id.activity_item_cut);
                activityStatusTv = (TextView) itemView.findViewById(R.id.activity_status_tv);
                activityTitleTv = (TextView) itemView.findViewById(R.id.activity_title_tv);
                activityTimeTv = (TextView) itemView.findViewById(R.id.activity_time_tv);
                activityAddressTv = (TextView) itemView.findViewById(R.id.activity_address_tv);
                activityOvertimeTv = (TextView) itemView.findViewById(R.id.activity_overtime_tv);
                activityPictureIv = (ImageView) itemView.findViewById(R.id.activity_picture_iv);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Activities activities);
    }

    private String formatTime(String timeStr) {
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("MM月dd日  HH:mm");
        try {
            result = format1.format(format.parse(timeStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
