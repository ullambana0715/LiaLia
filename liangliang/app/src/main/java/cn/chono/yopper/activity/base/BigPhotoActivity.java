package cn.chono.yopper.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.view.viewpager.HackyViewPager;
import cn.chono.yopper.view.viewpager.PhotoView;
import cn.chono.yopper.view.viewpager.PhotoViewAttacher;

/**
 * 大图 相册
 * Created by cc on 16/5/4.
 */
public class BigPhotoActivity extends MainFrameActivity {

    HackyViewPager tarotAstrologyBigPhoto_hvp;

    String[] mAlbumImages;

    ArrayList<String> mPhotoImages;

    int mSubscript;

    boolean isDataType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_tarot_astrology_big_photo);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.PHOTO_SUBSCRIPT))
            mSubscript = bundle.getInt(YpSettings.PHOTO_SUBSCRIPT);

        if (bundle.containsKey(YpSettings.PHOTO_STRING)) {
            isDataType = false;
            mAlbumImages = bundle.getStringArray(YpSettings.PHOTO_STRING);
        }


        if (bundle.containsKey(YpSettings.PHOTO_LIST)) {
            isDataType = true;
            mPhotoImages = bundle.getStringArrayList(YpSettings.PHOTO_LIST);
        }
        initView();
    }

    private void initView() {

        getTitleLayout().setVisibility(View.GONE);


        tarotAstrologyBigPhoto_hvp = (HackyViewPager) findViewById(R.id.tarotAstrologyBigPhoto_hvp);

        SamplePagerAdapter adapter = new SamplePagerAdapter(this);
        tarotAstrologyBigPhoto_hvp.setAdapter(adapter);
        tarotAstrologyBigPhoto_hvp.addOnPageChangeListener(new PageChageListener());

        if (isDataType) {

            if (mPhotoImages != null && mPhotoImages.size() > mSubscript)
                tarotAstrologyBigPhoto_hvp.setCurrentItem(mSubscript);

            return;
        }

        if (mAlbumImages != null && mAlbumImages.length > mSubscript)
            tarotAstrologyBigPhoto_hvp.setCurrentItem(mSubscript);


    }

    private class PageChageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {

        }

    }

    private class SamplePagerAdapter extends PagerAdapter {

        private Context context;


        private SamplePagerAdapter(Context context) {
            this.context = context;
        }


        @Override
        public int getCount() {

            int count;

            if (isDataType) {

                count = mPhotoImages == null ? 0 : mPhotoImages.size();

            } else {
                count = mAlbumImages == null ? 0 : mAlbumImages.length;

            }

            return count;
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_tarot_astrology_big_photo, container, false);
            container.addView(v);

            final ProgressBar item_tarot_astrology_big_photo_pbar = (ProgressBar) v.findViewById(R.id.item_tarot_astrology_big_photo_pbar);
            final PhotoView item_tarot_astrology_big_photo_pv = (PhotoView) v.findViewById(R.id.item_tarot_astrology_big_photo_pv);


            String imgeUrl;

            if (isDataType) {

                imgeUrl = mPhotoImages.get(position);

            } else {
                imgeUrl = mAlbumImages[position];

            }


            item_tarot_astrology_big_photo_pbar.setVisibility(View.VISIBLE);
            Glide.with(context).load(imgeUrl).error(R.drawable.error_default_icon).listener(new RequestListener<String, GlideDrawable>() {

                @Override
                public boolean onException(Exception arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3) {
                    item_tarot_astrology_big_photo_pbar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {
                    item_tarot_astrology_big_photo_pbar.setVisibility(View.GONE);
                    return false;
                }

            }).into(item_tarot_astrology_big_photo_pv);

            item_tarot_astrology_big_photo_pv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });


            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
