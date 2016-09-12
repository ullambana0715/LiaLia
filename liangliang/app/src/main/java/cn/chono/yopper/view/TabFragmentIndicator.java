package cn.chono.yopper.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.UnitUtil;

public class TabFragmentIndicator extends LinearLayout implements ViewPager.OnPageChangeListener, OnClickListener {

    OnDiscoverTabSelecterdListener onDiscoverTabSelecterdListener;

    Context mContext;
    ViewPager mViewPager;
    View container;
    private View slider;
    private int tabNum;
    private int selectedPage = 0;
    private float unitWidth;
    private int cursorWidth = 180;
    List<View> viewList = new ArrayList<>();

    public int getCursorWidth() {
        return cursorWidth;
    }

    SectionsPagerAdapter mSectionsPagerAdapter;
    private ArrayList<Class<?>> fragmentList = new ArrayList<Class<?>>();

    public TabFragmentIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    public void setViewPager(ViewPager viewPager, FragmentManager fragmentManager) {
        viewPager.setOffscreenPageLimit(3);
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    public void addFragment(int index, Class<?> claz) {
        fragmentList.add(index, claz);
        if (mSectionsPagerAdapter != null)
            mSectionsPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 设置tabview
     *
     * @param layoutId
     */
    public void setTabContainerView(int layoutId) {

        container = LayoutInflater.from(mContext).inflate(layoutId, null);

        this.addView(container, 0);

        int height = (int) getResources().getDimension(R.dimen.tab_height);
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.height = height;
        container.setLayoutParams(params);


        if (container instanceof ViewGroup) {
            tabNum = ((ViewGroup) container).getChildCount();
            for (int i = 0; i < tabNum; i++) {
                ((ViewGroup) container).getChildAt(i).setTag(i);
                ((ViewGroup) container).getChildAt(i).setOnClickListener(this);
            }
        }

        for (int i = 0; i < tabNum; i++) {
            View view = ((ViewGroup) container).getChildAt(i);
            viewList.add(view);
        }

    }

    public void setTabSliderView(int layoutId) {
        slider = LayoutInflater.from(mContext).inflate(layoutId, null);
        this.addView(slider, 1);
        setCursorWidth(cursorWidth);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            return Fragment.instantiate(mContext, fragmentList.get(index).getName(), null);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


    @Override
    public void onPageSelected(int position) {

        if (selectedPage != position) {
            selectedPage = position;
            setClik(position);

            onDiscoverTabSelecterdListener.onDiscoverTabSelected(position);


        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View v) {
        final int index = (Integer) v.getTag();

        if (selectedPage == index) {


            onDiscoverTabSelecterdListener.onDiscoverTabSelected(index);

            return;
        }
        setClik(index);


    }

    public void setClik(final int index) {


        slider.setTranslationX(0);
        TranslateAnimation animation = new TranslateAnimation(selectedPage * unitWidth, index * unitWidth, 0, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(100);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        slider.startAnimation(animation);


        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mViewPager.setCurrentItem(index, true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slider.clearAnimation();
                slider.setTranslationX(selectedPage * unitWidth);

            }
        });
    }

    public void setCursorW(int width) {
        this.cursorWidth = width;
    }

    /**
     * 设置cursor的宽度，并获取移动的单位长度float
     **/
    private void setCursorWidth(int Width) {


        int cursorWidth = UnitUtil.dip2px(Width, mContext) / tabNum;
        unitWidth = (float) UnitUtil.dip2px(Width, mContext) / tabNum;


        int cursorHeight = (int) getResources().getDimension(R.dimen.cursor_height);

        ViewGroup.LayoutParams params = slider.getLayoutParams();
        params.height = cursorHeight;
        params.width = cursorWidth;

        slider.setLayoutParams(params);
    }


    public void setOnDiscoverTabSelecterdListener(OnDiscoverTabSelecterdListener onDiscoverTabSelecterdListener) {
        this.onDiscoverTabSelecterdListener = onDiscoverTabSelecterdListener;
    }

    //新关注
    public void newAttion(Integer position) {
        //处理事件
        View view = viewList.get(position);
//        if(position == 0){
//            view.findViewById(R.id.icon_look_1).setVisibility(View.VISIBLE);
//        }
        ((ViewGroup) view).getChildAt(1).setVisibility(View.VISIBLE);
    }


    //取消新关注
    public void cancelAttion(Integer position) {
        //处理事件

        View view = viewList.get(position);
        Logger.e("子视图有" + ((ViewGroup) view).getChildCount() + "个" + position);
        ((ViewGroup) view).getChildAt(1).setVisibility(View.GONE);

        if (position == 1) {

            SharedprefUtil.saveBoolean(ContextUtil.getContext(), LoginUser.getInstance().getUserId() + "likeMe", false);

        } else if (position == 2) {

            SharedprefUtil.saveBoolean(ContextUtil.getContext(), LoginUser.getInstance().getUserId() + "likeEachOther", false);

        }
    }

    public interface OnDiscoverTabSelecterdListener {

        public void onDiscoverTabSelected(int tabId);
    }
}
