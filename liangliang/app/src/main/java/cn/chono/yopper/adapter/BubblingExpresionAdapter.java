package cn.chono.yopper.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import cn.chono.yopper.data.BubblingEvaluateDto;
import cn.chono.yopper.data.BubblingEvaluateDto.ToUser;
import cn.chono.yopper.data.BubblingEvaluateDto.User;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.FaceTextUtils;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.TimeUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class BubblingExpresionAdapter extends BaseAdapter {

    public interface OnItemExpresionClickLitener {


        void onExpresionItemClick(View view, int position, User user);

        void onExpresionIconItemClick(View view, int position, int userID);

    }

    private OnItemExpresionClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(
            OnItemExpresionClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private List<BubblingEvaluateDto.BubblingEvaluate> list;

    private Context context;

    private BitmapPool mPool;
    private CropCircleTransformation transformation;

    public BubblingExpresionAdapter(Context context) {
        this.context = context;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public void setData(List<BubblingEvaluateDto.BubblingEvaluate> list) {
        this.list = list;
    }

    public void addData(List<BubblingEvaluateDto.BubblingEvaluate> addlist) {
        if (list == null) {
            list = new ArrayList<BubblingEvaluateDto.BubblingEvaluate>();
        }
        list.addAll(addlist);
    }

    private class NewViewHolder {
        /**
         * 图片
         */
        private ImageView userIcon;

        private TextView nameTv;

        private TextView expresionTv;

        private TextView tiemTv;

        private LinearLayout expresionLayout;

        private View view_bottom;

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

    NewViewHolder newViewHolder;

    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            newViewHolder = new NewViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.bubbling_expresion_layout, null);

            newViewHolder.userIcon = (ImageView) convertView
                    .findViewById(R.id.userIcon);

            newViewHolder.nameTv = (TextView) convertView
                    .findViewById(R.id.nameTv);
            newViewHolder.expresionTv = (TextView) convertView
                    .findViewById(R.id.expresionTv);
            newViewHolder.tiemTv = (TextView) convertView
                    .findViewById(R.id.tiemTv);
            newViewHolder.expresionLayout = (LinearLayout) convertView
                    .findViewById(R.id.expresionLayout);
            newViewHolder.view_bottom = (View) convertView
                    .findViewById(R.id.view_bottom);
            convertView.setTag(newViewHolder);

        } else {
            newViewHolder = (NewViewHolder) convertView.getTag();
        }

        if (list.size() - 1 == position) {
            newViewHolder.view_bottom.setVisibility(View.GONE);
        } else {
            newViewHolder.view_bottom.setVisibility(View.VISIBLE);
        }

        final User user = list.get(position).getUser();
        if (user != null) {

            String uresUrl = user.getHeadImg();
            String uresIcoUrl = ImgUtils.DealImageUrl(uresUrl,
                    YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

            Glide.with(context).load(uresIcoUrl)
                    .error(R.drawable.error_user_icon)
                    .bitmapTransform(transformation)
                    .into(newViewHolder.userIcon);
            newViewHolder.nameTv.setText(user.getName());

        }

        String content = "";

        ToUser toUser = list.get(position).getToUser();
        int toUserNameSize = 0;
        int contentSize = 0;
        if (toUser != null) {
            content = "回复" + toUser.getName() + ":";
            content = content + list.get(position).getComment();
            contentSize = content.indexOf(toUser.getName());
            toUserNameSize = toUser.getName().length();
            String contentStr = CheckUtil.ToDBC(content);
            SpannableString spannableString = FaceTextUtils.toSpannableString(
                    context, contentStr);

            SpannableStringBuilder builder = new SpannableStringBuilder(
                    spannableString);

            ForegroundColorSpan blackSpan = new ForegroundColorSpan(context
                    .getResources().getColor(R.color.color_333333));

            builder.setSpan(blackSpan, contentSize, contentSize
                    + toUserNameSize, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            newViewHolder.expresionTv.setText(builder);

        } else {
            content = list.get(position).getComment();

            //
            // SpannableStringBuilder builder = new
            // SpannableStringBuilder(content);
            // ForegroundColorSpan blackSpan = new ForegroundColorSpan(context
            // .getResources().getColor(R.color.text_color_gray));
            //
            // builder.setSpan(blackSpan, 0, content.length(),
            // Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // newViewHolder.expresionTv.setText(builder);

            if (!TextUtils.isEmpty(content)) {
                String contentStr = CheckUtil.ToDBC(content);
                SpannableString spannableString = FaceTextUtils
                        .toSpannableString(context, contentStr);
                newViewHolder.expresionTv.setText(spannableString,
                        TextView.BufferType.SPANNABLE);
                newViewHolder.expresionTv.setTextColor(context.getResources()
                        .getColor(R.color.color_999999));
            }

        }

        long time = ISO8601.getTime(list.get(position).getCreateTime());
        String timeStr = TimeUtil.normalTimeFormat(time);
        newViewHolder.tiemTv.setText(timeStr);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            newViewHolder.expresionLayout
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.onExpresionItemClick(
                                    newViewHolder.expresionLayout, position,
                                    user);
                        }
                    });
            newViewHolder.userIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onExpresionIconItemClick(
                            newViewHolder.userIcon, position, user.getId());
                }
            });
        }
        return convertView;
    }

}
