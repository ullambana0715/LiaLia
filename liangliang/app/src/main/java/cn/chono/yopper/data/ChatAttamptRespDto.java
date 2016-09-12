package cn.chono.yopper.data;

public class ChatAttamptRespDto {

    //"#viewStatus": "Error = 0, // 出错， 标准出错提示  Success = 1, // 成功，不显示提示   HeadImageRequired = 2, // 需要上传头像，提示1
    // ChatWithVideoUserOnlyDennied = 3, // 对方拒绝接收非视频认证用户消息      VideoAndPointsRequired = 4, // 需要视频认证消耗P果，提示2 PointsRequired = 5, // 需要消耗P果",

    private int viewStatus;
    private boolean success;

    // "#message": "显示的提示消息",
    private String message;
    //"#pointsNeeded": "是否需要P果继续（>0)，P果的数量",
    private int pointsNeeded;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPointsNeeded() {
        return pointsNeeded;
    }

    public void setPointsNeeded(int pointsNeeded) {
        this.pointsNeeded = pointsNeeded;
    }


    public int getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(int viewStatus) {
        this.viewStatus = viewStatus;
    }
}
