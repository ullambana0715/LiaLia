package cn.chono.yopper.activity.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BubbleLimit.BubbleLimitRespBean;
import cn.chono.yopper.Service.Http.BubbleLimit.BubbleLimitService;
import cn.chono.yopper.Service.Http.DatingRequirment.DatingRequirmentRespBean;
import cn.chono.yopper.Service.Http.DatingRequirment.DatingRequirmentService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.YpBaseFragment;
import cn.chono.yopper.activity.appointment.AppointPublishTypeSelectActivity;
import cn.chono.yopper.activity.appointment.AppointmentFilterActivity;
import cn.chono.yopper.activity.near.BubblingAddressActivity;
import cn.chono.yopper.activity.near.DiscoverBubblingFragment;
import cn.chono.yopper.activity.near.DiscoverPeopleFragment;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AppointFilterDto;
import cn.chono.yopper.data.ChatAttamptRespDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.DatingRequirementData;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.event.DatingsFilterEvent;
import cn.chono.yopper.event.OnNearPeopleFilterEvent;
import cn.chono.yopper.event.OnNearTabEvent;
import cn.chono.yopper.event.OnTopEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.BackCallSex;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.TabFragmentIndicator;
import cn.chono.yopper.view.TabFragmentIndicator.OnDiscoverTabSelecterdListener;

public class DiscoverListFragment extends YpBaseFragment implements OnDiscoverTabSelecterdListener, OnClickListener {

    private ViewPager mViewPager;
    private TabFragmentIndicator tabFragmentIndicator;
    //人刷选
    private TextView discover_fragment_filter_tv;
    //冒泡
    private ImageView discover_fragment_bubbling_tv;


    private LinearLayout appoint_fragment_publish_iv;
    private LinearLayout appoint_fragment_filter_layout;
    private ImageView appoint_fragment_filter_iv;

    private Dialog sexDialog;

    private Dialog loadingDiaog;

    private Dialog limitdialog;
    private boolean isPostLimit = false;

    private int loginUserSex;

    private int appointType;
    private int sexType;
    private int sortType;
    private String firstArea = "";
    private String secondArea = "";

    DiscoverBubblingFragment discoverBubblingFragment;

    public DiscoverListFragment() {

    }

