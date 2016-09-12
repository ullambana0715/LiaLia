package cn.chono.yopper.activity.find;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.MyEnergy.MyEnergyRespBean;
import cn.chono.yopper.Service.Http.MyEnergy.MyEnergyService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.view.CircleEnergy;

/**
 * Created by yangjinyu on 16/3/4.
 */
public class MyEnergyActivity extends MainFrameActivity implements View.OnClickListener {

    public CircleEnergy mCircle;
    float progress = 0;
    TextView climbButton;

    MyEnergyRespBean.MyEnergyRespBeanResp.PowerConfigs zhudongconfigs;
    MyEnergyRespBean.MyEnergyRespBeanResp.PowerConfigs beidongconfigs;
    float energy;

    private LinearLayout mEnergy_introduct_view;
    private LayoutInflater mInflater;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progress < energy) {
                progress += 0.01;
                mHandler.sendEmptyMessageDelayed(0, 20);
                mCircle.setProgress(progress);
                mCircle.setInnerText((int) (progress * 100));
            }else if (energy == 0){
                mCircle.setProgress(0);
                mCircle.setInnerText(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.my_energy_activity);

        PushAgent.getInstance(this).onAppStart();

        getTvTitle().setText("我的能量");
        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCircle = (CircleEnergy) findViewById(R.id.my_energy);
        climbButton = (TextView) findViewById(R.id.goto_climbing);
        climbButton.setOnClickListener(this);

        mCircle.setStatus(CircleEnergy.STATUS_WORKING);
        mCircle.setInnerTextColor(getResources().getColor(R.color.color_ff7462));
        mCircle.setPaintWidth(10);

        mEnergy_introduct_view = (LinearLayout) findViewById(R.id.getEnergy_introduct_view);
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("我的能量"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("我的能量"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长


        MyEnergyService mService = new MyEnergyService(this);
        mService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                climbButton.setClickable(true);
                climbButton.setBackgroundResource(R.drawable.shape_defalut_corner);
                MyEnergyRespBean myEnergyRespBean = (MyEnergyRespBean) respBean;
                energy = myEnergyRespBean.getResp().getPower().getCurrentValue();
                energy = (float) energy / 100;
                mHandler.sendEmptyMessageDelayed(0, 100);
                List<MyEnergyRespBean.MyEnergyRespBeanResp.PowerConfigs> lp = myEnergyRespBean.getResp().getPowerConfigs();

                for (int i = 0; i < lp.size(); i++) {
                    if (lp.get(i).getActionType() == 1) {
                        zhudongconfigs = lp.get(i);
                    } else if (lp.get(i).getActionType() == 2) {
                        beidongconfigs = lp.get(i);
                    }
                }

                if (zhudongconfigs.getItems().size() > 0) {
                    mEnergy_introduct_view.removeAllViews();

                    for (int i = 0; i < zhudongconfigs.getItems().size(); i++) {
                        mEnergy_introduct_view.addView(initEnergyView(zhudongconfigs.getItems().get(i),i,zhudongconfigs.getDesc()));
                    }
                }

                if (beidongconfigs.getItems().size() > 0) {
                    for (int i = 0; i < beidongconfigs.getItems().size(); i++) {
                        mEnergy_introduct_view.addView(initEnergyView(beidongconfigs.getItems().get(i), i, beidongconfigs.getDesc()));
                    }
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
            }
        });
        mService.enqueue();
    }

    private View initEnergyView(MyEnergyRespBean.MyEnergyRespBeanResp.Items items,int position,String title) {
        View view = mInflater.inflate(R.layout.view_energy,null);
        TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
        TextView desc_tv = (TextView) view.findViewById(R.id.type_tv);
        TextView value_tv = (TextView) view.findViewById(R.id.power_value_tv);

        if(position == 0 && !TextUtils.isEmpty(title)){
            title_tv.setVisibility(View.VISIBLE);
            title_tv.setText(title);
        }

        desc_tv.setText(items.getItemDesc());
        value_tv.setText(items.getItemValue());
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goto_climbing:
                MobclickAgent.onEvent(this, "btn_find_event_goclimblist");
                ActivityUtil.jump(this, ClimbingListActivity.class, null, 0, 100);
                break;
        }
    }
}
