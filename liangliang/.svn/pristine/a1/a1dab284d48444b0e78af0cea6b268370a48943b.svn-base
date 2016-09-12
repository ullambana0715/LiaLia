package cn.chono.yopper.Service.Http.DattingAttampt;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class DattingAttamptBean extends ParameterBean {

    private int targetUserId;

    private boolean confirm;

    private Type type;

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static class  Type implements Serializable{
        private int datingType;
        private int locationId;
        private int locationIndex;

        public int getDatingType() {
            return datingType;
        }

        public void setDatingType(int datingType) {
            this.datingType = datingType;
        }

        public int getLocationId() {
            return locationId;
        }

        public void setLocationId(int locationId) {
            this.locationId = locationId;
        }

        public int getLocationIndex() {
            return locationIndex;
        }

        public void setLocationIndex(int locationIndex) {
            this.locationIndex = locationIndex;
        }
    }

}
