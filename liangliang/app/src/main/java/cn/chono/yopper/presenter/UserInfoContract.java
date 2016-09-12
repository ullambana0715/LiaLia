package cn.chono.yopper.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.RePortCallListener;

/**
 * Created by cc on 16/7/21.
 */
public interface UserInfoContract {


    interface View extends IView {


        void user_info_ll_optionVisible();

        void user_info_ll_optionGone();

        void user_info_iv_hotVisible();

        void user_info_iv_hotGone();

        void user_info_iv_video_stateVisible();

        void user_info_iv_video_stateGone();

        void user_info_iv_vipVisible();

        void user_info_iv_vipGone();

        void user_info_iv_activity_talentVisible();

        void user_info_iv_activity_talentGone();

        void user_info_tv_iconVisible();

        void user_info_tv_iconGone();

        void user_info_iv_video_inviteVisible();

        void user_info_iv_video_inviteGone();

        void user_info_ll_icon_hintVisible();

        void user_info_ll_icon_hintGone();

        void user_info_tv_modifyVisible();

        void user_info_tv_modifyGone();

        void user_info_v_match_lineVisible();

        void user_info_v_match_lineGone();

        void user_info_ll_match_lineVisible();

        void user_info_ll_match_lineGone();

        void user_info_ll_giftVisible();

        void user_info_ll_giftGone();

        void user_info_tv_appointmentVisible();

        void user_info_tv_appointmentGone();

        void user_info_ll_bubbleVisible();

        void user_info_ll_bubbleGone();

        void user_info_ll_bubble_contentVisible();

        void user_info_ll_bubble_contentGone();

        void user_info_tv_isprofilecompleteVisible();

        void user_info_tv_isprofilecompleteGone();

        void user_info_tv_modifysVisible();

        void user_info_tv_modifysGone();

        void user_info_tv_age_levelVisible();

        void user_info_tv_age_levelGone();

        void user_info_tv_lableVisible();

        void user_info_tv_lableGone();

        void user_info_rv_lableVisible();

        void user_info_rv_lableGone();

        void user_info_tv_phone_btnVisible();

        void user_info_tv_phone_btnGone();

        void user_info_bottom_layoutVisible();

        void user_info_bottom_layoutGone();

        void user_info_tv_dating_inviteVisible();

        void user_info_tv_dating_inviteGone();

        void user_info_tv_videoVisible();

        void user_info_tv_videoGone();


        void user_info_rv_videoVisible();

        void user_info_rv_videoGone();

        void user_info_tv_distanceGone();

        void user_info_tv_distanceVisible();



        void user_info_tv_private_photoVisible();

        void user_info_tv_private_photoGone();



        void user_info_rv_private_photoVisible();

        void user_info_rv_private_photoGone();


        void user_info_iv_vip(int drawableId);


        void user_info_tv_title(String msg);

        void user_info_iv_icon(String msg);

        void user_info_tv_detail_name(String msg);

        void user_info_tv_detail_sex(Object o);

        void user_info_tv_modify(String msg);

        void user_info_tv_time(String msg);

        void user_info_tv_distance(String msg);

        void user_info_iv_hor(Object o);

        void user_info_tv_hor(String msg);

        void user_info_rb_score(float o);

        void user_info_rv_photo(Object o);

        void user_info_rv_private_photo(Object o, boolean isUnlockPrivacyAlbum);

        void user_info_rv_video(Object o);

        void user_info_rv_gift(Object o);

        void user_info_tv_appointment(String msg);

        void user_info_rv_appointment(Object o, int loginUserId, int userID, int sex);

        void user_info_tv_bubble(String msg);

        void user_info_iv_bubble_img(String msg);
        void user_info_iv_bubble_img_private(String msg);

        void user_info_tv_bubble_title(String msg);

        void user_info_tv_bubble_content(String msg);


        void user_info_tv_age(String msg);

        void user_info_tv_age_level(String msg);

        void user_info_tv_emotional(String msg);

        void user_info_tv_height(String msg);

        void user_info_tv_weight(String msg);

        void user_info_tv_profession(String msg);

        void user_info_tv_income(String msg);

        void user_info_rv_lable(Object o);

        void user_info_tv_phone_state(String msg);

        void user_info_tv_phone_btn(String msg);

        void user_info_tv_video_state(String msg);

        void user_info_tv_video_btn(String msg);

        void user_info_tv_video_btn_color(int colorId);

        void user_info_tv_home(String msg);

        void user_info_tv_like(String msg);

        void user_info_tv_dislike(String msg);

        void user_info_tv_ID(String msg);

        void user_info_tv_everyday_match(String msg);

        void user_info_iv_like_icon(Object o);

        void user_info_tv_like_btn(String msg);

        void user_info_iv_video_invite(int id);

        void user_album_border(int id);


        void showDisCoverNetToast(String msg);


        void showRePortDialog(String title, String one, String two, String three, String four, String five, RePortCallListener rePortCallListener);

        void dismissRePortDialog();

        void showLoadingDialog();

        void dismissLoadingDialog();

        void showTimerCreateSuccessHintDialog(String msg);

        void dismissTimerCreateSuccessHintDialog();

        void showCreatePhotoDialog(android.view.View.OnClickListener onClickListener);

        void dismissCreatePhotoDialog();

        void showCreateHintOperateDialog(String title, String content, String msg1, String msg2, BackCallListener backCallListener);


        void dismissCreateHintOperateDialog();

        void showCreateNotCanPublishDatingHintDialog(List<DatingRequirment> list, String sureStr, final BackCallListener backCallListener);

        void dismissCreateNotCanPublishDatingHintDialog();


        void showCreateHotHintDialog(BackCallListener backCallListener);

        void dismissCreateHotHintDialog();

        void showBlockDialog(String title, String lift, String right, BackCallListener backCallListener);

        void dismissBlockDialog();


        void AlbumViewLargerImageActivity(Bundle bundle);

        void PrivacyAlbumViewLargerImageActivity(Bundle bundle);


        void VideoDetailGetActivity(Bundle bundle);

        void DatingDetailActivity(Bundle bundle);

        void ChatActivity(Bundle bundle);

        void AppointPublishTypeSelectActivity(Bundle bundle);

        void UserInfoEditActivity(Bundle bundle);

        void SimpleWebViewActivity(Bundle bundle);

        void VipOpenedActivity(Bundle bundle);

        void VipRenewalsActivity(Bundle bundle);

        void UserInfoActivity(Bundle bundle);

        void VideoCoverActivity(Bundle bundle);

        void AppleListActivity(Bundle bundle);

        void SettingPhoneActivity(Bundle bundle);

        void MyBubblingActivity(Bundle bundle);

        void VideoActivity(Bundle bundle);

        void onFinish();

        Context ApplicationContext();


    }


    interface Presenter extends IPresenter {

        void initDataAndLoadData();

        void onResume();

        void getDbUserData();

        void getHttpUserData();

        void doBlockRequest();

        void doReport(String msg);

        void createIconPhoto();

        void onActivityReenter(int requestCode, int resultCode, Intent data);

        void OptionClick();

        void IconIintClick();

        void IdentifyPhoneClick();

        void IdentifyVideoClick();

        void BubbleClick();

        void MatchLineClick();

        void ModifyClick();

        void ChatClick();

        void LikeClick();

        void DatingInviteClick();

        void onRefreshPage();

    }

}
