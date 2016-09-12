package cn.chono.yopper.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.duanqu.qupai.auth.AuthService;
import com.duanqu.qupai.auth.QupaiAuthListener;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.StartActivity;
import cn.chono.yopper.activity.video.VideoMusicActivity;
import cn.chono.yopper.api.HttpApi;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.im.Foreground;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocBaidu;
import cn.chono.yopper.tencent.TencentShareUtil;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.video.VideoContant;
import cn.chono.yopper.utils.video.VideoFileUtils;
import cn.chono.yopper.weibo.SinaWeiBoUtil;
import cn.chono.yopper.wxapi.WeixinUtils;

public class App extends MultiDexApplication {

//    public static boolean isActive = false;//全局变量isActive = false 记录当前已经进入后台
    public static final int VERSION_DB = 4;

    //Application对象

    public static App instance;
    public File cacheDir;

    public static HttpApi mHttpApi;

    private static Context context;


    public static DbUtils db;

    /**
     * 单例模式中获取唯一的YPApplication实例
     */
    public synchronized static App getInstance() {
        if (null == instance) {
            instance = new App();
        }
        return instance;

    }


    public static Context getContext() {
        return context;
    }



    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Foreground.init(this);

        ContextUtil.init(this);
        instance = this;

        setupGlide(this);// 配制图片加载属性

        SinaWeiBoUtil.initweibo(this);

        SinaWeiBoUtil.registerWeibo();

        TencentShareUtil.initTencent(this);
        // 微信
        WeixinUtils.initWeixin(this);

        WeixinUtils.regWeixin();

        DialogUtil.setContext(getApplicationContext());

        db = setDbUitls();

        YpSettings.gBaiduAvailable = true;

        try {
            // 初始化百度地图
            SDKInitializer.initialize(getApplicationContext());

        } catch (Exception e) {
            YpSettings.gBaiduAvailable = false;
        }

        if (YpSettings.gBaiduAvailable) {
            // 初始化百度地图定位
            try {
                LocBaidu.init(this);
            } catch (Exception e) {
                try {
                    LocBaidu.init(this);
                } catch (Exception e2) {
                    YpSettings.gBaiduAvailable = false;
                }
            }
        }

        // 位置服务
        Loc.ini(this);

        Loc.sendLocControlMessage(true);

        // // 趣拍初始化和配制
        initVideoQupaiService(this);

        umengDeal();

