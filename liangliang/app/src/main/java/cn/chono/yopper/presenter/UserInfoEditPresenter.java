package cn.chono.yopper.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.hwangjr.rxbus.RxBus;

import java.util.HashMap;
import java.util.Map;

import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.Profile;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.StringUtils;
import cn.chono.yopper.utils.UserInfoUtils;
import rx.Subscription;

/**
 * Created by cc on 16/7/30.
 */
public class UserInfoEditPresenter extends BasePresenter<UserInfoEditContract.View> implements UserInfoEditContract.Presenter {

    @Override
    public void detachView() {
        super.detachView();
        RxBus.get().unregister(this);
    }

    public UserInfoEditPresenter(Activity activity, UserInfoEditContract.View view) {
        super(activity, view);
        RxBus.get().register(this);
    }

    @Override
    public void getDbUserData() {

        UserInfo userInfo = DbHelperUtils.getUserInfo(mView.getUserID());

        if (userInfo != null) {


            userdto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);

            if (userdto != null) {

                setDateToView(userdto);
            }
        }
    }

    boolean isBackHint;

    @Override
    public void UserName() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.USER_NAME, userdto.getProfile().getName());

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6600);

        mView.EditUserNameActivity(bundle, 6600);

        isBackHint = true;
    }

    @Override
    public void UserAge() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        if (userdto.getProfile().getAge() == null) {

            bundle.putInt(YpSettings.USER_AGE, 14);

        } else {

            bundle.putInt(YpSettings.USER_AGE, userdto.getProfile().getAge());
        }

        bundle.putBoolean(YpSettings.USER_AGE_VISIBILITY, userdto.getProfile().isBirthdayPrivacy());

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6608);


        mView.EditUserAgeActivity(bundle, 6608);

        isBackHint = true;
    }

    @Override
    public void UserHeight() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        if (userdto.getProfile().getHeight() == null) {

            bundle.putInt(YpSettings.USER_HEIGHT, 150);

        } else {

            bundle.putInt(YpSettings.USER_HEIGHT, userdto.getProfile().getHeight());
        }

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6606);


        mView.EditUserHeightActivity(bundle, 6606);

        isBackHint = true;
    }

    @Override
    public void UserWeight() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        if (userdto.getProfile().getWeight() == null) {

            bundle.putInt(YpSettings.USER_WEIGHT, 30);

        } else {

            bundle.putInt(YpSettings.USER_WEIGHT, userdto.getProfile().getWeight());
        }

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6607);


        mView.EditUserWeightActivity(bundle, 6607);

        isBackHint = true;

    }

    @Override
    public void UserProfession() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.USER_PROFESSION, userdto.getProfile().getCareer());

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6604);

        mView.EditUserProfessionActivity(bundle, 6604);

        isBackHint = true;
    }

    @Override
    public void UserIncome() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        if (userdto.getProfile().getIncomeLevel() == null) {

            bundle.putInt(YpSettings.USER_INCOME, 1);

        } else {

            bundle.putInt(YpSettings.USER_INCOME, userdto.getProfile().getIncomeLevel());
        }


        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6603);


        mView.EditUserIncomeActivity(bundle, 6603);

        isBackHint = true;


    }

    @Override
    public void UserEmotional() {

        if (userdto == null) return;


        Bundle bundle = new Bundle();

        if (userdto.getProfile().getRelationship() == null) {

            bundle.putInt(YpSettings.USER_EMOTIONAL, Constant.Emotional_Type_secrecy);

        } else {

            bundle.putInt(YpSettings.USER_EMOTIONAL, userdto.getProfile().getRelationship());

        }

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6605);

        mView.EditUserEmotionalActivity(bundle, 6605);

        isBackHint = true;

    }

    @Override
    public void UserLable() {

        if (userdto == null) return;


        Bundle bundle = new Bundle();

        if (CheckUtil.isEmpty(userdto.getProfile().getTags())) {

            bundle.putString(YpSettings.USER_LABLE, "");

        } else {

            bundle.putString(YpSettings.USER_LABLE, userdto.getProfile().getTags());
        }

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6609);


        mView.EditUserLableActivity(bundle, 6609);

        isBackHint = true;

    }

    @Override
    public void UserHomeTown() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.USER_HOMETOWN, userdto.getProfile().getHometown());

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6610);


        mView.EditUserHomeTownActivity(bundle, 6610);

        isBackHint = true;
    }

    @Override
    public void UserDisLike() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.USER_DISLIKE, userdto.getProfile().getDislikes());

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6601);


        mView.EditUserDisLikeActivity(bundle, 6601);
    }

    @Override
    public void UserLike() {

        if (userdto == null) return;

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.USER_LIKE, userdto.getProfile().getLikes());

        bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6602);


        mView.EditUserLikeActivity(bundle, 6602);

        isBackHint = true;
    }

    @Override
    public void toBack() {


        if (isBackHint) {


            mView.showCreateHintOperateDialog("提示", "放弃修改资料?", "放弃", "继续编辑", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {

                    mView.dismissCreateHintOperateDialog();

                    mView.onFinish();




                }

                @Override
                public void onEnsure(View view, Object... obj) {

                    mView.dismissCreateHintOperateDialog();

                }
            });

            return;

        }


        mView.onFinish();

    }

    @Override
    public void submit() {


        int age = 0;

        if (userdto != null && userdto.getProfile() != null && userdto.getProfile().getAge() != null) {
            age = userdto.getProfile().getAge();
        }

        if (age >= 100) {

            mView.showDisCoverNetToast("年龄不能大于等于100");

            return;
        }
        // 保存修改


        submitData();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle bundle = null;

        if (data != null) {

            bundle = data.getExtras();
        }

        switch (resultCode) {

            case 6600://昵称编辑

                if (bundle != null) {

                    String user_name = bundle.getString(YpSettings.USER_NAME);


                    if (!CheckUtil.isEmpty(user_name)) {

                        mView.user_info_tv_detail_name(user_name);

                        userdto.getProfile().setName(user_name);
                    }
                }
                break;

            case 6601://不喜欢

                if (bundle != null) {

                    String dislike_str = bundle.getString(YpSettings.USER_DISLIKE);

                    mView.user_info_tv_dislike(StringUtils.isEmpty(dislike_str));

                    userdto.getProfile().setDislikes(StringUtils.isEmpty(dislike_str));


                }
                break;

            case 6602://喜欢

                if (bundle != null) {

                    String like_str = bundle.getString(YpSettings.USER_LIKE);


                    mView.user_info_tv_like(StringUtils.isEmpty(like_str));

                    userdto.getProfile().setLikes(StringUtils.isEmpty(like_str));


                }
                break;

            case 6603://收入

                if (bundle != null) {

                    int income_id = bundle.getInt(YpSettings.USER_INCOME);

                    userdto.getProfile().setIncomeLevel(income_id);

                    String income = UserInfoUtils.getIncome(income_id);

                    mView.user_info_tv_income(StringUtils.isEmpty(income));


                }
                break;


            case 6604://职位

                if (bundle != null) {

                    String pro_str = bundle.getString(YpSettings.USER_PROFESSION);

                    mView.user_info_tv_profession(StringUtils.isEmpty(pro_str));

                    userdto.getProfile().setCareer(StringUtils.isEmpty(pro_str));


                }
                break;

            case 6605://情感

                if (bundle != null) {

                    int emo_type = bundle.getInt(YpSettings.USER_EMOTIONAL);

                    String emotional = UserInfoUtils.getEmotional(emo_type);

                    userdto.getProfile().setRelationship(emo_type);

                    mView.user_info_tv_emotional(StringUtils.isEmpty(emotional));


                }
                break;

            case 6606://身高

                if (bundle != null) {

                    int height_num = bundle.getInt(YpSettings.USER_HEIGHT);

                    userdto.getProfile().setHeight(height_num);

                    mView.user_info_tv_height(height_num + "cm");
                }
                break;

            case 6607://体重

                if (bundle != null) {

                    int weight_num = bundle.getInt(YpSettings.USER_WEIGHT);

                    userdto.getProfile().setWeight(weight_num);

                    mView.user_info_tv_weight(weight_num + "kg");
                }
                break;

            case 6608://年龄

                if (bundle != null) {

                    boolean age_visibility = bundle.getBoolean(YpSettings.USER_AGE_VISIBILITY);

                    int age = bundle.getInt(YpSettings.USER_AGE);

                    if (age_visibility) {


                        mView.user_info_tv_age_levelGone();

                        mView.user_info_tv_age("保密");

                    } else {

                        mView.user_info_tv_age_levelVisible();

                        mView.user_info_tv_age_level((UserInfoUtils.getBornPeriod(age)));

                        mView.user_info_tv_age(age + "");


                    }

                    userdto.getProfile().setAge(age);

                    userdto.getProfile().setBirthdayPrivacy(age_visibility);
                    //保存显示还是不显示年龄
                }
                break;

            case 6609://标签

                if (bundle != null) {

                    String allLableList = bundle.getString(YpSettings.USER_LABLE);

                    if (!CheckUtil.isEmpty(allLableList) && !allLableList.equals("null")) {

                        mView.user_info_tv_lableGone();

                        mView.user_info_rv_lableVisible();

                        userdto.getProfile().setTags(allLableList);

                        if (allLableList.contains(",")) {

                            String tas[] = allLableList.split(",");

                            mView.user_info_rv_lable(tas);

                        } else {

                            String tas[] = new String[]{allLableList};

                            mView.user_info_rv_lable(tas);

                        }


                    } else {

                        userdto.getProfile().setTags("");

                        mView.user_info_tv_age_levelVisible();

                        mView.user_info_rv_lableGone();

                    }
                }

                break;

            case 6610://家乡

                if (bundle != null) {

                    String home_str = bundle.getString(YpSettings.USER_HOMETOWN);

                    mView.user_info_tv_home(StringUtils.isEmpty(home_str));

                    userdto.getProfile().setHometown(StringUtils.isEmpty(home_str));


                }
                break;


            default:
                break;
        }


    }


    UserDto userdto;


    private void submitData() {

        Profile profile = userdto.getProfile();

        if (TextUtils.isEmpty(profile.getName())) {

            return;
        }

        mView.showLoadingDialog("正在更新个人资料");

        Map<String, Object> map = new HashMap<>();


        map.put("name", profile.getName());

        if (profile.getAge() != null)

            map.put("age", profile.getAge());

        if (profile.getRelationship() != null)

            map.put("relationship", profile.getRelationship());

        if (!TextUtils.isEmpty(profile.getCareer()))

            map.put("career", profile.getCareer());

        if (profile.getHeight() != null)

            map.put("height", profile.getHeight());

        if (profile.getWeight() != null)

            map.put("weight", profile.getWeight());

        if (!TextUtils.isEmpty(profile.getLikes()))

            map.put("likes", profile.getLikes());

        if (!TextUtils.isEmpty(profile.getDislikes()))

            map.put("dislikes", profile.getDislikes());

        if (!TextUtils.isEmpty(profile.getHometown()))

            map.put("hometown", profile.getHometown());

        if (profile.getIncomeLevel() != null)

            map.put("incomeLevel", profile.getIncomeLevel());

        if (!TextUtils.isEmpty(profile.getTags()))

            map.put("tags", profile.getTags());


        map.put("birthdayPrivacy", profile.isBirthdayPrivacy());


        Subscription subscription = mHttpApi.putUserInfo(mView.getUserID(), map)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(b -> {


                    mView.dismissLoadingDialog();


                    if (b) {

                        CommonItemEvent commonItemEvent=new CommonItemEvent();

                        commonItemEvent.event=userdto;

                        RxBus.get().post("RefreshDataEvant",commonItemEvent);

                        String jsonstr = JsonUtils.toJson(userdto);

                        DbHelperUtils.saveUserInfo(mView.getUserID(), jsonstr);

                        mView.showCreateSuccessHintDialog("保存成功!");

                        successtimer = new SuccessTimer(2000, 1000);

                        successtimer.start();




                    }


                }, throwable -> {

                    mView.dismissLoadingDialog();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);

                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }


                });

        addSubscrebe(subscription);


    }


    SuccessTimer successtimer;

    private class SuccessTimer extends CountDownTimer {

        public SuccessTimer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {

            mView.dismissCreateSuccessHintDialog();

            mView.onFinish();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


    private void setDateToView(UserDto userdto) {


        if (userdto == null) return;


        mView.user_info_tv_title(StringUtils.isEmpty(userdto.getProfile().getName()));

        mView.user_info_tv_detail_name(userdto.getProfile().getName());


        //讨厌
        mView.user_info_tv_dislike(StringUtils.isEmpty(userdto.getProfile().getDislikes()));
        //喜欢
        mView.user_info_tv_like(StringUtils.isEmpty(userdto.getProfile().getLikes()));
        //家乡
        mView.user_info_tv_home(StringUtils.isEmpty(userdto.getProfile().getHometown()));


        //收入
        if (userdto.getProfile().getIncomeLevel() != null) {

            String incomeStr = UserInfoUtils.getIncome(userdto.getProfile().getIncomeLevel());

            if (TextUtils.equals(incomeStr, "未填写")) {

                mView.user_info_tv_income("");


            } else {
                mView.user_info_tv_income(StringUtils.isEmpty(incomeStr));
            }


        } else {
            mView.user_info_tv_income("");
        }

        //职业
        mView.user_info_tv_profession(StringUtils.isEmpty(userdto.getProfile().getCareer()));

        //情感状态
        if (userdto.getProfile().getRelationship() == null) {

            mView.user_info_tv_emotional(StringUtils.isEmpty(""));

        } else {

            String emotional = UserInfoUtils.getEmotional(userdto.getProfile().getRelationship());

            if (TextUtils.equals(emotional, "未填写")) {

                mView.user_info_tv_emotional(StringUtils.isEmpty(""));
            } else {
                mView.user_info_tv_emotional(StringUtils.isEmpty(emotional));
            }


        }


        //身高
        if (userdto.getProfile().getHeight() == null) {

            mView.user_info_tv_height("");


        } else {

            mView.user_info_tv_height(userdto.getProfile().getHeight() + "cm");
        }

        //体重
        if (userdto.getProfile().getWeight() == null) {

            mView.user_info_tv_weight("");

        } else {

            mView.user_info_tv_weight(userdto.getProfile().getWeight() + "kg");

        }

        //年龄
        if (userdto.getProfile().getAge() == null) {

            mView.user_info_tv_age("");

            mView.user_info_tv_age_levelGone();

        } else {

            if (userdto.getProfile().isBirthdayPrivacy()) {

                mView.user_info_tv_age_levelGone();

                mView.user_info_tv_age("保密");

            } else {

                mView.user_info_tv_age_levelVisible();

                mView.user_info_tv_age_level(UserInfoUtils.getBornPeriod(userdto.getProfile().getAge()));

                mView.user_info_tv_age(userdto.getProfile().getAge() + "");
            }

        }


        //标签
        //		isFirst++;

        if (!CheckUtil.isEmpty(userdto.getProfile().getTags()) && !userdto.getProfile().getTags().equals("null")) {

            mView.user_info_tv_lableGone();

            mView.user_info_rv_lableVisible();

            String tagStr = userdto.getProfile().getTags();


            String tas[] = tagStr.split(",");

            mView.user_info_rv_lable(tas);


        } else {


            mView.user_info_tv_lableVisible();

            mView.user_info_rv_lableGone();

        }


    }
}
