package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.utils.UnitUtil;

public class PublishBubblingAdapter extends BaseAdapter {

    public interface OnItemClickLitener {
        /**
         * 整条Item点击监听事件
         *
         * @param view
         * @param position
         */
        void onRemoveItemClick(View view, int position);

        /**
         * 内容图片点击监听
         *
         * @param view
         * @param position
         */
        void onAddItemClick(View view, int position);

    }

    OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {

        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    Context context;

    List<String> list;

    int screen_weight;

    public PublishBubblingAdapter(Context context) {

        this.context = context;

        screen_weight = UnitUtil.getScreenWidthPixels(context);// 屏幕的总宽度
    }

    public void setData(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {

        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }


    NewViewHolder newViewHolder = null;

    @SuppressWarnings("unused")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            newViewHolder = new NewViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.publish_bubbling_item_icon_layout, null);

            newViewHolder.contentIcon = (ImageView) convertView.findViewById(R.id.contentIcon);

            newViewHolder.removeIcon = (ImageView) convertView.findViewById(R.id.removeIcon);


            int range = UnitUtil.dip2px(10, context);// 减去最外层的padding的值转换成px

            int MaxWidth = screen_weight - range;// 一屏显示最大的宽度

            int gridviewItmeWidth = MaxWidth / 3;// 算出单个item的最大宽度

            int iconWidth = gridviewItmeWidth - UnitUtil.dip2px(10, context);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newViewHolder.contentIcon.getLayoutParams();

            params.width = iconWidth;

            params.height = iconWidth;

            newViewHolder.contentIcon.setLayoutParams(params);

            convertView.setTag(newViewHolder);

        } else {

            newViewHolder = (NewViewHolder) convertView.getTag();
        }

        final String urlStr = list.get(position);

        if (TextUtils.equals(urlStr, "NO_IMAGE")) {

            newViewHolder.removeIcon.setVisibility(View.GONE);


            Glide.with(context).load(R.drawable.icon_addpic_unfocused).error(R.drawable.error_default_icon).dontAnimate().into(newViewHolder.contentIcon);

        } else {

            newViewHolder.removeIcon.setVisibility(View.VISIBLE);

            Glide.with(context).load(urlStr).error(R.drawable.error_default_icon).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().centerCrop().into(newViewHolder.contentIcon);
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {

            newViewHolder.contentIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.equals(urlStr, "NO_IMAGE")) {

                        mOnItemClickLitener.onAddItemClick(newViewHolder.contentIcon, position);
                    }

                }
            });

            newViewHolder.removeIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemClickLitener.onRemoveItemClick(newViewHolder.removeIcon, position);
                }
            });
        }
        return convertView;
    }

    public class NewViewHolder {
        /**
         * 图片
         */
        public ImageView contentIcon;

        /**
         * 删除图片
         */
        public ImageView removeIcon;

    }

}
