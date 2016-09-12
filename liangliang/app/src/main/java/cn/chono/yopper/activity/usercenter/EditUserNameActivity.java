package cn.chono.yopper.activity.usercenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 编辑用户昵称
 *
 * @author sam.sun
 */
public class EditUserNameActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private EditText user_info_name_et;

    private String name;

    private int result_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initComponent();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString(YpSettings.USER_NAME);
            user_info_name_et.setText(name);
            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("姓名编辑"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("姓名编辑"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("姓名");
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
        contextView = mInflater.inflate(R.layout.user_info_edit_name_activity, null);

        user_info_name_et = (EditText) contextView.findViewById(R.id.user_info_name_et);

        user_info_name_et.setFilters(new InputFilter[]{filter});

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

        name = user_info_name_et.getText().toString().trim();

        if (CheckUtil.isEmpty(name)) {

            DialogUtil.showDisCoverNetToast(EditUserNameActivity.this, "姓名不能为空");
            return;
        }

        if (name.length() < 2 || name.length() > 16) {
            DialogUtil.showDisCoverNetToast(EditUserNameActivity.this, "姓名必须在2-16个字符之间");
            return;
        }

        hideSoftInputView();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.USER_NAME, name);
        intent.putExtras(bundle);
        EditUserNameActivity.this.setResult(result_code, intent);
        EditUserNameActivity.this.finish();
    }


    private final int maxLen = 16;
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;

            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                return dest.subSequence(0, dindex - 1);
            }

            int sindex = 0;
            while (count <= maxLen && sindex < source.length()) {
                char c = source.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                sindex--;
            }

            return source.subSequence(0, sindex);
        }


    };
}
