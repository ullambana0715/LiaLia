package cn.chono.yopper.Service.Http.LikeHandle;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class LikeHandleRespBean extends RespBean {
    private LikeHandleResultDto resp;

    public LikeHandleResultDto getResp() {
        return resp;
    }

    public void setResp(LikeHandleResultDto resp) {
        this.resp = resp;
    }


    public class LikeHandleResultDto {

        //0表示成功 1表示拉黑用户
        private int likeResult;

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getLikeResult() {
            return likeResult;
        }

        public void setLikeResult(int likeResult) {
            this.likeResult = likeResult;
        }
    }
}
