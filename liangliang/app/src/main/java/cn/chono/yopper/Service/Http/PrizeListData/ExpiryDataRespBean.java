package cn.chono.yopper.Service.Http.PrizeListData;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by jianghua on 2016/3/15.
 */
public class ExpiryDataRespBean extends RespBean {

    private String nextQuery;

    private List<Prize> list;

    private String start;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public List<Prize> getList() {
        return list;
    }

    public void setList(List<Prize> list) {
        this.list = list;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    /**
     * 奖品的实例
     */
    class Prize implements Serializable {
        // 奖品Id
        private String prizeId;

        // 奖品类型（1:实物 2:抽奖 3:苹果）
        private String type;

        // 1兑换奖品,2抽取的奖品,3全部可
        private String changeType;

        // 奖品状态（1:已领取 2:已过期 3:已兑换）
        private String state;

        // 奖品名称
        private String name;

        // 具体描述
        private String desc;

        // 价格
        private String price;

        // 奖品图片Url
        private String imageUrl;

        // 是否可兑换
        private String canExchange;

        // 兑换苹果数
        private String appleCount;

        // 创建时间
        private String createTime;

        //
        private String orderIndex;

        // 有效起始
        private String validDateFrom;

        // 有效结束
        private String validDateTo;

        // 有效天数
        private String validDayCounts;

        public String getPrizeId() {
            return prizeId;
        }

        public void setPrizeId(String prizeId) {
            this.prizeId = prizeId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getChangeType() {
            return changeType;
        }

        public void setChangeType(String changeType) {
            this.changeType = changeType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getCanExchange() {
            return canExchange;
        }

        public void setCanExchange(String canExchange) {
            this.canExchange = canExchange;
        }

        public String getAppleCount() {
            return appleCount;
        }

        public void setAppleCount(String appleCount) {
            this.appleCount = appleCount;
        }

        public String getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(String orderIndex) {
            this.orderIndex = orderIndex;
        }

        public String getValidDateFrom() {
            return validDateFrom;
        }

        public void setValidDateFrom(String validDateFrom) {
            this.validDateFrom = validDateFrom;
        }

        public String getValidDateTo() {
            return validDateTo;
        }

        public void setValidDateTo(String validDateTo) {
            this.validDateTo = validDateTo;
        }

        public String getValidDayCounts() {
            return validDayCounts;
        }

        public void setValidDayCounts(String validDayCounts) {
            this.validDayCounts = validDayCounts;
        }
    }
}
