package cn.chono.yopper.activity.register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lidroid.xutils.util.LogUtils;
import com.orhanobut.logger.Logger;
import com.tencent.TIMCallBack;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.Login3rd.Login3rdBean;
import cn.chono.yopper.Service.Http.Login3rd.Login3rdDto;
import cn.chono.yopper.Service.Http.Login3rd.Login3rdLogicService;
import cn.chono.yopper.Service.Http.Login3rd.Login3rdRespBean;
import cn.chono.yopper.Service.Http.LoginVCode.LoginVcodeBean;
import cn.chono.yopper.Service.Http.LoginVCode.LoginVcodeRespBean;
import cn.chono.yopper.Service.Http.LoginVCode.LoginVcodeService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.SubitUserInfo.SubitUserInfoBean;
import cn.chono.yopper.Service.Http.SubitUserInfo.SubitUserInfoService;
import cn.chono.yopper.Service.Http.SubmitUser3rdInfo.SubitUser3rdInfoBean;
import cn.chono.yopper.Service.Http.SubmitUser3rdInfo.SubitUser3rdInfoService;
import cn.chono.yopper.Service.Http.UploadingUserHeadImage.UploadingUserHeadImgBean;
import cn.chono.yopper.Service.Http.UploadingUserHeadImage.UploadingUserHeadImgRespBean;
import cn.chono.yopper.Service.Http.UploadingUserHeadImage.UploadingUserHeadImgService;
import cn.chono.yopper.activity.base.IndexActivity;
import cn.chono.yopper.activity.base.SelectEntryActivity;
import cn.chono.yopper.common.Constants;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.Profile;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.im.OfflinePushUtils;
import cn.chono.yopper.im.PushUtil;
import cn.chono.yopper.im.imEvent.MessageEvent;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.CropDirectionUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.view.cropper.CopperData;
import me.nereo.multi_image_selector.MultiImageSelector;

public class RegisterWriteInfoActivity extends MainFrameActivity implements OnClickListener, OnTouchListener, TIMCallBack {
    /**
     * 头像
     */
    private ImageView usre_icon_iv;

    /**
     * 输入昵称
     */
    private EditText register_nickname_et;
    /**
     * 选择星座
     */
    private TextView register_vconstellation_tv;
    /**
     * 男
     */
    private LinearLayout sex_male_layout;

    private ImageView sex_male_icon_iv;

    private TextView sex_male_tv;

    /**
     * 女
     */
    private LinearLayout sex_female_layout;

    private ImageView sex_female_icon_iv;

    private TextView sex_female_tv;


    /**
     * 下一步
     */
    private Button register_write_info_next_but;

    private String mobile;

    private String verifyCode;

    private Dialog photoDialog;

    private Dialog netDialog;

    private Dialog loadingDiaog;


    /**
     * 头像地址
     */
    private String albumImg;

    private String headImg;

    private double _latitude;

    private double _longtitude;

    /**
     * 星座
     */
    private int horoscope = 0;
    /**
     * 性别
     */
    private int sex = 0;
    /**
     * 呢称
     */
    private String name;

    private CopperData copperData;

    private BitmapPool mPool;

    private CropCircleTransformation transformation;

    private RelativeLayout register_info_layout;

    private Dialog imgdialog;

    private LocInfo myLoc;

    private boolean reigister_next_but_control = true;

    private int vendor;

    private String openId;

    private String token;

    private String nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.register_write_info_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        PushAgent.getInstance(this).onAppStart();

        receiveData();// 接收传上一个界面的数据

        initView();// 组件初始化

