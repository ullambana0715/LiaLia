package cn.chono.yopper.ui;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.chono.yopper.R;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.presenter.VideoContract;
import cn.chono.yopper.presenter.VideoPresenter;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.RePortCallListener;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by cc on 16/7/30.
 */
public class VideoActivity extends BaseActivity<VideoPresenter> implements VideoContract.View {

    @BindView(R.id.video_sv)
    SurfaceView mVideoSv;

    @BindView(R.id.video_iv_coverimg)
    ImageView mVideoIvCoverimg;

    @BindView(R.id.video_iv_start)
    ImageView mVideoIvStart;

    @BindView(R.id.video_pb)
    ProgressBar mVideoPb;

    @BindView(R.id.video_ll)
    RelativeLayout mVideoLl;

    @BindView(R.id.parise_count_tv)
    TextView mPariseCountTv;

    @BindView(R.id.iv_delect)
    ImageView mIvDelect;

    @BindView(R.id.video_ll_parise_delete)
    LinearLayout mVideoLlPariseDelete;

    @BindView(R.id.ll_back)
    LinearLayout mLlBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.option_tv)
    ImageView mOptionTv;

    @BindView(R.id.ll_option)
    LinearLayout mLlOption;

    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected int getLayout() {
        return R.layout.act_video;
    }

    @Override
    protected VideoPresenter getPresenter() {
        return new VideoPresenter(mContext, this);
    }


    private Handler mHandler = new Handler();
    private Runnable mLoadingRunnable = new Runnable() {

        @Override
        public void run() {

            mPresenter.initDataAndLoadData(mVideoSv);

        }
    };


    /**
     * 初始化变量 包括intent带的数据
     */
    @Override
    protected void initVariables() {


    }

    /**
     * 初始化View 属性设置  初始状态等等
     */
    @Override
    protected void initView() {

        int width = UnitUtil.getScreenWidthPixels(mContext);

        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) mVideoLl.getLayoutParams();

        linearParams.height = width;

        linearParams.width = width;

        mVideoLl.setLayoutParams(linearParams);


    }

    /**
     * 初始化数据并获取数据
     */
    @Override
    protected void initDataAndLoadData() {


        getWindow().getDecorView().post(() -> mHandler.post(mLoadingRunnable));


    }


    @Override
    public void video_llGome() {

        mVideoLl.setVisibility(View.GONE);

    }

    @Override
    public void video_llVisible() {

        mVideoLl.setVisibility(View.VISIBLE);

    }

    @Override
    public void video_iv_coverimgGone() {
        mVideoIvCoverimg.setVisibility(View.GONE);
    }

    @Override
    public void video_iv_coverimgVisible() {
        mVideoIvCoverimg.setVisibility(View.VISIBLE);
    }

    @Override
    public void video_pbGone() {
        mVideoPb.setVisibility(View.GONE);
    }

    @Override
    public void video_pbVisible() {
        mVideoPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void video_iv_startGone() {
        mVideoIvStart.setVisibility(View.GONE);
    }

    @Override
    public void video_iv_startVisible() {
        mVideoIvStart.setVisibility(View.VISIBLE);
    }

    @Override
    public void parise_count_tvGone() {

        mPariseCountTv.setVisibility(View.GONE);
    }

    @Override
    public void parise_count_tvVisible() {

        mPariseCountTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void video_ll_parise_deleteGone() {
        mVideoLlPariseDelete.setVisibility(View.GONE);
    }

    @Override
    public void video_ll_parise_deleteVisible() {
        mVideoLlPariseDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public void iv_delectGone() {

        mIvDelect.setVisibility(View.GONE);

    }

    @Override
    public void iv_delectVisible() {

        mIvDelect.setVisibility(View.VISIBLE);

    }

    @Override
    public void iv_delect(int id) {
        mIvDelect.setImageResource(id);
    }

    @Override
    public void video_iv_coverimg(String msg) {


        Glide.with(this).load(msg).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {


                mVideoIvCoverimg.setVisibility(View.GONE);

                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                mVideoIvStart.setVisibility(View.VISIBLE);

                mVideoIvStart.setBackgroundResource(R.drawable.video_play_icon);


                return false;
            }
        }).into(mVideoIvCoverimg);


        Glide.with(mContext).load(msg).into(mVideoIvCoverimg);

    }

    @Override
    public void video_iv_start(Object msg) {

        int id = (int) msg;

        mVideoIvStart.setBackgroundResource(id);

    }

    Dialog hihtDialog;

    @Override
    public void showCreateHintOperateDialog(String title, String msg, String msg1, String msg2, BackCallListener backCallListener) {

        hihtDialog = DialogUtil.createHintOperateDialog(mContext, title, msg, msg1, msg2, backCallListener);

        if (!isFinishing())

            hihtDialog.show();


    }

    @Override
    public void dismissCreateHintOperateDialog() {
        if (!isFinishing() && hihtDialog != null)

            hihtDialog.dismiss();
    }

    @Override
    public void showDisCoverNetToast(String msg) {

        if (TextUtils.isEmpty(msg)) {

            DialogUtil.showDisCoverNetToast(mContext);

        } else {

            DialogUtil.showDisCoverNetToast(mContext, msg);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void finish() {

        mPresenter.OnFinish();

        super.finish();
    }

    @OnClick(R.id.video_iv_start)
    public void onVideoLlClick() {
        mPresenter.videoLlClick();
    }

    @OnClick(R.id.parise_count_tv)
    public void onPariseClick() {
        mPresenter.pariseClick();
    }

    @OnClick(R.id.iv_delect)
    public void onDelectClick() {

        mPresenter.delectClick();
    }


    Dialog hintdialog, reportDialog;

    @Override
    public void showTimerCreateSuccessHintDialog(String msg) {

        hintdialog = DialogUtil.createSuccessHintDialog(mContext, msg);

        if (!isFinishing())
            hintdialog.show();

    }

    @Override
    public void dismissTimerCreateSuccessHintDialog() {
        if (!isFinishing() && hintdialog != null)
            hintdialog.dismiss();
    }


    @Override
    public void showRePortDialog(String title, String one, String two, String three, String four, String five, RePortCallListener rePortCallListener) {
        reportDialog = DialogUtil.RePortDialog(mContext, title, one, two, three, four, five, rePortCallListener);

        if (!isFinishing())
            reportDialog.show();
    }

    @Override
    public void dismissRePortDialog() {
        if (!isFinishing() && reportDialog != null)
            reportDialog.dismiss();
    }

    @Override
    public void parise_count_tv() {

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.photo_parised_icon);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        mPariseCountTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);


    }

    @Override
    public void parise_count_tv_cancel() {

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.photo_parise_icon);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        mPariseCountTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);//


    }

    @Override
    public void mPariseCountTv(String msg) {
        mPariseCountTv.setText(msg);
    }

    @OnClick(R.id.ll_back)
    public void onBackClick() {

        finish();
    }


    @OnClick(R.id.video_sv)
    public void onVideoSvClick() {

        mPresenter.VideoSvClick();
    }
}
