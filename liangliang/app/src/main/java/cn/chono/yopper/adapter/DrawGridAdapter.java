package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.MyExchanagePrize.ExpiryDataRespBean;
import cn.chono.yopper.activity.find.DrawActivity;

/**
 * Created by yangjinyu on 16/3/24.
 */
public class DrawGridAdapter extends BaseRecyclerAdapter<DrawGridAdapter.ViewHolder>{
    DrawActivity mActivity;

    public DrawGridAdapter(DrawActivity c){
        mActivity = c;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draw_grid, parent, false);

        return new ViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        holder.bonus_name.setText(mActivity.mList.get(position).getName());
        holder.bonus_value.setText(mActivity.mList.get(position).getPrizeLevel());
        Glide.with(mActivity).load(mActivity.mList.get(position).getImageUrl()).into(holder.bonus_img);
    }

    @Override
    public int getAdapterItemCount() {
        return mActivity.mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView bonus_img;
        private TextView bonus_name;
        private TextView bonus_value;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
//            if (isItem) {
                this.bonus_name = ((TextView) itemView.findViewById(R.id.bonus_name));
                this.bonus_value = ((TextView) itemView.findViewById(R.id.bonus_level));
                this.bonus_img = ((ImageView) itemView.findViewById(R.id.bonus_img));
//            }
        }
    }
}
