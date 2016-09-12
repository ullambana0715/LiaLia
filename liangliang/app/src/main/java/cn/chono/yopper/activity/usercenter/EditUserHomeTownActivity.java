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

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.wheel.AbstractWheelTextAdapter;
import cn.chono.yopper.view.wheel.OnWheelChangedListener;
import cn.chono.yopper.view.wheel.OnWheelScrollListener;
import cn.chono.yopper.view.wheel.WheelView;


/**
 * 修改编辑用户家乡
 *
 * @author sam.sun
 */
public class EditUserHomeTownActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private TextView edit_hometown_provinces_tv;

    private WheelView provinces_wheel;

    private WheelView city_wheel;

    private String strProvince = "上海";
    private String strCity = "普陀";


    private int result_code;

    private String home_str;

    private JSONObject mJsonObj;
    private String[] mProvinceDatas;
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    private ArrayList<String> arrProvinces = new ArrayList<String>();
    private ArrayList<String> arrCitys = new ArrayList<String>();
    private AddressTextAdapter provinceAdapter;
    private AddressTextAdapter cityAdapter;

    private int maxsize = 24;
    private int minsize = 14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            home_str = bundle.getString(YpSettings.USER_HOMETOWN);
            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }


        if (!CheckUtil.isEmpty(home_str)) {
            if (home_str.contains("/")) {
                String a[] = home_str.split("\\/");
                strProvince = a[0];
                strCity = a[1];
            } else {
                String b[] = home_str.split(" ");
                strProvince = b[0];
                strCity = b[1];
            }

        }
        initComponent();

        edit_hometown_provinces_tv.setText(strProvince + " " + strCity);


    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("家乡编辑"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("家乡编辑"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("家乡");
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
        mInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.user_info_edit_hometown_activity, null);

        edit_hometown_provinces_tv = (TextView) contextView.findViewById(R.id.edit_hometown_provinces_tv);
        provinces_wheel = (WheelView) contextView.findViewById(R.id.edit_hometown_provinces_wheel);
        city_wheel = (WheelView) contextView.findViewById(R.id.edit_hometown_city_wheel);


        initJsonData();
        initDatas();
        initProvinces();
        provinceAdapter = new AddressTextAdapter(this, arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
        provinces_wheel.setVisibleItems(5);
        provinces_wheel.setViewAdapter(provinceAdapter);
        provinces_wheel.setCurrentItem(getProvinceItem(strProvince));

        initCitys(mCitisDatasMap.get(strProvince));
        cityAdapter = new AddressTextAdapter(this, arrCitys, getCityItem(strCity), maxsize, minsize);
        city_wheel.setVisibleItems(5);
        city_wheel.setViewAdapter(cityAdapter);
        city_wheel.setCurrentItem(getCityItem(strCity));

        provinces_wheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                strProvince = currentText;
                setTextviewSize(currentText, provinceAdapter);
                String[] citys = mCitisDatasMap.get(currentText);
                initCitys(citys);
                cityAdapter = new AddressTextAdapter(EditUserHomeTownActivity.this, arrCitys, 0, maxsize, minsize);
                city_wheel.setVisibleItems(5);
                city_wheel.setViewAdapter(cityAdapter);
                city_wheel.setCurrentItem(0);
            }
        });

        provinces_wheel.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {


            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
                edit_hometown_provinces_tv.setText(strProvince + " " + strCity);
            }
        });

        city_wheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                strCity = currentText;
                setTextviewSize(currentText, cityAdapter);
            }
        });

        city_wheel.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {


            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
                edit_hometown_provinces_tv.setText(strProvince + " " + strCity);
            }
        });


        this.getMainLayout().addView(contextView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
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
        bundle.putString(YpSettings.USER_HOMETOWN, strProvince + " " + strCity);
        intent.putExtras(bundle);
        EditUserHomeTownActivity.this.setResult(result_code, intent);
        EditUserHomeTownActivity.this.finish();
    }


    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected AddressTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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
    public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
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
     * 从文件中读取地址数据
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("{\"citylist\":[{\"p\":\"北京\",\"c\":[{\"n\":\"东城\"},{\"n\":\"西城\"},{\"n\":\"崇文\"},{\"n\":\"宣武\"},{\"n\":\"朝阳\"},{\"n\":\"丰台\"},{\"n\":\"石景山\"},{\"n\":\"海淀\"},{\"n\":\"门头沟\"},{\"n\":\"房山\"},{\"n\":\"通州\"},{\"n\":\"顺义\"},{\"n\":\"昌平\"},{\"n\":\"大兴\"},{\"n\":\"平谷\"},{\"n\":\"怀柔\"},{\"n\":\"密云\"},{\"n\":\"延庆\"}]},{\"p\":\"上海\",\"c\":[{\"n\":\"黄浦区\"},{\"n\":\"卢湾区\"},{\"n\":\"徐汇区\"},{\"n\":\"长宁区\"},{\"n\":\"静安区\"},{\"n\":\"普陀区\"},{\"n\":\"闸北区\"},{\"n\":\"虹口区\"},{\"n\":\"杨浦区\"},{\"n\":\"闵行区\"},{\"n\":\"宝山区\"},{\"n\":\"嘉定区\"},{\"n\":\"浦东新区\"},{\"n\":\"金山区\"},{\"n\":\"松江区\"},{\"n\":\"奉贤区\"},{\"n\":\"青浦区\"},{\"n\":\"崇明县\"},{\"n\":\"南汇区\"}]},{\"p\":\"天津\",\"c\":[{\"n\":\"和平\"},{\"n\":\"河东\"},{\"n\":\"河西\"},{\"n\":\"南开\"},{\"n\":\"河北\"},{\"n\":\"红挢\"},{\"n\":\"滨海\"},{\"n\":\"东丽\"},{\"n\":\"西青\"},{\"n\":\"津南\"},{\"n\":\"北辰\"},{\"n\":\"宁河\"},{\"n\":\"武清\"},{\"n\":\"静海\"},{\"n\":\"宝坻\"},{\"n\":\"蓟县\"},{\"n\":\"大港\"},{\"n\":\"汉沽\"},{\"n\":\"塘沽\"}]},{\"p\":\"重庆\",\"c\":[{\"n\":\"万州\"},{\"n\":\"涪陵\"},{\"n\":\"渝中\"},{\"n\":\"大渡口\"},{\"n\":\"江北\"},{\"n\":\"沙坪坝\"},{\"n\":\"九龙坡\"},{\"n\":\"南岸\"},{\"n\":\"北碚\"},{\"n\":\"万盛\"},{\"n\":\"双桥\"},{\"n\":\"渝北\"},{\"n\":\"巴南\"},{\"n\":\"长寿\"},{\"n\":\"綦江\"},{\"n\":\"潼南\"},{\"n\":\"铜梁\"},{\"n\":\"大足\"},{\"n\":\"壁山\"},{\"n\":\"梁平\"},{\"n\":\"城口\"},{\"n\":\"丰都\"},{\"n\":\"垫江\"},{\"n\":\"武隆\"},{\"n\":\"忠县\"},{\"n\":\"开县\"},{\"n\":\"云阳\"},{\"n\":\"奉节\"},{\"n\":\"巫山\"},{\"n\":\"巫溪\"},{\"n\":\"黔江\"},{\"n\":\"石柱\"},{\"n\":\"秀山\"},{\"n\":\"酉阳\"},{\"n\":\"彭水\"},{\"n\":\"江津\"},{\"n\":\"合川\"},{\"n\":\"永川\"},{\"n\":\"南川\"}]},{\"p\":\"河北\",\"c\":[{\"n\":\"石家庄\"},{\"n\":\"唐山\"},{\"n\":\"秦皇岛\"},{\"n\":\"邯郸\"},{\"n\":\"邢台\"},{\"n\":\"保定\"},{\"n\":\"张家口\"},{\"n\":\"承德\"},{\"n\":\"沧州\"},{\"n\":\"廊坊\"},{\"n\":\"衡水\"}]},{\"p\":\"山西\",\"c\":[{\"n\":\"太原\"},{\"n\":\"大同\"},{\"n\":\"阳泉\"},{\"n\":\"长治\"},{\"n\":\"晋城\"},{\"n\":\"朔州\"},{\"n\":\"晋中\"},{\"n\":\"运城\"},{\"n\":\"忻州\"},{\"n\":\"临汾\"},{\"n\":\"吕梁\"}]},{\"p\":\"内蒙古\",\"c\":[{\"n\":\"呼和浩特\"},{\"n\":\"包头\"},{\"n\":\"乌海\"},{\"n\":\"通辽\"},{\"n\":\"鄂尔多斯\"},{\"n\":\"呼伦贝尔\"},{\"n\":\"巴彦淖尔\"},{\"n\":\"乌兰察布\"},{\"n\":\"兴安\"},{\"n\":\"锡林郭勒\"},{\"n\":\"阿拉善\"},{\"n\":\"赤峰\"}]},{\"p\":\"辽宁\",\"c\":[{\"n\":\"沈阳\"},{\"n\":\"大连\"},{\"n\":\"鞍山\"},{\"n\":\"抚顺\"},{\"n\":\"本溪\"},{\"n\":\"丹东\"},{\"n\":\"锦州\"},{\"n\":\"营口\"},{\"n\":\"阜新\"},{\"n\":\"辽阳\"},{\"n\":\"盘锦\"},{\"n\":\"铁岭\"},{\"n\":\"朝阳\"},{\"n\":\"葫芦岛\"}]},{\"p\":\"吉林\",\"c\":[{\"n\":\"长春\"},{\"n\":\"吉林\"},{\"n\":\"四平\"},{\"n\":\"辽源\"},{\"n\":\"通化\"},{\"n\":\"白山\"},{\"n\":\"延边\"},{\"n\":\"白城\"},{\"n\":\"松原\"}]},{\"p\":\"黑龙江\",\"c\":[{\"n\":\"哈尔滨\"},{\"n\":\"齐齐哈尔\"},{\"n\":\"鸡西\"},{\"n\":\"鹤岗\"},{\"n\":\"双鸭山\"},{\"n\":\"大庆\"},{\"n\":\"伊春\"},{\"n\":\"佳木斯\"},{\"n\":\"七台河\"},{\"n\":\"牡丹江\"},{\"n\":\"黑河\"},{\"n\":\"绥化\"},{\"n\":\"大兴安岭\"}]},{\"p\":\"江苏\",\"c\":[{\"n\":\"南京\"},{\"n\":\"无锡\"},{\"n\":\"徐州\"},{\"n\":\"常州\"},{\"n\":\"苏州\"},{\"n\":\"南通\"},{\"n\":\"连云港\"},{\"n\":\"淮安\"},{\"n\":\"盐城\"},{\"n\":\"扬州\"},{\"n\":\"镇江\"},{\"n\":\"泰州\"},{\"n\":\"宿迁\"}]},{\"p\":\"浙江\",\"c\":[{\"n\":\"杭州\"},{\"n\":\"宁波\"},{\"n\":\"温州\"},{\"n\":\"嘉兴\"},{\"n\":\"湖州\"},{\"n\":\"金华\"},{\"n\":\"衢州\"},{\"n\":\"舟山\"},{\"n\":\"台州\"},{\"n\":\"丽水\"},{\"n\":\"绍兴\"}]},{\"p\":\"安徽\",\"c\":[{\"n\":\"合肥\"},{\"n\":\"芜湖\"},{\"n\":\"蚌埠\"},{\"n\":\"马鞍山\"},{\"n\":\"淮北\"},{\"n\":\"铜陵\"},{\"n\":\"安庆\"},{\"n\":\"黄山\"},{\"n\":\"滁州\"},{\"n\":\"阜阳\"},{\"n\":\"宿州\"},{\"n\":\"巢湖\"},{\"n\":\"六安\"},{\"n\":\"亳州\"},{\"n\":\"池州\"},{\"n\":\"宣城\"},{\"n\":\"淮南\"}]},{\"p\":\"福建\",\"c\":[{\"n\":\"福州\"},{\"n\":\"厦门\"},{\"n\":\"莆田\"},{\"n\":\"三明\"},{\"n\":\"泉州\"},{\"n\":\"漳州\"},{\"n\":\"南平\"},{\"n\":\"龙岩\"},{\"n\":\"宁德\"}]},{\"p\":\"江西\",\"c\":[{\"n\":\"南昌\"},{\"n\":\"景德镇\"},{\"n\":\"萍乡\"},{\"n\":\"九江\"},{\"n\":\"新余\"},{\"n\":\"鹰潭\"},{\"n\":\"赣州\"},{\"n\":\"吉安\"},{\"n\":\"宜春\"},{\"n\":\"抚州\"},{\"n\":\"上饶\"}]},{\"p\":\"山东\",\"c\":[{\"n\":\"济南\"},{\"n\":\"青岛\"},{\"n\":\"淄博\"},{\"n\":\"枣庄\"},{\"n\":\"东营\"},{\"n\":\"烟台\"},{\"n\":\"潍坊\"},{\"n\":\"济宁\"},{\"n\":\"泰安\"},{\"n\":\"威海\"},{\"n\":\"日照\"},{\"n\":\"莱芜\"},{\"n\":\"临沂\"},{\"n\":\"德州\"},{\"n\":\"聊城\"},{\"n\":\"滨州\"},{\"n\":\"菏泽\"}]},{\"p\":\"河南\",\"c\":[{\"n\":\"郑州\"},{\"n\":\"开封\"},{\"n\":\"洛阳\"},{\"n\":\"平顶山\"},{\"n\":\"安阳\"},{\"n\":\"鹤壁\"},{\"n\":\"新乡\"},{\"n\":\"焦作\"},{\"n\":\"濮阳\"},{\"n\":\"许昌\"},{\"n\":\"漯河\"},{\"n\":\"三门峡\"},{\"n\":\"南阳\"},{\"n\":\"商丘\"},{\"n\":\"信阳\"},{\"n\":\"周口\"},{\"n\":\"驻马店\"},{\"n\":\"济源\"},{\"n\":\"邓州\"},{\"n\":\"巩义\"},{\"n\":\"汝州\"},{\"n\":\"永城\"}]},{\"p\":\"湖北\",\"c\":[{\"n\":\"武汉\"},{\"n\":\"黄石\"},{\"n\":\"十堰\"},{\"n\":\"宜昌\"},{\"n\":\"襄樊\"},{\"n\":\"鄂州\"},{\"n\":\"荆门\"},{\"n\":\"孝感\"},{\"n\":\"荆州\"},{\"n\":\"黄冈\"},{\"n\":\"咸宁\"},{\"n\":\"随州\"},{\"n\":\"恩施\"},{\"n\":\"仙桃\"},{\"n\":\"潜江\"},{\"n\":\"天门\"},{\"n\":\"神农架\"}]},{\"p\":\"湖南\",\"c\":[{\"n\":\"长沙\"},{\"n\":\"株洲\"},{\"n\":\"湘潭\"},{\"n\":\"衡阳\"},{\"n\":\"邵阳\"},{\"n\":\"岳阳\"},{\"n\":\"常德\"},{\"n\":\"张家界\"},{\"n\":\"益阳\"},{\"n\":\"郴州\"},{\"n\":\"永州\"},{\"n\":\"怀化\"},{\"n\":\"娄底\"},{\"n\":\"湘西\"}]},{\"p\":\"广东\",\"c\":[{\"n\":\"广州\"},{\"n\":\"韶关\"},{\"n\":\"深圳\"},{\"n\":\"珠海\"},{\"n\":\"汕头\"},{\"n\":\"佛山\"},{\"n\":\"江门\"},{\"n\":\"湛江\"},{\"n\":\"茂名\"},{\"n\":\"肇庆\"},{\"n\":\"惠州\"},{\"n\":\"梅州\"},{\"n\":\"汕尾\"},{\"n\":\"河源\"},{\"n\":\"阳江\"},{\"n\":\"清远\"},{\"n\":\"东莞\"},{\"n\":\"中山\"},{\"n\":\"潮州\"},{\"n\":\"揭阳\"},{\"n\":\"云浮\"}]},{\"p\":\"广西\",\"c\":[{\"n\":\"南宁\"},{\"n\":\"柳州\"},{\"n\":\"桂林\"},{\"n\":\"梧州\"},{\"n\":\"北海\"},{\"n\":\"防城港\"},{\"n\":\"钦州\"},{\"n\":\"贵港\"},{\"n\":\"玉林\"},{\"n\":\"百色\"},{\"n\":\"贺州\"},{\"n\":\"河池\"},{\"n\":\"来宾\"},{\"n\":\"崇左\"}]},{\"p\":\"海南\",\"c\":[{\"n\":\"海口\"},{\"n\":\"三亚\"},{\"n\":\"五指山\"},{\"n\":\"琼海\"},{\"n\":\"儋州\"},{\"n\":\"文昌\"},{\"n\":\"万宁\"},{\"n\":\"东方\"},{\"n\":\"白沙\"},{\"n\":\"保亭\"},{\"n\":\"昌江\"},{\"n\":\"澄迈\"},{\"n\":\"定安\"},{\"n\":\"乐东\"},{\"n\":\"临高\"},{\"n\":\"陵水\"},{\"n\":\" 南沙群岛\"},{\"n\":\" 琼中\"},{\"n\":\" 屯昌\"},{\"n\":\" 西沙群岛\"},{\"n\":\" 中沙群岛\"}]},{\"p\":\"四川\",\"c\":[{\"n\":\"成都\"},{\"n\":\"自贡\"},{\"n\":\"攀枝花\"},{\"n\":\"泸州\"},{\"n\":\"德阳\"},{\"n\":\"绵阳\"},{\"n\":\"广元\"},{\"n\":\"遂宁\"},{\"n\":\"内江\"},{\"n\":\"乐山\"},{\"n\":\"南充\"},{\"n\":\"眉山\"},{\"n\":\"宜宾\"},{\"n\":\"广安\"},{\"n\":\"达州\"},{\"n\":\"雅安\"},{\"n\":\"巴中\"},{\"n\":\"资阳\"},{\"n\":\"阿坝\"},{\"n\":\"甘孜\"},{\"n\":\"凉山\"}]},{\"p\":\"贵州\",\"c\":[{\"n\":\"贵阳\"},{\"n\":\"六盘水\"},{\"n\":\"遵义\"},{\"n\":\"安顺\"},{\"n\":\"铜仁\"},{\"n\":\"黔西南\"},{\"n\":\"毕节\"},{\"n\":\"黔东南\"},{\"n\":\"黔南\"}]},{\"p\":\"云南\",\"c\":[{\"n\":\"昆明\"},{\"n\":\"曲靖\"},{\"n\":\"玉溪\"},{\"n\":\"保山\"},{\"n\":\"昭通\"},{\"n\":\"丽江\"},{\"n\":\"普洱\"},{\"n\":\"临沧\"},{\"n\":\"楚雄\"},{\"n\":\"红河\"},{\"n\":\"文山\"},{\"n\":\"西双版纳\"},{\"n\":\"大理\"},{\"n\":\"德宏\"},{\"n\":\"怒江\"},{\"n\":\"迪庆\"}]},{\"p\":\"西藏\",\"c\":[{\"n\":\"拉萨\"},{\"n\":\"昌都\"},{\"n\":\"山南\"},{\"n\":\"日喀则\"},{\"n\":\"那曲\"},{\"n\":\"阿里\"},{\"n\":\"林芝\"}]},{\"p\":\"陕西\",\"c\":[{\"n\":\"西安\"},{\"n\":\"铜川\"},{\"n\":\"宝鸡\"},{\"n\":\"咸阳\"},{\"n\":\"渭南\"},{\"n\":\"延安\"},{\"n\":\"汉中\"},{\"n\":\"榆林\"},{\"n\":\"安康\"},{\"n\":\"商洛\"}]},{\"p\":\"甘肃\",\"c\":[{\"n\":\"兰州\"},{\"n\":\"嘉峪关\"},{\"n\":\"金昌\"},{\"n\":\"白银\"},{\"n\":\"天水\"},{\"n\":\"武威\"},{\"n\":\"张掖\"},{\"n\":\"平凉\"},{\"n\":\"酒泉\"},{\"n\":\"庆阳\"},{\"n\":\"定西\"},{\"n\":\"陇南\"},{\"n\":\"临夏\"},{\"n\":\"甘南\"}]},{\"p\":\"青海\",\"c\":[{\"n\":\"西宁\"},{\"n\":\"海东\"},{\"n\":\"海北\"},{\"n\":\"黄南\"},{\"n\":\"海南\"},{\"n\":\"果洛\"},{\"n\":\"玉树\"},{\"n\":\"海西\"}]},{\"p\":\"宁夏\",\"c\":[{\"n\":\"银川\"},{\"n\":\"石嘴山\"},{\"n\":\"吴忠\"},{\"n\":\"固原\"},{\"n\":\"中卫\"}]},{\"p\":\"新疆\",\"c\":[{\"n\":\"乌鲁木齐\"},{\"n\":\"克拉玛依\"},{\"n\":\"吐鲁番\"},{\"n\":\"哈密\"},{\"n\":\"昌吉\"},{\"n\":\"博尔塔拉\"},{\"n\":\"巴音郭楞\"},{\"n\":\"阿克苏\"},{\"n\":\"克孜勒苏\"},{\"n\":\"喀什\"},{\"n\":\"和田\"},{\"n\":\"伊犁\"},{\"n\":\"塔城\"},{\"n\":\"阿勒泰\"},{\"n\":\"石河子\"},{\"n\":\"阿拉尔\"},{\"n\":\"图木舒克\"},{\"n\":\"五家渠\"}]},{\"p\":\"香港\",\"c\":[{\"n\":\"中西\"},{\"n\":\"东\"},{\"n\":\"九龙城\"},{\"n\":\"观塘\"},{\"n\":\"南区\"},{\"n\":\"深水埗\"},{\"n\":\"湾仔\"},{\"n\":\"黄大仙\"},{\"n\":\"油尖旺\"},{\"n\":\"离岛\"},{\"n\":\"葵青\"},{\"n\":\"北\"},{\"n\":\"西贡\"},{\"n\":\"沙田\"},{\"n\":\"屯门\"},{\"n\":\"大埔\"},{\"n\":\"荃湾\"},{\"n\":\"元朗\"},{\"n\":\"新界\"}]},{\"p\":\"澳门\",\"c\":[{\"n\":\"花地玛堂\"},{\"n\":\"圣安多尼堂\"},{\"n\":\"大堂\"},{\"n\":\"望德堂\"},{\"n\":\"风顺堂\"},{\"n\":\"嘉模堂\"},{\"n\":\"圣方济各堂\"}]},{\"p\":\"台湾\",\"c\":[{\"n\":\"台北\"},{\"n\":\"新北\"},{\"n\":\"桃园\"},{\"n\":\"高雄\"},{\"n\":\"基隆\"},{\"n\":\"台中\"},{\"n\":\"台南\"},{\"n\":\"新竹市\"},{\"n\":\"嘉义市\"},{\"n\":\"宜兰县\"},{\"n\":\"新竹县\"},{\"n\":\"苗栗县\"},{\"n\":\"彰化县\"},{\"n\":\"南投县\"},{\"n\":\"嘉义县\"},{\"n\":\"云林县\"},{\"n\":\"屏东县\"},{\"n\":\"台东县\"},{\"n\":\"花莲县\"},{\"n\":\"澎湖县\"},{\"n\":\"阿莲\"},{\"n\":\"安定\"},{\"n\":\"安平\"},{\"n\":\"八德\"},{\"n\":\"八里\"},{\"n\":\"白河\"},{\"n\":\"白沙\"},{\"n\":\"板桥\"},{\"n\":\"褒忠\"},{\"n\":\"宝山\"},{\"n\":\"卑南\"},{\"n\":\"北斗\"},{\"n\":\"北港\"},{\"n\":\"北门\"},{\"n\":\"北埔\"},{\"n\":\"北投\"},{\"n\":\"补子\"},{\"n\":\"布袋\"},{\"n\":\"草屯\"},{\"n\":\"长宾\"},{\"n\":\"长治\"},{\"n\":\"潮州\"},{\"n\":\"车城\"},{\"n\":\"成功\"},{\"n\":\"城中区\"},{\"n\":\"池上\"},{\"n\":\"春日\"},{\"n\":\"刺桐\"}]},{\"p\":\"国外\",\"c\":[{\"n\":\"德国\"},{\"n\":\"新加坡\"},{\"n\":\"美国\"},{\"n\":\"加拿大\"},{\"n\":\"澳大利亚\"},{\"n\":\"日本\"},{\"n\":\"英国\"},{\"n\":\"巴西\"},{\"n\":\"俄罗斯\"},{\"n\":\"尼日利亚\"},{\"n\":\"马来西亚\"},{\"n\":\"爱尔兰\"},{\"n\":\"奥地利\"},{\"n\":\"挪威\"},{\"n\":\"意大利\"},{\"n\":\"西班牙\"},{\"n\":\"泰国\"},{\"n\":\"芬兰\"},{\"n\":\"丹麦\"},{\"n\":\"荷兰\"},{\"n\":\"阿联酋\"},{\"n\":\"瑞典\"},{\"n\":\"瑞士\"},{\"n\":\"比利时\"},{\"n\":\"新西兰\"},{\"n\":\"法国\"},{\"n\":\"匈牙利\"},{\"n\":\"其他\"},{\"n\":\"越南\"},{\"n\":\"以色列\"},{\"n\":\"科威特\"},{\"n\":\"希腊\"},{\"n\":\"南非\"},{\"n\":\"葡萄牙\"},{\"n\":\"墨西哥\"},{\"n\":\"印尼\"}]}]}");
            mJsonObj = new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据
     */
    private void initDatas() {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);
                String province = jsonP.getString("p");

                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("s");
                        mAreasDatas[k] = area;
                    }
                }
                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    /**
     * 初始化省会
     */
    public void initProvinces() {
        int length = mProvinceDatas.length;
        for (int i = 0; i < length; i++) {
            arrProvinces.add(mProvinceDatas[i]);
        }
    }

    /**
     * 根据省会，生成该省会的所有城市
     *
     * @param citys
     */
    public void initCitys(String[] citys) {
        if (citys != null) {
            arrCitys.clear();
            int length = citys.length;
            for (int i = 0; i < length; i++) {
                arrCitys.add(citys[i]);
            }
        } else {
            String[] city = mCitisDatasMap.get("上海");
            arrCitys.clear();
            int length = city.length;
            for (int i = 0; i < length; i++) {
                arrCitys.add(city[i]);
            }
        }
        if (arrCitys != null && arrCitys.size() > 0
                && !arrCitys.contains(strCity)) {
            strCity = arrCitys.get(0);
        }
    }

    /**
     * 初始化地点
     *
     * @param province
     * @param city
     */
    public void setAddress(String province, String city) {
        if (province != null && province.length() > 0) {
            this.strProvince = province;
        }
        if (city != null && city.length() > 0) {
            this.strCity = city;
        }
    }

    /**
     * 返回省会索引，没有就返回默认“四川”
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {
        int size = arrProvinces.size();
        int provinceIndex = 0;
        boolean noprovince = true;
        for (int i = 0; i < size; i++) {
            if (province.equals(arrProvinces.get(i))) {
                noprovince = false;
                return provinceIndex;
            } else {
                provinceIndex++;
            }
        }
        if (noprovince) {
            strProvince = "上海";
            return 8;
        }
        return provinceIndex;
    }

    /**
     * 得到城市索引，没有返回默认“成都”
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        int size = arrCitys.size();
        int cityIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrCitys.get(i));
            if (city.equals(arrCitys.get(i))) {
                nocity = false;
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        if (nocity) {
            strCity = "普陀";
            return 0;
        }
        return cityIndex;
    }


}
