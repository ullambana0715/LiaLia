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

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.MyExchanagePrize.ExpiryDataRespBean;

import com.andbase.tractor.utils.LogUtils;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ExchanagePrizeAdapter extends BaseRecyclerAdapter<ExchanagePrizeAdapter.NewViewHolder> {
    private Context context;
    private List<ExpiryDataRespBean.ExchanageResp.PrizeData> datalist;
    private boolean isExchanage = true;
    private OnItemClickListener mOnItemClickLitener;

    public ExchanagePrizeAdapter(Context paramContext) {
        this.context = paramContext;
    }

    public interface OnItemClickListener {
        void onItemClikc(View paramView, int posision, ExpiryDataRespBean.ExchanageResp.PrizeData paramPrizeData);
    }

    public void setDatalist(List<ExpiryDataRespBean.ExchanageResp.PrizeData> paramList, boolean paramBoolean) {
        this.isExchanage = paramBoolean;
        this.datalist = paramList;
    }

    public void setOnItemClickLitener(OnItemClickListener paramOnItemClickListener) {
        this.mOnItemClickLitener = paramOnItemClickListener;
    }

    public void addData(List<ExpiryDataRespBean.ExchanageResp.PrizeData> paramList, boolean paramBoolean) {
        this.isExchanage = paramBoolean;
        if (this.datalist == null) {
            this.datalist = new ArrayList();
        }
        this.datalist.addAll(paramList);
    }

    @Override
    public int getAdapterItemCount() {
        if (this.datalist != null) {
            return this.datalist.size();
        }
        return 0;
    }

    public List<ExpiryDataRespBean.ExchanageResp.PrizeData> getDatalist() {
        return this.datalist;
    }

    @Override
    public NewViewHolder getViewHolder(View paramView) {
        return new NewViewHolder(paramView, false);
    }

    @Override
    public void onBindViewHolder(final NewViewHolder holder, final int position, boolean paramBoolean) {

        holder.bonus_name.setText(datalist.get(position).getPrize().getName());
        holder.bonus_value.setText("价值 " + String.valueOf(new Double(datalist.get(position).getPrize().getPrice()).intValue()) + " RMB");

        //用户可退换奖品状态
        int status = datalist.get(position).getStatus();

        LogUtils.e("isExchanage == " + isExchanage +" status == " + status);
        if (!isExchanage) {
            if (status == 1) {
                holder.bonus_btn.setText("已领取");
            } else if (status == 2) {
                holder.bonus_btn.setText("已过期");
            } else if (status == 3) {
                holder.bonus_btn.setText("已兑换");
            }else{
                holder.bonus_btn.setText(String.valueOf(new Double(datalist.get(position).getPrize().getAppleCount()).intValue()) + "苹果");
            }
            holder.bonus_btn.setBackgroundResource(R.drawable.exchanage_btn_bg_gray);
        } else {
            if (status == 1) {
                holder.bonus_btn.setText("已领取");
                holder.bonus_btn.setBackgroundResource(R.drawable.exchanage_btn_bg_gray);
            } else if (status == 2) {
                holder.bonus_btn.setText("已过期");
                holder.bonus_btn.setBackgroundResource(R.drawable.exchanage_btn_bg_gray);
            } else if (status == 3) {
                holder.bonus_btn.setText("已兑换");
                holder.bonus_btn.setBackgroundResource(R.drawable.exchanage_btn_bg_gray);
            }else {
                holder.bonus_btn.setText(String.valueOf(new Double(datalist.get(position).getPrize().getAppleCount()).intValue()) + "苹果");
            }
        }

        Glide.with(this.context).load(datalist.get(position).getPrize().getImageUrl()).into(holder.bonus_img);
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ExchanagePrizeAdapter.this.mOnItemClickLitener.onItemClikc(holder.item_layout, position, datalist.get(position));
            }
        });
    }


    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean paramBoolean) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bonus_grid, parent, false);

        return new NewViewHolder(view, true);
    }


    public class NewViewHolder extends RecyclerView.ViewHolder {
        private TextView bonus_btn;
        private ImageView bonus_img;
        private TextView bonus_name;
        private TextView bonus_value;
        private RelativeLayout item_layout;

        public NewViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.item_layout = ((RelativeLayout) itemView.findViewById(R.id.bonus_item_layout));
                this.bonus_name = ((TextView) itemView.findViewById(R.id.bonus_name));
                this.bonus_value = ((TextView) itemView.findViewById(R.id.bonus_value));
                this.bonus_img = ((ImageView) itemView.findViewById(R.id.bonus_img));
                this.bonus_btn = ((TextView) itemView.findViewById(R.id.bonus_exchanage_btn));
            }
        }
    }

}
