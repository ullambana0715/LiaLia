package cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign;

/**
 * Created by sunquan on 16/4/28.
 */
public class AlipayGetSignResultsDto {

    private int result;// 检查结果（0：成功 1：过期 2：该时间已删除 3：已被其他支付方式支付过）

    private  String msg;// 失败信息

    private  String sign;// 签名结果

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
