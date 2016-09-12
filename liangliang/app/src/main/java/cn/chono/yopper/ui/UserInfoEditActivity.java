package cn.chono.yopper.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.usercenter.EditUserAgeActivity;
import cn.chono.yopper.activity.usercenter.EditUserDisLikeActivity;
import cn.chono.yopper.activity.usercenter.EditUserEmotionalActivity;
import cn.chono.yopper.activity.usercenter.EditUserHeightActivity;
import cn.chono.yopper.activity.usercenter.EditUserHomeTownActivity;
import cn.chono.yopper.activity.usercenter.EditUserIncomeActivity;
import cn.chono.yopper.activity.usercenter.EditUserLableActivity;
import cn.chono.yopper.activity.usercenter.EditUserLikeActivity;
import cn.chono.yopper.activity.usercenter.EditUserNameActivity;
import cn.chono.yopper.activity.usercenter.EditUserProfessionActivity;
import cn.chono.yopper.activity.usercenter.EditUserWeightActivity;
import cn.chono.yopper.adapter.UILableAdapter;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.presenter.UserInfoEditContract;
import cn.chono.yopper.presenter.UserInfoEditPresenter;
import cn.chono.yopper.recyclerview.Divider;
import cn.chono.yopper.recyclerview.DividerItemDecoration;
import cn.chono.yopper.utils.AppUtils;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.view.LayoutManager.FullyGridLayoutManager;

/**
 * Created by cc on 16/7/30.
 */
public class UserInfoEditActivity extends BaseActivity<UserInfoEditPresenter> implements UserInfoEditContract.View {

    @BindView(R.id.base_back_iv)
    ImageView mBaseBackIv;

    @BindView(R.id.base_back_tv)
    TextView mBaseBackTv;

    @BindView(R.id.base_back_layout)
    LinearLayout mBaseBackLayout;

    @BindView(R.id.base_title_tv)
    TextView mBaseTitleTv;

    @BindView(R.id.base_option_iv)
    ImageView mBaseOptionIv;

    @BindView(R.id.base_option_tv)
    TextView mBaseOptionTv;

    @BindView(R.id.base_option_layout)
    LinearLayout mBaseOptionLayout;

    @BindView(R.id.commont_top_title_ll)
    LinearLayout mCommontTopTitleLl;

    @BindView(R.id.user_info_edit_name_tv)
    TextView mUserInfoEditNameTv;

    @BindView(R.id.user_info_edit_name_layout)
    LinearLayout mUserInfoEditNameLayout;

    @BindView(R.id.user_info_edit_age_tv)
    TextView mUserInfoEditAgeTv;

    @BindView(R.id.user_info_tv_age)
    TextView mUserInfoTvAge;

    @BindView(R.id.user_info_tv_age_level)
    TextView mUserInfoTvAgeLevel;

    @BindView(R.id.user_info_edit_age_layout)
    LinearLayout mUserInfoEditAgeLayout;

    @BindView(R.id.user_info_edit_emo_tv)
    TextView mUserInfoEditEmoTv;

    @BindView(R.id.user_info_tv_emotional)
    TextView mUserInfoTvEmotional;

    @BindView(R.id.user_info_edit_emotional_layout)
    LinearLayout mUserInfoEditEmotionalLayout;

    @BindView(R.id.user_info_edit_height_name_tv)
    TextView mUserInfoEditHeightNameTv;

    @BindView(R.id.user_info_tv_height)
    TextView mUserInfoTvHeight;

    @BindView(R.id.user_info_edit_height_layout)
    LinearLayout mUserInfoEditHeightLayout;

    @BindView(R.id.user_info_edit_weight_tv)
    TextView mUserInfoEditWeightTv;

    @BindView(R.id.user_info_tv_weight)
    TextView mUserInfoTvWeight;

    @BindView(R.id.user_info_edit_weight_layout)
    LinearLayout mUserInfoEditWeightLayout;

    @BindView(R.id.user_info_edit_profession_tv)
    TextView mUserInfoEditProfessionTv;

    @BindView(R.id.user_info_tv_profession)
    TextView mUserInfoTvProfession;

    @BindView(R.id.user_info_edit_profession_layout)
    LinearLayout mUserInfoEditProfessionLayout;

    @BindView(R.id.user_info_tv_income)
    TextView mUserInfoTvIncome;

    @BindView(R.id.user_info_edit_income_layout)
    LinearLayout mUserInfoEditIncomeLayout;

    @BindView(R.id.user_info_rv_lable)
    RecyclerView mUserInfoRvLable;

    @BindView(R.id.user_info_tv_lable)
    TextView mUserInfoTvLable;

    @BindView(R.id.user_info_edit_lable_layout)
    LinearLayout mUserInfoEditLableLayout;

