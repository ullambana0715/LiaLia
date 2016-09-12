package cn.chono.yopper.utils;

/**
 * 请求地址
 *
 * @ClassName: HttpURL
 * @author: xianbin.zou
 * @date: 2015年3月26日 上午11:49:55 /api:http://116.231.138.29/help
 */

public class HttpURL {

    // 测试
    //	public static final String Test_URL = "http://www.chono.cn:8018";
    //外网测试
    //public static final String Test_URL = "http://www.chono.cn:88";


    public static final String Test_URL = "http://192.168.1.151:80";

//      public static final String Test_URL = "http://192.168.1.131:8005";
//    public static final String Test_URL = "http://192.168.1.145:82";

    //东东
//    public static final String Test_URL = "http://192.168.1.150:8082";


    // 正式服务器
    public static final String URL = "http://s01.apicdns.com";

    // webU
    // 正式
    public static final String webURL = "http://s01.apicdns.com/mobilesite/";

    // 测试weburl
    public static final String Test_webURL = "http://192.168.1.151:8033/mobilesite/";

    /**
     * 聊天图片上传
     */
    public static final String upload_chat_image = "/api/v2/chat/image";
    /**
     * 登陆
     */
    public static final String login = "/api/v2/user/login";


    /**
     * 访客列表
     */
    public static final String visitors_list = "/api/v2/user/";


    /**
     * 黑名单列表
     */
    public static final String block_list = "/api/v2/user/";


    /**
     * 根据id获取用户基本信息
     */

    public static final String get_user_info_with_id = "/api/v2/user/";


    /**
     * 获取附近地点列表（选择约会地点列表）
     */

    public static final String get_dating_address = "/api/v2/location/nearby?";

    /**
     * 获取用户资料,包括相册等
     */

    public static final String get_user_info_album = "/api/v2/user/";


    /**
     * 发布约会意愿请求
     */

    public static final String publish_wish = "/api/v2/activity/wish";


    /**
     * 发送短信验证吗
     */

    public static final String smscode = "/api/v2/user/smscode";
    /**
     * 验证短信验证吗
     */

    public static final String request_verify = "/api/v2/user/verify";
    /**
     * 短信验证吗登录
     */

    public static final String login_vcode = "/api/v2/user/login/vcode";
    /**
     * 注册用户
     */

    public static final String register_user = "/api/v2/user";


    /**
     * 上传用户相册照片
     */

    public static final String user_image = "/api/v2/user/image";
    /**
     * 上传用户头像照片
     * /api/v2/user/headimg?X={X}&Y={Y}&W={W}&H={H}&SaveToAlbum={SaveToAlbum}
     */

    public static final String user_headimg = "/api/v2/user/headimg?";
    /**
     * 保存编辑后的用户信息 /api/v2/user/{userId}/profile
     */


    /**
     * 更新用户资料
     */
    public static final String profile_user = "/api/v2/user/";

    /**
     * 发布约会
     */

    public static final String publish_dating = "/api/v2/activity/dating";


    /**
     * 获取版本信息
     */

    public static final String get_version_info = "/api/v2/version";

    /**
     * 退出登录
     */

    public static final String post_logout = "/api/v2/user/logout";


    /**
     * 重设密码
     */

    public static final String user_password = "/api/v2/user/password";


    /**
     * 每日首次报到
     */

    public static final String dailly_touch = "/api/v2/user/";

    /**
     * 获取P果积分
     */

    public static final String get_p_fruit_point = "/api/v2/user/";

    /**
     * 赠送P果
     */

    public static final String send_attraction = "/api/v2/user/";
    public static final String signup = "/api/v2/activities/";


    /**
     * 获取服务器的时间
     */

    public static final String timestamp = "/api/v2/timestamp";
    /**
     * 注册时收不到验证码的话，用户点击fasthelp可以向运营人员求助，
     */

    public static final String fasthelp = "/api/v2/fasthelp";

    /**
     * 刷新 token
     */

    public static final String refresh = "/api/v2/user/token/refresh";
    /**
     * 附近人
     */
    public static final String nearby = "/api/v2/users/nearby?";
    /**
     * 附近冒泡
     */
    public static final String bubbling_nearby = "/api/v2/bubble/nearby";
    /**
     * 附近冒泡-点赞（和点赞列表,和删除冒泡）
     */
    public static final String bubbling_bubble = "/api/v2/bubble";


    /**
     * 冒泡-上传图片
     */
    public static final String bubbling_Image = "/api/v2/bubble/image";

