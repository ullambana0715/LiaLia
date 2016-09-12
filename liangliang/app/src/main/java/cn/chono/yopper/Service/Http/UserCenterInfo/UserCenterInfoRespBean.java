package cn.chono.yopper.Service.Http.UserCenterInfo;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by jianghua on 2016/3/17.
 */
public class UserCenterInfoRespBean extends RespBean {

    private UserInfoBean resp;

    public UserInfoBean getResp() {
        return resp;
    }

    public void setResp(UserInfoBean resp) {
        this.resp = resp;
    }

    public class UserInfoBean implements Serializable {
        // 我的苹果数
        private int appleCount;

        // 我的能量
        private UserCenterPower myPower;


        private int keyCount;


        private int remainCharm;


        public int getAppleCount() {
            return appleCount;
        }

        public void setAppleCount(int appleCount) {
            this.appleCount = appleCount;
        }

        public UserCenterPower getMyPower() {
            return myPower;
        }

        public void setMyPower(UserCenterPower myPower) {
            this.myPower = myPower;
        }


        public int getKeyCount() {
            return keyCount;
        }

        public void setKeyCount(int keyCount) {
            this.keyCount = keyCount;
        }

        @Override
        public String toString() {
            return "UserInfoBean{" +
                    "appleCount=" + appleCount +
                    ", myPower=" + myPower +
                    ", keyCount=" + keyCount +
                    ", remainCharm=" + remainCharm +
                    '}';
        }

        public int getRemainCharm() {
            return remainCharm;
        }

        public void setRemainCharm(int remainCharm) {
            this.remainCharm = remainCharm;
        }

        public class UserCenterPower implements Serializable {
            // 用户Id
            private String userId;

            // 当前能量值
            private int currentValue;

            // 本期已充能量值
            private int totalValue;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public int getCurrentValue() {
                return currentValue;
            }

            public void setCurrentValue(int currentValue) {
                this.currentValue = currentValue;
            }

            public int getTotalValue() {
                return totalValue;
            }

            public void setTotalValue(int totalValue) {
                this.totalValue = totalValue;
            }

            @Override
            public String toString() {
                return "UserCenterPower{" +
                        "userId='" + userId + '\'' +
                        ", currentValue=" + currentValue +
                        ", totalValue=" + totalValue +
                        '}';
            }
        }


    }

    @Override
    public String toString() {
        return "UserCenterInfoRespBean{" +
                "resp=" + resp +
                '}';
    }
}
