package cn.chono.yopper.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingPraiseDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.TimeUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class BubblingPraiseListAdapter extends BaseAdapter {

    public interface OnItemPraiseClickLitener {

        /**
         * 内容图片点击监听
         *
         * @param view
         * @param position
         * @param userID
         */
        void onItemPraiseClick(View view, int position, int userID);

    }

    private OnItemPraiseClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(
            OnItemPraiseClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private Context context;
    private List<BubblingPraiseDto.BubblingPraise> list;

    private BitmapPool mPool;
    private CropCircleTransformation transformation;

    public BubblingPraiseListAdapter(Context context) {
        this.context = context;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public void setData(List<BubblingPraiseDto.BubblingPraise> list) {
        this.list = list;
    }

    public void addData(List<BubblingPraiseDto.BubblingPraise> addlist) {
        if (list == null) {
            list = new ArrayList<BubblingPraiseDto.BubblingPraise>();
        }
        list.addAll(addlist);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
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

    NewViewHolder newViewHolder = null;

    @SuppressWarnings({"unused", "unchecked"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            newViewHolder = new NewViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bubbling_praise_list_item_layout, null);

            newViewHolder.userIcon = (ImageView) convertView.findViewById(R.id.userIcon);
            newViewHolder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
            newViewHolder.constellationTv = (TextView) convertView.findViewById(R.id.constellationTv);
            newViewHolder.tiemTv = (TextView) convertView.findViewById(R.id.tiemTv);
            newViewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            convertView.setTag(newViewHolder);

        } else {
            newViewHolder = (NewViewHolder) convertView.getTag();
        }

        String url = list.get(position).getUser().getHeadImg();

        String imageurl = ImgUtils.DealImageUrl(url, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

        Glide.with(context).load(imageurl).error(R.drawable.error_user_icon).bitmapTransform(transformation).into(newViewHolder.userIcon);

        newViewHolder.nameTv.setText(list.get(position).getUser().getName());

        int sex = list.get(position).getUser().getSex();

        if (sex == 1) {
            Drawable sexDrawable = context.getResources().getDrawable(R.drawable.ic_sex_man);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            newViewHolder.constellationTv.setCompoundDrawables(sexDrawable, null, null, null);
            newViewHolder.constellationTv.setTextColor(context.getResources().getColor(R.color.color_8cd2ff));
        } else if (sex == 2) {
            Drawable sexDrawable = context.getResources().getDrawable(R.drawable.ic_sex_woman);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            newViewHolder.constellationTv.setCompoundDrawables(sexDrawable, null, null, null);
            newViewHolder.constellationTv.setTextColor(context.getResources().getColor(R.color.color_fe8cd9));
        }

        long time = ISO8601.getTime(list.get(position).getCreateTime());
        String timeStr = TimeUtil.normalTimeFormat(time);
        newViewHolder.tiemTv.setText(timeStr);
        newViewHolder.constellationTv.setText(CheckUtil
                .ConstellationMatching(list.get(position).getUser()
                        .getHoroscope()));

        if (mOnItemClickLitener != null) {
            newViewHolder.linearLayout
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.onItemPraiseClick(
                                    newViewHolder.linearLayout, position, list
                                            .get(position).getUser().getId());

                        }
                    });
        }
        return convertView;
    }

    public class NewViewHolder {
        /**
         * 图片
         */
        private ImageView userIcon;

        private TextView nameTv;
        private TextView constellationTv;
        private TextView tiemTv;

        private LinearLayout linearLayout;

    }

}
