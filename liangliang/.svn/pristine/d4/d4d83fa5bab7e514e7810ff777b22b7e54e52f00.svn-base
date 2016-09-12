package cn.chono.yopper.Service.Http.DatingAddress;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.ISO8601;

/**
 *  获取附近地点列表（选择约会地点列表）
 * Created by zxb on 2015/11/21.
 */
public class DatingAddressService extends HttpService {
    public DatingAddressService(Context context) {
        super(context);
    }

    DatingAddressBean addressBean;

    @Override
    public void enqueue() {
        OutDataClass = DatingAddressRespBean.class;
        HashMap<String, Object> HashMap = new HashMap<>();

        String time= ISO8601.encodeGB(addressBean.getDatingTime());


        HashMap.put("Lat", addressBean.getLat());
        HashMap.put("Lng", addressBean.getLng());
        HashMap.put("R", addressBean.getR());
        HashMap.put("DatingTime", time);
        HashMap.put("Start", addressBean.getStart());
        HashMap.put("Rows", addressBean.getRows());

        String type= addressBean.getType();
        if (! TextUtils.isEmpty(type))HashMap.put("type", type);
        String Sort= addressBean.getSort();
        if (! TextUtils.isEmpty(Sort))HashMap.put("Sort", Sort);

        callWrap = OKHttpUtils.get(context, HttpURL.get_dating_address, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        addressBean = (DatingAddressBean) iBean;
    }
}
