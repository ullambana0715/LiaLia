package cn.chono.yopper.Service.Http.ExpiryDate;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by jianghua on 2016/3/15.
 */
public class ExpiryDateRespBean extends RespBean {

    private ExpiryResp resp;

    public ExpiryResp getResp() {
        return resp;
    }

    public void setResp(ExpiryResp resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "ExpiryDateRespBean{" +
                "resp=" + resp +
                '}';
    }

    public class ExpiryResp implements Serializable {
        //兑换结果, 空:成功, AccountNotFound: 帐号不存在, InvalidAccountStatus: 帐号状态无效
        // Inadequate: 帐户余额不足, Failed: 兑换失败, PrizeNotFound: 奖品不存在;
        private String code;

        //错误信息
        private String msg;

        //奖品名称
        private String name;

        private String prizeId;

        private String urlPic;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrizeId() {
            return prizeId;
        }

        public void setPrizeId(String prizeId) {
            this.prizeId = prizeId;
        }

        public String getUrlPic() {
            return urlPic;
        }

        public void setUrlPic(String urlPic) {
            this.urlPic = urlPic;
        }

        @Override
        public String toString() {
            return "ExpiryResp{" +
                    "code='" + code + '\'' +
                    ", msg='" + msg + '\'' +
                    ", name='" + name + '\'' +
                    ", prizeId='" + prizeId + '\'' +
                    ", urlPic='" + urlPic + '\'' +
                    '}';
        }
    }
}