    private int tab_Id = 0;

    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.discover_fragment_layout, container, false);

        initView(convertView);
        RxBus.get().register(this);

        userId = LoginUser.getInstance().getUserId();

        loginUserSex = DbHelperUtils.getDbUserSex(userId);

        int peopleFilterType = DbHelperUtils.getPeopleFilter(userId);

        setPeopleFilterView(peopleFilterType);

        return convertView;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        RxBus.get().unregister(this);

    }

    /**
     * 初始化
     */
    private void initView(View view) {

        tab_Id = SharedprefUtil.getInt(getActivity(), YpSettings.DISCOVER_TAB_ID, 0);


        appoint_fragment_filter_layout = (LinearLayout) view.findViewById(R.id.appoint_fragment_filter_layout);
        appoint_fragment_filter_layout.setOnClickListener(this);
        appoint_fragment_filter_iv = (ImageView) view.findViewById(R.id.appoint_fragment_filter_iv);

        appoint_fragment_publish_iv = (LinearLayout) view.findViewById(R.id.appoint_fragment_publish_iv);
        appoint_fragment_publish_iv.setOnClickListener(this);


        mViewPager = (ViewPager) view.findViewById(R.id.discover_viewPager);
        discoverBubblingFragment = new DiscoverBubblingFragment();
        tabFragmentIndicator = (TabFragmentIndicator) view.findViewById(R.id.discover_tabFragmentIndicator);
        tabFragmentIndicator.addFragment(0, DiscoverPeopleFragment.class);
        tabFragmentIndicator.addFragment(1, AppointmentFragment.class);
        tabFragmentIndicator.addFragment(2, discoverBubblingFragment.getClass());


        tabFragmentIndicator.setTabContainerView(R.layout.tab_indicator_layout);

        tabFragmentIndicator.setTabSliderView(R.layout.tab_slider);
        tabFragmentIndicator.setOnDiscoverTabSelecterdListener(this);
        tabFragmentIndicator.setViewPager(mViewPager, getChildFragmentManager());

        discover_fragment_filter_tv = (TextView) view.findViewById(R.id.discover_fragment_filter_tv);
        discover_fragment_filter_tv.setOnClickListener(this);
        discover_fragment_bubbling_tv = (ImageView) view.findViewById(R.id.discover_fragment_bubbling_tv);
        discover_fragment_bubbling_tv.setOnClickListener(this);
        discover_fragment_bubbling_tv.setVisibility(View.INVISIBLE);

        mViewPager.setCurrentItem(tab_Id);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discover_fragment_filter_tv:// 筛选
                ViewsUtils.preventViewMultipleClick(v, 1000);

                sexDialog = DialogUtil.createSexDialog(getActivity(), "筛选", "全部", "只看男", "只看女", new BackCallSex() {
                    @Override
                    public void onAllPeopleLayout(View view, Object... obj) {
                        //全部

                        if (!getActivity().isFinishing()) {
                            sexDialog.dismiss();
                        }
                        setPeopleFilterView(0);
                        DbHelperUtils.savePeopleFilter(0, userId);

                        RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(1, 0));

                    }

                    @Override
                    public void onMenLayout(View view, Object... obj) {
                        //只看男
                        if (!getActivity().isFinishing()) {
                            sexDialog.dismiss();
                        }
                        setPeopleFilterView(1);

                        DbHelperUtils.savePeopleFilter(1, userId);

                        RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(1, 1));
                    }


                    @Override
                    public void onWomenLayout(View view, Object... obj) {


                        //只看女
                        if (!getActivity().isFinishing()) {
                            sexDialog.dismiss();
                        }
                        setPeopleFilterView(2);

                        DbHelperUtils.savePeopleFilter(2, userId);

                        RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(1, 2));

                    }
                });
                if (!getActivity().isFinishing()) {
                    sexDialog.show();
                }

                break;
            case R.id.discover_fragment_bubbling_tv:// 冒个泡
                ViewsUtils.preventViewMultipleClick(v, 1000);

                if (!isPostLimit) {
                    isPostLimit = true;
                    loadingDiaog = DialogUtil.LoadingDialog(getActivity(), null);
                    if (!getActivity().isFinishing()) {
                        loadingDiaog.show();
                    }
                    getBubbleLimit();
                }

                break;

            case R.id.appoint_fragment_publish_iv:// 发布
                isCan_publish_dating();
                break;


            case R.id.appoint_fragment_filter_layout:// 筛选
                // //设置layout在PopupWindow中显示的位置
                Bundle bd = new Bundle();
                bd.putInt("sortType", sortType);
                bd.putInt("sexType", sexType);
                bd.putString("firstArea", firstArea);
                bd.putString("secondArea", secondArea);
                bd.putInt("appointType", appointType);
                ActivityUtil.jump(getActivity(), AppointmentFilterActivity.class, bd, 0, 100);
                break;

            default:
                break;
        }

    }


    private void setPeopleFilterView(int sex) {
        switch (sex) {
            case 0:
                discover_fragment_filter_tv.setText("(全部)");
                break;
            case 1:
                discover_fragment_filter_tv.setText("(男)");
                break;
            case 2:
                discover_fragment_filter_tv.setText("(女)");
                break;
            case 3:
                discover_fragment_filter_tv.setText("(优质男)");
                break;

            default:
                break;
        }
    }


    @Override
    public void onDiscoverTabSelected(int tabId) {
        tab_Id = tabId;
        SharedprefUtil.saveInt(getActivity(), YpSettings.DISCOVER_TAB_ID, tab_Id);
        switch (tabId) {
            case 0:
                discover_fragment_filter_tv.setVisibility(View.VISIBLE);
                discover_fragment_bubbling_tv.setVisibility(View.GONE);
                appoint_fragment_publish_iv.setVisibility(View.GONE);
                appoint_fragment_filter_layout.setVisibility(View.GONE);
                RxBus.get().post("OnNearTabEvent", new OnNearTabEvent(1));
                break;

            case 1:
                discover_fragment_filter_tv.setVisibility(View.GONE);
                discover_fragment_bubbling_tv.setVisibility(View.GONE);
                appoint_fragment_publish_iv.setVisibility(View.VISIBLE);
                appoint_fragment_filter_layout.setVisibility(View.VISIBLE);
                RxBus.get().post("OnNearTabEvent", new OnNearTabEvent(2));
                getAppointFilter();
                break;

            case 2:
                discover_fragment_filter_tv.setVisibility(View.GONE);
                discover_fragment_bubbling_tv.setVisibility(View.VISIBLE);
                appoint_fragment_publish_iv.setVisibility(View.GONE);
                appoint_fragment_filter_layout.setVisibility(View.GONE);

                RxBus.get().post("OnNearTabEvent", new OnNearTabEvent(3));

                break;

            default:
                break;
        }

        if (tab_Id == 0) {

            RxBus.get().post("OnTopEvent", new OnTopEvent(0));

        } else if (tab_Id == 2) {
            RxBus.get().post("OnTopEvent", new OnTopEvent(2));
        }

    }


    private void getBubbleLimit() {
        BubbleLimitService bubbleLimitService = new BubbleLimitService(getActivity());
        bubbleLimitService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();
                isPostLimit = false;
                BubbleLimitRespBean bubbleLimitRespBean = (BubbleLimitRespBean) respBean;
                ChatAttamptRespDto dto = bubbleLimitRespBean.getResp();
                if (dto != null) {
                    if (dto.isSuccess() == true) {
                        SharedprefUtil.saveBoolean(getActivity(), YpSettings.BUBBLING_ADDRESS_STR, false);
                        Bundle bundle = new Bundle();
                        SharedprefUtil.save(getActivity(), YpSettings.BUBBLING_FROM_TAG_KEY, YpSettings.BUBBLING_FROM_TAG_DISCOVER);
                        ActivityUtil.jump(getActivity(), BubblingAddressActivity.class, bundle, 0, 200);
                    } else {
                        limitdialog = DialogUtil.createHintOperateDialog(getActivity(), "", dto.getMessage(), "查看帮助", "确定", backCallListener);
                        if (!getActivity().isFinishing()) {
                            limitdialog.show();
                        }
                    }
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();
                isPostLimit = false;
                // 没有网络的场合，去提示页
                DialogUtil.showDisCoverNetToast(getActivity());
            }
        });
        bubbleLimitService.enqueue();
    }


    private BackCallListener backCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!getActivity().isFinishing()) {
                limitdialog.dismiss();
            }

        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!getActivity().isFinishing()) {
                limitdialog.dismiss();
            }
            //跳转到web 查看帮助

            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Standard/AvatarAudit");
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "头像审核规范");
            bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

            ActivityUtil.jump(getActivity(), SimpleWebViewActivity.class, bundle, 0, 100);

        }
    };


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OnNearPeopleFilterEvent")

            }
    )
    public void onNearPeopleFilterEvent(OnNearPeopleFilterEvent event) {

        int filterProcess = event.getFilterProcess();
        if (filterProcess == 2) {
            int peopleFilterType = event.getPeopleFilterType();
            setPeopleFilterView(peopleFilterType);
            DbHelperUtils.savePeopleFilter(peopleFilterType, userId);

        }

    }

    private Dialog canNotPublishDatingDialog, canMsgNotPublishDatingDialog;

    /**
     * 判断是否满足约会发布条件
     */
    private void isCan_publish_dating() {

        loadingDiaog = DialogUtil.LoadingDialog(getActivity(), null);

        if (!(getActivity().isFinishing())) {
            loadingDiaog.show();
        }


        DatingRequirmentService datingRequirmentService = new DatingRequirmentService(getActivity());


        datingRequirmentService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();
                DatingRequirmentRespBean datingRequirmentRespBean = (DatingRequirmentRespBean) respBean;
                DatingRequirementData datingRequirementData = datingRequirmentRespBean.getResp();

                String limitMsg = datingRequirementData.getLimitMsg();

                if (!TextUtils.isEmpty(limitMsg)) {


                    canMsgNotPublishDatingDialog = DialogUtil.createHintOperateDialog(getActivity(), "", limitMsg, "", "确定", new BackCallListener() {

                        @Override
                        public void onEnsure(View view, Object... obj) {
                            canMsgNotPublishDatingDialog.dismiss();
                        }

                        @Override
                        public void onCancel(View view, Object... obj) {
                            canMsgNotPublishDatingDialog.dismiss();
                        }
                    });

                    canMsgNotPublishDatingDialog.show();


                    return;

                }


                List<DatingRequirment> requirements = datingRequirementData.getRequirements();


                boolean haveNotIsReady = false;

                if (null != requirements && requirements.size() > 0) {

                    for (int i = 0; i < requirements.size(); i++) {

                        if (requirements.get(i).isReady()) {
                            haveNotIsReady = true;
                            break;
                        }


                    }


                }

                if (haveNotIsReady) {
                    canNotPublishDatingDialog = DialogUtil.createNotCanPublishDatingHintDialog(getActivity(), requirements, "前往查看", new BackCallListener() {
                        @Override
                        public void onEnsure(View view, Object... obj) {
                            if (!getActivity().isFinishing()) {
                                canNotPublishDatingDialog.dismiss();
                            }
                            Bundle bundle = new Bundle();
                            bundle.putInt(YpSettings.USERID, userId);
                            ActivityUtil.jump(getActivity(), UserInfoActivity.class, bundle, 0, 100);
                        }

                        @Override
                        public void onCancel(View view, Object... obj) {
                            if (!getActivity().isFinishing()) {
                                canNotPublishDatingDialog.dismiss();
                            }
                        }
                    });
                    canNotPublishDatingDialog.show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(YpSettings.FROM_PAGE, "AppointmentFragment");
                    ActivityUtil.jump(getActivity(), AppointPublishTypeSelectActivity.class, bundle, 0, 100);

                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(getActivity());
                } else {
                    DialogUtil.showDisCoverNetToast(getActivity(), msg);
                }
            }
        });

        datingRequirmentService.enqueue();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("DatingsFilterEvent")

            }
    )
    public void datingsFilterEvent(DatingsFilterEvent event) {

        appointType = event.getAppointtype();
        sexType = event.getSextype();
        sortType = event.getSorttype();

        firstArea = event.getFirstArea_str();
        secondArea = event.getSecondArea_str();


        if (sexType == 0 && sortType == 0 && appointType == Constant.APPOINT_TYPE_NO_LIMIT && CheckUtil.isEmpty(firstArea) && CheckUtil.isEmpty(secondArea)) {
            //更换筛选的icon
            appoint_fragment_filter_iv.setBackgroundResource(R.drawable.filter_icon);
        } else {
            //更换筛选的icon
            appoint_fragment_filter_iv.setBackgroundResource(R.drawable.filter_icon_selected);

        }
    }

    private void getAppointFilter() {

        AppointFilterDto dto = DbHelperUtils.getAppointFilter(userId);
        if (dto != null) {
            appointType = dto.getDatingType();
            sexType = dto.getSexType();
            sortType = dto.getSortType();
            firstArea = dto.getFirstArea();
            secondArea = dto.getSecondArea();

        } else {
            appointType = 0;

            if (loginUserSex == 1) {
                sexType = 2;
            } else {
                sexType = 1;
            }

            sortType = 0;

            LocInfo myLoc = Loc.getLoc();
            if (myLoc != null && !CheckUtil.isEmpty(myLoc.getCity())) {
                firstArea = myLoc.getProvince();
                secondArea = myLoc.getCity();
            }

        }

        if (sexType != loginUserSex && sortType == 0 && appointType == Constant.APPOINT_TYPE_NO_LIMIT && !CheckUtil.isEmpty(firstArea) && !CheckUtil.isEmpty(secondArea)) {
            //更换筛选的icon
            appoint_fragment_filter_iv.setBackgroundResource(R.drawable.filter_icon);
        } else {
            //更换筛选的icon
            appoint_fragment_filter_iv.setBackgroundResource(R.drawable.filter_icon_selected);
        }

    }

}