    /**
     * 我的冒泡列表
     */
    public static final String bubbling_list = "/api/v2/bubble/list";


    /**
     * 举报
     */
    public static final String user_report = "/api/v2/report?";

    /**
     * 拉黑
     */
    public static final String user_block = "/api/v2/user/";


    /**
     * 获取当前用户的碰友
     */
    public static final String sync = "/api/v2/user/";


    /**
     * 意见反馈
     */
    public static final String feedback = "/api/v2/feedback";


    /**
     * 更新交友目的和公开
     */
    public static final String change_dating_purpose = "/api/v2/video/verification/user/";


    /**
     * v2.2.0版本约Ta
     */

    public static final String v2_dating_attampt = "/api/v2/chat/dattingattampt";


    /**
     * 基于邀约的聊天判断
     */

    public static final String v2_chat_dating_user_attampt = "/api/v3/datings/chat/attempt";

    /**
     * v2.2.0版本邀请
     */

    public static final String v2_invite = "/api/v2/user/invite?";


    /**
     * v2.2.0检查冒泡提交限制
     */

    public static final String v2_bubble_limit = "/api/v2/bubble/postlimit";


    /**
     * 认证视频详情
     */
    public static final String verification_vedio_detail = "/api/v2/video/verification/user/";
    public static final String private_album = "/api/v2/user/privatealbum";

    /**
     * 音乐文件列表
     */
    public static final String video_music = "/api/v2/video/music/list?";

    public static final String namespace = "lialia";
    public static final String namespace_img = "Thumbnail";
    public static final String namespace_video = "Video";
    public static final String namespace_audio = "Audio";
    /**
     * 发布、重新发布认证视频
     */
    public static final String verification_user = "/api/v2/video/verification/user/";

    /**
     * 颜值打分候选人列表 GET
     */
    public static final String facerating_candidates_dailly = "/api/v2/facerating/candidates/dailly?";
    /**
     * 颜值打分Post
     */
    public static final String facerating = "/api/v2/facerating";
    /**
     * 评分列表，历史评分列表
     */
    public static final String facerating_list = "/api/v2/facerating/list?";


    public static final String get_bizarea_city = "/api/v2/bizarea/areas?";

    public static final String appointments_publish = "/api/v2/appointments";

    /**
     * 发布邀约前置条件
     */
    public static final String appointments_requirements = "/api/v3/datings/requirements";


    public static final String user_mobile_verification_code = "/api/v2/user/mobile";

    /**
     * 退出登录
     */
    public static final String user_logout = "/api/v2/user/logout";

    public static final String user_login3rd = "/api/v2/user3rd/login";

    public static final String user_register3rd = "/api/v2/user3rd";
    /**
     * 获取所有父栏目
     */
    public static final String banners = "/api/v2/article/banners";

    /**
     * 获取所有父栏目中子栏目
     */
    public static final String subbanners = "/api/v2/article/banners/";

    /**
     * 获取发现中的活动列表
     */
    public static final String campaigns = "/api/v2/campaigns";

    public static final String get_article_comments = "/api/v2/article/";

    public static final String activity_info = "api/v2/activities/";


    /**
     * 评论回复,评论举报
     */


    public static final String article_commentOtherComment = "/api/v2/article/comment";

    /**
     * v3发布邀约
     */
    public static final String datings = "/api/v3/datings";


    /**
     * v3获取约会详情
     */

    public static final String get_v3_dating_detail = "/api/v3/datings/";


    /**
     * v3约会列表
     */
    public static final String dating_list = "/api/v3/datings?";

    /**
     * v3旅行配置信息
     */
    public static final String dating_travelConfigs = "/api/v3/datings/travelConfigs";
    /**
     * v3上传约会图片
     */
    public static final String dating_image = "/api/v3/datings/image";
    /**
     * v3获取约会感兴趣用户
     */
    public static final String get_v3_dating_user_list = "/api/v3/datings/";
    /**
     * 征婚限制
     */
    public static final String dating_marriageLimit = "/api/v3/datings/marriageLimit";


    /**
     * v3我的约会列表
     */
    public static final String my_dating_list = "/api/v3/datings/mine";

    /**
     * v3约会关闭
     */
    public static final String close_dating = "/api/v3/datings/";


    /**
     * 未读消息
     */

    public static final String v2_chat_read_msg = "/api/v2/chat/readmsg";

    //爬行榜活动信息
    public static final String climb_ranklist = "/api/v2/ranklists?";

    //爬榜
    public static final String climb_rank = "/api/v2/ranklist/climb";

    //爬行榜奖品信息
    public static final String climb_bonus_info = "/api/v2/ranklist/stage";

