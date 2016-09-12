package cn.chono.yopper.Service.Http.DaillyTouch;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class DaillyTouchRespBean extends RespBean {

    private DaillyTouch resp;

    public DaillyTouch getResp() {
        return resp;
    }

    public void setResp(DaillyTouch resp) {
        this.resp = resp;
    }

    public class DaillyTouch implements Serializable {

        private int daillyPointsGet;
        private int daillyFaceRatedTotal;
        private int daillyFaceRatingAllowed;
        private boolean hasPerfectMatch;

        private int dailyKeysGet;


        public int getDaillyPointsGet() {
            return daillyPointsGet;
        }

        public void setDaillyPointsGet(int daillyPointsGet) {
            this.daillyPointsGet = daillyPointsGet;
        }

        public int getDaillyFaceRatedTotal() {
            return daillyFaceRatedTotal;
        }

        public void setDaillyFaceRatedTotal(int daillyFaceRatedTotal) {
            this.daillyFaceRatedTotal = daillyFaceRatedTotal;
        }

        public int getDaillyFaceRatingAllowed() {
            return daillyFaceRatingAllowed;
        }

        public void setDaillyFaceRatingAllowed(int daillyFaceRatingAllowed) {
            this.daillyFaceRatingAllowed = daillyFaceRatingAllowed;
        }

        public boolean isHasPerfectMatch() {
            return hasPerfectMatch;
        }

        public void setHasPerfectMatch(boolean hasPerfectMatch) {
            this.hasPerfectMatch = hasPerfectMatch;
        }

        public int getDailyKeysGet() {
            return dailyKeysGet;
        }

        public void setDailyKeysGet(int dailyKeysGet) {
            this.dailyKeysGet = dailyKeysGet;
        }
    }
}
