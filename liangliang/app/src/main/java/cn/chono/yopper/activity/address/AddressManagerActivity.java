package cn.chono.yopper.activity.address;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ExpiryDate.ExpiryDateBean;
import cn.chono.yopper.Service.Http.ExpiryDate.ExpiryDateRespBean;
import cn.chono.yopper.Service.Http.ExpiryDate.ExpiryDateService;
import cn.chono.yopper.Service.Http.ExpiryDate.UserInfoBean;
import cn.chono.yopper.Service.Http.GetPrize.GetPrizeReqBean;
import cn.chono.yopper.Service.Http.GetPrize.GetPrizeRespBean;
import cn.chono.yopper.Service.Http.GetPrize.GetPrizeService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;

import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.find.BonusResultActivity;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.view.wheel.AbstractWheelTextAdapter;
import cn.chono.yopper.view.wheel.OnWheelChangedListener;
import cn.chono.yopper.view.wheel.WheelView;

/**
 * Created by jianghua on 2016/3/21.
 */
public class AddressManagerActivity extends MainFrameActivity implements View.OnClickListener {

    private int result_code = 1000;

    private EditText address_name_et;

    private EditText address_phone_et;

    private TextView address_area_et;

    private EditText address_detail_et;

    private Dialog loadingDialog;

    private String address_area;

    private String bouns_id;

    private WheelView provinces_wheel;

    private WheelView city_wheel;

    private WheelView district_wheel;
    private int maxsize = 20;
    private int minsize = 14;
    private String province_name;
    private String city_name;
    private String district_name;

    private String from_flag;

