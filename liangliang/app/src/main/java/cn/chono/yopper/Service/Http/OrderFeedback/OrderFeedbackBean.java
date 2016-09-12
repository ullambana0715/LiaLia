package cn.chono.yopper.Service.Http.OrderFeedback;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/5/5.
 */
public class OrderFeedbackBean extends ParameterBean {

    public String id;//订单Id

    public int feedbackType;//投诉类型（0：占卜师没有在约定时间出现 1：占卜师未经商量结束了服务 2：其他问题）

    public String description;//描述

    public String[] imageUrls;//图片Url集合
}
