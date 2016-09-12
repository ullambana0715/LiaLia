package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import cn.chono.yopper.smack.entity.EntityBase;

/**
 * 服务器获取文章栏目数据库
 *
 * @author SQ
 */
@Table(name = "BannerDb")
public class BannerDb extends EntityBase {



    //对话最后一条消息
    @Column(column = "bannerId")
    private String bannerId;

    @Column(column = "respStr")
    private String respStr;


    public BannerDb(String bannerId, String respStr) {
        super();
        this.bannerId = bannerId;
        this.respStr = respStr;
    }


    public BannerDb() {
        super();

    }

    public String getRespStr() {
        return respStr;
    }

    public void setRespStr(String respStr) {
        this.respStr = respStr;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }
}