        Intent intent = new Intent("cn.chono.yopper.umeng.msg");

        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        registerReceiver(umengHandleAlarmReceiver, new IntentFilter("cn.chono.yopper.umeng.msg"));

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 1000, sender);


    }


    private umengHandleAlarmReceiver umengHandleAlarmReceiver = new umengHandleAlarmReceiver();


    private class umengHandleAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            umengDeal();
        }
    }


    private HttpApi getHttpApi() {
        if (mHttpApi == null) {
            mHttpApi = HttpFactory.getHttpApi();
        }
        return mHttpApi;
    }


    private void umengDeal() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                String code = msg.extra.get("code").toString();
                String navigate = msg.extra.get("navigate").toString();
                if (code != null && code.equals("ArticleView")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("ArticleView", navigate);
                    ActivityUtil.jump(context, StartActivity.class, bundle, 2, 100);
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);


        // 只能在主进程进行离线推送监听器注册
        if(MsfSdkUtils.isMainProcess(this)) {

            // 设置离线推送监听器
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {

                    Logger.e("lailaiaiaiiaia===============");
                    // 这里的doNotify是ImSDK内置的通知栏提醒，应用也可以选择自己利用回调参数notification来构造自己的通知栏提醒
                    notification.doNotify(getApplicationContext(), R.drawable.application_icon);
                }
            });
        }

    }

    /**
     * 图片加载配制（自定义缓存路径）
     */
    private void setupGlide(Context context) {

        OkHttpClient mOkHttpClient = new OkHttpClient();

        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(mOkHttpClient));

        cacheDir = Glide.getPhotoCacheDir(context);
    }


    /**
     * Sqlist工具
     *
     * @return
     */
    private DbUtils setDbUitls() {
        String sdPath = this.getFilesDir().getPath() + "/Yopper/database/";
        DbUtils db = DbUtils.create(this, sdPath, "yopper.db", VERSION_DB,
                new DbUpgradeListener() {

                    @Override
                    public void onUpgrade(DbUtils arg0, int oldVersion, int newVersion) {

                    }
                });
        return db;
    }


    /**
     * 趣拍对象（只有初始化成功后对象才存在）；使用时必须自我判断对象是否存在。 qupaiService ！=null;
     */

    public static QupaiService qupaiService;

    /**
     * 趣拍的初始化和配制
     *
     * @param context
     * @return
     */
    private void initVideoQupaiService(Context context) {


        AuthService service = AuthService.getInstance();

        service.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                Logger.e("errorCode=" + errorCode + "=message=" + message);
            }

            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {

                Logger.e("趣拍初始化成功");
                VideoContant.accessToken = responseMessage;//鉴权完成返回accessToke
            }
        });

        service.startAuth(context, VideoContant.APP_KEY, VideoContant.APP_SECRET, VideoContant.space);


        qupaiService = QupaiManager.getQupaiService(context);


        UISettings _UISettings = new UISettings() {

            @Override
            public boolean hasEditor() {
                return true;//是否需要编辑功能
            }

            @Override
            public boolean hasImporter() {
                return false;//是否需要导入功能
            }

            @Override
            public boolean hasGuide() {
                return false;//是否启动引导功能，建议用户第一次使用时设置为true
            }


            @Override
            public boolean hasSkinBeautifer() {
                return true;//是否显示美颜图标
            }
        };

        //压缩参数
        MovieExportOptions movie_options = new MovieExportOptions.Builder()
                .setVideoBitrate(2000 * 1000)
                .configureMuxer("movflags", "+faststart")
                .build();

        //输出视频的参数
        ProjectOptions projectOptions = new ProjectOptions.Builder()
                //输出视频宽高目前只能设置1：1的宽高，建议设置480*480.
                .setVideoSize(480, 480)
                //帧率
                .setVideoFrameRate(30)
                //时长区间
                .setDurationRange(2, 8)
                .get();

        //缩略图参数,可设置取得缩略图的数量，默认10张
        ThumbnailExportOptions thumbnailExportOptions = new ThumbnailExportOptions.Builder().setCount(10).get();

        VideoSessionCreateInfo info = new VideoSessionCreateInfo.Builder()
                //水印地址，如"assets://Qupai/watermark/qupai-logo.png"
                .setWaterMarkPath("")
                //水印的位置
                .setWaterMarkPosition(0)
                //摄像头方向,可配置前置或后置摄像头
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
                //美颜百分比,设置之后内部会记住，多次设置无效
                .setBeautyProgress(80)
                //默认是否开启
                .setBeautySkinOn(true)
                .setMovieExportOptions(movie_options)
                .setThumbnailExportOptions(thumbnailExportOptions)
                .build();

        //初始化，建议在application里面做初始化，这里做是为了方便开发者认识参数的意义
        qupaiService.initRecord(info, projectOptions, _UISettings);


        if (qupaiService != null) {
            qupaiService.addMusic(99999999, "情窦初开", "assets://Qupai/music/99999999");
            qupaiService.addMusic(99999998, "快乐女生", "assets://Qupai/music/99999998");

            Intent moreMusic = new Intent();// //是否需要更多音乐页面--如果不需要填空
            moreMusic.setClass(App.this, VideoMusicActivity.class);

            qupaiService.hasMroeMusic(moreMusic);

            new Thread() {
                @Override
                public void run() {

                    super.run();
                    List<VideoFileUtils.VideoMuiscData> muiscFileList = VideoFileUtils.saveMuiscFile(VideoFileUtils.getVideoMuiscRoot().listFiles());
                    for (VideoFileUtils.VideoMuiscData data : muiscFileList) {
                        qupaiService.addMusic(data.id, data.name, "file://" + data.muiscPath);
                    }

                }
            }.start();
        }

    }


}
