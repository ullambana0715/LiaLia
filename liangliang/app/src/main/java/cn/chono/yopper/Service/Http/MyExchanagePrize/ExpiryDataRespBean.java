package cn.chono.yopper.Service.Http.MyExchanagePrize;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

public class ExpiryDataRespBean extends RespBean {
    private ExchanageResp resp;

    public ExchanageResp getResp() {
        return this.resp;
    }

    public void setResp(ExchanageResp paramExchanageResp) {
        this.resp = paramExchanageResp;
    }

    public String toString() {
        return "ExpiryDataRespBean{resp=" + this.resp + '}';
    }

    public class ExchanageResp
            implements Serializable {
        private boolean canExcange;
        private List<PrizeData> list;
        private String nextQuery;
        private int appleDrawPrice;

        public int getAppleDrawPrice() {
            return appleDrawPrice;
        }

        public void setAppleDrawPrice(int appleDrawPrice) {
            this.appleDrawPrice = appleDrawPrice;
        }

        private Integer start;

        public ExchanageResp() {
        }

        public List<PrizeData> getList() {
            return this.list;
        }

        public String getNextQuery() {
            return this.nextQuery;
        }

        public Integer getStart() {
            return this.start;
        }

        public boolean isCanExcange() {
            return this.canExcange;
        }

        public void setCanExcange(boolean paramBoolean) {
            this.canExcange = paramBoolean;
        }

        public void setList(List<PrizeData> paramList) {
            this.list = paramList;
        }

        public void setNextQuery(String paramString) {
            this.nextQuery = paramString;
        }

        public void setStart(Integer paramInteger) {
            this.start = paramInteger;
        }

        public String toString() {
            return "ExchanageResp{canExcange=" + this.canExcange + ", nextQuery='" + this.nextQuery + '\'' + ", start=" + this.start + ", list=" + this.list + '}';
        }

        public class PrizeData  implements Serializable {
            private double count;
            private String expiryDate;
            private boolean isObtain;
            private PrizeBean prize;
            private Integer status;
            private String useDate;
            private String userId;
            private String userPrizeId;

            public PrizeData() {
            }

            public double getCount() {
                return this.count;
            }

            public String getExpiryDate() {
                return this.expiryDate;
            }

            public PrizeBean getPrize() {
                return this.prize;
            }

            public Integer getStatus() {
                return this.status;
            }

            public String getUseDate() {
                return this.useDate;
            }

            public String getUserId() {
                return this.userId;
            }

            public String getUserPrizeId() {
                return this.userPrizeId;
            }

            public boolean isObtain() {
                return this.isObtain;
            }

            public void setCount(double paramDouble) {
                this.count = paramDouble;
            }

            public void setExpiryDate(String paramString) {
                this.expiryDate = paramString;
            }

            public void setIsObtain(boolean paramBoolean) {
                this.isObtain = paramBoolean;
            }

            public void setPrize(PrizeBean paramPrizeBean) {
                this.prize = paramPrizeBean;
            }

            public void setStatus(Integer paramInteger) {
                this.status = paramInteger;
            }

            public void setUseDate(String paramString) {
                this.useDate = paramString;
            }

            public void setUserId(String paramString) {
                this.userId = paramString;
            }

            public void setUserPrizeId(String paramString) {
                this.userPrizeId = paramString;
            }

            public String toString() {
                return "PrizeData{userPrizeId='" + this.userPrizeId + '\'' + ", userId='" + this.userId + '\'' + ", count=" + this.count + ", expiryDate='" + this.expiryDate + '\'' + ", useDate='" + this.useDate + '\'' + ", status=" + this.status + ", isObtain=" + this.isObtain + ", prize=" + this.prize + '}';
            }

            public class PrizeBean
                    implements Serializable {
                private double appleCount;
                private Integer changeType;
                private String desc;
                private String drawValidDateFrom;
                private String drawValidDateTo;
                private String exchangeValidDateFrom;
                private String exchangeValidDateTo;
                private String imageUrl;
                private String name;
                private double orderIndex;
                private double price;
                private String prizeId;
                private Integer state;
                private Integer type;
                private Integer validDayCounts;

                public PrizeBean() {
                }

                public double getAppleCount() {
                    return this.appleCount;
                }

                public Integer getChangeType() {
                    return this.changeType;
                }

                public String getDesc() {
                    return this.desc;
                }

                public String getDrawValidDateFrom() {
                    return this.drawValidDateFrom;
                }

                public String getDrawValidDateTo() {
                    return this.drawValidDateTo;
                }

                public String getExchangeValidDateFrom() {
                    return this.exchangeValidDateFrom;
                }

                public String getExchangeValidDateTo() {
                    return this.exchangeValidDateTo;
                }

                public String getImageUrl() {
                    return this.imageUrl;
                }

                public String getName() {
                    return this.name;
                }

                public double getOrderIndex() {
                    return this.orderIndex;
                }

                public double getPrice() {
                    return this.price;
                }

                public String getPrizeId() {
                    return this.prizeId;
                }

                public Integer getState() {
                    return this.state;
                }

                public Integer getType() {
                    return this.type;
                }

                public Integer getValidDayCounts() {
                    return this.validDayCounts;
                }

                public void setAppleCount(double paramDouble) {
                    this.appleCount = paramDouble;
                }

                public void setChangeType(Integer paramInteger) {
                    this.changeType = paramInteger;
                }

                public void setDesc(String paramString) {
                    this.desc = paramString;
                }

                public void setDrawValidDateFrom(String paramString) {
                    this.drawValidDateFrom = paramString;
                }

                public void setDrawValidDateTo(String paramString) {
                    this.drawValidDateTo = paramString;
                }

                public void setExchangeValidDateFrom(String paramString) {
                    this.exchangeValidDateFrom = paramString;
                }

                public void setExchangeValidDateTo(String paramString) {
                    this.exchangeValidDateTo = paramString;
                }

                public void setImageUrl(String paramString) {
                    this.imageUrl = paramString;
                }

                public void setName(String paramString) {
                    this.name = paramString;
                }

                public void setOrderIndex(double paramDouble) {
                    this.orderIndex = paramDouble;
                }

                public void setPrice(double paramDouble) {
                    this.price = paramDouble;
                }

                public void setPrizeId(String paramString) {
                    this.prizeId = paramString;
                }

                public void setState(Integer paramInteger) {
                    this.state = paramInteger;
                }

                public void setType(Integer paramInteger) {
                    this.type = paramInteger;
                }

                public void setValidDayCounts(Integer paramInteger) {
                    this.validDayCounts = paramInteger;
                }

                public String toString() {
                    return "PrizeBean{prizeId='" + this.prizeId + '\'' + ", type=" + this.type + ", changeType=" + this.changeType + ", state=" + this.state + ", name='" + this.name + '\'' + ", desc='" + this.desc + '\'' + ", price=" + this.price + ", imageUrl='" + this.imageUrl + '\'' + ", appleCount=" + this.appleCount + ", orderIndex=" + this.orderIndex + ", exchangeValidDateFrom='" + this.exchangeValidDateFrom + '\'' + ", exchangeValidDateTo='" + this.exchangeValidDateTo + '\'' + ", drawValidDateFrom='" + this.drawValidDateFrom + '\'' + ", drawValidDateTo='" + this.drawValidDateTo + '\'' + ", validDayCounts=" + this.validDayCounts + '}';
                }
            }
        }
    }
}
