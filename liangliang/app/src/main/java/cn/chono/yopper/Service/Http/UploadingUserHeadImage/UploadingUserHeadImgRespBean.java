package cn.chono.yopper.Service.Http.UploadingUserHeadImage;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class UploadingUserHeadImgRespBean extends RespBean {

    private HeadImgUrl resp;

    public HeadImgUrl getResp() {
        return resp;
    }

    public void setResp(HeadImgUrl resp) {
        this.resp = resp;
    }


    public class HeadImgUrl implements Serializable {

        private String headImgUrl;
        private String albumImgUrl;

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public String getAlbumImgUrl() {
            return albumImgUrl;
        }

        public void setAlbumImgUrl(String albumImgUrl) {
            this.albumImgUrl = albumImgUrl;
        }
    }
}