    @BindView(R.id.user_info_edit_home_tv)
    TextView mUserInfoEditHomeTv;

    @BindView(R.id.user_info_tv_home)
    TextView mUserInfoTvHome;

    @BindView(R.id.user_info_edit_home_layout)
    LinearLayout mUserInfoEditHomeLayout;

    @BindView(R.id.user_info_edit_like_tv)
    TextView mUserInfoEditLikeTv;

    @BindView(R.id.user_info_tv_like)
    TextView mUserInfoTvLike;

    @BindView(R.id.user_info_edit_like_layout)
    LinearLayout mUserInfoEditLikeLayout;

    @BindView(R.id.user_info_edit_dislike_tv)
    TextView mUserInfoEditDislikeTv;

    @BindView(R.id.user_info_tv_dislike)
    TextView mUserInfoTvDislike;

    @BindView(R.id.user_info_edit_dislike_layout)
    LinearLayout mUserInfoEditDislikeLayout;

    @BindView(R.id.user_info_edit_bottom_layout)
    LinearLayout mUserInfoEditBottomLayout;

    @BindView(R.id.user_info_tv_detail_name)
    TextView mUserInfoTvDetailName;

    @Override
    protected int getLayout() {
        return R.layout.act_user_info_edit;
    }

    @Override
    protected UserInfoEditPresenter getPresenter() {
        return new UserInfoEditPresenter(mContext, this);
    }


    int userID;

    /**
     * 初始化变量 包括intent带的数据
     */
    @Override
    protected void initVariables() {

        Bundle bunble = this.getIntent().getExtras();

        if (bunble != null) {

            userID = bunble.getInt(YpSettings.USERID, LoginUser.getInstance().getUserId());

        }
    }

    /**
     * 初始化View 属性设置  初始状态等等
     */
    @Override
    protected void initView() {

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration();

        dividerItemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            public Divider getHorizontalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(getResources().getColor(R.color.color_ffffff)).size(5).build();
            }

