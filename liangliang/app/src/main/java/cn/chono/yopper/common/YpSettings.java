package cn.chono.yopper.common;

import cn.chono.yopper.data.VersionChkDTO;

/**
 * 系统常量 Author: sunquan
 */
public class YpSettings {


    public static final boolean isTest = false;


    // 拍照类型
    public static final int CAMERAIMAGE = 9999; // 拍照上传


    // 百度地图是否可用
    public static boolean gBaiduAvailable;

    /**
     * 图片暂存目录句柄
     */
    public static final String IMAGE_CACHE_DIRECTORY_ICON = "icon/";


    // 窗口跳转动画 右边进 左出
    public static final int ACTIVITY_IN_RIGHT_OUT_LEFT_ANIM_TYPE = 100;

    public final static String BUNDLE_KEY_WEB_URL_TYPE = "BUNDLE_KEY_WEB_URL_TYPE"; // 要加载的url界面标记
    public final static String BUNDLE_KEY_WEB_URL = "BUNDLE_KEY_WEB_URL"; // 要加载的url
    public final static String BUNDLE_KEY_WEB_TITLE = "BUNDLE_KEY_WEB_TITLE"; // 要加载的页面显示的title
    public final static String BUNDLE_KEY_WEB_HIDE_TITLE = "BUNDLE_KEY_WEB_HIDE_TITLE"; // 跳到web页后是否要隐藏应用本身的title
    public final static String BUNDLE_KEY_WEB_NEED_HOST = "BUNDLE_KEY_WEB_NEED_HOST"; // 跳到web页后是否需要加主机地址

    public final static String PREF_VIDEO_EXIST_USER = "Qupai_has_video_exist_in_user_list_pref";

    public static int RECORDE_SHOW = 10001;

    /**
     * 文件目录管理
     */
    public static final String CONFIG_FILE = "Config_Yopper"; // 配置信息句柄

    public static final String IS_OPEAN_MESSAGE_VOICE = "Is_Opean_Message_voice"; // 配置信息句柄


    public static final String MESSAGE_OTHER_JID = "message_other_jid"; // 对方的jid

    public static final String USERID = "userId"; // 用户id
    public static final String USER_NAME = "user_name"; // 用户昵称
    public static final String USER_DISLIKE = "user_dislike"; // 用户讨厌
    public static final String USER_ICON = "user_icon"; // 用户头像地址
    public static final String USER_HOROSCOPE = "user_horscope"; // 用户星座
    public static final String USER_SEX = "user_sex"; // 用户性别

    public static final String USER_LIKE = "user_like"; // 用户喜欢

    public static final String USER_INCOME = "user_income"; // 用户喜欢

    public static final String USER_PROFESSION = "user_profession"; // 用户职位

    public static final String USER_EMOTIONAL = "user_emotional"; // 用户情感状态

    public static final String USER_HOMETOWN = "user_hometown"; // 用户家乡

    public static final String USER_AGE = "user_age"; // 年龄

    public static final String USER_AGE_VISIBILITY = "user_age_visibility"; // 年龄显示隐藏

    public static final String USER_HEIGHT = "user_height"; // 身高

    public static final String USER_WEIGHT = "user_weight"; // 体重

    public static final String USER_LABLE = "user_lable"; // 标签
    public static final String VIDEO_ID = "video_id"; // 标签


    public static final String APPOINTMENT_INTENT_YTPE = "appointment_intent_type"; // 约会意愿类型


    public static final String NOTIFICATION_TITLE = "notification_title"; // 通知的标题：心动消息 冒泡通知


    public static final String INTENT_RESULT_CODE = "intent_result_code";

    public static final int FLAG_CHOOSE_PHONE = 6;
    public static final int FLAG_MODIFY_FINISH = 7;

    public static final int FLAG_MODIFY_FINISH_SELECT_ONE = 1111;


    public static final String suppose = "suppose";
    public static final String album_invite = "album_invite";


