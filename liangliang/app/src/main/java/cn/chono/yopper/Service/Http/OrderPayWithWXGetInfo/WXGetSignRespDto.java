package cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo;

/**
 * Created by cc on 16/5/19.
 */
public class WXGetSignRespDto {

    private int checkResult;//检果结果（0：成功 1：过期 2：该时间已删除 3：已被其他支付支付过）

    private String msg;//失败信息

    private WXGetSignUnifiedDto wxUnifiedResp;

    public int getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(int checkResult) {
        this.checkResult = checkResult;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WXGetSignUnifiedDto getWxUnifiedResp() {
        return wxUnifiedResp;
    }

    public void setWxUnifiedResp(WXGetSignUnifiedDto wxUnifiedResp) {
        this.wxUnifiedResp = wxUnifiedResp;
    }
}