        initLoadingDialog();// dialog的初始化


    }

    private void getLocinfo() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                int count = 0;
                while (true) {
                    boolean isExist = Loc.IsLocExist();
                    if (isExist) {
                        break;
                    }
                    count++;
                    if (count > 9) {
                        break;
                    }
                    SystemClock.sleep(1000);
                }

                myLoc = Loc.getLoc();
                if (myLoc != null && myLoc.getLoc() != null) {
                    LocHandler.sendEmptyMessage(1);

                } else {
                    LocHandler.sendEmptyMessage(0);
                }
            }
        }).start();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MobclickAgent.onPageStart(getString(R.string.register_write_info_title)); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        MobclickAgent.onResume(this); // 统计时长

        Loc.sendLocControlMessage(true);

        getLocinfo();


    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.register_write_info_title)); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
        Loc.sendLocControlMessage(false);
    }

    /**
     * 组件初始化
     */
    private void initView() {

        SharedprefUtil.saveBoolean(this, Constants.AUTO_RECONNECT, true);

        register_info_layout = (RelativeLayout) findViewById(R.id.register_info_layout);

        usre_icon_iv = (ImageView) findViewById(R.id.usre_icon_iv);

        register_nickname_et = (EditText) findViewById(R.id.register_nickname_et);

        register_vconstellation_tv = (TextView) findViewById(R.id.register_vconstellation_tv);


        sex_male_layout = (LinearLayout) findViewById(R.id.sex_male_layout);
        sex_male_icon_iv = (ImageView) findViewById(R.id.sex_male_icon_iv);
        sex_male_tv = (TextView) findViewById(R.id.sex_male_tv);

        sex_female_layout = (LinearLayout) findViewById(R.id.sex_female_layout);
        sex_female_icon_iv = (ImageView) findViewById(R.id.sex_female_icon_iv);
        sex_female_tv = (TextView) findViewById(R.id.sex_female_tv);

        register_write_info_next_but = (Button) findViewById(R.id.register_write_info_next_but);

        register_info_layout.setOnTouchListener(this);
        register_info_layout.setOnClickListener(this);

        usre_icon_iv.setOnClickListener(this);
        register_vconstellation_tv.setOnClickListener(this);

        sex_male_layout.setOnClickListener(this);
        sex_female_layout.setOnClickListener(this);

        register_write_info_next_but.setOnClickListener(this);


        mPool = Glide.get(this).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);


        // 设置标题栏
        getTvTitle().setText(R.string.register_write_info_title);
        getBtnGoBack().setVisibility(View.GONE);
        getBtnOption().setVisibility(View.GONE);


        register_nickname_et.setFilters(new InputFilter[]{filter});

        if (!TextUtils.isEmpty(nickname)) {
            register_nickname_et.setText(nickname);
            name = nickname;
        }

        register_nickname_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s)) {
                    name = s.toString().trim();

                } else {
                    name = "";
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    name = s.toString().trim();
                } else {
                    name = "";
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                setNextButtonStyle();

            }
        });


    }

    /**
     * 接收传上一个界面的数据
     */
    private void receiveData() {

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            mobile = bundle.getString("mobile");
            verifyCode = bundle.getString("verifyCode");

            if (bundle.containsKey("vendor")) {
                vendor = bundle.getInt("vendor");
            }

            if (bundle.containsKey("openId")) {
                openId = bundle.getString("openId");
            }

            if (bundle.containsKey("token")) {
                token = bundle.getString("token");
            }

            if (bundle.containsKey("nickname")) {
                nickname = bundle.getString("nickname");
            }

        }

    }

    /**
     * 设置头像是对dialog的初始化
     */
    private void initLoadingDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout loadingview = (LinearLayout) inflater.inflate(R.layout.loading_dialog, null);

        loadingDiaog = DialogUtil.hineDialog(RegisterWriteInfoActivity.this, loadingview);
    }

    /**
     * 定位
     */
    private Handler LocHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {
                if (msg.what == 0) {// 没有定位

                } else if (msg.what == 1) {// 找到位置

                    _latitude = myLoc.getLoc().getLatitude();
                    _longtitude = myLoc.getLoc().getLongitude();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_info_layout:

                hideSoftInputView();

                break;

            case R.id.usre_icon_iv:// 头像

                MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "btn_register_select_head_image_begin");

                copperData = new CopperData();


                photoDialog = DialogUtil.createPhotoDialog(RegisterWriteInfoActivity.this, new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        photoDialog.dismiss();


                        MultiImageSelector.create()
                                .showCamera(true) // 是否显示相机. 默认为显示
                                .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                                .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                                .start(RegisterWriteInfoActivity.this, YpSettings.ICON_REQUEST_IMAGE);


                    }
                });

                if (!isFinishing()) {
                    photoDialog.show();
                }

                break;
            case R.id.register_vconstellation_tv:// 选择星座

                MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "reg_select_star");

                Bundle consBu = new Bundle();
                consBu.putInt("horoscope", horoscope);

                Intent consIn = new Intent();
                consIn.setClass(RegisterWriteInfoActivity.this, ConstellationActivity.class);
                consIn.putExtras(consBu);

                startActivityForResult(consIn, YpSettings.FLAG_CONSTELLATION);


                break;
            case R.id.sex_male_layout:// 男

                hideSoftInputView();

                sex = 1;

                sex_female_icon_iv.setImageResource(R.drawable.sex_female_icon_normal);
                sex_female_tv.setTextColor(this.getResources().getColor(R.color.color_b2b2b2));

                sex_male_icon_iv.setImageResource(R.drawable.sex_male_icon);
                sex_male_tv.setTextColor(this.getResources().getColor(R.color.color_3187fc));

                netDialog = DialogUtil.createHintOperateDialog(RegisterWriteInfoActivity.this, "", "注册成功后，性别不可以修改", "", "确定", backCallListener);
                if (!isFinishing()) {
                    netDialog.show();
                }
                setNextButtonStyle();

                break;
            case R.id.sex_female_layout:// 女

                hideSoftInputView();

                sex = 2;

                sex_female_icon_iv.setImageResource(R.drawable.sex_female_icon);
                sex_female_tv.setTextColor(this.getResources().getColor(R.color.color_f858c3));

                sex_male_icon_iv.setImageResource(R.drawable.sex_male_icon_normal);
                sex_male_tv.setTextColor(this.getResources().getColor(R.color.color_b2b2b2));


                netDialog = DialogUtil.createHintOperateDialog(RegisterWriteInfoActivity.this, "", "注册成功后，性别不可以修改", "", "确定", backCallListener);
                if (!isFinishing()) {
                    netDialog.show();
                }
                setNextButtonStyle();
                break;
            case R.id.register_write_info_next_but:// 进入俩俩

                hideSoftInputView();
                name = register_nickname_et.getText().toString().trim();

                if (TextUtils.isEmpty(albumImg)) {

                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "请上传头像");


                } else if (TextUtils.isEmpty(name)) {

                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "请输入昵称");


                } else if (name.length() < 2) {

                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "用户昵称请输入2-16个字符");


                } else if (horoscope == 0) {

                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "请选择星座");


                } else if (sex == 0) {

                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "请选择性别");


                } else {

                    if (!reigister_next_but_control) {
                        return;
                    }

                    // 友盟自定义事件统计
                    MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "btn_reg_submit");

                    reigister_next_but_control = true;
                    douploadingUserHeadImg(albumImg);

                }

                break;

            default:
                break;
        }

    }

    private final int maxLen = 16;
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;

            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                return dest.subSequence(0, dindex - 1);
            }

            int sindex = 0;
            while (count <= maxLen && sindex < source.length()) {
                char c = source.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                sindex--;
            }

            return source.subSequence(0, sindex);
        }


    };


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case YpSettings.ICON_REQUEST_IMAGE:// 拍照


                if (resultCode == RESULT_OK) {

                    List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);


                    for (String p : mSelectPath) {

                        Bitmap bm = ImgUtils.resizesBitmap(p);
                        if (null != bm) {
                            // 保存在自己定义文件的路径
                            String file_Path = ImgUtils.saveImgFile(RegisterWriteInfoActivity.this, bm);
                            // 回收内存空间
                            bm.recycle();
                            // 去剪切
                            CropDirectionUtil.cropDirection(RegisterWriteInfoActivity.this, HeadImgCompileActivity.class, YpSettings.HEAD_IMG_REGISTER, file_Path);
                        } else {
                            DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "选取失败，请重新选择上传！");
                        }

                    }
                }

                setNextButtonStyle();
                break;

            case YpSettings.HEAD_IMG_REGISTER:// 剪切回来

                if (data != null) {

                    copperData = (CopperData) data.getExtras().getSerializable("copperData");

                    if (copperData != null) {

                        MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "btn_register_select_head_image_succeed");

                        albumImg = copperData.getCroppedImage();

                        Logger.e(albumImg);

                        Glide.with(RegisterWriteInfoActivity.this).load(albumImg).diskCacheStrategy(DiskCacheStrategy.NONE).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "选取失败，请重新选择上传！");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }

                        }).bitmapTransform(transformation).into(usre_icon_iv);

                    } else {

                        DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "选取失败，请重新选择上传！");

                    }

                } else {

                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "选取失败，请重新选择上传！");
                }

                setNextButtonStyle();

                break;

            case YpSettings.FLAG_CONSTELLATION:

                if (data != null) {
                    horoscope = data.getExtras().getInt("horoscope");
                    if (0 != horoscope) {
                        register_vconstellation_tv.setText(CheckUtil.ConstellationMatching(horoscope));
                        register_vconstellation_tv.setTextColor(this.getResources().getColor(R.color.color_333333));
                    }

                }

                setNextButtonStyle();

                break;

            default:
                break;

        }

    }

    private BackCallListener ImgbackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                imgdialog.dismiss();
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                imgdialog.dismiss();
            }

        }
    };

    /**
     * 头像上传中
     *
     * @param filePath
     */
    private void douploadingUserHeadImg(String filePath) {
        MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "upload_head_image_begin");
        if (!isFinishing()) {
            loadingDiaog.show();

        }

        UploadingUserHeadImgBean imgBean = new UploadingUserHeadImgBean();
        imgBean.setFilePath(filePath);
        imgBean.setSaveToAlbum(true);

        UploadingUserHeadImgService uploadingUserHeadImgService = new UploadingUserHeadImgService(this);
        uploadingUserHeadImgService.parameter(imgBean);
        uploadingUserHeadImgService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                UploadingUserHeadImgRespBean imgRespBean = (UploadingUserHeadImgRespBean) respBean;

                UploadingUserHeadImgRespBean.HeadImgUrl imagurl = imgRespBean.getResp();
                albumImg = imagurl.getHeadImgUrl();
                headImg = imagurl.getHeadImgUrl();

                if (0 == vendor) {
                    submitUserInfo();
                } else {
                    submitUser3rdInfo(vendor);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                reigister_next_but_control = true;
                MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "upload_head_image_defeat");

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this);
                } else {
                    netDialog = DialogUtil.createHintOperateDialog(RegisterWriteInfoActivity.this, "", msg, "", "确定", backCallListener);
                    if (!isFinishing()) {
                        netDialog.show();
                    }
                }

            }
        });

        uploadingUserHeadImgService.enqueue();


    }

    private void submitUserInfo() {


        SubitUserInfoBean infoBean = new SubitUserInfoBean();
        infoBean.setVerifyCode(verifyCode);
        infoBean.setMobile(mobile);
        infoBean.set_latitude(_latitude);
        infoBean.set_longtitude(_longtitude);
        infoBean.setAlbumImg(albumImg);
        infoBean.setHashedPassword(null);//密码已经没有了
        infoBean.setHeadImg(headImg);
        infoBean.setHoroscope(horoscope);
        infoBean.setName(name);
        infoBean.setSex(sex);


        SubitUserInfoService subitUserInfoService = new SubitUserInfoService(this);
        subitUserInfoService.parameter(infoBean);
        subitUserInfoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loginVcode(mobile, verifyCode);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "register_api_defeat");

                reigister_next_but_control = true;
                loadingDiaog.dismiss();
                String msg = respBean.getMsg();

                if (msg == null) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this);
                } else {
                    netDialog = DialogUtil.createHintOperateDialog(RegisterWriteInfoActivity.this, "", msg, "", "确定", backCallListener);
                    if (!isFinishing()) {
                        netDialog.show();
                    }

                }

            }
        });


        subitUserInfoService.enqueue();


    }


    /**
     * 验证码登陆哦
     */
    public void loginVcode(String mobile, String verifyCode) {


        LoginVcodeBean loginVcodeBean = new LoginVcodeBean();
        loginVcodeBean.setMobile(mobile);
        loginVcodeBean.setVerifyCode(verifyCode);

        LoginVcodeService loginVcodeService = new LoginVcodeService(RegisterWriteInfoActivity.this);
        loginVcodeService.parameter(loginVcodeBean);
        loginVcodeService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "reg_login_API_succeed");

                LoginVcodeRespBean loginVcodeRespBean = (LoginVcodeRespBean) respBean;


                LoginUser loginUser = loginVcodeRespBean.getResp();

                if (loginUser != null) {

                    setUserInfoJump(loginUser);

                } else {
                    loadingDiaog.dismiss();
                    reigister_next_but_control = true;
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                reigister_next_but_control = true;
                MobclickAgent.onEvent(RegisterWriteInfoActivity.this, "reg_login_API_defeat");
                String msg = respBean.getMsg();
                if (!TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, msg);
                } else {
                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, msg);
                }

                ActivityUtil.jump(RegisterWriteInfoActivity.this, SelectEntryActivity.class, null, 2, 100);
            }
        });
        loginVcodeService.enqueue();
    }


    private Dialog loginImErrorDialog;

    private void loginIm() {

        //登录之前要初始化群和好友关系链缓存
        if (YpSettings.isTest) {
            LoginBusiness.loginIm(LoginUser.getInstance().getUserId() + "@test", LoginUser.getInstance().getUserSig(), this);
        } else {
            LoginBusiness.loginIm(LoginUser.getInstance().getUserId() + "", LoginUser.getInstance().getUserSig(), this);
        }

    }


    @Override
    public void onError(int i, String s) {
        loadingDiaog.dismiss();
        reigister_next_but_control = true;

//        String msg = "";
//        switch (i) {
//            case 6208:
//                //离线状态下被其他终端踢下线
//                msg = getString(R.string.kick_logout);
//                break;
//            default:
//                msg = getString(R.string.login_error);
//                break;
//        }

        DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, "登录失败，请稍后重试");

        ActivityUtil.jump(RegisterWriteInfoActivity.this, SelectEntryActivity.class, null, 2, 100);

    }

    @Override
    public void onSuccess() {

        loadingDiaog.dismiss();
        reigister_next_but_control = true;

        AddAliasAsyncTask addAliasAsyncTask = new AddAliasAsyncTask();
        addAliasAsyncTask.execute();

        Logger.e("登录成功");
        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();

        OfflinePushUtils.initPush(RegisterWriteInfoActivity.this);

        Bundle bundle = new Bundle();
        // 需要清理task 所以传入flagtype=2
        ActivityUtil.jump(RegisterWriteInfoActivity.this, IndexActivity.class, bundle, 2, 0);
    }


    private void setUserInfoJump(LoginUser loginUser) {

        LoginUser.getInstance().setLoginUser(loginUser);

        AppUtil.setRefreshTokenExpiration(this, loginUser.getExpiration());

        UserDto userInfo = new UserDto();
        Profile profile = new Profile();
        profile.setSex(sex);
        profile.setId(loginUser.getUserId());
        profile.setName(name);
        userInfo.setProfile(profile);

        String jsonstr = JsonUtils.toJson(userInfo);

        if (null != userInfo) {
            // 保存数据
            DbHelperUtils.saveUserInfo(loginUser.getUserId(), jsonstr);
            // 保存数据
            DbHelperUtils.saveBaseUser(loginUser.getUserId(), userInfo);
        }

        loginIm();
    }


    private void submitUser3rdInfo(final int vendor) {

        SubitUser3rdInfoBean user3rdInfoBean = new SubitUser3rdInfoBean();
        user3rdInfoBean.setVendor(vendor);
        user3rdInfoBean.setOpenId(openId);
        user3rdInfoBean.setAccessToken(token);
        user3rdInfoBean.setHeadImg(headImg);
        user3rdInfoBean.setHoroscope(horoscope);
        user3rdInfoBean.setLat(_latitude);
        user3rdInfoBean.setLng(_longtitude);
        user3rdInfoBean.setName(name);
        user3rdInfoBean.setSex(sex);

        SubitUser3rdInfoService user3rdInfoService = new SubitUser3rdInfoService(this);

        user3rdInfoService.parameter(user3rdInfoBean);

        user3rdInfoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                login3RD(vendor, "", openId, token);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                reigister_next_but_control = true;
                loadingDiaog.dismiss();
                String msg = respBean.getMsg();

                if (msg == null) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this);
                } else {
                    netDialog = DialogUtil.createHintOperateDialog(RegisterWriteInfoActivity.this, "", msg, "", "确定", backCallListener);
                    if (!isFinishing()) {
                        netDialog.show();
                    }

                }

            }
        });
        user3rdInfoService.enqueue();

    }

    private void login3RD(int vendor, final String code, String openId, String token) {

        LogUtils.e("vendor=" + vendor);
        LogUtils.e("code=" + code);
        LogUtils.e("openId=" + openId);
        LogUtils.e("token=" + token);

        Login3rdBean login3rdBean = new Login3rdBean();

        login3rdBean.setVendor(vendor);

        login3rdBean.setCode(code);

        login3rdBean.setOpenId(openId);

        login3rdBean.setToken(token);


        Login3rdLogicService login3rdService = new Login3rdLogicService(this);

        login3rdService.parameter(login3rdBean);

        login3rdService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                Login3rdRespBean login3rdRespBean = (Login3rdRespBean) respBean;

                Login3rdDto login3rdDto = login3rdRespBean.getResp();
                if (null == login3rdDto) {

                    loadingDiaog.dismiss();
                    reigister_next_but_control = true;
                    return;


                }

                LoginUser loginUser = new LoginUser();

                loginUser.setAuthToken(login3rdDto.getAuthToken());
                loginUser.setRefreshToken(login3rdDto.getRefreshToken());
                loginUser.setUserId(login3rdDto.getUserId());
                loginUser.setExpiration(login3rdDto.getExpiration());
                loginUser.setUserSig(login3rdDto.getUserSig());

                if (null != loginUser) {
                    setUserInfoJump(loginUser);
                } else {
                    loadingDiaog.dismiss();
                    reigister_next_but_control = true;
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();
                reigister_next_but_control = true;

                String msg = respBean.getMsg();
                if (!TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this, msg);

                } else {

                    DialogUtil.showDisCoverNetToast(RegisterWriteInfoActivity.this);

                }

                ActivityUtil.jump(RegisterWriteInfoActivity.this, SelectEntryActivity.class, null, 2, 100);


            }
        });

        login3rdService.enqueue();
    }


    public class AddAliasAsyncTask extends AsyncTask<Void, Integer, String> {

        // 该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
        // 主要用于进行异步操作
        @Override
        protected String doInBackground(Void... params) {

            try {
                PushAgent mPushAgent = PushAgent.getInstance(RegisterWriteInfoActivity.this);
                mPushAgent.removeAlias(LoginUser.getInstance().getUserId() + "", "chono");
                mPushAgent.addAlias(LoginUser.getInstance().getUserId() + "", "chono");
            } catch (Exception e) {
                e.printStackTrace();
            }

            TalkingDataAppCpa.onLogin(LoginUser.getInstance().getUserId() + "");

            return null;

        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideSoftInputView();
        return false;
    }

    private BackCallListener backCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                netDialog.dismiss();
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                netDialog.dismiss();
            }

        }
    };


    private void setNextButtonStyle() {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(albumImg) && 0 != sex && 0 != horoscope) {//可以下一步
            register_write_info_next_but.setEnabled(true);

        } else {//不可以进入
            register_write_info_next_but.setEnabled(false);
        }


    }

}
