package cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser;

import cn.chono.yopper.data.AppointOwner;
import cn.chono.yopper.data.AppointmentDto;

/**
 * Created by sunquan on 16/5/10.
 */
public class DatingInfoStateDto {

    private AppointmentDto dating;

    //基于邀约聊天状态 0 1未处理 3同意 4拒绝 2再考虑
    private int chatState;

    // 当前用户是否发起者
    private boolean isCurrentUserLauncher;


    public AppointmentDto getDating() {
        return dating;
    }

    public void setDating(AppointmentDto dating) {
        this.dating = dating;
    }

    public int getChatState() {
        return chatState;
    }

    public void setChatState(int chatState) {
        this.chatState = chatState;
    }

    public boolean isCurrentUserLauncher() {
        return isCurrentUserLauncher;
    }

    public void setCurrentUserLauncher(boolean currentUserLauncher) {
        isCurrentUserLauncher = currentUserLauncher;
    }
}
