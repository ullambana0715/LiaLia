package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andbase.tractor.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.tencent.TencentShareUtil;
import cn.chono.yopper.utils.BackCall;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DealImgUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.weibo.SinaWeiBoUtil;
import cn.chono.yopper.wxapi.WeixinUtils;

/**
 * Created by jianghua on 2016/3/31.
 */
public class BonusResultActivity extends MainFrameActivity {
//    private String share_url = HttpURL.Test_webURL + "share/award?userid=%@&userimg=%@&prizename=%@&prizeimg=%@";

    private String from_flag;
    private int resultCode;

    private String prizeName = null;
    private String prizeUrl = null;

    private BitmapPool mPool;
    private CropCircleTransformation transformation;


    @Override
    public void onResume() {

        super.onResume();

        if ("mybonus".equals(from_flag)) {//来自我的奖品页面


            MobclickAgent.onPageStart("领奖结果"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        } else {


            MobclickAgent.onPageStart("兑奖结果"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        }


        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();


        if ("mybonus".equals(from_flag)) {//来自我的奖品页面

            MobclickAgent.onPageEnd("领奖结果"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)


        } else {

            MobclickAgent.onPageEnd("兑奖结果"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)

        }


        MobclickAgent.onPause(this); // 统计时长

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_bonus_result);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            prizeName = bundle.getString("prize_name");
            prizeUrl = bundle.getString("prize_url");
            from_flag = bundle.getString("from_flag");
            resultCode = bundle.getInt("result_code");
        }

        mPool = Glide.get(this).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);

        this.getGoBackLayout().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.VISIBLE);
        this.getBtnOption().setVisibility(View.GONE);
        this.gettvOption().setText("完成");
        this.gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));

        this.getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        this.getOptionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        ImageView prize_img_bg = (ImageView) findViewById(R.id.prize_bg_img);
        ImageView bonus_img = (ImageView) findViewById(R.id.bonus_image);
        LogUtils.e("图片地址：" + prizeUrl + "状态码：" + resultCode);
        if (!TextUtils.isEmpty(prizeUrl)) {
            String imageurl = ImgUtils.DealImageUrl(prizeUrl, 260, 260);
            Glide.with(this).load(imageurl).bitmapTransform(transformation).into(bonus_img);
        }

        TextView gongxi = (TextView) findViewById(R.id.alert_success_tv);
        TextView alertContent = (TextView) findViewById(R.id.error_bonus_content_tv);
        TextView prizeNameTv = (TextView) findViewById(R.id.prize_name_tv);
        TextView getPrizeInfo = (TextView) findViewById(R.id.success_bonus_tv);


        Button shareBtn = (Button) findViewById(R.id.success_btn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = String.valueOf(LoginUser.getInstance().getUserId());
                String userImg = null;
                String link;
                String title = "人品爆棚！还有谁？！";
                String content = "我在“俩俩”中获得了" + prizeName;
                String imageUrl = "http://yuepengwww.oss-cn-hangzhou.aliyuncs.com/images/share/152X152.jpg";
                UserInfo userInfo = DbHelperUtils.getUserInfo(LoginUser.getInstance().getUserId());

                if (userInfo != null) {
                    UserDto dto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
                    if (dto != null) {
                        userImg = dto.getProfile().getHeadImg();
                    }
                }

                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(userImg) || TextUtils.isEmpty(prizeName) || TextUtils.isEmpty(prizeUrl)) {
                    DialogUtil.showDisCoverNetToast(BonusResultActivity.this, "分享数据异常");
                    return;
                }

                if (YpSettings.isTest) {
                    link = HttpURL.Test_webURL + "share/award?userid=" + userId + "&userimg=" + userImg + "&prizename=" + prizeName + "&prizeimg=" + prizeUrl;
                } else {
                    link = HttpURL.webURL + "share/award?userid=" + userId + "&userimg=" + userImg + "&prizename=" + prizeName + "&prizeimg=" + prizeUrl;
                }

                LogUtils.e("share url = " + link);
                share(link, title, content, imageUrl);
            }
        });

        Button againBtn = (Button) findViewById(R.id.repety_btn);
        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        if (resultCode == 1) {//兑换或者领奖成功
            gongxi.setVisibility(View.VISIBLE);
            getPrizeInfo.setVisibility(View.VISIBLE);
            shareBtn.setVisibility(View.VISIBLE);
            againBtn.setVisibility(View.GONE);
            prizeNameTv.setVisibility(View.VISIBLE);

            alertContent.setText("已经成功领取了  ");
            prizeNameTv.setText(prizeName);
        } else {
            gongxi.setVisibility(View.GONE);
            getPrizeInfo.setVisibility(View.GONE);
            shareBtn.setVisibility(View.GONE);
            againBtn.setVisibility(View.VISIBLE);

            alertContent.setText("很遗憾，领奖过程出了点问题");
            againBtn.setText("重新领取");
        }

        if ("mybonus".equals(from_flag)) {//来自我的奖品页面
            getTvTitle().setText("领奖结果");

        } else {
            getTvTitle().setText("兑奖结果");
        }

    }

    /**
     * 分享
     */
    private void share(final String link, final String title, final String content, final String img) {

        // 分享
        Dialog dialog = DialogUtil.createShareDialog(BonusResultActivity.this, new BackCall() {

            @Override
            public void deal(int which, Object... obj) {

                switch (which) {
                    case R.id.setting_share_to_sina_weibo:

                        SinaWeiBoUtil.sendMessage(BonusResultActivity.this, true, false, title, content, img, link);

                        break;

                    case R.id.setting_share_to_qq:

                        TencentShareUtil.shareToQQ(BonusResultActivity.this, title, content, link, img, getString(R.string.app_name));
                        break;

                    case R.id.setting_share_to_weixin_friend:
                        // 微信朋友圈分享

                        if (WeixinUtils.isWeixinAvailable()) {

                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    final Bitmap bmp = DealImgUtils.getHtmlByteArray(img);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null == bmp) {
                                                Bitmap bmpp = BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);
                                                WeixinUtils.sendFriendTextAndPicture(title, content, link, bmpp);
                                            } else {
                                                WeixinUtils.sendFriendTextAndPicture(title, content, link, bmp);
                                            }
                                        }
                                    });

                                }
                            }.start();

                        } else {
                            DialogUtil.showTopToast(BonusResultActivity.this, "您的手机没有安装微信!");
                        }
                        break;

                    case R.id.setting_share_to_weixin:

                        if (WeixinUtils.isWeixinAvailable()) {

                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    final Bitmap bmp = DealImgUtils.getHtmlByteArray(img);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null == bmp) {
                                                Bitmap bmpp = BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);
                                                WeixinUtils.sendTextAndPicture(title, content, link, bmpp);
                                            } else {
                                                WeixinUtils.sendTextAndPicture(title, content, link, bmp);
                                            }
                                        }
                                    });

                                }
                            }.start();
                        } else {
                            DialogUtil.showTopToast(BonusResultActivity.this, "您的手机没有安装微信!");
                        }

                        break;

                    case R.id.setting_share_to_qq_zone:

                        // http://cdn.duitang.com/uploads/item/201209/03/20120903120924_TSsvE.jpeg
                        ArrayList<String> img_url_list = new ArrayList<String>();
                        img_url_list.add(img);
                        TencentShareUtil.shareToQzone(BonusResultActivity.this, title, content, link, img_url_list, getString(R.string.app_name));

                        break;

                    default:
                        break;
                }

                super.deal(which, obj);
            }

        });
        if (!isFinishing()) {
            dialog.show();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
