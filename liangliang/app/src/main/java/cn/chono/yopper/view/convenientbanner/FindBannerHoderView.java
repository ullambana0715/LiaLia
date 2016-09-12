package cn.chono.yopper.view.convenientbanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cc.carousel.holder.Holder;
import cn.chono.yopper.R;

import cn.chono.yopper.Service.Http.DiscoverInfos.CampaignsEntity;

/**
 * Created by cc on 16/6/12.
 */
public class FindBannerHoderView implements Holder<CampaignsEntity> {

    private ImageView mImageView;

    @Override
    public View createView(Context context) {


        View bannerView = LayoutInflater.from(context).inflate(R.layout.item_findbanner, null);

        mImageView = (ImageView) bannerView.findViewById(R.id.item_findbanner_iv_banner);

        return bannerView;
    }

    @Override
    public void UpdateUI(Context context, int position, CampaignsEntity data) {


        mImageView.setImageResource(R.drawable.error_default_icon);

        String url = data.imageUrl;

        if (mImageView != null)

            Glide.with(context).load(url).into(mImageView);


    }


}