    private PopupWindow mPopwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAddressNames();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from_flag = bundle.getString("from_flag");
            bouns_id = bundle.getString("bouns_id");
        }

        setContentView(R.layout.address_manager_layout);

        final UserInfoBean userBean = getAddress();

        findViewById(R.id.address_edit_back).setOnClickListener(this);

        findViewById(R.id.address_area_layout).setOnClickListener(this);

        Button saveBtn = (Button) findViewById(R.id.address_save_btn);
        saveBtn.setOnClickListener(this);

        address_name_et = (EditText) findViewById(R.id.address_name_et);
        address_name_et.setText(userBean.getName());

        address_phone_et = (EditText) findViewById(R.id.address_phone_et);
        address_phone_et.setText(userBean.getPhone());

        address_area_et = (TextView) findViewById(R.id.address_area_et);
        address_area_et.setText(userBean.getArea());

        address_detail_et = (EditText) findViewById(R.id.address_detail_et);
        address_detail_et.setText(userBean.getAddress());

        address_area = userBean.getArea();


        if (!TextUtils.isEmpty(from_flag)) {
            if ("mybonus".equals(from_flag)) {//领取奖品
                saveBtn.setText("领取");
            } else {//兑换
                saveBtn.setText("兑换");
            }

        }

        edtiTextListener();
        initPop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initPop(Context context){

        View contentView = LayoutInflater.from(context).inflate(
                R.layout.view_address_layout, null);

        initWheelView(contentView);

        mPopwindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

        mPopwindow.setFocusable(true);
        mPopwindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.address_manager_btn_bg));
    }

    private void initWheelView(View view) {

        provinces_wheel = (WheelView) view.findViewById(R.id.address_manager_provinces_wheel);
        city_wheel = (WheelView) view.findViewById(R.id.address_manager_city_wheel);
        district_wheel = (WheelView) view.findViewById(R.id.address_manager_district_wheel);

        province_name = province.get(0);
        city_name = firstWheel.get(province.get(0)).get(0);
        district_name = secondWheel.get(firstWheel.get(province.get(0)).get(0)).get(0);
        provinceAdapter = new AddressTextAdapter(this, province, 0, maxsize, minsize);
        provinces_wheel.setVisibleItems(5);
        provinces_wheel.setViewAdapter(provinceAdapter);
        provinces_wheel.setCurrentItem(0);

        provinces_wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                province_name = currentText;
                setTextviewSize(currentText, provinceAdapter);
                List<String> cityArray = firstWheel.get(province_name);

                cityAdapter = new AddressTextAdapter(AddressManagerActivity.this, cityArray, 0, maxsize, minsize);
                city_wheel.setVisibleItems(5);
                city_wheel.setViewAdapter(cityAdapter);
                city_wheel.setCurrentItem(0);

                List<String> districtArray = secondWheel.get(cityArray.get(0));
                districtAdapter = new AddressTextAdapter(AddressManagerActivity.this, districtArray, 0, maxsize, minsize);
                district_wheel.setVisibleItems(5);
                district_wheel.setViewAdapter(districtAdapter);
                district_wheel.setCurrentItem(0);
                address_area_et.setText(province_name + " " + cityAdapter.getItemText(city_wheel.getCurrentItem()) + " " + districtAdapter.getItemText(district_wheel.getCurrentItem()));
            }
        });

        cityAdapter = new AddressTextAdapter(this, firstWheel.get(province.get(0)), 0, maxsize, minsize);
        city_wheel.setVisibleItems(5);
        city_wheel.setViewAdapter(cityAdapter);
        city_wheel.setCurrentItem(0);

        city_wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                city_name = currentText;
                setTextviewSize(currentText, cityAdapter);
                List<String> districtArray = secondWheel.get(city_name);
                districtAdapter = new AddressTextAdapter(AddressManagerActivity.this, districtArray, 0, maxsize, minsize);
                district_wheel.setVisibleItems(5);
                district_wheel.setViewAdapter(districtAdapter);
                district_wheel.setCurrentItem(0);
                address_area_et.setText(province_name + " " + cityAdapter.getItemText(city_wheel.getCurrentItem()) + " " + districtAdapter.getItemText(district_wheel.getCurrentItem()));
            }
        });

        districtAdapter = new AddressTextAdapter(this, secondWheel.get(firstWheel.get(province.get(0)).get(0)), 0, maxsize, minsize);
        district_wheel.setVisibleItems(5);
        district_wheel.setViewAdapter(districtAdapter);
        district_wheel.setCurrentItem(0);

        district_wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) districtAdapter.getItemText(wheel.getCurrentItem());
                district_name = currentText;
                setTextviewSize(currentText, districtAdapter);
                address_area_et.setText(province_name + " " + cityAdapter.getItemText(city_wheel.getCurrentItem()) + " " + districtAdapter.getItemText(district_wheel.getCurrentItem()));
            }
        });
    }

    private void edtiTextListener() {
        address_name_et.addTextChangedListener(new TextWatcher() {

            //flag避免settext导致触发ontextChanaged
            private int flag = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 8 && flag == 0) {
                    flag = 1;
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "超过字符限制");
                    String newStr = s.toString().substring(0, 8);
                    address_name_et.setText(newStr);
                    address_name_et.setSelection(newStr.length());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                flag = 0;
            }
        });

        address_phone_et.addTextChangedListener(new TextWatcher() {
            int flag = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 11 && flag == 0) {
                    flag = 1;
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "超过字符限制");
                    String newStr = s.toString().substring(0, 11);
                    address_phone_et.setText(newStr);
                    address_phone_et.setSelection(newStr.length());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                flag = 0;
            }
        });

        address_detail_et.addTextChangedListener(new TextWatcher() {
            int flag = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().contains(" ")){

                }
                if (s.toString().length() > 30 && flag == 0) {
                    flag = 1;
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "超过字符限制");
                    String newStr = s.toString().substring(0, 30);
                    address_detail_et.setText(newStr);
                    address_detail_et.setSelection(newStr.length());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                flag = 0;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 6600){
            setResult(1);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.address_edit_back:

                finish();
                break;

            case R.id.address_area_layout:
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if(mPopwindow != null){
                    if(!mPopwindow.isShowing()){
                        mPopwindow.showAtLocation(AddressManagerActivity.this.findViewById(R.id.address_save_btn), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }else{
                        mPopwindow.dismiss();
                    }
                }
//                Bundle bundle = new Bundle();
//
//                bundle.putString(YpSettings.USER_HOMETOWN, "".equals(address_area) == true ? "上海" + " " + "普陀" : address_area);
//                bundle.putInt(YpSettings.INTENT_RESULT_CODE, 6700);
//
//                ActivityUtil.jumpForResult(AddressManagerActivity.this, EditUserHomeTownActivity.class, bundle, 6700, 0, 100);
                break;

            case R.id.address_save_btn:

                String address_name = address_name_et.getText().toString();

                String address_phone = address_phone_et.getText().toString();

                String address_area = address_area_et.getText().toString();

                String address_detail = address_detail_et.getText().toString();


                //判断是否是中文
                if (!checkNameChese(address_name)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "姓名只能输入中文");
                    address_name_et.setText("");
                    return;
                }

                if (TextUtils.isEmpty(address_name)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "请填写用户名");
                    return;
                }

                if (TextUtils.isEmpty(address_phone)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "请填写联系电话");
                    return;
                }

                if (TextUtils.isEmpty(address_area)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "请填写区域");
                    return;
                }

                if (TextUtils.isEmpty(address_detail)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "请填写详细地址");
                    return;
                }

                UserInfoBean userBean = new UserInfoBean();
                userBean.setName(address_name);
                userBean.setPhone(address_phone);
                userBean.setArea(address_area);
                userBean.setAddress(address_detail);

                saveAddress(userBean);

                if (!TextUtils.isEmpty(from_flag) && !TextUtils.isEmpty(bouns_id)) {

                    if ("mybonus".equals(from_flag)) {//领取奖品
                        getBonus(bouns_id, userBean);
                    } else {//兑换
                        exchanageBonus(bouns_id, userBean);
                    }
                }else{
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this,"出现不明异常");
                }
                break;

            default:
                break;
        }
    }

    /**
     * 获取用户兑奖地址信息
     */
    private UserInfoBean getAddress() {

        loadingDialog = DialogUtil.LoadingDialog(AddressManagerActivity.this, null);
        if (!isFinishing()) {
            loadingDialog.show();
        }

        UserInfoBean userBean = new UserInfoBean();

        userBean.setName(SharedprefUtil.get(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_NAME, ""));

        userBean.setPhone(SharedprefUtil.get(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_PHONE, ""));

        userBean.setArea(SharedprefUtil.get(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_AREA, ""));

        userBean.setAddress(SharedprefUtil.get(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_DETAIL, ""));


        loadingDialog.dismiss();

        return userBean;
    }

    /**
     * 保存用户兑奖地址
     *
     * @param userBean
     */
    private void saveAddress(UserInfoBean userBean) {

        loadingDialog = DialogUtil.LoadingDialog(AddressManagerActivity.this, null);
        if (!isFinishing()) {
            loadingDialog.show();
        }


        SharedprefUtil.save(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_NAME, userBean.getName());

        SharedprefUtil.save(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_PHONE, userBean.getPhone());

        SharedprefUtil.save(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_AREA, userBean.getArea());

        SharedprefUtil.save(this, LoginUser.getInstance().getUserId() + YpSettings.ADDRESS_USER_DETAIL, userBean.getAddress());

        loadingDialog.dismiss();
    }

    /**
     * 兑换奖品
     *
     * @param prizeId
     * @param userBean
     */
    private void exchanageBonus(String prizeId, UserInfoBean userBean) {

        //初始化兑奖参数
        ExpiryDateBean paramBean = new ExpiryDateBean();
        paramBean.setPrizeId(prizeId);
        paramBean.setUserAddress(userBean);

        ExpiryDateService service = new ExpiryDateService(this);
        service.parameter(paramBean);

        service.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                ExpiryDateRespBean bean = (ExpiryDateRespBean) respBean;
                ExpiryDateRespBean.ExpiryResp resp = bean.getResp();
                if (null == resp) return;

                String resultCode = resp.getMsg();

                Bundle bundle = new Bundle();
                if (TextUtils.isEmpty(resultCode)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "兑换成功");
                    bundle.putString("from_flag", from_flag);
                    bundle.putInt("result_code", 1);
                    bundle.putString("prize_name",resp.getName());
                    bundle.putString("prize_url", resp.getUrlPic());

                } else {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, resp.getMsg());
                    bundle.putString("from_flag", from_flag);
                    bundle.putInt("result_code", 0);
                    bundle.putString("prize_name",resp.getName());
                    bundle.putString("prize_url",resp.getUrlPic());
                }
                ActivityUtil.jumpForResult(AddressManagerActivity.this, BonusResultActivity.class, bundle, 6600, 0, 100);
                finish();
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                String msg = respBean.getMsg();
                if (!TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, msg);
                    return;
                }

                DialogUtil.showDisCoverNetToast(AddressManagerActivity.this);
            }
        });
        service.enqueue();
    }

    /**
     * 领奖
     */
    private void getBonus(String prizeId, UserInfoBean userBean) {
        GetPrizeReqBean reqBean = new GetPrizeReqBean();
        reqBean.setUserPrizeId(prizeId);
        reqBean.setUserAddress(userBean);

        GetPrizeService getPrizeService = new GetPrizeService(this);

        if (reqBean == null) {
            return;
        }

        getPrizeService.parameter(reqBean);
        getPrizeService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                GetPrizeRespBean prizeRespBean = (GetPrizeRespBean) respBean;
                GetPrizeRespBean.GetPrizeRespBeanContent resp = prizeRespBean.getResp();

                if (null == resp) return;

                String resultStr = resp.getMsg();

                Bundle bundle = new Bundle();
                if (TextUtils.isEmpty(resultStr)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, "领奖成功");
                    bundle.putString("from_flag", from_flag);
                    bundle.putInt("result_code", 1);
                    bundle.putString("prize_name",resp.getPrize().getName());
                    bundle.putString("prize_url", resp.getPrize().getImageUrl());

                } else {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, resp.getMsg());
                    bundle.putString("from_flag", from_flag);
                    bundle.putInt("result_code", 0);
                    bundle.putString("prize_name",resp.getPrize().getName());
                    bundle.putString("prize_url", resp.getPrize().getImageUrl());
                }

                ActivityUtil.jumpForResult(AddressManagerActivity.this, BonusResultActivity.class, bundle, 6600, 0, 100);
                finish();
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String msg = respBean.getMsg();

                if (!TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(AddressManagerActivity.this, msg);
                    return;
                }
                DialogUtil.showDisCoverNetToast(AddressManagerActivity.this);
            }
        });
        getPrizeService.enqueue();
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public boolean checkNameChese(String name) {

        //中文的正则表达式
        Pattern p = Pattern.compile("^[\u4E00-\u9FA5\uF900-\uFA2D]+$");
        Matcher m = p.matcher(name);
        if(m.matches()){
            return true;
        }
        return false;
    }

    HashMap<String, List<String>> firstWheel = new HashMap<>();
    HashMap<String, List<String>> secondWheel = new HashMap<>();

    List<String> province = new ArrayList<>();

    AddressTextAdapter provinceAdapter;
    AddressTextAdapter cityAdapter;
    AddressTextAdapter districtAdapter;

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
                textvew.setTextSize(maxsize);
            } else {
                textvew.setTextSize(minsize);
            }
        }
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        List<String> mlist = new ArrayList<>();

        protected AddressTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);

            for (String s : list) {
                if (s.contains("|")) {
                    mlist.add(s.substring(0, s.indexOf("|")));
                } else {
                    mlist.add(s);
                }
            }
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return mlist.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return mlist.get(index) + "";
        }
    }

    public void getAddressNames() {
        get(names0);
        get(names);
        for (String s : province) {
        }

        for (String key : firstWheel.keySet()) {
        }

        for (String key : secondWheel.keySet()) {
        }
    }

    public void get(String names) {
        names = names.replace("\n", "");
        names = names.replace(" ", "");
        String[] firstTemp = names.split("\"\\},");
        String provinceS = "";
        String temp = "";
        for (int i = 0; i < firstTemp.length; i++) {
            temp = firstTemp[i].substring(1, firstTemp[i].indexOf("\":{"));
            provinceS = temp;
            province.add(temp);
            temp = firstTemp[i].substring(firstTemp[i].indexOf("{") + 1);

            String[] tempArr = temp.split(":");
            List<String> ls = new ArrayList<>();
            List<String> city = new ArrayList<>();
            if (tempArr.length == 2) {//当只有一个节点的时候，比如北京下面的北京
                temp = tempArr[0].replace("\"", "");
                city.add(temp);
                firstWheel.put(provinceS, city);
                tempArr = tempArr[1].replace("\"", "").split(",");
                ls = Arrays.asList(tempArr);
                secondWheel.put(temp, ls);
                ls = null;
                city = null;
            } else {//多个节点的时候，用冒号做分割，可以考虑用引号带逗号分割
                for (int j = 1; j < tempArr.length; j++) {
                    if ((j != tempArr.length - 1) && j != 0) {
                        tempArr[j - 1] = tempArr[j - 1] + ":" + tempArr[j].substring(0, tempArr[j].lastIndexOf(","));
                        tempArr[j] = tempArr[j].substring(tempArr[j].lastIndexOf(",") + 1, tempArr[j].length() - 1);
                        city.add(tempArr[j - 1].substring(1, tempArr[j - 1].indexOf(":")).replace("\"", ""));

                        String[] sec = tempArr[j - 1].split(":");
                        sec = sec[1].replace("\"", "").split(",");
                        ls = Arrays.asList(sec);
                        secondWheel.put(tempArr[j - 1].substring(1, tempArr[j - 1].indexOf(":")).replace("\"", ""), ls);
                    } else {
                        String[] sec = tempArr[j].replace("\"", "").split(",");
                        ls = Arrays.asList(sec);
                        secondWheel.put(tempArr[j - 1].substring(1, tempArr[j - 1].indexOf(":")).replace("\"", ""), ls);
                    }

                    if (j == tempArr.length - 2) {
                        tempArr[j] = tempArr[j] + ":" + tempArr[j + 1];
                        city.add(tempArr[j].substring(1, tempArr[j].indexOf(":")));
                    }
                    firstWheel.put(provinceS, city);
                    ls = null;
                }
                city = null;
            }
        }
    }


    String names0 =
            "\"北京市\": {\n" +
                    "        \"北京市\": \"北京市|1015,昌平区|1029,朝阳区|1021,崇文区|1019,大兴区|1030,东城区|1017,房山区|1026,丰台区|1022,海淀区|1024,怀柔区|1031,门头沟区|1025,密云县|1033,平谷区|1032,石景山区|1023,顺义区|1028,天安门|1016,通州区|1027,西城区|1018,宣武区|1020,延庆县|1034\"\n" +
                    "    },\n" +
                    "    \"上海市\": {\n" +
                    "        \"上海市\": \"宝山区|759,长宁区|752,崇明县|767,奉贤区|766,虹口区|756,黄浦区|749,嘉定区|760,金山区|762,静安区|753,卢湾区|750,闵行区|758,南汇区|765,浦东新区|761,普陀区|754,青浦区|764,上海市|748,松江区|763,徐汇区|751,杨浦区|757,闸北区|755\"\n" +
                    "    },\n" +
                    "    \"天津市\": {\n" +
                    "        \"天津市\": \"宝坻区|1342,北辰区|1340,滨海新区|1343,大港区|1336,东丽区|1337,汉沽区|1335,和平区|1328,河北区|1332,河东区|1329,河西区|1330,红桥区|1333,蓟县|1346,津南区|1339,静海县|1345,南开区|1331,宁河县|1344,塘沽区|1334,天津市|1327,武清区|1341,西青区|1338\"\n" +
                    "    },\n" +
                    "    \"重庆市\": {\n" +
                    "        \"重庆市\": \"巴南区|3597,北碚区|3593,璧山县|3605,长寿区|3599,城口县|3607,大渡口区|3588,大足县|3603,垫江县|3609,丰都县|3608,奉节县|3614,涪陵区|3586,江北区|3589,九龙坡区|3591,开县|3612,梁平县|3606,南岸区|3592,彭水县|3620,綦江县|3600,黔江区|3598,荣昌县|3604,沙坪坝区|3590,石柱县|3617,双桥区|3595,铜梁县|3602,潼南县|3601,万盛区|3594,万州区|3585,巫山县|3615,巫溪县|3616,武隆县|3610,秀山县|3618,酉阳县|3619,渝北区|3596,渝中区|3587,云阳县|3613,忠县|3611,重庆市|3584\"\n" +
                    "    },\n" +
                    "    \"安徽省\": {\n" +
                    "        \"合肥市\": \"包河区|1390,长丰县|1391,肥东县|1392,肥西县|1393,合肥市|1386,庐阳区|1388,蜀山区|1389,瑶海区|1387\",\n" +
                    "        \"安庆市\": \"安庆市|1394,枞阳县|1399,大观区|1396,怀宁县|1398,郊区|1397,潜山县|1400,宿松县|1402,太湖县|1401,桐城市|1405,望江县|1403,迎江区|1395,岳西县|1404\",\n" +
                    "        \"蚌埠市\": \"蚌埠市|1458,蚌山区|1460,固镇县|1465,怀远县|1463,淮上区|1462,龙子湖区|1459,五河县|1464,禹会区|1461\",\n" +
                    "        \"亳州市\": \"亳州市|1373,利辛县|1377,蒙城县|1376,谯城区|1374,涡阳县|1375\",\n" +
                    "        \"巢湖市\": \"巢湖市|1420,含山县|1424,和县|1425,居巢区|1421,庐江县|1422,无为县|1423\",\n" +
                    "        \"池州市\": \"池州市|1426,东至县|1428,贵池区|1427,青阳县|1430,石台县|1429\",\n" +
                    "        \"滁州市\": \"滁州市|1443,定远县|1448,凤阳县|1449,来安县|1446,琅琊区|1444,明光市|1451,南谯区|1445,全椒县|1447,天长市|1450\",\n" +
                    "        \"阜阳市\": \"阜南县|1477,阜阳市|1471,临泉县|1475,太和县|1476,颍东区|1473,颍泉区|1474,颍上县|1478,颍州区|1472\",\n" +
                    "        \"淮北市\": \"杜集区|1432,淮北市|1431,烈山区|1434,濉溪县|1435,相山区|1433\",\n" +
                    "        \"淮南市\": \"八公山区|1440,大通区|1437,凤台县|1442,淮南市|1436,潘集区|1441,田家庵区|1438,谢家集区|1439\",\n" +
                    "        \"黄山市\": \"黄山区|1486,黄山市|1484,徽州区|1487,祁门县|1491,屯溪区|1485,歙县|1488,休宁县|1489,黟县|1490\",\n" +
                    "        \"六安市\": \"霍邱县|1382,霍山县|1385,金安区|1379,金寨县|1384,六安市|1378,寿县|1381,舒城县|1383,裕安区|1380\",\n" +
                    "        \"马鞍山市\": \"当涂县|1483,花山区|1481,金家庄区|1480,马鞍山市|1479,雨山区|1482\",\n" +
                    "        \"宿州市\": \"砀山县|1416,灵璧县|1418,泗县|1419,宿州市|1414,萧县|1417,埇桥区|1415\",\n" +
                    "        \"铜陵市\": \"郊区|1469,狮子山区|1468,铜官山区|1467,铜陵市|1466,铜陵县|1470\",\n" +
                    "        \"芜湖市\": \"繁昌县|1456,镜湖区|1453,鸠江区|1454,南陵县|1457,芜湖市|1452,芜湖县|1455\",\n" +
                    "        \"宣城市\": \"广德县|1409,绩溪县|1411,泾县|1410,旌德县|1412,郎溪县|1408,宁国市|1413,宣城市|1406,宣州区|1407\"\n" +
                    "    },\n" +
                    "    \"福建省\": {\n" +
                    "        \"福州市\": \"福州市|3269,仓山区|3272,长乐市|3282,福清市|3281,鼓楼区|3270,晋安区|3274,连江县|3276,罗源县|3277,马尾区|3273,闽侯县|3275,闽清县|3278,平潭县|3280,台江区|3271,永泰县|3279\",\n" +
                    "        \"龙岩市\": \"长汀县|3291,连城县|3295,龙岩市|3289,上杭县|3293,武平县|3294,新罗区|3290,永定县|3292,漳平市|3296\",\n" +
                    "        \"南平市\": \"光泽县|3220,建瓯市|3225,建阳市|3226,南平市|3216,浦城县|3219,邵武市|3223,顺昌县|3218,松溪县|3221,武夷山市|3224,延平区|3217,政和县|3222\",\n" +
                    "        \"宁德市\": \"福安市|3242,福鼎市|3243,古田县|3237,蕉城区|3235,宁德市|3234,屏南县|3238,寿宁县|3239,霞浦县|3236,柘荣县|3241,周宁县|3240\",\n" +
                    "        \"莆田市\": \"城厢区|3284,涵江区|3285,荔城区|3286,莆田市|3283,仙游县|3288,秀屿区|3287\",\n" +
                    "        \"泉州市\": \"安溪县|3250,德化县|3252,丰泽区|3246,惠安县|3249,金门县|3253,晋江市|3255,鲤城区|3245,洛江区|3247,南安市|3256,泉港区|3248,泉州市|3244,石狮市|3254,永春县|3251\",\n" +
                    "        \"三明市\": \"大田县|3209,建宁县|3214,将乐县|3212,梅列区|3204,明溪县|3206,宁化县|3208,清流县|3207,三明市|3203,三元区|3205,沙县|3211,泰宁县|3213,永安市|3215,尤溪县|3210\",\n" +
                    "        \"厦门市\": \"海沧区|3229,湖里区|3230,集美区|3231,思明区|3228,同安区|3232,厦门市|3227,翔安区|3233\",\n" +
                    "        \"漳州市\": \"长泰县|3263,东山县|3264,华安县|3267,龙海市|3268,龙文区|3259,南靖县|3265,平和县|3266,芗城区|3258,云霄县|3260,漳浦县|3261,漳州市|3257,诏安县|3262\"\n" +
                    "    },\n" +
                    "    \"甘肃省\": {\n" +
                    "        \"兰州市\": \"城关区|3118,皋兰县|3122,红古区|3120,兰州市|3117,西固区|3119,永登县|3121,榆中县|3123\",\n" +
                    "        \"白银市\": \"白银区|3178,白银市|3177,会宁县|3181,景泰县|3182,靖远县|3180,平川区|3179\",\n" +
                    "        \"定西市\": \"安定区|3132,定西市|3131,临洮县|3136,陇西县|3134,岷县|3138,通渭县|3133,渭源县|3135,漳县|3137\",\n" +
                    "        \"甘南州\": \"迭部县|3173,甘南州|3168,合作市|3169,临潭县|3170,碌曲县|3175,玛曲县|3174,夏河县|3176,舟曲县|3172,卓尼县|3171\",\n" +
                    "        \"嘉峪关市\": \"嘉峪关市|3124\",\n" +
                    "        \"金昌市\": \"金昌市|3190,金川区|3191,永昌县|3192\",\n" +
                    "        \"酒泉市\": \"阿克塞县|3187,敦煌市|3189,金塔县|3185,酒泉市|3183,肃北县|3186,肃州区|3184,玉门市|3188\",\n" +
                    "        \"临夏州\": \"东乡族自治县|3116,广河县|3114,和政县|3115,康乐县|3112,临夏市|3110,临夏县|3111,临夏州|3109,永靖县|3113\",\n" +
                    "        \"陇南市\": \"成县|3195,宕昌县|3197,徽县|3201,康县|3198,礼县|3200,两当县|3202,陇南市|3193,文县|3196,武都区|3194,西和县|3199\",\n" +
                    "        \"平凉市\": \"崇信县|3143,华亭县|3144,泾川县|3141,静宁县|3146,崆峒区|3140,灵台县|3142,平凉市|3139,庄浪县|3145\",\n" +
                    "        \"庆阳市\": \"合水县|3152,华池县|3151,环县|3150,宁县|3154,庆城县|3149,庆阳市|3147,西峰区|3148,镇原县|3155,正宁县|3153\",\n" +
                    "        \"天水市\": \"甘谷县|3128,秦安县|3127,清水县|3126,天水市|3125,武山县|3129,张家川县|3130\",\n" +
                    "        \"武威市\": \"古浪县|3166,凉州区|3164,民勤县|3165,天祝县|3167,武威市|3163\",\n" +
                    "        \"张掖市\": \"甘州区|3157,高台县|3161,临泽县|3160,民乐县|3159,山丹县|3162,肃南县|3158,张掖市|3156\"\n" +
                    "    },\n" +
                    "    \"广东省\": {\n" +
                    "        \"广州市\": \"白云区|1814,从化市|1819,番禺区|1816,广州市|1809,海珠区|1812,花都区|1817,黄埔区|1815,荔湾区|1810,天河区|1813,越秀区|1811,增城市|1818\",\n" +
                    "        \"潮州市\": \"潮安县|1892,潮州市|1890,饶平县|1893,湘桥区|1891\",\n" +
                    "        \"东莞市\": \"东莞市|1796\",\n" +
                    "        \"佛山市\": \"佛山市|1804,高明区|1808,南海区|1805,三水区|1807,顺德区|1806\",\n" +
                    "        \"河源市\": \"东源县|1863,和平县|1862,河源市|1857,连平县|1861,龙川县|1860,源城区|1858,紫金县|1859\",\n" +
                    "        \"惠州市\": \"博罗县|1823,惠城区|1821,惠东县|1824,惠阳区|1822,惠州市|1820,龙门县|1825\",\n" +
                    "        \"江门市\": \"恩平市|1856,鹤山市|1855,江门市|1851,开平市|1854,台山市|1853,新会区|1852\",\n" +
                    "        \"揭阳市\": \"惠来县|1829,揭东县|1827,揭西县|1828,揭阳市|1826,普宁市|1830\",\n" +
                    "        \"茂名市\": \"电白县|1910,高州市|1911,化州市|1912,茂港区|1909,茂名市|1907,茂南区|1908,信宜市|1913\",\n" +
                    "        \"梅州市\": \"大埔县|1834,丰顺县|1835,蕉岭县|1838,梅江区|1832,梅县|1833,梅州市|1831,平远县|1837,五华县|1836,兴宁市|1839\",\n" +
                    "        \"清远市\": \"佛冈县|1873,连南县|1876,连山县|1875,连州市|1879,清城区|1872,清新县|1877,清远市|1871,阳山县|1874,英德市|1878\",\n" +
                    "        \"汕头市\": \"潮南区|1844,潮阳区|1843,澄海区|1845,金平区|1842,龙湖区|1841,南澳县|1846,汕头市|1840\",\n" +
                    "        \"汕尾市\": \"海丰县|1848,陆丰市|1850,陆河县|1849,汕尾市|1847\",\n" +
                    "        \"韶关市\": \"乐昌市|1928,南雄市|1929,曲江区|1922,仁化县|1924,乳源县|1926,韶关市|1919,始兴县|1923,翁源县|1925,武江区|1920,新丰县|1927,浈江区|1921\",\n" +
                    "        \"深圳市\": \"宝安区|1868,福田区|1866,龙岗区|1869,罗湖区|1865,南山区|1867,深圳市|1864,盐田区|1870\",\n" +
                    "        \"阳江市\": \"江城区|1915,阳春市|1918,阳东县|1917,阳江市|1914,阳西县|1916\",\n" +
                    "        \"云浮市\": \"罗定市|1803,新兴县|1800,郁南县|1801,云安县|1802,云城区|1799,云浮市|1798\",\n" +
                    "        \"湛江市\": \"赤坎区|1881,雷州市|1888,廉江市|1887,麻章区|1884,坡头区|1883,遂溪县|1885,吴川市|1889,霞山区|1882,徐闻县|1886,湛江市|1880\",\n" +
                    "        \"肇庆市\": \"德庆县|1904,鼎湖区|1900,端州区|1899,封开县|1903,高要市|1905,广宁县|1901,怀集县|1902,四会市|1906,肇庆市|1898\",\n" +
                    "        \"中山市\": \"中山市|1797\",\n" +
                    "        \"珠海市\": \"斗门区|1896,金湾区|1897,香洲区|1895,珠海市|1894\"\n" +
                    "    },\n" +
                    "    \"广西自治区\": {\n" +
                    "        \"南宁市\": \"宾阳县|1943,横县|1944,江南区|1935,良庆区|1937,隆安县|1940,马山县|1941,南宁市|1933,上林县|1942,武鸣县|1939,西乡塘区|1936,兴宁区|1934,邕宁区|1938\",\n" +
                    "        \"百色市\": \"百色市|2001,德保县|2005,靖西县|2006,乐业县|2009,凌云县|2008,隆林县|2012,那坡县|2007,平果县|2004,田东县|2003,田林县|2010,田阳县|2002,西林县|2011\",\n" +
                    "        \"北海市\": \"北海市|1930,合浦县|1932,铁山港区|1931\",\n" +
                    "        \"崇左市\": \"崇左市|1945,大新县|1949,扶绥县|1946,龙州县|1948,宁明县|1947,凭祥市|1951,天等县|1950\",\n" +
                    "        \"防城港市\": \"东兴市|2029,防城港市|2025,防城区|2027,港口区|2026,上思县|2028\",\n" +
                    "        \"贵港市\": \"贵港市|2013,桂平市|2016,平南县|2015,覃塘区|2014\",\n" +
                    "        \"桂林市\": \"恭城县|1977,灌阳县|1973,桂林市|1966,临桂县|1968,灵川县|1969,龙胜县|1974,平乐县|1976,全州县|1970,兴安县|1971,阳朔县|1967,永福县|1972,资源县|1975\",\n" +
                    "        \"河池市\": \"巴马县|1991,大化县|1993,东兰县|1988,都安县|1992,凤山县|1987,河池市|1983,环江县|1990,金城江区|1984,罗城县|1989,南丹县|1985,天峨县|1986,宜州市|1994\",\n" +
                    "        \"贺州市\": \"富川县|2020,贺州市|2017,昭平县|2018,钟山县|2019\",\n" +
                    "        \"来宾市\": \"合山市|1957,金秀县|1956,来宾市|1952,武宣县|1955,象州县|1954,忻城县|1953\",\n" +
                    "        \"柳州市\": \"柳城县|1961,柳江县|1960,柳南区|1959,柳州市|1958,鹿寨县|1962,融安县|1963,融水县|1964,三江县|1965\",\n" +
                    "        \"钦州市\": \"灵山县|2023,浦北县|2024,钦北区|2022,钦州市|2021\",\n" +
                    "        \"梧州市\": \"苍梧县|1979,岑溪市|1982,蒙山县|1981,藤县|1980,梧州市|1978\",\n" +
                    "        \"玉林市\": \"北流市|2000,博白县|1998,陆川县|1997,容县|1996,兴业县|1999,玉林市|1995\"\n" +
                    "    },\n" +
                    "    \"贵州省\": {\n" +
                    "        \"贵阳市\": \"白云区|3409,贵阳市|3405,开阳县|3411,南明区|3406,清镇市|3414,乌当区|3408,息烽县|3412,小河区|3410,修文县|3413,云岩区|3407\",\n" +
                    "        \"安顺市\": \"安顺市|3390,关岭县|3395,平坝县|3392,普定县|3393,西秀区|3391,镇宁县|3394,紫云县|3396\",\n" +
                    "        \"毕节地区\": \"毕节地区|3397,毕节市|3398,大方县|3399,赫章县|3404,金沙县|3401,纳雍县|3403,黔西县|3400,织金县|3402\",\n" +
                    "        \"六盘水市\": \"六盘水市|3377,六枝特区|3379,盘县|3381,水城县|3380,钟山区|3378\",\n" +
                    "        \"黔东南州\": \"岑巩县|3447,从江县|3454,丹寨县|3457,黄平县|3443,剑河县|3450,锦屏县|3449,凯里市|3442,雷山县|3455,黎平县|3452,麻江县|3456,黔东南州|3441,榕江县|3453,三穗县|3445,施秉县|3444,台江县|3451,天柱县|3448,镇远县|3446\",\n" +
                    "        \"黔南州\": \"长顺县|3467,都匀市|3459,独山县|3464,福泉市|3460,贵定县|3462,惠水县|3469,荔波县|3461,龙里县|3468,罗甸县|3466,平塘县|3465,黔南州|3458,三都县|3470,瓮安县|3463\",\n" +
                    "        \"铜仁地区\": \"德江县|3437,江口县|3432,石阡县|3434,思南县|3435,松桃县|3439,铜仁地区|3430,铜仁市|3431,万山特区|3440,沿河县|3438,印江县|3436,玉屏县|3433\",\n" +
                    "        \"兴义市\": \"安龙县|3389,册亨县|3388,普安县|3384,晴隆县|3385,望谟县|3387,兴仁县|3383,兴义市|3382,贞丰县|3386\",\n" +
                    "        \"遵义市\": \"赤水市|3428,道真县|3422,凤冈县|3424,红花岗区|3416,汇川区|3417,湄潭县|3425,仁怀市|3429,绥阳县|3420,桐梓县|3419,务川县|3423,习水县|3427,余庆县|3426,正安县|3421,遵义市|3415,遵义县|3418\"\n" +
                    "    },\n" +
                    "    \"海南省\": {\n" +
                    "        \"海口市\": \"海口市|2853,龙华区|2855,美兰区|2857,琼山区|2856,秀英区|2854\",\n" +
                    "        \"三亚市\": \"三亚市|2836\",\n" +
                    "        \"五指山市\": \"白沙县|2847,保亭县|2851,昌江县|2848,澄迈县|2845,儋州市|2839,定安县|2843,东方市|2842,乐东县|2849,临高县|2846,陵水县|2850,琼海市|2838,琼中县|2852,屯昌县|2844,万宁市|2841,文昌市|2840,五指山市|2837\"\n" +
                    "    },\n" +
                    "    \"河北省\": {\n" +
                    "        \"石家庄市\": \"长安区|2474,高邑县|2490,藁城市|2498,晋州市|2499,井陉矿区|2483,井陉县|2485,灵寿县|2489,鹿泉市|2501,栾城县|2487,平山县|2494,桥东区|2475,桥东区|2476,桥东区|2477,桥西区|2479,桥西区|2480,桥西区|2478,深泽县|2491,石家庄市|2473,无极县|2493,辛集市|2497,新华区|2482,新华区|2481,新乐市|2500,行唐县|2488,裕华区|2484,元氏县|2495,赞皇县|2492,赵县|2496,正定县|2486\",\n" +
                    "        \"保定市\": \"安国市|2393,安新县|2384,保定市|2369,北市区|2371,博野县|2389,定兴县|2378,定州市|2392,阜平县|2376,高碑店市|2394,高阳县|2380,涞水县|2375,涞源县|2382,蠡县|2387,满城县|2373,南市区|2372,清苑县|2374,曲阳县|2386,容城县|2381,顺平县|2388,唐县|2379,望都县|2383,新市区|2370,雄县|2390,徐水县|2377,易县|2385,涿州市|2391\",\n" +
                    "        \"沧州市\": \"泊头市|2469,沧县|2459,沧州市|2455,东光县|2461,海兴县|2462,河间市|2472,黄骅市|2471,孟村县|2468,南皮县|2465,青县|2460,任丘市|2470,肃宁县|2464,吴桥县|2466,献县|2467,新华区|2457,新华区|2456,盐山县|2463,运河区|2458\",\n" +
                    "        \"承德市\": \"承德市|2443,承德县|2447,丰宁县|2452,宽城县|2453,隆化县|2451,滦平县|2450,平泉县|2449,双滦区|2445,双桥区|2444,围场县|2454,兴隆县|2448,鹰手营子矿区|2446\",\n" +
                    "        \"邯郸市\": \"成安县|2553,磁县|2556,丛台区|2548,大名县|2554,肥乡县|2557,峰峰矿区|2550,复兴区|2549,馆陶县|2562,广平县|2561,邯郸市|2546,邯郸县|2551,邯山区|2547,鸡泽县|2560,临漳县|2552,邱县|2559,曲周县|2564,涉县|2555,魏县|2563,武安市|2565,永年县|2558\",\n" +
                    "        \"衡水市\": \"安平县|2516,阜城县|2519,故城县|2517,衡水市|2510,冀州市|2520,景县|2518,饶阳县|2515,深州市|2521,桃城区|2511,武强县|2514,武邑县|2513,枣强县|2512\",\n" +
                    "        \"廊坊市\": \"安次区|2411,霸州市|2419,大厂县|2418,大城县|2416,固安县|2413,广阳区|2412,廊坊市|2410,三河市|2420,文安县|2417,香河县|2415,永清县|2414\",\n" +
                    "        \"秦皇岛市\": \"北戴河区|2505,昌黎县|2507,抚宁县|2508,海港区|2503,卢龙县|2509,秦皇岛市|2502,青龙县|2506,山海关区|2504\",\n" +
                    "        \"唐山市\": \"丰南区|2400,丰润区|2401,古冶区|2398,开平区|2399,乐亭县|2404,路北区|2397,路南区|2396,滦南县|2403,滦县|2402,迁安市|2409,迁西县|2405,唐海县|2407,唐山市|2395,玉田县|2406,遵化市|2408\",\n" +
                    "        \"邢台市\": \"柏乡县|2532,广宗县|2539,巨鹿县|2537,临城县|2530,临西县|2543,隆尧县|2533,内丘县|2531,南宫市|2544,南和县|2535,宁晋县|2536,平乡县|2540,桥东区|2525,桥东区|2524,桥东区|2523,桥西区|2528,桥西区|2527,桥西区|2526,清河县|2542,任县|2534,沙河市|2545,威县|2541,新河县|2538,邢台市|2522,邢台县|2529\",\n" +
                    "        \"张家口市\": \"赤城县|2441,崇礼县|2442,沽源县|2433,怀安县|2437,怀来县|2439,康保县|2432,桥东区|2422,桥东区|2423,桥东区|2424,桥西区|2427,桥西区|2426,桥西区|2425,尚义县|2434,万全县|2438,蔚县|2435,下花园区|2429,宣化区|2428,宣化县|2430,阳原县|2436,张北县|2431,张家口市|2421,涿鹿县|2440\"\n" +
                    "    },\n" +
                    "    \"河南省\": {\n" +
                    "        \"郑州市\": \"登封市|2719,二七区|2709,巩义市|2715,管城区|2710,惠济区|2713,金水区|2711,上街区|2712,新密市|2717,新郑市|2718,荥阳市|2716,郑州市|2707,中牟县|2714,中原区|2708\",\n" +
                    "        \"安阳市\": \"安阳市|2618,安阳县|2623,北关区|2620,滑县|2625,林州市|2627,龙安区|2622,内黄县|2626,汤阴县|2624,文峰区|2619,殷都区|2621\",\n" +
                    "        \"鹤壁市\": \"鹤壁市|2731,鹤山区|2732,浚县|2735,淇滨区|2734,淇县|2736,山城区|2733\",\n" +
                    "        \"焦作市\": \"博爱县|2694,济源市|2697,焦作市|2688,解放区|2689,马村区|2691,孟州市|2699,沁阳市|2698,山阳区|2692,温县|2696,武陟县|2695,修武县|2693,中站区|2690\",\n" +
                    "        \"开封市\": \"鼓楼区|2642,开封市|2639,开封县|2646,兰考县|2647,龙亭区|2640,杞县|2643,顺河区|2641,通许县|2644,尉氏县|2645\",\n" +
                    "        \"洛阳市\": \"吉利区|2665,涧西区|2664,老城区|2662,栾川县|2669,洛龙区|2666,洛宁县|2673,洛阳市|2661,孟津县|2667,汝阳县|2671,嵩县|2670,西工区|2663,新安县|2668,偃师市|2675,伊川县|2674,宜阳县|2672\",\n" +
                    "        \"漯河市\": \"临颍县|2680,漯河市|2676,舞阳县|2679,郾城区|2677,召陵区|2678\",\n" +
                    "        \"南阳市\": \"邓州市|2597,方城县|2588,内乡县|2591,南阳市|2584,南召县|2587,社旗县|2593,唐河县|2594,桐柏县|2596,宛城区|2585,卧龙区|2586,西峡县|2589,淅川县|2592,新野县|2595,镇平县|2590\",\n" +
                    "        \"平顶山市\": \"宝丰县|2633,郏县|2636,鲁山县|2635,平顶山市|2628,汝州市|2638,石龙区|2631,卫东区|2630,舞钢市|2637,新华区|2629,叶县|2634,湛河区|2632\",\n" +
                    "        \"濮阳市\": \"范县|2685,华龙区|2682,南乐县|2684,濮阳市|2681,濮阳县|2687,清丰县|2683,台前县|2686\",\n" +
                    "        \"三门峡市\": \"湖滨区|2567,灵宝市|2572,卢氏县|2570,三门峡市|2566,陕县|2569,渑池县|2568,义马市|2571\",\n" +
                    "        \"商丘市\": \"梁园区|2609,民权县|2611,宁陵县|2613,商丘市|2608,睢县|2612,睢阳区|2610,夏邑县|2616,永城市|2617,虞城县|2615,柘城县|2614\",\n" +
                    "        \"新乡市\": \"长垣县|2658,封丘县|2657,凤泉区|2651,红旗区|2649,辉县市|2660,获嘉县|2654,牧野区|2652,卫滨区|2650,卫辉市|2659,新乡市|2648,新乡县|2653,延津县|2656,原阳县|2655\",\n" +
                    "        \"信阳市\": \"固始县|2580,光山县|2577,淮滨县|2582,潢川县|2581,罗山县|2576,平桥区|2575,商城县|2579,浉河区|2574,息县|2583,新县|2578,信阳市|2573\",\n" +
                    "        \"许昌市\": \"长葛市|2706,魏都区|2701,襄城县|2704,许昌市|2700,许昌县|2702,鄢陵县|2703,禹州市|2705\",\n" +
                    "        \"周口市\": \"郸城县|2603,扶沟县|2599,淮阳县|2604,鹿邑县|2606,商水县|2601,沈丘县|2602,太康县|2605,西华县|2600,项城市|2607,周口市|2598\",\n" +
                    "        \"驻马店市\": \"泌阳县|2727,平舆县|2724,确山县|2726,汝南县|2728,上蔡县|2723,遂平县|2729,西平县|2722,新蔡县|2730,驿城区|2721,正阳县|2725,驻马店市|2720\"\n" +
                    "    },\n" +
                    "    \"黑龙江省\": {\n" +
                    "        \"齐齐哈尔市\": \"甘南县|3921,昂昂溪区|3916,拜泉县|3925,富拉尔基区|3917,富裕县|3922,建华区|3914,克东县|3924,克山县|3923,龙江县|3918,龙沙区|3913,讷河市|3926,齐齐哈尔市|3912,泰来县|3920,铁锋区|3915,依安县|3919\",\n" +
                    "        \"大庆市\": \"大庆市|3854,大同区|3859,杜尔伯特县|3863,红岗区|3858,林甸县|3862,龙凤区|3856,让胡路区|3857,萨尔图区|3855,肇源县|3861,肇州县|3860\",\n" +
                    "        \"大兴安岭地区\": \"大兴安岭地区|3850,呼玛县|3851,漠河县|3853,塔河县|3852\",\n" +
                    "        \"哈尔滨市\": \"巴彦县|3843,宾县|3842,道里区|3833,道外区|3835,方正县|3841,哈尔滨市|3832,呼兰区|3839,木兰县|3844,南岗区|3834,平房区|3837,尚志市|3848,双城市|3847,松北区|3838,通河县|3845,五常市|3849,香坊区|3836,延寿县|3846,依兰县|3840\",\n" +
                    "        \"鹤岗市\": \"东山区|3902,工农区|3899,鹤岗市|3896,萝北县|3904,南山区|3900,绥滨县|3905,向阳区|3898,向阳区|3897,兴安区|3901,兴山区|3903\",\n" +
                    "        \"黑河市\": \"爱辉区|3907,北安市|3910,黑河市|3906,孙吴县|3909,五大连池市|3911,逊克县|3908\",\n" +
                    "        \"鸡西市\": \"城子河区|3891,滴道区|3889,恒山区|3888,虎林市|3894,鸡东县|3893,鸡冠区|3887,鸡西市|3886,梨树区|3890,麻山区|3892,密山市|3895\",\n" +
                    "        \"佳木斯市\": \"东风区|3815,抚远县|3820,富锦市|3822,桦川县|3818,桦南县|3817,佳木斯市|3811,郊区|3816,前进区|3814,汤原县|3819,同江市|3821,向阳区|3813,向阳区|3812\",\n" +
                    "        \"牡丹江市\": \"爱民区|3867,东安区|3865,东宁县|3869,海林市|3872,林口县|3870,牡丹江市|3864,穆棱市|3874,宁安市|3873,绥芬河市|3871,西安区|3868,阳明区|3866\",\n" +
                    "        \"七台河市\": \"勃利县|3793,七台河市|3789,茄子河区|3792,桃山区|3791,新兴区|3790\",\n" +
                    "        \"双鸭山市\": \"宝清县|3830,宝山区|3827,集贤县|3828,尖山区|3824,岭东区|3825,饶河县|3831,双鸭山市|3823,四方台区|3826,友谊县|3829\",\n" +
                    "        \"绥化市\": \"安达市|3883,北林区|3876,海伦市|3885,兰西县|3878,明水县|3881,青冈县|3879,庆安县|3880,绥化市|3875,绥棱县|3882,望奎县|3877,肇东市|3884\",\n" +
                    "        \"伊春市\": \"翠峦区|3798,带岭区|3805,红星区|3807,嘉荫县|3809,金山屯区|3801,美溪区|3800,南岔区|3795,上甘岭区|3808,汤旺河区|3804,铁力市|3810,乌马河区|3803,乌伊岭区|3806,五营区|3802,西林区|3797,新青区|3799,伊春市|3794,友好区|3796\"\n" +
                    "    },\n" +
                    "    \"湖北省\": {\n" +
                    "        \"武汉市\": \"蔡甸区|2919,东西湖区|2917,汉南区|2918,汉阳区|2913,洪山区|2916,黄陂区|2921,江岸区|2910,江汉区|2911,江夏区|2920,硚口区|2912,青山区|2915,武昌区|2914,武汉市|2909,新洲区|2922\",\n" +
                    "        \"鄂州市\": \"鄂城区|2951,鄂州市|2948,华容区|2950,梁子湖区|2949\",\n" +
                    "        \"恩施州\": \"巴东县|2904,恩施市|2901,恩施州|2900,鹤峰县|2908,建始县|2903,来凤县|2907,利川市|2902,咸丰县|2906,宣恩县|2905\",\n" +
                    "        \"黄冈市\": \"红安县|2958,黄冈市|2955,黄梅县|2963,黄州区|2956,罗田县|2959,麻城市|2964,蕲春县|2962,团风县|2957,武穴市|2965,浠水县|2961,英山县|2960\",\n" +
                    "        \"黄石市\": \"大冶市|2972,黄石港区|2967,黄石市|2966,铁山区|2970,西塞山区|2968,下陆区|2969,阳新县|2971\",\n" +
                    "        \"荆门市\": \"东宝区|2933,掇刀区|2934,京山县|2935,荆门市|2932,沙洋县|2936,钟祥市|2937\",\n" +
                    "        \"荆州市\": \"公安县|2926,洪湖市|2930,监利县|2927,江陵县|2928,荆州区|2925,荆州市|2923,沙市区|2924,石首市|2929,松滋市|2931\",\n" +
                    "        \"十堰市\": \"丹江口市|2870,房县|2869,茅箭区|2863,十堰市|2862,郧西县|2866,郧县|2865,张湾区|2864,竹山县|2867,竹溪县|2868\",\n" +
                    "        \"随州市\": \"广水市|2954,随州市|2952,曾都区|2953\",\n" +
                    "        \"仙桃市\": \"潜江市|2859,神农架林区|2861,天门市|2860,仙桃市|2858\",\n" +
                    "        \"咸宁市\": \"赤壁市|2877,崇阳县|2875,嘉鱼县|2873,通城县|2874,通山县|2876,咸安区|2872,咸宁市|2871\",\n" +
                    "        \"襄樊市\": \"保康县|2944,樊城区|2940,谷城县|2943,老河口市|2945,南漳县|2942,襄城区|2939,襄樊市|2938,襄阳区|2941,宜城市|2947,枣阳市|2946\",\n" +
                    "        \"孝感市\": \"安陆市|2884,大悟县|2881,汉川市|2885,孝昌县|2880,孝感市|2878,孝南区|2879,应城市|2883,云梦县|2882\",\n" +
                    "        \"宜昌市\": \"长阳县|2895,当阳市|2898,点军区|2889,五峰县|2896,伍家岗区|2888,西陵区|2887,猇亭区|2890,兴山县|2893,夷陵区|2891,宜昌市|2886,宜都市|2897,远安县|2892,枝江市|2899,秭归县|2894\"\n" +
                    "    }";

    String names = "\"湖南省\": {\n" +
            "        \"长沙市\": \"长沙市|3098,长沙县|3104,芙蓉区|3099,开福区|3102,浏阳市|3107,宁乡县|3106,天心区|3100,望城县|3105,雨花区|3103,岳麓区|3101\",\n" +
            "        \"常德市\": \"安乡县|2992,常德市|2989,鼎城区|2991,汉寿县|2993,津市市|2998,澧县|2994,临澧县|2995,石门县|2997,桃源县|2996,武陵区|2990\",\n" +
            "        \"郴州市\": \"安仁县|3096,北湖区|3087,郴州市|3086,桂东县|3095,桂阳县|3089,嘉禾县|3092,临武县|3093,汝城县|3094,苏仙区|3088,宜章县|3090,永兴县|3091,资兴市|3097\",\n" +
            "        \"衡阳市\": \"常宁市|3072,衡东县|3069,衡南县|3067,衡山县|3068,衡阳市|3060,衡阳县|3066,耒阳市|3071,南岳区|3065,祁东县|3070,石鼓区|3063,雁峰区|3062,蒸湘区|3064,珠晖区|3061\",\n" +
            "        \"怀化市\": \"辰溪县|3008,鹤城区|3005,洪江市|3016,怀化市|3004,会同县|3010,靖州县|3014,麻阳县|3011,通道县|3015,新晃县|3012,溆浦县|3009,沅陵县|3007,芷江县|3013,中方县|3006\",\n" +
            "        \"娄底市\": \"冷水江市|2977,涟源市|2978,娄底市|2973,娄星区|2974,双峰县|2975,新化县|2976\",\n" +
            "        \"邵阳市\": \"北塔区|3076,城步县|3084,大祥区|3075,洞口县|3081,隆回县|3080,邵东县|3077,邵阳市|3073,邵阳县|3079,双清区|3074,绥宁县|3082,武冈市|3085,新宁县|3083,新邵县|3078\",\n" +
            "        \"湘潭市\": \"韶山市|3043,湘潭市|3038,湘潭县|3041,湘乡市|3042,雨湖区|3039,岳塘区|3040\",\n" +
            "        \"湘西州\": \"保靖县|3049,凤凰县|3047,古丈县|3050,花垣县|3048,吉首市|3045,龙山县|3052,泸溪县|3046,湘西州|3044,永顺县|3051\",\n" +
            "        \"益阳市\": \"安化县|3058,赫山区|3055,南县|3056,桃江县|3057,益阳市|3053,沅江市|3059,资阳区|3054\",\n" +
            "        \"永州市\": \"道县|3032,东安县|3030,江华县|3037,江永县|3033,蓝山县|3035,冷水滩区|3028,宁远县|3034,祁阳县|3029,双牌县|3031,新田县|3036,永州市|3027\",\n" +
            "        \"岳阳市\": \"华容县|2984,君山区|2982,临湘市|2988,汨罗市|2987,平江县|2986,湘阴县|2985,岳阳楼区|2980,岳阳市|2979,岳阳县|2983,云溪区|2981\",\n" +
            "        \"张家界市\": \"慈利县|3002,桑植县|3003,武陵源区|3001,永定区|3000,张家界市|2999\",\n" +
            "        \"株洲市\": \"茶陵县|3024,荷塘区|3018,醴陵市|3026,芦淞区|3019,石峰区|3020,天元区|3021,炎陵县|3025,攸县|3023,株洲市|3017,株洲县|3022\"\n" +
            "    },\n" +
            "    \"吉林省\": {\n" +
            "        \"长春市\": \"长春市|1112,朝阳区|1115,德惠市|1122,二道区|1116,九台市|1120,宽城区|1114,绿园区|1117,南关区|1113,农安县|1119,双阳区|1118,榆树市|1121\",\n" +
            "        \"白城市\": \"白城市|1087,大安市|1092,洮北区|1088,洮南市|1091,通榆县|1090,镇赉县|1089\",\n" +
            "        \"白山市\": \"八道江区|1094,白山市|1093,长白县|1097,抚松县|1095,靖宇县|1096,临江市|1098\",\n" +
            "        \"吉林市\": \"昌邑区|1057,船营区|1059,丰满区|1060,桦甸市|1063,吉林市|1056,蛟河市|1062,龙潭区|1058,磐石市|1065,舒兰市|1064,永吉县|1061\",\n" +
            "        \"辽源市\": \"东丰县|1102,东辽县|1103,辽源市|1099,龙山区|1100,西安区|1101\",\n" +
            "        \"四平市\": \"公主岭市|1071,梨树县|1069,双辽市|1072,四平市|1066,铁东区|1068,铁西区|1067,伊通县|1070\",\n" +
            "        \"松原市\": \"长岭县|1084,扶余县|1086,宁江区|1083,乾安县|1085,松原市|1082\",\n" +
            "        \"通化市\": \"东昌区|1105,二道江区|1106,辉南县|1108,集安市|1111,柳河县|1109,梅河口市|1110,通化市|1104,通化县|1107\",\n" +
            "        \"延边州\": \"安图县|1081,敦化市|1076,和龙市|1079,珲春市|1077,龙井市|1078,图们市|1075,汪清县|1080,延边州|1073,延吉市|1074\"\n" +
            "    },\n" +
            "    \"江苏省\": {\n" +
            "        \"南京市\": \"白下区|2142,高淳县|2154,鼓楼区|2145,鼓楼区|2146,建邺区|2144,江宁区|2151,溧水县|2153,六合区|2152,南京市|2140,浦口区|2148,栖霞区|2149,秦淮区|2143,下关区|2147,玄武区|2141,雨花台区|2150\",\n" +
            "        \"常州市\": \"常州市|2170,金坛市|2177,溧阳市|2176,戚墅堰区|2173,天宁区|2171,武进区|2175,新北区|2174,钟楼区|2172\",\n" +
            "        \"淮安市\": \"楚州区|2215,洪泽县|2219,淮安市|2213,淮阴区|2216,金湖县|2221,涟水县|2218,清河区|2214,清浦区|2217,盱眙县|2220\",\n" +
            "        \"连云港市\": \"东海县|2249,赣榆县|2248,灌南县|2251,灌云县|2250,海州区|2247,连云港市|2244,连云区|2245,新浦区|2246\",\n" +
            "        \"南通市\": \"崇川区|2156,港闸区|2157,海安县|2158,海门市|2163,南通市|2155,启东市|2160,如东县|2159,如皋市|2161,通州市|2162\",\n" +
            "        \"苏州市\": \"沧浪区|2233,常熟市|2239,虎丘区|2236,金阊区|2235,昆山市|2241,平江区|2234,苏州市|2232,太仓市|2243,吴江市|2242,吴中区|2237,相城区|2238,张家港市|2240\",\n" +
            "        \"宿迁市\": \"沭阳县|2167,泗洪县|2169,泗阳县|2168,宿城区|2165,宿迁市|2164,宿豫区|2166\",\n" +
            "        \"泰州市\": \"姜堰市|2212,靖江市|2210,泰兴市|2211,泰州市|2208,兴化市|2209\",\n" +
            "        \"无锡市\": \"北塘区|2202,滨湖区|2205,崇安区|2200,惠山区|2204,江阴市|2206,南长区|2201,无锡市|2199,锡山区|2203,宜兴市|2207\",\n" +
            "        \"徐州市\": \"丰县|2185,鼓楼区|2179,鼓楼区|2180,贾汪区|2183,九里区|2182,沛县|2186,邳州市|2190,泉山区|2184,睢宁县|2188,铜山县|2187,新沂市|2189,徐州市|2178,云龙区|2181\",\n" +
            "        \"盐城市\": \"滨海县|2226,大丰市|2231,东台市|2230,阜宁县|2227,建湖县|2229,射阳县|2228,亭湖区|2223,响水县|2225,盐城市|2222,盐都区|2224\",\n" +
            "        \"扬州市\": \"宝应县|2195,高邮市|2197,广陵区|2192,邗江区|2193,江都市|2198,维扬区|2194,扬州市|2191,仪征市|2196\",\n" +
            "        \"镇江市\": \"丹徒区|2255,丹阳市|2256,京口区|2253,句容市|2258,润州区|2254,扬中市|2257,镇江市|2252\"\n" +
            "    },\n" +
            "    \"江西省\": {\n" +
            "        \"南昌市\": \"安义县|2293,东湖区|2286,进贤县|2294,南昌市|2285,南昌县|2291,青山湖区|2290,青云谱区|2288,湾里区|2289,西湖区|2287,新建县|2292\",\n" +
            "        \"抚州市\": \"崇仁县|2325,东乡县|2330,抚州市|2320,广昌县|2331,金溪县|2328,乐安县|2326,黎川县|2323,临川区|2321,南城县|2322,南丰县|2324,宜黄县|2327,资溪县|2329\",\n" +
            "        \"赣州市\": \"安远县|2353,崇义县|2352,大余县|2350,定南县|2355,赣县|2348,赣州市|2346,会昌县|2360,龙南县|2354,南康市|2364,宁都县|2357,全南县|2356,瑞金市|2363,上犹县|2351,石城县|2362,信丰县|2349,兴国县|2359,寻乌县|2361,于都县|2358,章贡区|2347\",\n" +
            "        \"吉安市\": \"安福县|2306,吉安市|2295,吉安县|2298,吉水县|2299,吉州区|2296,井冈山市|2308,青原区|2297,遂川县|2304,泰和县|2303,万安县|2305,峡江县|2300,新干县|2301,永丰县|2302,永新县|2307\",\n" +
            "        \"景德镇市\": \"昌江区|2336,浮梁县|2338,景德镇市|2335,乐平市|2339,珠山区|2337\",\n" +
            "        \"九江市\": \"德安县|2279,都昌县|2281,湖口县|2282,九江市|2272,九江县|2275,庐山区|2273,彭泽县|2283,瑞昌市|2284,武宁县|2276,星子县|2280,修水县|2277,浔阳区|2274,永修县|2278\",\n" +
            "        \"萍乡市\": \"安源区|2341,莲花县|2343,芦溪县|2345,萍乡市|2340,上栗县|2344,湘东区|2342\",\n" +
            "        \"上饶市\": \"德兴市|2271,广丰县|2262,横峰县|2265,鄱阳县|2268,铅山县|2264,上饶市|2259,上饶县|2261,万年县|2269,婺源县|2270,信州区|2260,弋阳县|2266,余干县|2267,玉山县|2263\",\n" +
            "        \"新余市\": \"分宜县|2334,新余市|2332,渝水区|2333\",\n" +
            "        \"宜春市\": \"丰城市|2317,奉新县|2311,高安市|2319,靖安县|2315,上高县|2313,铜鼓县|2316,万载县|2312,宜春市|2309,宜丰县|2314,袁州区|2310,樟树市|2318\",\n" +
            "        \"鹰潭市\": \"贵溪市|2368,鹰潭市|2365,余江县|2367,月湖区|2366\"\n" +
            "    },\n" +
            "    \"辽宁省\": {\n" +
            "        \"沈阳市\": \"大东区|3514,东陵区|3519,法库县|3524,和平区|3512,皇姑区|3515,康平县|3523,辽中县|3522,沈河区|3513,沈阳市|3511,苏家屯区|3518,铁西区|3517,铁西区|3516,新城子区|3520,新民市|3525,于洪区|3521\",\n" +
            "        \"鞍山市\": \"鞍山市|3575,海城市|3583,立山区|3579,千山区|3580,台安县|3581,铁东区|3576,铁西区|3577,铁西区|3578,岫岩县|3582\",\n" +
            "        \"本溪市\": \"本溪市|3504,本溪县|3509,桓仁县|3510,明山区|3507,南芬区|3508,平山区|3505,溪湖区|3506\",\n" +
            "        \"朝阳市\": \"北票市|3502,朝阳市|3497,朝阳县|3500,建平县|3501,凌源市|3503,龙城区|3499,双塔区|3498\",\n" +
            "        \"大连市\": \"长海县|3485,大连市|3478,甘井子区|3482,金州区|3484,旅顺口区|3483,普兰店市|3487,沙河口区|3481,瓦房店市|3486,西岗区|3480,中山区|3479,庄河市|3488\",\n" +
            "        \"丹东市\": \"丹东市|3471,东港市|3476,凤城市|3477,宽甸县|3475,元宝区|3472,振安区|3474,振兴区|3473\",\n" +
            "        \"抚顺市\": \"东洲区|3491,抚顺市|3489,抚顺县|3494,清原县|3496,顺城区|3493,望花区|3492,新宾县|3495,新抚区|3490\",\n" +
            "        \"阜新市\": \"阜新市|3568,阜新县|3573,海州区|3569,清河门区|3571,太平区|3570,细河区|3572,彰武县|3574\",\n" +
            "        \"葫芦岛市\": \"葫芦岛市|3538,建昌县|3543,连山区|3539,龙港区|3540,南票区|3541,绥中县|3542,兴城市|3544\",\n" +
            "        \"锦州市\": \"古塔区|3562,黑山县|3565,锦州市|3561,凌海市|3567,凌河区|3563,太和区|3564,义县|3566\",\n" +
            "        \"辽阳市\": \"白塔区|3546,灯塔市|3552,弓长岭区|3549,宏伟区|3548,辽阳市|3545,辽阳县|3551,太子河区|3550,文圣区|3547\",\n" +
            "        \"盘锦市\": \"大洼县|3529,盘锦市|3526,盘山县|3530,双台子区|3527,兴隆台区|3528\",\n" +
            "        \"铁岭市\": \"昌图县|3558,调兵山市|3559,开原市|3560,清河区|3555,铁岭市|3553,铁岭县|3556,西丰县|3557,银州区|3554\",\n" +
            "        \"营口市\": \"鲅鱼圈区|3534,大石桥市|3537,盖州市|3536,老边区|3535,西市区|3533,营口市|3531,站前区|3532\"\n" +
            "    },\n" +
            "    \"内蒙古自治区\": {\n" +
            "        \"阿拉善盟\": \"阿拉善盟|1011,阿拉善右旗|1013,阿拉善左旗|1012,额济纳旗|1014\",\n" +
            "        \"巴彦淖尔市\": \"巴彦淖尔市|959,磴口县|962,杭锦后旗|966,临河区|960,乌拉特后旗|965,乌拉特前旗|963,乌拉特中旗|964,五原县|961\",\n" +
            "        \"包头市\": \"包头市|927,达尔罕茂明安联合旗|935,东河区|928,固阳县|934,九原区|932,昆都仑区|929,青山区|930,石拐区|931,土默特右旗|933\",\n" +
            "        \"赤峰市\": \"阿鲁科尔沁旗|971,敖汉旗|979,巴林右旗|973,巴林左旗|972,赤峰市|967,红山区|968,喀喇沁旗|977,克什克腾旗|975,林西县|974,宁城县|978,松山区|970,翁牛特旗|976,元宝山区|969\",\n" +
            "        \"鄂尔多斯市\": \"达拉特旗|991,东胜区|990,鄂尔多斯市|989,鄂托克旗|994,鄂托克前旗|993,杭锦旗|995,乌审旗|996,伊金霍洛旗|997,准格尔旗|992\",\n" +
            "        \"呼和浩特市\": \"和林格尔县|956,呼和浩特市|949,回民区|951,清水河县|957,赛罕区|953,土默特左旗|954,托克托县|955,武川县|958,新城区|950,玉泉区|952\",\n" +
            "        \"呼伦贝尔市\": \"阿荣旗|938,陈巴尔虎旗|941,额尔古纳市|947,鄂伦春自治旗|939,鄂温克族自治旗|940,根河市|948,海拉尔区|937,呼伦贝尔市|936,满洲里市|944,新巴尔虎右旗|943,新巴尔虎左旗|942,牙克石市|945,扎兰屯市|946\",\n" +
            "        \"通辽市\": \"霍林郭勒市|988,开鲁县|984,科尔沁区|981,科尔沁左翼后旗|983,科尔沁左翼中旗|982,库伦旗|985,奈曼旗|986,通辽市|980,扎鲁特旗|987\",\n" +
            "        \"乌海市\": \"海勃湾区|917,海南区|918,乌达区|919,乌海市|916\",\n" +
            "        \"乌兰察布市\": \"察哈尔右翼后旗|913,察哈尔右翼前旗|911,察哈尔右翼中旗|912,丰镇市|915,化德县|907,集宁区|905,凉城县|910,商都县|908,四子王旗|914,乌兰察布市|904,兴和县|909,卓资县|906\",\n" +
            "        \"锡林郭勒盟\": \"阿巴嘎旗|1001,东乌珠穆沁旗|1004,多伦县|1010,二连浩特市|999,苏尼特右旗|1003,苏尼特左旗|1002,太仆寺旗|1006,西乌珠穆沁旗|1005,锡林郭勒盟|998,锡林浩特市|1000,镶黄旗|1007,正蓝旗|1009,正镶白旗|1008\",\n" +
            "        \"兴安盟\": \"阿尔山市|922,科尔沁右翼前旗|923,科尔沁右翼中旗|924,突泉县|926,乌兰浩特市|921,兴安盟|920,扎赉特旗|925\"\n" +
            "    },\n" +
            "    \"宁夏区\": {\n" +
            "        \"银川市\": \"贺兰县|1371,金凤区|1369,灵武市|1372,西夏区|1368,兴庆区|1367,银川市|1366,永宁县|1370\",\n" +
            "        \"固原市\": \"固原市|1356,泾源县|1360,隆德县|1359,彭阳县|1361,西吉县|1358,原州区|1357\",\n" +
            "        \"石嘴山市\": \"大武口区|1363,惠农区|1364,平罗县|1365,石嘴山市|1362\",\n" +
            "        \"吴忠市\": \"利通区|1352,青铜峡市|1355,同心县|1354,吴忠市|1351,盐池县|1353\",\n" +
            "        \"中卫市\": \"海原县|1350,沙坡头区|1348,中宁县|1349,中卫市|1347\"\n" +
            "    },\n" +
            "    \"青海省\": {\n" +
            "        \"西宁市\": \"城北区|3779,城东区|3776,城西区|3778,城中区|3777,大通县|3780,湟源县|3782,湟中县|3781,西宁市|3775\",\n" +
            "        \"果洛州\": \"班玛县|3739,达日县|3741,甘德县|3740,果洛州|3737,久治县|3742,玛多县|3743,玛沁县|3738\",\n" +
            "        \"海北州\": \"刚察县|3755,海北州|3751,海晏县|3754,门源县|3752,祁连县|3753\",\n" +
            "        \"海东地区\": \"海东地区|3744,互助县|3748,化隆县|3749,乐都县|3747,民和县|3746,平安县|3745,循化县|3750\",\n" +
            "        \"海南州\": \"共和县|3757,贵德县|3759,贵南县|3761,海南州|3756,同德县|3758,兴海县|3760\",\n" +
            "        \"海西州\": \"德令哈市|3764,都兰县|3766,格尔木市|3763,海西州|3762,天峻县|3767,乌兰县|3765\",\n" +
            "        \"黄南州\": \"河南县|3787,黄南州|3783,尖扎县|3785,同仁县|3784,泽库县|3786\",\n" +
            "        \"玉树州\": \"称多县|3771,囊谦县|3773,曲麻莱县|3774,玉树县|3769,玉树州|3768,杂多县|3770,治多县|3772\"\n" +
            "    },\n" +
            "    \"山东省\": {\n" +
            "        \"济南市\": \"长清区|1557,槐荫区|1554,济南市|1549,济阳县|1559,历城区|1556,历下区|1550,平阴县|1558,商河县|1560,市中区|1552,市中区|1553,市中区|1551,天桥区|1555,章丘市|1561\",\n" +
            "        \"滨州市\": \"滨城区|1586,滨州市|1585,博兴县|1591,惠民县|1587,无棣县|1589,阳信县|1588,沾化县|1590,邹平县|1592\",\n" +
            "        \"德州市\": \"德城区|1517,德州市|1516,乐陵市|1526,临邑县|1521,陵县|1518,宁津县|1519,平原县|1523,齐河县|1522,庆云县|1520,武城县|1525,夏津县|1524,禹城市|1527\",\n" +
            "        \"东营市\": \"东营区|1493,东营市|1492,广饶县|1497,河口区|1494,垦利县|1495,利津县|1496\",\n" +
            "        \"菏泽市\": \"牡丹区|1619,曹县|1620,成武县|1622,单县|1621,定陶县|1626,东明县|1627,巨野县|1623,鄄城县|1625,郓城县|1624\",\n" +
            "        \"济宁市\": \"济宁市|1562,嘉祥县|1570,金乡县|1569,梁山县|1573,曲阜市|1574,任城区|1566,市中区|1565,市中区|1564,市中区|1563,泗水县|1572,微山县|1567,汶上县|1571,兖州市|1575,鱼台县|1568,邹城市|1576\",\n" +
            "        \"莱芜市\": \"钢城区|1639,莱城区|1638,莱芜市|1637\",\n" +
            "        \"聊城市\": \"茌平县|1632,东阿县|1633,东昌府区|1629,高唐县|1635,冠县|1634,聊城市|1628,临清市|1636,莘县|1631,阳谷县|1630\",\n" +
            "        \"临沂市\": \"苍山县|1505,费县|1506,河东区|1501,莒南县|1508,兰山区|1499,临沭县|1510,临沂市|1498,罗庄区|1500,蒙阴县|1509,平邑县|1507,郯城县|1503,沂南县|1502,沂水县|1504\",\n" +
            "        \"青岛市\": \"城阳区|1647,黄岛区|1644,即墨市|1649,胶南市|1651,胶州市|1648,莱西市|1652,崂山区|1645,李沧区|1646,平度市|1650,青岛市|1640,市北区|1642,市南区|1641,四方区|1643\",\n" +
            "        \"日照市\": \"东港区|1529,莒县|1532,岚山区|1530,日照市|1528,五莲县|1531\",\n" +
            "        \"泰安市\": \"岱岳区|1544,东平县|1546,肥城市|1548,宁阳县|1545,泰安市|1542,泰山区|1543,新泰市|1547\",\n" +
            "        \"威海市\": \"环翠区|1512,荣成市|1514,乳山市|1515,威海市|1511,文登市|1513\",\n" +
            "        \"潍坊市\": \"潍坊市|1593,安丘市|1603,昌乐县|1599,昌邑市|1605,坊子区|1596,高密市|1604,寒亭区|1595,奎文区|1597,临朐县|1598,青州市|1600,寿光市|1602,潍城区|1594,诸城市|1601\",\n" +
            "        \"烟台市\": \"长岛县|1611,福山区|1608,海阳市|1618,莱山区|1610,莱阳市|1613,莱州市|1614,龙口市|1612,牟平区|1609,蓬莱市|1615,栖霞市|1617,烟台市|1606,招远市|1616,芝罘区|1607\",\n" +
            "        \"枣庄市\": \"山亭区|1540,市中区|1534,市中区|1535,市中区|1536,台儿庄区|1539,滕州市|1541,薛城区|1537,峄城区|1538,枣庄市|1533\",\n" +
            "        \"淄博市\": \"博山区|1579,高青县|1583,桓台县|1582,临淄区|1580,沂源县|1584,张店区|1578,周村区|1581,淄博市|1577\"\n" +
            "    },\n" +
            "    \"山西省\": {\n" +
            "        \"太原市\": \"古交市|1711,尖草坪区|1705,晋源区|1707,娄烦县|1710,清徐县|1708,太原市|1701,万柏林区|1706,小店区|1702,杏花岭区|1704,阳曲县|1709,迎泽区|1703\",\n" +
            "        \"长治市\": \"长治市|1767,长治县|1774,长子县|1780,城区|1768,城区|1769,城区|1770,城区|1771,壶关县|1779,郊区|1773,郊区|1772,黎城县|1778,潞城市|1784,平顺县|1777,沁县|1782,沁源县|1783,屯留县|1776,武乡县|1781,襄垣县|1775\",\n" +
            "        \"大同市\": \"城区|1686,城区|1687,城区|1688,城区|1689,大同市|1685,大同县|1700,广灵县|1696,浑源县|1698,矿区|1691,矿区|1690,灵丘县|1697,南郊区|1692,天镇县|1695,新荣区|1693,阳高县|1694,左云县|1699\",\n" +
            "        \"晋城市\": \"城区|1739,城区|1740,城区|1741,城区|1742,高平市|1747,晋城市|1738,陵川县|1745,沁水县|1743,阳城县|1744,泽州县|1746\",\n" +
            "        \"晋中市\": \"和顺县|1730,介休市|1737,晋中市|1726,灵石县|1736,平遥县|1735,祁县|1734,寿阳县|1732,太谷县|1733,昔阳县|1731,榆次区|1727,榆社县|1728,左权县|1729\",\n" +
            "        \"临汾市\": \"安泽县|1660,大宁县|1664,汾西县|1668,浮山县|1661,古县|1659,洪洞县|1658,侯马市|1669,霍州市|1670,吉县|1662,临汾市|1653,蒲县|1667,曲沃县|1655,隰县|1665,乡宁县|1663,襄汾县|1657,尧都区|1654,翼城县|1656,永和县|1666\",\n" +
            "        \"吕梁市\": \"方山县|1680,汾阳市|1684,交城县|1674,交口县|1682,岚县|1679,离石区|1672,临县|1676,柳林县|1677,吕梁市|1671,石楼县|1678,文水县|1673,孝义市|1683,兴县|1675,中阳县|1681\",\n" +
            "        \"朔州市\": \"怀仁县|1753,山阴县|1750,朔城区|1749,朔州市|1748,应县|1751,右玉县|1752\",\n" +
            "        \"忻州市\": \"代县|1716,定襄县|1714,繁峙县|1717,河曲县|1723,静乐县|1719,岢岚县|1722,宁武县|1718,偏关县|1724,神池县|1720,五台县|1715,五寨县|1721,忻府区|1713,忻州市|1712,原平市|1725\",\n" +
            "        \"阳泉市\": \"城区|1786,城区|1787,城区|1788,城区|1789,郊区|1792,郊区|1793,矿区|1790,矿区|1791,平定县|1794,阳泉市|1785,盂县|1795\",\n" +
            "        \"运城市\": \"河津市|1766,稷山县|1759,绛县|1761,临猗县|1756,平陆县|1764,芮城县|1765,万荣县|1757,闻喜县|1758,夏县|1763,新绛县|1760,盐湖区|1755,垣曲县|1762,运城市|1754\"\n" +
            "    },\n" +
            "    \"陕西省\": {\n" +
            "        \"西安市\": \"灞桥区|3722,碑林区|3720,长安区|3727,高陵县|3731,户县|3730,蓝田县|3728,莲湖区|3721,临潼区|3726,未央区|3723,西安市|3718,新城区|3719,阎良区|3725,雁塔区|3724,周至县|3729\",\n" +
            "        \"安康市\": \"安康市|3644,白河县|3654,汉滨区|3645,汉阴县|3646,岚皋县|3650,宁陕县|3648,平利县|3651,石泉县|3647,旬阳县|3653,镇坪县|3652,紫阳县|3649\",\n" +
            "        \"宝鸡市\": \"宝鸡市|3655,陈仓区|3658,凤县|3666,凤翔县|3659,扶风县|3661,金台区|3657,麟游县|3665,陇县|3663,眉县|3662,岐山县|3660,千阳县|3664,太白县|3667,渭滨区|3656\",\n" +
            "        \"汉中市\": \"城固县|3697,佛坪县|3705,汉台区|3695,汉中市|3694,留坝县|3704,略阳县|3702,勉县|3700,南郑县|3696,宁强县|3701,西乡县|3699,洋县|3698,镇巴县|3703\",\n" +
            "        \"商洛市\": \"丹凤县|3639,洛南县|3638,山阳县|3641,商洛市|3636,商南县|3640,商州区|3637,镇安县|3642,柞水县|3643\",\n" +
            "        \"铜川市\": \"铜川市|3732,王益区|3733,耀州区|3735,宜君县|3736,印台区|3734\",\n" +
            "        \"渭南市\": \"白水县|3714,澄城县|3712,大荔县|3710,富平县|3715,韩城市|3716,合阳县|3711,华县|3708,华阴市|3717,临渭区|3707,蒲城县|3713,潼关县|3709,渭南市|3706\",\n" +
            "        \"咸阳市\": \"彬县|3630,长武县|3631,淳化县|3633,泾阳县|3626,礼泉县|3628,乾县|3627,秦都区|3622,三原县|3625,渭城区|3624,武功县|3634,咸阳市|3621,兴平市|3635,旬邑县|3632,杨凌区|3623,永寿县|3629\",\n" +
            "        \"延安市\": \"安塞县|3673,宝塔区|3669,富县|3676,甘泉县|3675,黄陵县|3680,黄龙县|3679,洛川县|3677,延安市|3668,延长县|3670,延川县|3671,宜川县|3678,志丹县|3674,子长县|3672\",\n" +
            "        \"榆林市\": \"定边县|3687,府谷县|3684,横山县|3685,佳县|3690,靖边县|3686,米脂县|3689,清涧县|3692,神木县|3683,绥德县|3688,吴堡县|3691,榆林市|3681,榆阳区|3682,子洲县|3693\"\n" +
            "    },\n" +
            "    \"四川省\": {\n" +
            "        \"成都市\": \"成都市|1208,成华区|1213,崇州市|1227,大邑县|1221,都江堰市|1224,金牛区|1211,金堂县|1218,锦江区|1209,龙泉驿区|1214,彭州市|1225,郫县|1220,蒲江县|1222,青白江区|1215,青羊区|1210,邛崃市|1226,双流县|1219,温江区|1217,武侯区|1212,新都区|1216,新津县|1223\",\n" +
            "        \"阿坝州\": \"阿坝县|1315,阿坝州|1304,黑水县|1312,红原县|1317,金川县|1310,九寨沟县|1309,理县|1306,马尔康县|1313,茂县|1307,壤塘县|1314,若尔盖县|1316,松潘县|1308,汶川县|1305,小金县|1311\",\n" +
            "        \"巴中市\": \"巴中市|1182,巴州区|1183,南江县|1185,平昌县|1186,通江县|1184\",\n" +
            "        \"达州市\": \"达县|1292,达州市|1290,大竹县|1295,开江县|1294,渠县|1296,通川区|1291,万源市|1297,宣汉县|1293\",\n" +
            "        \"德阳市\": \"德阳市|1201,广汉市|1205,旌阳区|1202,罗江县|1204,绵竹市|1207,什邡市|1206,中江县|1203\",\n" +
            "        \"甘孜州\": \"巴塘县|1257,白玉县|1253,丹巴县|1245,道孚县|1248,稻城县|1259,得荣县|1260,德格县|1252,甘孜县|1250,甘孜州|1242,九龙县|1246,康定县|1243,理塘县|1256,泸定县|1244,炉霍县|1249,色达县|1255,石渠县|1254,乡城县|1258,新龙县|1251,雅江县|1247\",\n" +
            "        \"广安市\": \"广安市|1196,华蓥市|1200,邻水县|1199,武胜县|1198,岳池县|1197\",\n" +
            "        \"广元市\": \"苍溪县|1195,朝天区|1191,广元市|1187,剑阁县|1194,青川县|1193,市中区|1188,市中区|1189,旺苍县|1192,元坝区|1190\",\n" +
            "        \"乐山市\": \"峨边县|1133,峨眉山市|1135,夹江县|1131,犍为县|1129,金口河区|1128,井研县|1130,乐山市|1123,马边县|1134,沐川县|1132,沙湾区|1126,市中区|1125,市中区|1124,五通桥区|1127\",\n" +
            "        \"凉山州\": \"布拖县|1152,德昌县|1147,甘洛县|1158,会东县|1149,会理县|1148,金阳县|1153,雷波县|1160,凉山州|1143,美姑县|1159,冕宁县|1156,木里县|1145,宁南县|1150,普格县|1151,西昌市|1144,喜德县|1155,盐源县|1146,越西县|1157,昭觉县|1154\",\n" +
            "        \"泸州市\": \"古蔺县|1241,合江县|1239,江阳区|1235,龙马潭区|1237,泸县|1238,泸州市|1234,纳溪区|1236,叙永县|1240\",\n" +
            "        \"眉山市\": \"丹棱县|1266,东坡区|1262,洪雅县|1265,眉山市|1261,彭山县|1264,青神县|1267,仁寿县|1263\",\n" +
            "        \"绵阳市\": \"安县|1273,北川县|1275,涪城区|1269,江油市|1277,绵阳市|1268,平武县|1276,三台县|1271,盐亭县|1272,游仙区|1270,梓潼县|1274\",\n" +
            "        \"内江市\": \"东兴区|1139,隆昌县|1142,内江市|1136,市中区|1137,市中区|1138,威远县|1140,资中县|1141\",\n" +
            "        \"南充市\": \"高坪区|1163,嘉陵区|1164,阆中市|1170,南部县|1165,南充市|1161,蓬安县|1167,顺庆区|1162,西充县|1169,仪陇县|1168,营山县|1166\",\n" +
            "        \"攀枝花市\": \"东区|1229,米易县|1232,攀枝花市|1228,仁和区|1231,西区|1230,盐边县|1233\",\n" +
            "        \"遂宁市\": \"安居区|1300,船山区|1299,大英县|1303,蓬溪县|1301,射洪县|1302,遂宁市|1298\",\n" +
            "        \"雅安市\": \"宝兴县|1326,汉源县|1322,芦山县|1325,名山县|1320,石棉县|1323,天全县|1324,雅安市|1318,荥经县|1321,雨城区|1319\",\n" +
            "        \"宜宾市\": \"长宁县|1176,翠屏区|1172,高县|1177,珙县|1178,江安县|1175,筠连县|1179,南溪县|1174,屏山县|1181,兴文县|1180,宜宾市|1171,宜宾县|1173\",\n" +
            "        \"资阳市\": \"安岳县|1287,简阳市|1289,乐至县|1288,雁江区|1286,资阳市|1285\",\n" +
            "        \"自贡市\": \"大安区|1281,富顺县|1284,贡井区|1280,荣县|1283,沿滩区|1282,自贡市|1278,自流井区|1279\"\n" +
            "    },\n" +
            "    \"西藏自治区\": {\n" +
            "        \"拉萨市\": \"城关区|3311,达孜县|3317,当雄县|3313,堆龙德庆县|3316,拉萨市|3310,林周县|3312,墨竹工卡县|3318,尼木县|3314,曲水县|3315\",\n" +
            "        \"阿里地区\": \"阿里地区|3369,措勤县|3376,噶尔县|3372,改则县|3375,革吉县|3374,普兰县|3370,日土县|3373,札达县|3371\",\n" +
            "        \"昌都地区\": \"八宿县|3345,边坝县|3349,察雅县|3344,昌都地区|3338,昌都县|3339,丁青县|3343,贡觉县|3341,江达县|3340,类乌齐县|3342,洛隆县|3348,芒康县|3347,左贡县|3346\",\n" +
            "        \"林芝地区\": \"波密县|3355,察隅县|3356,工布江达县|3352,朗县|3357,林芝地区|3350,林芝县|3351,米林县|3353,墨脱县|3354\",\n" +
            "        \"那曲地区\": \"安多县|3363,巴青县|3367,班戈县|3366,比如县|3361,嘉黎县|3360,那曲地区|3358,那曲县|3359,尼玛县|3368,聂荣县|3362,申扎县|3364,索县|3365\",\n" +
            "        \"日喀则地区\": \"昂仁县|3326,白朗县|3328,定结县|3331,定日县|3323,岗巴县|3337,吉隆县|3334,江孜县|3322,康马县|3330,拉孜县|3325,南木林县|3321,聂拉木县|3335,仁布县|3329,日喀则地区|3319,日喀则市|3320,萨嘎县|3336,萨迦县|3324,谢通门县|3327,亚东县|3333,仲巴县|3332\",\n" +
            "        \"山南地区\": \"措美县|3304,错那县|3308,贡嘎县|3300,加查县|3306,浪卡子县|3309,隆子县|3307,洛扎县|3305,乃东县|3298,琼结县|3302,曲松县|3303,桑日县|3301,山南地区|3297,扎囊县|3299\"\n" +
            "    },\n" +
            "    \"新疆自治区\": {\n" +
            "        \"乌鲁木齐市\": \"达坂城区|2036,东山区|2037,沙依巴克区|2032,水磨沟区|2034,天山区|2031,头屯河区|2035,乌鲁木齐市|2030,乌鲁木齐县|2038,新市区|2033\",\n" +
            "        \"阿克苏地区\": \"阿克苏地区|2118,阿克苏市|2119,阿瓦提县|2126,拜城县|2124,柯坪县|2127,库车县|2121,沙雅县|2122,温宿县|2120,乌什县|2125,新和县|2123\",\n" +
            "        \"阿勒泰地区\": \"阿勒泰地区|2128,阿勒泰市|2129,布尔津县|2130,福海县|2132,富蕴县|2131,哈巴河县|2133,吉木乃县|2135,青河县|2134\",\n" +
            "        \"阿图什市\": \"阿合奇县|2138,阿克陶县|2137,阿图什市|2136,乌恰县|2139\",\n" +
            "        \"巴音郭楞州\": \"巴音郭楞州|2095,博湖县|2104,和静县|2102,和硕县|2103,库尔勒市|2096,轮台县|2097,且末县|2100,若羌县|2099,尉犁县|2098,焉耆县|2101\",\n" +
            "        \"博尔塔拉州\": \"博尔塔拉州|2055,博乐市|2056,精河县|2057,温泉县|2058\",\n" +
            "        \"昌吉州\": \"昌吉市|2106,昌吉州|2105,阜康市|2107,呼图壁县|2109,吉木萨尔县|2112,玛纳斯县|2110,米泉市|2108,木垒县|2113,奇台县|2111\",\n" +
            "        \"哈密地区\": \"哈密地区|2072,哈密市|2073,伊吾县|2074\",\n" +
            "        \"和田地区\": \"策勒县|2069,和田地区|2063,和田市|2064,和田县|2065,洛浦县|2068,民丰县|2071,墨玉县|2066,皮山县|2067,于田县|2070\",\n" +
            "        \"喀什地区\": \"巴楚县|2086,伽师县|2085,喀什地区|2075,喀什市|2076,麦盖提县|2083,莎车县|2081,疏附县|2077,疏勒县|2078,叶城县|2082,英吉沙县|2079,岳普湖县|2084,泽普县|2080\",\n" +
            "        \"克拉玛依市\": \"白碱滩区|2053,独山子区|2051,克拉玛依区|2052,克拉玛依市|2050,乌尔禾区|2054\",\n" +
            "        \"石河子市\": \"阿拉尔市|2115,石河子市|2114,图木舒克市|2116,五家渠市|2117\",\n" +
            "        \"塔城地区\": \"额敏县|2090,和布克赛尔县|2094,沙湾县|2091,塔城地区|2087,塔城市|2088,托里县|2092,乌苏市|2089,裕民县|2093\",\n" +
            "        \"吐鲁番地区\": \"鄯善县|2061,吐鲁番地区|2059,吐鲁番市|2060,托克逊县|2062\",\n" +
            "        \"伊犁州\": \"察布查尔县|2043,巩留县|2045,霍城县|2044,奎屯市|2041,尼勒克县|2049,特克斯县|2048,新源县|2046,伊犁州|2039,伊宁市|2040,伊宁县|2042,昭苏县|2047\"\n" +
            "    },\n" +
            "    \"云南省\": {\n" +
            "        \"昆明市\": \"安宁市|841,呈贡县|833,东川区|832,富民县|835,官渡区|830,晋宁县|834,昆明市|827,禄劝县|839,盘龙区|829,石林县|837,嵩明县|838,五华区|828,西山区|831,寻甸县|840,宜良县|836\",\n" +
            "        \"保山市\": \"保山市|782,昌宁县|787,龙陵县|786,隆阳区|783,施甸县|784,腾冲县|785\",\n" +
            "        \"楚雄州\": \"楚雄市|865,楚雄州|864,大姚县|870,禄丰县|874,牟定县|867,南华县|868,双柏县|866,武定县|873,姚安县|869,永仁县|871,元谋县|872\",\n" +
            "        \"大理州\": \"大理州|794,宾川县|798,大理市|795,洱源县|804,鹤庆县|806,剑川县|805,弥渡县|799,南涧县|800,巍山县|801,祥云县|797,漾濞县|796,永平县|802,云龙县|803\",\n" +
            "        \"德宏州\": \"德宏州|807,梁河县|810,陇川县|812,潞西市|809,瑞丽市|808,盈江县|811\",\n" +
            "        \"迪庆州\": \"德钦县|902,迪庆州|900,维西县|903,香格里拉县|901\",\n" +
            "        \"红河州\": \"个旧市|884,河口县|895,红河县|893,红河州|883,建水县|888,开远市|885,泸西县|891,绿春县|894,蒙自县|886,弥勒县|890,屏边县|887,石屏县|889,元阳县|892\",\n" +
            "        \"丽江市\": \"古城区|777,华坪县|780,丽江市|776,宁蒗县|781,永胜县|779,玉龙县|778\",\n" +
            "        \"临沧市\": \"沧源县|775,凤庆县|770,耿马县|774,临沧市|768,临翔区|769,永德县|772,云县|771,镇康县|773\",\n" +
            "        \"墨江县\": \"江城县|791,景东县|789,景谷县|790,澜沧县|792,墨江县|788,西盟县|793\",\n" +
            "        \"怒江州\": \"福贡县|815,贡山县|816,兰坪县|817,泸水县|814,怒江州|813\",\n" +
            "        \"曲靖市\": \"富源县|860,会泽县|861,陆良县|857,罗平县|859,马龙县|856,麒麟区|855,曲靖市|854,师宗县|858,宣威市|863,沾益县|862\",\n" +
            "        \"文山州\": \"富宁县|826,广南县|825,麻栗坡县|822,马关县|823,丘北县|824,文山县|819,文山州|818,西畴县|821,砚山县|820\",\n" +
            "        \"西双版纳州\": \"景洪市|897,勐海县|898,勐腊县|899,西双版纳州|896\",\n" +
            "        \"玉溪市\": \"澄江县|877,峨山县|881,华宁县|879,江川县|876,通海县|878,新平县|882,易门县|880,玉溪市|875\",\n" +
            "        \"昭通市\": \"大关县|847,鲁甸县|844,巧家县|845,水富县|853,绥江县|849,威信县|852,盐津县|846,彝良县|851,永善县|848,昭通市|842,昭阳区|843,镇雄县|850\"\n" +
            "    },\n" +
            "    \"浙江省\": {\n" +
            "        \"杭州市\": \"滨江区|2782,淳安县|2786,富阳市|2788,拱墅区|2780,杭州市|2776,建德市|2787,江干区|2779,临安市|2789,上城区|2777,桐庐县|2785,西湖区|2781,下城区|2778,萧山区|2783,余杭区|2784\",\n" +
            "        \"湖州市\": \"安吉县|2806,长兴县|2805,德清县|2804,湖州市|2801,南浔区|2803,吴兴区|2802\",\n" +
            "        \"嘉兴市\": \"海盐县|2760,海宁市|2761,嘉善县|2759,嘉兴市|2757,平湖市|2762,桐乡市|2763,秀洲区|2758\",\n" +
            "        \"金华市\": \"东阳市|2834,金东区|2828,金华市|2826,兰溪市|2832,磐安县|2831,浦江县|2830,武义县|2829,婺城区|2827,义乌市|2833,永康市|2835\",\n" +
            "        \"丽水市\": \"缙云县|2740,景宁县|2745,丽水市|2737,莲都区|2738,龙泉市|2746,青田县|2739,庆元县|2744,松阳县|2742,遂昌县|2741,云和县|2743\",\n" +
            "        \"宁波市\": \"北仑区|2768,慈溪市|2774,奉化市|2775,海曙区|2765,江北区|2767,江东区|2766,宁波市|2764,宁海县|2772,象山县|2771,鄞州区|2770,余姚市|2773,镇海区|2769\",\n" +
            "        \"衢州市\": \"常山县|2822,江山市|2825,开化县|2823,柯城区|2820,龙游县|2824,衢江区|2821,衢州市|2819\",\n" +
            "        \"绍兴市\": \"上虞市|2812,绍兴市|2807,绍兴县|2809,嵊州市|2813,新昌县|2810,越城区|2808,诸暨市|2811\",\n" +
            "        \"台州市\": \"黄岩区|2749,椒江区|2748,临海市|2756,路桥区|2750,三门县|2752,台州市|2747,天台县|2753,温岭市|2755,仙居县|2754,玉环县|2751\",\n" +
            "        \"温州市\": \"苍南县|2796,洞头县|2793,乐清市|2800,龙湾区|2792,鹿城区|2791,平阳县|2795,瑞安市|2799,泰顺县|2798,温州市|2790,文成县|2797,永嘉县|2794\",\n" +
            "        \"舟山市\": \"岱山县|2817,定海区|2815,普陀区|2816,嵊泗县|2818,舟山市|2814\"\n" +
            "    },\n" +
            "    \"香港行政区\": {\n" +
            "        \"香港\": \"香港|3788\"\n" +
            "    },\n" +
            "    \"澳门行政区\": {\n" +
            "        \"澳门\": \"澳门|3108\"\n" +
            "    },\n" +
            "    \"台湾省\": {\n" +
            "        \"台北市\": \"台北市|1041\",\n" +
            "        \"高雄市\": \"高雄市|1055\",\n" +
            "        \"高雄县\": \"高雄县|1054\",\n" +
            "        \"花莲县\": \"花莲县|1052\",\n" +
            "        \"基隆市\": \"基隆市|1045\",\n" +
            "        \"嘉义市\": \"嘉义市|1044\",\n" +
            "        \"苗栗县\": \"苗栗县|1053\",\n" +
            "        \"南投县\": \"南投县|1036\",\n" +
            "        \"澎湖县\": \"澎湖县|1051\",\n" +
            "        \"屏东县\": \"屏东县|1047\",\n" +
            "        \"台北县\": \"台北县|1040\",\n" +
            "        \"台东县\": \"台东县|1037\",\n" +
            "        \"台南市\": \"台南市|1043\",\n" +
            "        \"台南县\": \"台南县|1042\",\n" +
            "        \"台中市\": \"台中市|1039\",\n" +
            "        \"台中县\": \"台中县|1038\",\n" +
            "        \"桃园县\": \"桃园县|1050\",\n" +
            "        \"新竹市\": \"新竹市|1049\",\n" +
            "        \"宜兰县\": \"宜兰县|1046\",\n" +
            "        \"云林县\": \"云林县|1035\",\n" +
            "        \"彰化县\": \"彰化县|1048\"\n" +
            "    }";
}
