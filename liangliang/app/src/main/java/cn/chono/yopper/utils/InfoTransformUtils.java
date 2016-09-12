package cn.chono.yopper.utils;

import android.text.TextUtils;

import cn.chono.yopper.common.Constant;

/**
 * Created by cc on 16/3/31.
 */
public class InfoTransformUtils {

    /**
     * 父母健在情况
     *
     * @param parentsBeing
     * @return
     */
    public static String getParentsBeingAlive(int parentsBeing) {
        String parents = "";

        switch (parentsBeing) {
            case Constant.Parents_Type_Parents:
                parents = "双亲健在";
                break;
            case Constant.Parents_Type_Mother:
                parents = "父亲健在";
                break;
            case Constant.Parents_Type_Father:
                parents = "母亲健在";
                break;
        }

        return parents;
    }


    /**
     * 父母健在情况
     *
     * @param parents
     * @return
     */
    public static int getParentsBeingAlive(String parents) {
        int parentsType = 0;

        if (TextUtils.equals("双亲健在", parents)) {
            parentsType = Constant.Parents_Type_Parents;

        } else if (TextUtils.equals("父亲健在", parents)) {
            parentsType = Constant.Parents_Type_Mother;

        } else if (TextUtils.equals("母亲健在", parents)) {
            parentsType = Constant.Parents_Type_Father;
        }

        return parentsType;
    }

    /**
     * 收入情况
     *
     * @param incomeType
     * @return
     */

    public static String getIncome(int incomeType) {
        String income = "";


        switch (incomeType) {


            case Constant.Income_Type_secrecy:

                income = "保密";

                break;
            case Constant.Income_Type_3000down:

                income = "3000以下";

                break;
            case Constant.Income_Type_3000up:

                income = "3000以上";

                break;
            case Constant.Income_Type_5000up:

                income = "5000以上";

                break;
            case Constant.Income_Type_10000up:

                income = "10000以上";

                break;
            case Constant.Income_Type_20000up:

                income = "20000以上";

                break;
            case Constant.Income_Type_50000up:

                income = "50000以上";

                break;
        }

        return income;
    }

    /**
     * 收入情况
     *
     * @param income
     * @return
     */
    public static int getIncome(String income) {
        int incomeType = 1;
        if (TextUtils.equals("保密", income))
            incomeType = Constant.Income_Type_secrecy;
        else if (TextUtils.equals("3000以下", income)) {
            incomeType = Constant.Income_Type_3000down;
        } else if (TextUtils.equals("3000以上", income)) {
            incomeType = Constant.Income_Type_3000up;
        } else if (TextUtils.equals("5000以上", income)) {
            incomeType = Constant.Income_Type_5000up;
        } else if (TextUtils.equals("10000以上", income)) {
            incomeType = Constant.Income_Type_10000up;
        } else if (TextUtils.equals("20000以上", income)) {
            incomeType = Constant.Income_Type_20000up;
        } else if (TextUtils.equals("50000以上", income)) {
            incomeType = Constant.Income_Type_50000up;
        }


        return incomeType;
    }

    /**
     * 收入情况
     *
     * @param incomeType
     * @return
     */

    public static String getPersonalsIncome(int incomeType) {
        String income = "";


        switch (incomeType) {

            case Constant.Income_Type_3000down:

                income = "3000以下";

                break;
            case Constant.Income_Type_3000up:

                income = "3000以上";

                break;
            case Constant.Income_Type_5000up:

                income = "5000以上";

                break;
            case Constant.Income_Type_10000up:

                income = "10000以上";

                break;
            case Constant.Income_Type_20000up:

                income = "20000以上";

                break;
            case Constant.Income_Type_50000up:

                income = "50000以上";

                break;
        }

        return income;
    }


    public static int getPersonalsIncome(String income) {
        int incomeType = 1;

        if (TextUtils.equals("3000以下", income)) {
            incomeType = Constant.Income_Type_3000down;
        } else if (TextUtils.equals("3000以上", income)) {
            incomeType = Constant.Income_Type_3000up;
        } else if (TextUtils.equals("5000以上", income)) {
            incomeType = Constant.Income_Type_5000up;
        } else if (TextUtils.equals("10000以上", income)) {
            incomeType = Constant.Income_Type_10000up;
        } else if (TextUtils.equals("20000以上", income)) {
            incomeType = Constant.Income_Type_20000up;
        } else if (TextUtils.equals("50000以上", income)) {
            incomeType = Constant.Income_Type_50000up;
        }


        return incomeType;
    }


