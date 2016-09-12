package cn.chono.yopper.Service.Http.OAuthToken;

import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class OAuthTokenRespBean extends RespBean {

    private OAuthTokenDto resp;

    public OAuthTokenDto getResp() {
        return resp;
    }

    public void setResp(OAuthTokenDto resp) {
        this.resp = resp;
    }


    public class OAuthTokenDto{
        private String accessToken;
        private String refreshToken;
        private int unionId;
        private String[] scopes;


        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public int getUnionId() {
            return unionId;
        }

        public void setUnionId(int unionId) {
            this.unionId = unionId;
        }

        public String[] getScopes() {
            return scopes;
        }

        public void setScopes(String[] scopes) {
            this.scopes = scopes;
        }
    }
}
