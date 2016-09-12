package cn.chono.yopper.Service.Http.OpenAlbumWithApple;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/7/26.
 */
public class OpenAlbumRespBean extends RespBean {
    private int result;// V3.1 0表示成功解锁，1表示没有足够的苹果
    private String msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
