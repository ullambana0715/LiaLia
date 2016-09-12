package cn.chono.yopper.activity.order;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderFeedback.OrderFeedbackBean;
import cn.chono.yopper.Service.Http.OrderFeedback.OrderFeedbackEntity;
import cn.chono.yopper.Service.Http.OrderFeedback.OrderFeedbackRespEntity;
import cn.chono.yopper.Service.Http.OrderFeedback.OrderFeedbackService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UploadingOrderImage.UploadingOrderImageBean;
import cn.chono.yopper.Service.Http.UploadingOrderImage.UploadingOrderImageRespBean;
import cn.chono.yopper.Service.Http.UploadingOrderImage.UploadingOrderImageService;
import cn.chono.yopper.activity.base.BigPhotoActivity;
import cn.chono.yopper.adapter.UserOrderFeedBackPhotoAdapter;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.event.OrderListEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 问题反馈
 * Created by cc on 16/5/5.
 */
public class UserOrderFeedBackActivity extends MainFrameActivity implements UserOrderFeedBackPhotoAdapter.OnPhotoAdapterListener {

    RadioGroup userOrderFeedback_rg_type;

    EditText userOrderFeedback_et_feedback;

    TextView userOrderFeedback_et_et_number_tv;

    GridView userOrderFeedback_gv_photo;

    String mFeedBackContent;

    UserOrderFeedBackPhotoAdapter mUserOrderFeedBackPhotoAdapter;

    Dialog photoDialog, delete_dlg;


    String filePath;

    Integer feedbackType;

    ArrayList<String> uploadingPhotoData = new ArrayList<>();

    int isHttpHandler = 0;

    ArrayList<String> photoData;

    int photoSize;

    int uploadingOk = 1100000;

    Dialog loading_dlg;

    String orderId;


