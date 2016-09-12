package cn.chono.yopper.activity.usercenter;

import android.os.Bundle;
import android.view.View;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;

/**
 * Created by jianghua on 2016/6/15.
 */
public class MyActivitiesNoFoundActivity extends MainFrameActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_nofound_activity);

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getTvTitle().setText("找不到活动");
    }
}
