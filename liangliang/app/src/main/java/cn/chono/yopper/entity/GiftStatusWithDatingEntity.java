package cn.chono.yopper.entity;

/**
 * Created by cc on 16/8/9.
 */
public class GiftStatusWithDatingEntity {


    /**
     * giftTransferRelationStatus : 0
     * msg : sample string 1
     */
// 结果（0：成功 1：需要上传头像 2：对方拒绝接收非视频认证用户消息 3：拒绝签收礼物）
    public int result;

    // 失败信息
    public String msg;
}
