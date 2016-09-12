package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import cn.chono.yopper.smack.entity.EntityBase;

/**
 * 用户自选 文章栏目数据库
 *
 * @author SQ
 */
@Table(name = "UserSelectBannerDb")
public class UserSelectBannerDb extends EntityBase {



    //对话最后一条消息
    @Column(column = "bannerIdAddUserID")
    private String bannerIdAddUserID;

    @Column(column = "respStr")
    private String respStr;


    public UserSelectBannerDb(String bannerId, String respStr) {
        super();
        this.bannerIdAddUserID = bannerId;
        this.respStr = respStr;
    }


    public UserSelectBannerDb() {
        super();

    }

    public String getRespStr() {
        return respStr;
    }

    public void setRespStr(String respStr) {
        this.respStr = respStr;
    }

    public String getBannerIdAddUserID() {
        return bannerIdAddUserID;
    }

    public void setBannerIdAddUserID(String bannerIdAddUserID) {
        this.bannerIdAddUserID = bannerIdAddUserID;
    }
}