    /**
     * 恋爱史
     *
     * @param loveHistoryType
     * @return
     */
    public static String getLoveHistory(int loveHistoryType) {

        String loveHistory = "";

        switch (loveHistoryType) {
            case Constant.LoveHistory_Type_No:
                loveHistory = "恋爱啥滋味";
                break;
            case Constant.LoveHistory_Type_One:
                loveHistory = "有过一次恋爱经历";
                break;
            case Constant.LoveHistory_Type_Two:
                loveHistory = "有过两次恋爱经历";
                break;
            case Constant.LoveHistory_Type_Three:
                loveHistory = "有过三次恋爱经历";
                break;
            case Constant.LoveHistory_Type_Many:
                loveHistory = "有过多次恋爱经历";
                break;


        }

        return loveHistory;

    }

    /**
     * 恋爱史
     *
     * @param loveHistory
     * @return
     */
    public static int getLoveHistory(String loveHistory) {

        int loveHistoryType = 0;

        if (TextUtils.equals("恋爱啥滋味", loveHistory)) {
            loveHistoryType = Constant.LoveHistory_Type_No;
        } else if (TextUtils.equals("有过一次恋爱经历", loveHistory)) {
            loveHistoryType = Constant.LoveHistory_Type_One;
        } else if (TextUtils.equals("有过两次恋爱经历", loveHistory)) {
            loveHistoryType = Constant.LoveHistory_Type_Two;
        } else if (TextUtils.equals("有过三次恋爱经历", loveHistory)) {
            loveHistoryType = Constant.LoveHistory_Type_Three;
        } else if (TextUtils.equals("有过多次恋爱经历", loveHistory)) {
            loveHistoryType = Constant.LoveHistory_Type_Many;
        }

        return loveHistoryType;

    }

    /**
     * 子女情况
     *
     * @param children
     * @return
     */
    public static String getChildrenCondition(int children) {

        String child = "";

        switch (children) {
            case Constant.Child_Type_No:
                child = "无";
                break;
            case Constant.Child_Type_Have:
                child = "有";
                break;
            case Constant.Child_Type_Have_I:
                child = "有/孩子跟我";
                break;
            case Constant.Child_Type_Have_Parents:
                child = "有/孩子跟我父母";
                break;
            case Constant.Child_Type_Have_Other_Side:
                child = "孩子跟对方";
                break;
        }

        return child;

    }

    /**
     * 子女情况
     *
     * @param child
     * @return
     */
    public static int getChildrenCondition(String child) {
        int children = 0;

        if (TextUtils.equals("无", child)) {
            children = Constant.Child_Type_No;
        } else if (TextUtils.equals("有", child)) {
            children = Constant.Child_Type_Have;
        } else if (TextUtils.equals("有/孩子跟我", child)) {
            children = Constant.Child_Type_Have_I;
        } else if (TextUtils.equals("有/孩子跟我父母", child)) {
            children = Constant.Child_Type_Have_Parents;
        } else if (TextUtils.equals("孩子跟对方", child)) {
            children = Constant.Child_Type_Have_Other_Side;
        }

        return children;
    }

    /**
     * 饮酒情况
     *
     * @param drinkingType
     * @return
     */
    public static String getDrinkConditions(int drinkingType) {

        String drinking = "";
        switch (drinkingType) {

            case Constant.Drinking_Type_JingChangYinJiu:
                drinking = "经常饮酒";
                break;
            case Constant.Drinking_Type_OuErXiaoZhuo:
                drinking = "偶尔小酌";
                break;
            case Constant.Drinking_Type_BuHuiHeJiu:
                drinking = "不会喝酒";
                break;
            case Constant.Drinking_Type_OuErHePuTaoJiu:
                drinking = "偶尔饮葡萄酒";
                break;
            case Constant.Drinking_Type_HuiHeYiXiePiJiu:
                drinking = "会喝一些啤酒";
                break;
            case Constant.Drinking_Type_HuiHeYiDian:
                drinking = "会喝一点";
                break;
            case Constant.Drinking_Type_XiHuanBaiJiu:
                drinking = "喜欢白酒";
                break;
            case Constant.Drinking_Type_XiHuanYangJiu:
                drinking = "喜欢洋酒";
                break;
            case Constant.Drinking_Type_XiHuanPiJiu:
                drinking = "喜欢啤酒";
                break;
            case Constant.Drinking_Type_XiHuanZiNiangJiu:
                drinking = "喜欢自酿酒";
                break;
            case Constant.Drinking_Type_XinQingBuHaoShiHe:
                drinking = "心情不好时喝";
                break;
            case Constant.Drinking_Type_JuHuiYingChouShiHe:
                drinking = "聚会应酬时喝";
                break;

        }

        return drinking;

    }

