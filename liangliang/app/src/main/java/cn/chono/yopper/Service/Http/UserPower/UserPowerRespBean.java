package cn.chono.yopper.Service.Http.UserPower;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by jianghua on 2016/3/15.
 */
public class UserPowerRespBean extends RespBean {
    //能量信息
    private Power power;

    //能量配置上限信息
    private PowerConfigLimit powerConfigLimit;

    //能量配置信息
    private PowerConfig powerConfig;


    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public PowerConfigLimit getPowerConfigLimit() {
        return powerConfigLimit;
    }

    public void setPowerConfigLimit(PowerConfigLimit powerConfigLimit) {
        this.powerConfigLimit = powerConfigLimit;
    }

    public PowerConfig getPowerConfig() {
        return powerConfig;
    }

    public void setPowerConfig(PowerConfig powerConfig) {
        this.powerConfig = powerConfig;
    }


    /**
     * 能量信息
     */
    class Power implements Serializable {
        // 能量值
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 能量配置上限信息
     */
    class PowerConfigLimit implements Serializable {
        // 能量配置Id
        private String powerCofigId;

        // 配置项名称
        private String itemName;

        // 每日上限
        private String upperLimit;

        public String getPowerCofigId() {
            return powerCofigId;
        }

        public void setPowerCofigId(String powerCofigId) {
            this.powerCofigId = powerCofigId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getUpperLimit() {
            return upperLimit;
        }

        public void setUpperLimit(String upperLimit) {
            this.upperLimit = upperLimit;
        }
    }

    /**
     * 能量配置信息
     */
    class PowerConfig implements Serializable {
        // 能量配置Id
        private String powerCofigId;

        // 配置项名称
        private String itemName;

        // 配置项类型（1:主动获取 2:被动获取）
        private String itemType;

        // 配置值
        private String itemValue;

        // 每日上限
        private String upperLimit;

        public String getPowerCofigId() {
            return powerCofigId;
        }

        public void setPowerCofigId(String powerCofigId) {
            this.powerCofigId = powerCofigId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getItemValue() {
            return itemValue;
        }

        public void setItemValue(String itemValue) {
            this.itemValue = itemValue;
        }

        public String getUpperLimit() {
            return upperLimit;
        }

        public void setUpperLimit(String upperLimit) {
            this.upperLimit = upperLimit;
        }
    }
}
