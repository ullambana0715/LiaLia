package cn.chono.yopper.activity.base;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DiscoverInfos.BannersEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.CampaignsEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.DiscoverInfosBean;
import cn.chono.yopper.Service.Http.DiscoverInfos.DiscoverInfosEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.DiscoverInfosRespEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.DiscoverInfosService;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindAstrologysEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindLuckEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindStoresEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindTarotAstrologysEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindTarotEntity;
import cn.chono.yopper.Service.Http.GainVersionInfo.GainVersionInfoRespBean;
import cn.chono.yopper.Service.Http.GainVersionInfo.GainVersionInfoService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.find.ArticleContentDetailActivity;
import cn.chono.yopper.activity.find.ArticleListActivity;
import cn.chono.yopper.activity.find.AstrolabeActivity;
import cn.chono.yopper.activity.find.CampaignsActivity;
import cn.chono.yopper.activity.find.FindWebActivity;
import cn.chono.yopper.activity.find.TarotOrAstrologysListActivity;
import cn.chono.yopper.activity.find.TarotWebActivity;
import cn.chono.yopper.adapter.FindsAdapter;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.FindsAdapterData;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.VersionChkDTO;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;

/**
 * Created by cc on 16/2/19.
 */
public class FindFragment extends Fragment {


    private RecyclerView find_context_recyclerView;

    private FindsAdapter findAdapter;


    private Dialog campaigns_dialog;


    private FindTarotAstrologysEntity mFindTarotAstrologysEntity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RxBus.get().register(this);

        View convertView = inflater.inflate(R.layout.find_activity, container, false);

        initView(convertView);

        initData();//初始化数据


        getData();


