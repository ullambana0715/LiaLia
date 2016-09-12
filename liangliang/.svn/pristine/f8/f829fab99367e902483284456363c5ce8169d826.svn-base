package cn.chono.yopper.Service.Http.BlockList;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 黑名单列表
 * Created by zxb on 2015/11/22.
 */
public class BlockListService extends HttpService {
    public BlockListService(Context context) {
        super(context);
    }

    private BlockListBean listBean;


    @Override
    public void enqueue() {
        OutDataClass = BlockListRespBean.class;

        HashMap<String, Object> linkedHashMap = new HashMap<>();
        linkedHashMap.put("start", listBean.getStart());

        String url = HttpURL.block_list + listBean.getUserId() + "/block/list?";

        callWrap = OKHttpUtils.get(context, url, linkedHashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        listBean = (BlockListBean) iBean;
    }
}
