package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andbase.tractor.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.duanqu.qupai.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.Draw.DrawListRespBean;
import cn.chono.yopper.Service.Http.Draw.DrawListService;
import cn.chono.yopper.Service.Http.Draw.DrawReqBean;
import cn.chono.yopper.Service.Http.Draw.DrawRespBean;
import cn.chono.yopper.Service.Http.Draw.DrawService;
import cn.chono.yopper.Service.Http.Draw.DrawUserRespBean;
import cn.chono.yopper.Service.Http.Draw.DrawUserService;
import cn.chono.yopper.Service.Http.Draw.DrawWithAppleService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.adapter.DrawGridAdapter;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * Created by yangjinyu on 16/3/23.
 */
public class DrawActivity extends MainFrameActivity implements View.OnClickListener{
    //设置一个时间常量，此常量有两个作用，1.圆灯视图显示与隐藏中间的切换时间；2.指针转一圈所需要的时间，现设置为500毫秒
    private static final long ONE_WHEEL_TIME = 500;
    //设定结束角度，起始位置为22.5度用23代替。每+45度为一个方位。22.5---22.5+45为数组1的谢谢参与
    private int desDegree = 23 + 45;

    private ImageView pointIv;
    private ImageView wheelIv;
    private TextView bonus_info;
    private View no0;
    private View no1;
    private View no2;
    private View no3;
    boolean isDrawed;
    TextView apple_text;
    ImageView drawButton;
    String userPrizeId;
    StringBuffer drawUsers = new StringBuffer();

    public List<DrawListRespBean.DrawListInfo> mList = new ArrayList<>();
    //指针转圈圈数数据源
    private int[] laps = {3, 6, 9, 12};
    //指针所指向的角度数据源，因为有6个选项，所有此处是6个值
    private int[] angles = {360, 315, 270, 225, 180, 135, 90, 45,};

