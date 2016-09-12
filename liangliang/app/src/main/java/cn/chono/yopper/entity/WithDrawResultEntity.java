package cn.chono.yopper.entity;

/**
 * Created by sunquan on 16/8/4.
 */
public class WithDrawResultEntity {

    // 提现结果（0：成功 1：金额太少 2：魅力值不足 3：超过每日上限;4：已超过每日上限）
    private int result;

    // 失败信息
    private String msg;


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
}
