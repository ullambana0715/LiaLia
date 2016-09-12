package cn.chono.yopper.data;

/**
 * Created by SQ on 2015/11/9.
 */
public class SyncDto {
    private LookmeDto visits;
    //0-未认证 ；1-审核中；2-已认证；3-未通过
    private  int videoVerificationStatus;

    public LookmeDto getVisits() {
        return visits;
    }

    public void setVisits(LookmeDto visits) {
        this.visits = visits;
    }

    public int getVideoVerificationStatus() {
        return videoVerificationStatus;
    }

    public void setVideoVerificationStatus(int videoVerificationStatus) {
        this.videoVerificationStatus = videoVerificationStatus;
    }
}
