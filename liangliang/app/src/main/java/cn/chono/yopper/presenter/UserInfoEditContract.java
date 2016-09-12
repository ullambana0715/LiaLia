package cn.chono.yopper.presenter;

import android.content.Intent;
import android.os.Bundle;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.utils.BackCallListener;

/**
 * Created by cc on 16/7/30.
 */
public interface UserInfoEditContract {

    interface View extends IView {


        int getUserID();

        void user_info_tv_age_levelVisible();

        void user_info_tv_age_levelGone();

        void user_info_tv_lableVisible();

        void user_info_tv_lableGone();

        void user_info_rv_lableVisible();

        void user_info_rv_lableGone();


        void user_info_tv_title(String msg);


        void user_info_tv_detail_name(String msg);


        void user_info_tv_age(String msg);

        void user_info_tv_age_level(String msg);

        void user_info_tv_emotional(String msg);

        void user_info_tv_height(String msg);

        void user_info_tv_weight(String msg);

        void user_info_tv_profession(String msg);

        void user_info_tv_income(String msg);

        void user_info_rv_lable(Object o);


        void user_info_tv_home(String msg);

        void user_info_tv_like(String msg);

        void user_info_tv_dislike(String msg);

        void showCreateHintOperateDialog(String title, String msg, String msg1, String msg2, BackCallListener backCallListener);


        void dismissCreateHintOperateDialog();

        void showCreateSuccessHintDialog(String msg);

        void dismissCreateSuccessHintDialog();


        void showLoadingDialog(String msg);

        void dismissLoadingDialog();

        void showDisCoverNetToast(String msg);

        void onFinish();


        void EditUserNameActivity(Bundle bundle, int code);

        void EditUserAgeActivity(Bundle bundle, int code);

        void EditUserHeightActivity(Bundle bundle, int code);

        void EditUserWeightActivity(Bundle bundle, int code);

        void EditUserProfessionActivity(Bundle bundle, int code);

        void EditUserIncomeActivity(Bundle bundle, int code);

        void EditUserEmotionalActivity(Bundle bundle, int code);

        void EditUserLableActivity(Bundle bundle, int code);

        void EditUserHomeTownActivity(Bundle bundle, int code);

        void EditUserDisLikeActivity(Bundle bundle, int code);

        void EditUserLikeActivity(Bundle bundle, int code);


    }


    interface Presenter extends IPresenter {

        void getDbUserData();

        void UserName();

        void UserAge();

        void UserHeight();

        void UserWeight();

        void UserProfession();

        void UserIncome();

        void UserEmotional();

        void UserLable();

        void UserHomeTown();

        void UserDisLike();

        void UserLike();

        void toBack();

        void submit();

        void onActivityResult(int requestCode, int resultCode, Intent data);


    }
}
