package cn.chono.yopper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.data.IndexActivities;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by jianghua on 2016/6/13.
 */
public class ActivityAdapter extends BaseRecyclerAdapter<ActivityAdapter.NewHolderView> {

    private Context context;
    private List<IndexActivities> dtoList;
    private OnItemClickListener onItemClickListener;

    public ActivityAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public void setData(List<IndexActivities> list) {
        this.dtoList = list;
    }

    public void addAll(List<IndexActivities> list) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_base_activities_view, parent, false);
        return new NewHolderView(view, true);
    }

    @Override
    public void onBindViewHolder(NewHolderView holder, final int position, boolean isItem) {

        IndexActivities dto = dtoList.get(position);

        holder.activitiesTitleTv.setText(dto.getTitle());
        holder.activitiesTimeTv.setText(formatTime(dto.getActivityStartTime()));
        holder.activitiesAddressTv.setText(dto.getCity());

        String imageUrl = ImgUtils.DealImageUrl(dto.getTitleImageUrl(), 640, 320);
        Glide.with(context).load(imageUrl)
                .placeholder(R.drawable.activities_default_icon)
                .error(R.drawable.activities_default_icon)
                .into(holder.activitiesIv);
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
        private LinearLayout layoutItem;
        private RelativeLayout activityItemLayout;
        private ImageView activitiesIv;
        private TextView activitiesTitleTv;
        private TextView activitiesTimeTv;
        private TextView activitiesAddressTv;

        public NewHolderView(View itemView, boolean isItme) {
            super(itemView);
            if (isItme) {
                layoutItem = (LinearLayout) itemView.findViewById(R.id.activities_item_ll);
                ViewGroup.LayoutParams params;
                int width = getWidth() - (int)UnitUtil.dip2px(32,context);   //(int)(640 * density);
                int high = width/2; // (int)UnitUtil.dip2px(175,context);//(int)(350 * density);
                params = layoutItem.getLayoutParams();
                params.height = high;
                params.width = width;
                layoutItem.setLayoutParams(params);



                activityItemLayout = (RelativeLayout) itemView.findViewById(R.id.activity_item_layout);
                activitiesIv = (ImageView) itemView.findViewById(R.id.activities_iv);
                activitiesTitleTv = (TextView) itemView.findViewById(R.id.activities_title_tv);
                activitiesTimeTv = (TextView) itemView.findViewById(R.id.activities_time_tv);
                activitiesAddressTv = (TextView) itemView.findViewById(R.id.activities_address_tv);
            }
        }
    }

    private String formatTime(String timeStr) {
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
        try {
            result = format1.format(format.parse(timeStr));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int month = Integer.parseInt(result.substring(0, result.indexOf("-")));
        int day = Integer.parseInt(result.substring(result.lastIndexOf("-") + 1));
        result = month + "." + day;
        return result;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, IndexActivities activityDto);
    }

    private int getWidth(){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}
