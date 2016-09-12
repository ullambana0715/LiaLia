package cn.chono.yopper.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;
import com.umeng.message.PushAgent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RefreshToken.RefreshTokenBean;
import cn.chono.yopper.Service.Http.RefreshToken.RefreshTokenRespBean;
import cn.chono.yopper.Service.Http.RefreshToken.RefreshTokenService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoRespBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoService;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.task.RemoveAliasAsyncTask;
import cn.chono.yopper.utils.AppUtil;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.JsonUtils;

/**
 *
 */
public abstract class BaseActivity<T extends IPresenter> extends AppCompatActivity  {

    protected T mPresenter;

    protected Activity mContext;

    protected Unbinder unbinder;

    protected View contentView;


    public boolean isNeedBindService = false;

    public boolean isNeedInitContext = false;

//    private Handler mHandler = new Handler();
//    private Runnable mLoadingRunnable = new Runnable() {
//
//        @Override
//        public void run() {
//            initVariables();
//            initView();
//            initDataAndLoadData();
//
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        contentView = LayoutInflater.from(this).inflate(R.layout.act_base, null);

        View contextView = LayoutInflater.from(this).inflate(getLayout(), null);

        getBaseContentLayout().addView(contextView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        onContentViewBgColor();

        setContentView(contentView);

        unbinder = ButterKnife.bind(this);

        mContext = this;

        mPresenter = getPresenter();



        if (isNeedInitContext) {
            ContextUtil.init(this);
        }

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

//        getWindow().getDecorView().post(() -> mHandler.post(mLoadingRunnable));

        initVariables();
        initView();
        initDataAndLoadData();

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


        unbinder.unbind();
        if (mPresenter != null) mPresenter.detachView();
    }


    public LinearLayout getBaseContentLayout() {

        return ButterKnife.findById(contentView, R.id.base_content_layout);
    }

    public void onContentViewBgColor() {

    }

    protected abstract int getLayout();

    protected abstract T getPresenter();

    /**
     * 初始化变量 包括intent带的数据
     */
    protected abstract void initVariables();

    /**
     * 初始化View 属性设置  初始状态等等
     */
    protected abstract void initView();

    /**
     * 初始化数据并获取数据
     */
    protected abstract void initDataAndLoadData();



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
                AppUtil.setRefreshTokenExpiration(mContext,loginUser.getExpiration());
                LoginUser.getInstance().setLoginUser(loginUser);
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


        UserInfoBean userInfoBean = new UserInfoBean();

        userInfoBean.setUserId(userid);
        userInfoBean.setDating(true);
        userInfoBean.setBubble(true);
        userInfoBean.setFriends(true);
        userInfoBean.setWish(true);
        userInfoBean.setVerification(true);
        userInfoBean.setFaceRating(true);
        userInfoBean.setNewAlbum(true);

        if (latitude != 0 && longtitude != 0 && latitude != longtitude) {
            userInfoBean.setLat(pt.latitude);
            userInfoBean.setLng(pt.longitude);
        }

        UserInfoService userInfoService = new UserInfoService(this);
        userInfoService.parameter(userInfoBean);
        userInfoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UserInfoRespBean userInfoRespBean = (UserInfoRespBean) respBean;
                UserDto userdto = userInfoRespBean.getResp();
                String jsonstr = JsonUtils.toJson(userdto);
                if (null != userdto) {
                    // 保存数据
                    DbHelperUtils.saveUserInfo(userid, jsonstr);
                    // 保存数据
                    DbHelperUtils.saveBaseUser(userid, userdto);
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

            }
        });
        userInfoService.enqueue();

    }

}
