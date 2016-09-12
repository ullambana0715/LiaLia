package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.utils.StringUtils;

/**
 * Created by cc on 16/7/22.
 */
public class UILableAdapter<T> extends BaseRecyclerAdapter<UILableAdapter.ItemViewHolder> {

    Context mContext;

    T[] list ;


    public void setList(T[] list) {
        this.list = list;
        notifyDataSetChanged();
    }



    @Override
    public ItemViewHolder getViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_lable, parent, false);


        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position, boolean isItem) {

        String str= (String) list[position];

        holder.mUILableIv.setText(StringUtils.isEmpty(str));

    }

    @Override
    public int getAdapterItemCount() {

        return list == null ? 0 : list.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.UILable_iv)
        TextView mUILableIv;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
