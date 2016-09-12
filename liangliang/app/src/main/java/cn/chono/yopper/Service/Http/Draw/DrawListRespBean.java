package cn.chono.yopper.Service.Http.Draw;

import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/24.
 */
public class DrawListRespBean extends RespBean {
    private DrawRespContent resp;

    public DrawRespContent getResp() {
        return resp;
    }

    public void setResp(DrawRespContent resp) {
        this.resp = resp;
    }

    public class DrawRespContent{
        List<DrawListInfo> drawPrizeInfoList;

        public List<DrawListInfo> getDrawPrizeInfoList() {
            return drawPrizeInfoList;
        }

        public void setDrawPrizeInfoList(List<DrawListInfo> drawPrizeInfoList) {
            this.drawPrizeInfoList = drawPrizeInfoList;
        }
    }
    public class DrawListInfo{
        private String imageUrl;
        private String name;
        private int prizeLevel;
        private int type;
        private double appleCount;

        public String getImageUrl() {
            return imageUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getAppleCount() {
            return appleCount;
        }

        public void setAppleCount(double appleCount) {
            this.appleCount = appleCount;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrizeLevel() {
            return prizeLevel;
        }

        public void setPrizeLevel(int prizeLevel) {
            this.prizeLevel = prizeLevel;
        }
    }
}
