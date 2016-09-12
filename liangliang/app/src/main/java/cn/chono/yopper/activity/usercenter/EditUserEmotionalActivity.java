package cn.chono.yopper.activity.usercenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 编辑用户感情状况
 *
 * @author sam.sun
 */
public class EditUserEmotionalActivity extends MainFrameActivity implements OnClickListener {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    //单身
    private TextView edit_emo_lone_tv;
    //恋爱中
    private TextView edit_emo_loving_tv;
    //已婚
    private TextView edit_emo_married_tv;
    //同性
    private TextView edit_emo_gay_tv;
    //保密
    private TextView edit_emo_secrecy_tv;


    private int emotional_type;

    private int result_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initComponent();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            emotional_type = bundle.getInt(YpSettings.USER_EMOTIONAL);

            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }
        setSelectedBg(emotional_type);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("情感状态修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("情感状态修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("情感状态");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                return_name();
            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.user_info_edit_emotional_activity, null);


        edit_emo_lone_tv = (TextView) contextView.findViewById(R.id.edit_emo_lone_tv);
        edit_emo_lone_tv.setOnClickListener(this);

        edit_emo_loving_tv = (TextView) contextView.findViewById(R.id.edit_emo_loving_tv);
        edit_emo_loving_tv.setOnClickListener(this);

        edit_emo_married_tv = (TextView) contextView.findViewById(R.id.edit_emo_married_tv);
        edit_emo_married_tv.setOnClickListener(this);

        edit_emo_gay_tv = (TextView) contextView.findViewById(R.id.edit_emo_gay_tv);
        edit_emo_gay_tv.setOnClickListener(this);

        edit_emo_secrecy_tv = (TextView) contextView.findViewById(R.id.edit_emo_secrecy_tv);
        edit_emo_secrecy_tv.setOnClickListener(this);


        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return_name();
        }
        return true;
    }

    private void return_name() {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(YpSettings.USER_EMOTIONAL, emotional_type);
        intent.putExtras(bundle);
        EditUserEmotionalActivity.this.setResult(result_code, intent);
        EditUserEmotionalActivity.this.finish();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.edit_emo_lone_tv://单身
                emotional_type = Constant.Emotional_Type_lone;
                break;

            case R.id.edit_emo_loving_tv://恋爱中
                emotional_type = Constant.Emotional_Type_loving;
                break;

            case R.id.edit_emo_married_tv://已婚
                emotional_type = Constant.Emotional_Type_married;
                break;

            case R.id.edit_emo_gay_tv://同性
                emotional_type = Constant.Emotional_Type_Gay;
                break;

            case R.id.edit_emo_secrecy_tv://保密
                emotional_type = Constant.Emotional_Type_secrecy;
                break;

        }
        setSelectedBg(emotional_type);
    }


    private void setSelectedBg(int emotional_id) {

        switch (emotional_id) {
            case Constant.Emotional_Type_secrecy:
                edit_emo_lone_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_loving_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_married_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_gay_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_secrecy_tv.setBackgroundResource(R.drawable.profession_selected);
                break;

            case Constant.Emotional_Type_lone:
                edit_emo_lone_tv.setBackgroundResource(R.drawable.profession_selected);
                edit_emo_loving_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_married_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_gay_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_secrecy_tv.setBackgroundResource(R.drawable.profession_normal);
                break;

            case Constant.Emotional_Type_married:
                edit_emo_lone_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_loving_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_married_tv.setBackgroundResource(R.drawable.profession_selected);
                edit_emo_gay_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_secrecy_tv.setBackgroundResource(R.drawable.profession_normal);
                break;

            case Constant.Emotional_Type_loving:
                edit_emo_lone_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_loving_tv.setBackgroundResource(R.drawable.profession_selected);
                edit_emo_married_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_gay_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_secrecy_tv.setBackgroundResource(R.drawable.profession_normal);
                break;

            case Constant.Emotional_Type_Gay:
                edit_emo_lone_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_loving_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_married_tv.setBackgroundResource(R.drawable.profession_normal);
                edit_emo_gay_tv.setBackgroundResource(R.drawable.profession_selected);
                edit_emo_secrecy_tv.setBackgroundResource(R.drawable.profession_normal);
                break;
        }
    }
}
