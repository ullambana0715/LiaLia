package cn.chono.yopper.Service.Http.ExpiryDate;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 2016/3/15.
 */
public class UserInfoBean implements Serializable {

    //姓名, 必填项
    private String name;

    //联系电话
    private String phone;

    //所在地区
    private String area;

    //详细地址
    private String address;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
