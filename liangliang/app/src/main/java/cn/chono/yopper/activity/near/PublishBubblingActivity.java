package cn.chono.yopper.activity.near;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BubblingBubble.BubblingBubbleBean;
import cn.chono.yopper.Service.Http.BubblingBubble.BubblingBubbleRespBean;
import cn.chono.yopper.Service.Http.BubblingBubble.BubblingBubbleService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UploadingBubbleImage.UploadingBubbleImageBean;
import cn.chono.yopper.Service.Http.UploadingBubbleImage.UploadingBubbleImageRespBean;
import cn.chono.yopper.Service.Http.UploadingBubbleImage.UploadingBubbleImageService;
import cn.chono.yopper.activity.base.IndexActivity;
import cn.chono.yopper.activity.usercenter.MyBubblingActivity;
import cn.chono.yopper.adapter.PublishBubblingAdapter;
import cn.chono.yopper.adapter.PublishBubblingAdapter.OnItemClickLitener;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.NearLoc;
import cn.chono.yopper.data.NearPlaceDto;
import cn.chono.yopper.event.BubblingListEvent;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.UnitUtil;
import me.nereo.multi_image_selector.MultiImageSelector;

public class PublishBubblingActivity extends MainFrameActivity implements OnItemClickLitener, OnClickListener, OnTouchListener {

     GridView ypGridView;

     EditText editText;

     ImageView typeIcon;

     TextView typeTv;

     PublishBubblingAdapter adapter;

     Dialog photoDialog;

     Dialog netDialog;


     TextView btnGoBack_container;

     ImageView btnOption_container;



     NearPlaceDto nearPlaceDto;

     CropCircleTransformation transformation;

     BitmapPool mPool;

     LinearLayout layoutIcon;

    /**
     * 冒泡图片地址
     */
     List<String> bublingImageURL = new ArrayList<String>();

     double _latitude;

     double _longtitude;

     Dialog _loadingDiaog;

     Dialog hintdialog;

     Integer id = 0;


     HorizontalScrollView scrollView;

     String from_tag;

     LocInfo myLoc;

     int screen_weight;

    List<String> okselectedList = new ArrayList<>();

    int number=9;

