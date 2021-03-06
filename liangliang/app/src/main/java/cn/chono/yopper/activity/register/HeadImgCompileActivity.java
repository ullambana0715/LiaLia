package cn.chono.yopper.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.HeadImgBase;
import cn.chono.yopper.ui.UserInfoEditActivity;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.cropper.ClipImageLayout;
import cn.chono.yopper.view.cropper.CopperData;

public class HeadImgCompileActivity extends MainFrameActivity implements OnClickListener {

    private ProgressBar loading_bar;
    private ClipImageLayout cropImageView;
    private Button cancel;
    private Button apply;
    private HeadImgBase headImgBase;

    private float bitmapWide = 0;
    private float bitmapHigh = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.head_img_compile_activity);
        PushAgent.getInstance(this).onAppStart();
        initView();// 组件初始化
        headImgBase = receiveData();

        setCropImageView(headImgBase.getFilePath());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("截取头像"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("截取头像"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 组件初始化
     */
    private void initView() {
        getTitleLayout().setVisibility(View.GONE);// 隐藏标题
        loading_bar = (ProgressBar) findViewById(R.id.loading_bar);
        cropImageView = (ClipImageLayout) findViewById(R.id.CropImageView);
        cropImageView.setVisibility(View.GONE);
        cancel = (Button) findViewById(R.id.cancel);
        apply = (Button) findViewById(R.id.apply);
        ViewsUtils.preventViewMultipleClick(apply, false);
        cancel.setOnClickListener(this);
        apply.setOnClickListener(this);
    }

    private HeadImgBase receiveData() {
        HeadImgBase headImgBase = (HeadImgBase) getIntent().getExtras().getSerializable("headImgBase");
        return headImgBase;
    }

    private void setCropImageView(String filePaht) {
        File file = new File(filePaht);

        Glide.with(this).load(file).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap arg0,
                                                GlideAnimation<? super Bitmap> arg1) {
                        loading_bar.setVisibility(View.GONE);
                        if (arg0 != null) {
                            cropImageView.setVisibility(View.VISIBLE);
                            bitmapWide = arg0.getWidth();
                            bitmapHigh = arg0.getHeight();
                            cropImageView.setZoomImageDrawable(arg0);
                            ViewsUtils.preventViewMultipleClick(apply, true);
                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cancel:

                finish();

                break;

            case R.id.apply:

                String imgUrl = ImgUtils.saveImgFile(HeadImgCompileActivity.this, cropImageView.clip());

                if (TextUtils.isEmpty(imgUrl)) {
                    return;
                }

                CopperData copperData = new CopperData();
                copperData.setActualCropX((int) calculateCX(bitmapWide));
                copperData.setActualCropY((int) calculateCY(bitmapHigh));
                copperData.setActualCropWidth((int) calculateCW(bitmapWide));
                copperData.setActualCropHeight((int) calculateCH(bitmapHigh));
                copperData.setCrop(true);
                copperData.setCroppedImage(imgUrl);
                setImgResults(copperData, headImgBase.getId());

                break;

            default:
                break;
        }

    }


    private void setImgResults(CopperData copperData, int isID) {
        Bundle bundle = new Bundle();

        Intent intent = new Intent();

        bundle.putSerializable("copperData", copperData);

        if (bundle != null) {

            intent.putExtras(bundle);
        }

        switch (isID) {
            case YpSettings.HEAD_IMG_REGISTER:

                intent.setClass(HeadImgCompileActivity.this, RegisterWriteInfoActivity.class);

                HeadImgCompileActivity.this.setResult(Activity.RESULT_OK, intent);

                break;

            case YpSettings.USER_COMPILE:

                intent.setClass(HeadImgCompileActivity.this, UserInfoEditActivity.class);

                HeadImgCompileActivity.this.setResult(Activity.RESULT_OK, intent);

                break;
        }
        HeadImgCompileActivity.this.finish();
    }

    private float calculateCX(float imageWight) {

        float cx = (cropImageView.getImageBordeX() - cropImageView.getImageLift())
                / (cropImageView.getImageRight() - cropImageView.getImageLift())
                * imageWight;
        return cx;

    }

    private float calculateCY(float imageHight) {

        float cy = (cropImageView.getImageBordeY() - cropImageView.getImageTop())
                / (cropImageView.getImageBottom() - cropImageView.getImageTop())
                * imageHight;
        return cy;

    }

    private float calculateCW(float imageWight) {

        float cw = (cropImageView.getImageBordeWidth() / (cropImageView.getImageRight() - cropImageView.getImageLift())) * imageWight;
        return cw;

    }

    private float calculateCH(float imageHight) {

        float ch = (cropImageView.getImageBordeHight() / (cropImageView.getImageBottom() - cropImageView.getImageTop())) * imageHight;
        return ch;

    }

}