    /**
     * 饮酒情况
     *
     * @param drinking
     * @return
     */
    public static Integer getDrinkConditions(String drinking) {

        Integer drinkingType = null;


        if (TextUtils.equals("经常饮酒", drinking)) {
            drinkingType = Constant.Drinking_Type_JingChangYinJiu;
        } else if (TextUtils.equals("偶尔小酌", drinking)) {
            drinkingType = Constant.Drinking_Type_OuErXiaoZhuo;
        } else if (TextUtils.equals("不会喝酒", drinking)) {
            drinkingType = Constant.Drinking_Type_BuHuiHeJiu;
        } else if (TextUtils.equals("偶尔饮葡萄酒", drinking)) {
            drinkingType = Constant.Drinking_Type_OuErHePuTaoJiu;
        } else if (TextUtils.equals("会喝一些啤酒", drinking)) {
            drinkingType = Constant.Drinking_Type_HuiHeYiXiePiJiu;
        } else if (TextUtils.equals("会喝一点", drinking)) {
            drinkingType = Constant.Drinking_Type_HuiHeYiDian;
        } else if (TextUtils.equals("喜欢白酒", drinking)) {
            drinkingType = Constant.Drinking_Type_XiHuanBaiJiu;
        } else if (TextUtils.equals("喜欢洋酒", drinking)) {
            drinkingType = Constant.Drinking_Type_XiHuanYangJiu;
        } else if (TextUtils.equals("喜欢啤酒", drinking)) {
            drinkingType = Constant.Drinking_Type_XiHuanPiJiu;
        } else if (TextUtils.equals("喜欢自酿酒", drinking)) {
            drinkingType = Constant.Drinking_Type_XiHuanZiNiangJiu;
        } else if (TextUtils.equals("心情不好时喝", drinking)) {
            drinkingType = Constant.Drinking_Type_XinQingBuHaoShiHe;
        } else if (TextUtils.equals("聚会应酬时喝", drinking)) {
            drinkingType = Constant.Drinking_Type_JuHuiYingChouShiHe;
        }

        return drinkingType;

    }

    /**
     * 健康自评
     *
     * @param healthType
     * @return
     */
    public static String getHealthCondition(int healthType) {

        String health = "";

        switch (healthType) {

            case Constant.Health_Type_Superb:
                health = "身体极好";

                break;

            case Constant.Health_Type_Normal:
                health = "一切正常";

                break;
            case Constant.Health_Type_Sub_Health:
                health = "有点亚健康";

                break;

            case Constant.Health_Type_Small_Problems:
                health = "有时候有些小毛病";

                break;

            case Constant.Health_Type_Not_Very_Good:
                health = "目前不太好";

                break;


        }
        return health;
    }

    /**
     * 健康自评
     *
     * @param health
     * @return
     */

    public static int getHealthCondition(String health) {
        int healthType = 0;

        if (TextUtils.equals("身体极好", health)) {
            healthType = Constant.Health_Type_Superb;
        } else if (TextUtils.equals("一切正常", health)) {
            healthType = Constant.Health_Type_Normal;
        } else if (TextUtils.equals("有点亚健康", health)) {
            healthType = Constant.Health_Type_Sub_Health;
        } else if (TextUtils.equals("有时候有些小毛病", health)) {
            healthType = Constant.Health_Type_Small_Problems;
        } else if (TextUtils.equals("目前不太好", health)) {
            healthType = Constant.Health_Type_Not_Very_Good;
        }

        return healthType;
    }

    /**
     * 婚后住房
     *
     * @param marriedHouseType
     * @return
     */
    public static String getMarriedHouseCondition(int marriedHouseType) {

        String marriedHouse = "";
        switch (marriedHouseType) {

            case Constant.MarriedHouse_Type_Hometown:
                marriedHouse = "家乡有房，但在工作地租住";
                break;
            case Constant.MarriedHouse_Type_BothSides:
                marriedHouse = "计划购买，双方共担";
                break;
            case Constant.MarriedHouse_Type_Own:
                marriedHouse = "计划购买，我独自承担";
                break;
            case Constant.MarriedHouse_Type_Bought:
                marriedHouse = "已购房产";
                break;
            case Constant.MarriedHouse_Type_Parents:
                marriedHouse = "父母住";
                break;
            case Constant.MarriedHouse_Type_Unit:
                marriedHouse = "单位配房";
                break;
        }

        return marriedHouse;
    }

