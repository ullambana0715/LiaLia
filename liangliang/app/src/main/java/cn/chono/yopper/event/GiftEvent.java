package cn.chono.yopper.event;

/**
 * Created by cc on 16/7/1.
 */
public class GiftEvent{

    public  String giftImg;

    public int charmValue;

    public String toSendWords;


    public GiftEvent(String giftImg, int charmValue, String toSendWords) {
        this.giftImg = giftImg;
        this.charmValue = charmValue;
        this.toSendWords = toSendWords;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
    }

    public int getCharmValue() {
        return charmValue;
    }

    public void setCharmValue(int charmValue) {
        this.charmValue = charmValue;
    }


    public String getToSendWords() {
        return toSendWords;
    }

    public void setToSendWords(String toSendWords) {
        this.toSendWords = toSendWords;
    }
}
