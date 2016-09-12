package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterIconClickLitener;
import cn.chono.yopper.utils.TimeUtil;


public class UserOrderListAdapter<T> extends BaseRecyclerAdapter<UserOrderListAdapter.ViewHolder> {

    private List<T> list;

    private Context mContext;


    private OnAdapterIconClickLitener mOnAdapterIconClickLitener;

    public void setOnAdapterIconClickLitener(OnAdapterIconClickLitener onAdapterIconClickLitener) {
        mOnAdapterIconClickLitener = onAdapterIconClickLitener;
    }

    private OnOrderItemClickLitener mOnOrderItemClickLitener;

    public void setOnOrderItemClickLitener(OnOrderItemClickLitener onOrderItemClickLitener) {
        mOnOrderItemClickLitener = onOrderItemClickLitener;
    }



    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    public UserOrderListAdapter(Context context) {
        this.mContext = context;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public void setData(List<T> list) {
        this.list = list;
    }

    public void addData(List<T> morelist) {
        if (null == list) {
            list = new ArrayList<T>();
        }
        list.addAll(morelist);

    }

    public List<T> getDatas() {
        return list;
    }


    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        // 给ViewHolder设置布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_order, parent, false);
        return new ViewHolder(v, true);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i, boolean isItem) {
        // 给ViewHolder设置元素

        final OrderDetailDto dto = (OrderDetailDto) list.get(i);

        if (dto == null) {
            return;
        }

        if (dto.getUser()!=null){

            String imageurl = dto.getUser().getHeadImg();
            if (!CheckUtil.isEmpty(imageurl)) {
                imageurl = ImgUtils.DealImageUrl(imageurl, 150, 150);
                Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(viewHolder.item_user_order_head_img_iv);
            } else {
                viewHolder.item_user_order_head_img_iv.setBackgroundResource(R.drawable.message_head_default_icon);
            }

            viewHolder.item_user_order_name_tv.setText(dto.getUser().getName());

        }else {
            viewHolder.item_user_order_head_img_iv.setBackgroundResource(R.drawable.message_head_default_icon);
        }





        if (dto.getCounselType() == 0) {
            viewHolder.item_user_order_type_name_tv.setText(R.string.astrolabe_consultant);
        } else {
            viewHolder.item_user_order_type_name_tv.setText(R.string.tarot_consultant);
        }


        switch (dto.getOrderStatus()) {

            case 0://待付款
                viewHolder.item_user_order_handle_layout.setVisibility(View.VISIBLE);
                viewHolder.item_user_order_handle_line.setVisibility(View.VISIBLE);

                viewHolder.item_user_order_handle_left_tv.setText(R.string.cancel_order);
                viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));

