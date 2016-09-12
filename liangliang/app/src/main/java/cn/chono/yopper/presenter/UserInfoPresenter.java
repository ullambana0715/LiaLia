package cn.chono.yopper.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.duanqu.qupai.utils.AppGlobalSetting;
import com.google.common.io.Files;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;
import com.tencent.TIMConversationType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.chono.yopper.R;
import cn.chono.yopper.activity.register.HeadImgCompileActivity;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.App;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.data.AttributeDto;
import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.UserPhoto;
import cn.chono.yopper.data.ZoomViewerDto;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.entity.PrivacyAlbum;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.im.imObserver.ChatObserver;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.TextMessage;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.CropDirectionUtil;
import cn.chono.yopper.utils.DatingUtils;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.InfoTransformUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.MultipartUtil;
import cn.chono.yopper.utils.RePortCallListener;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.StringUtils;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.UserInfoUtils;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.utils.video.VideoContant;
import cn.chono.yopper.utils.video.VideoFileUtils;
import cn.chono.yopper.utils.video.VideoRecordResult;
import cn.chono.yopper.utils.video.VideoRequestCode;
import cn.chono.yopper.view.cropper.CopperData;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.RequestBody;
import rx.Subscription;

/**
 * Created by cc on 16/7/21.
 */
public class UserInfoPresenter extends BasePresenter<UserInfoContract.View> implements UserInfoContract.Presenter {


    boolean isChanage = false;//用来标记好友关系更改是否有触发

    @Override
    public void detachView() {
        super.detachView();
        RxBus.get().unregister(this);
    }

    public UserInfoPresenter(Activity activity, UserInfoContract.View view) {
        super(activity, view);

        RxBus.get().register(this);


    }


    int loginUserId;

    double latitude = 0;

    double longtitude = 0;

    UserDto userdto;


    CopperData copperData;

    Integer imageType;

    final int image_photo = 0;

    final int image_private_photo = 1;

    List<String> uploadingList = new ArrayList<>();


    ZoomViewerDto sq;

    int InviteType; //邀请类型， 1 邀请上传相片 2 邀请上传视频 3 邀请公开视频

    final int InviteTypeAlbum = 1;

    final int InviteTypeVideo = 2;

    ChatObserver mChatObserver;


    int userID;

    String fromPage;

    @Override
    public void initDataAndLoadData() {


        Bundle bundle = mActivity.getIntent().getExtras();

        if (bundle != null && bundle.containsKey(YpSettings.USERID)) {

            userID = bundle.getInt(YpSettings.USERID);

            fromPage = bundle.getString(YpSettings.FROM_PAGE);
        }


        loginUserId = LoginUser.getInstance().getUserId();

        LocInfo myLoc = Loc.getLoc();

        if (myLoc != null && myLoc.getLoc() != null) {

            latitude = myLoc.getLoc().getLatitude();

            longtitude = myLoc.getLoc().getLongitude();
        }


        if (userID == loginUserId) {//当前用户是自己

            mView.user_info_tv_dating_inviteGone();

            mView.user_info_bottom_layoutGone();

            mView.user_info_ll_optionGone();

            mView.user_info_tv_iconVisible();

            mView.user_info_tv_modifyGone();

            mView.user_info_tv_modifysVisible();

            mView.user_info_tv_distanceGone();


        } else {//当前用户不是自己的

            mView.user_info_tv_dating_inviteVisible();

            mView.user_info_bottom_layoutVisible();

            mView.user_info_ll_optionVisible();

            mView.user_info_tv_iconGone();

            mView.user_info_tv_modifyGone();

            mView.user_info_tv_modifysGone();

            mView.user_info_tv_distanceVisible();


        }

        getDbUserData();

        getHttpUserData();


    }

    @Override
    public void onResume() {

    }

    /**
     * 修改个人资料页回来
     *
     * @param commonItemEvent
     */
    @Subscribe(


            thread = EventThread.MAIN_THREAD,

            tags = {

                    @Tag("RefreshDataEvant")

            }
    )
    public void RefreshDataEvant(CommonItemEvent commonItemEvent) {

        userdto = (UserDto) commonItemEvent.event;

        setDateToView(userdto);

    }

    /**
     * 邀约信息删除与修改
     * <p>
     * 上传形象视频
     *
     * @param commonItemEvent
     */
    @Subscribe(


            thread = EventThread.MAIN_THREAD,

            tags = {

                    @Tag("DatingDetailDelectModify"),
                    @Tag("UserInfoActivityGeneralVideosEvent")

            }
    )
    public void DatingDetailDelectModify(CommonItemEvent commonItemEvent) {

        getHttpUserData();

    }


    /**
     * 邀约里已经发启了聊天时，
     *
     * @param commonItemEvent
     */
    @Subscribe(


            thread = EventThread.MAIN_THREAD,

            tags = {

                    @Tag("DatingDetailChat")

            }
    )
    public void DatingDetailChat(CommonItemEvent commonItemEvent) {

        setDatingViewData(userdto);

    }

    /**
     * 本地数据
     */
    @Override
    public void getDbUserData() {

        UserInfo userInfo = DbHelperUtils.getUserInfo(userID);
        if (userInfo != null) {

            userdto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
            if (userdto != null) {
                setDateToView(userdto);
            }
        }

    }

    /**
     * 网络数据
     */

    @Override
    public void getHttpUserData() {


        LatLng pt = new LatLng(latitude, longtitude);

        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        Double lat = null;
        Double log = null;

        if (latitude != 0 && longtitude != 0 && latitude != longtitude) {

            lat = pt.latitude;
            log = pt.longitude;

        }


        Subscription subscription = mHttpApi
                .getUserInfo(userID, true, true, true, false, true, true, true, true, true, true, true, true, true, lat, log)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(userdto -> {

                    this.userdto = userdto;

                    Logger.e(userdto.toString());

                    String jsonstr = JsonUtils.toJson(userdto);

                    // 保存数据
                    DbHelperUtils.saveUserInfo(userID, jsonstr);
                    // 保存数据
                    DbHelperUtils.saveBaseUser(userID, userdto);

                    setDateToView(userdto);


                }, throwable -> {

                    ApiResp apiResp = ErrorHanding.handle(throwable);


                    if (apiResp != null && (TextUtils.equals("404", apiResp.getStatus()) || TextUtils.equals("410", apiResp.getStatus()))) {


                        mView.showDisCoverNetToast("该用户不存在");

                        return;

                    }


                    if (apiResp != null && TextUtils.equals("Unauthorized", apiResp.getErrCode())) {

                        //用户被系统疯禁了

                        mView.showDisCoverNetToast(apiResp.getMsg());

                        mView.onFinish();


//                        mView.showCreateHintOperateDialog("", apiResp.getMsg(), "", "确定", new BackCallListener() {
//                            @Override
//                            public void onCancel(View view, Object... obj) {
//
//                                mView.dismissCreateHintOperateDialog();
//                            }
//
//                            @Override
//                            public void onEnsure(View view, Object... obj) {
//
//                                mView.dismissCreateHintOperateDialog();
//
//                                mView.onFinish();
//                            }
//                        });


                    } else {


                        if (apiResp == null) {


                            mView.showDisCoverNetToast(null);
                        } else {

                            mView.showDisCoverNetToast(apiResp.getMsg());

                        }

                    }


                });


        addSubscrebe(subscription);


    }