    /**
     * 婚后住房
     *
     * @param marriedHouse
     * @return
     */
    public static int getMarriedHouseCondition(String marriedHouse) {

        int marriedHouseType = 0;


        if (TextUtils.equals("家乡有房，但在工作地租住", marriedHouse)) {
            marriedHouseType = Constant.MarriedHouse_Type_Hometown;
        } else if (TextUtils.equals("计划购买，双方共担", marriedHouse)) {
            marriedHouseType = Constant.MarriedHouse_Type_BothSides;
        } else if (TextUtils.equals("计划购买，我独自承担", marriedHouse)) {
            marriedHouseType = Constant.MarriedHouse_Type_Own;
        } else if (TextUtils.equals("已购房产", marriedHouse)) {
            marriedHouseType = Constant.MarriedHouse_Type_Bought;
        } else if (TextUtils.equals("父母住房", marriedHouse)) {
            marriedHouseType = Constant.MarriedHouse_Type_Parents;
        } else if (TextUtils.equals("单位配房", marriedHouse)) {
            marriedHouseType = Constant.MarriedHouse_Type_Unit;
        }

        return marriedHouseType;

    }


    /**
     * 结婚时间
     *
     * @param wishMarriageTimeType
     * @return
     */
    public static String getPublishWishMarriageTime(int wishMarriageTimeType) {

        String wishMarriageTime = "";

        switch (wishMarriageTimeType) {

            case Constant.WishMarriageTime_Type_A_Year:
                wishMarriageTime = "希望一年内结婚";
                break;
            case Constant.WishMarriageTime_Type_Three_Months:
                wishMarriageTime = "希望三个月内结婚";
                break;
            case Constant.WishMarriageTime_Type_At_Any_Time:
                wishMarriageTime = "相处好就结婚";
                break;

        }


        return wishMarriageTime;
    }

    /**
     * 结婚时间
     *
     * @param wishMarriageTime
     * @return
     */
    public static int getPublishWishMarriageTime(String wishMarriageTime) {

        int wishMarriageTimeType = 0;

        if (TextUtils.equals("希望一年内结婚", wishMarriageTime)) {
            wishMarriageTimeType = Constant.WishMarriageTime_Type_A_Year;
        } else if (TextUtils.equals("希望三个月内结婚", wishMarriageTime)) {
            wishMarriageTimeType = Constant.WishMarriageTime_Type_Three_Months;
        } else if (TextUtils.equals("相处好就结婚", wishMarriageTime)) {
            wishMarriageTimeType = Constant.WishMarriageTime_Type_At_Any_Time;
        }


        return wishMarriageTimeType;
    }

    /**
     * 结婚时间
     *
     * @param wishMarriageTimeType
     * @return
     */
    public static String getWishMarriageTime(int wishMarriageTimeType) {

        String wishMarriageTime = "";

        switch (wishMarriageTimeType) {

            case Constant.WishMarriageTime_Type_A_Year:
                wishMarriageTime = "一年内结婚";
                break;
            case Constant.WishMarriageTime_Type_Three_Months:
                wishMarriageTime = "三个月内结婚";
                break;
            case Constant.WishMarriageTime_Type_At_Any_Time:
                wishMarriageTime = "相处好结婚";
                break;

        }


        return wishMarriageTime;
    }

    /**
     * 结婚时间
     *
     * @param wishMarriageTime
     * @return
     */
    public static int getWishMarriageTime(String wishMarriageTime) {

        int wishMarriageTimeType = 0;

        if (TextUtils.equals("一年内结婚", wishMarriageTime)) {
            wishMarriageTimeType = Constant.WishMarriageTime_Type_A_Year;
        } else if (TextUtils.equals("三个月内结婚", wishMarriageTime)) {
            wishMarriageTimeType = Constant.WishMarriageTime_Type_Three_Months;
        } else if (TextUtils.equals("相处好结婚", wishMarriageTime)) {
            wishMarriageTimeType = Constant.WishMarriageTime_Type_At_Any_Time;
        }


        return wishMarriageTimeType;
    }


    /**
     * 学历
     *
     * @param educations
     * @return
     */
    public static String getEducation(int educations) {

        String education = "";

        switch (educations) {
            case Constant.Education_Type_Senior_Middle_School:
                education = "高中";
                break;
            case Constant.Education_Type_Secondary:
                education = "中专";
                break;
            case Constant.Education_Type_College:
                education = "大专";
                break;
            case Constant.Education_Type_Undergraduate_Course:
                education = "本科";
                break;
            case Constant.Education_Type_Double_Degree:
                education = "双学士";
                break;
            case Constant.Education_Type_Master_Degree:
                education = "硕士";
                break;
            case Constant.Education_Type_Doctor:
                education = "博士";
                break;
            case Constant.Education_Type_Postdoctoral:
                education = "博士后";
                break;
        }

        return education;
    }

