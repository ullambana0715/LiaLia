package cn.chono.yopper.Service.Http.DiscoverInfos;

/**
 * Created by cc on 16/6/14.
 */
public class CampaignsEntity {


    /**
     * id : sample string 1
     * title : sample string 2
     * type : sample string 3
     * imageUrl : sample string 4
     * redirectUrl : sample string 5
     * description : sample string 6
     * seq : 7
     */

    public String id; // 编号
    public String title; // 广告标题
    public String type;// WebView类型, Default:简单展示, Article:文章详情
    public String imageUrl; // 广告图片
    public String redirectUrl;// 导向链接,绝对路径
    public String description; // 描述
    public int seq;   // 排序字段
}
