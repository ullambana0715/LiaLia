package cn.chono.yopper.Service.Http.ActivityOrderInfo;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/6/15.
 */
public class ActivityOrderInfoRespBean extends RespBean {
    private ActivityOrderInfo resp;

    public ActivityOrderInfo getResp() {
        return resp;
    }

    public void setResp(ActivityOrderInfo resp) {
        this.resp = resp;
    }

    public class ActivityOrderInfo{
        private ActivityOrder activity;
        private int freeOfflineActivityCount;
        private double discount;
        private double vipFee;
        private String orderId;
        private int orderType;
        private String orderNo;
        private int totalFee;
        private String productName;
        private String productDescription;
        private String createTime;

        public ActivityOrder getActivity() {
            return activity;
        }

        public void setActivity(ActivityOrder activity) {
            this.activity = activity;
        }

        public int getFreeOfflineActivityCount() {
            return freeOfflineActivityCount;
        }

        public void setFreeOfflineActivityCount(int freeOfflineActivityCount) {
            this.freeOfflineActivityCount = freeOfflineActivityCount;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getVipFee() {
            return vipFee;
        }

        public void setVipFee(double vipFee) {
            this.vipFee = vipFee;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(int totalFee) {
            this.totalFee = totalFee;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

    public class ActivityOrder{
        private String activityId;
        private String title;
        private String titleImageUrl;
        private String activityStartTime;
        private String city;
        private double fee;
        private String address;
        private String joinEndTime;
        private boolean allowFreeByMember;
        private int activityStatus;

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleImageUrl() {
            return titleImageUrl;
        }

        public void setTitleImageUrl(String titleImageUrl) {
            this.titleImageUrl = titleImageUrl;
        }

        public String getActivityStartTime() {
            return activityStartTime;
        }

        public void setActivityStartTime(String activityStartTime) {
            this.activityStartTime = activityStartTime;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getJoinEndTime() {
            return joinEndTime;
        }

        public void setJoinEndTime(String joinEndTime) {
            this.joinEndTime = joinEndTime;
        }

        public boolean isAllowFreeByMember() {
            return allowFreeByMember;
        }

        public void setAllowFreeByMember(boolean allowFreeByMember) {
            this.allowFreeByMember = allowFreeByMember;
        }

        public int getActivityStatus() {
            return activityStatus;
        }

        public void setActivityStatus(int activityStatus) {
            this.activityStatus = activityStatus;
        }
    }
}
