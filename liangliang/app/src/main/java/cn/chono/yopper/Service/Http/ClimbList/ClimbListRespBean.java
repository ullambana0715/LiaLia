package cn.chono.yopper.Service.Http.ClimbList;

import com.tencent.mm.sdk.modelbase.BaseResp;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/15.
 */
public class ClimbListRespBean extends RespBean {
    private ClimbListRespBeanTemp resp;

    public ClimbListRespBeanTemp getResp() {
        return resp;
    }

    public void setResp(ClimbListRespBeanTemp resp) {
        this.resp = resp;
    }


    public class ClimbListRespBeanTemp implements Serializable {

        //榜单分页数据
        private Rank ranklists;

        // 我的排名
        private int myRank;

        // 我的能量值
        private int currentPower;

        //榜单周期Id
        private String stageId;

        // 榜单期数
        private String stageNumber;

        //当前榜单周期结束时间
        private String endTime;

        public Rank getRanklists() {
            return ranklists;
        }

        public void setRanklists(Rank ranklists) {
            this.ranklists = ranklists;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStageNumber() {
            return stageNumber;
        }

        public void setStageNumber(String stageNumber) {
            this.stageNumber = stageNumber;
        }

        public String getStageId() {
            return stageId;
        }

        public void setStageId(String stageId) {
            this.stageId = stageId;
        }

        public int getCurrentPower() {
            return currentPower;
        }

        public void setCurrentPower(int currentPower) {
            this.currentPower = currentPower;
        }

        public int getMyRank() {
            return myRank;
        }

        public void setMyRank(int myRank) {
            this.myRank = myRank;
        }

        @Override
        public String toString() {
            return "ClimbListRespBeanTemp{" +
                    "ranklists=" + ranklists +
                    ", myRank=" + myRank +
                    ", currentPower=" + currentPower +
                    ", stageId='" + stageId + '\'' +
                    ", stageNumber='" + stageNumber + '\'' +
                    ", endTime='" + endTime + '\'' +
                    '}';
        }

        // 榜单数据对象
        public class Rank implements Serializable {

            private String nextQuery;

            private int start;

            private List<RankItem> list;

            public String getNextQuery() {
                return nextQuery;
            }

            public void setNextQuery(String nextQuery) {
                this.nextQuery = nextQuery;
            }

            public List<RankItem> getList() {
                return list;
            }

            public void setList(List<RankItem> list) {
                this.list = list;
            }

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public class RankItem implements Serializable {

                // 榜单Id
                private String ranklistId;

                //用户信息
                private RankUser user;

                //能量信息
                private RankUserPower power;

                public String getRanklistId() {
                    return ranklistId;
                }

                public void setRanklistId(String ranklistId) {
                    this.ranklistId = ranklistId;
                }

                public RankUserPower getPower() {
                    return power;
                }

                public void setPower(RankUserPower power) {
                    this.power = power;
                }

                public RankUser getUser() {
                    return user;
                }

                public void setUser(RankUser user) {
                    this.user = user;
                }


                public class RankUser implements Serializable {
                    // 用户Id
                    private String userId;

                    // 昵称
                    private String name;

                    // 星座
                    private int horoscope;

                    //对外头像
                    private String headImg;

                    //对外头像
                    private int sex;

                    private int isVip;

                    // V2.5.4 vip身份  ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
                    private int currentUserPosition;


                    public String getHeadImg() {
                        return headImg;
                    }

                    public void setHeadImg(String headImg) {
                        this.headImg = headImg;
                    }

                    public String getUserId() {
                        return userId;
                    }

                    public void setUserId(String userId) {
                        this.userId = userId;
                    }

                    public int getSex() {
                        return sex;
                    }

                    public void setSex(int sex) {
                        this.sex = sex;
                    }

                    public int getHoroscope() {
                        return horoscope;
                    }

                    public void setHoroscope(int horoscope) {
                        this.horoscope = horoscope;
                    }

                    public String getName() {
                        return name;
                    }

                    public int getIsVip() {
                        return isVip;
                    }

                    public void setIsVip(int isVip) {
                        this.isVip = isVip;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getCurrentUserPosition() {
                        return currentUserPosition;
                    }

                    public void setCurrentUserPosition(int currentUserPosition) {
                        this.currentUserPosition = currentUserPosition;
                    }
                }

                public class RankUserPower implements Serializable {
                    // 用户Id
                    private String userId;

                    //当前能量值
                    private int currentValue;

                    //本期已充能量值
                    private int totalValue;

                    public String getUserId() {
                        return userId;
                    }

                    public void setUserId(String userId) {
                        this.userId = userId;
                    }

                    public int getTotalValue() {
                        return totalValue;
                    }

                    public void setTotalValue(int totalValue) {
                        this.totalValue = totalValue;
                    }

                    public int getCurrentValue() {
                        return currentValue;
                    }

                    public void setCurrentValue(int currentValue) {
                        this.currentValue = currentValue;
                    }
                }

            }
        }
    }
}
