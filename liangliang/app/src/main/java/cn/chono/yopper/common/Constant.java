package cn.chono.yopper.common;

public class Constant {

    /**
     * 约会类型
     */
    /**
     * 吃饭
     */
    public static final int APPOINT_TYPE_EAT = 3;


    /**
     * 电影
     */
    public static final int APPOINT_TYPE_MOVIE = 4;
    /**
     * 狗
     */
    public static final int APPOINT_TYPE_DOG = 6;
    /**
     * 运动
     */
    public static final int APPOINT_TYPE_FITNESS = 5;
    /**
     * 唱歌
     */
    public static final int APPOINT_TYPE_KTV = 7;
    /**
     * 全部
     */
    public static final int APPOINT_TYPE_NO_LIMIT = 0;

    /**
     * 征婚
     */

    public static final int APPOINT_TYPE_MARRIED = 1;

    /**
     * 其他
     */
    public static final int APPOINT_TYPE_OTHERS = 8;

    /**
     * 旅游
     */
    public static final int APPOINT_TYPE_TRAVEL = 2;

    /**
     * 情感状态类型
     */
    public static final int Emotional_Type_secrecy = 0;
    public static final int Emotional_Type_lone = 1;
    public static final int Emotional_Type_married = 2;
    public static final int Emotional_Type_loving = 3;
    public static final int Emotional_Type_Gay = 4;


    /**
     * 收入类型
     */

    public static final int Income_Type_secrecy = 0;
    public static final int Income_Type_3000down = 1;
    public static final int Income_Type_3000up = 2;
    public static final int Income_Type_5000up = 3;
    public static final int Income_Type_10000up = 4;
    public static final int Income_Type_20000up = 5;
    public static final int Income_Type_50000up = 6;


    /**
     * 父母健在情况
     */
    public static final int Parents_Type_Parents = 0;//双亲健在
    public static final int Parents_Type_Mother = 1;//父亲健在
    public static final int Parents_Type_Father = 2;//母亲健在

    /**
     * 子女情况
     */
    public static final int Child_Type_No = 0;//无
    public static final int Child_Type_Have = 1;//有
    public static final int Child_Type_Have_I = 2;//有/孩子跟我
    public static final int Child_Type_Have_Parents = 3;//有/孩子跟我父母
    public static final int Child_Type_Have_Other_Side = 4;//孩子跟对方

    /**
     * 恋爱史
     */
    public static final int LoveHistory_Type_No = 0;//恋爱啥滋味？
    public static final int LoveHistory_Type_One = 1;//一次
    public static final int LoveHistory_Type_Two = 2;//两次
    public static final int LoveHistory_Type_Three = 3;//三次
    public static final int LoveHistory_Type_Many = 4;//多次


    /**
     * 饮酒情况
     */
    public static final int Drinking_Type_JingChangYinJiu = 0;//经常饮酒
    public static final int Drinking_Type_OuErXiaoZhuo = 1;//偶尔小酌
    public static final int Drinking_Type_BuHuiHeJiu = 2;//不会喝酒
    public static final int Drinking_Type_OuErHePuTaoJiu = 3;//偶尔饮葡萄酒
    public static final int Drinking_Type_HuiHeYiXiePiJiu = 4;//会喝一些啤酒
    public static final int Drinking_Type_HuiHeYiDian = 5;//会喝一点
    public static final int Drinking_Type_XiHuanBaiJiu = 6;//喜欢白酒
    public static final int Drinking_Type_XiHuanYangJiu = 7;//喜欢洋酒
    public static final int Drinking_Type_XiHuanPiJiu = 8;//喜欢啤酒
    public static final int Drinking_Type_XiHuanZiNiangJiu = 9;//喜欢自酿酒
    public static final int Drinking_Type_XinQingBuHaoShiHe = 10;//心情不好时喝
    public static final int Drinking_Type_JuHuiYingChouShiHe = 11;//聚会应酬时喝

    /**
     * 健康自评
     */
    public static final int Health_Type_Superb = 0;//身体极好
    public static final int Health_Type_Normal = 1;//一切正常
    public static final int Health_Type_Sub_Health = 2;//有点亚健康
    public static final int Health_Type_Small_Problems = 3;//有时候有些小毛病
    public static final int Health_Type_Not_Very_Good = 4;//目前不太好


    /**
     * 婚后住房
     */
    public static final int MarriedHouse_Type_Hometown = 0;//家乡有房
    public static final int MarriedHouse_Type_BothSides = 1;//计划购买，双方共担
    public static final int MarriedHouse_Type_Own = 2;//计划购买，我肚子承担
    public static final int MarriedHouse_Type_Bought = 3;//已购房产
    public static final int MarriedHouse_Type_Parents = 4;//父母住房
    public static final int MarriedHouse_Type_Unit = 5;//单位配房

    /**
     * 结婚时间
     */

    public static final int WishMarriageTime_Type_Three_Months = 0;//三个月内结婚
    public static final int WishMarriageTime_Type_A_Year = 1;//一年内结婚
    public static final int WishMarriageTime_Type_At_Any_Time = 2;//相处好结婚


    /**
     * 学历
     */
    public static final int Education_Type_Senior_Middle_School = 0;//高中
    public static final int Education_Type_Secondary = 1;//中专
    public static final int Education_Type_College = 2;//大专
    public static final int Education_Type_Undergraduate_Course = 3;//本科
    public static final int Education_Type_Double_Degree = 4;//双学士
    public static final int Education_Type_Master_Degree = 5;//硕士
    public static final int Education_Type_Doctor = 6;//博士
    public static final int Education_Type_Postdoctoral = 7;//博士后
    /**
     * 目标对象
     */
    public static final int TargetObject_Type_Tyrant_M = 1;//土豪男
    public static final int TargetObject_Type_Overbearing_Chairman = 2;//霸道总裁
    public static final int TargetObject_Type_Rich_Handsome = 3;//高富帅
    public static final int TargetObject_Type_Female_Temperament = 4;//气质女
    public static final int TargetObject_Type_Silly_Sweet_White = 5;//傻白甜
    public static final int TargetObject_Type_Phu_Bai = 6;//白富美

