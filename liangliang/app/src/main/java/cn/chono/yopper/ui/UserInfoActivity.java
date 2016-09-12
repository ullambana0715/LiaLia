package cn.chono.yopper.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.appointment.AppointPublishTypeSelectActivity;
import cn.chono.yopper.activity.appointment.DatingDetailActivity;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.activity.chat.ChatActivity;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.activity.usercenter.MyBubblingActivity;
import cn.chono.yopper.activity.usercenter.SettingPhoneActivity;
import cn.chono.yopper.activity.usercenter.VipOpenedActivity;
import cn.chono.yopper.activity.usercenter.VipRenewalsActivity;
import cn.chono.yopper.activity.video.VideoCoverActivity;
import cn.chono.yopper.activity.video.VideoDetailGetActivity;
import cn.chono.yopper.adapter.UIAppointmentAdapter;
import cn.chono.yopper.adapter.UIGiftAdapter;
import cn.chono.yopper.adapter.UILableAdapter;
import cn.chono.yopper.adapter.UIPhotoAdapter;
import cn.chono.yopper.adapter.UIPrivatePhotoAdapter;
import cn.chono.yopper.adapter.UIVideoAdapter;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.data.MyGiftSum;
import cn.chono.yopper.data.UserPhoto;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.entity.PrivacyAlbum;
import cn.chono.yopper.glide.BlurTransformation;
import cn.chono.yopper.presenter.UserInfoContract;
import cn.chono.yopper.presenter.UserInfoPresenter;
import cn.chono.yopper.recyclerview.Divider;
import cn.chono.yopper.recyclerview.DividerItemDecoration;
import cn.chono.yopper.utils.AppUtils;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.RePortCallListener;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.view.LayoutManager.FullyGridLayoutManager;
import cn.chono.yopper.view.LayoutManager.FullyLinearLayoutManager;
import cn.chono.yopper.view.MyNestedScrollView;

/**
 * Created by cc on 16/7/21.
 */