    /**
     * 拉黑
     */
    @Override
    public void doBlockRequest() {


        mView.showLoadingDialog();

        Subscription subscription = mHttpApi.postBlockRequest(loginUserId, userID, true)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(blockRequest -> {

                    mView.dismissLoadingDialog();

                    mView.showTimerCreateSuccessHintDialog("拉黑成功!");

                    Logger.e("userid=" + loginUserId + "---jid=" + userID);

                    ChatUtils.deleteBlockUserMessageList(loginUserId + "", userID + "");

                    successtimer = new SuccessTimer(2000, 1000);

                    successtimer.start();

                    isChanage = true;

                    //拉黑成功后，从新刷新一下个人资料接口。用于刷新 喜欢的状态

                    if (userdto.getUserLikeState() != 0) {

                        getHttpUserData();
                    }


                }, throwable -> {

                    mView.dismissLoadingDialog();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);

                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }


                });

        addSubscrebe(subscription);

    }

    /**
     * 举报
     *
     * @param msg
     */
    @Override
    public void doReport(String msg) {


        mView.showLoadingDialog();


        Map<String, Object> map = new HashMap<>();

        map.put("type", 2);

        map.put("Id", userID);

        map.put("Content", msg);


        Subscription subscription = mHttpApi.postReport(map)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(report -> {

                    mView.dismissLoadingDialog();

                    mView.showTimerCreateSuccessHintDialog("举报成功!");

                    successtimer = new SuccessTimer(2000, 1000);

                    successtimer.start();

                    isChanage = true;

                }, throwable -> {

                    mView.dismissLoadingDialog();

                    ApiResp apiResp = ErrorHanding.handle(throwable);


                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }


                });

        addSubscrebe(subscription);

    }

    /**
     * 更换头像
     */
    @Override
    public void createIconPhoto() {

        Logger.e("更换头像");

        copperData = new CopperData();

        mView.showCreatePhotoDialog(view -> {

            mView.dismissCreatePhotoDialog();

            MultiImageSelector.create()
                    .showCamera(true) // 是否显示相机. 默认为显示
                    .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                    .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                    .start(mActivity, YpSettings.ICON_REQUEST_IMAGE);


        });


    }

    @Override
    public void onActivityReenter(int requestCode, int resultCode, Intent data) {

        Logger.e(requestCode + "");
        Logger.e(resultCode + "");


        if (requestCode == YpSettings.ICON_REQUEST_IMAGE) {//头像

            if (resultCode == Activity.RESULT_OK) {

                List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);


                for (String p : mSelectPath) {


                    photoHeadDeal(p);

                }

            }
        } else if (requestCode == YpSettings.PHOTO_REQUEST_IMAGE) {//相册

            if (resultCode == Activity.RESULT_OK) {

                List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                setUriBitmap(mSelectPath);


            }


        } else if (requestCode == YpSettings.USER_COMPILE) {//剪切后

            if (resultCode == Activity.RESULT_OK) {

                copperData = (CopperData) data.getExtras().getSerializable("copperData");

                if (copperData != null) {

                    String albumImg = copperData.getCroppedImage();

                    Logger.e("albumImg" + albumImg);

                    douploadingUserHeadImg(false, albumImg);//上传头像

                }


            }

        } else if (requestCode == VideoRequestCode.RECORDE_SHOW) {

            if (resultCode == Activity.RESULT_OK) {


                VideoRecordResult vrr = new VideoRecordResult(data);

                String videoPath = vrr.getPath();

                String[] image = vrr.getThumbnail();

                vrr.getDuration();


                try {


                    //拷贝move操作
                    Files.move(new File(videoPath), new File(VideoContant.VIDEOPATH));

                    String[] images = new String[10];

                    for (int i = 0; i < image.length; i++) {

                        String imageUrl = VideoContant.THUMBPATH + System.currentTimeMillis() + ".png";

                        Files.move(new File(image[i]), new File(imageUrl));

                        images[i] = imageUrl;
                    }


                    //清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作
                    if (null != App.getInstance().qupaiService) {

                        App.getInstance().qupaiService.deleteDraft(mActivity, data);

                    }

                    //跳转到编辑界面
                    Bundle be = new Bundle();

                    be.putString(YpSettings.VIDEO_PATH_NAME, VideoFileUtils.newOutgoingFilePath());

                    be.putStringArray(YpSettings.VIDEO_PATH_NAME_IMG, images);

                    be.putString(YpSettings.VIDEO_PATH_TYPE, YpSettings.VIDEO_PATH_TYPE_UserInfoPresenter);


                    mView.VideoCoverActivity(be);


                } catch (IOException e) {
                    e.printStackTrace();

                    Logger.e("尼玛来了了了了");
                }
            }
        }


    }

    @Override
    public void OptionClick() {

        mView.showBlockDialog("操作", "举报", "拉黑", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {
                ViewsUtils.preventViewMultipleClick(view, 3000);

                mView.dismissBlockDialog();

                mView.showRePortDialog("举报原因", "诽谤谩骂", "色情骚扰", "垃圾广告", "欺诈（酒托、饭托等）", "违法（涉毒、暴恐等）", new RePortCallListener() {
                    @Override
                    public void onOne(View view, Object... obj) {

                        mView.dismissRePortDialog();

                        doReport("诽谤谩骂");

                    }

                    @Override
                    public void onTwo(View view, Object... obj) {


                        mView.dismissRePortDialog();

                        doReport("色情骚扰");

                    }

                    @Override
                    public void onThree(View view, Object... obj) {


                        mView.dismissRePortDialog();

                        doReport("垃圾广告");

                    }

                    @Override
                    public void onFour(View view, Object... obj) {


                        mView.dismissRePortDialog();

                        doReport("欺诈（酒托、饭托等）");

                    }

                    @Override
                    public void onFive(View view, Object... obj) {


                        mView.dismissRePortDialog();

                        doReport("违法（涉毒、暴恐等）");

                    }
                });


            }

            @Override
            public void onEnsure(View view, Object... obj) {
                ViewsUtils.preventViewMultipleClick(view, 3000);

                mView.dismissBlockDialog();

                mView.showCreateHintOperateDialog("提示", "拉黑后将不会收到对方发来的消息,可在“设置->黑名单”中解除,是否确认?", "取消", "确认", new BackCallListener() {
                    @Override
                    public void onCancel(View view, Object... obj) {

                        mView.dismissCreateHintOperateDialog();


                    }

                    @Override
                    public void onEnsure(View view, Object... obj) {

                        mView.dismissCreateHintOperateDialog();

                        doBlockRequest();
                    }
                });
            }
        });
    }


    /**
     * 头像没有通过审核
     */
    @Override
    public void IconIintClick() {

        //跳转到web 查看帮助

        userIconPassAuditWebHint();
    }

    /**
     * 手机认证
     */
    @Override
    public void IdentifyPhoneClick() {

        if (userID == loginUserId) {
            if (((userdto.getProfile().getStatus() >> 2) & 1) == 0) {
                //去认证手机
                Bundle buns = new Bundle();
                buns.putInt(YpSettings.FROM_PAGE, 1);
                buns.putInt(YpSettings.USERID, userID);

                mView.SettingPhoneActivity(buns);


            } else {
                if (!CheckUtil.isEmpty(userdto.getProfile().getUid())) {
                    //更换手机号码页面
                    Bundle buns = new Bundle();
                    buns.putInt(YpSettings.FROM_PAGE, 2);
                    buns.putInt(YpSettings.USERID, userID);
                    buns.putString("mobile", userdto.getProfile().getMobile());
                    mView.SettingPhoneActivity(buns);
                }

            }

        }
    }

    /**
     * 视频认证
     */
    @Override
    public void IdentifyVideoClick() {

//        if (userID != loginUserId) {
//            return;
//        }
//

        if (userdto != null && userdto.getVideoVerification() != null) {

            if (userID == loginUserId) {

                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.USERID, userID);

                mView.VideoDetailGetActivity(bundle);


            } else {

                if (userdto.getVideoVerification().getStatus() == 2) {

                    //进ta人视频详情页面
                    Bundle bundle = new Bundle();

                    bundle.putInt(YpSettings.USERID, userID);

                    mView.VideoDetailGetActivity(bundle);


                } else {
                    //邀请

                    AddPhotoVideoInvite(false, 2);


                }
            }
        }

    }

    /**
     * 冒泡
     */
    @Override
    public void BubbleClick() {

        Bundle dle = new Bundle();

        dle.putInt(YpSettings.USERID, userdto.getProfile().getId());

        mView.MyBubblingActivity(dle);
    }

    /**
     * 星运
     */
    @Override
    public void MatchLineClick() {

        Bundle bundle = new Bundle();

        if (userID != loginUserId) {

            //星座匹配
            UserDto mydto = DbHelperUtils.getDbUserInfo(loginUserId);

            int myhor = mydto.getProfile().getHoroscope();

            String myheadimg = mydto.getProfile().getHeadImg();

            int hor = userdto.getProfile().getHoroscope();

            int mysex = mydto.getProfile().getSex();

            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL,
                    "Constellation/Matching?first_horoscope=" + myhor + "&second_horoscope=" + hor + "&first_headimg=" + myheadimg + "&second_headimg=" + userdto.getProfile().getHeadImg() + "&first_sex=" + mysex);

            bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "星座匹配");

            mView.SimpleWebViewActivity(bundle);

        } else {

            //跳转到每日星运
            int hor = userdto.getProfile().getHoroscope();

            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Constellation/Luck?id=" + hor + "&userid=" + userID + "&AuthToken=" + LoginUser.getInstance().getAuthToken());

            bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "每日星运");

            mView.SimpleWebViewActivity(bundle);
        }


    }

    /**
     * 修改
     */
    @Override
    public void ModifyClick() {

        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, userID);

        mView.UserInfoEditActivity(bundle);

    }

    /**
     * 聊天
     */
    @Override
    public void ChatClick() {

        // 查看别人资料是聊天；查看自己资料是编辑
//

        joinDialog = createDatingDialog(userdto.getDatingList(), 1);

        if (!mActivity.isFinishing()) {

            joinDialog.show();

        }


    }


    /**
     * 无邀约
     */
    @Override
    public void DatingInviteClick() {

        //邀请他参加我的邀约
        invitationAppoint();
    }

    /**
     * 喜欢
     */
    @Override
    public void LikeClick() {


        int likeId = userdto.getUserLikeState();


        Boolean isLiske = null;

        if (likeId == 0) { //0表示不喜欢

            isLiske = true;

        } else if (likeId == 1) {//1表示我喜欢

            isLiske = false;


        } else if (likeId == 2) {//2表示喜欢我

            isLiske = true;

        } else if (likeId == 3) {//3表示相互喜欢

            isLiske = false;

        }


        Subscription subscription = mHttpApi.cancelLike(userID, isLiske)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(cancelIlike -> {

                    isChanage = true;

                    if (cancelIlike.getLikeResult() == 1) {


                        mView.showDisCoverNetToast((TextUtils.isEmpty(cancelIlike.getMessage()) ? cancelIlike.getMessage() : "不能喜欢黑名单中的用户"));


                        return;
                    }


                    if (cancelIlike.getLikeResult() == 2) {


                        mView.showDisCoverNetToast((TextUtils.isEmpty(cancelIlike.getMessage()) ? cancelIlike.getMessage() : "对方拒绝你的添加"));


                        return;
                    }

//                    if (cancelIlike.getLikeResult() == 1 || cancelIlike.getLikeResult() == 2) {
//
//                        mView.showCreateHintOperateDialog("", StringUtils.isEmpty(cancelIlike.getMessage()), "", "好的", new BackCallListener() {
//
//                            @Override
//                            public void onCancel(View view, Object... obj) {
//                                mView.dismissCreateHintOperateDialog();
//                            }
//
//                            @Override
//                            public void onEnsure(View view, Object... obj) {
//                                mView.dismissCreateHintOperateDialog();
//                            }
//                        });
//
//                        return;
//
//                    }


//                    if (cancelIlike.getLikeResult() == 3) {
//
//                        mView.showCreateHintOperateDialog("", StringUtils.isEmpty(cancelIlike.getMessage()), "取消", "去完善", new BackCallListener() {
//
//                            @Override
//                            public void onCancel(View view, Object... obj) {
//                                mView.dismissCreateHintOperateDialog();
//                            }
//
//                            @Override
//                            public void onEnsure(View view, Object... obj) {
//
//                                mView.dismissCreateHintOperateDialog();
//
//                                Bundle bundle = new Bundle();
//
//                                bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
//
//                                mView.UserInfoActivity(bundle);
//
//
//                            }
//                        });
//
//                        return;
//
//                    }


                    userdto.setUserLikeState(cancelIlike.getUserLikeState());


                    //喜欢

                    setLikeViewData(userdto);


                    if (cancelIlike.getUserLikeState() == 0 || cancelIlike.getUserLikeState() == 2) {

                        mView.showDisCoverNetToast((TextUtils.isEmpty(cancelIlike.getMessage()) ? cancelIlike.getMessage() : "取消关注成功"));

                        return;

                    }

                    if (cancelIlike.getUserLikeState() == 1) {

                        mView.showDisCoverNetToast((TextUtils.isEmpty(cancelIlike.getMessage()) ? cancelIlike.getMessage() : "关注成功，已将对方加入喜欢的人"));

                        return;
                    }

                    if (cancelIlike.getUserLikeState() == 3) {

                        mView.showCreateHintOperateDialog("", StringUtils.isEmpty(cancelIlike.getMessage()), "", "好的", new BackCallListener() {

                            @Override
                            public void onCancel(View view, Object... obj) {
                                mView.dismissCreateHintOperateDialog();
                            }

                            @Override
                            public void onEnsure(View view, Object... obj) {
                                mView.dismissCreateHintOperateDialog();
                            }
                        });

                        return;
                    }


                }, throwable -> {

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }


                });


        addSubscrebe(subscription);


    }


    @Override
    public void onRefreshPage() {

        Logger.e("onRefreshPage ＝＝＝" + isChanage);

        if (!TextUtils.isEmpty(fromPage)) {

            if (isChanage) {

                Logger.e("调用刷新数据＝＝＝" + fromPage);

                if (fromPage.equals(YpSettings.LIKE_ILIKE_PAGE)) {

                    RxBus.get().post("ILikeEvent", new CommonEvent());

                } else if (fromPage.equals(YpSettings.LIKE_LIKEME_PAGE)) {

                    RxBus.get().post("LikeMeEvent", new CommonEvent());

                } else if (fromPage.equals(YpSettings.LIKE_LIKEEACH_PAGE)) {

                    RxBus.get().post("LikeEachEvent", new CommonEvent());

                }

            }


        }


    }

    /**
     * 有邀约 感兴趣，先聊聊 btn   打开聊天
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("ItemUserInfoDatingInterested")
            }

    )
    public void ItemUserInfoDatingInterested(CommonItemEvent commonItemEvent) {

        String datingId = (String) commonItemEvent.event;

        int datingType = (int) commonItemEvent.type;


        boolean isExist = (boolean) commonItemEvent.position;


        if (!isExist) {

            Bundle bd = new Bundle();

            bd.putInt(YpSettings.USERID, userID);

            bd.putInt(YpSettings.DATINGS_TYPE, datingType);

            bd.putString(YpSettings.DATINGS_ID, datingId);

            mView.DatingDetailActivity(bd);

        } else {

            Bundle bd = new Bundle();

            bd.putInt(YpSettings.USERID, userID);

            bd.putString(YpSettings.DATINGS_ID, datingId);

            mView.ChatActivity(bd);


        }


    }


    /**
     * 有邀约，自己
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("ItemUserInfoDatingInterestedLogin")
            }

    )
    public void ItemUserInfoDatingInterestedLogin(CommonItemEvent commonItemEvent) {


        String datingId = (String) commonItemEvent.event;

        int datingType = (int) commonItemEvent.type;

        Bundle bd = new Bundle();

        bd.putInt(YpSettings.USERID, userID);

        bd.putInt(YpSettings.DATINGS_TYPE, datingType);

        bd.putString(YpSettings.DATINGS_ID, datingId);

        mView.DatingDetailActivity(bd);

    }


    /**
     * 无邀约，自己
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("ItemUserInfoDatingCanPublishDatingLogin")
            }

    )
    public void ItemUserInfoDatingCanPublishDatingLogin(CommonItemEvent commonItemEvent) {


        Subscription subscription = mHttpApi.getDatingRequirementData()


                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(dto -> {


                            String limitMsg = dto.getLimitMsg();

                            if (!TextUtils.isEmpty(limitMsg)) {

                                mView.showCreateHintOperateDialog("", limitMsg, "", "确定", new BackCallListener() {
                                    @Override
                                    public void onCancel(View view, Object... obj) {

                                        mView.dismissCreateHintOperateDialog();
                                    }

                                    @Override
                                    public void onEnsure(View view, Object... obj) {

                                        mView.dismissCreateHintOperateDialog();
                                    }
                                });

                                return;
                            }


                            List<DatingRequirment> requirements = dto.getRequirements();


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

                                mView.showCreateNotCanPublishDatingHintDialog(requirements, "确定", new BackCallListener() {
                                    @Override
                                    public void onCancel(View view, Object... obj) {
                                        mView.dismissCreateNotCanPublishDatingHintDialog();
                                    }

                                    @Override
                                    public void onEnsure(View view, Object... obj) {
                                        mView.dismissCreateNotCanPublishDatingHintDialog();
                                    }
                                });


                            } else {
                                Bundle bundle = new Bundle();

                                bundle.putString(YpSettings.FROM_PAGE, "UserInfoActivity");

                                mView.AppointPublishTypeSelectActivity(bundle);

                            }


                        }

                        , throwable ->

                        {


                            ApiResp apiResp = ErrorHanding.handle(throwable);

                            if (apiResp == null) {


                                mView.showDisCoverNetToast(null);
                            } else {

                                mView.showDisCoverNetToast(apiResp.getMsg());

                            }


                        }

                );

        addSubscrebe(subscription);

    }


    /**
     * 邀请他参加我的邀约
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("ItemUserInfoDatingCanPublishDating")
            }

    )
    public void ItemUserInfoDatingCanPublishDating(CommonItemEvent commonItemEvent) {

        Logger.e("-----ItemUserInfoDatingCanPublishDating---");


        invitationAppoint();

    }


    /**
     * 添加相册
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("AddPhotoEvent")
            }

    )
    public void AddPhotoEvent(CommonItemEvent commonItemEvent) {

        imageType = image_photo;

        uploadingList = new ArrayList<>();


        int albumSize;
        if (userdto.getPhotos() == null) {
            albumSize = 0;
        } else {
            albumSize = userdto.getPhotos().size();
        }

        int maxPhoto = userdto.getAlbumMax() - albumSize;

        if (maxPhoto > 9)

            maxPhoto = 9;


        createPhoto(maxPhoto);
    }


    /**
     * 邀请添加相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("InviteAddPhotoEvent")
            }

    )
    public void InviteAddPhotoEvent(CommonItemEvent commonItemEvent) {

        InviteType = InviteTypeAlbum;

        Logger.e("InviteAddPhotoEvent");

        AddPhotoVideoInvite(false, InviteType);

        Logger.e("-----InviteAddPhotoEvent");

    }


    /**
     * 点击相册item
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("ItmePhotoEvent")
            }

    )
    public void ItmePhotoEvent(CommonItemEvent commonItemEvent) {


        int position = (int) commonItemEvent.position;

        imageType = image_photo;


        if (userID != loginUserId) {

            if (UserInfoUtils.iscanLook(userdto, position)) {


                List<UserPhoto> albumList = new ArrayList<>();

                albumList = UserInfoUtils.getAlbumCanLookList(userdto.getPhotos(), userdto.getAlbumMask());


                int lookposition = UserInfoUtils.getAlbumUrlPosition(albumList, userdto.getPhotos().get(position).getImageUrl());


                if (albumList != null && albumList.size() > 0) {


                    Bundle bundle = new Bundle();

                    bundle.putSerializable("Data", (Serializable) albumList);

                    bundle.putInt("position", lookposition);

                    bundle.putInt("type", userID);

                    mView.AlbumViewLargerImageActivity(bundle);

                }


            } else {


                int status = DbHelperUtils.getDbLoginUserHeadStatus();

                if (((status >> 0) & 1) == 0) {
                    //登录用户头像审核不通过


                    mView.showCreateHintOperateDialog("", "你的头像尚未通过审核,无法查看更多对方照片", "查看帮助", "确定", new BackCallListener() {
                        @Override
                        public void onCancel(View view, Object... obj) {

                            mView.dismissCreateHintOperateDialog();

                            IconIintClick();


                        }

                        @Override
                        public void onEnsure(View view, Object... obj) {

                            mView.dismissCreateHintOperateDialog();

                        }
                    });


                } else {
                    //登录用户头像审核通过


                    mView.showCreateHintOperateDialog("", "公平起见,你需要上传更多的个人照片才能解锁查看更多对方照片", "取消", "立即上传", new BackCallListener() {
                        @Override
                        public void onCancel(View view, Object... obj) {

                            mView.dismissCreateHintOperateDialog();


                            Logger.e("dfdfdfdfd");
                        }

                        @Override
                        public void onEnsure(View view, Object... obj) {

                            mView.dismissCreateHintOperateDialog();

                            Bundle bundle = new Bundle();

                            bundle.putInt(YpSettings.USERID, loginUserId);

                            mView.UserInfoActivity(bundle);


                        }
                    });


                }


            }

        } else {//自己的


            Logger.e("Position===" + position);

            Bundle bundle = new Bundle();

            bundle.putSerializable("Data", (Serializable) userdto.getPhotos());

            bundle.putInt("position", position);

            bundle.putInt("type", userID);


            mView.AlbumViewLargerImageActivity(bundle);


            Logger.e("走了要跳大大   ");


        }


    }


    /**
     * 删除个人相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("DeteleAlbumEvent"),


            }

    )
    public void DeteleAlbumEvent(CommonItemEvent commonItemEvent) {

        int position = (int) commonItemEvent.position;

        Logger.e(userdto.getPhotos().size() + "＝ta ya");

        if (userdto.getPhotos() != null && userdto.getPhotos().size() > 0 && userdto.getPhotos().size() > position) {


            Logger.e(position + "＝tllllll");

            userdto.getPhotos().remove(position);

            setPhotoViewData(userdto);
        }


    }

    /**
     * 点赞个人相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("PraiseAlbumEvent"),


            }

    )
    public void PraiseAlbumEvent(CommonItemEvent commonItemEvent) {

        int position = (int) commonItemEvent.position;

        int praiseStatus = (int) commonItemEvent.event;

        int praiseCount = (int) commonItemEvent.type;

        if (userdto.getPhotos() != null && userdto.getPhotos().size() > 0 && userdto.getPhotos().size() > position) {

            userdto.getPhotos().get(position).setPraiseStatus(praiseStatus);
            userdto.getPhotos().get(position).setPraiseCount(praiseCount);
            setPhotoViewData(userdto);
        }


    }


    /**
     * 添加私密相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("AddPrivatePhotoEvent")
            }

    )
    public void AddPrivatePhotoEvent(CommonItemEvent commonItemEvent) {

        imageType = image_private_photo;

        uploadingList = new ArrayList<>();

        int albumSize;

        if (userdto.getPrivacyAlbum() == null) {

            albumSize = 0;

        } else {

            albumSize = userdto.getPrivacyAlbum().size();
        }

        int maxPrivatePhoto = userdto.getPrivacyAlbumMax() - albumSize;

        if (maxPrivatePhoto > 9)

            maxPrivatePhoto = 9;

        createPhoto(maxPrivatePhoto);

    }

    /**
     * 查看私密相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("ItemPrivatePhotoEvent")
            }

    )
    public void ItemPrivatePhotoEvent(CommonItemEvent commonItemEvent) {


        Logger.e("发过来了了水口子");

        int position = (int) commonItemEvent.position;


        if (userID == loginUserId) {//自己


            Bundle bundle = new Bundle();

            bundle.putSerializable("Data", (Serializable) userdto.getPrivacyAlbum());

            bundle.putInt("position", position);

            bundle.putInt("type", userID);

            mView.PrivacyAlbumViewLargerImageActivity(bundle);

            Logger.e("我自己的");

            return;

        }

        if (userdto.isUnlockPrivacyAlbum()) {


            Bundle bundle = new Bundle();

            bundle.putSerializable("Data", (Serializable) userdto.getPrivacyAlbum());

            bundle.putInt("position", position);

            bundle.putInt("type", userID);

            mView.PrivacyAlbumViewLargerImageActivity(bundle);

            Logger.e("对方的");


            return;

        }


        ItemVerificationPrivatePhotoEvent(commonItemEvent);

        Logger.e("对方没有解的");


    }

    /**
     * 解锁私密相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("ItemVerificationPrivatePhotoEvent")
            }

    )
    public void ItemVerificationPrivatePhotoEvent(CommonItemEvent commonItemEvent) {

        int position = (int) commonItemEvent.position;


        mView.showCreateHintOperateDialog("", "解锁需要10个苹果, 是否付出苹果查看（解锁成功当天内可任意查看对方私密照片）", "取消", "查看", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                mView.dismissCreateHintOperateDialog();
            }

            @Override
            public void onEnsure(View view, Object... obj) {

                mView.dismissCreateHintOperateDialog();

                mView.showLoadingDialog();

                Subscription subscription = mHttpApi.putPrivateAlbumVerification(loginUserId, userID)


                        .compose(SchedulersCompat.applyIoSchedulers())

                        .compose(RxResultHelper.handleResult())

                        .subscribe(privateAlbum -> {

                            mView.dismissLoadingDialog();

                            //苹果充足，直接查看
                            if (privateAlbum.result == 0) {


                                userdto.setUnlockPrivacyAlbum(true);


                                //私密相册

                                setPrivatePhotoViewData(userdto);


                                //冒泡

                                setBubbleViewData(userdto);


                                Bundle bundle = new Bundle();

                                bundle.putSerializable("Data", (Serializable) userdto.getPrivacyAlbum());

                                bundle.putInt("position", position);

                                bundle.putInt("type", userID);


                                mView.PrivacyAlbumViewLargerImageActivity(bundle);


                                RxBus.get().post("OnLock", userID);


                                return;
                            }

                            //苹果不足

                            mView.showCreateHintOperateDialog("", StringUtils.isEmpty(privateAlbum.msg), "取消", "购买苹果", new BackCallListener() {
                                @Override
                                public void onCancel(View view, Object... obj) {

                                    mView.dismissCreateHintOperateDialog();
                                }

                                @Override
                                public void onEnsure(View view, Object... obj) {

                                    mView.dismissCreateHintOperateDialog();

                                    Bundle appleBundle = new Bundle();

                                    appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);

                                    mView.AppleListActivity(appleBundle);


                                }
                            });


                        }, throwable -> {

                            mView.dismissLoadingDialog();

                            ApiResp apiResp = ErrorHanding.handle(throwable);

                            if (apiResp == null) {


                                mView.showDisCoverNetToast(null);
                            } else {

                                mView.showDisCoverNetToast(apiResp.getMsg());

                            }


                        });

                addSubscrebe(subscription);


            }
        });


    }


    /**
     * 删除个人相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("DetelePrivacyAlbumEvent"),


            }

    )
    public void DetelePrivacyAlbumEvent(CommonItemEvent commonItemEvent) {

        int position = (int) commonItemEvent.position;

        Logger.e(userdto.getPhotos().size() + "＝ta ya");

        if (userdto.getPrivacyAlbum() != null && userdto.getPrivacyAlbum().size() > 0 && userdto.getPrivacyAlbum().size() > position) {


            Logger.e(position + "＝tllllll");

            userdto.getPrivacyAlbum().remove(position);

            setPrivatePhotoViewData(userdto);
        }


    }


    /**
     * 点赞个人相册
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("PraisePrivacyAlbumEvent"),


            }

    )
    public void PraisePrivacyAlbumEvent(CommonItemEvent commonItemEvent) {

        int position = (int) commonItemEvent.position;

        int praiseStatus = (int) commonItemEvent.event;

        int praiseCount = (int) commonItemEvent.type;

        if (userdto.getPrivacyAlbum() != null && userdto.getPrivacyAlbum().size() > 0 && userdto.getPrivacyAlbum().size() > position) {

            userdto.getPrivacyAlbum().get(position).setPraiseStatus(praiseStatus);

            userdto.getPrivacyAlbum().get(position).setPraiseCount(praiseCount);

            setPrivatePhotoViewData(userdto);
        }


    }


    /**
     * 添加视频
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("AddItemVideoLoindingEvent")
            }

    )
    public void AddItemVideoLoindingEvent(CommonItemEvent commonItemEvent) {

        int videosSize;

        if (userdto.getGeneralVideos() == null) {

            videosSize = 0;

        } else {

            videosSize = userdto.getGeneralVideos().size();
        }


        int videoMax = userdto.getGeneralVideoMax() - videosSize;

        if (videoMax == 0) {

            return;
        }


        if (null != App.getInstance().qupaiService) {

            //引导，只显示一次，这里用SharedPreferences记录
            final AppGlobalSetting sp = new AppGlobalSetting(mView.ApplicationContext());

            Boolean isGuideShow = sp.getBooleanGlobalItem(YpSettings.PREF_VIDEO_EXIST_USER, true);


            /**
             * 建议上面的initRecord只在application里面调用一次。这里为了能够开发者直观看到改变所以可以调用多次
             */
            App.getInstance().qupaiService.showRecordPage(mActivity, YpSettings.RECORDE_SHOW, isGuideShow);

            sp.saveGlobalConfigItem(YpSettings.PREF_VIDEO_EXIST_USER, false);


        }

    }

    /**
     * 查看视频
     *
     * @param commonItemEvent
     */
    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("ItemVideoLoindingEvent")
            }

    )
    public void ItemVideoLoindingEvent(CommonItemEvent commonItemEvent) {

        Logger.e("====ItemVideoLoindingEvent");

        GeneralVideos generalVideos = (GeneralVideos) commonItemEvent.event;

        int position = (int) commonItemEvent.position;


        if (userID == loginUserId) {

            Bundle bundle = new Bundle();

            bundle.putSerializable("Data", generalVideos);

            bundle.putInt("position", position);

            bundle.putInt(YpSettings.USERID, userID);

            bundle.putString("type", "UserInfoPresenter");

            mView.VideoActivity(bundle);

            return;
        }


        Subscription subscription = HttpFactory.getHttpApi().getUnlockVideo(userID, generalVideos.getVideoId())

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(videoBean -> {

                    int status = videoBean.getResult();

                    if (status == 0) {


                        Bundle bundle = new Bundle();

                        bundle.putSerializable("Data", generalVideos);

                        bundle.putInt("position", position);

                        bundle.putInt(YpSettings.USERID, userID);

                        bundle.putString("type", "UserInfoPresenter");

                        mView.VideoActivity(bundle);


                    } else if (status == 1) {

                        String msg = TextUtils.isEmpty(videoBean.getMsg()) ? "普通用户每天只能免费查看5个视频，VIP不受此限制" : videoBean.getMsg();

                        mView.showCreateHintOperateDialog("", msg, "取消", "查看VIP", new BackCallListener() {
                            @Override
                            public void onCancel(View view, Object... obj) {
                                mView.dismissCreateHintOperateDialog();
                            }

                            @Override
                            public void onEnsure(View view, Object... obj) {

                                mView.dismissCreateHintOperateDialog();

                                Bundle bundle = new Bundle();

                                bundle.putInt("apple_count", 0);

                                bundle.putInt("userPosition", 0);

                                mView.VipOpenedActivity(bundle);

                            }
                        });


                    }
                }, throwable -> {

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }
                });

        addSubscrebe(subscription);


    }


    /**
     * 视频点赞
     *
     * @param commonItemEvent
     */
    @Subscribe(


            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("PariseVideoEvent")
            }

    )

    public void PariseVideoEvent(CommonItemEvent commonItemEvent) {


        GeneralVideos generalVideos = (GeneralVideos) commonItemEvent.event;

        int position = (int) commonItemEvent.position;


        if (userdto.getGeneralVideos() != null && userdto.getGeneralVideos().size() > 0 && userdto.getGeneralVideos().size() > position) {

            userdto.getGeneralVideos().get(position).setPraiseCount(generalVideos.getPraiseCount());

            userdto.getGeneralVideos().get(position).setPraiseStatus(generalVideos.getPraiseStatus());

            setVideoViewData(userdto);

        }


    }

    /**
     * 视频删除
     *
     * @param commonItemEvent
     */
    @Subscribe(


            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("DelectVideoEvent")
            }

    )

    public void DelectVideoEvent(CommonItemEvent commonItemEvent) {


        GeneralVideos generalVideos = (GeneralVideos) commonItemEvent.event;

        int position = (int) commonItemEvent.position;

        Logger.e("祯＝＝" + position);


        if (userdto.getGeneralVideos() != null && userdto.getGeneralVideos().size() > 0 && userdto.getGeneralVideos().size() > position) {

            userdto.getGeneralVideos().remove(position);


            Logger.e("祯＝＝" + position);

            setVideoViewData(userdto);

        }


    }


    public void createPhoto(int max) {


        Logger.e("相册");

        MultiImageSelector.create()
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(max) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
//                .single() // 单选模式
                .multi() // 多选模式, 默认模式;
//              .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(mActivity, YpSettings.PHOTO_REQUEST_IMAGE);


    }


    /**
     * 相册与视频 邀请
     *
     * @param invite
     * @param InviteType
     */

    private void AddPhotoVideoInvite(boolean invite, int InviteType) {


        mView.showLoadingDialog();

        Map<String, Object> map = new HashMap<>();

        map.put("InviteeId", userID);

        map.put("inviteType", InviteType);

        map.put("confirm", invite);

        Subscription subscription = mHttpApi.attampt(map)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(dto -> {

                    mView.dismissLoadingDialog();


                    if (dto != null) {

                        if (dto.getViewStatus() == 0) {

                            if (!CheckUtil.isEmpty(dto.getMessage())) {

                                mView.showDisCoverNetToast(dto.getMessage());

                            } else {
                                mView.showDisCoverNetToast("邀请失败");

                            }
                        } else if (dto.getViewStatus() == 1) {
                            if (!CheckUtil.isEmpty(dto.getMessage())) {
                                mView.showDisCoverNetToast(dto.getMessage());

                            } else {
                                mView.showDisCoverNetToast("邀请成功");

                            }

                        } else if (dto.getViewStatus() == 2) {


                            mView.showCreateHintOperateDialog("", dto.getMessage(), "取消", "邀请", new BackCallListener() {
                                @Override
                                public void onCancel(View view, Object... obj) {

                                    mView.dismissCreateHintOperateDialog();

                                    AddPhotoVideoInvite(true, InviteType);
                                }

                                @Override
                                public void onEnsure(View view, Object... obj) {

                                    mView.dismissCreateHintOperateDialog();

                                }
                            });


                        } else if (dto.getViewStatus() == 3) {
                            //已经邀请
                            if (!CheckUtil.isEmpty(dto.getMessage())) {

                                mView.showDisCoverNetToast(dto.getMessage());

                            } else {

                                mView.showDisCoverNetToast("已经邀请过了");

                            }

                        } else if (dto.getViewStatus() == 4) {


                            mView.showCreateHintOperateDialog("", dto.getMessage(), "取消", "立即认证", new BackCallListener() {
                                @Override
                                public void onCancel(View view, Object... obj) {
                                    mView.dismissCreateHintOperateDialog();


                                }

                                @Override
                                public void onEnsure(View view, Object... obj) {

                                    mView.dismissCreateHintOperateDialog();

                                    Bundle bundle = new Bundle();
                                    bundle.putInt(YpSettings.USERID, loginUserId);
                                    mView.VideoDetailGetActivity(bundle);

                                }
                            });


                        }
                    }


                }, throwable -> {

                    Logger.e("11111111111111111");

                    mView.dismissLoadingDialog();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }

                });


        addSubscrebe(subscription);


    }


    /**
     * 私密相册 解锁请求
     *
     * @param userId
     * @param lookedUserId
     */
    private void putPrivateAlbumVerification(int userId, int lookedUserId) {


    }

    private List<UserPhoto> getPrisePhoto(List<String> list) {

        Logger.e(list.toString());


        List<UserPhoto> priseNumber = new ArrayList<>();

        List<UserPhoto> pht = userdto.getPhotos();


        for (int i = 0; i < list.size(); i++) {

            String imgUrl = list.get(i);

            if (pht == null || pht.size() == 0) {


                priseNumber.add(i, new UserPhoto());

                continue;
            }


            for (int j = 0; j < pht.size(); j++) {

                UserPhoto userPhoto = pht.get(j);

                String phtStr = userPhoto.getImageUrl();

                if (TextUtils.equals(imgUrl, phtStr)) {

                    priseNumber.add(i, userPhoto);

                    break;


                } else {
                    priseNumber.add(i, new UserPhoto());
                }


            }


        }

        return priseNumber;


    }


    /**
     * 更换头像 去剪切
     *
     * @param filePath
     */
    private void photoHeadDeal(String filePath) {

        Bitmap bm = ImgUtils.resizesBitmap(filePath);
        if (null != bm) {
            // 保存在自己定义文件的路径
            String file_Path = ImgUtils.saveImgFile(mActivity, bm);
            // 回收内存空间
            bm.recycle();
            // 去剪切
            CropDirectionUtil.cropDirection(mActivity, HeadImgCompileActivity.class, YpSettings.USER_COMPILE, file_Path);
        } else {
            mView.showDisCoverNetToast("选取失败，请重新选择上传！");
        }
    }


    /**
     * 相册上传前
     *
     * @throws
     * @Title: setUriBitmap
     * @Description: 上传图片前对图片的操作(这里用一句话描述这个方法的作用)
     * @param: @param url
     * @return: void
     */
    private void setUriBitmap(List<String> list) {

        mView.showLoadingDialog();


        Subscription subscription = rx.Observable.from(list).subscribe(

                s -> {

                    Bitmap bm = ImgUtils.resizesBitmap(s);

                    if (null != bm) {
                        // 保存在自己定义文件的路径

                        String filePath = ImgUtils.saveImgFile(mActivity, bm);

                        bm.recycle();

                        if (!CheckUtil.isEmpty(filePath)) {

                            douploadingImage(filePath, list);

                        } else {
                            mView.dismissLoadingDialog();
                            mView.showDisCoverNetToast("选取失败，请重新选择上传！");
                        }

                    } else {
                        mView.dismissLoadingDialog();
                        mView.showDisCoverNetToast("选取失败，请重新选择上传！");
                    }


                }, throwable -> {

                    mView.dismissLoadingDialog();
                    mView.showDisCoverNetToast("选取失败，请重新选择上传！");
                }


        );

        addSubscrebe(subscription);


    }

    /**
     * 上传头像
     *
     * @param SaveToAlbum
     * @param filePath
     */
    private void douploadingUserHeadImg(boolean SaveToAlbum, String filePath) {


        mView.showLoadingDialog();

        Map<String, Object> urlMap = new HashMap<>();

        urlMap.put("saveToAlbum", false);


        Map<String, RequestBody> map = MultipartUtil.getFilesBody(filePath, "data");


        Subscription subscription = mHttpApi.uploadingHead(urlMap, map)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())


                .subscribe(headEntity -> {


                            Logger.e(headEntity.toString());

                            doputHeadImg(loginUserId, headEntity.headImgUrl);


                        }, throwable -> {

                            Logger.e("失败=" + throwable.getMessage());

                            mView.dismissLoadingDialog();

                            mView.showDisCoverNetToast("选取失败，请重新选择上传！");

                        }


                );

        addSubscrebe(subscription);


    }

    /**
     * 更新用户头像请求
     *
     * @param userId
     * @param imgUrl
     */
    private void doputHeadImg(int userId, String imgUrl) {

        Subscription subscription = mHttpApi.putHeadImg(userId, imgUrl)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(b -> {

                    mView.dismissLoadingDialog();

                    String iconUrl = ImgUtils.DealImageUrl(imgUrl, 640, 640);

                    userdto.getProfile().getHeadImg();

                    mView.user_info_iv_icon(iconUrl);


                }, throwable -> {

                    mView.dismissLoadingDialog();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }

                });

        addSubscrebe(subscription);


    }


    /**
     * 上传相册图片
     *
     * @param filePath
     */
    private void douploadingImage(String filePath, List<String> list) {

        Logger.e("上传相册图片=");


        Map<String, RequestBody> map = MultipartUtil.getFilesBody(filePath, "data");


        Subscription subscription = mHttpApi.uploadingImage(map)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(s -> {

                            Logger.e("s=" + s);


                            uploadingList.add(s);


                            if (uploadingList.size() == list.size()) {

                                putAlbum(userID, imageType, uploadingList);

                            }


                        }, throwable -> {

                            Toast.makeText(App.getInstance().getApplicationContext(), "选取失败，请重新选择上传！", Toast.LENGTH_SHORT).show();

//                            mView.showDisCoverNetToast("\"选取失败，请重新选择上传！\"");

//                            handleErrorHint("选取失败，请重新选择上传！");
                            mView.dismissLoadingDialog();


                            Logger.e("throwable=" + throwable.getMessage());

                        }

                );

        addSubscrebe(subscription);


    }


    /**
     * 更新相册或私密相册
     *
     * @param userId
     * @param imageType
     * @param album
     */

    private void putAlbum(int userId, int imageType, List<String> album) {

        Logger.e(album.toString());


        List<String> currList = new ArrayList<>();


        if (imageType == image_photo) {

            if (userdto.getPhotos() != null && userdto.getPhotos().size() > 0) {

                for (int i = 0; i < userdto.getPhotos().size(); i++) {


                    currList.add(userdto.getPhotos().get(i).getImageUrl());


                }

            }

            currList.addAll(album);

        } else if (imageType == image_private_photo) {


            if (userdto.getPrivacyAlbum() != null && userdto.getPrivacyAlbum().size() > 0) {


                for (int i = 0; i < userdto.getPrivacyAlbum().size(); i++) {


                    currList.add(userdto.getPrivacyAlbum().get(i).getImageUrl());

                }


            }

            currList.addAll(album);

        }


        Logger.e(currList.toString());


        Subscription subscription = HttpFactory.getHttpApi().putAlbum(userId, imageType, currList)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(b -> {

                    mView.dismissLoadingDialog();


                    getHttpUserData();


//                    if (imageType == image_photo) {
//
//
//                        if (userdto.getPhotos() == null) {
//
//                            userdto.setPhotos(new ArrayList<>());
//
//                        }
//
//
//                        for (int i = 0; i < album.size(); i++) {
//
//                            UserPhoto userPhoto = new UserPhoto();
//
//                            userPhoto.setImageUrl(album.get(i));
//
//                            userdto.getPhotos().add(userPhoto);
//
//                        }
//
//
//                        setPhotoViewData(userdto);
//
//
//                    } else if (imageType == image_private_photo) {
//
//
//                        if (userdto.getPrivacyAlbum() == null) {
//
//                            userdto.setPrivacyAlbum(new ArrayList<>());
//                        }
//
//
//                        for (int i = 0; i < album.size(); i++) {
//
//                            PrivacyAlbum privacyAlbum = new PrivacyAlbum();
//
//                            privacyAlbum.setImageUrl(album.get(i));
//
//                            userdto.getPrivacyAlbum().add(privacyAlbum);
//
//
//                        }
//
//                        setPrivatePhotoViewData(userdto);


//                    }


                }, throwable -> {

                    mView.dismissLoadingDialog();

                    mView.showDisCoverNetToast("选取失败，请重新选择上传！");


                });


        addSubscrebe(subscription);

    }


    private void setDateToView(UserDto userdto) {


        if (userdto == null) return;


        mView.user_info_tv_title(StringUtils.isEmpty(userdto.getProfile().getName()));

        mView.user_info_tv_detail_name(userdto.getProfile().getName());

        String iconUrl = ImgUtils.DealImageUrl(userdto.getProfile().getHeadImg(), 640, 640);

        mView.user_info_iv_icon(iconUrl);

        int status = userdto.getProfile().getStatus();


        //头像未审核
        if (((status >> 0) & 1) == 0) {

            if (userID == loginUserId) {


                mView.user_info_ll_icon_hintVisible();
            } else {
                mView.user_info_ll_icon_hintGone();
            }
        } else {
            mView.user_info_ll_icon_hintGone();
        }

        //手机认证
        if (userID == loginUserId) {

            mView.user_info_tv_phone_btnVisible();

            if (((status >> 2) & 1) == 0) {

                mView.user_info_tv_phone_state("");

                mView.user_info_tv_phone_btn("立即认证");


            } else {

                mView.user_info_tv_phone_state("手机号码已认证");

                if (!CheckUtil.isEmpty(userdto.getProfile().getUid())) {

                    mView.user_info_tv_phone_btn("更换");

                } else {
                    mView.user_info_tv_phone_btn("");
                }

            }

        } else {
            mView.user_info_tv_phone_btnGone();
            if (((status >> 2) & 1) == 0) {
                mView.user_info_tv_phone_state("");
            } else {
                mView.user_info_tv_phone_state("手机号码已认证");
            }
        }

        //头像认证
        if (userdto.getVideoVerification() != null) {

            if (userID == loginUserId) {


                switch (userdto.getVideoVerification().getStatus()) {
                    //未认证
                    case 0:

                        mView.user_info_iv_video_inviteVisible();

                        mView.user_info_iv_video_invite(R.drawable.icon_video_not_certified);

                        mView.user_info_iv_video_stateGone();

                        mView.user_info_tv_video_state("尚未通过视频认证");

                        mView.user_info_tv_video_btn("立即认证");

                        mView.user_info_tv_video_btn_color(R.color.color_3187fc);

                        break;
                    //审核中
                    case 1:

                        mView.user_info_iv_video_inviteVisible();

                        mView.user_info_iv_video_invite(R.drawable.icon_video_check);

                        mView.user_info_iv_video_stateGone();

                        mView.user_info_tv_video_state("视频审核中...");

                        mView.user_info_tv_video_btn("");

                        break;
                    //已认证
                    case 2:

                        mView.user_info_iv_video_inviteGone();

                        mView.user_info_iv_video_invite(0);

                        mView.user_info_iv_video_stateVisible();

                        mView.user_info_tv_video_state("已通过视频认证");


                        if (userdto.getVideoVerification().getPurpose() == 1) {

                            mView.user_info_tv_video_btn("结交知己好友");

                        } else if (userdto.getVideoVerification().getPurpose() == 2) {
                            mView.user_info_tv_video_btn("邂逅浪漫爱情");

                        } else if (userdto.getVideoVerification().getPurpose() == 3) {
                            mView.user_info_tv_video_btn("寻找结婚对象");

                        }
                        mView.user_info_tv_video_btn_color(R.color.color_b2b2b2);
                        break;
                    //未通过
                    case 3:

                        mView.user_info_iv_video_inviteVisible();

                        mView.user_info_iv_video_invite(R.drawable.icon_video_not_certified);

                        mView.user_info_iv_video_stateGone();

                        mView.user_info_tv_video_state("尚未通过视频认证");

                        mView.user_info_tv_video_btn("立即认证");
                        mView.user_info_tv_video_btn_color(R.color.color_3187fc);
                        break;
                }
            } else { //看别人的时候

                if (userdto.getVideoVerification().getStatus() == 2) {

                    mView.user_info_iv_video_inviteGone();

                    mView.user_info_iv_video_invite(0);

                    mView.user_info_iv_video_stateVisible();

                    mView.user_info_tv_video_state("已通过视频认证");


                    if (userdto.getVideoVerification().getPurpose() == 1) {

                        mView.user_info_tv_video_btn("结交知己好友");


                    } else if (userdto.getVideoVerification().getPurpose() == 2) {

                        mView.user_info_tv_video_btn("邂逅浪漫爱情");


                    } else if (userdto.getVideoVerification().getPurpose() == 3) {

                        mView.user_info_tv_video_btn("寻找结婚对象");

                    }

                    mView.user_info_tv_video_btn_color(R.color.color_b2b2b2);

                } else {

                    mView.user_info_iv_video_inviteVisible();

                    mView.user_info_iv_video_invite(R.drawable.icon_video_invite);

                    mView.user_info_iv_video_stateGone();

                    mView.user_info_tv_video_state("尚未通过视频认证");

                    mView.user_info_tv_video_btn("邀请");

                    mView.user_info_tv_video_btn_color(R.color.color_3187fc);

                }
            }

        }


        //hot
        if (userdto.isHot()) {
            mView.user_info_iv_hotVisible();
        } else {
            mView.user_info_iv_hotGone();

        }
        // 活动达人
        if (userdto.isActivityExpert()) {
            mView.user_info_iv_activity_talentVisible();

        } else {
            mView.user_info_iv_activity_talentGone();

        }
        Logger.e("Vip等级");

        //Vip等级
        switch (userdto.getCurrentUserPosition()) {
            //不是vip
            case 0:
                mView.user_info_iv_vipGone();
                break;
            //白银
            case 1:
                mView.user_info_iv_vipVisible();
                mView.user_info_iv_vip(R.drawable.icon_userinfo_vip_silver);
                break;
            //黄金
            case 2:
                mView.user_info_iv_vipVisible();
                mView.user_info_iv_vip(R.drawable.icon_userinfo_vip_gold);

                break;
            //铂金
            case 3:
                mView.user_info_iv_vipVisible();
                mView.user_info_iv_vip(R.drawable.icon_userinfo_vip_platinum);

                break;
            //钻石
            case 4:
                mView.user_info_iv_vipVisible();
                mView.user_info_iv_vip(R.drawable.icon_userinfo_vip_diamond);

                break;

        }


        //资料完善
        if (userID == loginUserId) {


            if (!userdto.isProfileComplete()) {

                mView.user_info_tv_isprofilecompleteVisible();

            } else {

                mView.user_info_tv_isprofilecompleteGone();

            }


        } else {
            mView.user_info_tv_isprofilecompleteGone();
        }

        //矩离
        String location = CheckUtil.getSpacingTool(userdto.getDistance());

        mView.user_info_tv_distance(location);

        //时间
        long time = ISO8601.getTime(userdto.getLastActiveTime());

        String times = TimeUtil.LivelyTimeFormat(time);


        mView.user_info_tv_time(times);


        //性别
        if (userdto.getProfile().getSex() == 1) {

            mView.user_info_tv_detail_sex(R.drawable.ic_sex_man);

        } else {

            mView.user_info_tv_detail_sex(R.drawable.ic_sex_woman);

        }

        //星座
        int horoscope = userdto.getProfile().getHoroscope();

        mView.user_info_tv_hor(CheckUtil.ConstellationMatching(horoscope));


        if (horoscope == 1) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_baiyang);

        } else if (horoscope == 2) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_jinniu);

        } else if (horoscope == 3) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_shuangzi);

        } else if (horoscope == 4) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_juxie);

        } else if (horoscope == 5) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_shizi);

        } else if (horoscope == 6) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_chunv);

        } else if (horoscope == 7) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_tianchen);

        } else if (horoscope == 8) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_tianxie);

        } else if (horoscope == 9) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_sheshou);

        } else if (horoscope == 10) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_mojie);

        } else if (horoscope == 11) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_shuiping);

        } else if (horoscope == 12) {

            mView.user_info_iv_hor(R.drawable.user_info_hor_shuangyu);

        }

        //星座
        if (userID == loginUserId) {


            mView.user_info_v_match_lineVisible();

            mView.user_info_ll_match_lineVisible();

            mView.user_info_tv_everyday_match("星座运势");

            setMatchView(userdto.getTodayLucky());


        } else {


            if (DbHelperUtils.getDbUserSex(loginUserId) == userdto.getProfile().getSex()) {
                mView.user_info_v_match_lineGone();
                mView.user_info_ll_match_lineGone();

            } else {

                mView.user_info_v_match_lineVisible();
                mView.user_info_ll_match_lineVisible();
                mView.user_info_tv_everyday_match("星座匹配");
                setMatchView(userdto.getHoroscopeMatch());
            }

        }

        //讨厌
        mView.user_info_tv_dislike(StringUtils.isEmpty(userdto.getProfile().getDislikes()));
        //喜欢
        mView.user_info_tv_like(StringUtils.isEmpty(userdto.getProfile().getLikes()));
        //家乡
        mView.user_info_tv_home(StringUtils.isEmpty(userdto.getProfile().getHometown()));
        //ID
        mView.user_info_tv_ID(StringUtils.isEmpty(userdto.getProfile().getId() + ""));

        //收入
        if (userdto.getProfile().getIncomeLevel() != null) {

            String incomeStr = UserInfoUtils.getIncome(userdto.getProfile().getIncomeLevel());

            if (TextUtils.equals(incomeStr, "未填写")) {

                mView.user_info_tv_income("");


            } else {
                mView.user_info_tv_income(StringUtils.isEmpty(incomeStr));
            }


        } else {
            mView.user_info_tv_income("");
        }

        //职业
        mView.user_info_tv_profession(StringUtils.isEmpty(userdto.getProfile().getCareer()));

        //情感状态
        if (userdto.getProfile().getRelationship() == null) {

            mView.user_info_tv_emotional(StringUtils.isEmpty(""));

        } else {

            String emotional = UserInfoUtils.getEmotional(userdto.getProfile().getRelationship());

            if (TextUtils.equals(emotional, "未填写")) {

                mView.user_info_tv_emotional(StringUtils.isEmpty(""));
            } else {
                mView.user_info_tv_emotional(StringUtils.isEmpty(emotional));
            }


        }


        //冒泡

        setBubbleViewData(userdto);


        //身高
        if (userdto.getProfile().getHeight() == null) {

            mView.user_info_tv_height("");


        } else {

            mView.user_info_tv_height(userdto.getProfile().getHeight() + "cm");
        }

        //体重
        if (userdto.getProfile().getWeight() == null) {

            mView.user_info_tv_weight("");

        } else {

            mView.user_info_tv_weight(userdto.getProfile().getWeight() + "kg");

        }

        //年龄
        if (userdto.getProfile().getAge() == null) {

            mView.user_info_tv_age("");

            mView.user_info_tv_age_levelGone();

        } else {

            if (userdto.getProfile().isBirthdayPrivacy()) {

                mView.user_info_tv_age_levelGone();

                mView.user_info_tv_age("保密");
            } else {

                mView.user_info_tv_age_levelVisible();

                mView.user_info_tv_age_level(UserInfoUtils.getBornPeriod(userdto.getProfile().getAge()));

                mView.user_info_tv_age(userdto.getProfile().getAge() + "");
            }

        }

        //邀约


        setDatingViewData(userdto);


        //标签
        //		isFirst++;

        if (!CheckUtil.isEmpty(userdto.getProfile().getTags()) && !userdto.getProfile().getTags().equals("null")) {

            mView.user_info_tv_lableGone();

            mView.user_info_rv_lableVisible();

            String tagStr = userdto.getProfile().getTags();
            String tas[] = tagStr.split(",");

            mView.user_info_rv_lable(tas);


        } else {


            mView.user_info_tv_lableVisible();

            mView.user_info_rv_lableGone();

        }


        //相册

        setPhotoViewData(userdto);


        //私密相册

        setPrivatePhotoViewData(userdto);


        //视频  user_info_rv_video


        setVideoViewData(userdto);

        //礼物

        setGiftSumViewData(userdto);


        //喜欢

        setLikeViewData(userdto);

    }

    /**
     * 冒泡
     *
     * @param userdto
     */
    private void setBubbleViewData(UserDto userdto) {

        if (userdto.getBubble() != null && userdto.getBubble().getTotal() > 0) {


            mView.user_info_ll_bubbleVisible();

            mView.user_info_tv_bubble(userdto.getBubble().getTotal() + "");

            mView.user_info_tv_bubble_title(StringUtils.isEmpty(userdto.getBubble().getAddressName()));

            mView.user_info_tv_bubble_content(StringUtils.isEmpty(userdto.getBubble().getContent()));


            if (userdto.getBubble().getType() == 2) {//私密相册


                String imgUrl = ImgUtils.DealImageUrl(userdto.getBubble().getImageUrl(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);


                if (loginUserId != userID && !userdto.isUnlockPrivacyAlbum()) {

                    mView.user_info_iv_bubble_img_private(imgUrl);

                    mView.user_album_border(R.drawable.ic_private);

                } else {

                    mView.user_info_iv_bubble_img(imgUrl);

                    mView.user_album_border(0);
                }


            } else if (userdto.getBubble().getType() == 3) {//形象视频


                String coverImgUrl = ImgUtils.DealVideoImageUrl(userdto.getBubble().getImageUrl(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);


                mView.user_info_iv_bubble_img(coverImgUrl);

//                mView.user_album_border(R.drawable.ic_play);
                mView.user_album_border(0);


            } else {


                String imageurl = ImgUtils.DealImageUrl(userdto.getBubble().getImageUrl(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

                mView.user_info_iv_bubble_img(imageurl);

                mView.user_album_border(0);

            }


        } else {

            mView.user_info_ll_bubbleGone();

            mView.user_info_tv_bubble("");

        }

    }

    /**
     *
     * @param userdto
     */
    private void setDatingViewData(UserDto userdto) {

        if (userdto.getDatingList() == null || userdto.getDatingList().size() == 0) {

            mView.user_info_tv_dating_inviteGone();

            mView.user_info_tv_appointment("");

        } else {

            if (loginUserId == userID) {
                mView.user_info_tv_dating_inviteGone();

            } else {
                mView.user_info_tv_dating_inviteVisible();
            }


            mView.user_info_tv_appointment(userdto.getDatingList().size() + "");

        }

        mView.user_info_rv_appointment(userdto.getDatingList(), loginUserId, userID, userdto.getProfile().getSex());

    }


    /**
     * 设置喜欢btn 数据
     *
     * @param userdto
     */
    private void setLikeViewData(UserDto userdto) {


        int likeId = userdto.getUserLikeState();

        if (likeId == 0) { //0表示不喜欢

            mView.user_info_tv_like_btn("喜欢");

            mView.user_info_iv_like_icon(R.drawable.icon_userinfo_like);

        } else if (likeId == 1) {//1表示我喜欢

            mView.user_info_tv_like_btn("已喜欢");

            mView.user_info_iv_like_icon(R.drawable.icon_userinfo_like_selected);


        } else if (likeId == 2) {//2表示喜欢我

            mView.user_info_tv_like_btn("Ta喜欢我");

            mView.user_info_iv_like_icon(R.drawable.icon_userinfo_like);

        } else if (likeId == 3) {//3表示相互喜欢

            mView.user_info_tv_like_btn("互相喜欢");

            mView.user_info_iv_like_icon(R.drawable.icon_userinfo_like_selected);

        }


    }


    /**
     * 设置相册的数据
     *
     * @param userdto
     */
    private void setPhotoViewData(UserDto userdto) {


        if (userID == loginUserId) {//自己


            if (userdto.getPhotos() != null && userdto.getPhotos().size() > userdto.getAlbumMax()) {


//                List<String> album = UserInfoUtils.userPhotoToAlbum(userdto);

                mView.user_info_rv_photo(userdto.getPhotos().subList(0, userdto.getAlbumMax()));


                Logger.e("1");


            } else { //自己的要上传

                Logger.e("2");

//                List<String> album = UserInfoUtils.userPhotoToAlbum(userdto);

                mView.user_info_rv_photo(changeAlbumData(userdto.getPhotos(), userdto.getAlbumMax(), YpSettings.suppose));


            }


        } else {

            Logger.e("album" + userdto.getPhotos());

            if (userdto.getPhotos() != null && userdto.getPhotos().size() > 0 && userdto.getPhotos().size() > userdto.getAlbumMax()) {


//                List<String> album = UserInfoUtils.userPhotoToAlbum(userdto);


                mView.user_info_rv_photo(userdto.getPhotos().subList(0, userdto.getAlbumMax()));


            } else { //别人的要邀请上传

                List<String> album = UserInfoUtils.userPhotoToAlbum(userdto);

                mView.user_info_rv_photo(changeAlbumData(userdto.getPhotos(), userdto.getAlbumMax(), YpSettings.album_invite));


            }


        }

    }

    /**
     * 私密相册
     *
     * @param userdto
     */
    private void setPrivatePhotoViewData(UserDto userdto) {


        if (userID == loginUserId) {//自己


            mView.user_info_tv_private_photoVisible();

            mView.user_info_rv_private_photoVisible();


            if (userdto.getPrivacyAlbum() != null && userdto.getPrivacyAlbum().size() > 0 && userdto.getPrivacyAlbum().size() > userdto.getPrivacyAlbumMax()) {//已有私密相册


//                List<String> album = userdto.getPrivacyAlbum();

                mView.user_info_rv_private_photo(userdto.getPrivacyAlbum().subList(0, userdto.getPrivacyAlbumMax()), userdto.isUnlockPrivacyAlbum());


            } else {//没有私密相册  自己的要上传

//                List<String> album = userdto.getPrivacyAlbum();


                mView.user_info_rv_private_photo(changePrivateAlbumData(userdto.getPrivacyAlbum(), userdto.getPrivacyAlbumMax(), YpSettings.suppose), userdto.isUnlockPrivacyAlbum());


            }


        } else {//别人的


            if (userdto.getPrivacyAlbum() == null || userdto.getPrivacyAlbum().size() == 0) {

                mView.user_info_tv_private_photoGone();

                mView.user_info_rv_private_photoGone();

            } else if (userdto.getPrivacyAlbum().size() > userdto.getPrivacyAlbumMax()) {//已有私密相册

//                List<String> album = userdto.getPrivacyAlbum();

                mView.user_info_tv_private_photoVisible();

                mView.user_info_rv_private_photoVisible();

                mView.user_info_rv_private_photo(userdto.getPrivacyAlbum().subList(0, userdto.getPrivacyAlbumMax()), userdto.isUnlockPrivacyAlbum());


            } else {

//                List<String> album = userdto.getPrivacyAlbum();

                mView.user_info_tv_private_photoVisible();

                mView.user_info_rv_private_photoVisible();

                mView.user_info_rv_private_photo(userdto.getPrivacyAlbum(), userdto.isUnlockPrivacyAlbum());
            }
        }


    }

    /**
     * 视频
     *
     * @param userdto
     */
    private void setVideoViewData(UserDto userdto) {


        if (userID == loginUserId) {//是自己

            mView.user_info_tv_videoVisible();

            mView.user_info_rv_videoVisible();


            if (userdto.getGeneralVideos() != null && userdto.getGeneralVideos().size() > 0 && userdto.getGeneralVideos().size() > userdto.getGeneralVideoMax()) {


                mView.user_info_rv_video(userdto.getGeneralVideos().subList(0, userdto.getGeneralVideoMax()));


            } else { //自己的要上传

                mView.user_info_rv_video(changeVideoData(userdto.getGeneralVideos(), userdto.getGeneralVideoMax(), YpSettings.suppose));

            }


        } else {//不是自己


            if (userdto.getGeneralVideos() == null || userdto.getGeneralVideos().size() == 0) {

                mView.user_info_tv_videoGone();

                mView.user_info_rv_videoGone();

            } else if (userdto.getGeneralVideos().size() > userdto.getGeneralVideoMax()) {//已有私密相册


                mView.user_info_tv_videoVisible();

                mView.user_info_rv_videoVisible();

                mView.user_info_rv_video(userdto.getGeneralVideos().subList(0, userdto.getGeneralVideoMax()));


            } else {

                mView.user_info_tv_videoVisible();

                mView.user_info_rv_videoVisible();

                mView.user_info_rv_video(userdto.getGeneralVideos());
            }
        }


    }


    private void setGiftSumViewData(UserDto userdto) {


        if (userdto.getMyGiftSum() == null || userdto.getMyGiftSum().size() == 0) {

            mView.user_info_ll_giftGone();


        } else {

            mView.user_info_ll_giftVisible();

            mView.user_info_rv_gift(userdto.getMyGiftSum());

        }


    }

    private List<GeneralVideos> changeVideoData(List<GeneralVideos> videoList, int max, String albumType) {


        List<GeneralVideos> list = new ArrayList<>();

        GeneralVideos generalVideos = new GeneralVideos();

        generalVideos.setCoverImgUrl(YpSettings.suppose);


        if (videoList == null) {

            list.add(generalVideos);

        } else if (videoList.size() < max) {

            list = videoList;


            // 判断是否有标示suppose
            for (int i = 0; i < list.size(); i++) {// 清空所有的标识
                if (TextUtils.equals(list.get(i).getCoverImgUrl(), YpSettings.suppose)) {
                    list.remove(i);
                    break;
                }
            }

            // 设置 userInfo对象
            list.add(generalVideos);

        } else if (videoList.size() >= max) {

            list = videoList.subList(0, max);

        }


        return list;

    }

    /**
     * 如果长度大于等于max 截取前max张，如果小于max，则造一个空邀请的
     *
     * @param ablumList
     * @return
     */
    private List<UserPhoto> changeAlbumData(List<UserPhoto> ablumList, int max, String albumType) {

        List<UserPhoto> dolist = new ArrayList<UserPhoto>();// 包括标记符的

        if (ablumList == null) {

            UserPhoto userPhoto = new UserPhoto();

            userPhoto.setImageUrl(albumType);

            dolist.add(userPhoto);

        } else if (ablumList.size() < max) {

            dolist.addAll(ablumList);

            // 判断是否有标示suppose

            for (int i = 0; i < dolist.size(); i++) {// 清空所有的标识

                UserPhoto userPhoto = dolist.get(i);

                if (TextUtils.equals(userPhoto.getImageUrl(), albumType)) {

                    dolist.remove(i);

                    break;
                }
            }

            // 设置 userInfo对象

            UserPhoto userPhoto = new UserPhoto();

            userPhoto.setImageUrl(albumType);

            dolist.add(userPhoto);


        } else if (ablumList.size() >= max) {

            dolist = ablumList.subList(0, max);
        }

        return dolist;
    }

    /**
     * 如果长度大于等于max 截取前max张，如果小于max，则造一个空邀请的
     *
     * @param ablumList
     * @return
     */
    private List<PrivacyAlbum> changePrivateAlbumData(List<PrivacyAlbum> ablumList, int max, String albumType) {

        List<PrivacyAlbum> dolist = new ArrayList<PrivacyAlbum>();// 包括标记符的

        if (ablumList == null) {

            PrivacyAlbum privacyAlbum = new PrivacyAlbum();

            privacyAlbum.setImageUrl(albumType);

            dolist.add(privacyAlbum);

        } else if (ablumList.size() < max) {

            dolist.addAll(ablumList);

            // 判断是否有标示suppose

            for (int i = 0; i < dolist.size(); i++) {// 清空所有的标识

                PrivacyAlbum privacyAlbum = new PrivacyAlbum();


                if (TextUtils.equals(privacyAlbum.getImageUrl(), albumType)) {

                    dolist.remove(i);

                    break;
                }
            }

            // 设置 对象

            PrivacyAlbum privacyAlbum = new PrivacyAlbum();

            privacyAlbum.setImageUrl(albumType);

            dolist.add(privacyAlbum);


        } else if (ablumList.size() >= max) {

            dolist = ablumList.subList(0, max);
        }

        return dolist;
    }


    private void setMatchView(int score) {

        Logger.e("setMatchView=" + score);

        if (score == 0) {

            mView.user_info_rb_score(0);


        } else if (score > 0 && score < 20) {

            mView.user_info_rb_score(0.5f);


        } else if (score == 20) {
            mView.user_info_rb_score(1);


        } else if (score > 20 && score < 40) {
            mView.user_info_rb_score(1.5f);


        } else if (score == 40) {
            mView.user_info_rb_score(2);


        } else if (score > 40 && score < 60) {
            mView.user_info_rb_score(2.5f);


        } else if (score == 60) {
            mView.user_info_rb_score(3);


        } else if (score > 60 && score < 80) {
            mView.user_info_rb_score(3.5f);


        } else if (score == 80) {
            mView.user_info_rb_score(4);

        } else if (score > 80 && score < 100) {
            mView.user_info_rb_score(4.5f);


        } else if (score == 100) {
            mView.user_info_rb_score(5);


        }

    }


    private void userIconPassAuditWebHint() {

        //跳转到web 查看帮助

        Bundle bun = new Bundle();

        bun.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Standard/AvatarAudit");

        bun.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "头像审核规范");

        bun.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

        mView.SimpleWebViewActivity(bun);


    }


    /**
     * 聊天
     *
     * @param userId
     * @param datingId
     */
    private void postChatRequest(int userId, String datingId) {

        mView.showLoadingDialog();

        Subscription subscription = mHttpApi.ChatDatingUserAttampt(userId, datingId)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(dto -> {

                    mView.dismissLoadingDialog();


                    if (dto != null) {
                        if (dto.getViewStatus() == 0) {

                            if (!CheckUtil.isEmpty(dto.getMessage())) {

                                mView.showDisCoverNetToast(dto.getMessage());


                            } else {

                                mView.showDisCoverNetToast("无法发起聊天");

                            }
                        } else if (dto.getViewStatus() == 1) {


                            setChatActivity(userId, datingId, dto.getSendMsg());

//                            Bundle bundle = new Bundle();
//
//                            bundle.putInt(YpSettings.USERID, userId);
//
//                            bundle.putString(YpSettings.DATINGS_ID, datingId);
//
//                            if (!CheckUtil.isEmpty(dto.getSendMsg())) {
//
//                                mChatObserver = new ChatObserver(userId + "", TIMConversationType.C2C);
//
//
//                                long date = System.currentTimeMillis();
//
//                                TextMsg msg = new TextMsg(MessageType.Text, dto.getSendMsg(), 0, datingId, 0);
//
//                                String msg_str = JsonUtils.toJson(msg);
//
//
//                                AttributeDto attributeDto = new AttributeDto();
//
//                                attributeDto.setMask(0);
//
//                                attributeDto.setDateid(datingId);
//
//                                attributeDto.setCounsel(0);
//
//                                attributeDto.setType(MessageType.Text);
//
//                                String attributeDto_str = JsonUtils.toJson(attributeDto);
//
//
//                                ImMessage message = new TextMessage(dto.getSendMsg(), attributeDto_str);
//
//                                String TIMMessageStr = JsonUtils.toJson(message.getMessage());
//
//                                String msgId = message.getMessage().getMsgId();
//
//
//                                ChatUtils.SaveOrUpdateChatMsgToDB(userId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgId, ChatDto.succeed, datingId, 0, TIMMessageStr);
//
//                                ChatUtils.saveMessageRecord(msg_str, userId + "", ChatDto.succeed, ChatDto.s_type, date, 0, datingId, TIMMessageStr);
//
//                                mChatObserver.sendMessage(message.getMessage());
//
//                            }
//                            mView.ChatActivity(bundle);


                        } else if (dto.getViewStatus() == 2) {

                            //头像不通过 帮助 取消

                            mView.showCreateHintOperateDialog("", dto.getMessage(), "查看帮助", "立即上传", new BackCallListener() {
                                @Override
                                public void onCancel(View view, Object... obj) {

                                    mView.dismissCreateHintOperateDialog();


                                    //跳转到web 查看帮助

                                    userIconPassAuditWebHint();
                                }

                                @Override
                                public void onEnsure(View view, Object... obj) {

                                    mView.dismissCreateHintOperateDialog();

                                    Bundle bundles = new Bundle();

                                    bundles.putInt(YpSettings.USERID, loginUserId);

                                    mView.UserInfoActivity(bundles);

                                }
                            });


                        } else if (dto.getViewStatus() == 3) {


                            // 对方拒绝接收非视频认证用户消息

                            showCreateHintOperateDialog(dto.getMessage());


                        } else if (dto.getViewStatus() == 6) {


                            //确定

                            showCreateHotHintDialog();


                        } else if (dto.getViewStatus() == 7) {

                            mView.showDisCoverNetToast("该用户已被系统封禁，无法与他联系");


                        }
                    }


                }, throwable -> {

                    mView.dismissLoadingDialog();


                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }


                });

        addSubscrebe(subscription);

    }

    /**
     * hot 提示
     */
    private void showCreateHotHintDialog() {

        mView.showCreateHotHintDialog(new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {
                mView.dismissCreateHotHintDialog();
            }

            @Override
            public void onEnsure(View view, Object... obj) {

                mView.dismissCreateHotHintDialog();

                //根据登陆用户的VIP状态跳转页面
                //若没有开通过VIP，点击进入VIP介绍页
                //若VIP已过期，点击进入续费页面

                Bundle bundle = new Bundle();

                int userPosition = DbHelperUtils.getOldVipPosition(loginUserId);

                bundle.putInt("userPosition", userPosition);

                if (0 == userPosition) {

                    mView.VipOpenedActivity(bundle);

                } else {
                    mView.VipRenewalsActivity(bundle);
                }


            }
        });


    }

    /**
     * 对方拒绝接收非视频认证用户消息
     */
    private void showCreateHintOperateDialog(String msg) {

        mView.showCreateHintOperateDialog("", msg, "取消", "立即认证", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                mView.dismissCreateHintOperateDialog();

            }

            @Override
            public void onEnsure(View view, Object... obj) {

                mView.dismissCreateHintOperateDialog();

                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.USERID, loginUserId);

                mView.VideoDetailGetActivity(bundle);


            }
        });


    }

    /**
     * 启动聊天时，
     *
     * @param userId
     * @param dateId
     * @param msaager
     */

    private void setChatActivity(int userId, String dateId, String msaager) {


        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, userId);

        bundle.putString(YpSettings.DATINGS_ID, dateId);

        if (!CheckUtil.isEmpty(msaager)) {

            mChatObserver = new ChatObserver(userID + "", TIMConversationType.C2C);

            long date = System.currentTimeMillis();

            TextMsg msg = new TextMsg(MessageType.Text, msaager, 0, dateId, 0);

            String msg_Str = JsonUtils.toJson(msg);

            AttributeDto attributeDto = new AttributeDto();

            attributeDto.setMask(0);

            attributeDto.setDateid(dateId);

            attributeDto.setCounsel(0);

            attributeDto.setType(MessageType.Text);

            String attributeDto_str = JsonUtils.toJson(attributeDto);

            ImMessage message = new TextMessage(msaager, attributeDto_str);

            String TIMMessageStr = JsonUtils.toJson(message.getMessage());

            String msgId = message.getMessage().getMsgId();

            ChatUtils.SaveOrUpdateChatMsgToDB(userID + "", msg_Str, date, ChatDto.s_type, ChatDto.readed_status, msgId, ChatDto.succeed, dateId, 0, TIMMessageStr);

            ChatUtils.saveMessageRecord(msg_Str, userID + "", ChatDto.succeed, ChatDto.s_type, date, 0, dateId, TIMMessageStr);

            mChatObserver.sendMessage(message.getMessage(),null);
        }


        mView.ChatActivity(bundle);


    }

    /**
     * 邀约邀请
     *
     * @param dateId
     * @param targetUserId
     */
    private void DatingsInvite(String dateId, String targetUserId) {

        mView.showLoadingDialog();

        Subscription subscription = mHttpApi.DatingsInvite(dateId, targetUserId)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())


                .subscribe(datingsInviteEntity -> {

                    mView.dismissLoadingDialog();


                    if (datingsInviteEntity.getResult() == null) {


                        return;
                    }


                    if (datingsInviteEntity.getResult() == 0) {//成功


                        setChatActivity(userID, dateId, datingsInviteEntity.getReturnMsg());

                        return;
                    }


                    if (datingsInviteEntity.getResult() == 1) {//需要上传头像

                        userIconPassAuditWebHint();


                        return;
                    }


                    if (datingsInviteEntity.getResult() == 2) {//对方拒绝接收非视频认证用户消息


                        // 对方拒绝接收非视频认证用户消息

                        showCreateHintOperateDialog(datingsInviteEntity.getReturnMsg());


                        return;
                    }


                    if (datingsInviteEntity.getResult() == 3) {//对方Hot

                        showCreateHotHintDialog();


                        return;
                    }
                    if (datingsInviteEntity.getResult() == 4) {//重复邀请

                        mView.showDisCoverNetToast(datingsInviteEntity.getReturnMsg());

                        return;
                    }


                    mView.showDisCoverNetToast(datingsInviteEntity.getReturnMsg());

                }, throwable -> {

                    mView.dismissLoadingDialog();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }


                });

        addSubscrebe(subscription);


    }


    SuccessTimer successtimer;

    private class SuccessTimer extends CountDownTimer {

        public SuccessTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            mView.dismissTimerCreateSuccessHintDialog();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


    /**
     * 邀约他参加我的邀约
     *
     * @param
     */
    private void invitationAppoint() {


        //邀请他参加我的邀约
        List<AppointDetailDto> list = DbHelperUtils.getDatingList(loginUserId);

        if (list != null && list.size() > 0) {

            joinDialog = createDatingDialog(list, 0);

            if (!mActivity.isFinishing()) {

                joinDialog.show();

            }

        } else {

            mView.showDisCoverNetToast("您尚未发布任何活动，无法邀请他。请先返回到邀约页发布一条");

        }


    }


    Dialog joinDialog;

    /**
     * 不能发布约会 资料补全提示
     *
     * @param
     * @return
     */
    public Dialog createDatingDialog(List<AppointDetailDto> list, int type) {

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.chat_send_dating_hint_layout, null);

        final Dialog dialog = new Dialog(mActivity, R.style.dialog_BOT_style);

        Window window = dialog.getWindow();

        window.setGravity(Gravity.CENTER);

        window.getDecorView().setPadding(0, 0, 0, 0);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setCancelable(true);

        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(view);

        // 获得按钮
        LinearLayout invite_dating_add_layout = (LinearLayout) view.findViewById(R.id.invite_dating_add_layout);

        TextView invite_dating_title_tv = (TextView) view.findViewById(R.id.invite_dating_title_tv);

        TextView invite_dating_no_data_tv = (TextView) view.findViewById(R.id.invite_dating_no_data_tv);

        TextView invite_dating_join_me_tv = (TextView) view.findViewById(R.id.invite_dating_join_me_tv);


        if (type == 0) {

            invite_dating_title_tv.setText("邀请Ta报名我的邀约");

        } else {

            invite_dating_title_tv.setText("参加Ta的邀约建立私聊吧");
        }

        if (invite_dating_add_layout.getChildCount() > 0) {

            invite_dating_add_layout.removeAllViews();
        }

        if (list != null && list.size() > 0) {

            invite_dating_add_layout.setVisibility(View.VISIBLE);

            invite_dating_no_data_tv.setVisibility(View.GONE);

            invite_dating_join_me_tv.setVisibility(View.GONE);

            for (int i = 0; i < list.size(); i++) {

                View datingView = initInviteView(list.get(i), i, list.size(), type, userdto.getProfile().getSex());

                invite_dating_add_layout.addView(datingView);
            }

        } else {

            invite_dating_add_layout.setVisibility(View.GONE);

            invite_dating_no_data_tv.setVisibility(View.VISIBLE);

            invite_dating_join_me_tv.setVisibility(View.VISIBLE);
        }

        invite_dating_join_me_tv.setOnClickListener(v -> {

            joinDialog.dismiss();

            //邀请他参加我的邀约
            invitationAppoint();


        });

        return dialog;

    }


    private View initInviteView(final AppointDetailDto dto, int position, int length, final int type, int sex) {

        int targetSex = 3;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_invate_dating, null);


        LinearLayout invite_dating_layout = (LinearLayout) view.findViewById(R.id.invite_dating_layout);

        ImageView invite_dating_type_img_iv = (ImageView) view.findViewById(R.id.invite_dating_type_img_iv);

        ImageView invite_dating_have_chat_iv = (ImageView) view.findViewById(R.id.invite_dating_have_chat_iv);

        View invite_dating_line = view.findViewById(R.id.invite_dating_line);

        TextView invite_dating_content_tv = (TextView) view.findViewById(R.id.invite_dating_content_tv);


        if (position + 1 == length) {

            invite_dating_line.setVisibility(View.GONE);
        }


        switch (dto.getActivityType()) {
            case Constant.APPOINT_TYPE_MARRIED:

                targetSex = 0;

                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_married_icon);

                break;

            case Constant.APPOINT_TYPE_EAT:

                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_eat_icon);

                targetSex = dto.getDine().getTargetSex();

                break;

            case Constant.APPOINT_TYPE_TRAVEL:

                targetSex = InfoTransformUtils.getTravleTargetSex(dto.getTravel().getTargetObject());


                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_travel_icon);

                break;

            case Constant.APPOINT_TYPE_OTHERS:

                targetSex = dto.getOther().getTargetSex();

                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_others_icon);
                break;

            case Constant.APPOINT_TYPE_MOVIE:

                targetSex = dto.getMovie().getTargetSex();

                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_movie_icon);

                break;

            case Constant.APPOINT_TYPE_FITNESS:

                targetSex = dto.getSports().getTargetSex();

                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_fitness_icon);

                break;

            case Constant.APPOINT_TYPE_DOG:

                targetSex = dto.getWalkTheDog().getTargetSex();

                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_dog_icon);

                break;

            case Constant.APPOINT_TYPE_KTV:

                targetSex = dto.getSinging().getTargetSex();

                invite_dating_type_img_iv.setBackgroundResource(R.drawable.dating_ktv_icon);

                break;
        }


        final int target_Sex = targetSex;

        final String title = DatingUtils.getDatingTitle(dto, sex);

        invite_dating_content_tv.setText(title);


        if (!TextUtils.isEmpty(dto.getDatingId())) {

            boolean isExist = DbHelperUtils.isExistChatRecordWithDating(loginUserId + "", userID + "", dto.getDatingId());

            if (isExist) {

                invite_dating_have_chat_iv.setVisibility(View.VISIBLE);

            } else {

                invite_dating_have_chat_iv.setVisibility(View.INVISIBLE);
            }

        }


        invite_dating_layout.setOnClickListener(v -> {

            joinDialog.dismiss();

            gotoChat(dto.getDatingId(), type, target_Sex);


        });

        return view;
    }


    private void gotoChat(String datingId, int type, int targetSex) {

        boolean isExist = DbHelperUtils.isExistChatRecordWithDating(loginUserId + "", userID + "", datingId);

        if (!isExist) {

            if (type == 0) {

                DatingsInvite(datingId, String.valueOf(userID));

            } else {


                if (targetSex != 0) {



                    int userSex = DbHelperUtils.getDbUserSex(loginUserId);

                    if (userSex == targetSex) {

                        postChatRequest(userID, datingId);

                    } else {

                        mView.showDisCoverNetToast("与邀约要求性别不符");
                    }


                } else {

                    postChatRequest(userID, datingId);


                }


            }


        } else {

            Bundle bd = new Bundle();

            bd.putInt(YpSettings.USERID, userID);

            bd.putString(YpSettings.DATINGS_ID, datingId);

            mView.ChatActivity(bd);


        }

    }
}
