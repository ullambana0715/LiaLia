package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by cc on 16/3/21.
 */
public class TravelObjects implements Serializable {

    private int objectsID;

    private String context;

    public TravelObjects(int objectsID, String context) {
        this.objectsID = objectsID;
        this.context = context;
    }

    public int getObjectsID() {
        return objectsID;
    }

    public void setObjectsID(int objectsID) {
        this.objectsID = objectsID;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
