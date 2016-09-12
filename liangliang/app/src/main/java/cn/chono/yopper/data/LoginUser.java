package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.exception.DbException;
import java.io.Serializable;
import java.util.List;
import cn.chono.yopper.base.App;

@Table(name = "LoginUser")
public class LoginUser extends EntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(column = "userId")
    private int userId = 0;

    @Column(column = "refreshToken")
    private String refreshToken = "";

    @Column(column = "authToken")
    private String authToken = "";

    @Column(column = "expiration")
    private long expiration = -1;

    @Column(column = "msg")
    private String msg = "";

    @Column(column = "userSig")
    private String userSig;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }


    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }


    private static LoginUser loginUser;

    public static LoginUser getInstance() {

        if (loginUser == null) {
            try {
                List<LoginUser> list = App.getInstance().db.findAll(LoginUser.class);
                if (list != null && list.size() > 0) {
                    loginUser = list.get(0);
                } else {
                    loginUser = new LoginUser(0, "", "", -1, "", "");
                }

            } catch (DbException e) {
                e.printStackTrace();
                loginUser = new LoginUser(0, "", "", -1, "", "");
            }
        }
        return loginUser;
    }


    public static void reSetLoginUser() {

        try {
            List<LoginUser> list = App.getInstance().db.findAll(LoginUser.class);
            if (list != null && list.size() > 0) {
                App.getInstance().db.deleteAll(LoginUser.class);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        loginUser = new LoginUser(0, "", "", -1, "", "");
    }


    public static void setLoginUser(LoginUser dto) {

        if (loginUser == null) {
            loginUser = new LoginUser();
        }
        loginUser.setUserId(dto.getUserId());
        loginUser.setRefreshToken(dto.getRefreshToken());
        loginUser.setMsg(dto.getMsg());
        loginUser.setExpiration(dto.getExpiration());
        loginUser.setAuthToken(dto.getAuthToken());
        loginUser.setUserSig(dto.getUserSig());

        try {
            List<LoginUser> list = App.getInstance().db.findAll(LoginUser.class);
            if (list != null && list.size() > 0) {
                App.getInstance().db.deleteAll(LoginUser.class);
            }

            App.getInstance().db.save(loginUser);

        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    public LoginUser(int userId, String refreshToken, String authToken, long expiration, String msg, String userSig) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.authToken = authToken;
        this.expiration = expiration;
        this.msg = msg;
        this.userSig = userSig;
    }

    public LoginUser() {

    }


    @Override
    public String toString() {
        return "LoginUser{" +
                "userId=" + userId +
                ", refreshToken='" + refreshToken + '\'' +
                ", authToken='" + authToken + '\'' +
                ", expiration=" + expiration +
                ", msg='" + msg + '\'' +
                ", userSig='" + userSig + '\'' +
                '}';
    }
}