    /**
     * 学历
     *
     * @param education
     * @return
     */
    public static int getEducation(String education) {

        int educations = 0;

        if (TextUtils.equals("高中", education)) {
            educations = Constant.Education_Type_Senior_Middle_School;
        } else if (TextUtils.equals("中专", education)) {
            educations = Constant.Education_Type_Secondary;
        } else if (TextUtils.equals("大专", education)) {
            educations = Constant.Education_Type_College;
        } else if (TextUtils.equals("本科", education)) {
            educations = Constant.Education_Type_Undergraduate_Course;
        } else if (TextUtils.equals("双学士", education)) {
            educations = Constant.Education_Type_Double_Degree;
        } else if (TextUtils.equals("硕士", education)) {
            educations = Constant.Education_Type_Master_Degree;
        } else if (TextUtils.equals("博士", education)) {
            educations = Constant.Education_Type_Doctor;
        } else if (TextUtils.equals("博士后", education)) {
            educations = Constant.Education_Type_Postdoctoral;
        }


        return educations;
    }


    /**
     * 目标对象
     *
     * @param object
     * @return
     */
    public static String getTargetObject(int object) {

        String targetObject = "";

        switch (object) {
            case Constant.TargetObject_Type_Tyrant_M:
                targetObject = "土豪男";
                break;
            case Constant.TargetObject_Type_Overbearing_Chairman:
                targetObject = "霸道总裁";
                break;
            case Constant.TargetObject_Type_Rich_Handsome:
                targetObject = "高富帅";
                break;
            case Constant.TargetObject_Type_Female_Temperament:
                targetObject = "气质女";
                break;
            case Constant.TargetObject_Type_Silly_Sweet_White:
                targetObject = "傻白甜";
                break;
            case Constant.TargetObject_Type_Phu_Bai:
                targetObject = "白富美";
                break;
        }

        return targetObject;

    }

    /**
     * 目标对象
     *
     * @param targetObject
     * @return
     */
    public static int getTargetObject(String targetObject) {

        int object = 0;

        if (TextUtils.equals("土豪男", targetObject)) {
            object = Constant.TargetObject_Type_Tyrant_M;
        } else if (TextUtils.equals("霸道总裁", targetObject)) {
            object = Constant.TargetObject_Type_Overbearing_Chairman;
        } else if (TextUtils.equals("高富帅", targetObject)) {
            object = Constant.TargetObject_Type_Rich_Handsome;
        } else if (TextUtils.equals("气质女", targetObject)) {
            object = Constant.TargetObject_Type_Female_Temperament;
        } else if (TextUtils.equals("傻白甜", targetObject)) {
            object = Constant.TargetObject_Type_Silly_Sweet_White;
        } else if (TextUtils.equals("白富美", targetObject)) {
            object = Constant.TargetObject_Type_Phu_Bai;
        }


        return object;

    }



    /**
     * 获取旅游的目标对象性别
     *
     * @param object
     * @return
     */
    public static int getTravleTargetSex(int object) {

        int targetSex =0;

        switch (object) {
            case Constant.TargetObject_Type_Tyrant_M:
                targetSex=1;
                break;
            case Constant.TargetObject_Type_Overbearing_Chairman:
                targetSex=1;
                break;
            case Constant.TargetObject_Type_Rich_Handsome:
                targetSex=1;
                break;
            case Constant.TargetObject_Type_Female_Temperament:
                targetSex=2;
                break;
            case Constant.TargetObject_Type_Silly_Sweet_White:
                targetSex=2;
                break;
            case Constant.TargetObject_Type_Phu_Bai:
                targetSex=2;
                break;
        }

        return targetSex;

    }



    /**
     * 预计时间
     *
     * @param timeType
     * @return
     */
    public static String getPlanTime(int timeType) {

        String planTime = "";

        switch (timeType) {

            case Constant.PlanTime_Type_Round_Trip:
                planTime = "当天往返";
                break;

            case Constant.PlanTime_Type_Twelve_Days:
                planTime = "一两天";
                break;

            case Constant.PlanTime_Type_Twenty_Three_Days:
                planTime = "两三天";
                break;

            case Constant.PlanTime_Type_Thirty_Five_Days:
                planTime = "三五天";
                break;

            case Constant.PlanTime_Type_Days_Half:
                planTime = "十天半月";
                break;

            case Constant.PlanTime_Type_Ready:
                planTime = "还准备回来吗？";
                break;

            case Constant.PlanTime_Type_Will:
                planTime = "会不会回不来？";
                break;

        }

        return planTime;

    }

