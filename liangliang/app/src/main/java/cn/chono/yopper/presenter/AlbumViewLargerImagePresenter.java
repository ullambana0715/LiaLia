package cn.chono.yopper.presenter;

import android.app.Activity;
import android.view.View;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserPhoto;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SchedulersCompat;
import rx.Subscription;

/**
 * Created by cc on 16/8/4.
 */
public class AlbumViewLargerImagePresenter extends BasePresenter<AlbumViewLargerImageContract.View> implements AlbumViewLargerImageContract.Presenter {

    @Override
    public void detachView() {
        super.detachView();

        RxBus.get().unregister(this);
    }

    public AlbumViewLargerImagePresenter(Activity activity, AlbumViewLargerImageContract.View view) {
        super(activity, view);

        RxBus.get().register(this);

        loginUserId = LoginUser.getInstance().getUserId();
    }

    int loginUserId;

    List<UserPhoto> list;

    int position;

    int userId;


    @Override
    public void initDataAndLoadData(Object o, int position, int userId) {

        list = (List<UserPhoto>) o;

        this.position = position;

        this.userId = userId;


        mView.setAdapterData(list, this.position, userId, loginUserId);
    }




    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("AlbumOnPhotoTapListenerEvent")
            }

    )

    public void AlbumOnPhotoTapListenerEvent(CommonEvent commonEvent) {

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


                    @Tag("AlbumDelectOnClickListenerEvent")
            }

    )


    public synchronized void AlbumDelectOnClickListenerEvent(CommonItemEvent commonItemEvent) {


        mView.showCreateHintOperateDialog("", "是否删除？", "取消", "确认", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {
                mView.dismissCreateHintOperateDialog();
            }

            @Override
            public void onEnsure(View view, Object... obj) {
                mView.dismissCreateHintOperateDialog();


                list = (List<UserPhoto>) commonItemEvent.event;

                int position = (int) commonItemEvent.position;


                List<UserPhoto> delectList = new ArrayList<UserPhoto>();

                delectList.addAll(list);

                delectList.remove(position);


                List<String> listStr = new ArrayList<String>();

                if (delectList == null || delectList.size() == 0) {
                    listStr.add("");
                }


                for (int i = 0; i < delectList.size(); i++) {

                    listStr.add(delectList.get(i).getImageUrl());


                }

                Logger.e("listStr=" + listStr.toString());


                Subscription subscription = HttpFactory.getHttpApi().putAlbum(loginUserId, 0, listStr)

                        .compose(SchedulersCompat.applyIoSchedulers())

                        .compose(RxResultHelper.handleResult())

                        .subscribe(b -> {

                            if (b) {

                                //删除成功 除通知给用户详情，刷新本地数据

                                CommonItemEvent detele = new CommonItemEvent();

                                detele.position = position;

                                RxBus.get().post("DeteleAlbumEvent", detele);


                                list = new ArrayList<UserPhoto>();

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


                    @Tag("AlbumOnClickListenerEvent")
            }

    )

    public void AlbumOnClickListenerEvent(CommonItemEvent commonEvent) {

        UserPhoto userPhoto = (UserPhoto) commonEvent.event;

        int position= (int) commonEvent.position;

        Logger.e(userPhoto.getPraiseStatus() + "");

        boolean isPareis;

        if (userPhoto.getPraiseStatus() == 1) {//这里的逻辑跟后台接口定义的相反。因为在处理点击事件时，已经把状态做了本地处理

            isPareis = false;//这里是要点赞
        } else {

            isPareis = true; //这里是要取消点赞
        }


        CommonItemEvent praise = new CommonItemEvent();

        praise.position = position;

        praise.event=userPhoto.getPraiseStatus();

        praise.type=userPhoto.getPraiseCount();

        RxBus.get().post("PraiseAlbumEvent", praise);


        String url = userPhoto.getImageUrl();


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