    // 版本检查结果全局变量
    public static VersionChkDTO gVersionChkDTO;

    public static final String CHAT_IMAGE_URL = "chat_image_url"; // 图片URL


    public static final String ConnectionClosedOnErrorMsg = "ConnectionClosedOnErrorMsg";

    public static final String message_voice_time = "message_voice_time";

    public static final String chat_message_time = "chat_message_time";

    public static final String chat_message_conversation_time = "chat_message_conversation_time";

    public static final String chat_message_list_time = "chat_message_list_time";


    public static final String FROM_PAGE = "from_page";
    public static final int IMG_SIZE_150 = 150;
    public static final int IMG_SIZE_300 = 300;
    public static final int IMG_SIZE_360 = 360;
    public static final int USER_COMPILE = 11;

    public static final int HEAD_IMG_REGISTER = 12;

    public static final int FLAG_CONSTELLATION = 13;

    public static final String DISCOVER_TAB_ID = "Discover_tab_id";

    public static final String PHOTO_TAG = "PHOTO_TAG";
    public static final String PHOTO_TAG_NMAE = "PHOTO_TAG_NAEM";
    public static final String PHOTO_TAG_LIST = "PHOTO_TAG_LIST";

    public static final int PHOTO_SELECT = 511;
    public static final int PHOTO_BUBBLING = 411;
    public static final int PHOTO_TYPE = 311;

    public static final String PHOTO_GALLERY_TAG = "PHOTO_GALLERY_TAG";
    public static final int PHOTO_TAG_LIST_ONE = 511;
    public static final String BUBBLING_LIST = "bubblingList";
    public static final String BUBBLING_LIST_POSITION = "bubblingposition";
    public static final String BUBBLING_LIST_ID = "BUBBLING_LIST_ID";
    public static final String ADDRESS_LIST_DTO = "ADDRESS_LIST_DTO";
    public static final String ZOOM_LIST_DTO = "ZOOM_LIST_DTO";

    public static final int LIKE_TYPE_ILIKE = 0;
    public static final int LIKE_TYPE_LIKEME = 1;
    public static final int LIKE_TYPE_LIKEEACH = 2;

    public static final String LIKE_ILIKE_PAGE = "ilikefragment";
    public static final String LIKE_LIKEME_PAGE = "likemefragment";
    public static final String LIKE_LIKEEACH_PAGE = "likeeachfragment";


    public static final String SCORE_ZOOM_LIST_DTO = "SCORE_ZOOM_LIST_DTO";


    public static String BUBBLING_PUBLISH = "BUBBLING_PUBLISH";


    public static String BUBBLING_ADDRESS_STR = "BUBBLING_ADDRESS_STR";

    public static String BUBBLING_FROM_TAG_KEY = "BUBBLING_FROM_TAG_KEY";
    public static String BUBBLING_FROM_TAG_DISCOVER = "BUBBLING_FROM_TAG_DISCOVER";

    public static String BUBBLING_FROM_TAG_USERCENTER_MY = "BUBBLING _FROM_TAG_USERCENTER_MY";

    public static final String VIDEO_PATH_NAME = "VIDEO_PATH_NAME";
    public static final String VIDEO_PATH_NAME_IMG = "VIDEO_PATH_NAME_IMG";
    public static final String VIDEO_PATH_TYPE = "VIDEO_PATH_TYPE";
    public static final String VIDEO_PATH_TYPE_UserInfoPresenter = "UserInfoPresenter";

    public static String CONNECT_STATE = "CONNECT_STATE";
    public static String PURPOSE_KEY = "PURPOSE_KEY";

    public static String ACTIVITY_FEE = "ACTIVITY_FEE";
    public static String ACTIVITY_ID = "ACTIVITY_ID";
    public static String ACTIVITY_STATUS = "ACTIVITY_STATUS";
    public static String SIGHUP_STATUS = "SIGHUP_STATUS";
    public static final String VIDEO_URL = "VIDEO_URL";

