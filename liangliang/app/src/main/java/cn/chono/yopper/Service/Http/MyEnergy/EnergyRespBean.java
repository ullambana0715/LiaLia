package cn.chono.yopper.Service.Http.MyEnergy;

import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/18.
 */
public class EnergyRespBean extends RespBean {
    private MyEnergyRespBeanResp resp;

    public MyEnergyRespBeanResp getResp() {
        return resp;
    }

    public void setResp(MyEnergyRespBeanResp resp) {
        this.resp = resp;
    }

    public class MyEnergyRespBeanResp extends RespBean {
        private String userId;
        private String currentValue ;
        private String totalValue ;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(String currentValue) {
            this.currentValue = currentValue;
        }

        public String getTotalValue() {
            return totalValue;
        }

        public void setTotalValue(String totalValue) {
            this.totalValue = totalValue;
        }
    }
}
