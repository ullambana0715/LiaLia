package cn.chono.yopper.model.like;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import cn.chono.yopper.R;
import cn.chono.yopper.activity.appointment.DatingDetailActivity;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.entity.likeBean.ILike;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;
import rx.Subscription;

/**
 * Created by jianghua on 2016/7/18.
 */
public class ILikePresenter extends BasePresenter<ILikeContract.ILikeView> implements ILikeContract.ILikePresenter {

    public ILikePresenter(Activity activity, ILikeContract.ILikeView view) {
        super(activity, view);
    }

    @Override
    public void getData(int type, boolean isRefresh) {

        Subscription subscription = null;


//        mView.onGoneRemind();

        if (type == YpSettings.LIKE_TYPE_ILIKE) {
            subscription = HttpFactory.getHttpApi().getIlike(0)
                    .compose(SchedulersCompat.applyIoSchedulers())
                    .compose(RxResultHelper.handleResult())
                    .subscribe(likeBase -> {

                        if (null != likeBase.getList() && likeBase.getList().size() > 0) {
                            Logger.e("fragment 有数据");
                            mView.setData(likeBase.getList());

                            String nextQuery = likeBase.getNextQuery();

                            if (!TextUtils.isEmpty(nextQuery)) {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "", nextQuery);
                            } else {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "", "");
                            }

                        } else {

                            if (!isRefresh) {
                                Logger.e("fragment 没有数据");
                                mView.handNoDataError();

                            }
                            saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "", "");
                        }
                        mView.onRefreshFinish();

                    }, throwable -> {

                        Logger.e("我喜欢的人：：：异常了" + isRefresh);

                        if (!isRefresh) {

                            mView.handNetError();
                            return;

                        }

                        mView.onRefreshError("网络异常，请稍后再试");
                    });

        } else if (type == YpSettings.LIKE_TYPE_LIKEME) {
            subscription = HttpFactory.getHttpApi().getLikeMe(0)
                    .compose(SchedulersCompat.applyIoSchedulers())
                    .compose(RxResultHelper.handleResult())
                    .subscribe(likeMe -> {

                        if (null != likeMe.getList() && likeMe.getList().size() > 0) {
                            mView.setData(likeMe.getList());
                            String nextQuery = likeMe.getNextQuery();
                            Logger.e("喜欢我的人：：：" + likeMe.toString());
                            if (!TextUtils.isEmpty(nextQuery)) {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "", nextQuery);
                            } else {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "", "");
                            }

                        } else {

                            if (!isRefresh) {
                                mView.handNoDataError();
                            }
                            saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "", "");
                        }
                        mView.onRefreshFinish();

                    }, throwable -> {
                        Logger.e("喜欢我的人：：：异常了" + isRefresh);
                        if (!isRefresh) {
                            mView.handNetError();
                            return;

                        }
                        mView.onRefreshError("网络异常，请稍后再试");
                    });

        } else if (type == YpSettings.LIKE_TYPE_LIKEEACH) {
            subscription = HttpFactory.getHttpApi().getLikeEach(0)
                    .compose(SchedulersCompat.applyIoSchedulers())
                    .compose(RxResultHelper.handleResult())
                    .subscribe(likeEach -> {

                        if (null != likeEach.getList() && likeEach.getList().size() > 0) {
                            mView.setData(likeEach.getList());
                            String nextQuery = likeEach.getNextQuery();
                            Logger.e("互相喜欢的人：：：" + likeEach.toString());
                            if (!TextUtils.isEmpty(nextQuery)) {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEEACH + "", nextQuery);
                            } else {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEEACH + "", "");
                            }

                        } else {

                            if (!isRefresh) {
                                mView.handNoDataError();
                            }
                            saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEEACH + "", "");
                        }
                        mView.onRefreshFinish();

                    }, throwable -> {
                        Logger.e("互相喜欢的人：：：异常了" + isRefresh);
                        if (!isRefresh) {
                            mView.handNetError();
                            return;

                        }
                        mView.onRefreshError("网络异常，请稍后再试");
                    });

        }

        if (null != subscription) {
            addSubscrebe(subscription);
        }
    }

    @Override
    public void getMoreData(int type) {
        String start = "";
        Subscription subscription = null;
        if (type == YpSettings.LIKE_TYPE_ILIKE) {
            start = getMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "");
            if (!TextUtils.isEmpty(start)) {
                Logger.e("开始加载更多");
                subscription = mHttpApi.getIlike(Integer.parseInt(start))
                        .compose(SchedulersCompat.applyIoSchedulers())
                        .compose(RxResultHelper.handleResult())
                        .subscribe(ilikebase -> {

                            String nextQuery = "";
                            if (null != ilikebase.getList() && ilikebase.getList().size() != 0) {
                                mView.addData(ilikebase.getList());
                                nextQuery = ilikebase.getNextQuery();
                                Logger.e("更多我喜欢的人：：：" + ilikebase.getNextQuery());

                            } else {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "", "");
                            }

                            if (!TextUtils.isEmpty(nextQuery)) {
                                mView.canLoadMore(true);
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "", nextQuery);
                            } else {
                                mView.canLoadMore(false);
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "", "");
                            }

                        }, throwable -> {
                            mView.onLoadMoreError(ErrorHanding.handleError(throwable));
                        });

            } else {
                Logger.e("不能加载更多");
                mView.canLoadMore(false);
            }

        } else if (type == YpSettings.LIKE_TYPE_LIKEME) {
            start = getMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "");
            if (!TextUtils.isEmpty(start)) {

                subscription = HttpFactory.getHttpApi().getLikeMe(Integer.parseInt(start))
                        .compose(SchedulersCompat.applyIoSchedulers())
                        .compose(RxResultHelper.handleResult())
                        .subscribe(ilikebase -> {

                            String nextQuery = "";
                            if (null != ilikebase.getList() && ilikebase.getList().size() != 0) {
                                mView.addData(ilikebase.getList());
                                nextQuery = ilikebase.getNextQuery();
                                Logger.e("喜欢我的人：：：" + ilikebase.toString());

                            } else {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_ILIKE + "", "");
                            }

                            if (!TextUtils.isEmpty(nextQuery)) {
                                mView.canLoadMore(true);
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "", nextQuery);
                            } else {
                                mView.canLoadMore(false);
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "", "");
                            }

                        }, throwable -> {
                            mView.onLoadMoreError(ErrorHanding.handleError(throwable));
                        });

            } else {
                mView.canLoadMore(false);
            }

        } else if (type == YpSettings.LIKE_TYPE_LIKEEACH) {
            start = getMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEEACH + "");
            if (!TextUtils.isEmpty(start)) {

                subscription = HttpFactory.getHttpApi().getLikeEach(Integer.parseInt(start))
                        .compose(SchedulersCompat.applyIoSchedulers())
                        .compose(RxResultHelper.handleResult())
                        .subscribe(ilikebase -> {

                            String nextQuery = "";
                            if (null != ilikebase.getList() && ilikebase.getList().size() == 0) {
                                mView.addData(ilikebase.getList());
                                nextQuery = ilikebase.getNextQuery();
                                Logger.e("互相喜欢的人：：：" + ilikebase.toString());

                            } else {
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEEACH + "", "");
                            }

                            if (!TextUtils.isEmpty(nextQuery)) {
                                mView.canLoadMore(true);
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "", nextQuery);
                            } else {
                                mView.canLoadMore(false);
                                saveMoreUrl(LoginUser.getInstance().getUserId() + YpSettings.LIKE_TYPE_LIKEME + "", "");
                            }

                        }, throwable -> {
                            mView.onLoadMoreError(ErrorHanding.handleError(throwable));
                        });

            } else {
                mView.canLoadMore(false);
            }

        }

        if (null != subscription) {
            addSubscrebe(subscription);
        }
    }

    private void saveMoreUrl(String key, String page) {
        SharedPreferences sp = mActivity.getSharedPreferences(mActivity.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, page);
        editor.commit();
    }

    private String getMoreUrl(String key) {
        SharedPreferences sp = mActivity.getSharedPreferences(mActivity.getPackageName(), Context.MODE_PRIVATE);
        String page = sp.getString(key, "");
        String result = "";
        if (!TextUtils.isEmpty(page)) {
            result = page.substring(page.indexOf("=") + 1);
        }

        return result;
    }

    @Override
    public void onAdapterIconClick(ILike.UserInfoBean il, int type) {

        String fromPage;

        if (YpSettings.LIKE_TYPE_ILIKE == type) {

            fromPage = YpSettings.LIKE_ILIKE_PAGE;

        } else if (YpSettings.LIKE_TYPE_LIKEME == type) {

            fromPage = YpSettings.LIKE_LIKEME_PAGE;

        } else {

            fromPage = YpSettings.LIKE_LIKEEACH_PAGE;

        }

        Bundle bundle = new Bundle();
        bundle.putInt(YpSettings.USERID, il.getId());
        bundle.putString(YpSettings.FROM_PAGE, fromPage);
        ActivityUtil.jump(mActivity, UserInfoActivity.class, bundle, 0, 100);
    }

    @Override
    public void OnTakePartEvent(String actionId) {
        if (CheckUtil.isEmpty(actionId)) {
            return;
        }

        Logger.e("活动的ID =======" + actionId);
        //去活动详情

        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "活动详情");
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "activity/dtails?activityId=" + actionId);
        bundle.putString(YpSettings.ACTIVITY_ID, actionId);

        bundle.putInt(YpSettings.SOURCE_YTPE_KEY, 600);

        ActivityUtil.jump(mActivity, SimpleWebViewActivity.class, bundle, 0, 100);
    }

    @Override
    public void OnDatingsEvent(int userId, String datingId) {
        //去邀约详情
        if (CheckUtil.isEmpty(datingId)) {
            return;
        }
        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, userId);

        bundle.putString(YpSettings.DATINGS_ID, datingId);

        ActivityUtil.jump(mActivity, DatingDetailActivity.class, bundle, 0, 100);

    }

    @Override
    public void onItemLongClick(int userid, int position, int type) {
        showBlockDialog(userid, position, type);
    }

    @Override
    public void onUnLockUser(int userId, int type) {
        checkKey(userId, type);
    }

    public void checkKey(int userId, int type) {

        loadDialog = DialogUtil.LoadingDialog(mActivity, null);

        Subscription subscription = HttpFactory.getHttpApi().onCheckKey()

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(unlock -> {

                    loadDialog.dismiss();

                    Logger.e("校验解锁相关信息：" + unlock.toString());

                    ForegroundColorSpan defaultColor = new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_666666));
                    ForegroundColorSpan ff7462Color = new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_ff7462));

                    if (unlock.getResult() == 0) {//0，钥匙充足，1钥匙不足

                        String html = "<html><body>解锁需要<font color=\"#ff7462\">"
                                + unlock.getConsumerKeyCount() + "</font>把钥匙"
                                + "<br>您现在有<font color=\"#ff7462\">" + unlock.getUserKeyCount() + "</font>把钥匙</body></html>";

                        showUnlockDialog(userId, html, "立即使用", "暂不使用", false, type);

                    } else {

                        String html = "<html><body>解锁需要<font color=\"#ff7462\">"
                                + unlock.getConsumerKeyCount() + "</font>把钥匙"
                                + "<br>您今天钥匙已被用光</body></html>";

                        showUnlockDialog(userId, html, "快速获取钥匙", "取消", true, type);
                    }
                }, throwable -> {

                    loadDialog.dismiss();

                    handleError(throwable);

                });
        addSubscrebe(subscription);
    }

    private Dialog blockDialog;
    private Dialog unLockDialog;
    private Dialog loadDialog;

    public void showBlockDialog(final int id, final int position, int type) {
        loadDialog = DialogUtil.LoadingDialog(mActivity, null);
        // 初始化一个自定义的Dialog
        blockDialog = new MyDialog(mActivity, R.style.MyDialog, R.layout.select_operate_dialog_layout,
                contentView -> {


                    TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                    LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                    LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                    LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                    TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                    select_operate_dialog_title_tv.setText("操作");
                    select_operate_dialog_one_tv.setText("取消喜欢");

                    select_operate_dialog_one_layout.setVisibility(View.VISIBLE);

                    select_operate_dialog_two_layout.setVisibility(View.GONE);

                    select_operate_dialog_three_layout.setVisibility(View.GONE);


                    // 点击升级按钮
                    select_operate_dialog_one_tv.setOnClickListener(v -> {
                        ViewsUtils.preventViewMultipleClick(v, 3000);
                        blockDialog.dismiss();
                        loadDialog.show();
                        Subscription subscription = HttpFactory.getHttpApi().cancelLike(id, false)
                                .compose(SchedulersCompat.applyIoSchedulers())
                                .compose(RxResultHelper.handleResult())
                                .subscribe(cancelIlike -> {
                                    if (cancelIlike.getLikeResult() == 0) {

                                        loadDialog.dismiss();

                                        mView.showMessage("取消成功");

                                        if (type == YpSettings.LIKE_TYPE_ILIKE) {

                                            RxBus.get().post("ilikecancelSuccess", position);

                                        } else if (type == YpSettings.LIKE_TYPE_LIKEME) {

                                            RxBus.get().post("likeMecancelSuccess", position);

                                        } else {

                                            RxBus.get().post("likeEachcancelSuccess", position);

                                        }

                                    }


                                }, throwable -> {

                                    loadDialog.dismiss();

                                    if (throwable == null) {
                                        return;
                                    }
                                    mView.showMessage(ErrorHanding.handleError(throwable));
                                });

                        addSubscrebe(subscription);
                    });
                });
        blockDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
        blockDialog.show();
    }

    public void showUnlockDialog(int userid, String tvStr, String btn1, String btn2, boolean isGetKey, int type) {

        loadDialog = DialogUtil.LoadingDialog(mActivity, null);

        unLockDialog = new MyDialog(mActivity, R.style.MyDialog, R.layout.view_likelock_dialog, contentView -> {

            TextView remindTv = (TextView) contentView.findViewById(R.id.remind_tv);

            ImageView imageView = (ImageView) contentView.findViewById(R.id.remind_iv);

            imageView.setVisibility(View.VISIBLE);

            Button sureBtn = (Button) contentView.findViewById(R.id.sure_btn);

            Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);

            remindTv.setText(Html.fromHtml(tvStr));

            remindTv.setGravity(Gravity.CENTER);

            sureBtn.setText(btn1);

            cancelBtn.setText(btn2);

            sureBtn.setOnClickListener(v -> {

                ViewsUtils.preventViewMultipleClick(v, 1000);

                loadDialog.show();

                if (!isGetKey) {

                    //left btn
                    HashMap<String, Object> map = new HashMap<String, Object>();

                    map.put("targetUserId", userid);

                    Subscription subscription = HttpFactory.getHttpApi().putUnlockPeople(map)

                            .compose(SchedulersCompat.applyIoSchedulers())

                            .compose(RxResultHelper.handleResult())

                            .subscribe(unlock -> {

                                Logger.e("解锁成功返回了");

                                loadDialog.dismiss();

//                                if (!TextUtils.isEmpty(unlock.getMsg())) {
//
//                                    mView.showMessage(unlock.getMsg());
//
//                                } else {

                                mView.showMessage("解锁成功");

//                                }

                                if (type == YpSettings.LIKE_TYPE_LIKEME) {

                                    RxBus.get().post("likeMelockSuccess", true);

                                } else {

                                    RxBus.get().post("likeEachlockSuccess", true);

                                }

                            }, throwable -> {

                                Logger.e("解锁操作异常了");

                                loadDialog.dismiss();

                                if (null != throwable) {

                                    mView.showMessage(ErrorHanding.handleError(throwable));

                                }

                            });

                    mCompositeSubscription.add(subscription);

                } else {//跳转获取钥匙页面

                    loadDialog.dismiss();

                    RxBus.get().post("toGetKey", true);

                }
                unLockDialog.dismiss();
            });

            cancelBtn.setOnClickListener(v -> {
                //right btn
                unLockDialog.dismiss();
            });
        });
        unLockDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
        unLockDialog.show();
    }

}