        return convertView;
    }

    private void initView(View convertView) {


        find_context_recyclerView = (RecyclerView) convertView.findViewById(R.id.find_context_recyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        find_context_recyclerView.setLayoutManager(layoutManager);
        find_context_recyclerView.setItemAnimator(new DefaultItemAnimator());

        findAdapter = new FindsAdapter(getActivity());

        find_context_recyclerView.setAdapter(findAdapter);


    }


    private void initData() {

        mFindTarotAstrologysEntity = new FindTarotAstrologysEntity();

        FindTarotEntity findTarotEntity = new FindTarotEntity();

        findTarotEntity.description = "小心纸牌的无限力量";

        findTarotEntity.name = "塔罗占卜";

        findTarotEntity.iconUrl = R.drawable.find_tarot_icon;

        mFindTarotAstrologysEntity.findTarotEntity = findTarotEntity;


        FindAstrologysEntity findAstrologysEntity = new FindAstrologysEntity();

        findAstrologysEntity.description = "无尽人生奥秘";

        findAstrologysEntity.name = "星盘解读";

        findAstrologysEntity.iconUrl = R.drawable.find_astrologys_icon;

        mFindTarotAstrologysEntity.findAstrologysEntity = findAstrologysEntity;


        FindLuckEntity findLuckEntity = new FindLuckEntity();

        findLuckEntity.name = "每日星运";

        findLuckEntity.description = "星座不求人";

        findLuckEntity.iconUrl = R.drawable.find_luck_icon;

        mFindTarotAstrologysEntity.findLuckEntity = findLuckEntity;


        FindStoresEntity findStoresEntity = new FindStoresEntity();

        findStoresEntity.name = "旗舰门店";

        findStoresEntity.description = "面对面解答";

        findStoresEntity.iconUrl = R.drawable.find_stores_icon;


        mFindTarotAstrologysEntity.findStoresEntity = findStoresEntity;


    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void getData() {

        String strProvince = "上海";

        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && !CheckUtil.isEmpty(myLoc.getCity())) {

            strProvince = myLoc.getProvince();


        }


        DiscoverInfosBean discoverInfosBean = new DiscoverInfosBean();

        discoverInfosBean.city = strProvince;


        DiscoverInfosService discoverInfosService = new DiscoverInfosService(getActivity());

        discoverInfosService.parameter(discoverInfosBean);

        discoverInfosService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);


                DiscoverInfosRespEntity discoverInfosRespEntity = (DiscoverInfosRespEntity) respBean;

                DiscoverInfosEntity discoverInfosEntity = discoverInfosRespEntity.resp;

                findAdapter.setInitAdapterData(initAdapterData(discoverInfosEntity));


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                findAdapter.setInitAdapterData(initAdapterData(null));
            }
        });
        discoverInfosService.enqueue();

    }

    private List<FindsAdapterData> initAdapterData(DiscoverInfosEntity discoverInfosEntity) {

        List<FindsAdapterData> findsAdapterDatas = new ArrayList<>();

        if (discoverInfosEntity != null && discoverInfosEntity.campaigns != null && discoverInfosEntity.campaigns.size() > 0) {

            FindsAdapterData campaigns = new FindsAdapterData();

            campaigns.type = 0;

            campaigns.campaigns = discoverInfosEntity.campaigns;

            findsAdapterDatas.add(campaigns);
        }

        if (discoverInfosEntity != null && discoverInfosEntity.banners != null && discoverInfosEntity.banners.size() > 0) {


            FindsAdapterData banners = new FindsAdapterData();


            banners.type = 1;

            banners.banners = discoverInfosEntity.banners;

            findsAdapterDatas.add(banners);

        }

        if (mFindTarotAstrologysEntity != null) {

            FindsAdapterData tarotAstrologysEntity = new FindsAdapterData();


            tarotAstrologysEntity.type = 2;

            tarotAstrologysEntity.findTarotAstrologysEntity = mFindTarotAstrologysEntity;

            findsAdapterDatas.add(tarotAstrologysEntity);
        }

        if (discoverInfosEntity != null && discoverInfosEntity.todayArticles != null && discoverInfosEntity.todayArticles.size() > 0) {

            FindsAdapterData todayArticles = new FindsAdapterData();

            todayArticles.type = 3;

            todayArticles.todayArticles = discoverInfosEntity.todayArticles;

            findsAdapterDatas.add(todayArticles);

        }

        return findsAdapterDatas;

    }


    /**
     * 获取版本信息
     */
    private void getVersionInfo() {

        GainVersionInfoService gainVersionInfoService = new GainVersionInfoService(getActivity());
        gainVersionInfoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                GainVersionInfoRespBean gainVersionInfoRespBean = (GainVersionInfoRespBean) respBean;
                VersionChkDTO dto = gainVersionInfoRespBean.getResp();

                if (dto != null) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(dto.getDownloadLink());
                    intent.setData(content_url);
                    startActivity(intent);

                }
            }
        });

        gainVersionInfoService.enqueue();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("Tarot")

            }

    )
    public void rxTarot(Object o) {



        MobclickAgent.onEvent(getActivity(), "btn_find_event_Tarotcards");
        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "tarot");

        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "塔罗占卜");

        ActivityUtil.jump(getActivity(), TarotWebActivity.class, bundle, 0, 100);


    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("TarotList")

            }

    )
    public void rxTarotList(Object o) {


        MobclickAgent.onEvent(getActivity(), "btn_find_event_tarot");


        Bundle tarotBd = new Bundle();

        tarotBd.putInt(YpSettings.COUNSEL_TYPE, Constant.CounselorType_Tarot);

        ActivityUtil.jump(getActivity(), TarotOrAstrologysListActivity.class, tarotBd, 0, 100);


    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("Astrologys")

            }

    )
    public void rxAstrologys(Object o) {


        MobclickAgent.onEvent(getActivity(), "btn_find_event_Astrolabechart");

        ActivityUtil.jump(getActivity(), AstrolabeActivity.class, null, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("AstrologysList")

            }

    )
    public void rxAstrologysList(Object o) {


        MobclickAgent.onEvent(getActivity(), "btn_find_event_astrolabe");

        Bundle astrologyBd = new Bundle();

        astrologyBd.putInt(YpSettings.COUNSEL_TYPE, Constant.CounselorType_Astrology);

        ActivityUtil.jump(getActivity(), TarotOrAstrologysListActivity.class, astrologyBd, 0, 100);

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("ArticleList")

            }

    )
    public void ArticleList(BannersEntity banners) {


        MobclickAgent.onEvent(getActivity(), "btn_find_event_Morearticle");

        Bundle bundle = new Bundle();

        String bannerId = banners.bannerId;

        String name = banners.name;

        bundle.putString(YpSettings.BannerId, bannerId);

        bundle.putString(YpSettings.BannerName, name);

        bundle.putBoolean("allowUserDefine", banners.allowUserDefine);

        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Article/index/" + bannerId);

        ActivityUtil.jump(getActivity(), ArticleListActivity.class, bundle, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("webEvent")

            }

    )
    public void webEvent(BannersEntity banners) {


        MobclickAgent.onEvent(getActivity(), "btn_find_article");

        Bundle bundle = new Bundle();
//
//        bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST, false);
//
//        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, webUrl);
//
//        ActivityUtil.jump(getActivity(), WebActivity.class, bundle, 0, 100);

        bundle.putString("TITLE", banners.name);

        bundle.putString("URL", banners.redirectUrl);

        ActivityUtil.jump(getActivity(), CampaignsActivity.class, bundle, 0, 100);

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("ActivityDetail")

            }

    )
    public void ActivityDetail(CampaignsEntity campaignsEntity) {


        int index = campaignsEntity.redirectUrl.indexOf("activityId=");

        int urlSize = campaignsEntity.redirectUrl.length();

        String dataStr = null;

        if (-1 != index) {

            dataStr = (String) campaignsEntity.redirectUrl.subSequence(index, urlSize);
        }

        String activityId = null;

        if (!TextUtils.isEmpty(dataStr)){

            int ind = dataStr.indexOf("=");

            int size = dataStr.length();

            if (-1 != ind) {

                activityId = (String) dataStr.subSequence(ind+1, size);
            }
        }

        LogUtils.e(activityId);

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "活动详情");

        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "activity/dtails?activityId=" + activityId);

        bundle.putInt(YpSettings.SOURCE_YTPE_KEY, 600);

        bundle.putString(YpSettings.ACTIVITY_ID, (activityId ==null ? "":activityId));



        ActivityUtil.jump(getActivity(), SimpleWebViewActivity.class, bundle, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("Default")

            }

    )
    public void Default(CampaignsEntity campaignsEntity) {

        Bundle bundle = new Bundle();

        bundle.putString("TITLE", campaignsEntity.title);

        bundle.putString("URL", campaignsEntity.redirectUrl);

        ActivityUtil.jump(getActivity(), CampaignsActivity.class, bundle, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("Article")

            }

    )
    public void Article(CampaignsEntity campaignsEntity) {

        Bundle bd = new Bundle();

        bd.putString(YpSettings.BUNDLE_KEY_WEB_URL, campaignsEntity.redirectUrl);

        bd.putString(YpSettings.BUNDLE_KEY_WEB_URL_TYPE, campaignsEntity.type);

        bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

        ActivityUtil.jump(getActivity(), ArticleContentDetailActivity.class, bd, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("RecommendDetail")

            }

    )
    public void RecommendDetail(String Id) {

        LogUtils.e("ID==="+Id);

        Bundle bd = new Bundle();

        StringBuffer stringBuffe = new StringBuffer("Article/Content/");

        stringBuffe.append(Id);

        bd.putString(YpSettings.BUNDLE_KEY_WEB_URL, stringBuffe.toString());

        bd.putString(YpSettings.BUNDLE_KEY_WEB_URL_TYPE, Id);

        bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

        ActivityUtil.jump(getActivity(), ArticleContentDetailActivity.class, bd, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("campaigns_dialog")

            }

    )
    public void campaigns_dialog(CampaignsEntity campaignsEntity) {

        campaigns_dialog = DialogUtil.createHintOperateDialog(getActivity(), "", "使用此功能需要升级到最新版，现在去更新？", "取消", "确认", new BackCallListener() {
            @Override
            public void onEnsure(View view, Object... obj) {
                campaigns_dialog.dismiss();
            }

            @Override
            public void onCancel(View view, Object... obj) {

                getVersionInfo();

                campaigns_dialog.dismiss();
            }

        });

        campaigns_dialog.show();

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("TarotShop")

            }

    )
    public void TarotShop(Object o) {

        MobclickAgent.onEvent(getActivity(), "btn_find_event_Store");

        Bundle bundle = new Bundle();

        bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST, true);

        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "about/TarotShop");

        bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, true);

        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "旗舰门店");

        ActivityUtil.jump(getActivity(), FindWebActivity.class, bundle, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("Luck")

            }

    )
    public void Luck(Object o) {

        MobclickAgent.onEvent(getActivity(), "btn_find_event_Horoscope");

        UserDto mydto = DbHelperUtils.getDbUserInfo(LoginUser.getInstance().getUserId());
        int myhor = mydto.getProfile().getHoroscope();

        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Constellation/Luck?id=" + myhor + "&userid=" + LoginUser.getInstance().getUserId() + "&AuthToken=" + LoginUser.getInstance().getAuthToken());
        bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, true);
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "每日星运");
        ActivityUtil.jump(getActivity(), FindWebActivity.class, bundle, 0, 100);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister(this);

        if (findAdapter != null) {
            findAdapter.unregister();
        }


    }


}
