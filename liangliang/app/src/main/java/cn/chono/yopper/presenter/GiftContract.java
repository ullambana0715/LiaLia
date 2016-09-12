package cn.chono.yopper.presenter;

import java.util.List;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.entity.chatgift.GiftInfoEntity;
import cn.chono.yopper.entity.chatgift.GiftOrdreReq;

/**
 * Created by sunquan on 16/8/4.
 */
public interface GiftContract {

    interface View extends IView {

        void updateAppleCountView(int count);

        void updateGiftNoPassList(List<GiftInfoEntity> list);

        void updateGiftPassHotList(List<GiftInfoEntity> list);

        void showNoGiftPassHotDataView();

        void onClikGiftWhySendNoResponse();

        void onClikGiftWhyGirlLikeGift();

        void onClikGetApple();

    }

    interface Presenter extends IPresenter {

        void getAllGifts();

        void giveGift(String giftID, String datingId, int toUserId, boolean isFirstCall,String giftImg,int charmValue);

        void appleQuickly(GiftOrdreReq rep);

    }
}
