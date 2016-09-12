package cn.chono.yopper.model.like;

import java.util.List;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.entity.likeBean.ILike;

/**
 * Created by jianghua on 2016/7/18.
 */
public interface ILikeContract {

    interface ILikeView extends IView{
        //填充数据
        void setData(List<ILike> data);

        void addData(List<ILike> data);

        void showMessage(String msg);

        void handNetError();

        void handNoDataError();

        void onRefreshFinish();

        void onRefreshError(String msg);

        void canLoadMore(boolean flag);

        void onLoadMoreError(String msg);

        void onGoneRemind();
    }

    interface ILikePresenter extends IPresenter{
        //添加业务处理方法type>请求的类别 isrefresh>是否是刷新操作
        void getData(int type,boolean isRefresh);

        void getMoreData(int type);

        //点击头像
        void onAdapterIconClick(ILike.UserInfoBean il,int type);

        //点击活动
        void OnTakePartEvent(String actionId);

        //点击邀约
        void OnDatingsEvent(int userId, String datingId);

        //长按
        void onItemLongClick(int userid,int position,int type);

        //解锁 type 请求解锁页面
        void onUnLockUser(int userId,int type);
    }
}
