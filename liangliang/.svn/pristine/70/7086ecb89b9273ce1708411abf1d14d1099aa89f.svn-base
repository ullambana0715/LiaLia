package cn.chono.yopper.Service.Http.BlockListMore;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/22.
 */
public class BlockListMoreService extends HttpService {
    public BlockListMoreService(Context context) {
        super(context);
    }

    private BlockListMoreBean listMoreBean;

    @Override
    public void enqueue() {
        OutDataClass = BlockListMoreRespBean.class;

        callWrap = OKHttpUtils.get(context, listMoreBean.getNextQuery(), okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        listMoreBean = (BlockListMoreBean) iBean;

    }
}
