package cn.chono.yopper.Service.Http.ArticleReportComment;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/2/25.
 */
public class ArticleReportCommentBean extends ParameterBean {

    private String commentId;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