    /**
     * 预计时间
     *
     * @param planTime
     * @return
     */
    public static int getPlanTime(String planTime) {

        int timeType = 0;

        if (TextUtils.equals("当天往返", planTime)) {
            timeType = Constant.PlanTime_Type_Round_Trip;
        } else if (TextUtils.equals("一两天", planTime)) {
            timeType = Constant.PlanTime_Type_Twelve_Days;
        } else if (TextUtils.equals("两三天", planTime)) {
            timeType = Constant.PlanTime_Type_Twenty_Three_Days;
        } else if (TextUtils.equals("三五天", planTime)) {
            timeType = Constant.PlanTime_Type_Thirty_Five_Days;
        } else if (TextUtils.equals("十天半月", planTime)) {
            timeType = Constant.PlanTime_Type_Days_Half;
        } else if (TextUtils.equals("还准备回来吗？", planTime)) {
            timeType = Constant.PlanTime_Type_Ready;
        } else if (TextUtils.equals("会不会回不来？", planTime)) {
            timeType = Constant.PlanTime_Type_Will;
        }


        return timeType;

    }


    /**
     * 出行方式
     *
     * @param methods
     * @return
     */
    public static String getMethod(int methods) {

        String method = "";


        switch (methods) {

            case Constant.Method_Type_Round_Trip:
                method = "往返双飞";
                break;

            case Constant.Method_Type_EMU:
                method = "动车高铁";
                break;

            case Constant.Method_Type_Cruise:
                method = "游轮";
                break;

            case Constant.Method_Type_By_Car:
                method = "自驾";
                break;

            case Constant.Method_Type_Coach:
                method = "大巴";
                break;

            case Constant.Method_Type_Cycling:
                method = "骑行";
                break;
        }

        return method;


    }

    /**
     * 出行方式
     *
     * @param method
     * @return
     */
    public static int getMethod(String method) {

        int methods = 0;

        if (TextUtils.equals("往返双飞", method)) {
            methods = Constant.Method_Type_Round_Trip;
        } else if (TextUtils.equals("动车高铁", method)) {
            methods = Constant.Method_Type_EMU;
        } else if (TextUtils.equals("游轮", method)) {
            methods = Constant.Method_Type_Cruise;
        } else if (TextUtils.equals("自驾", method)) {
            methods = Constant.Method_Type_By_Car;
        } else if (TextUtils.equals("大巴", method)) {
            methods = Constant.Method_Type_Coach;
        } else if (TextUtils.equals("骑行", method)) {
            methods = Constant.Method_Type_Cycling;
        }


        return methods;


    }


    /**
     * 费用
     *
     * @param travelCostType
     * @return
     */
    public static String getTravelCostType(int travelCostType, int sex) {

        String travelCost = "";

        switch (travelCostType) {

            case Constant.TravelCost_Type_I_All:
                travelCost = "我承担全部";
                break;
            case Constant.TravelCost_Type_AA:
                travelCost = "AA";
                break;
            case Constant.TravelCost_Type_Male:

                if (sex == 1) {
                    travelCost = "来回机票我承担";
                } else if (sex == 2) {
                    travelCost = "希望男方买单";
                } else {
                    travelCost = "";
                }


                break;
            case Constant.TravelCost_Type_You:

                if (sex == 1) {
                    travelCost = "承担对方2000以内的费用";
                } else if (sex == 2) {
                    travelCost = "你丑你买单";
                } else {
                    travelCost = "";
                }


                break;
            case Constant.TravelCost_Type_Big_Chest:

                if (sex == 1) {
                    travelCost = "承担对方3000以内的费用";
                } else if (sex == 2) {
                    travelCost = "我胸大你承担";
                } else {
                    travelCost = "";
                }

                break;
            case Constant.TravelCost_Type_Random:

                if (sex == 1) {

                    travelCost = "承担对方5000以内的费用";

                } else if (sex == 2) {
                    travelCost = "剪刀石头布";
                } else {
                    travelCost = "";
                }


                break;

        }


        return travelCost;
    }

    /**
     * 费用
     *
     * @param travelCost
     * @return
     */
    public static int getTravelCostType(String travelCost) {

        int travelCostType = 0;


        if (TextUtils.equals("我承担全部", travelCost)) {
            travelCostType = Constant.TravelCost_Type_I_All;
        } else if (TextUtils.equals("AA", travelCost)) {
            travelCostType = Constant.TravelCost_Type_AA;
        } else if (TextUtils.equals("希望男方买单", travelCost) || TextUtils.equals("来回机票我承担", travelCost)) {
            travelCostType = Constant.TravelCost_Type_Male;
        } else if (TextUtils.equals("你丑你买单", travelCost) || TextUtils.equals("承担对方2000以内的费用", travelCost)) {
            travelCostType = Constant.TravelCost_Type_You;
        } else if (TextUtils.equals("我胸大你承担", travelCost) || TextUtils.equals("承担对方3000以内的费用", travelCost)) {
            travelCostType = Constant.TravelCost_Type_Big_Chest;
        } else if (TextUtils.equals("剪刀石头布", travelCost) || TextUtils.equals("承担对方5000以内的费用", travelCost)) {
            travelCostType = Constant.TravelCost_Type_Random;
        }


        return travelCostType;
    }


