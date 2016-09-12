package cn.chono.yopper.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;

public class ConstellationActivity extends MainFrameActivity implements View.OnClickListener {

    private ImageView aries_rb;
    private ImageView taurus_rb;
    private ImageView gemini_rb;
    private ImageView cancer_rb;
    private ImageView leo_rb;
    private ImageView virgo_rb;
    private ImageView libra_rb;
    private ImageView scorpio_rb;
    private ImageView sagittarius_rb;
    private ImageView capricornus_rb;
    private ImageView aquarius_rb;
    private ImageView pisces_rb;

    private int tag = 0;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择星座"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择星座"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.constellation_activity);
        PushAgent.getInstance(this).onAppStart();

        initView();// 初始化

        if(getIntent().getExtras()!=null){
            tag = getIntent().getExtras().getInt("horoscope");// 获取传弟过来的参数
        }
        setBgView(tag);

    }

    private void initView() {

        getTvTitle().setText("选择星座");

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle consBu = new Bundle();
                consBu.putInt("horoscope", tag);

                Intent it = new Intent();
                it.setClass(ConstellationActivity.this, RegisterWriteInfoActivity.class);
                it.putExtras(consBu);

                setResult(YpSettings.FLAG_CONSTELLATION, it);

                finish();
            }
        });
        aries_rb = (ImageView) findViewById(R.id.aries_rb);
        taurus_rb = (ImageView) findViewById(R.id.taurus_rb);
        gemini_rb = (ImageView) findViewById(R.id.gemini_rb);
        cancer_rb = (ImageView) findViewById(R.id.cancer_rb);
        leo_rb = (ImageView) findViewById(R.id.leo_rb);
        virgo_rb = (ImageView) findViewById(R.id.virgo_rb);
        libra_rb = (ImageView) findViewById(R.id.libra_rb);
        scorpio_rb = (ImageView) findViewById(R.id.scorpio_rb);
        sagittarius_rb = (ImageView) findViewById(R.id.sagittarius_rb);
        capricornus_rb = (ImageView) findViewById(R.id.capricornus_rb);
        aquarius_rb = (ImageView) findViewById(R.id.aquarius_rb);
        pisces_rb = (ImageView) findViewById(R.id.pisces_rb);

        aries_rb.setOnClickListener(this);
        taurus_rb.setOnClickListener(this);
        gemini_rb.setOnClickListener(this);
        cancer_rb.setOnClickListener(this);
        leo_rb.setOnClickListener(this);
        virgo_rb.setOnClickListener(this);
        libra_rb.setOnClickListener(this);
        scorpio_rb.setOnClickListener(this);
        sagittarius_rb.setOnClickListener(this);
        capricornus_rb.setOnClickListener(this);
        aquarius_rb.setOnClickListener(this);
        pisces_rb.setOnClickListener(this);


    }


    private void setBgView(int tag) {
        switch (tag) {

            case 1:
                aries_rb.setSelected(true);
                break;
            case 2:

                taurus_rb.setSelected(true);


                break;
            case 3:
                gemini_rb.setSelected(true);

                break;
            case 4:

                cancer_rb.setSelected(true);
                break;
            case 5:

                leo_rb.setSelected(true);
                break;
            case 6:
                virgo_rb.setSelected(true);
                break;
            case 7:
                libra_rb.setSelected(true);
                break;
            case 8:
                scorpio_rb.setSelected(true);
                break;
            case 9:
                sagittarius_rb.setSelected(true);
                break;
            case 10:
                capricornus_rb.setSelected(true);
                break;

            case 11:
                aquarius_rb.setSelected(true);

                break;
            case 12:
                pisces_rb.setSelected(true);
                break;

            default:
                break;
        }

    }


    @Override
    public void onClick(View v) {
        String vTag = (String) v.getTag();

        aries_rb.setSelected(false);
        taurus_rb.setSelected(false);
        gemini_rb.setSelected(false);
        cancer_rb.setSelected(false);
        leo_rb.setSelected(false);
        virgo_rb.setSelected(false);
        libra_rb.setSelected(false);
        scorpio_rb.setSelected(false);
        sagittarius_rb.setSelected(false);
        capricornus_rb.setSelected(false);
        aquarius_rb.setSelected(false);
        pisces_rb.setSelected(false);

        tag = Integer.valueOf(vTag);

        setBgView(tag);

        Bundle consBu = new Bundle();
        consBu.putInt("horoscope", tag);

        Intent it = new Intent();
        it.setClass(ConstellationActivity.this, RegisterWriteInfoActivity.class);
        it.putExtras(consBu);

        setResult(YpSettings.FLAG_CONSTELLATION, it);

        finish();

    }
}
