package cn.chono.yopper.event;

/**
 * Created by sunquan on 16/5/31.
 */
public class OnNearPeopleFilterEvent {


    //  1代表筛选条件选择  2代表刷选失败
    private int filterProcess;

    //附近人类型
    private int peopleFilterType;

    public OnNearPeopleFilterEvent() {
    }

    public OnNearPeopleFilterEvent(int filterProcess, int peopleFilterType) {
        this.filterProcess = filterProcess;
        this.peopleFilterType = peopleFilterType;
    }

    public int getPeopleFilterType() {
        return peopleFilterType;
    }

    public void setPeopleFilterType(int peopleFilterType) {
        this.peopleFilterType = peopleFilterType;
    }

    public int getFilterProcess() {
        return filterProcess;
    }

    public void setFilterProcess(int filterProcess) {
        this.filterProcess = filterProcess;
    }
}