    //我的奖品列表
    public static final String bonus_list = "/api/v2/prizes/user?";

    //我的能量页面
    public static final String my_energy = "/api/v2/powerAndConfigs";

    //主页获取能量值
    public static final String energy = "/api/v2/power";

    /**
     * 获取兑奖结果
     */
    public static final String expiry_result = "/api/v2/prize/exchange?";

    /**
     * 保存当前用户收奖品地址
     * <p>
     * 获取当前用户地址
     */
    public static final String now_user_address = "/api/v2/prize/user/address";

    /**
     * 获取当前用户能量信息
     */
    public static final String now_user_power = "/api/v2/power";

    /**
     * P果（积分）信息获取
     */
    public static final String pfruit_info = "/api/v2/user/";

    /**
     * 获取苹果可兑换奖品列表
     */
    public static final String expiry_date_list = "/api/v2/prizes/exchange?";

    //苹果兑换奖品页面
    public static final String exchange_bonus_list = "/api/v2/prize/list/user?";

    //抽奖接口
    public static final String draw = "/api/v2/prize/draw?";
    //抽奖界面奖品列表地址
    public static final String draw_list = "/api/v2/prize/draw";
    //抽奖界面中奖用户滚动列表
    public static final String draw_user_list = "/api/v2/prize/draw/user";

    /**
     * 获取当前登录用户个人中心的信息
     */
    public static final String user_center_info = "/api/v2/user/settings";

    /**
     * 照片点赞
     */
    public static final String parise_photo = "/api/v2/user/photo";

    //兑奖请求
    public static final String get_prize = "/api/v2/prize?";

    //支付宝支付前置条件以及获取签名
    public static final String alipay_get_sign = "/api/v2/orders/";

    //订单详情
    public static final String order_detail = "/api/v2/orders/";


    //我的订单列表
    public static final String my_order_list = "/api/v2/orders/user?";

    //投诉订单
    public static final String order_feedback = "/api/v2/orders/";

    //取消订单
    public static final String order_cancel = "/api/v2/orders/";


    public static final String counselors = "/api/v2/counselors";

    public static final String evaluation = "/api/v2/orders/evaluation?";

    public static final String order_counsel = "/api/v2/orders/counsel";


    //微信支付前置条件以及获取签名
    public static final String wx_pay_get_sign = "/api/v2/orders/wxPayUnifiedOrder";
    //评价订单
    public static final String order_evaluation = "/api/v2/orders/";


    //上传问题反馈的图片
    public static final String orders_image = "/api/v2/orders/image";

    //商品列表
    public static final String products = "/api/v2/products";


    //获取第三方OAuthToken
    public static final String get_OAuthToken = "/api/v2/user3rd/OAuthToken";


    //商品下单（购买苹果、星钻）
    public static final String orders_product = "/api/v2/orders/product";

    //投诉订单限制
    public static final String orders_limits = "/api/v2/orders/";
    //用户购买钥匙
    public static final String userkey = "/api/v2/userkeys/order";


    //获取邀约详情以及邀约处理状态 以及是否主动被动发起
    public static final String get_dating_status = "/api/v2/dating/state?";

    //邀约意图处理：同意 拒绝 再考虑
    public static final String dating_attempt_handle = "/api/v3/datings/chat/attempt";

    //解锁
    public static final String unlock = "/api/v2/userkeys/";


    //获取锁状态
    public static final String get_key_status = "/api/v2/chat/state?";


    //完成订单
    public static final String completion_order = "/api/v2/orders/";
    //根据用户Id和咨询师Id获取最近一条有效订单
    public static final String order_lastest = "/api/v2/orders/lastest?";


    /**
     * 邀约邀请用户
     */
    public static final String invite_datings = "/api/v3/datings/invite";


    //V2.5.4 VIP配置列表

    public static final String vipConfigs = "/api/v2/users/VipConfigs";

    public static final String vipPay = "/api/v2/users/";

    /**
     * 喜欢列表
     */
    public static final String like_list = "/api/v2/users/like?";


    public static final String like_handle = "/api/v2/users/";

    /**
     * 我的活动
     */
    public static final String mine_activities = "/api/v2/activities/mine?";

    public static final String activity_sign = "/api/v2/activities/";

    /**
     * 首页活动列表
     */
    public static final String index_activities = "/api/v2/activities?";

    public static final String DiscoverInfos = "/api/v2/article/DiscoverInfos?";

    //游戏订单详情
    public static final String game_order_detail = "/api/v2/orders/";


}
