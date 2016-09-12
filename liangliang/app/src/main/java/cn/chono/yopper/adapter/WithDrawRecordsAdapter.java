package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.entity.WithDrawItemEntity;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.TimeUtils;

/**
 * Created by yangjinyu on 16/7/31.
 */
public class WithDrawRecordsAdapter<T> extends BaseRecyclerAdapter<WithDrawRecordsAdapter.NewHolderView> {


    private Context context;
    List<T> list;

    public void setData(List<T> dataList) {
        this.list = dataList;
    }

    public void addAll(List<T> dataList) {
        if (null == list) {
            list = new ArrayList<>();
        }

        list.addAll(dataList);
    }

    public WithDrawRecordsAdapter(Context c) {
        context = c;
    }

    @Override
    public NewHolderView getViewHolder(View view) {
        return new NewHolderView(view);
    }

    @Override
    public NewHolderView onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_withdraw_record, parent, false);
        return new NewHolderView(view);
    }

    @Override
    public void onBindViewHolder(WithDrawRecordsAdapter.NewHolderView holder, int position, boolean isItem) {

        WithDrawItemEntity dto = (WithDrawItemEntity) list.get(position);


        if (position == 0) {
            holder.itemWithdrawRecordsLine.setVisibility(View.INVISIBLE);
        } else {
            holder.itemWithdrawRecordsLine.setVisibility(View.VISIBLE);
        }


        if (position == list.size() - 1) {

            holder.itemWithdrawRecordsTopLine.setVisibility(View.INVISIBLE);

            holder.itemWithdrawRecordsBottomLine.setVisibility(View.INVISIBLE);

        } else {

            holder.itemWithdrawRecordsTopLine.setVisibility(View.VISIBLE);

            holder.itemWithdrawRecordsBottomLine.setVisibility(View.VISIBLE);

        }

        holder.itemWithdrawRecordsAttractTv.setText("您使用" + dto.getUsedCharm() + "魅力");

        double cash = dto.getCash() / 100;

        holder.itemWithdrawRecordsMoneyTv.setText("兑换金额" + cash + "元");

        long time = ISO8601.getTime(dto.getExchangeTime());

        String exchangeTime = TimeUtils.longToString(time, "yyyy-MM-dd HH:mm:ss");

        holder.itemWithdrawRecordsTime.setText(exchangeTime);

    }

    @Override
    public int getAdapterItemCount() {
        return null != list ? list.size() : 0;
    }

    class NewHolderView extends RecyclerView.ViewHolder {


        @BindView(R.id.item_withdraw_records_line)
        View itemWithdrawRecordsLine;

        @BindView(R.id.item_withdraw_records_top_line)
        View itemWithdrawRecordsTopLine;

        @BindView(R.id.item_withdraw_records_time)
        TextView itemWithdrawRecordsTime;

        @BindView(R.id.item_withdraw_records_bottom_line)
        RelativeLayout itemWithdrawRecordsBottomLine;

        @BindView(R.id.item_withdraw_records_attract_tv)
        TextView itemWithdrawRecordsAttractTv;

        @BindView(R.id.item_withdraw_records_money_tv)
        TextView itemWithdrawRecordsMoneyTv;

        public NewHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
