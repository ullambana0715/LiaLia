package cn.chono.yopper.data;

/**
 * Created by sunquan on 16/6/14.
 */
public class LikeDto {

    private boolean isActivityExpert;

    // V2.5.4 vip身份类型  ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    private int currentUserPosition;

    private ActivityDto activity;

    private LikeDatingDto dating;

    private User userInfo;


    public boolean isActivityExpert() {
        return isActivityExpert;
    }

    public void setActivityExpert(boolean activityExpert) {
        isActivityExpert = activityExpert;
    }

    public int getCurrentUserPosition() {
        return currentUserPosition;
    }

    public void setCurrentUserPosition(int currentUserPosition) {
        this.currentUserPosition = currentUserPosition;
    }

    public ActivityDto getActivity() {
        return activity;
    }

    public void setActivity(ActivityDto activity) {
        this.activity = activity;
    }

    public LikeDatingDto getDating() {
        return dating;
    }

    public void setDating(LikeDatingDto dating) {
        this.dating = dating;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    public class ActivityDto {

        public String activityId;

        public String activityName;


        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }
    }


    public class LikeDatingDto {

        public String datingId;

        public String datingName;


        public String getDatingId() {
            return datingId;
        }

        public void setDatingId(String datingId) {
            this.datingId = datingId;
        }

        public String getDatingName() {
            return datingName;
        }

        public void setDatingName(String datingName) {
            this.datingName = datingName;
        }
    }

}
