package cn.chono.yopper.Service.Http.BlockRequest;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class BlockRequestBean extends ParameterBean {

    private boolean Block;
    private  int userId;

    private int id;

    public boolean isBlock() {
        return Block;
    }

    public void setBlock(boolean block) {
        Block = block;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
