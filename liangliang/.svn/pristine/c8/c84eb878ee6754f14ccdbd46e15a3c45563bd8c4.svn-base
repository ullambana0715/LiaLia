package cn.chono.yopper.Service.Http.BlockRequest;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 删除黑名单
 * Created by zxb on 2015/11/22.
 */
public class BlockRequestService extends HttpService {
    public BlockRequestService(Context context) {
        super(context);
    }

    private BlockRequestBean requestBean;

    @Override
    public void enqueue() {
        OutDataClass = BlockRequestRespBean.class;

        HashMap<String, Object> linkedHashMap = new HashMap<>();


        String url = HttpURL.user_block + requestBean.getUserId() + "/block/" + requestBean.getId() + "?Block=" + requestBean.isBlock();

        callWrap = OKHttpUtils.post(context, url, linkedHashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        requestBean = (BlockRequestBean) iBean;
    }
}
