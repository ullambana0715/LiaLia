package cn.chono.yopper.Service.Http.Draw;

import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/4/7.
 */
public class DrawUserRespBean extends RespBean {
    private UserPrizeInfo resp;

    public UserPrizeInfo getResp() {
        return resp;
    }

    public void setResp(UserPrizeInfo resp) {
        this.resp = resp;
    }

    public class UserPrizeInfo{
        private List<UserPrizeInfoMsg> userPrizeInfoMsg;

        public List<UserPrizeInfoMsg> getUserPrizeInfoMsg() {
            return userPrizeInfoMsg;
        }

        public void setUserPrizeInfoMsg(List<UserPrizeInfoMsg> userPrizeInfoMsg) {
            this.userPrizeInfoMsg = userPrizeInfoMsg;
        }
    }

    public class UserPrizeInfoMsg{
        private String userId;
        private String userName;
        private String prizeId;
        private String prizeName;
        private double price;
        private double appleCount;
        private double count;
        private int status;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPrizeId() {
            return prizeId;
        }

        public void setPrizeId(String prizeId) {
            this.prizeId = prizeId;
        }

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getAppleCount() {
            return appleCount;
        }

        public void setAppleCount(double appleCount) {
            this.appleCount = appleCount;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
