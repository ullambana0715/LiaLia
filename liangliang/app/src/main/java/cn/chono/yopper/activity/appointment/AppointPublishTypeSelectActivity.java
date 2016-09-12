package cn.chono.yopper.activity.appointment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingsMarriageLimit.DatingsMarriageLimitData;
import cn.chono.yopper.Service.Http.DatingsMarriageLimit.DatingsMarriageLimitRespBean;
import cn.chono.yopper.Service.Http.DatingsMarriageLimit.DatingsMarriageLimitService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;

/**
 * 邀约发布类型选择
 */
public class AppointPublishTypeSelectActivity extends Activity implements OnGestureListener {
    private ViewFlipper flipper;
    private GestureDetector detector;
    private LayoutInflater mInflater;
    private LinearLayout appoint_publish_type_select_layout;

    /**
     * Called when the activity is first created.
     */

    private String frompage;

    private Dialog publishDatingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appoint_pulish_type_select_activity);

        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frompage = this.getIntent().getExtras().getString(YpSettings.FROM_PAGE);
        detector = new GestureDetector(this,this);
        flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);
        appoint_publish_type_select_layout = (LinearLayout) this.findViewById(R.id.appoint_publish_type_select_layout);

        flipper.addView(addOnePageView());

        flipper.addView(addTwoPageView());

        appoint_publish_type_select_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View addOnePageView() {

        View one_layoutView = mInflater.inflate(R.layout.appoint_publish_type_one_page_layout, null);

        ImageView appoint_publish_type_more_iv = (ImageView) one_layoutView.findViewById(R.id.appoint_publish_type_more_iv);
        ImageView appoint_publish_type_eat_iv = (ImageView) one_layoutView.findViewById(R.id.appoint_publish_type_eat_iv);
        ImageView appoint_publish_type_movice_iv = (ImageView) one_layoutView.findViewById(R.id.appoint_publish_type_movice_iv);


        ImageView appoint_publish_type_personals_iv = (ImageView) one_layoutView.findViewById(R.id.appoint_publish_type_personals_iv);
        ImageView appoint_publish_type_travel_iv = (ImageView) one_layoutView.findViewById(R.id.appoint_publish_type_travel_iv);
        ImageView appoint_publish_type_fitness_iv = (ImageView) one_layoutView.findViewById(R.id.appoint_publish_type_fitness_iv);

        LinearLayout appoint_publish_type_one_page_layout = (LinearLayout) one_layoutView.findViewById(R.id.appoint_publish_type_one_page_layout);


        appoint_publish_type_personals_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//征婚

                datingsMarriageLimit();
            }
        });

        appoint_publish_type_travel_iv.setOnClickListener(new OnClickListener() {//旅行
            @Override
            public void onClick(View v) {


                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_TRAVEL + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);//type 是用来标记下级界面的跳转
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, TravelDestinationActivity.class, bundle, 0, 100);

                finish();

            }
        });

        appoint_publish_type_fitness_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {//运动

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_FITNESS + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_FITNESS);
                bundle.putInt("type", 0);//type 是用来标记下级界面的跳转
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, ReleaseAppointmentThemeActivity.class, bundle, 0, 100);
                finish();
            }
        });

        appoint_publish_type_one_page_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appoint_publish_type_eat_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {//吃饭

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_EAT + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_EAT);
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, ReleaseAppointmentActivity.class, bundle, 0, 100);
                finish();
            }
        });

        appoint_publish_type_movice_iv.setOnClickListener(new OnClickListener() {//电影

            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_MOVIE + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MOVIE);
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, ReleaseAppointmentActivity.class, bundle, 0, 100);
                finish();
            }
        });


        appoint_publish_type_more_iv.setOnClickListener(new OnClickListener() {//更多

            @Override
            public void onClick(View v) {
                flipper.setInAnimation(AnimationUtils.loadAnimation(AppointPublishTypeSelectActivity.this, R.anim.push_left_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(AppointPublishTypeSelectActivity.this, R.anim.push_left_out));
                flipper.showNext();
            }
        });
        return one_layoutView;
    }

    private View addTwoPageView() {

        View two_layoutView = mInflater.inflate(R.layout.appoint_publish_type_two_page_layout, null);

        LinearLayout appoint_publish_type_two_page_layout = (LinearLayout) two_layoutView.findViewById(R.id.appoint_publish_type_two_page_layout);
        ImageView appoint_publish_type_back_iv = (ImageView) two_layoutView.findViewById(R.id.appoint_publish_type_back_iv);

        ImageView appoint_publish_type_dog_iv = (ImageView) two_layoutView.findViewById(R.id.appoint_publish_type_dog_iv);

        ImageView appoint_publish_type_ktv_iv = (ImageView) two_layoutView.findViewById(R.id.appoint_publish_type_ktv_iv);
        ImageView appoint_publish_type_other_iv = (ImageView) two_layoutView.findViewById(R.id.appoint_publish_type_other_iv);

        appoint_publish_type_two_page_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appoint_publish_type_back_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                flipper.setInAnimation(AnimationUtils.loadAnimation(AppointPublishTypeSelectActivity.this, R.anim.push_right_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(AppointPublishTypeSelectActivity.this, R.anim.push_right_out));
                flipper.showPrevious();
            }
        });


        appoint_publish_type_dog_iv.setOnClickListener(new OnClickListener() {//溜狗

            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_DOG + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_DOG);
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, ReleaseAppointmentActivity.class, bundle, 0, 100);
                finish();
            }
        });


        appoint_publish_type_ktv_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {//唱歌
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_KTV + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_KTV);
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, ReleaseAppointmentActivity.class, bundle, 0, 100);
                finish();
            }
        });

        appoint_publish_type_other_iv.setOnClickListener(new OnClickListener() {//其他
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_NO_LIMIT + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_OTHERS);
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, ReleaseAppointmentActivity.class, bundle, 0, 100);
                finish();
            }
        });

        return two_layoutView;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    private void datingsMarriageLimit() {

        DatingsMarriageLimitService datingsMarriageLimitService = new DatingsMarriageLimitService(this);


        datingsMarriageLimitService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DatingsMarriageLimitRespBean datingsMarriageLimitRespBean = (DatingsMarriageLimitRespBean) respBean;

                DatingsMarriageLimitData limitData = datingsMarriageLimitRespBean.getResp();

                if (null == limitData) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("typeId", Constant.APPOINT_TYPE_MARRIED + "");
                    MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                    Bundle bundle = new Bundle();
                    bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MARRIED);
                    bundle.putString(YpSettings.FROM_PAGE, frompage);
                    ActivityUtil.jump(AppointPublishTypeSelectActivity.this, MarriageSeekingActivity.class, bundle, 0, 100);
                    finish();

                    return;
                }

                if (limitData.isPass()) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("typeId", Constant.APPOINT_TYPE_MARRIED + "");
                    MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                    Bundle bundle = new Bundle();
                    bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MARRIED);
                    bundle.putString(YpSettings.FROM_PAGE, frompage);
                    ActivityUtil.jump(AppointPublishTypeSelectActivity.this, MarriageSeekingActivity.class, bundle, 0, 100);
                    finish();


                    return;
                }

                String limitMsg = limitData.getMsg();
                final String datingId = limitData.getDatingId();

                publishDatingDialog = DialogUtil.createHintOperateDialog(AppointPublishTypeSelectActivity.this, "", limitMsg, "取消", "去修改", new BackCallListener() {



                    @Override
                    public void onCancel(View view, Object... obj) {
                        publishDatingDialog.dismiss();
                    }

                    @Override
                    public void onEnsure(View view, Object... obj) {
                        publishDatingDialog.dismiss();

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("typeId", Constant.APPOINT_TYPE_MARRIED + "");
                        MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                        Bundle bundle = new Bundle();
                        bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MARRIED);
                        bundle.putString("datingId", datingId);
                        bundle.putString(YpSettings.FROM_PAGE, frompage);
                        ActivityUtil.jump(AppointPublishTypeSelectActivity.this, MarriageSeekingActivity.class, bundle, 0, 100);
                        finish();

                    }
                });

                publishDatingDialog.show();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", Constant.APPOINT_TYPE_MARRIED + "");
                MobclickAgent.onEvent(AppointPublishTypeSelectActivity.this, "date_selected", map);

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MARRIED);
                bundle.putString(YpSettings.FROM_PAGE, frompage);
                ActivityUtil.jump(AppointPublishTypeSelectActivity.this, MarriageSeekingActivity.class, bundle, 0, 100);
                finish();

            }
        });

        datingsMarriageLimitService.enqueue();

    }

}
