package cn.chono.yopper.Service.Http.SubitUserInfo;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class SubitUserInfoRespBean extends RespBean {

    private RegisterUser resp;

    public RegisterUser getResp() {
        return resp;
    }

    public void setResp(RegisterUser resp) {
        this.resp = resp;
    }

    public class RegisterUser implements Serializable {
        /**
         * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
         */

        private static final long serialVersionUID = 1L;
        private int horoscope;
        private String mobile;
        private String name;
        private int sex;
        private int userId;


        public int getHoroscope() {
            return horoscope;
        }

        public void setHoroscope(int horoscope) {
            this.horoscope = horoscope;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "RegisterUser [horoscope=" + horoscope + ", mobile=" + mobile
                    + ", name=" + name + ", sex=" + sex + ", userId=" + userId
                    + "]";
        }

    }

}
