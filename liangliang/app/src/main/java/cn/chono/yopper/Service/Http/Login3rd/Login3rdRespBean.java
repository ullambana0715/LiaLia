package cn.chono.yopper.Service.Http.Login3rd;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/1/27.
 */
public class Login3rdRespBean extends RespBean {
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */

    private static final long serialVersionUID = 1L;


    private Login3rdDto resp;

    public Login3rdDto getResp() {
        return resp;
    }

    public void setResp(Login3rdDto resp) {
        this.resp = resp;
    }
}
