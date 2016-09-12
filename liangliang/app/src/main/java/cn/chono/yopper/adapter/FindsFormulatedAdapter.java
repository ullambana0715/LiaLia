package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DiscoverInfos.BannersEntity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * Created by cc on 16/6/12.
 */
public class FindsFormulatedAdapter extends BaseAdapter {


    Context mContext;

    List<BannersEntity> subBanners;

    public FindsFormulatedAdapter(Context context) {
        mContext = context;
    }

    public void setSubBanners(List<BannersEntity> subBanners) {
        this.subBanners = subBanners;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {


        return subBanners == null ? 0 : subBanners.size();
    }

    @Override
    public Object getItem(int position) {
        return subBanners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ViewHolder viewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_finds_formulated, parent, false);

            viewHolder.itemFindsFormulated_iv_icon = (ImageView) convertView.findViewById(R.id.itemFindsFormulated_iv_icon);

            viewHolder.itemFindsFormulated_tv_name = (TextView) convertView.findViewById(R.id.itemFindsFormulated_tv_name);

            convertView.setTag(viewHolder);


        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        final BannersEntity bannersSubBanners = subBanners.get(position);

        if (0 == position) {

            RxBus.get().post("bannersEvent", bannersSubBanners);

        }


        String imageurl = ImgUtils.DealImageUrl(bannersSubBanners.iconUrl, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

        Glide.with(mContext).load(imageurl).centerCrop().error(R.drawable.error_default_icon).into(viewHolder.itemFindsFormulated_iv_icon);


        viewHolder.itemFindsFormulated_tv_name.setText(bannersSubBanners.name == null ? "" : bannersSubBanners.name);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);


                if (!TextUtils.isEmpty(bannersSubBanners.redirectUrl)) {

                    RxBus.get().post("webEvent",bannersSubBanners);

                    return;
                }

                RxBus.get().post("ArticleList", bannersSubBanners);

            }
        });


        return convertView;
    }


    private class ViewHolder {

        ImageView itemFindsFormulated_iv_icon;

        TextView itemFindsFormulated_tv_name;


    }
}
