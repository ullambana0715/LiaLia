package cn.chono.yopper.Service.Http.GetPrize;

import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListRespBean;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/21.
 */
public class GetPrizeRespBean extends RespBean {
    private GetPrizeRespBeanContent resp;

    public GetPrizeRespBeanContent getResp() {
        return resp;
    }

    public void setResp(GetPrizeRespBeanContent resp) {
        this.resp = resp;
    }

    public class GetPrizeRespBeanContent{
        private String userId;
        private String code;
        private String msg;
        private Prize prize;
        private double count;
        private String expiryDate;
        private String useDate;
        private int status;
        private String addressName;
        private String addressPhone;
        private String addressArea;
        private String addressDetail;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Prize getPrize() {
            return prize;
        }

        public void setPrize(Prize prize) {
            this.prize = prize;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getUseDate() {
            return useDate;
        }

        public void setUseDate(String useDate) {
            this.useDate = useDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public String getAddressPhone() {
            return addressPhone;
        }

        public void setAddressPhone(String addressPhone) {
            this.addressPhone = addressPhone;
        }

        public String getAddressArea() {
            return addressArea;
        }

        public void setAddressArea(String addressArea) {
            this.addressArea = addressArea;
        }

        public String getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(String addressDetail) {
            this.addressDetail = addressDetail;
        }
    }

    public class Prize {
        private String prizeId;
        private int type;
        private int changeType;
        private int state;
        private String name;
        private String desc;
        private double price;
        private String imageUrl;
        private boolean canExchange;
        private double appleCount;
        private int orderIndex;
        private String exchangeValidDateFrom;
        private String exchangeValidDateTo;
        private String drawValidDateFrom;
        private String drawValidDateTo;
        private int validDayCounts;
        private int changeTypeA;
        private int changeTypeB;
        private String prizeLevel;
        private double probability;
        private int maxCount;
        private int count;

        public String getPrizeId() {
            return prizeId;
        }

        public void setPrizeId(String prizeId) {
            this.prizeId = prizeId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getChangeType() {
            return changeType;
        }

        public void setChangeType(int changeType) {
            this.changeType = changeType;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public boolean isCanExchange() {
            return canExchange;
        }

        public void setCanExchange(boolean canExchange) {
            this.canExchange = canExchange;
        }

        public double getAppleCount() {
            return appleCount;
        }

        public void setAppleCount(double appleCount) {
            this.appleCount = appleCount;
        }

        public int getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
        }

        public String getExchangeValidDateFrom() {
            return exchangeValidDateFrom;
        }

        public void setExchangeValidDateFrom(String exchangeValidDateFrom) {
            this.exchangeValidDateFrom = exchangeValidDateFrom;
        }

        public String getExchangeValidDateTo() {
            return exchangeValidDateTo;
        }

        public void setExchangeValidDateTo(String exchangeValidDateTo) {
            this.exchangeValidDateTo = exchangeValidDateTo;
        }

        public String getDrawValidDateFrom() {
            return drawValidDateFrom;
        }

        public void setDrawValidDateFrom(String drawValidDateFrom) {
            this.drawValidDateFrom = drawValidDateFrom;
        }

        public String getDrawValidDateTo() {
            return drawValidDateTo;
        }

        public void setDrawValidDateTo(String drawValidDateTo) {
            this.drawValidDateTo = drawValidDateTo;
        }

        public int getValidDayCounts() {
            return validDayCounts;
        }

        public void setValidDayCounts(int validDayCounts) {
            this.validDayCounts = validDayCounts;
        }

        public int getChangeTypeA() {
            return changeTypeA;
        }

        public void setChangeTypeA(int changeTypeA) {
            this.changeTypeA = changeTypeA;
        }

        public int getChangeTypeB() {
            return changeTypeB;
        }

        public void setChangeTypeB(int changeTypeB) {
            this.changeTypeB = changeTypeB;
        }

        public String getPrizeLevel() {
            return prizeLevel;
        }

        public void setPrizeLevel(String prizeLevel) {
            this.prizeLevel = prizeLevel;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(int maxCount) {
            this.maxCount = maxCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