    Handler uploadingImageHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == isHttpHandler) {

                if (photoData.size() > isHttpHandler) {
                    douploadingImage(photoData.get(isHttpHandler));

                }

            } else if (msg.what == uploadingOk) {// 上传成功后

                isHttpHandler = 0;

                int uploadingPhotoDataSize = uploadingPhotoData.size();

                String[] imageUrls = new String[uploadingPhotoDataSize];

                for (int i = 0; i < uploadingPhotoDataSize; i++) {
                    imageUrls[i] = uploadingPhotoData.get(i);
                }


                postFeedBack(orderId, feedbackType, mFeedBackContent, imageUrls);
            }

        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_order_feedback);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.ORDER_ID))
            orderId = bundle.getString(YpSettings.ORDER_ID);


        initView();

        initData();
    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("反馈投诉"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长


    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("反馈投诉"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    private void initView() {

        loading_dlg = DialogUtil.LoadingDialog(this);

        getTvTitle().setText(getResources().getString(R.string.feedback));

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView optionTv = gettvOption();
        optionTv.setVisibility(View.VISIBLE);
        optionTv.setText("提交");
        getOptionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (feedbackType == null) {
                    DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, "请选择问题类型");
                    return;
                }


                if (feedbackType == 2 && TextUtils.isEmpty(mFeedBackContent)) {

                    DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, "请输入问题描述");

                    return;
                }

                photoData = mUserOrderFeedBackPhotoAdapter.getPhotoData();

                photoSize = photoData.size();

                loading_dlg.show();

                if (photoSize > 0) {//上传图片
                    uploadingImageHandler.sendEmptyMessage(isHttpHandler);

                } else {//不需要上传
                    uploadingImageHandler.sendEmptyMessage(uploadingOk);
                }

            }
        });


        userOrderFeedback_rg_type = (RadioGroup) findViewById(R.id.userOrderFeedback_rg_type);

        userOrderFeedback_et_feedback = (EditText) findViewById(R.id.userOrderFeedback_et_feedback);

        userOrderFeedback_et_et_number_tv = (TextView) findViewById(R.id.userOrderFeedback_et_et_number_tv);

        userOrderFeedback_gv_photo = (GridView) findViewById(R.id.userOrderFeedback_gv_photo);


        userOrderFeedback_et_feedback.addTextChangedListener(new AddTextWatcher());


        mUserOrderFeedBackPhotoAdapter = new UserOrderFeedBackPhotoAdapter(this, this);

        userOrderFeedback_gv_photo.setAdapter(mUserOrderFeedBackPhotoAdapter);


        userOrderFeedback_rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton feedbackButton = (RadioButton) findViewById(checkedId);
                feedbackType = (Integer) feedbackButton.getTag();
            }
        });
    }

    private void initData() {

        Resources res = getResources();

        String[] feedback = res.getStringArray(R.array.feedback);

        for (int i = 0; i < feedback.length; i++) {


            RadioButton feedbackButton = (RadioButton) LayoutInflater.from(UserOrderFeedBackActivity.this).inflate(R.layout.radiogroup_button, null);


            feedbackButton.setText(feedback[i]);


            feedbackButton.setTag(i);


            userOrderFeedback_rg_type.addView(feedbackButton);

        }


    }

    @Override
    public void OnDeleteListener(final int position, String photo) {

        delete_dlg = DialogUtil.createHintOperateDialog(UserOrderFeedBackActivity.this, "", "确认删除这张照片？", "取消", "删除", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {
                delete_dlg.dismiss();
            }

            @Override
            public void onEnsure(View view, Object... obj) {
                mUserOrderFeedBackPhotoAdapter.deleteData(position);
                delete_dlg.dismiss();
            }
        });

        delete_dlg.show();


    }

    @Override
    public void OnItemListener(int position, String photo, ArrayList<String> photoData) {

        Bundle bundle = new Bundle();

        bundle.putStringArrayList(YpSettings.PHOTO_LIST, photoData);

        bundle.putInt(YpSettings.PHOTO_SUBSCRIPT, position);


        ActivityUtil.jump(UserOrderFeedBackActivity.this, BigPhotoActivity.class, bundle, 0, 100);
    }

    @Override
    public void OnAddListener() {

        photoDialog = DialogUtil.createPhotoDialog(this, view -> {


            photoDialog.dismiss();


            MultiImageSelector.create()
                    .showCamera(true) // 是否显示相机. 默认为显示
                    .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                    .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                    .start(this, YpSettings.ICON_REQUEST_IMAGE);


        });


        if (!isFinishing()) {
            photoDialog.show();
        }

    }


    class AddTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (!TextUtils.isEmpty(s)) {
                mFeedBackContent = s.toString().trim();

            } else {
                mFeedBackContent = "";
            }


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                mFeedBackContent = s.toString().trim();

            } else {
                mFeedBackContent = "";
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(mFeedBackContent)) {

                int length = mFeedBackContent.length();

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(length);

                stringBuilder.append("/200");

                userOrderFeedback_et_et_number_tv.setText(stringBuilder.toString());


            }
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {


            case YpSettings.ICON_REQUEST_IMAGE:// 拍照


                if (resultCode == RESULT_OK) {


                    List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);


                    for (String p : mSelectPath) {

                        setUriBitmap(p);

                    }


                }


                break;


            default:
                break;

        }

    }


    /**
     * @throws
     * @Title: setUriBitmap
     * @Description: 上传图片前对图片的操作(这里用一句话描述这个方法的作用)
     * @param: @param url
     * @return: void
     */
    private void setUriBitmap(String file_Path) {


        Bitmap bm = ImgUtils.resizesBitmap(file_Path);

        if (null != bm) {
            // 保存在自己定义文件的路径

            filePath = ImgUtils.saveImgFile(this, bm);
            bm.recycle();
            if (!CheckUtil.isEmpty(filePath)) {

                mUserOrderFeedBackPhotoAdapter.addData(filePath);

            } else {
                DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, "选取失败，请重新选择上传！");
            }
        } else {
            DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, "选取失败，请重新选择上传！");
        }

    }


    /**
     * 上传图片
     *
     * @param photofile
     */
    private void douploadingImage(String photofile) {


        UploadingOrderImageBean orderImageBean = new UploadingOrderImageBean();

        orderImageBean.setFilePath(photofile);

        UploadingOrderImageService orderImageService = new UploadingOrderImageService(this);

        orderImageService.parameter(orderImageBean);

        orderImageService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UploadingOrderImageRespBean orderImageRespBean = (UploadingOrderImageRespBean) respBean;

                String url = orderImageRespBean.getResp();

                uploadingPhotoData.add(url);
                if (uploadingPhotoData.size() == photoSize) {// 判断是不是全部上传完
                    LogUtils.e("图片全部上传成功");
                    uploadingImageHandler.sendEmptyMessage(uploadingOk);
                } else {
                    isHttpHandler = isHttpHandler + 1;
                    LogUtils.e("图片" + isHttpHandler + "开始上传");
                    uploadingImageHandler.sendEmptyMessage(isHttpHandler);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loading_dlg.dismiss();
                isHttpHandler = 0;
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this);
                    return;
                } else {
                    DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, msg);
                }
            }
        });
        orderImageService.enqueue();


    }

    /**
     * 提交反馈内容
     *
     * @param orderId
     * @param feedbackType
     * @param description
     * @param imageUrls
     */
    private void postFeedBack(final String orderId, int feedbackType, String description, String[] imageUrls) {

        OrderFeedbackBean orderFeedbackBean = new OrderFeedbackBean();

        orderFeedbackBean.id = orderId;
        orderFeedbackBean.feedbackType = feedbackType;
        orderFeedbackBean.description = description;
        orderFeedbackBean.imageUrls = imageUrls;


        OrderFeedbackService ordersFeedbackService = new OrderFeedbackService(this);
        ordersFeedbackService.parameter(orderFeedbackBean);

        ordersFeedbackService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loading_dlg.dismiss();

                OrderFeedbackRespEntity orderFeedbackRespEntity = (OrderFeedbackRespEntity) respBean;

                OrderFeedbackEntity orderFeedbackEntity = orderFeedbackRespEntity.resp;


                if (orderFeedbackEntity.result == 0) {

                    if (TextUtils.isEmpty(orderFeedbackEntity.msg)) {
                        DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, "提交成功");
                    } else {
                        DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, orderFeedbackEntity.msg);
                    }

                    RxBus.get().post("OrderListEvent", new OrderListEvent());

                    finish();

                    return;
                }

                DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, orderFeedbackEntity.msg);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loading_dlg.dismiss();
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this);
                    return;
                } else {
                    DialogUtil.showDisCoverNetToast(UserOrderFeedBackActivity.this, msg);
                }

            }
        });

        ordersFeedbackService.enqueue();


    }
}
