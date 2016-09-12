package cn.chono.yopper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingList;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;


public class DiscoverBubblingInfoIconAdapter extends BaseAdapter {

    public interface OnIconItemClickLitener {

        /**
         * 内容图片点击监听
         *
         * @param view
         * @param position
         */
        void onIconItemClick(View view, int position, List<String> list,
                             int imgViewWidth, int imgViewHight);

//		void checkLock(boolean islock);

    }

    private OnIconItemClickLitener mOnIconItemClickLitener;

    public void setOnIconItemClickLitener(
            OnIconItemClickLitener mOnIconItemClickLitener) {
        this.mOnIconItemClickLitener = mOnIconItemClickLitener;
    }

    private List<String> list;

    private BubblingList bubblingList;

    private Context context;

    float startScale;

    public DiscoverBubblingInfoIconAdapter(Context context) {
        this.context = context;
    }

    private class NewViewHolder {
        /**
         * 图片
         */
        private ImageView contentIcon;

        private ImageView contentPrivateIcon;

    }

    public void setData(BubblingList list) {
        bubblingList = list;
        this.list = bubblingList.getImageUrls();
    }

    @Override
    public int getCount() {

        int size = 0;

        if (list == null) {
            size = 0;
        } else if (list.size() > 9) {
            size = 9;
        } else {

            size = list.size();
        }

        return size;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private NewViewHolder newViewHolder;

    @SuppressWarnings("unused")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;
        if (v == null) {
            newViewHolder = new NewViewHolder();
            v = LayoutInflater.from(context).inflate(
                    R.layout.discover_bubbling_item_icon_layout, null);

            newViewHolder.contentIcon = (ImageView) v
                    .findViewById(R.id.contentIcon);
            newViewHolder.contentIcon.setScaleType(ScaleType.CENTER_CROP);

            newViewHolder.contentPrivateIcon = (ImageView) v.findViewById(R.id.contentPrivateIcon);
            int weight = UnitUtil.getScreenWidthPixels(context);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newViewHolder.contentIcon
                    .getLayoutParams();
            int range = UnitUtil.dip2px(84, context);
            params.width = (weight - range) / 3;
            params.height = (weight - range) / 3;
            newViewHolder.contentIcon.setLayoutParams(params);
            newViewHolder.contentPrivateIcon.setLayoutParams(params);
            v.setTag(newViewHolder);

        } else {
            newViewHolder = (NewViewHolder) v.getTag();
        }

        String url = list.get(position).toString();

        String imageurl = ImgUtils.DealImageUrl(url, YpSettings.IMG_SIZE_150,
                YpSettings.IMG_SIZE_150);

        Glide.with(context).load(imageurl).dontAnimate().centerCrop().error(R.drawable.error_user_icon)
                .into(newViewHolder.contentIcon);

        Logger.e("adapter是否是加密照片：：" + bubblingList.isUnlockPrivateImage());
        if(bubblingList.isUnlockPrivateImage()){//false 加密  true 不加密
            newViewHolder.contentPrivateIcon.setVisibility(View.GONE);
        }else{
            newViewHolder.contentPrivateIcon.setVisibility(View.VISIBLE);
        }

        // 如果设置了回调，则设置点击事件
        if (mOnIconItemClickLitener != null) {
            newViewHolder.contentIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int wight = newViewHolder.contentIcon.getWidth();
                    int hight = newViewHolder.contentIcon.getHeight();
					mOnIconItemClickLitener.onIconItemClick(
							newViewHolder.contentIcon, position, list, wight,
							hight);
                }
            });
        }

        newViewHolder.contentPrivateIcon.setOnClickListener(v1 -> {
            RxBus.get().post("bubblinginfolock", true);
        });

        return v;
    }

}
