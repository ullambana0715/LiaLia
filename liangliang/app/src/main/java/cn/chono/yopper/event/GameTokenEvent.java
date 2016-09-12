package cn.chono.yopper.event;

/**
 * Created by sunquan on 16/6/3.
 */
public class GameTokenEvent {
    private String accessToken;
    private String refreshToken;
    private int unionId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getUnionId() {
        return unionId;
    }

    public void setUnionId(int unionId) {
        this.unionId = unionId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public GameTokenEvent(String accessToken, String refreshToken, int unionId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.unionId = unionId;
    }
}
