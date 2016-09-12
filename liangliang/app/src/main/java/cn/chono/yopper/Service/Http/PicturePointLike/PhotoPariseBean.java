package cn.chono.yopper.Service.Http.PicturePointLike;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 2016/3/18.
 */
public class PhotoPariseBean extends ParameterBean {

    //照片路径
    private String imagePath;

    //是否取消
    private boolean isCancel;

    public boolean getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
