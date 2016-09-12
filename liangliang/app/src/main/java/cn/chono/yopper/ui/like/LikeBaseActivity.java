package cn.chono.yopper.ui.like;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.view.TabFragmentIndicator;

/**
 * Created by jianghua on 2016/7/20.
 */
public class LikeBaseActivity extends MainFrameActivity implements TabFragmentIndicator.OnDiscoverTabSelecterdListener, View.OnClickListener {

    @BindView(R.id.like_goback_iv)
    LinearLayout likeGobackIv;
    @BindView(R.id.like_tabIndicator)
    TabFragmentIndicator likeTabIndicator;
    @BindView(R.id.tab_like_pager)
    ViewPager tabLikePager;

    protected Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_baselike);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this);

        likeGobackIv.setOnClickListener(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UnitUtil.dip2px(280, this), UnitUtil.dip2px(48, this));

        likeTabIndicator.addFragment(0, ILikeFragment.class);
        likeTabIndicator.addFragment(1, LikeMeFragment.class);
        likeTabIndicator.addFragment(2, LikeEachOtherFragment.class);

        likeTabIndicator.setLayoutParams(params);

        likeTabIndicator.setTabContainerView(R.layout.tab_like_indicator_layout);

        likeTabIndicator.setCursorW(280);
        likeTabIndicator.setTabSliderView(R.layout.tab_slider);
        likeTabIndicator.setOnDiscoverTabSelecterdListener(this);
        likeTabIndicator.setViewPager(tabLikePager, getSupportFragmentManager());

        tabLikePager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like_goback_iv:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setRedRemind();
    }

    private void setRedRemind() {
        boolean likeMe = SharedprefUtil.getBoolean(ContextUtil.getContext(), LoginUser.getInstance().getUserId() + "likeMe", false);

        if (likeMe) {
//            RxBus.get().post("likeMe", new CommonEvent());

            likeTabIndicator.newAttion(1);
        }else{
            likeTabIndicator.cancelAttion(1);
        }

        boolean likeEach = SharedprefUtil.getBoolean(ContextUtil.getContext(), LoginUser.getInstance().getUserId() + "likeEachOther", false);

        if (likeEach) {
//            RxBus.get().post("likeEachOther", new CommonEvent());
            likeTabIndicator.newAttion(2);
        }else{
            likeTabIndicator.cancelAttion(2);
        }
    }

    @Override
    public void onDiscoverTabSelected(int tabId) {
        switch (tabId) {
            case 0:
                RxBus.get().post("fragment_type_0", 0);
                break;
            case 1:
                RxBus.get().post("fragment_type_1", 1);

                break;
            case 2:
                RxBus.get().post("fragment_type_2", 2);

                break;
            default:
                break;
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeMe")

            }

    )
    public void onTopEvent(CommonEvent event) {

        setRedRemind();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachOther")

            }

    )
    public void likeEachOther(CommonEvent event) {

        setRedRemind();
    }


//    @Subscribe(
//            thread = EventThread.MAIN_THREAD,
//            tags = {
//                    @Tag("cancelAttion")
//
//            }
//
//    )
//    public void cancelAttion(Integer obj) {
//        setRedRemind();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        RxBus.get().unregister(this);
    }
}
