package cn.chono.yopper.Service.Http.ArticleTalkinjectedService;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/2/25.
 */
public class ArticleTalkinjectedBean extends ParameterBean {

    private String id;

    private String content;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