    /**
     * 邀约旅行时间
     *
     * @param timeType
     * @return
     */
    public static String getMeetingTravelTimeType(int timeType) {

        String meetingTime = "";

        switch (timeType) {
            case Constant.Travel_Time_Type_Recent:
                meetingTime = "近期出发";
                break;
            case Constant.Travel_Time_Type_A_weekend:
                meetingTime = "某个周末";
                break;
            case Constant.Travel_Time_Type_Specific_Date:
                meetingTime = "具体日期";
                break;
            case Constant.Travel_Time_Type_Negotiable:
                meetingTime = "可商议";
                break;
            case Constant.Travel_Time_Type_Stay_Away:
                meetingTime = "说走就走";
                break;

        }


        return meetingTime;


    }

    /**
     * 邀约旅行时间
     *
     * @param meetingTime
     * @return
     */
    public static int getMeetingTravelTimeType(String meetingTime) {

        int timeType = 0;

        if (TextUtils.equals("近期出发", meetingTime)) {
            timeType = Constant.Travel_Time_Type_Recent;
        } else if (TextUtils.equals("某个周末", meetingTime)) {
            timeType = Constant.Travel_Time_Type_A_weekend;
        } else if (TextUtils.equals("具体日期", meetingTime)) {
            timeType = Constant.Travel_Time_Type_Specific_Date;
        } else if (TextUtils.equals("可商议", meetingTime)) {
            timeType = Constant.Travel_Time_Type_Negotiable;
        } else if (TextUtils.equals("说走就走", meetingTime)) {
            timeType = Constant.Travel_Time_Type_Stay_Away;
        }


        return timeType;


    }


    /**
     * 买单
     *
     * @param costType
     * @return
     */
    public static String getCostType(int costType) {

        String cost = "";

        switch (costType) {
            case Constant.CostType_I:

                cost = "我买单";

                break;
            case Constant.CostType_AA:

                cost = "AA";

                break;
            case Constant.CostType_You:

                cost = "你买单";

                break;

        }

        return cost;

    }

    /**
     * 买单
     *
     * @param cost
     * @return
     */
    public static int getCostType(String cost) {

        int costType = 0;

        if (TextUtils.equals("我买单", cost)) {
            costType = Constant.CostType_I;
        } else if (TextUtils.equals("AA", cost)) {
            costType = Constant.CostType_AA;
        } else if (TextUtils.equals("你买单", cost)) {
            costType = Constant.CostType_You;
        }


        return costType;

    }


    /**
     * 邀约详情同伴情况
     *
     * @param companionConditionType
     * @param sex                    1男。2女
     * @return
     */
    public static String getDatingDetailCompanionCondition(int companionConditionType, int sex) {

        String companionCondition = "";

        switch (companionConditionType) {

            case Constant.Companion_Type_No_Carry:

                companionCondition = "";

                break;


            case Constant.Companion_Type_Carry_A_Girlfriends:

                if (1 == sex) {
                    companionCondition = "可携带一名闺蜜";

                } else if (2 == sex) {
                    companionCondition = "我要携带一名闺蜜";
                } else {
                    companionCondition = "";
                }


                break;


            case Constant.Companion_Type_Carry_Two_Or_Three_Friends:

                if (1 == sex) {
                    companionCondition = "可携带两三好友";

                } else if (2 == sex) {
                    companionCondition = "我要携带两三好友";
                } else {
                    companionCondition = "";
                }

                break;

        }
        return companionCondition;
    }


    /**
     * 同伴情况
     *
     * @param companionConditionType
     * @param sex                    1男。2女
     * @return
     */
    public static String getCompanionCondition(int companionConditionType, int sex) {

        String companionCondition = "";

        switch (companionConditionType) {

            case Constant.Companion_Type_No_Carry:

                companionCondition = "";

                break;


            case Constant.Companion_Type_Carry_A_Girlfriends:

                if (1 == sex) {
                    companionCondition = Constant.Companion_Type_Carry_A_Girlfriends_Male_Str;

                } else if (2 == sex) {
                    companionCondition = Constant.Companion_Type_Carry_A_Girlfriends_Female_Str;
                } else {
                    companionCondition = "";
                }


                break;


            case Constant.Companion_Type_Carry_Two_Or_Three_Friends:

                if (1 == sex) {
                    companionCondition = Constant.Companion_Type_Carry_Two_Or_Three_Friends_Male_Str;

                } else if (2 == sex) {
                    companionCondition = Constant.Companion_Type_Carry_Two_Or_Three_Friends_Female_Str;
                } else {
                    companionCondition = "";
                }

                break;


        }
        return companionCondition;
    }

