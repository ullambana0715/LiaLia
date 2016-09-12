package cn.chono.yopper.entity;

/**
 * Created by sunquan on 16/5/16.
 */
public class DatingsInviteEntity {

    private Integer result;// 是否成功（0：成功 1：需要上传头像 2：对方拒绝接收非视频认证用户消息 3：对方Hot 4：重复邀请）\r\n             当对方Hot时，app端组织提示文案

    private String returnMsg;



    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }


    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