    /**
     * 预计时间
     */
    public static final int PlanTime_Type_Round_Trip = 0;//当天往返
    public static final int PlanTime_Type_Twelve_Days = 1;//一两天
    public static final int PlanTime_Type_Twenty_Three_Days = 2;//两三天
    public static final int PlanTime_Type_Thirty_Five_Days = 3;//三五天
    public static final int PlanTime_Type_Days_Half = 4;//十天半月
    public static final int PlanTime_Type_Ready = 5;//还准备回来吗？
    public static final int PlanTime_Type_Will = 6;//会不会回不来？
    /**
     * 出行方式
     */
    public static final int Method_Type_Round_Trip = 0;//往返双飞
    public static final int Method_Type_EMU = 1;//动车高铁
    public static final int Method_Type_Cruise = 2;//游轮
    public static final int Method_Type_By_Car = 3;//自驾
    public static final int Method_Type_Coach = 4;//大巴
    public static final int Method_Type_Cycling = 5;//骑行


    /**
     * 费用
     */
    public static final int Cost_Type_I_All = 0;//我承担全部
    public static final int Cost_Type_AA = 1;//AA
    public static final int Cost_Type_I = 2;//来回机票我承担
    public static final int Cost_Type_Other_Side = 3;//承担对方2000以内的费用

    /**
     * 费用
     */
    public static final int TravelCost_Type_I_All = 0;//我承担全部
    public static final int TravelCost_Type_AA = 1;//AA
    public static final int TravelCost_Type_Male = 2;//希望男方买单
    public static final int TravelCost_Type_You = 3;//你丑你买单
    public static final int TravelCost_Type_Big_Chest = 4;//我胸大你承担
    public static final int TravelCost_Type_Random = 5;//剪刀石头布

    /**
     * 旅行时间
     */
    public static final int Travel_Time_Type_Recent = 0;//近期出发
    public static final int Travel_Time_Type_A_weekend = 1;//某个周末
    public static final int Travel_Time_Type_Specific_Date = 2;//具体日期
    public static final int Travel_Time_Type_Negotiable = 3;//可商议
    public static final int Travel_Time_Type_Stay_Away = 4;//说走就走

    /**
     * 买单
     */
    public static final int CostType_I = 0;//我买单
    public static final int CostType_AA = 1;//AA
    public static final int CostType_You = 2;//你买单

    /**
     * 同伴情况
     */
    public static final int Companion_Type_No_Carry = 0;//不携带
    public static final int Companion_Type_Carry_A_Girlfriends = 1;//可携带一名闺蜜
    public static final int Companion_Type_Carry_Two_Or_Three_Friends = 2;//可携带三两好友

    /**
     * 同伴情况  方案
     */
    public static final String Companion_Type_No_Carry_Str = "不携带";
    public static final String Companion_Type_Carry_A_Girlfriends_Male_Str = "对方可携带一名闺蜜";
    public static final String Companion_Type_Carry_A_Girlfriends_Female_Str = "我要携带一名闺蜜";
    public static final String Companion_Type_Carry_Two_Or_Three_Friends_Male_Str = "对方可携带两三好友";
    public static final String Companion_Type_Carry_Two_Or_Three_Friends_Female_Str = "我要携带两三好友";

    /**
     * 邀约时间类型
     */
    public static final int MeetingTime_Type_Any = 0;//不限时间
    public static final int MeetingTime_Type_On_The_Weekend = 1;//平时周末
    public static final int MeetingTime_Type_Select_Time = 2;//选择时间

    /**
     * 对象性别
     */
    public static final int TargetSex_Type_Any = 0;//不限
    public static final int TargetSex_Type_Male = 1;//男
    public static final int TargetSex_Type_Female = 2;//女
    /**
     * 接送类型
     */
    public static final int Carry_Type_No = 0;//不用接送
    public static final int Carry_Type_I_Pick = 1;//我接送
    public static final int Carry_Type_Need_I_Pick = 2;//需接送我
    /**
     * 接送类型 方案
     */
    public static final String Carry_Type_No_Str = "不用接送";
    public static final String Carry_Type_I_Pick_Str = "我接送";
    public static final String Carry_Type_Need_I_Pick_Str = "需接送我";


    /**
     * 占卜师类型
     */
    public static int CounselorType_Tarot = 0;//塔罗师
    public static int CounselorType_Astrology = 1;//占星师
    public static int CounselorType_Psychological = 2;//心理咨询师
    public static int CounselorType_Activity = 3;//活动支付


    /**
     * 咨询服务下单 咨询类型（0：塔罗 1：星盘）
     */
    public static int OrdersType_counsel_Tarot = 0;//苹果
    public static int OrdersType_counsel_Star = 1;//星钻



    /**
     *我的订单（用户）
     */
    public static int OrderType_Advisory = 0;//咨询
    public static int OrderType_Apple = 1;//苹果
    public static int OrderType_Star = 2;//星钻


     /**
     * 商品列表类型
     */
    public static int ProductType_Apple = 0;//苹果
    public static int ProductType_Star = 1;//星钻



     /**
     * 商品下单类型（商品下单（购买苹果、星钻）1：苹果 2：星钻）
     */
    public static int Orders_ProductType_Apple = 1;//苹果

    public static int Orders_ProductType_Star = 2;//星钻




}
