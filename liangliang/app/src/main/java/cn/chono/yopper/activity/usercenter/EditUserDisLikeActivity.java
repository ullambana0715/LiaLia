package cn.chono.yopper.activity.usercenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 编辑用户讨厌
 *
 * @author sam.sun
 */
public class EditUserDisLikeActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private EditText user_info_dislike_et;

    private String dislike;


    private int result_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initComponent();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            dislike = bundle.getString(YpSettings.USER_DISLIKE);
            user_info_dislike_et.setText(dislike);
            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("讨厌编辑"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("讨厌编辑"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("讨厌");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                return_dislike();
            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.user_info_edit_dislike_activity, null);

        user_info_dislike_et = (EditText) contextView.findViewById(R.id.user_info_dislike_et);

        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return_dislike();

        }
        return true;
    }

    private void return_dislike() {
        hideSoftInputView();

        dislike = user_info_dislike_et.getText().toString().trim();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.USER_DISLIKE, dislike);
        intent.putExtras(bundle);
        EditUserDisLikeActivity.this.setResult(result_code, intent);
        EditUserDisLikeActivity.this.finish();
    }
}