                viewHolder.item_user_order_handle_right_tv.setText(R.string.payment_text);
                viewHolder.item_user_order_handle_right_tv.setBackgroundResource(R.drawable.ff7462_bg_5_corners);
                viewHolder.item_user_order_handle_right_tv.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));

                viewHolder.item_user_order_status_tv.setText("待付款");
                break;

            case 1://预约成功

                viewHolder.item_user_order_handle_layout.setVisibility(View.VISIBLE);
                viewHolder.item_user_order_handle_line.setVisibility(View.VISIBLE);

                viewHolder.item_user_order_status_tv.setText("预约成功");


                viewHolder.item_user_order_handle_right_tv.setText(R.string.consultant_text);
                viewHolder.item_user_order_handle_right_tv.setBackgroundResource(R.drawable.ff7462_bg_5_corners);
                viewHolder.item_user_order_handle_right_tv.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));

                if (dto.getFeedbackStatus() == 0) {
                    //未投诉
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.complaint_text);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));

                } else if (dto.getFeedbackStatus() == 1) {
                    //处理中
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_handling);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.color.color_ffffff);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
                } else {
                    //已处理
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_result);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));
                }

                break;

            case 2://已取消

                viewHolder.item_user_order_handle_layout.setVisibility(View.GONE);
                viewHolder.item_user_order_handle_line.setVisibility(View.GONE);

                viewHolder.item_user_order_status_tv.setText("已取消");

                break;

            case 3://咨询完成-未评价
                viewHolder.item_user_order_handle_layout.setVisibility(View.VISIBLE);
                viewHolder.item_user_order_handle_line.setVisibility(View.VISIBLE);

                viewHolder.item_user_order_status_tv.setText("咨询完成");


                viewHolder.item_user_order_handle_right_tv.setText(R.string.evaluate_text);
                viewHolder.item_user_order_handle_right_tv.setBackgroundResource(R.drawable.ff7462_bg_5_corners);
                viewHolder.item_user_order_handle_right_tv.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));

                if (dto.getFeedbackStatus() == 0) {
                    //未投诉
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.complaint_text);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));

                } else if (dto.getFeedbackStatus() == 1) {
                    //处理中
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_handling);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.color.color_ffffff);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
                } else {
                    //已处理
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_result);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));
                }


                break;

            case 4://咨询完成-已评价
                viewHolder.item_user_order_handle_layout.setVisibility(View.VISIBLE);
                viewHolder.item_user_order_handle_line.setVisibility(View.VISIBLE);

                viewHolder.item_user_order_status_tv.setText("咨询完成");

                if (dto.getFeedbackStatus() == 0) {
                    //未投诉
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.complaint_text);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));

                } else if (dto.getFeedbackStatus() == 1) {
                    //处理中
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_handling);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.color.color_ffffff);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
                } else {
                    //已处理
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_result);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));
                }

                viewHolder.item_user_order_handle_right_tv.setText(R.string.had_evaluate_text);
                viewHolder.item_user_order_handle_right_tv.setBackgroundResource(R.drawable.d6d6d6_border_5_corners_white_bg);
                viewHolder.item_user_order_handle_right_tv.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                break;

            case 5://待确认完成

                viewHolder.item_user_order_handle_layout.setVisibility(View.VISIBLE);
                viewHolder.item_user_order_handle_line.setVisibility(View.VISIBLE);

                viewHolder.item_user_order_status_tv.setText("预约成功");


                viewHolder.item_user_order_handle_right_tv.setText(R.string.consultant_text);
                viewHolder.item_user_order_handle_right_tv.setBackgroundResource(R.drawable.ff7462_bg_5_corners);
                viewHolder.item_user_order_handle_right_tv.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));

                if (dto.getFeedbackStatus() == 0) {
                    //未投诉
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.complaint_text);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));

                } else if (dto.getFeedbackStatus() == 1) {
                    //处理中
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_handling);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.color.color_ffffff);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
                } else {
                    //已处理
                    viewHolder.item_user_order_handle_left_tv.setText(R.string.feedback_result);
                    viewHolder.item_user_order_handle_left_tv.setBackgroundResource(R.drawable.ff7462_1_border_5_corners_white_bg);
                    viewHolder.item_user_order_handle_left_tv.setTextColor(mContext.getResources().getColor(R.color.color_ff7462));
                }

                break;

        }




        StringBuilder sb=new StringBuilder("￥");

        sb.append((dto.getTotalFee()/100));

        viewHolder.item_user_order_money_tv.setText(sb.toString());

        long time = ISO8601.getTime(dto.getBookingTime());
        String timeStr = TimeUtil.getTime(time);

        viewHolder.item_user_order_time_tv.setText(timeStr);


        String idStr = CheckUtil.splitStringWithNum(dto.getOrderNo(), 4);

        viewHolder.item_user_order_id_tv.setText(idStr);




        // 如果设置了回调，则设置点击事件
        if (mOnAdapterIconClickLitener != null) {
            viewHolder.item_user_order_head_img_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnAdapterIconClickLitener.onAdapterIconClick(i, dto);
                }
            });

        }

        if (mOnOrderItemClickLitener==null){
            return;
        }

        viewHolder.item_user_order_handle_left_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dto.getOrderStatus() == 0) {
                    //取消订单
                    mOnOrderItemClickLitener.OnCancelEvent(i,dto.getOrderId());
                } else {
                    if (dto.getFeedbackStatus() == 0) {
                        //投诉
                        mOnOrderItemClickLitener.OnComplaintsEvent(i,dto.getOrderId());

                    } else if (dto.getFeedbackStatus() == 2) {
                        //反馈结果
                        mOnOrderItemClickLitener.OnFeedbackEvent(i,dto.getOrderId(),dto.getFeedbackResult());
                    }
                }
            }

        });

        viewHolder.item_user_order_handle_right_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (dto.getOrderStatus()) {
                    case 0://付款

                        mOnOrderItemClickLitener.OnPaymentEvent(i,dto.getOrderId());

                        break;

                    case 1://咨询

                        mOnOrderItemClickLitener.OnAdvisoryEvent(i,dto.getOrderId(),dto.getUser().getUserId(),dto.getOrderStatus(),dto.getCounselType());
                        break;

                    case 3://评价

                        mOnOrderItemClickLitener.OnEvaluationEvent(i,dto.getOrderId());
                        break;

                    case 5://待确认完成：咨询师端请求结束
                        mOnOrderItemClickLitener.OnAdvisoryEvent(i,dto.getOrderId(),dto.getUser().getUserId(),dto.getOrderStatus(),dto.getCounselType());
                        break;

                }
            }
        });

    }


    @Override
    public int getAdapterItemCount() {
        // 返回数据总数
        return list == null ? 0 : list.size();
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_user_order_type_name_tv;

        private TextView item_user_order_status_tv;

        private ImageView item_user_order_head_img_iv;

        private TextView item_user_order_name_tv;

        private TextView item_user_order_id_tv;

        private TextView item_user_order_money_tv;

        private TextView item_user_order_time_tv;

        private View item_user_order_handle_line;

        private LinearLayout item_user_order_handle_layout;

        private TextView item_user_order_handle_left_tv;

        private TextView item_user_order_handle_right_tv;

        public ViewHolder(View v, boolean isItme) {
            super(v);

            if (isItme) {

                item_user_order_type_name_tv = (TextView) v.findViewById(R.id.item_user_order_type_name_tv);

                item_user_order_status_tv = (TextView) v.findViewById(R.id.item_user_order_status_tv);

                item_user_order_head_img_iv = (ImageView) v.findViewById(R.id.item_user_order_head_img_iv);

                item_user_order_name_tv = (TextView) v.findViewById(R.id.item_user_order_name_tv);

                item_user_order_id_tv = (TextView) v.findViewById(R.id.item_user_order_id_tv);

                item_user_order_money_tv = (TextView) v.findViewById(R.id.item_user_order_money_tv);

                item_user_order_time_tv = (TextView) v.findViewById(R.id.item_user_order_time_tv);

                item_user_order_handle_line = v.findViewById(R.id.item_user_order_handle_line);

                item_user_order_handle_layout = (LinearLayout) v.findViewById(R.id.item_user_order_handle_layout);

                item_user_order_handle_left_tv = (TextView) v.findViewById(R.id.item_user_order_handle_left_tv);

                item_user_order_handle_right_tv = (TextView) v.findViewById(R.id.item_user_order_handle_right_tv);

            }


        }
    }

    public interface OnOrderItemClickLitener {

        void OnCancelEvent(int position,String orderId);

        void OnComplaintsEvent(int position,String orderId);

        void OnFeedbackEvent(int position,String orderId,String feedbackResult);

        void OnPaymentEvent(int position,String orderId);

        void OnAdvisoryEvent(int position,String orderId,int counselId,int orderStatus,int counselType);

        void OnEvaluationEvent(int position,String orderId);


    }


}
