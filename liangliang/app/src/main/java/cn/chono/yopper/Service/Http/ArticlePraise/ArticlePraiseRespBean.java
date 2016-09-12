package cn.chono.yopper.Service.Http.ArticlePraise;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/2/25.
 */
public class ArticlePraiseRespBean extends RespBean {



    private DataResp resp;

    public DataResp getResp() {
        return resp;
    }

    public void setResp(DataResp resp) {
        this.resp = resp;
    }

    public class DataResp implements Serializable{
        private boolean resp;

        public boolean isResp() {
            return resp;
        }

        public void setResp(boolean resp) {
            this.resp = resp;
        }
    }
}
