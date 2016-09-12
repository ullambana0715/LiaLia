package cn.chono.yopper.Service.Http.BrokenLock;

/**
 * Created by sunquan on 16/5/10.
 */
public class UnLockRespDto {

    private boolean isSuccess;

    private String errorCode;

    private String errorMsg;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
