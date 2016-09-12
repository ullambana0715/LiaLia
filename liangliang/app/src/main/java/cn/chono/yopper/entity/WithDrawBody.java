package cn.chono.yopper.entity;

import java.io.Serializable;


public class WithDrawBody implements Serializable {


    //金额（单位：分）
    private String aliPayAccount;

    //支付宝账号
    private int cash;

    //支付宝姓名
    private String aliPayAccountName;


    public WithDrawBody(int cash, String aliPayAccount, String aliPayAccountName) {
        this.aliPayAccount = aliPayAccount;
        this.cash = cash;
        this.aliPayAccountName = aliPayAccountName;
    }
}
