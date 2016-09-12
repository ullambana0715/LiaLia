package cn.chono.yopper.Service.Http.MyEnergy;

import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/3/18.
 */
public class MyEnergyRespBean extends RespBean {
    private MyEnergyRespBeanResp resp;

    public MyEnergyRespBeanResp getResp() {
        return resp;
    }

    public void setResp(MyEnergyRespBeanResp resp) {
        this.resp = resp;
    }

    public class MyEnergyRespBeanResp extends RespBean {


        private Power power;
        private List<PowerConfigs> powerConfigs;

        public Power getPower() {
            return power;
        }

        public void setPower(Power power) {
            this.power = power;
        }

        public List<PowerConfigs> getPowerConfigs() {
            return powerConfigs;
        }

        public void setPowerConfigs(List<PowerConfigs> powerConfigs) {
            this.powerConfigs = powerConfigs;
        }

        public class Power {
            private String userId;
            private int currentValue;
            private int totalValue;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public int getCurrentValue() {
                return currentValue;
            }

            public void setCurrentValue(int currentValue) {
                this.currentValue = currentValue;
            }

            public int getTotalValue() {
                return totalValue;
            }

            public void setTotalValue(int totalValue) {
                this.totalValue = totalValue;
            }
        }

        public class PowerConfigs {
            private int actionType;
            private String desc;

            public int getActionType() {
                return actionType;
            }

            public void setActionType(int actionType) {
                this.actionType = actionType;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public List<Items> getItems() {
                return items;
            }

            public void setItems(List<Items> items) {
                this.items = items;
            }

            private List<Items> items;
        }

        public class Items {
            private String itemDesc;
            private String itemValue;

            public String getItemDesc() {
                return itemDesc;
            }

            public void setItemDesc(String itemDesc) {
                this.itemDesc = itemDesc;
            }

            public String getItemValue() {
                return itemValue;
            }

            public void setItemValue(String itemValue) {
                this.itemValue = itemValue;
            }
        }
    }
}
