package cn.chono.yopper.Service.Http.DiscoverInfos;

/**
 * Created by cc on 16/6/14.
 */
public class BannersEntity {


    /**
     * bannerId : sample string 1
     * name : sample string 2
     * iconUrl : sample string 3
     * description : sample string 4
     * allowUserDefine : true
     * redirectUrl : sample string 6
     * seq : 7
     */

    public String bannerId;// 栏目编号
    public String name;// 名称
    public String iconUrl; // 图标
    public String description;// 描述
    public boolean allowUserDefine; // 用户可自定义配置栏目
    public String redirectUrl; // 导向链接, 绝对路径, 允许为空
    public int seq; // 排序字段
}
