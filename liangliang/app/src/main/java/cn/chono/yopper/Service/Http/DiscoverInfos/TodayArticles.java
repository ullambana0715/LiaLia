package cn.chono.yopper.Service.Http.DiscoverInfos;

import java.util.List;

/**
 * Created by cc on 16/6/14.
 */
public class TodayArticles {


    /**
     * id : 1
     * title : sample string 2
     * imageUrls : ["sample string 1","sample string 2","sample string 3"]
     * readCount : 3
     * commentCount : 4
     */

    public String id;// 编号
    public String title;// 标题
    public int readCount; // 评论数
    public int commentCount;// 阅读数
    public List<String> imageUrls;// 图片
}