public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.View {

    @BindView(R.id.user_info_iv_icon)
    ImageView mUserInfoIvIcon;

    @BindView(R.id.user_info_ll_back)
    LinearLayout mUserInfoLlBack;

    @BindView(R.id.user_info_tv_title)
    TextView mUserInfoTvTitle;

    @BindView(R.id.user_info_option_tv)
    ImageView mUserInfoOptionTv;

    @BindView(R.id.user_info_ll_option)
    LinearLayout mUserInfoLlOption;

    @BindView(R.id.user_info_ll_title)
    LinearLayout mUserInfoLlTitle;

    @BindView(R.id.user_info_tv_icon)
    TextView mUserInfoTvIcon;

    @BindView(R.id.user_info_iv_video_invite)
    ImageView mUserInfoIvVideoInvite;

    @BindView(R.id.user_info_iv_hot)
    ImageView mUserInfoIvHot;

    @BindView(R.id.user_info_iv_video_state)
    ImageView mUserInfoIvVideoState;

    @BindView(R.id.user_info_iv_vip)
    ImageView mUserInfoIvVip;

    @BindView(R.id.user_info_iv_activity_talent)
    ImageView mUserInfoIvActivityTalent;

    @BindView(R.id.user_info_ll_icon_hint)
    RelativeLayout mUserInfoLlIconHint;

    @BindView(R.id.user_info_fl_icon)
    FrameLayout mUserInfoFlIcon;

    @BindView(R.id.user_info_tv_detail_name)
    TextView mUserInfoTvDetailName;

    @BindView(R.id.user_info_tv_detail_sex)
    ImageView mUserInfoTvDetailSex;

    @BindView(R.id.user_info_tv_modify)
    TextView mUserInfoTvModify;

    @BindView(R.id.user_info_tv_time)
    TextView mUserInfoTvTime;

    @BindView(R.id.user_info_v_time_line)
    View mUserInfoVTimeLine;

    @BindView(R.id.user_info_tv_distance)
    TextView mUserInfoTvDistance;

    @BindView(R.id.user_info_iv_hor)
    ImageView mUserInfoIvHor;

    @BindView(R.id.user_info_tv_hor)
    TextView mUserInfoTvHor;

    @BindView(R.id.user_info_v_match_line)
    View mUserInfoVMatchLine;

    @BindView(R.id.user_info_tv_everyday_match)
    TextView mUserInfoTvEverydayMatch;

    @BindView(R.id.user_info_rb_score)
    RatingBar mUserInfoRbScore;

    @BindView(R.id.user_info_tv_photo)
    TextView mUserInfoTvPhoto;

    @BindView(R.id.user_info_rv_photo)
    RecyclerView mUserInfoRvPhoto;

    @BindView(R.id.user_info_tv_private_photo)
    TextView mUserInfoTvPrivatePhoto;

    @BindView(R.id.user_info_rv_private_photo)
    RecyclerView mUserInfoRvPrivatePhoto;

    @BindView(R.id.user_info_tv_video)
    TextView mUserInfoTvVideo;

    @BindView(R.id.user_info_rv_video)
    RecyclerView mUserInfoRvVideo;

    @BindView(R.id.user_info_rv_gift)
    RecyclerView mUserInfoRvGift;

    @BindView(R.id.user_info_ll_gift)
    LinearLayout mUserInfoLlGift;

    @BindView(R.id.user_info_tv_appointment)
    TextView mUserInfoTvAppointment;

    @BindView(R.id.user_info_rv_appointment)
    RecyclerView mUserInfoRvAppointment;

    @BindView(R.id.user_info_ll_appointment)
    LinearLayout mUserInfoLlAppointment;

    @BindView(R.id.user_info_tv_bubble)
    TextView mUserInfoTvBubble;

    @BindView(R.id.user_info_ll_bubble)
    LinearLayout mUserInfoLlBubble;

    @BindView(R.id.user_info_tv_isprofilecomplete)
    TextView mUserInfoTvIsprofilecomplete;

    @BindView(R.id.user_info_tv_age)
    TextView mUserInfoTvAge;

    @BindView(R.id.user_info_tv_age_level)
    TextView mUserInfoTvAgeLevel;

    @BindView(R.id.user_info_tv_emotional)
    TextView mUserInfoTvEmotional;

    @BindView(R.id.user_info_tv_height)
    TextView mUserInfoTvHeight;

    @BindView(R.id.user_info_height_layout)
    LinearLayout mUserInfoHeightLayout;

    @BindView(R.id.user_info_tv_weight)
    TextView mUserInfoTvWeight;

    @BindView(R.id.user_info_weight_layout)
    LinearLayout mUserInfoWeightLayout;

    @BindView(R.id.user_info_tv_profession)
    TextView mUserInfoTvProfession;

    @BindView(R.id.user_info_tv_income)
    TextView mUserInfoTvIncome;

    @BindView(R.id.user_info_rv_lable)
    RecyclerView mUserInfoRvLable;

    @BindView(R.id.user_info_tv_lable)
    TextView mUserInfoTvLable;

    @BindView(R.id.user_info_tv_phone_state)
    TextView mUserInfoTvPhoneState;

    @BindView(R.id.user_info_tv_phone_btn)
    TextView mUserInfoTvPhoneBtn;

    @BindView(R.id.user_info_tv_video_state)
    TextView mUserInfoTvVideoState;

    @BindView(R.id.user_info_tv_video_btn)
    TextView mUserInfoTvVideoBtn;

    @BindView(R.id.user_info_tv_home)
    TextView mUserInfoTvHome;

    @BindView(R.id.user_info_tv_like)
    TextView mUserInfoTvLike;

    @BindView(R.id.user_info_tv_dislike)
    TextView mUserInfoTvDislike;

    @BindView(R.id.user_info_tv_ID)
    TextView mUserInfoTvID;

    @BindView(R.id.user_info_ll_info)
    LinearLayout mUserInfoLlInfo;

    @BindView(R.id.user_info_ll_like)
    LinearLayout mUserInfoLlLike;

    @BindView(R.id.user_info_iv_chat)
    ImageView mUserInfoIvChat;

    @BindView(R.id.user_info_ll_chat)
    LinearLayout mUserInfoLlChat;

    @BindView(R.id.user_info_bottom_layout)
    LinearLayout mUserInfoBottomLayout;

    @BindView(R.id.user_info_iv_bubble_img)
    ImageView mUserInfoIvBubbleImg;

    @BindView(R.id.user_album_border)
    ImageView mUserAlbumBorder;

    @BindView(R.id.user_info_ll_bubble_img)
    RelativeLayout mUserInfoLlBubbleImg;

    @BindView(R.id.user_info_tv_bubble_title)
    TextView mUserInfoTvBubbleTitle;

    @BindView(R.id.user_info_tv_bubble_content)
    TextView mUserInfoTvBubbleContent;

    @BindView(R.id.user_info_tv_modifys)
    TextView mUserInfoTvModifys;

    @BindView(R.id.user_info_ll_time)
    LinearLayout mUserInfoLlTime;

    @BindView(R.id.user_info_ll_match_line)
    LinearLayout mUserInfoLlMatchLine;

    @BindView(R.id.user_info_ll_bubble_content)
    LinearLayout mUserInfoLlBubbleContent;

    @BindView(R.id.user_info_iv_like_icon)
    ImageView mUserInfoIvLikeIcon;

    @BindView(R.id.user_info_tv_like_btn)
    TextView mUserInfoTvLikeBtn;

    @BindView(R.id.user_info_tv_dating_invite)
    TextView mUserInfoTvDatingInvite;

    @BindView(R.id.user_info_nsv)
    MyNestedScrollView mUserInfoNsv;

    @BindView(R.id.user_info_tv_chat_btn)
    TextView mUserInfoTvChatBtn;


    @Override
    protected int getLayout() {
        return R.layout.act_user_info;
    }

    @Override
    protected UserInfoPresenter getPresenter() {
        return new UserInfoPresenter(mContext, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void initVariables() {


    }

    @Override
    protected void initView() {

        int width = UnitUtil.getScreenWidthPixels(mContext);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);

        LinearLayout.LayoutParams frparams = new LinearLayout.LayoutParams(width, width);

        mUserInfoFlIcon.setLayoutParams(frparams);

        mUserInfoIvIcon.setLayoutParams(params);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration();

        dividerItemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            public Divider getHorizontalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(getResources().getColor(R.color.color_ffffff)).size(3).build();
            }

            public Divider getVerticalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(Color.alpha(Color.alpha(getResources().getColor(R.color.color_ffffff)))).size(3).build();
            }
        });


        mUserInfoRvPhoto.addItemDecoration(dividerItemDecoration);

        FullyGridLayoutManager phototManager = new FullyGridLayoutManager(mContext, 6);

        phototManager.setSmoothScrollbarEnabled(true);

        mUserInfoRvPhoto.setLayoutManager(phototManager);

        mUserInfoRvPhoto.setNestedScrollingEnabled(true);

        mUIPhotoAdapter = new UIPhotoAdapter(mContext);

        mUserInfoRvPhoto.setAdapter(mUIPhotoAdapter);


        mUserInfoRvPrivatePhoto.addItemDecoration(dividerItemDecoration);

        FullyGridLayoutManager prviatePhototManager = new FullyGridLayoutManager(mContext, 6);

        prviatePhototManager.setSmoothScrollbarEnabled(true);

        mUserInfoRvPrivatePhoto.setLayoutManager(prviatePhototManager);

        mUserInfoRvPrivatePhoto.setNestedScrollingEnabled(true);

        mUIPrivatePhotoAdapter = new UIPrivatePhotoAdapter(mContext);

        mUserInfoRvPrivatePhoto.setAdapter(mUIPrivatePhotoAdapter);


        mUserInfoRvVideo.addItemDecoration(dividerItemDecoration);

        FullyGridLayoutManager videoManager = new FullyGridLayoutManager(mContext, 6);

        videoManager.setSmoothScrollbarEnabled(true);

        mUserInfoRvVideo.setLayoutManager(videoManager);

        mUserInfoRvVideo.setNestedScrollingEnabled(true);

        mUIVideoAdapter = new UIVideoAdapter(mContext);

        mUserInfoRvVideo.setAdapter(mUIVideoAdapter);


        FullyLinearLayoutManager giftManager = new FullyLinearLayoutManager(mContext);

        giftManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        giftManager.setSmoothScrollbarEnabled(true);

        mUserInfoRvGift.setLayoutManager(giftManager);

        mUserInfoRvGift.setNestedScrollingEnabled(true);

        mUIGiftAdapter = new UIGiftAdapter(mContext);

        mUserInfoRvGift.setAdapter(mUIGiftAdapter);


        FullyLinearLayoutManager appointmentManager = new FullyLinearLayoutManager(mContext);

        appointmentManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        giftManager.setSmoothScrollbarEnabled(true);

        mUserInfoRvAppointment.setLayoutManager(appointmentManager);

        mUserInfoRvAppointment.setNestedScrollingEnabled(true);

        mUIAppointmentAdapter = new UIAppointmentAdapter();

        mUserInfoRvAppointment.setAdapter(mUIAppointmentAdapter);


        mUserInfoRvLable.addItemDecoration(dividerItemDecoration);

        FullyGridLayoutManager lableManager = new FullyGridLayoutManager(mContext, 3);

        lableManager.setSmoothScrollbarEnabled(true);

        mUserInfoRvLable.setLayoutManager(lableManager);

        mUserInfoRvLable.setNestedScrollingEnabled(true);

        mUILableAdapter = new UILableAdapter();

        mUserInfoRvLable.setAdapter(mUILableAdapter);

        mUserInfoNsv.setVisibility(View.VISIBLE);

    }


    UIPhotoAdapter mUIPhotoAdapter;

    UIPrivatePhotoAdapter mUIPrivatePhotoAdapter;

    UIVideoAdapter mUIVideoAdapter;

    UIGiftAdapter mUIGiftAdapter;

    UIAppointmentAdapter mUIAppointmentAdapter;

    UILableAdapter mUILableAdapter;

    @Override
    protected void initDataAndLoadData() {

        mPresenter.initDataAndLoadData();

    }


    @Override
    public void user_info_ll_optionVisible() {
        mUserInfoLlOption.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_ll_optionGone() {
        mUserInfoLlOption.setVisibility(View.INVISIBLE);
    }

    @Override
    public void user_info_iv_hotVisible() {
        mUserInfoIvHot.setVisibility(View.VISIBLE);

    }

    @Override
    public void user_info_iv_hotGone() {
        mUserInfoIvHot.setVisibility(View.GONE);

    }

    @Override
    public void user_info_iv_video_stateVisible() {
        mUserInfoIvVideoState.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_iv_video_stateGone() {
        mUserInfoIvVideoState.setVisibility(View.GONE);
    }

    @Override
    public void user_info_iv_vipVisible() {
        mUserInfoIvVip.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_iv_vipGone() {
        mUserInfoIvVip.setVisibility(View.GONE);
    }

    @Override
    public void user_info_iv_activity_talentVisible() {
        mUserInfoIvActivityTalent.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_iv_activity_talentGone() {
        mUserInfoIvActivityTalent.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_iconVisible() {
        mUserInfoTvIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_iconGone() {
        mUserInfoTvIcon.setVisibility(View.GONE);
    }

    @Override
    public void user_info_iv_video_inviteVisible() {

        mUserInfoIvVideoInvite.setVisibility(View.VISIBLE);

    }

    @Override
    public void user_info_iv_video_inviteGone() {
        mUserInfoIvVideoInvite.setVisibility(View.GONE);
    }

    @Override
    public void user_info_ll_icon_hintVisible() {
        mUserInfoLlIconHint.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_ll_icon_hintGone() {
        mUserInfoLlIconHint.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_modifyVisible() {
        mUserInfoTvModify.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_modifyGone() {
        mUserInfoTvModify.setVisibility(View.GONE);
    }

    @Override
    public void user_info_v_match_lineVisible() {
        mUserInfoVMatchLine.setVisibility(View.VISIBLE);

    }

    @Override
    public void user_info_v_match_lineGone() {
        mUserInfoVMatchLine.setVisibility(View.GONE);
    }

    @Override
    public void user_info_ll_match_lineVisible() {
        mUserInfoLlMatchLine.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_ll_match_lineGone() {
        mUserInfoLlMatchLine.setVisibility(View.GONE);
    }

    @Override
    public void user_info_ll_giftVisible() {
        mUserInfoLlGift.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_ll_giftGone() {
        mUserInfoLlGift.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_appointmentVisible() {
        mUserInfoTvAppointment.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_appointmentGone() {
        mUserInfoTvAppointment.setVisibility(View.GONE);
    }

    @Override
    public void user_info_ll_bubbleVisible() {
        mUserInfoLlBubble.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_ll_bubbleGone() {
        mUserInfoLlBubble.setVisibility(View.GONE);
    }

    @Override
    public void user_info_ll_bubble_contentVisible() {
        mUserInfoLlBubbleContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_ll_bubble_contentGone() {
        mUserInfoLlBubbleContent.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_isprofilecompleteVisible() {
        mUserInfoTvIsprofilecomplete.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_isprofilecompleteGone() {
        mUserInfoTvIsprofilecomplete.setVisibility(View.INVISIBLE);
    }

    @Override
    public void user_info_tv_modifysVisible() {
        mUserInfoTvModifys.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_modifysGone() {
        mUserInfoTvModifys.setVisibility(View.GONE);
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
    public void user_info_tv_phone_btnVisible() {
        mUserInfoTvPhoneBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_phone_btnGone() {
        mUserInfoTvPhoneBtn.setVisibility(View.GONE);
    }

    @Override
    public void user_info_bottom_layoutVisible() {
        mUserInfoBottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_bottom_layoutGone() {

        mUserInfoBottomLayout.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_dating_inviteVisible() {
        mUserInfoTvDatingInvite.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_dating_inviteGone() {
        mUserInfoTvDatingInvite.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_videoVisible() {
        mUserInfoTvVideo.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_videoGone() {
        mUserInfoTvVideo.setVisibility(View.GONE);
    }

    @Override
    public void user_info_rv_videoVisible() {
        mUserInfoRvVideo.setVisibility(View.VISIBLE);

    }

    @Override
    public void user_info_rv_videoGone() {
        mUserInfoRvVideo.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_distanceGone() {
        mUserInfoTvDistance.setVisibility(View.GONE);
        mUserInfoVTimeLine.setVisibility(View.GONE);
    }

    @Override
    public void user_info_tv_distanceVisible() {
        mUserInfoTvDistance.setVisibility(View.VISIBLE);
        mUserInfoVTimeLine.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_private_photoVisible() {
        mUserInfoTvPrivatePhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_tv_private_photoGone() {
        mUserInfoTvPrivatePhoto.setVisibility(View.GONE);
    }

    @Override
    public void user_info_rv_private_photoVisible() {
        mUserInfoRvPrivatePhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void user_info_rv_private_photoGone() {
        mUserInfoRvPrivatePhoto.setVisibility(View.GONE);
    }


    @Override
    public void user_info_iv_vip(int drawableId) {

        mUserInfoIvVip.setBackgroundResource(drawableId);
    }

    @Override
    public void user_info_tv_title(String msg) {
        mUserInfoTvTitle.setText(msg);
    }

    @Override
    public void user_info_iv_icon(String msg) {

        Glide.with(mContext).load(msg).into(mUserInfoIvIcon);

    }

    @Override
    public void user_info_tv_detail_name(String msg) {
        mUserInfoTvDetailName.setText(msg);
    }

    @Override
    public void user_info_tv_detail_sex(Object o) {

        int id = (int) o;

        mUserInfoTvDetailSex.setImageResource(id);

    }

    @Override
    public void user_info_tv_modify(String msg) {
        mUserInfoTvModify.setText(msg);
    }

    @Override
    public void user_info_tv_time(String msg) {
        mUserInfoTvTime.setText(msg);
    }

    @Override
    public void user_info_tv_distance(String msg) {
        mUserInfoTvDistance.setText(msg);
    }

    @Override
    public void user_info_iv_hor(Object o) {

        int id = (int) o;

        mUserInfoIvHor.setImageResource(id);

    }

    @Override
    public void user_info_tv_hor(String msg) {
        mUserInfoTvHor.setText(msg);
    }

    @Override
    public void user_info_rb_score(float o) {


        mUserInfoRbScore.setRating(o);

    }

    @Override
    public void user_info_rv_photo(Object o) {


        List<UserPhoto> list = (List<UserPhoto>) o;

        Logger.e("user_info_rv_photo=" + o);


        mUIPhotoAdapter.setList(list);


    }

    @Override
    public void user_info_rv_private_photo(Object o, boolean isUnlockPrivacyAlbum) {

        List<PrivacyAlbum> pri = (List<PrivacyAlbum>) o;

        mUIPrivatePhotoAdapter.setList(pri, isUnlockPrivacyAlbum);

    }

    @Override
    public void user_info_rv_video(Object o) {

        List<GeneralVideos> list = (List<GeneralVideos>) o;

        mUIVideoAdapter.setList(list);

    }

    @Override
    public void user_info_rv_gift(Object o) {

        List<MyGiftSum> myGiftSum = (List<MyGiftSum>) o;

        mUIGiftAdapter.setList(myGiftSum);


    }

    @Override
    public void user_info_tv_appointment(String msg) {
        mUserInfoTvAppointment.setText(msg);
    }

    @Override
    public void user_info_rv_appointment(Object o, int loginUserId, int userID, int sex) {

        List<AppointDetailDto> list = (List<AppointDetailDto>) o;

        Logger.e("----------------=" + loginUserId);
        Logger.e("===================" + userID);

        mUIAppointmentAdapter.setList(list, loginUserId, userID, sex);

    }

    @Override
    public void user_info_tv_bubble(String msg) {
        mUserInfoTvBubble.setText(msg);
    }

    @Override
    public void user_info_iv_bubble_img(String msg) {

        Glide.with(mContext).load(msg).into(mUserInfoIvBubbleImg);


    }

    @Override
    public void user_info_iv_bubble_img_private(String msg) {

        BlurTransformation mBlurTransformation = new BlurTransformation(mContext, Glide.get(mContext).getBitmapPool(), 8, 8);

        Glide.with(mContext).load(msg).error(R.drawable.error_user_icon).bitmapTransform(mBlurTransformation).into(mUserInfoIvBubbleImg);
    }

    @Override
    public void user_info_tv_bubble_title(String msg) {

        if (TextUtils.isEmpty(msg)) {
            mUserInfoTvBubbleTitle.setVisibility(View.GONE);
            return;
        }
        mUserInfoTvBubbleTitle.setVisibility(View.VISIBLE);
        mUserInfoTvBubbleTitle.setText(msg);
    }

    @Override
    public void user_info_tv_bubble_content(String msg) {

        if (TextUtils.isEmpty(msg)) {
            mUserInfoTvBubbleContent.setVisibility(View.GONE);
            return;
        }
        mUserInfoTvBubbleContent.setVisibility(View.VISIBLE);
        mUserInfoTvBubbleContent.setText(msg);
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
    public void user_info_tv_phone_state(String msg) {
        mUserInfoTvPhoneState.setText(msg);
    }

    @Override
    public void user_info_tv_phone_btn(String msg) {
        mUserInfoTvPhoneBtn.setText(msg);
    }

    @Override
    public void user_info_tv_video_state(String msg) {
        mUserInfoTvVideoState.setText(msg);
    }

    @Override
    public void user_info_tv_video_btn(String msg) {
        mUserInfoTvVideoBtn.setText(msg);
    }

    @Override
    public void user_info_tv_video_btn_color(int colorId) {

        mUserInfoTvVideoBtn.setTextColor(getResources().getColor(colorId));
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

    @Override
    public void user_info_tv_ID(String msg) {
        mUserInfoTvID.setText(msg);
    }

    @Override
    public void user_info_tv_everyday_match(String msg) {
        mUserInfoTvEverydayMatch.setText(msg);
    }

    @Override
    public void user_info_iv_like_icon(Object o) {
        int id = (int) o;

        mUserInfoIvLikeIcon.setImageResource(id);
    }

    @Override
    public void user_info_tv_like_btn(String msg) {
        mUserInfoTvLikeBtn.setText(msg);
    }

    @Override
    public void user_info_iv_video_invite(int id) {
        mUserInfoIvVideoInvite.setBackgroundResource(id);
    }

    @Override
    public void user_album_border(int id) {
        mUserAlbumBorder.setBackgroundResource(id);
    }

    @Override
    public void showDisCoverNetToast(String msg) {

        if (TextUtils.isEmpty(msg)) {

            DialogUtil.showDisCoverNetToast(UserInfoActivity.this);
            return;
        }
        DialogUtil.showDisCoverNetToast(UserInfoActivity.this, msg);
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

    Dialog helpdialog, hintdialog, photoDialog, imgdialog, canNotPublishDatingDialog, blockDialog, reportDialog, loadingDiaog;


    @Override
    public void showLoadingDialog() {
        loadingDiaog = DialogUtil.LoadingDialog(mContext, null);
        if (!isFinishing())
            loadingDiaog.show();
    }

    @Override
    public void dismissLoadingDialog() {

        if (loadingDiaog != null && !isFinishing()) {
            loadingDiaog.dismiss();
        }

    }

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
    public void showCreatePhotoDialog(View.OnClickListener onClickListener) {
        photoDialog = DialogUtil.createPhotoDialog(mContext, onClickListener);
        if (!isFinishing()) {
            photoDialog.show();
        }
    }

    @Override
    public void dismissCreatePhotoDialog() {

        if (!isFinishing() && photoDialog != null)
            photoDialog.dismiss();

    }

    @Override
    public void showCreateHintOperateDialog(String title, String content, String msg1, String msg2, BackCallListener backCallListener) {
        imgdialog = DialogUtil.createHintOperateDialog(mContext, title, content, msg1, msg2, backCallListener);
        if (!isFinishing()) {
            imgdialog.show();
        }
    }

    @Override
    public void dismissCreateHintOperateDialog() {
        if (!isFinishing() && imgdialog != null)
            imgdialog.dismiss();
    }

    @Override
    public void showCreateNotCanPublishDatingHintDialog(List<DatingRequirment> list, String sureStr, BackCallListener backCallListener) {

        canNotPublishDatingDialog = DialogUtil.createNotCanPublishDatingHintDialog(UserInfoActivity.this, list, sureStr, backCallListener);

        if (!isFinishing()) {
            canNotPublishDatingDialog.show();
        }

    }

    @Override
    public void dismissCreateNotCanPublishDatingHintDialog() {
        if (!isFinishing() && canNotPublishDatingDialog != null)
            canNotPublishDatingDialog.dismiss();
    }

    @Override
    public void showCreateHotHintDialog(BackCallListener backCallListener) {
        helpdialog = DialogUtil.createHotHintDialog(mContext, backCallListener);
        if (!isFinishing()) {
            helpdialog.show();
        }
    }

    @Override
    public void dismissCreateHotHintDialog() {
        if (!isFinishing() && helpdialog != null) {
            helpdialog.dismiss();
        }
    }

    @Override
    public void showBlockDialog(String title, String lift, String right, BackCallListener backCallListener) {

        blockDialog = DialogUtil.BlockDialog(mContext, title, lift, right, backCallListener);

        if (!isFinishing())
            blockDialog.show();


    }

    @Override
    public void dismissBlockDialog() {
        if (!isFinishing() && blockDialog != null)
            blockDialog.dismiss();
    }

    @Override
    public void AlbumViewLargerImageActivity(Bundle bundle) {

        AppUtils.jump(mContext, AlbumViewLargerImageActivity.class, bundle);

    }

    @Override
    public void PrivacyAlbumViewLargerImageActivity(Bundle bundle) {


        AppUtils.jump(mContext, PrivacyAlbumViewLargerImageActivity.class, bundle);


    }


    @Override
    public void VideoDetailGetActivity(Bundle bundle) {
        AppUtils.jump(mContext, VideoDetailGetActivity.class, bundle);
    }

    @Override
    public void DatingDetailActivity(Bundle bundle) {
        AppUtils.jump(mContext, DatingDetailActivity.class, bundle);
    }

    @Override
    public void ChatActivity(Bundle bundle) {
        AppUtils.jump(mContext, ChatActivity.class, bundle);
    }

    @Override
    public void AppointPublishTypeSelectActivity(Bundle bundle) {

        AppUtils.jump(mContext, AppointPublishTypeSelectActivity.class, bundle);
    }

    @Override
    public void UserInfoEditActivity(Bundle bundle) {

        AppUtils.jump(mContext, UserInfoEditActivity.class, bundle);
    }

    @Override
    public void SimpleWebViewActivity(Bundle bundle) {

        AppUtils.jump(mContext, SimpleWebViewActivity.class, bundle);
    }

    @Override
    public void VipOpenedActivity(Bundle bundle) {

        AppUtils.jump(mContext, VipOpenedActivity.class, bundle);
    }

    @Override
    public void VipRenewalsActivity(Bundle bundle) {

        AppUtils.jump(mContext, VipRenewalsActivity.class, bundle);
    }

    @Override
    public void UserInfoActivity(Bundle bundle) {
        AppUtils.jump(mContext, UserInfoActivity.class, bundle);
    }

    @Override
    public void VideoCoverActivity(Bundle bundle) {

        AppUtils.jump(mContext, VideoCoverActivity.class, bundle);
    }

    @Override
    public void AppleListActivity(Bundle bundle) {
        AppUtils.jump(mContext, AppleListActivity.class, bundle);
    }

    @Override
    public void SettingPhoneActivity(Bundle bundle) {
        AppUtils.jump(mContext, SettingPhoneActivity.class, bundle);
    }

    @Override
    public void MyBubblingActivity(Bundle bundle) {

        AppUtils.jump(mContext, MyBubblingActivity.class, bundle);


    }

    @Override
    public void VideoActivity(Bundle bundle) {

        AppUtils.jump(mContext, VideoActivity.class, bundle);
    }

    @Override
    public void onFinish() {
        finish();
    }

    @Override
    public Context ApplicationContext() {
        return this.getApplicationContext();
    }


    /**
     * 返回
     */
    @OnClick(R.id.user_info_ll_back)
    public void onBackClick() {

        finish();
    }

    /**
     * 右上角
     */
    @OnClick(R.id.user_info_ll_option)
    public void onOptionClick() {

        mPresenter.OptionClick();

    }


    /**
     * 更换头像
     */
    @OnClick(R.id.user_info_tv_icon)
    public void onIconClick() {

        mPresenter.createIconPhoto();

    }

    /**
     * 修改
     *
     * @param view
     */
    @OnClick({R.id.user_info_tv_modify, R.id.user_info_tv_modifys})
    public void onModifyClick(View view) {

        mPresenter.ModifyClick();
    }


    /**
     * 头像未审核
     */
    @OnClick(R.id.user_info_ll_icon_hint)
    public void onIconHintClick() {

        mPresenter.IconIintClick();
    }

    /**
     * 手机未认证
     */
    @OnClick(R.id.user_info_tv_phone_btn)
    public void onIdentifyPhoneClick() {
        mPresenter.IdentifyPhoneClick();
    }

    /**
     * 视频未认证
     */
    @OnClick({R.id.user_info_tv_video_btn, R.id.user_info_iv_video_invite})
    public void onIdentifyVideoClick() {
        mPresenter.IdentifyVideoClick();
    }

    /**
     * 冒泡
     */
    @OnClick(R.id.user_info_ll_bubble)
    public void onBubbleClick() {
        mPresenter.BubbleClick();
    }

    /**
     * 星运，运势
     */
    @OnClick(R.id.user_info_ll_match_line)
    public void onMatchLineClick() {
        mPresenter.MatchLineClick();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPresenter.onActivityReenter(requestCode, resultCode, data);
    }

    /**
     * 聊天
     */
    @OnClick(R.id.user_info_ll_chat)
    public void onChatClick() {
        mPresenter.ChatClick();
    }

    @OnClick(R.id.user_info_ll_like)
    public void onLikeClick() {

        Logger.e("点击喜欢的人了");

        mPresenter.LikeClick();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.onRefreshPage();


    }

    @OnClick(R.id.user_info_tv_dating_invite)
    public void onDatingInviteClick() {

        mPresenter.DatingInviteClick();
    }
}
