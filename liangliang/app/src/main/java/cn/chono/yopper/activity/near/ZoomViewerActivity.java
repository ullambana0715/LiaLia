package cn.chono.yopper.activity.near;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.ZoomViewerDto;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.view.viewpager.HackyViewPager;
import cn.chono.yopper.view.viewpager.PhotoView;

public class ZoomViewerActivity extends MainFrameActivity {

    private HackyViewPager viewPager;

    private ZoomViewerDto sq;

    private SamplePagerAdapter adapter;

    private String type;


    int loginUserId;

    int userId;




    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.zoom_image_viewer_activity);

        PushAgent.getInstance(this).onAppStart();

        sq = (ZoomViewerDto) getIntent().getExtras().getSerializable(YpSettings.ZOOM_LIST_DTO);

        loginUserId = LoginUser.getInstance().getUserId();

        initView();

        setViewPageData();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(type) && type.equals("ArticleContentDetailActivity")) {

            MobclickAgent.onPageStart("文章（预览照片）"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        } else {

            MobclickAgent.onPageStart("冒泡（预览照片）"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        }


        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!TextUtils.isEmpty(type) && type.equals("ArticleContentDetailActivity")) {

            MobclickAgent.onPageEnd("文章（预览照片）"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        } else {

            MobclickAgent.onPageEnd("冒泡（预览照片）"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        }


        MobclickAgent.onPause(this); // 统计时长
    }

    private void initView() {

        getTitleLayout().setVisibility(View.GONE);

        viewPager = (HackyViewPager) findViewById(R.id.constellation_viewPager);

        adapter = new SamplePagerAdapter(ZoomViewerActivity.this);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new PageChageListener());
    }

    private void setViewPageData() {
        if (sq == null) {
            return;
        }


        userId = sq.UserId;

        list = sq.list;


        adapter.setData(list);

        viewPager.setCurrentItem(sq.position);

        adapter.notifyDataSetChanged();
    }

    private class PageChageListener implements OnPageChangeListener {

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

        private List<String> lists;

        private SamplePagerAdapter(Context context) {
            this.context = context;
        }

        public void setData(List<String> list) {

            this.lists = list;

        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {

            View v = LayoutInflater.from(context).inflate(R.layout.zoom_image_item_layout, null);

            container.addView(v);

            final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

            final PhotoView photoView = (PhotoView) v.findViewById(R.id.photoView);

            final String imgeUrl = list.get(position).toString().trim();


            String minimgUrl = ImgUtils.DealImageUrl(imgeUrl, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);


            Glide.with(context).load(minimgUrl).into(photoView);


            progressBar.setVisibility(View.VISIBLE);


            Glide.with(context).load(imgeUrl).error(R.drawable.error_default_icon).listener(new RequestListener<String, GlideDrawable>() {

                @Override
                public boolean onException(Exception arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3) {

                    progressBar.setVisibility(View.GONE);

                    DialogUtil.showDisCoverNetToast(ZoomViewerActivity.this);

                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {

                    progressBar.setVisibility(View.GONE);

                    return false;
                }

            }).into(photoView);

            photoView.setOnPhotoTapListener((view, x, t) -> {

                finish();
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


    @Override
    public void finish() {


        Intent i = new Intent();
        i.putExtra(YpSettings.ZOOM_LIST_DTO, sq);
        setResult(YpSettings.ZOOM_LIST_DTO_CODE, i);

        super.finish();
    }


}