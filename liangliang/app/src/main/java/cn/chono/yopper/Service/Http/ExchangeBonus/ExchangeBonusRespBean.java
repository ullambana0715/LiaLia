package cn.chono.yopper.Service.Http.ExchangeBonus;

import java.util.List;

import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListRespBean;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/21.
 */
public class ExchangeBonusRespBean extends RespBean{
    private ExchangeBonusRespContent resp;

    public ExchangeBonusRespContent getResp() {
        return resp;
    }

    public void setResp(ExchangeBonusRespContent resp) {
        this.resp = resp;
    }

    public class ExchangeBonusRespContent{
        private String nextQuery;
        private int start;
        private List<MyBonusListRespBean.MyBonusListRespContent.Prize> prize;

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

        public List<MyBonusListRespBean.MyBonusListRespContent.Prize> getPrize() {
            return prize;
        }

        public void setPrize(List<MyBonusListRespBean.MyBonusListRespContent.Prize> prize) {
            this.prize = prize;
        }
    }
}
