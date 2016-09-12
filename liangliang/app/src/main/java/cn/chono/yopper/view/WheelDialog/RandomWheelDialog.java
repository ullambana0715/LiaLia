package cn.chono.yopper.view.WheelDialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import cn.chono.yopper.R;
import cn.chono.yopper.view.wheel.OnWheelChangedListener;
import cn.chono.yopper.view.wheel.OnWheelScrollListener;
import cn.chono.yopper.view.wheel.WheelView;

/**
 * Created by cc on 16/3/24.
 */
public class RandomWheelDialog extends Dialog {


    private int maxsize = 24;
    private int minsize = 14;

    private OnWheelListener onWheelListener;

    private String[] oneData;


    private WheelView one_wheelView,two_wheelView;

    private WheelDialogAdapter one_wheelDialogAdapter;

    private String oneSelectData;


    public RandomWheelDialog(Context context, String[] oneData, OnWheelListener onWheelListener) {
        super(context, R.style.dialog_BOT_style);
        this.oneData = oneData;
        this.onWheelListener = onWheelListener;
        initView();
    }

    public RandomWheelDialog(Context context, String[] oneData, String oneSelectData, OnWheelListener onWheelListener) {
        super(context, R.style.dialog_BOT_style);
        this.oneData = oneData;
        this.onWheelListener = onWheelListener;
        this.oneSelectData = oneSelectData;
        initView();
    }


    private void initView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.random_wheel_layout, null);

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(view);


        one_wheelView = (WheelView) view.findViewById(R.id.one_wheelView);
        two_wheelView = (WheelView) view.findViewById(R.id.two_wheelView);
        two_wheelView.setVisibility(View.GONE);

        if (null == oneData || oneData.length <= 0) {
            one_wheelView.setVisibility(View.GONE);
            return;
        }

        one_wheelDialogAdapter = new WheelDialogAdapter(getContext(), oneData, getOneIncomeItem(oneData, oneSelectData), maxsize, minsize);

        one_wheelView.setVisibleItems(5);
        one_wheelView.setViewAdapter(one_wheelDialogAdapter);


        one_wheelView.setCurrentItem(getOneIncomeItem(oneData, oneSelectData));
        setTextviewSize(oneSelectData, one_wheelDialogAdapter);


        one_wheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                oneSelectData = (String) one_wheelDialogAdapter.getItemText(wheel.getCurrentItem());

                setTextviewSize(oneSelectData, one_wheelDialogAdapter);


            }
        });

        one_wheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                oneSelectData = (String) one_wheelDialogAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(oneSelectData, one_wheelDialogAdapter);

                if (null != onWheelListener) {
                    onWheelListener.onWeelListener(oneSelectData);
                    onWheelListener.onWeelListener(oneSelectData, "");

                }


            }
        });

        if (null != onWheelListener) {
            onWheelListener.onWeelListener(oneSelectData);
            onWheelListener.onWeelListener(oneSelectData, "");

        }


        setIncome(oneSelectData);


    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, WheelDialogAdapter adapter) {
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
            this.oneSelectData = income;
        }
    }

    /**
     * @return
     */
    public int getOneIncomeItem(String[] data, String income_str) {


        if (null == data && data.length<=0) {
            return 0;
        }
        int size = data.length;
        int incomeIndex = 0;


        if (!TextUtils.isEmpty(income_str)){
            for (int i = 0; i < size; i++) {

                if (income_str.equals(data[i])) {
                    incomeIndex = i;
                    oneSelectData = income_str;
                    break;
                }
            }
        }else{
            oneSelectData=data[0];
        }





        return incomeIndex;
    }


}