    public static int sEntrance = 0;
    public static final int FROM_CLIMBING = 0;
    public static final int FROM_MYACCOUNT = 1;
    public static final String ENTRANCE = "entrance";
    int appleCount;
    int appleCost;
    //监听动画状态的监听器
    private AnimationListener al = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            dialog = DialogUtil.createHintOperateDialog(DrawActivity.this, "提示", drawInfo, "", "确认", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {

                }

                @Override
                public void onEnsure(View view, Object... obj) {
                    dialog.dismiss();
                    if (sEntrance == FROM_CLIMBING){
                        finish();
                    }else if (sEntrance == FROM_MYACCOUNT){
//                        drawWithApple();
                        dialog.dismiss();
                        reSetAnimation();
                    }
                }

            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    reSetAnimation();
                }
            });
            dialog.show();
            isDrawed = false;
        }
    };
    DrawUserRespBean drawUserRespBean;
    Dialog dialog;
    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("抽奖"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("抽奖"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    double appleBonusCount;
    int appleBonusLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.draw_activity);

        PushAgent.getInstance(this).onAppStart();

        getTvTitle().setText("抽奖");
        userPrizeId = getIntent().getExtras().getString("userprize_id");
        appleCount = getIntent().getExtras().getInt("applecount");
        appleCost = getIntent().getExtras().getInt("applecost");
        sEntrance = getIntent().getExtras().getInt(ENTRANCE);

        getBtnGoBack().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        drawButton = (ImageView)findViewById(R.id.draw_btn_img);
        bonus_info = (TextView)findViewById(R.id.bonus_info);
        pointIv = (ImageView) findViewById(R.id.point);
        wheelIv = (ImageView) findViewById(R.id.main_wheel);
        apple_text = (TextView)findViewById(R.id.apple_text);
        apple_text.setText("每次抽奖需要"+appleCost+"苹果,您当前有"+appleCount+"苹果");
        if (sEntrance == FROM_CLIMBING){
            apple_text.setVisibility(View.GONE);
        }else {
            apple_text.setVisibility(View.VISIBLE);
        }

        no0 = (View) findViewById(R.id.no0);
        no1 = (View) findViewById(R.id.no1);
        no2 = (View) findViewById(R.id.no2);
        no3 = (View) findViewById(R.id.no3);
        drawButton.setOnClickListener(this);
        pointIv.setOnClickListener(this);

        DrawListService drawListService = new DrawListService(this);
        drawListService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                System.out.println("onSuccess");
                mList = ((DrawListRespBean) respBean).getResp().getDrawPrizeInfoList();
                if (mList.size() > 0){
                    no0.setVisibility(View.VISIBLE);

                    ((TextView) (no0.findViewById(R.id.bonus_name))).setText(mList.get(0).getName());
                    LogUtils.e("");
                    Glide.with(DrawActivity.this).load(mList.get(0).getImageUrl()).into((ImageView) no0.findViewById(R.id.bonus_img));
                    if(mList.get(0).getPrizeLevel() == 1){
                        ((TextView) (no0.findViewById(R.id.bonus_level))).setText("一等奖");
                    }else if (mList.get(0).getPrizeLevel() == 2){
                        ((TextView) (no0.findViewById(R.id.bonus_level))).setText("二等奖");
                    }else if (mList.get(0).getPrizeLevel() == 3){
                        ((TextView) (no0.findViewById(R.id.bonus_level))).setText("三等奖");
                    }else{
                        ((TextView) (no0.findViewById(R.id.bonus_level))).setText("活跃奖");
                    }
                }
                if(mList.size() >1){
                    no1.setVisibility(View.VISIBLE);

                    ((TextView) (no1.findViewById(R.id.bonus_name))).setText(mList.get(1).getName());
                    Glide.with(DrawActivity.this).load(mList.get(1).getImageUrl()).into((ImageView) no1.findViewById(R.id.bonus_img));
                    if(mList.get(1).getPrizeLevel() == 1){
                        ((TextView) (no1.findViewById(R.id.bonus_level))).setText("一等奖");
                    }else if (mList.get(1).getPrizeLevel() == 2){
                        ((TextView) (no1.findViewById(R.id.bonus_level))).setText("二等奖");
                    }else if (mList.get(1).getPrizeLevel() == 3){
                        ((TextView) (no1.findViewById(R.id.bonus_level))).setText("三等奖");
                    }else{
                        ((TextView) (no1.findViewById(R.id.bonus_level))).setText("活跃奖");
                    }
                }
                if(mList.size() >2){
                    no2.setVisibility(View.VISIBLE);

                    ((TextView) (no2.findViewById(R.id.bonus_name))).setText(mList.get(2).getName());
                    Glide.with(DrawActivity.this).load(mList.get(2).getImageUrl()).into((ImageView) no2.findViewById(R.id.bonus_img));
                    if(mList.get(2).getPrizeLevel() == 1){
                        ((TextView) (no2.findViewById(R.id.bonus_level))).setText("一等奖");
                    }else if (mList.get(2).getPrizeLevel() == 2){
                        ((TextView) (no2.findViewById(R.id.bonus_level))).setText("二等奖");
                    }else if (mList.get(2).getPrizeLevel() == 3){
                        ((TextView) (no2.findViewById(R.id.bonus_level))).setText("三等奖");
                    }else{
                        ((TextView) (no2.findViewById(R.id.bonus_level))).setText("活跃奖");
                    }
                }
                if(mList.size() >3){
                    no3.setVisibility(View.VISIBLE);

                    ((TextView) (no3.findViewById(R.id.bonus_name))).setText(mList.get(3).getName());
                    Glide.with(DrawActivity.this).load(mList.get(3).getImageUrl()).into((ImageView) no3.findViewById(R.id.bonus_img));
                    if(mList.get(3).getPrizeLevel() == 1){
                        ((TextView) (no3.findViewById(R.id.bonus_level))).setText("一等奖");
                    }else if (mList.get(3).getPrizeLevel() == 2){
                        ((TextView) (no3.findViewById(R.id.bonus_level))).setText("二等奖");
                    }else if (mList.get(3).getPrizeLevel() == 3){
                        ((TextView) (no3.findViewById(R.id.bonus_level))).setText("三等奖");
                    }else{
                        ((TextView) (no3.findViewById(R.id.bonus_level))).setText("活跃奖");
                    }
                }
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                System.out.println("onFail");
            }
        });
        drawListService.enqueue();

        DrawUserService drawUserService = new DrawUserService(this);
        drawUserService.callBack(new OnCallBackSuccessListener(){
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                drawUserRespBean = (DrawUserRespBean)respBean;
                System.out.println("DrawUserRespBean");
                for (int i=0;i<drawUserRespBean.getResp().getUserPrizeInfoMsg().size();i++){
                    drawUsers.append("恭喜");
                    drawUsers.append(drawUserRespBean.getResp().getUserPrizeInfoMsg().get(i).getUserName());
                    drawUsers.append("获得了");
                    drawUsers.append(drawUserRespBean.getResp().getUserPrizeInfoMsg().get(i).getPrizeName());
                    drawUsers.append("   ");
                }
                bonus_info.setText(drawUsers);
            }
        },new OnCallBackFailListener(){
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
            }
        });
        drawUserService.enqueue();
    }

    @Override
    public void onClick(View v) {
        if(isDrawed) {
           return;
        }
        if(v.getId() == R.id.draw_btn_img){
            if (sEntrance == FROM_CLIMBING){
                drawClimb();
            }else if (sEntrance == FROM_MYACCOUNT){
                drawWithApple();
            }
        }else if (v.getId() == R.id.point){
            if (sEntrance == FROM_CLIMBING){
                drawClimb();
            }else if (sEntrance == FROM_MYACCOUNT){
                drawWithApple();
            }
        }
    }
    String drawInfo;
    void drawClimb() {
            isDrawed = true;
            final DrawReqBean drawReqBean = new DrawReqBean();
            System.out.println(userPrizeId + "---------userPrizeId");
            drawReqBean.setUserPrizeId(userPrizeId);
            DrawService drawService = new DrawService(this);
            drawService.parameter(drawReqBean);
            drawService.callBack(new OnCallBackSuccessListener() {
                @Override
                public void onSuccess(RespBean respBean) {
                    super.onSuccess(respBean);
                    DrawRespBean drawRespBean = (DrawRespBean) respBean;
                    System.out.println(drawRespBean.getResp().getPrize() + "---------getPrize");
                    if (drawRespBean.getResp().getPrize() != null) {//中奖了
                        setPrizeLevelInfo(drawRespBean.getResp().getPrize().getPrizeLevel(),
                                drawRespBean.getResp().getPrize().getName());
                    } else {
                        setPrizeLevelInfo(0,"");
                    }

                    startAnimation();

                }
            }, new OnCallBackFailListener() {
                @Override
                public void onFail(RespBean respBean) {
                    super.onFail(respBean);
                    isDrawed = false;
                    Toast.makeText(DrawActivity.this,"网络错误，请重试",Toast.LENGTH_SHORT).show();
                }
            });
            drawService.enqueue();
    }

    void setPrizeLevelInfo(int level,String name){
        switch (level){
            case 1:
                desDegree =   (int)(Math.random()*20);
                drawInfo = "恭喜你中了"+name;
                break;
            case 2:
                desDegree =  23+135+ (int)(Math.random()*40);
                drawInfo = "恭喜你中了"+name;
                break;
            case 3:
                desDegree =  23+45+ (int)(Math.random()*40);
                drawInfo = "恭喜你中了"+name;
                break;
            case 4:
                desDegree = 23 + 90 + (int) (Math.random() * 40);
                drawInfo = "恭喜你中了"+name;
                break;
            case 10:
                desDegree =  23+ (int)(Math.random()*40);
                drawInfo = "很可惜没有中奖";
                break;
            default:
                desDegree =  23+ (int)(Math.random()*40);
                drawInfo = "很可惜没有中奖";
                break;
        }
    }

    void startAnimation(){
        int lap = laps[(int) (Math.random() * 4)];
        int angle = angles[(int) (Math.random() * 8)];
        //每次转圈角度增量
        int increaseDegree = lap * 360;
        //初始化旋转动画，后面的四个参数是用来设置以自己的中心点为圆心转圈
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, increaseDegree + desDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);


        //计算动画播放总时间
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        //设置动画播放时间
        rotateAnimation.setDuration(time);
        //设置动画播放完后，停留在最后一帧画面上
        rotateAnimation.setFillAfter(true);
        //设置动画的加速行为，是先加速后减速
        rotateAnimation.setInterpolator(DrawActivity.this,
                android.R.anim.accelerate_decelerate_interpolator);
        //设置动画的监听器
        rotateAnimation.setAnimationListener(al);
        //开始播放动画
        wheelIv.startAnimation(rotateAnimation);
    }

    void reSetAnimation(){
        desDegree = 23 + 45;
        wheelIv.clearAnimation();
    }

    void drawWithApple(){
        isDrawed = true;
        if(appleCount < appleCost){
            Toast.makeText(this,"苹果数量不够",Toast.LENGTH_SHORT).show();
        }else{
            DrawWithAppleService drawService = new DrawWithAppleService(this);
            drawService.callBack(new OnCallBackSuccessListener() {
                @Override
                public void onSuccess(RespBean respBean) {
                    super.onSuccess(respBean);
                    DrawRespBean bean = (DrawRespBean)respBean;

                    appleCount -= appleCost;
                    setPrizeLevelInfo(bean.getResp().getPrize().getPrizeLevel(),bean.getResp().getPrize().getName());

                    startAnimation();
                    apple_text.setText("每次抽奖需要"+appleCost+"苹果,您当前有"+appleCount+"苹果");

                }
            }, new OnCallBackFailListener() {
                @Override
                public void onFail(RespBean respBean) {
                    super.onFail(respBean);
//                    setPrizeLevelInfo(0,"");
//                    startAnimation();
                    Toast.makeText(DrawActivity.this,"网络错误，请重试",Toast.LENGTH_SHORT).show();
                    isDrawed = false;
                }
            });
            drawService.enqueue();
        }
    }
}
