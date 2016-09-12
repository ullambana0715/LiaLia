package cn.chono.yopper.activity.appointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.DrinkingBean;
import cn.chono.yopper.utils.DialogUtil;

/**
 * 征婚喝酒项
 * Created by cc on 16/3/31.
 */
public class DrinkingOptionsActivity extends MainFrameActivity {

    private TextView appoint_drinking_tv;

    private ImageView appoint_drinking_et_iv;

    private GridView drinking_gridView;

    private DrinkingAdapter adapter;

    private String drinking1, drinking2, drinking3;

    private List<DrinkingBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_datings_drinkingoptions);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("drinking1"))
            drinking1 = bundle.getString("drinking1");
        if (bundle.containsKey("drinking2"))
            drinking2 = bundle.getString("drinking2");
        if (bundle.containsKey("drinking3"))
            drinking3 = bundle.getString("drinking3");
        initView();
        setDatas();


    }

    private void initView() {
        appoint_drinking_tv = (TextView) findViewById(R.id.appoint_drinking_tv);
        appoint_drinking_et_iv = (ImageView) findViewById(R.id.appoint_drinking_et_iv);
        drinking_gridView = (GridView) findViewById(R.id.drinking_gridView);

        appoint_drinking_et_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appoint_drinking_tv.setText("");

                drinking1 = "";
                drinking2 = "";
                drinking3 = "";

                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setType(false);
                }
                selectList = new ArrayList<String>();
                adapter.notifyDataSetChanged();
            }
        });


        getTvTitle().setText(getResources().getString(R.string.text_drinking));

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView option = gettvOption();
        option.setText(getResources().getString(R.string.affirm));

        getOptionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putSerializable("drinking1", drinking1);
                bundle.putSerializable("drinking2", drinking2);
                bundle.putSerializable("drinking3", drinking3);


                Intent intent = new Intent(DrinkingOptionsActivity.this, MarriageSeekingActivity.class);
                intent.putExtras(bundle);

                setResult(YpSettings.MOVEMENT_DRINKING, intent);

                finish();


            }
        });

        adapter = new DrinkingAdapter(this);

        drinking_gridView.setAdapter(adapter);

    }

    private void setDatas() {



//        <item>经常饮酒</item>
//        <item>偶尔小酌</item>
//        <item>不会喝酒</item>
//        <item>偶尔饮葡萄酒</item>
//        <item>会喝一些啤酒</item>
//        <item>会喝一点</item>
//        <item>喜欢白酒</item>
//        <item>喜欢洋酒</item>
//        <item>喜欢啤酒</item>
//        <item>喜欢自酿酒</item>
//        <item>心情不好时喝</item>
//        <item>聚会应酬时喝</item>

        DrinkingBean drinkingBean1 = new DrinkingBean();
        drinkingBean1.setContext("经常饮酒");


        DrinkingBean drinkingBean2 = new DrinkingBean();
        drinkingBean2.setContext("偶尔小酌");

        DrinkingBean drinkingBean3 = new DrinkingBean();
        drinkingBean3.setContext("不会喝酒");
        DrinkingBean drinkingBean4 = new DrinkingBean();
        drinkingBean4.setContext("偶尔饮葡萄酒");
        DrinkingBean drinkingBean5 = new DrinkingBean();
        drinkingBean5.setContext("会喝一些啤酒");
        DrinkingBean drinkingBean6 = new DrinkingBean();
        drinkingBean6.setContext("会喝一点");
        DrinkingBean drinkingBean7 = new DrinkingBean();
        drinkingBean7.setContext("喜欢白酒");
        DrinkingBean drinkingBean8 = new DrinkingBean();
        drinkingBean8.setContext("喜欢洋酒");
        DrinkingBean drinkingBean9 = new DrinkingBean();
        drinkingBean9.setContext("喜欢啤酒");
        DrinkingBean drinkingBean10 = new DrinkingBean();
        drinkingBean10.setContext("喜欢自酿酒");
        DrinkingBean drinkingBean11 = new DrinkingBean();
        drinkingBean11.setContext("心情不好时喝");
        DrinkingBean drinkingBean12 = new DrinkingBean();
        drinkingBean12.setContext("聚会应酬时喝");

        data.add(drinkingBean1);
        data.add(drinkingBean2);
        data.add(drinkingBean3);
        data.add(drinkingBean4);
        data.add(drinkingBean5);
        data.add(drinkingBean6);
        data.add(drinkingBean7);
        data.add(drinkingBean8);
        data.add(drinkingBean9);
        data.add(drinkingBean10);
        data.add(drinkingBean11);
        data.add(drinkingBean12);

        LogUtils.e("drinking1="+drinking1);
        LogUtils.e("drinking2="+drinking2);
        LogUtils.e("drinking3="+drinking3);


        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getContext();
            if (!TextUtils.isEmpty(drinking1) && TextUtils.equals(drinking1, name)) {
                data.get(i).setType(true);
                selectList.add(drinking1);
            }

            if (!TextUtils.isEmpty(drinking2) && TextUtils.equals(drinking2, name)) {
                data.get(i).setType(true);
                selectList.add(drinking2);
            }
            if (!TextUtils.isEmpty(drinking3) && TextUtils.equals(drinking3, name)) {
                data.get(i).setType(true);
                selectList.add(drinking3);
            }
        }

        setContextData();


    }


    /**
     * Created by cc on 16/3/22.
     */
    public class DrinkingAdapter extends BaseAdapter {

        private Context context;

        public DrinkingAdapter(Context context) {
            this.context = context;
        }


        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private ViewHolder holder;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.travel_pay_item_layout, null);
                holder = new ViewHolder();

                holder.view = (TextView) convertView.findViewById(R.id.contextView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.view.setText(data.get(position).getContext());

            LogUtils.e("dddd="+data.get(position).isType());


            if (data.get(position).isType()) {
                holder.view.setBackgroundResource(R.drawable.travel_label_lable_corners_setect);
                holder.view.setTextColor(context.getResources().getColor(R.color.color_ffffff));
            } else {
                holder.view.setBackgroundResource(R.drawable.travel_label_lable_corners_default);
                holder.view.setTextColor(context.getResources().getColor(R.color.color_333333));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isType = data.get(position).isType();
                    String name = data.get(position).getContext();

                    if (isType) {
                        isType = false;
                        isData(name);
                    } else {

                        if (selectList.size() >= 3) {

                            DialogUtil.showDisCoverNetToast((Activity) context, "最多只能选择三项");
                            return;
                        }
                        isType = true;
                        selectList.add(name);

                    }
                    data.get(position).setType(isType);
                    notifyDataSetChanged();
                    setContextData();
                }
            });


            return convertView;
        }


        public class ViewHolder {

            private TextView view;


        }

        public void isData(String name) {

            LogUtils.e("--1--"+selectList.toString());

            for (int i = 0; i < selectList.size(); i++) {

                if (TextUtils.equals(name, selectList.get(i))) {
                    selectList.remove(i);
                    break;
                }

            }

        }

    }

    private ArrayList<String> selectList = new ArrayList<>();


    public void setContextData() {

        LogUtils.e("----"+selectList.toString());


        if (selectList.size()>0){
            for (int i = 0; i < selectList.size(); i++) {
                switch (i) {
                    case 0:
                        drinking1 = selectList.get(i);
                        drinking2 = "";
                        drinking3 = "";
                        break;
                    case 1:
                        drinking2 = selectList.get(i);
                        drinking3 = "";
                        break;
                    case 2:
                        drinking3 = selectList.get(i);
                        break;
                }
            }
        }else{
            drinking1="";
            drinking2="";
            drinking3="";
        }


        StringBuffer stringBuffer = new StringBuffer();


        if (!TextUtils.isEmpty(drinking1)){
            stringBuffer.append(drinking1);
        }

        if (!TextUtils.isEmpty(drinking2)){
            stringBuffer.append(",");
            stringBuffer.append(drinking2);
        }

        if (!TextUtils.isEmpty(drinking3)){
            stringBuffer.append(",");
            stringBuffer.append(drinking3);
        }



        appoint_drinking_tv.setText(stringBuffer.toString());

    }


}
