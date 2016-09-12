package cn.chono.yopper.Service.Http.ClimbingRank;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by jianghua on 2016/3/30.
 */
public class ClimbRankRespBean extends RespBean {

    private ClimbResp resp;

    public ClimbResp getResp() {
        return resp;
    }

    public void setResp(ClimbResp resp) {
        this.resp = resp;
    }

    public class ClimbResp implements Serializable {
        // 爬榜结果
        private int result;

        // 失败信息
        private String message;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "ClimbResp{" +
                    "result=" + result +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
