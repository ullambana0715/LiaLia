package cn.chono.yopper.Service.Http.MyBonusList;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/10.
 */
public class MyBonusListRespBean extends RespBean {
    private MyBonusListRespContent resp;

    public MyBonusListRespContent getResp() {
        return resp;
    }

    public void setResp(MyBonusListRespContent resp) {
        this.resp = resp;
    }

    public class MyBonusListRespContent extends RespBean {
        private boolean canExcange;
        private String nextQuery;
        private int start;
        private List<BonusItem> list;

        public boolean isCanExcange() {
            return canExcange;
        }

        public void setCanExcange(boolean canExcange) {
            this.canExcange = canExcange;
        }

        public String getNextQuery() {
            return nextQuery;
        }

        public void setNextQuery(String nextQuery) {
            this.nextQuery = nextQuery;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public List<BonusItem> getList() {
            return list;
        }

        public void setList(List<BonusItem> list) {
            this.list = list;
        }


        @Override
        public String toString() {
            return "MyBonusListRespContent{" +
                    "canExcange=" + canExcange +
                    ", nextQuery='" + nextQuery + '\'' +
                    ", start=" + start +
                    ", list=" + list +
                    '}';
        }

        public class BonusItem implements Serializable {

            private String userPrizeId;

            private String userId;

            private Prize prize;

            // 数量
            private double count;

            // 有效期
            private String expiryDate;

            // 使用日期，未使用为null
            private String useDate;

            // 奖品状态（1:已领取 2:已过期 3:已兑换）
            private Integer status;

            // 是否已兑换
            private boolean isObtain;

            public String getUserPrizeId() {
                return userPrizeId;
            }

            public void setUserPrizeId(String userPrizeId) {
                this.userPrizeId = userPrizeId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
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

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public boolean isObtain() {
                return isObtain;
            }

            public void setIsObtain(boolean isObtain) {
                this.isObtain = isObtain;
            }

            @Override
            public String toString() {
                return "BonusItem{" +
                        "userPrizeId='" + userPrizeId + '\'' +
                        ", userId='" + userId + '\'' +
                        ", prize=" + prize +
                        ", count=" + count +
                        ", expiryDate='" + expiryDate + '\'' +
                        ", useDate='" + useDate + '\'' +
                        ", status=" + status +
                        ", isObtain=" + isObtain +
                        '}';
            }
        }

        public class Prize implements Serializable{
            private String prizeId;
            private int type;
            private int changeType;
            private int state;
            private String name;
            private String desc;
            private double price;
            private String imageUrl;
            private double appleCount;
            private int orderIndex;
            private int prizeLevel;
            private String exchangeValidDateFrom;
            private String drawValidDateFrom;
            private String drawValidDateTo;
            private int validDayCounts;
            private String exchangeValidDateTo;

            @Override
            public String toString() {
                return "Prize{" +
                        "prizeId='" + prizeId + '\'' +
                        ", type=" + type +
                        ", changeType=" + changeType +
                        ", state=" + state +
                        ", name='" + name + '\'' +
                        ", desc='" + desc + '\'' +
                        ", price=" + price +
                        ", imageUrl='" + imageUrl + '\'' +
                        ", appleCount=" + appleCount +
                        ", orderIndex=" + orderIndex +
                        ", prizeLevel=" + prizeLevel +
                        ", exchangeValidDateFrom='" + exchangeValidDateFrom + '\'' +
                        ", drawValidDateFrom='" + drawValidDateFrom + '\'' +
                        ", drawValidDateTo='" + drawValidDateTo + '\'' +
                        ", validDayCounts=" + validDayCounts +
                        ", exchangeValidDateTo='" + exchangeValidDateTo + '\'' +
                        '}';
            }

            public int getPrizeLevel() {
                return prizeLevel;
            }

            public void setPrizeLevel(int prizeLevel) {
                this.prizeLevel = prizeLevel;
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

            public int getValidDayCounts() {
                return validDayCounts;
            }

            public void setValidDayCounts(int validDayCounts) {
                this.validDayCounts = validDayCounts;
            }
        }
    }
}