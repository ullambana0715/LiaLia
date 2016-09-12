package cn.chono.yopper.presenter;

import android.app.Activity;
import android.view.View;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.PrivacyAlbum;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SchedulersCompat;
import rx.Subscription;

/**
 * Created by cc on 16/8/4.
 */
public class PrivacyAlbumViewLargerImagePresenter extends BasePresenter<PrivacyAlbumViewLargerImageContract.View> implements PrivacyAlbumViewLargerImageContract.Presenter {

    @Override
    public void detachView() {
        super.detachView();
        RxBus.get().unregister(this);
    }

    public PrivacyAlbumViewLargerImagePresenter(Activity activity, PrivacyAlbumViewLargerImageContract.View view) {
        super(activity, view);

        RxBus.get().register(this);

        loginUserId = LoginUser.getInstance().getUserId();
    }

    int loginUserId;

    List<PrivacyAlbum> list;

    int position;

    int userId;


    @Override
    public void initDataAndLoadData(Object o, int position, int userId) {

        list = (List<PrivacyAlbum>) o;

        this.position = position;

        this.userId = userId;


        mView.setAdapterData(list, position, userId, loginUserId);
    }



    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("PrivacyAlbumOnPhotoTapListenerEvent")
            }

    )

    public void PrivacyAlbumOnPhotoTapListenerEvent(CommonEvent commonEvent) {

        mActivity.finish();


    }


    /**
     * 删除
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("PrivacyAlbumDelectOnClickListenerEvent")
            }

    )


    public void PrivacyAlbumDelectOnClickListenerEvent(CommonItemEvent commonItemEvent) {


        mView.showCreateHintOperateDialog("", "是否删除？", "取消", "确认", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {
                mView.dismissCreateHintOperateDialog();
            }

            @Override
            public void onEnsure(View view, Object... obj) {
                mView.dismissCreateHintOperateDialog();


                list = (List<PrivacyAlbum>) commonItemEvent.event;

                int position = (int) commonItemEvent.position;


                List<PrivacyAlbum> delectList = new ArrayList<PrivacyAlbum>();

                delectList.addAll(list);

                delectList.remove(position);


                List<String> listStr = new ArrayList<String>();


                if (delectList == null || delectList.size() == 0) {
                    listStr.add("");
                }


                for (int i = 0; i < delectList.size(); i++) {

                    listStr.add(delectList.get(i).getImageUrl());


                }


                Subscription subscription = HttpFactory.getHttpApi().putAlbum(loginUserId, 1, listStr)

                        .compose(SchedulersCompat.applyIoSchedulers())

                        .compose(RxResultHelper.handleResult())

                        .subscribe(b -> {

                            if (b) {

                                //删除成功 除通知给用户详情，刷新本地数据

                                CommonItemEvent detele = new CommonItemEvent();

                                detele.position = position;

                                RxBus.get().post("DetelePrivacyAlbumEvent", detele);

                                list = new ArrayList<PrivacyAlbum>();

                                list.addAll(delectList);

                                if (list == null || list.size() == 0) {

                                    mActivity.finish();

                                    return;
                                }


                                mView.setAdapterData(list, position, userId, loginUserId);


                            }


                        }, throwable -> {

                            DialogUtil.showDisCoverNetToast(mActivity, "删除失败");


                        });


                addSubscrebe(subscription);


            }
        });


    }

    /**
     * 点赞
     *
     * @param commonEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("PrivacyAlbumOnClickListenerEvent")
            }

    )

    public void PrivacyAlbumOnClickListenerEvent(CommonItemEvent commonEvent) {

        PrivacyAlbum privacyAlbum = (PrivacyAlbum) commonEvent.event;

        int position= (int) commonEvent.position;

        boolean isPareis;

        if (privacyAlbum.getPraiseStatus() == 1) {//这里的逻辑跟后台接口定义的相反。因为在处理点击事件时，已经把状态做了本地处理

            isPareis = false;//这里是要点赞
        } else {

            isPareis = true; //这里是要取消点赞
        }


        CommonItemEvent praise = new CommonItemEvent();

        praise.position = position;

        praise.event=privacyAlbum.getPraiseStatus();

        praise.type=privacyAlbum.getPraiseCount();

        RxBus.get().post("PraisePrivacyAlbumEvent", praise);



        String url = privacyAlbum.getImageUrl();

        Subscription subscription = mHttpApi.putPraise(url, isPareis)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(b -> {


                }, throwable -> {


//                    ApiResp apiResp = ErrorHanding.handle(throwable);
//
//                    if (apiResp == null) {
//
//
//                        mView.showDisCoverNetToast(null);
//                    } else {
//
//                        mView.showDisCoverNetToast(apiResp.getMsg());
//
//                    }


                });

        addSubscrebe(subscription);


    }


}
