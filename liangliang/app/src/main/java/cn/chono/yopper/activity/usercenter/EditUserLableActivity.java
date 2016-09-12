package cn.chono.yopper.activity.usercenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.FlowCenterLayout;

/**
 * 用户标签编辑
 *
 * @author sam.sun
 */
public class EditUserLableActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private FlowCenterLayout lable_flow_layout;

    private List<String> selectedList = new ArrayList<String>();

    private String[] allLableList;

    private int result_code;

    private String lableStr = "";


    private String[] labarray = {"影视协会", "吃货一枚", "瑜伽", "酒逢知己千杯少", "肉食动物", "麦霸", "游泳", "运动达人", "家有萌宠", "羽毛球", "文艺小资", "咖啡控", "电影爱好者",
            "中国好声音", "骑行", "K歌之王", "夜店嗨客", "我是喵主人", "茶香书香", "我是汪主人", "健身", "跑步", "泡吧", "素食主义"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initComponent();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            lableStr = bundle.getString(YpSettings.USER_LABLE);
            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }

        if (!CheckUtil.isEmpty(lableStr)) {
            String tas[] = lableStr.split(",");

            for (int i = 0; i < tas.length; i++) {
                selectedList.add(tas[i]);
            }
        }

        allLableList = randArray(labarray);

        initChildViews();
    }


    public static String[] randArray(String[] arr) {
        String[] arr2 = new String[arr.length];
        int count = arr.length;
        int cbRandCount = 0;// 索引
        int cbPosition = 0;// 位置
        int k = 0;
        do {
            Random rand = new Random();
            int r = count - cbRandCount;
            cbPosition = rand.nextInt(r);
            arr2[k++] = arr[cbPosition];
            cbRandCount++;
            arr[cbPosition] = arr[r - 1];// 将最后一位数值赋值给已经被使用的cbPosition
        } while (cbRandCount < count);

        return arr2;
    }


    private void initChildViews() {
        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 15;
        lp.bottomMargin = 15;

        for (int i = 0; i < allLableList.length; i++) {
            TextView view = new TextView(this);
            String lable_str = allLableList[i];
            view.setText(lable_str);
            view.setTextColor(getResources().getColor(R.color.color_565656));
            boolean ishave = false;
            if (selectedList != null && selectedList.size() > 0) {
                for (int j = 0; j < selectedList.size(); j++) {
                    if (lable_str.equals(selectedList.get(j))) {

                        ishave = true;
                        break;
                    }
                }
            }

            if (ishave) {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.lable_selected_textview_bg));
                view.setTextColor(getResources().getColor(R.color.color_ffffff));
                view.setTag(new Wode(lable_str, true));
            } else {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.lable_textview_bg));
                view.setTextColor(getResources().getColor(R.color.color_565656));
                view.setTag(new Wode(lable_str, false));
            }


            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Wode dto = (Wode) v.getTag();

                    if (dto.isSelect) {
                        v.setTag(new Wode(dto.getStr(), false));
                        v.setBackgroundDrawable(getResources().getDrawable(R.drawable.lable_textview_bg));
                        TextView tvview = (TextView) v;
                        tvview.setTextColor(getResources().getColor(R.color.color_565656));
                        ChangeSelectedListData(dto.getStr(), false);
                    } else {

                        if (selectedList.size() >= 5) {
                            DialogUtil.showDisCoverNetToast(EditUserLableActivity.this, "最多选择五个标签哦");
                        } else {
                            v.setTag(new Wode(dto.getStr(), true));
                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.lable_selected_textview_bg));
                            TextView tvview = (TextView) v;
                            tvview.setTextColor(getResources().getColor(R.color.color_ffffff));
                            ChangeSelectedListData(dto.getStr(), true);
                        }
                    }

                }
            });
            lable_flow_layout.addView(view, lp);
            lable_flow_layout.requestLayout();
        }
    }


    private void ChangeSelectedListData(String str, boolean isadd) {
        if (isadd) {
            selectedList.add(str);
        } else {
            for (int i = 0; i < selectedList.size(); i++) {
                if (str.equals(selectedList.get(i))) {
                    selectedList.remove(i);
                    break;
                }
            }

        }
    }

    class Wode {
        private String str;
        private boolean isSelect;

        public Wode(String str, boolean isSelect) {
            super();
            this.str = str;
            this.isSelect = isSelect;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("标签修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("标签修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("标签");
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
        contextView = mInflater.inflate(R.layout.user_info_edit_lable_activity, null);

        lable_flow_layout = (FlowCenterLayout) contextView.findViewById(R.id.user_info_eidt_lable_flowlayout);

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
        bundle.putString(YpSettings.USER_LABLE, formatLableStr(selectedList));
        intent.putExtras(bundle);
        this.setResult(result_code, intent);
        this.finish();
    }


    private String formatLableStr(List<String> selectedList) {
        String lableStr = "";

        if (selectedList != null && selectedList.size() > 0) {


            for (int i = 0; i < selectedList.size(); i++) {
                String str = selectedList.get(i);
                if (selectedList.size() == 1) {
                    lableStr = lableStr + str;
                } else if (i == selectedList.size() - 1) {
                    lableStr = lableStr + str;
                } else {
                    lableStr = lableStr + str + ",";
                }
            }
        }
     
        return lableStr;

    }
}
