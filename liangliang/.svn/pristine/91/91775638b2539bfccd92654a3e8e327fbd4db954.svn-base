package cn.chono.yopper.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;

import cn.chono.yopper.R;
import cn.chono.yopper.view.wheel.AbstractWheelTextAdapter;
import cn.chono.yopper.view.wheel.OnWheelChangedListener;
import cn.chono.yopper.view.wheel.OnWheelScrollListener;
import cn.chono.yopper.view.wheel.WheelView;

/**
 * Created by SQ on 2015/12/22.
 */
public class DatingFilterWheelDialog extends Dialog {

    private OnSelectedListener onSelectedListener;

    private WheelView dating_filter_wheel;
    private ArrayList<String> dataList;

    private IncomeTextAdapter IncomeAdapter;

    private String selectedstr = "";

    private int maxsize = 24;
    private int minsize = 14;
    private int wheeltype;

    public DatingFilterWheelDialog(Context context, int wheelType, OnSelectedListener listener, ArrayList<String> list, String selected_str) {
        super(context, R.style.dialog_BOT_style);
        onSelectedListener = listener;
        View view =   LayoutInflater.from(context).inflate(R.layout.dating_filter_wheel_layout, null);
        dating_filter_wheel=(WheelView) view.findViewById(R.id.dating_filter_wheel);
        dataList=list;

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(view);

        selectedstr=selected_str;

        wheeltype=wheelType;

        IncomeAdapter = new IncomeTextAdapter(context, dataList, getIncomeItem(selectedstr), maxsize, minsize);
        dating_filter_wheel.setVisibleItems(5);
        dating_filter_wheel.setViewAdapter(IncomeAdapter);
        dating_filter_wheel.setCurrentItem(getIncomeItem(selectedstr));


        dating_filter_wheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) IncomeAdapter.getItemText(wheel.getCurrentItem());
                selectedstr = currentText;
                setTextviewSize(currentText, IncomeAdapter);
            }
        });

        dating_filter_wheel.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) IncomeAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, IncomeAdapter);
                onSelectedListener.onSelectedListener(wheeltype,currentText);
            }
        });

        setIncome(selectedstr);
    }


    public interface  OnSelectedListener{
        public void onSelectedListener(int wheeltype,String currentText);
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
     * 初始化
     */
    public void setIncome(String income) {
        if (income != null && income.length() > 0) {
            this.selectedstr = income;
        }
    }

    /**
     * @return
     */
    public int getIncomeItem(String income_str) {
        int size = dataList.size();
        int incomeIndex = 0;

        for (int i = 0; i < size; i++) {

            if (income_str.equals(dataList.get(i))) {
                incomeIndex = i;
                selectedstr = income_str;
                break;
            }
        }
        LogUtils.e(income_str);
        LogUtils.e(incomeIndex + "");
        return incomeIndex;
    }

}