    public static final String VIDEO_IMG_URl = "VIDEO_IMG_URl";

    public static final String VIDEO_STATE = "VIDEO_STATE";
    public static final String VIDEO_OPEN = "VIDEO_OPEN";
    public static final String VIDEO_PURPOSE = "VIDEO_PURPOSE";
    public static final String VIDEO_CHAT_WITH_USER_ONLY = "VIDEO_CHAT_WITH_USER_ONLY";

    public static final String DAILLY_TASK_KEYS = "DAILLY_TASK_KEYS";//今日任务--P果


    public static final String FROM_TAG = "from_tag";//标记从哪个页面过来的
    public static final int FROM_LOGIN = 1000;
    public static final int FROM_REGIDTER = 1001;



    public static final int WEB_CODE = 2000;
    public static final String WEB_DATA_KEY = "WEB_DATA_KEY";

    public static final String SOURCE_YTPE_KEY = "SOURCE_YTPE_KEY";

    public static final String BannerId = "BannerId";

    public static final String BannerName = "BannerName";

    public static final int TRAVE_DESTINATION = 6001;
    public static final int TRAVE_LABEL = 6002;
    public static final int TRAVE_OBJECTS = 6003;
    public static final int TRAVE_PAY = 6004;
    public static final int MOVEMENT_THEME = 7000;
    public static final int MOVEMENT_LOCATION = 7001;
    public static final int MOVEMENT_SEND_MESSAGE = 8001;
    public static final int MOVEMENT_TIME = 9001;
    public static final int MOVEMENT_DRINKING = 9002;

    public static final int DATINGS_TO_MARRIAGE = 10001;


    public static final String DATINGS_TYPE = "DATINGS_TYPE"; // 邀约类型

    public static final String DATINGS_ID = "DATINGS_ID"; // 邀约id

    public static final String ADDRESS_USER_NAME = "address_name";//地址管理  用户名

    public static final String ADDRESS_USER_PHONE = "address_phone";//地址管理  联系方式

    public static final String ADDRESS_USER_AREA = "address_area";//地址管理  所在区域

    public static final String ADDRESS_USER_DETAIL = "address_detail";//地址管理  详细地址


    public static final String ORDER_TYPE = "order_type";//订单类型
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";//商品类型

    public static final String ORDER_ID = "orderId";//订单号

    public static final String ProductName = "productName";//购买数量 = "buy_num";//购买数量


    public static final String CONSULT_NAME = "consult_name";//咨询名

    public static final String ORDER_NO = "order_no";//订单编号

    public static final String CONSULT_TIME = "consult_time";//咨询时间


    public static final String PAY_COST = "pay_cost";//费用

    public static final String PAY_TYPE = "pay_type";//购买类型


    public static final String COUNSEL_TYPE = "counselType";//咨询类型

    public static final String COUNSEL_TABDATA = "COUNSEL_TABDATA";//咨询类型

    public static final String COUNSEL_STATUS = "counselStatus";//咨询状态


    public static final String PHOTO_STRING = "stringArray";//相册
    public static final String PHOTO_LIST = "listString";//相册
    public static final String PHOTO_SUBSCRIPT = "PHOTO_Subscript";//相册下标

    public static final String COUNSE_DATA = "COUNSE_DATA";//咨询时间

    public static final String COUNSE_CHARE = "COUNSE_CHARE";//咨询价格

    public static final String ORDER_FEEDBACK_PHOTO = "ORDER_FEEDBACK_PHOTO";//咨询时间

    public static final int ICON_REQUEST_IMAGE=400;
    public static final int PHOTO_REQUEST_IMAGE=401;

    public static final int ZOOM_LIST_DTO_CODE=500;


    public static final String Charm_Num = "Charm_Num";//魅力数量

    public static final String WithDraw_Account = "WithDraw_Account";//兑换金额


    public static String VersionName = "versionName";

    public static String ToSendWords = "toSendWords";

    public static String BubblingItemRefresh = "BubblingItemRefresh";

}
