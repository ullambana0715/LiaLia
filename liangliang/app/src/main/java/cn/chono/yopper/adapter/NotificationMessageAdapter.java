package cn.chono.yopper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.util.ArrayList;
import java.util.List;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.NotificationMsg;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.TimeUtil;

public class NotificationMessageAdapter extends BaseAdapter {
    /**
     * 这个用来填充list
     */
    private List<ChatDto> list;
    /**
     * context上下文,用来获得convertView
     */
    private Context mContext;


    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    public NotificationMessageAdapter(Context context) {
        // 初始化
        mContext = context;

        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public List<ChatDto> getList() {
        return list;
    }

    public void setList(List<ChatDto> list) {
        if (list == null) {
            list = new ArrayList<ChatDto>();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        // 如果没有设置过,初始化convertView
        if (convertView == null) {
            // 获得设置的view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.notify_message_list_item, parent, false);

            // 初始化holder
            holder = new ViewHolder();

            holder.icon_img_iv = (ImageView) convertView.findViewById(R.id.message_item_icon_img_iv);

            holder.name_tv = (TextView) convertView.findViewById(R.id.message_item_name_tv);

            holder.time_tv = (TextView) convertView.findViewById(R.id.message_item_time_tv);

            holder.content_tv = (TextView) convertView.findViewById(R.id.message_item_content_tv);

            holder.num_tv = (TextView) convertView.findViewById(R.id.message_item_num_tv);

            holder.message_item_icon_iv = (ImageView) convertView.findViewById(R.id.message_item_icon_iv);

            holder.message_item_badge_tv = (TextView) convertView.findViewById(R.id.message_item_badge_tv);

            holder.message_item_charm_tv = (TextView) convertView.findViewById(R.id.message_item_charm_tv);

            convertView.setTag(holder);

        } else {
            // 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();

        }


        ChatDto dto = list.get(position);

        NotificationMsg notificationMsg = JsonUtils.fromJson(dto.getMessage(), NotificationMsg.class);

        int notifytype = notificationMsg.getNotifytype();





        holder.name_tv.setText(notificationMsg.getTitle() + "");


        String imageurl = ImgUtils.DealImageUrl(notificationMsg.getAvatar(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

        Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(holder.icon_img_iv);


        String attractionCount = "";
        if (notificationMsg.getExtra() != null) {
            if(notificationMsg.getExtra().get("attractionCount")!=null){
                attractionCount = notificationMsg.getExtra().get("attractionCount").toString();
            }
        }

        if (notifytype == 28) {
            holder.content_tv.setVisibility(View.VISIBLE);
            holder.content_tv.setText(notificationMsg.getContent() + "");
            holder.message_item_charm_tv.setVisibility(View.VISIBLE);
            holder.message_item_charm_tv.setText("魅力值+"+Integer.valueOf(attractionCount));
        } else {
            holder.content_tv.setVisibility(View.VISIBLE);
            holder.message_item_charm_tv.setVisibility(View.GONE);
            holder.content_tv.setText(notificationMsg.getContent() + "");
        }


        // 时间设置
        holder.time_tv.setText(msgTimeFormat(dto.getDate()) + "");

        if (notificationMsg.getBadge() == -1) {
            //不显示数量
            holder.num_tv.setVisibility(View.INVISIBLE);
            holder.message_item_badge_tv.setVisibility(View.INVISIBLE);
        } else {

            if (notificationMsg.getBadge() == 0) {
                holder.num_tv.setVisibility(View.GONE);
                holder.message_item_badge_tv.setVisibility(View.VISIBLE);

            } else if (notificationMsg.getBadge() >= 1) {
                if (notificationMsg.getBadge() < 10) {
                    holder.num_tv.setBackgroundResource(R.drawable.circle_messaga_num_bg);
                } else {
                    holder.num_tv.setBackgroundResource(R.drawable.center_messaga_num_bg);
                }
                holder.message_item_badge_tv.setVisibility(View.GONE);
                holder.num_tv.setVisibility(View.VISIBLE);
                holder.num_tv.setText(notificationMsg.getBadge() + "");
            }
        }

        if (CheckUtil.isEmpty(notificationMsg.getIcon())) {
            holder.message_item_icon_iv.setVisibility(View.INVISIBLE);

        } else {
            if (!dto.getJid().equals("system_notification")) {
                holder.message_item_icon_iv.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(notificationMsg.getIcon()).into(holder.message_item_icon_iv);
            }

        }


        return convertView;

    }

    /**
     * ViewHolder
     *
     * @Title:
     * @Description:主要是避免了不断的view获取初始化.
     * @Since:2015-3-22
     * @Version:
     */
    class ViewHolder {

        public ImageView icon_img_iv;
        public TextView name_tv;

        public TextView time_tv;
        public TextView content_tv;
        public TextView num_tv;

        public TextView message_item_badge_tv;

        public TextView message_item_charm_tv;

        public ImageView message_item_icon_iv;

    }

    private String msgTimeFormat(long datetime) {

        String time = "";

        long system_time = System.currentTimeMillis();
        if (TimeUtil.isToday(datetime)) {
            // 是当天

            int min = TimeUtil.getdifMin(datetime, system_time);

            if (min == 0) {
                time = "刚刚";
            } else if (1 <= min && min < 60) {
                time = min + "分钟前";
            } else {
                time = TimeUtil.gethour_minString(datetime);
            }

        } else {
            // 消息时间不是当天
            time = TimeUtil.getDateString(datetime, system_time);
        }

        return time;
    }

}