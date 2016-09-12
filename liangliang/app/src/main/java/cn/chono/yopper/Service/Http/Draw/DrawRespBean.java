package cn.chono.yopper.Service.Http.Draw;

import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListRespBean;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/25.
 */
public class DrawRespBean extends RespBean {
    private DrawRespBeanContent resp;

    public DrawRespBeanContent getResp() {
        return resp;
    }

    public void setResp(DrawRespBeanContent resp) {
        this.resp = resp;
    }

    public class DrawRespBeanContent{
        private String userId;
        private String code;
        private String msg;

        public MyBonusListRespBean.MyBonusListRespContent.Prize getPrize() {
            return prize;
        }

        public void setPrize(MyBonusListRespBean.MyBonusListRespContent.Prize prize) {
            this.prize = prize;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        private MyBonusListRespBean.MyBonusListRespContent.Prize prize;
    }
}
