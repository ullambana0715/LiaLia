package cn.chono.yopper.activity.base;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.GameOrderDetail.GameOrderDetailBean;
import cn.chono.yopper.Service.Http.GameOrderDetail.GameOrderDetailDto;
import cn.chono.yopper.Service.Http.GameOrderDetail.GameOrderDetailRespBean;
import cn.chono.yopper.Service.Http.GameOrderDetail.GameOrderDetailService;
import cn.chono.yopper.Service.Http.OAuthToken.OAuthTokenRespBean;
import cn.chono.yopper.Service.Http.OAuthToken.OAuthTokenService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.order.UserAppleOrderPayActivity;
import cn.chono.yopper.event.GameOrderEvent;
import cn.chono.yopper.event.GameTokenEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ISO8601;

public class KingdomFragment extends Fragment {


    private String webURL = "http://xzwg.filecdns.com/";

//    private String webURL = "http://192.168.1.151:8066/";

    private Dialog loadingDiaog;

    private double latitude = 0;
    private double longtitude = 0;

    private WebView campaigns_webview;

    private ViewStub error_network_vs;

    private View convertView;

    private String accessToken;


    private String orderjs;

    public KingdomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.campaigns_activity, container, false);
        RxBus.get().register(this);
        initView();
        initWebView();
        getOAuthToken();
        return convertView;
    }


    private void initView() {

        campaigns_webview = (WebView) convertView.findViewById(R.id.campaigns_webview);

        error_network_vs = (ViewStub) convertView.findViewById(R.id.error_network_vs);

    }


    private void initWebView() {


        campaigns_webview.getSettings().setJavaScriptEnabled(true);// 允许使用js
        campaigns_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        campaigns_webview.getSettings().setDomStorageEnabled(true);

//        campaigns_webview.getSettings().setSupportZoom(true);
//        campaigns_webview.getSettings().setBuiltInZoomControls(true);
//        campaigns_webview.getSettings().setUseWideViewPort(true);
//        campaigns_webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        campaigns_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        campaigns_webview.getSettings().setLoadWithOverviewMode(true);
//        campaigns_webview.getSettings().setAppCacheEnabled(true);

        campaigns_webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.e("游戏的url=" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                campaigns_webview.stopLoading();

                campaigns_webview.setVisibility(View.GONE);

                handleNetError();

            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {

                return super.shouldOverrideKeyEvent(view, event);


            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String valueUrl = ISO8601.decodeURIComponent(url);//decode URL 我们肉眼能看得清楚的URL
                try {


                    if (valueUrl.toLowerCase().startsWith("http://localhost/return?data=")) {

                        valueUrl = valueUrl.replace("http://localhost/return?data=", "");

                        LogUtils.e("shouldOverrideUrlLoading=" + valueUrl);

                        JSONObject obj = new JSONObject(valueUrl);

                        String type = obj.getString("type");

                        if (!CheckUtil.isEmpty(type) && type.equals("order")) {//订单

                            JSONObject dataobj = obj.getJSONObject("data");
                            String orderid = dataobj.getString("orderid");
                            orderjs = dataobj.getString("inject");
                            getGameOrderDetail(orderid);
                            return true;

                        }

                        if (!CheckUtil.isEmpty(type) && type.equals("showUserInfo")) {//订单

                            JSONObject dataobj = obj.getJSONObject("data");
                            String userid = dataobj.getString("userId");
                            goUserInfo(userid);
                            return true;

                        }

                    }

                } catch (JSONException e) {

                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }


    @Override
    public void onDestroy() {

        RxBus.get().unregister(this);

        super.onDestroy();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("GameTokenEvent")

            }
    )
    public void gameTokenEvent(GameTokenEvent event) {
        if (event != null) {

            if (!TextUtils.equals(accessToken, event.getAccessToken())) {

                campaigns_webview.setVisibility(View.VISIBLE);
                error_network_vs.setVisibility(View.GONE);

                LocInfo myLoc = Loc.getLoc();
                if (myLoc != null && myLoc.getLoc() != null) {
                    latitude = myLoc.getLoc().getLatitude();
                    longtitude = myLoc.getLoc().getLongitude();
                }

                accessToken = event.getAccessToken();
                webURL = webURL + "?accessToken=" + event.getAccessToken() + "&refreshToken=" + event.getRefreshToken() + "&unionId=" + event.getUnionId() + "&lat=" + latitude + "&lng=" + longtitude + "&spend=true";
                campaigns_webview.loadUrl(webURL);
            }
        }
    }


    /**
     * 获取版本信息
     */

    private void getOAuthToken() {

        if (CheckUtil.isEmpty(accessToken)) {
            loadingDiaog = DialogUtil.LoadingDialog(getActivity(), null);
            if (!getActivity().isFinishing()) {
                loadingDiaog.show();
            }
        }

        OAuthTokenService oAuthTokenService = new OAuthTokenService(getActivity());
        oAuthTokenService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                if (loadingDiaog != null) {
                    loadingDiaog.dismiss();
                }

                OAuthTokenRespBean oAuthTokenRespBean = (OAuthTokenRespBean) respBean;
                OAuthTokenRespBean.OAuthTokenDto dto = oAuthTokenRespBean.getResp();

                if (dto != null) {
                    campaigns_webview.setVisibility(View.VISIBLE);
                    error_network_vs.setVisibility(View.GONE);

                    LocInfo myLoc = Loc.getLoc();
                    if (myLoc != null && myLoc.getLoc() != null) {
                        latitude = myLoc.getLoc().getLatitude();
                        longtitude = myLoc.getLoc().getLongitude();
                    }

                    if (CheckUtil.isEmpty(accessToken)) {

                        accessToken = dto.getAccessToken();
                        webURL = webURL + "?accessToken=" + dto.getAccessToken() + "&refreshToken=" + dto.getRefreshToken() + "&unionId=" + dto.getUnionId() + "&lat=" + latitude + "&lng=" + longtitude + "&spend=true";
                        campaigns_webview.loadUrl(webURL);
                    } else {

                        if (!TextUtils.equals(accessToken, dto.getAccessToken())) {
                            accessToken = dto.getAccessToken();
                            webURL = "http://xzwg.filecdns.com/";
                            webURL = webURL + "?accessToken=" + dto.getAccessToken() + "&refreshToken=" + dto.getRefreshToken() + "&unionId=" + dto.getUnionId() + "&lat=" + latitude + "&lng=" + longtitude + "&spend=true";
                            campaigns_webview.loadUrl(webURL);
                        }

                    }


                } else {

                    campaigns_webview.setVisibility(View.GONE);
                    handleNetError();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                if (loadingDiaog != null) {
                    loadingDiaog.dismiss();
                }

                if (CheckUtil.isEmpty(accessToken)) {
                    campaigns_webview.setVisibility(View.GONE);
                    handleNetError();
                }
            }
        });

        oAuthTokenService.enqueue();
    }


    private void handleNetError() {

        error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) convertView.findViewById(R.id.error_network_layout);
        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) convertView.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.equals(webURL, "http://xzwg.filecdns.com/")) {

                    getOAuthToken();

                } else {
                    campaigns_webview.setVisibility(View.VISIBLE);
                    campaigns_webview.loadUrl(webURL);
                    error_network_vs.setVisibility(View.GONE);
                }


            }
        });
    }


    private void goUserInfo(String userid) {

        Bundle bundle = new Bundle();
        bundle.putInt(YpSettings.USERID, Integer.valueOf(userid));
        ActivityUtil.jump(getActivity(), UserInfoActivity.class, bundle, 0, 100);
    }


    /**
     * 获取订单详情
     */
    private void getGameOrderDetail(String orderId) {

        loadingDiaog = DialogUtil.LoadingDialog(getActivity(), null);
        if (!getActivity().isFinishing()) {
            loadingDiaog.show();
        }

        GameOrderDetailService gameOrderDetailService = new GameOrderDetailService(getActivity());

        GameOrderDetailBean gameOrderDetailBean = new GameOrderDetailBean();

        gameOrderDetailBean.setId(orderId);

        gameOrderDetailService.parameter(gameOrderDetailBean);

        gameOrderDetailService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                GameOrderDetailRespBean gameOrderDetailRespBean = (GameOrderDetailRespBean) respBean;

                GameOrderDetailDto dto = gameOrderDetailRespBean.getResp();

                Bundle bundle = new Bundle();

                bundle.putString(YpSettings.ORDER_ID, dto.getOrderId());
                bundle.putString(YpSettings.ProductName, dto.getProductName());
                bundle.putLong(YpSettings.PAY_COST, dto.getTotalFee() / 100);
                bundle.putInt(YpSettings.PAY_TYPE, 2);

                ActivityUtil.jump(getActivity(), UserAppleOrderPayActivity.class, bundle, 0, 100);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(getActivity());
                    return;
                }
                DialogUtil.showDisCoverNetToast(getActivity(), msg);

            }
        });
        gameOrderDetailService.enqueue();

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("GameOrderEvent")

            }
    )
    public void gameOrderEvent(GameOrderEvent event) {

        if (!CheckUtil.isEmpty(orderjs)) {

            campaigns_webview.loadUrl("javascript:" + orderjs);//注入js
        }
    }


}