    /**
     * 同伴情况
     *
     * @param companionCondition
     * @return
     */
    public static int getCompanionCondition(String companionCondition) {

        int companionConditionType = 0;

        if (TextUtils.equals(Constant.Companion_Type_No_Carry_Str, companionCondition)) {
            companionConditionType = Constant.Companion_Type_No_Carry;
        } else if (TextUtils.equals(Constant.Companion_Type_Carry_A_Girlfriends_Male_Str, companionCondition) || TextUtils.equals(Constant.Companion_Type_Carry_A_Girlfriends_Female_Str, companionCondition)) {
            companionConditionType = Constant.Companion_Type_Carry_A_Girlfriends;
        } else if (TextUtils.equals(Constant.Companion_Type_Carry_Two_Or_Three_Friends_Male_Str, companionCondition) || TextUtils.equals(Constant.Companion_Type_Carry_Two_Or_Three_Friends_Female_Str, companionCondition)) {
            companionConditionType = Constant.Companion_Type_Carry_Two_Or_Three_Friends;
        }


        return companionConditionType;
    }


    /**
     * 邀约时间类型
     *
     * @param meetingTimeType
     * @return
     */
    public static String getMeetingTime(int meetingTimeType) {

        String meetingTime = "";

        switch (meetingTimeType) {

            case Constant.MeetingTime_Type_Any:
                meetingTime = "不限时间";

                break;

            case Constant.MeetingTime_Type_On_The_Weekend:
                meetingTime = "平时周末";

                break;

            case Constant.MeetingTime_Type_Select_Time:
                meetingTime = "选择时间";

                break;

        }

        return meetingTime;

    }

    /**
     * 邀约时间类型
     *
     * @param meetingTime
     * @return
     */
    public static int getMeetingTime(String meetingTime) {

        int meetingTimeType = 0;

        if (TextUtils.equals("不限时间", meetingTime)) {
            meetingTimeType = Constant.MeetingTime_Type_Any;
        } else if (TextUtils.equals("平时周末", meetingTime)) {
            meetingTimeType = Constant.MeetingTime_Type_On_The_Weekend;
        } else if (TextUtils.equals("选择时间", meetingTime)) {
            meetingTimeType = Constant.MeetingTime_Type_Select_Time;
        }


        return meetingTimeType;

    }


    /**
     * 对象性别
     *
     * @param targetSexType
     * @return
     */
    public static String getTargetSex(int targetSexType) {

        String sex = "";

        switch (targetSexType) {

            case Constant.TargetSex_Type_Any:

                sex = "不限";

                break;

            case Constant.TargetSex_Type_Male:

                sex = "男";

                break;

            case Constant.TargetSex_Type_Female:

                sex = "女";

                break;

        }

        return sex;
    }

    /**
     * 对象性别
     *
     * @param sex
     * @return
     */
    public static int getTargetSex(String sex) {

        int targetSexType = 0;

        if (TextUtils.equals("不限", sex)) {
            targetSexType = Constant.TargetSex_Type_Any;
        } else if (TextUtils.equals("男", sex)) {
            targetSexType = Constant.TargetSex_Type_Male;
        } else if (TextUtils.equals("女", sex)) {
            targetSexType = Constant.TargetSex_Type_Female;
        }


        return targetSexType;
    }


    /**
     * 接送类型
     *
     * @param carryType
     * @return
     */
    public static String getCarry(int carryType) {

        String carry = "";

        switch (carryType) {

            case Constant.Carry_Type_No:
                carry = "";
                break;
            case Constant.Carry_Type_I_Pick:
                carry = "我接送";
                break;
            case Constant.Carry_Type_Need_I_Pick:
                carry = "需接送我";
                break;

        }

        return carry;
    }

    /**
     * 接送类型
     *
     * @param carry
     * @return
     */
    public static int getCarry(String carry) {

        int carryType = 0;


        if (TextUtils.equals("不用接送", carry)) {
            carryType = Constant.Carry_Type_No;
        } else if (TextUtils.equals("我接送", carry)) {
            carryType = Constant.Carry_Type_I_Pick;
        } else if (TextUtils.equals("需接送我", carry)) {
            carryType = Constant.Carry_Type_Need_I_Pick;
        }


        return carryType;
    }


}