     int isbublingImageUrlListSize = 0;
     List<String> bublingImageUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.publish_buling_activity);

        screen_weight = UnitUtil.getScreenWidthPixels(this);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            nearPlaceDto = (NearPlaceDto) bundle.getSerializable(YpSettings.ADDRESS_LIST_DTO);

            from_tag = SharedprefUtil.get(this, YpSettings.BUBBLING_FROM_TAG_KEY, "");
        }

        initView();

        initLoadingDialog();

        setViewData(nearPlaceDto);

        addNoImage();

    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("发布冒泡"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        MobclickAgent.onResume(this); // 统计时长

        Loc.sendLocControlMessage(true);

        getLocinfo();


        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(gridViewWidth, 0);
            }
        }), 300);

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
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("发布冒泡"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        MobclickAgent.onPause(this); // 统计时长

        Loc.sendLocControlMessage(false);
    }

    private void removeNoImage() {

        if (okselectedList != null && okselectedList.size() > 0) {

            for (int i = 0; i < okselectedList.size(); i++) {

                String urlStr = okselectedList.get(i);

                if (TextUtils.equals(urlStr, "NO_IMAGE")) {

                    okselectedList.remove(i);

                    break;
                }

            }

        }

    }


    @SuppressWarnings({"unchecked", "unused"})
    private void setViewData(NearPlaceDto nearPlaceDto) {

        if (nearPlaceDto == null) {

            return;
        }

        NearLoc loc = nearPlaceDto.getLoc();

        if (loc == null) {
            id = 0;
        } else {
            id = loc.getId();
        }

        if (id == null || id <= 0) {

            typeIcon.setBackgroundResource(R.drawable.discover_type_icon_no);

            Glide.with(this).load(R.drawable.my_loc_address_icon).into(typeIcon);

        } else {

            typeIcon.setBackgroundResource(R.drawable.discover_type_icon);


            String typeUrl = nearPlaceDto.getLoc().getTypeImgUrl();

            Glide.with(this).load(typeUrl).bitmapTransform(transformation).into(typeIcon);
        }

        if (!CheckUtil.isEmpty(nearPlaceDto.getLoc().getName())) {

            typeTv.setText(nearPlaceDto.getLoc().getName());
        }
    }

    private Handler LocHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            try {

                if (msg.what == 0) {// 定位失败

                } else if (msg.what == 1) {// 找到位置

                    _latitude = myLoc.getLoc().getLatitude();

                    _longtitude = myLoc.getLoc().getLongitude();

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    };


    /**
     * list添加+号数据
     */
    private void addNoImage() {

        if (okselectedList.size() < 9) {

            okselectedList.add("NO_IMAGE");

        }


        setGridViewLayoutParams(okselectedList.size());

        adapter.setData(okselectedList);

        adapter.notifyDataSetChanged();

        scrollView.getViewTreeObserver().addOnGlobalFocusChangeListener(
                new OnGlobalFocusChangeListener() {

                    @Override
                    public void onGlobalFocusChanged(View oldFocus,
                                                     View newFocus) {
                        // 也可以使用ScrollTo()方法 但smoothScrollTo()方法更安全
                        scrollView.smoothScrollTo(gridViewWidth, 0);
                        // 进入Activity后gridView将自动滑动 移动距离自己控制
                    }
                });
    }

    int gridViewWidth;

    private void setGridViewLayoutParams(int listSize) {

        int range = UnitUtil.dip2px(10, this);// 减去最外层的padding的值转换成px

        int MaxWidth = screen_weight - range;// 一屏显示最大的宽度

        int gridviewItmeWidth = MaxWidth / 3;// 算出单个item的最大宽度

        gridViewWidth = gridviewItmeWidth * listSize;// 根据数据list的长度最终算出gridView的宽度

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);


        ypGridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键

        ypGridView.setColumnWidth(gridviewItmeWidth); // 设置列表项宽

        ypGridView.setNumColumns(listSize); // 设置列数量=列表集合数
    }

    /**
     * 组件初始化
     */
    private void initView() {

        layoutIcon = (LinearLayout) findViewById(R.id.layoutIcon);

        ypGridView = (GridView) findViewById(R.id.ypGridView);

        editText = (EditText) findViewById(R.id.editText);

        typeIcon = (ImageView) findViewById(R.id.typeIcon);

        typeTv = (TextView) findViewById(R.id.typeTv);

        adapter = new PublishBubblingAdapter(this);

        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) scrollView
                .getLayoutParams();

        params.width = screen_weight;

        scrollView.setLayoutParams(params);

        ypGridView.setAdapter(adapter);

        adapter.setOnItemClickLitener(this);

        btnGoBack_container = (TextView) findViewById(R.id.back_tv);

        btnOption_container = (ImageView) findViewById(R.id.publish_bubbling_iv);

        mPool = Glide.get(this).getBitmapPool();

        transformation = new CropCircleTransformation(mPool);

        layoutIcon.setOnTouchListener(this);

        btnGoBack_container.setOnClickListener(this);

        btnOption_container.setOnClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (arg0.toString().length() >= 100) {

                    DialogUtil.showDisCoverNetToast(PublishBubblingActivity.this, "最多只能输入100个字哦");

                }
            }
        });
    }

    /**
     * 设置头像是对dialog的初始化
     */
    private void initLoadingDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout loadingview = (LinearLayout) inflater.inflate(R.layout.loading_dialog, null);

        _loadingDiaog = DialogUtil.hineDialog(this, loadingview);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        hideSoftInputView();

        return false;
    }

    @Override
    public void onRemoveItemClick(View view, int position) {


        okselectedList.remove(position);


        removeNoImage();

        addNoImage();

        adapter.setData(okselectedList);

        adapter.notifyDataSetChanged();

    }



    @Override
    public void onAddItemClick(View view, int position) {

        removeNoImage();

        number = 9 - okselectedList.size();


        photoDialog = DialogUtil.createPhotoDialog(this, view1 -> {

            photoDialog.dismiss();


            MultiImageSelector.create()

                    .showCamera(true) // 是否显示相机. 默认为显示

                    .count(number) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
//                    .single() // 单选模式

                    .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效

                    .start(PublishBubblingActivity.this, YpSettings.ICON_REQUEST_IMAGE);


        });

        if (!isFinishing()) {

            photoDialog.show();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_tv:// 返回（取消）

                hideSoftInputView();

                finish();

                break;
            case R.id.publish_bubbling_iv:// 发布冒泡

                hideSoftInputView();

                _loadingDiaog.show();

                text = editText.getText().toString().trim();

                if (TextUtils.isEmpty(text)) {

                    _loadingDiaog.dismiss();


                    netDialog = DialogUtil.createHintOperateDialog(PublishBubblingActivity.this, "", "发布冒泡的文字内容不能为空", "", "确定", backCallListener);

                    if (!isFinishing()) {

                        netDialog.show();

                    }

                    return;
                }

                isbublingImageUrlListSize = bublingArray();

                if (isbublingImageUrlListSize > 0) {

                    httpHandler.sendEmptyMessage(isHttpHandler);

                } else {

                    httpHandler.sendEmptyMessage(6666);
                }

                break;

            default:
                break;
        }

    }

    private int isHttpHandler = 0;

    private Handler httpHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == isHttpHandler) {

                if (bublingImageUrlList.size() > isHttpHandler) {

                    douploadingBublingImg(bublingImageUrlList.get(isHttpHandler));

                } else {

                    _loadingDiaog.dismiss();

                    DialogUtil.showDisCoverNetToast(PublishBubblingActivity.this);

                }


            } else if (msg.what == 6666) {// 上传成功后

                onBubblinghttp(id, nearPlaceDto.getLoc().getAddress(), nearPlaceDto.getLoc().getName(), bublingImageURL, text);

            }

        }

        ;
    };

    private String text;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == YpSettings.ICON_REQUEST_IMAGE) {//头像

            if (resultCode == RESULT_OK) {


                removeNoImage();

                List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                okselectedList.addAll(mSelectPath);

                addNoImage();


            } else {

                DialogUtil.showDisCoverNetToast(PublishBubblingActivity.this, "选取失败，请重新选择上传！");

            }
        }


    }



    /**
     * 上传图片 前的处理后上传图片
     */
    private int bublingArray() {

        int isbublingImageUrlListSize = 0;

        removeNoImage();

        bublingImageUrlList = new ArrayList<String>();// 最终要上传服务器的图片地址集合

        for (int i = 0; i < okselectedList.size(); i++) {// 对图片压缩处理

            String imgUrl = okselectedList.get(i);

            if (!TextUtils.isEmpty(imgUrl)) {

                try {

                    Bitmap bitmap = ImgUtils.resizesBitmap(imgUrl);

                    String bitmapUrl = ImgUtils.saveImgFile(PublishBubblingActivity.this, bitmap);

                    bublingImageUrlList.add(bitmapUrl);


                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

        }

        isbublingImageUrlListSize = bublingImageUrlList.size();

        return isbublingImageUrlListSize;

    }

    /**
     * 头像上传中
     *
     * @param filePath
     */
    private void douploadingBublingImg(String filePath) {


        UploadingBubbleImageBean bubbleImageBean = new UploadingBubbleImageBean();

        bubbleImageBean.setFilePath(filePath);

        UploadingBubbleImageService imageService = new UploadingBubbleImageService(this);

        imageService.parameter(bubbleImageBean);

        imageService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UploadingBubbleImageRespBean imageRespBean = (UploadingBubbleImageRespBean) respBean;

                String resp = imageRespBean.getResp();

                if (!TextUtils.isEmpty(resp)) {// 上传成功

                    LogUtils.e("图片" + isHttpHandler + "上传成功");

                    bublingImageURL.add(resp);

                    if (bublingImageURL.size() == isbublingImageUrlListSize) {// 判断是不是全部上传完

                        LogUtils.e("图片全部上传成功");

                        httpHandler.sendEmptyMessage(6666);

                    } else {

                        isHttpHandler = isHttpHandler + 1;

                        LogUtils.e("图片" + isHttpHandler + "开始上传");

                        httpHandler.sendEmptyMessage(isHttpHandler);

                    }
                } else {// 上传失败

                    _loadingDiaog.dismiss();

                    LogUtils.e("图片上传失败返回url=null");

                    DialogUtil.showDisCoverNetToast(PublishBubblingActivity.this);
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                _loadingDiaog.dismiss();

                LogUtils.e("图片上传失败====");

                String msg = respBean.getMsg();

                if (TextUtils.isEmpty(msg)) {

                    // 没有网络的场合，去提示页

                    DialogUtil.showDisCoverNetToast(PublishBubblingActivity.this);

                    return;

                } else {

                    DialogUtil.showDisCoverNetToast(PublishBubblingActivity.this, msg);
                }

            }
        });

        imageService.enqueue();


    }


    /**
     * 提交冒泡请求
     */

    private void onBubblinghttp(int locId, String address, String addressName,
                                List<String> imageUrls, String text) {


        LatLng pt = new LatLng(_latitude, _longtitude);

        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        String url = HttpURL.bubbling_bubble + "?check=true";


        BubblingBubbleBean bubblingBubbleBean = new BubblingBubbleBean();

        bubblingBubbleBean.setCheck(true);

        bubblingBubbleBean.setLng(pt.longitude);

        bubblingBubbleBean.setLat(pt.latitude);

        bubblingBubbleBean.setAddress(address);

        bubblingBubbleBean.setAddressName(addressName);

        bubblingBubbleBean.setLocId(locId);

        bubblingBubbleBean.setText(text);

        bubblingBubbleBean.setImageUrls(imageUrls);

        BubblingBubbleService bubblingBubbleService = new BubblingBubbleService(this);

        bubblingBubbleService.parameter(bubblingBubbleBean);

        bubblingBubbleService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {


                BubblingBubbleRespBean bubbleRespBean = (BubblingBubbleRespBean) respBean;

                _loadingDiaog.dismiss();
                // 提交成功
                hintdialog = DialogUtil.createSuccessHintDialog(PublishBubblingActivity.this, "冒泡发布成功!");

                if (!PublishBubblingActivity.this.isFinishing()) {

                    hintdialog.show();

                    successtimer = new SuccessTimer(2000, 1000);

                    successtimer.start();
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                _loadingDiaog.dismiss();

                String msg = respBean.getMsg();

                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页

                    DialogUtil.showDisCoverNetToast(PublishBubblingActivity.this);

                } else {

                    netDialog = DialogUtil.createHintOperateDialog(PublishBubblingActivity.this, "", msg, "", "确定", backCallListener);

                    if (!isFinishing()) {

                        netDialog.show();
                    }
                }


            }
        });

        bubblingBubbleService.enqueue();

    }

    private SuccessTimer successtimer;

    private class SuccessTimer extends CountDownTimer {

        public SuccessTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {

            if (hintdialog != null) {

                hintdialog.dismiss();

            }

            SharedprefUtil.saveBoolean(PublishBubblingActivity.this, YpSettings.BUBBLING_PUBLISH, true);// 保存为true，标记冒泡列表要刷新

            if (TextUtils.equals(YpSettings.BUBBLING_FROM_TAG_DISCOVER, from_tag)) {

                RxBus.get().post("BubblingListEvent", new BubblingListEvent(1));

                ActivityUtil.jump(PublishBubblingActivity.this, IndexActivity.class, null, 1, 200);

            } else if (TextUtils.equals(YpSettings.BUBBLING_FROM_TAG_USERCENTER_MY, from_tag)) {

                RxBus.get().post("BubblingListEvent", new BubblingListEvent(2));

                ActivityUtil.jump(PublishBubblingActivity.this, MyBubblingActivity.class, null, 1, 200);


            } else {

                RxBus.get().post("BubblingListEvent", new BubblingListEvent(1));

                ActivityUtil.jump(PublishBubblingActivity.this, IndexActivity.class, null, 1, 200);
            }


        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void finish() {

        if (TextUtils.equals(YpSettings.BUBBLING_FROM_TAG_DISCOVER, from_tag)) {

            ActivityUtil.jump(PublishBubblingActivity.this, IndexActivity.class, null, 1, 200);

        } else if (TextUtils.equals(YpSettings.BUBBLING_FROM_TAG_USERCENTER_MY, from_tag)) {

            ActivityUtil.jump(PublishBubblingActivity.this, MyBubblingActivity.class, null, 1, 200);

        } else {

            ActivityUtil.jump(PublishBubblingActivity.this, IndexActivity.class, null, 1, 200);
        }

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

}
