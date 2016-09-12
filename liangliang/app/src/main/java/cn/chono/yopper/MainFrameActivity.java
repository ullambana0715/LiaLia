package cn.chono.yopper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.orhanobut.logger.Logger;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RefreshToken.RefreshTokenBean;
import cn.chono.yopper.Service.Http.RefreshToken.RefreshTokenRespBean;
import cn.chono.yopper.Service.Http.RefreshToken.RefreshTokenService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoRespBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoService;
import cn.chono.yopper.api.HttpApi;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.App;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.task.RemoveAliasAsyncTask;
import cn.chono.yopper.utils.AppUtil;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import retrofit2.http.HTTP;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainFrameActivity extends AppCompatActivity {

    // 功能组件
    // 主框架界面顶部标题布局
    private LinearLayout main_frame_titlelayout;

    private LinearLayout main_btnGoBack_container;

    // 主框架顶部左侧返回按钮
    private ImageView btnGoBack;

    private LinearLayout main_frame_tvTitle_layout;

    private TextView tvTitle;

    private TextView tvBack;

    private LinearLayout main_frame_btnOption_containers;

    private ImageView btnOption;

    private TextView tvOption;

    private LinearLayout mainLayout;

    // 本地缓存数据
    private LayoutInflater mInflater;

    public boolean isNeedInitContext = false;

    protected CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 内容部分

        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.main_frame);

        if (isNeedInitContext) {
            ContextUtil.init(this);
        }

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(LoginUser.getInstance().getAuthToken())) {
            refreshToken(this, LoginUser.getInstance().getAuthToken());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /**
     * *************************************************************************
     * * 取组件
     */

    public LinearLayout getTitleLayout() {
        main_frame_titlelayout = (LinearLayout) findViewById(R.id.main_frame_titlelayout);
        return main_frame_titlelayout;
    }

    public LinearLayout getGoBackLayout() {
        main_btnGoBack_container = (LinearLayout) findViewById(R.id.main_frame_btnGoBack_container);
        return main_btnGoBack_container;
    }

    public ImageView getBtnGoBack() {
        btnGoBack = (ImageView) findViewById(R.id.main_frame_btnGoBack);
        return btnGoBack;
    }

    public LinearLayout getTvTitleLayout() {
        main_frame_tvTitle_layout = (LinearLayout) findViewById(R.id.main_frame_tvTitle_layout);
        return main_frame_tvTitle_layout;
    }

    public TextView gettvBack() {
        tvBack = (TextView) findViewById(R.id.main_frame_tvback);
        return tvBack;
    }

    public TextView getTvTitle() {
        tvTitle = (TextView) findViewById(R.id.main_frame_tvTitle);
        return tvTitle;
    }

    public LinearLayout getOptionLayout() {
        main_frame_btnOption_containers = (LinearLayout) findViewById(R.id.main_frame_btnOption_containers);
        return main_frame_btnOption_containers;
    }

    public ImageView getBtnOption() {
        btnOption = (ImageView) findViewById(R.id.main_frame_btnOption);
        return btnOption;
    }

    public TextView gettvOption() {
        tvOption = (TextView) findViewById(R.id.main_frame_tvOption);
        return tvOption;
    }

    public LinearLayout getMainLayout() {
        mainLayout = (LinearLayout) findViewById(R.id.main_frame_layout);
        return mainLayout;
    }

    /**
     * 设置布局内容
     *
     * @param LayoutID
     */
    public View setContentLayout(int LayoutID) {
        if (LayoutID == -1) {
            return null;
        }
        View contextView = mInflater.inflate(LayoutID, null);
        getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return contextView;
    }


    /**
     * 隐藏软键盘 hideSoftInputView
     *
     * @param
     * @return void 返回类型
     * @throws
     * @Title: hideSoftInputView
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 刷新
     *
     * @param @param context 设定文件
     * @return void 返回类型
     * @throws
     * @Title: refreshToken
     */
    private void refreshToken(final Context context, String token) {

        final long expiration = AppUtil.getRefreshTokenExpiration(context);


        long totalTime = (long) (expiration - (1 * 1000 * 60 * 60 * 24 * 10));// 过期前的时间

        final long current = System.currentTimeMillis();

        if (current < totalTime) {
            return;
        }

        if (current == expiration || current > expiration || TextUtils.isEmpty(token)) {

            RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();
            loginAsyncTask.execute("登录已过期，请重新登录");

            return;
        }


        RefreshTokenBean refreshTokenBean = new RefreshTokenBean();
        refreshTokenBean.setRefreshToken(token);

        RefreshTokenService refreshTokenService = new RefreshTokenService(this);
        refreshTokenService.parameter(refreshTokenBean);
        refreshTokenService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                RefreshTokenRespBean refreshTokenRespBean = (RefreshTokenRespBean) respBean;
                LoginUser loginUser = refreshTokenRespBean.getResp();
                LoginUser.getInstance().setLoginUser(loginUser);
                AppUtil.setRefreshTokenExpiration(MainFrameActivity.this, loginUser.getExpiration());

            }
        }, new OnCallBackFailListener());

        refreshTokenService.enqueue();
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    public void getUserInfo(final int userid) {

        double latitude = 0;
        double longtitude = 0;
        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            latitude = myLoc.getLoc().getLatitude();
            longtitude = myLoc.getLoc().getLongitude();
        }

        LatLng pt = new LatLng(latitude, longtitude);

        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        Double lat = null;
        Double log = null;

        if (latitude != 0 && longtitude != 0 && latitude != longtitude) {

            lat = pt.latitude;
            log = pt.longitude;

        }


        Subscription subscription = HttpFactory.getHttpApi()
                .getUserInfo(userid, true, true, true, false, true, true, true, true, true, true, true, true, true, lat, log)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(userdto -> {

                    Logger.e(userdto.toString());

                    String jsonstr = JsonUtils.toJson(userdto);

                    // 保存数据
                    DbHelperUtils.saveUserInfo(userid, jsonstr);
                    // 保存数据
                    DbHelperUtils.saveBaseUser(userid, userdto);


                }, throwable -> {


                });


        addSubscrebe(subscription);

    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }



}
