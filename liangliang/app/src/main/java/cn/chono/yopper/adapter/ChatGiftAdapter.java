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

import butterknife.BindView;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.entity.chatgift.GiftInfoEntity;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.ImgUtils;


public class ChatGiftAdapter extends BaseAdapter {

    @BindView(R.id.chat_item_gift_iv)
    ImageView chatItemGiftIv;
    @BindView(R.id.chat_item_charm_tv)
    TextView chatItemCharmTv;
    @BindView(R.id.chat_item_pcount_tv)
    TextView chatItemPcountTv;

    private Context context;
    private List<GiftInfoEntity> list = new ArrayList<>();

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    public ChatGiftAdapter(Context context,List<GiftInfoEntity> datas) {
        list = datas;
        this.context = context;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from (context).inflate(R.layout.item_gift_text, null);
            holder = new ViewHolder();
            holder.chatItemCharmTv = (TextView) convertView.findViewById(R.id.chat_item_charm_tv);
            holder.chatItemGiftIv = (ImageView) convertView.findViewById(R.id.chat_item_gift_iv);
            holder.chatItemPcountTv = (TextView) convertView.findViewById(R.id.chat_item_pcount_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.chatItemCharmTv.setText("+" + list.get(position).getCharm());

        String imageUrl = ImgUtils.DealImageUrl(list.get(position).getImageUrl(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
//        .bitmapTransform(transformation)
        Glide.with(context).load(imageUrl).into(holder.chatItemGiftIv);

        holder.chatItemPcountTv.setText(list.get(position).getAppleCount() + "个苹果");
        return convertView;
    }

    class ViewHolder {
        ImageView chatItemGiftIv;
        TextView chatItemCharmTv;
        TextView chatItemPcountTv;
    }
}
