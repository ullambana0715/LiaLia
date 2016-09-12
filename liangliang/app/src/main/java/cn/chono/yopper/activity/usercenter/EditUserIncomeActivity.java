package cn.chono.yopper.activity.usercenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.wheel.AbstractWheelTextAdapter;
import cn.chono.yopper.view.wheel.OnWheelChangedListener;
import cn.chono.yopper.view.wheel.OnWheelScrollListener;
import cn.chono.yopper.view.wheel.WheelView;


/**
 * 修改编辑用户收入
 *
 * @author sam.sun
 */
public class EditUserIncomeActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private TextView user_info_income_tv;

    private WheelView edit_income_wheel;


    private int income_type;


    private int result_code;


    private ArrayList<String> income_list = new ArrayList<String>();

    private IncomeTextAdapter IncomeAdapter;

    private String strIncome = "3000元以下";


    private int maxsize = 24;
    private int minsize = 14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initComponent();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            income_type = bundle.getInt(YpSettings.USER_INCOME, 1);
            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }

        setIncomeData(income_type);

        income_list.add("3000元以下");
        income_list.add("3000元以上");
        income_list.add("5000元以上");
        income_list.add("10000元以上");
        income_list.add("20000元以上");
        income_list.add("50000元以上");
        income_list.add("保密");

        IncomeAdapter = new IncomeTextAdapter(this, income_list, getIncomeItem(strIncome), maxsize, minsize);
        edit_income_wheel.setVisibleItems(5);
        edit_income_wheel.setViewAdapter(IncomeAdapter);
        edit_income_wheel.setCurrentItem(getIncomeItem(strIncome));


        edit_income_wheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) IncomeAdapter.getItemText(wheel.getCurrentItem());
                strIncome = currentText;
                setTextviewSize(currentText, IncomeAdapter);
            }
        });

        edit_income_wheel.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) IncomeAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, IncomeAdapter);
                getIncomeData(currentText);
                user_info_income_tv.setText(currentText);
            }
        });

        setIncome(strIncome);
    }


    private class IncomeTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected IncomeTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, IncomeTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(24);
            } else {
                textvew.setTextSize(14);
            }
        }
    }


    /**
     * 初始化收入
     *
     * @param income
     */
    public void setIncome(String income) {
        if (income != null && income.length() > 0) {
            this.strIncome = income;
        }
    }

    /**
     * 获取收入位置
     * @param
     * @return
     */
    public int getIncomeItem(String income_str) {
        int size = income_list.size();
        int incomeIndex = 0;

        for (int i = 0; i < size; i++) {

            if (income_str.equals(income_list.get(i))) {
                incomeIndex = i;
                strIncome = income_str;
                break;
            }
        }
        LogUtils.e(income_str);
        LogUtils.e(incomeIndex + "");
        return incomeIndex;
    }


    private void setIncomeData(int type) {
        switch (type) {
            case Constant.Income_Type_3000down:
                user_info_income_tv.setText("3000元以下");
                strIncome = "3000元以下";
                break;
            case Constant.Income_Type_3000up:
                user_info_income_tv.setText("3000元以上");
                strIncome = "3000元以上";
                break;

            case Constant.Income_Type_5000up:
                user_info_income_tv.setText("5000元以上");
                strIncome = "5000元以上";
                break;

            case Constant.Income_Type_10000up:
                user_info_income_tv.setText("10000元以上");
                strIncome = "10000元以上";
                break;

            case Constant.Income_Type_20000up:
                user_info_income_tv.setText("20000元以上");
                strIncome = "20000元以上";
                break;
            case Constant.Income_Type_50000up:

                user_info_income_tv.setText("50000元以上");
                strIncome = "50000元以上";
                break;
            case Constant.Income_Type_secrecy:
                user_info_income_tv.setText("保密");
                strIncome = "保密";
                break;
        }

    }


    private void getIncomeData(String incomestr) {
        if (incomestr.equals("3000元以下")) {
            income_type = Constant.Income_Type_3000down;
        }

        if (incomestr.equals("3000元以上")) {
            income_type = Constant.Income_Type_3000up;
        }

        if (incomestr.equals("5000元以上")) {
            income_type = Constant.Income_Type_5000up;
        }

        if (incomestr.equals("10000元以上")) {
            income_type = Constant.Income_Type_10000up;
        }

        if (incomestr.equals("20000元以上")) {
            income_type = Constant.Income_Type_20000up;
        }
        if (incomestr.equals("50000元以上")) {
            income_type = Constant.Income_Type_50000up;
        }
        if (incomestr.equals("保密")) {
            income_type = Constant.Income_Type_secrecy;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("收入修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("收入修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("收入");
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
        contextView = mInflater.inflate(R.layout.user_info_edit_income_activity, null);

        user_info_income_tv = (TextView) contextView.findViewById(R.id.edit_income_tv);
        edit_income_wheel = (WheelView) contextView.findViewById(R.id.edit_income_wheel);

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
        bundle.putInt(YpSettings.USER_INCOME, income_type);
        intent.putExtras(bundle);
        this.setResult(result_code, intent);
        this.finish();
    }


}
