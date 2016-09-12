package cn.chono.yopper.activity.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.login.LoginOrRegisterActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 选择是登陆还是注册
 *
 * @author zxb
 */
public class SelectEntryActivity extends MainFrameActivity implements OnClickListener {
    private Button login_but;
    private Button register_but;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_entry_activity);
        PushAgent.getInstance(this).onAppStart();
        initView();
    }

    /**
     * 组件初始化
     */
    private void initView() {

        login_but = (Button) findViewById(R.id.login_but);
        register_but = (Button) findViewById(R.id.register_but);
        login_but.setOnClickListener(this);
        register_but.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择（登录/注册）"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
        ViewsUtils.preventViewMultipleClick(login_but, true);
        ViewsUtils.preventViewMultipleClick(register_but, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择（登录/注册）"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_but:// 登陆
                ViewsUtils.preventViewMultipleClick(v, false);
                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.FROM_PAGE, 0);
                ActivityUtil.jump(SelectEntryActivity.this, LoginOrRegisterActivity.class, bundle, 0, 0);
                break;

            case R.id.register_but:// 注册

                ViewsUtils.preventViewMultipleClick(v, false);
                MobclickAgent.onEvent(SelectEntryActivity.this, "btn_register");
                Bundle bd = new Bundle();
                bd.putInt(YpSettings.FROM_PAGE, 1);
                ActivityUtil.jump(SelectEntryActivity.this, LoginOrRegisterActivity.class, bd, 0, 100);
                break;

            default:
                break;
        }

    }

}
