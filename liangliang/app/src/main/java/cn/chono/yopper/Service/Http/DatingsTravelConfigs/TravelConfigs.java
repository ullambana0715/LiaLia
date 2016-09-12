package cn.chono.yopper.Service.Http.DatingsTravelConfigs;

import java.util.Arrays;

/**
 * Created by cc on 16/4/5.
 */
public class TravelConfigs {

    private String category;

    private String [] itemNames;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getItemNames() {
        return itemNames;
    }

    public void setItemNames(String[] itemNames) {
        this.itemNames = itemNames;
    }

    @Override
    public String toString() {
        return "TravelConfigs{" +
                "category='" + category + '\'' +
                ", itemNames=" + Arrays.toString(itemNames) +
                '}';
    }
}