            public Divider getVerticalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(Color.alpha(Color.alpha(getResources().getColor(R.color.color_ffffff)))).size(5).build();
            }
        });


        FullyGridLayoutManager lableManager = new FullyGridLayoutManager(mContext, 3);

        mUserInfoRvLable.addItemDecoration(dividerItemDecoration);

        lableManager.setSmoothScrollbarEnabled(true);

        mUserInfoRvLable.setLayoutManager(lableManager);

        mUserInfoRvLable.setNestedScrollingEnabled(true);

        mUILableAdapter = new UILableAdapter();

        mUserInfoRvLable.setAdapter(mUILableAdapter);

    }

    UILableAdapter mUILableAdapter;

    /**
     * 初始化数据并获取数据
     */
    @Override
    protected void initDataAndLoadData() {

        mPresenter.getDbUserData();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public int getUserID() {
        return userID;
    }

    @Override
    public void user_info_tv_age_levelVisible() {
        mUserInfoTvAgeLevel.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_age_levelGone() {
        mUserInfoTvAgeLevel.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_lableVisible() {

        mUserInfoTvLable.setVisibility(View.VISIBLE);

    }

    @Override
    public void user_info_tv_lableGone() {
        mUserInfoTvLable.setVisibility(View.GONE);
    }

    @Override
    public void user_info_rv_lableVisible() {
        mUserInfoRvLable.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_rv_lableGone() {
        mUserInfoRvLable.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_title(String msg) {
        mBaseTitleTv.setText(msg);
    }


    @Override
    public void user_info_tv_detail_name(String msg) {
        mUserInfoTvDetailName.setText(msg);
    }


    @Override
    public void user_info_tv_age(String msg) {
        mUserInfoTvAge.setText(msg);
    }

    @Override
    public void user_info_tv_age_level(String msg) {
        mUserInfoTvAgeLevel.setText(msg);
    }

    @Override
    public void user_info_tv_emotional(String msg) {
        mUserInfoTvEmotional.setText(msg);
    }

    @Override
    public void user_info_tv_height(String msg) {
        mUserInfoTvHeight.setText(msg);
    }

    @Override
    public void user_info_tv_weight(String msg) {
        mUserInfoTvWeight.setText(msg);
    }

    @Override
    public void user_info_tv_profession(String msg) {
        mUserInfoTvProfession.setText(msg);
    }

    @Override
    public void user_info_tv_income(String msg) {
        mUserInfoTvIncome.setText(msg);
    }

    @Override
    public void user_info_rv_lable(Object o) {


        String tas[] = (String[]) o;

        mUILableAdapter.setList(tas);
    }


    @Override
    public void user_info_tv_home(String msg) {
        mUserInfoTvHome.setText(msg);
    }

    @Override
    public void user_info_tv_like(String msg) {
        mUserInfoTvLike.setText(msg);
    }

    @Override
    public void user_info_tv_dislike(String msg) {
        mUserInfoTvDislike.setText(msg);
    }


    Dialog dialog, loadingDiaog, hintdialog;

    @Override
    public void showCreateHintOperateDialog(String title, String msg, String msg1, String msg2, BackCallListener backCallListener) {

        dialog = DialogUtil.createHintOperateDialog(mContext, title, msg, msg1, msg2, backCallListener);

        if (!isFinishing()) {

            dialog.show();
        }
    }

    @Override
    public void dismissCreateHintOperateDialog() {
        if (!isFinishing() && dialog != null) {

            dialog.dismiss();
        }
    }

    @Override
    public void showCreateSuccessHintDialog(String msg) {
        hintdialog = DialogUtil.createSuccessHintDialog(mContext, msg);

        if (!isFinishing())
            hintdialog.show();
    }

    @Override
    public void dismissCreateSuccessHintDialog() {
        if (!isFinishing() && hintdialog != null)
            hintdialog.dismiss();
    }

    @Override
    public void showLoadingDialog(String msg) {

        loadingDiaog = DialogUtil.LoadingDialog(UserInfoEditActivity.this, msg);

        if (!isFinishing()) {

            loadingDiaog.show();
        }

    }

    @Override
    public void dismissLoadingDialog() {
        if (!isFinishing() && loadingDiaog != null) {

            loadingDiaog.dismiss();
        }
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
    public void onFinish() {

        finish();
    }

    @Override
    public void EditUserNameActivity(Bundle bundle, int code) {

        AppUtils.jumpForResult(mContext, EditUserNameActivity.class, bundle, code);

    }

    @Override
    public void EditUserAgeActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserAgeActivity.class, bundle, code);
    }

    @Override
    public void EditUserHeightActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserHeightActivity.class, bundle, code);
    }

    @Override
    public void EditUserWeightActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserWeightActivity.class, bundle, code);
    }

    @Override
    public void EditUserProfessionActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserProfessionActivity.class, bundle, code);
    }

    @Override
    public void EditUserIncomeActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserIncomeActivity.class, bundle, code);
    }

    @Override
    public void EditUserEmotionalActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserEmotionalActivity.class, bundle, code);
    }

    @Override
    public void EditUserLableActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserLableActivity.class, bundle, code);
    }

    @Override
    public void EditUserHomeTownActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserHomeTownActivity.class, bundle, code);
    }

    @Override
    public void EditUserDisLikeActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserDisLikeActivity.class, bundle, code);
    }

    @Override
    public void EditUserLikeActivity(Bundle bundle, int code) {
        AppUtils.jumpForResult(mContext, EditUserLikeActivity.class, bundle, code);
    }

    @OnClick({R.id.base_back_layout,
            R.id.user_info_edit_name_layout,
            R.id.user_info_edit_age_layout,
            R.id.user_info_edit_emotional_layout,
            R.id.user_info_edit_height_layout,
            R.id.user_info_edit_weight_layout,
            R.id.user_info_edit_profession_layout,
            R.id.user_info_edit_income_layout,
            R.id.user_info_edit_lable_layout,
            R.id.user_info_rv_lable,
            R.id.user_info_edit_home_layout,
            R.id.user_info_edit_like_layout,
            R.id.user_info_edit_dislike_layout,
            R.id.user_info_edit_bottom_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_back_layout:
                mPresenter.toBack();
                break;
            case R.id.user_info_edit_name_layout:
                mPresenter.UserName();
                break;
            case R.id.user_info_edit_age_layout:
                mPresenter.UserAge();
                break;
            case R.id.user_info_edit_emotional_layout:
                mPresenter.UserEmotional();
                break;
            case R.id.user_info_edit_height_layout:
                mPresenter.UserHeight();
                break;
            case R.id.user_info_edit_weight_layout:
                mPresenter.UserWeight();
                break;
            case R.id.user_info_edit_profession_layout:
                mPresenter.UserProfession();
                break;
            case R.id.user_info_edit_income_layout:
                mPresenter.UserIncome();
                break;
            case R.id.user_info_edit_lable_layout:
                mPresenter.UserLable();
                break;
            case R.id.user_info_rv_lable:
                mPresenter.UserLable();
                break;
            case R.id.user_info_edit_home_layout:
                mPresenter.UserHomeTown();
                break;
            case R.id.user_info_edit_like_layout:
                mPresenter.UserLike();
                break;
            case R.id.user_info_edit_dislike_layout:
                mPresenter.UserDisLike();
                break;
            case R.id.user_info_edit_bottom_layout:
                mPresenter.submit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {


            mPresenter.toBack();

            return true;
        }

        return false;
    }
}
