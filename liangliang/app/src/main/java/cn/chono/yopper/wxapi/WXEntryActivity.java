package cn.chono.yopper.wxapi;

import android.app.*;
import android.content.*;
import android.os.*;

import cn.chono.yopper.utils.CommonObservable;
import cn.chono.yopper.utils.CommonObserver;

import com.lidroid.xutils.util.LogUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.*;


/**
 * 接收微信的请求及返回值的Activity
 * 
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WeixinUtils.handleIntent(getIntent(), this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		WeixinUtils.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		com.lidroid.xutils.util.LogUtils.e("=====================req"+req.toString());
		// Toast.makeText(this, "openid = " + req.openId,
		// Toast.LENGTH_SHORT).show();
		//
		// switch (req.getType()) {
		// case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
		// goToGetMsg();
		// break;
		// case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
		// goToShowMsg((ShowMessageFromWX.Req) req);
		// break;
		// case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
		// Toast.makeText(this, R.string.launch_from_wx,
		// Toast.LENGTH_SHORT).show();
		// break;
		// default:
		// break;
		// }
		// IOUtils.writeTestInfo(WXEntryActivity.this, "log_WeiXinLogin.txt",
		// "微信登录\r\n" + req.getType()+ "---------------------\r\n");
		// switch (req.getType()) {
		// case ConstantsAPI.COMMAND_SENDAUTH:
		// dologin((SendAuth.Req) req);
		// break;
		// }

		//ActivityUtil.jumpNotForResult(this, IndexActivity.class, new Bundle(), false);
		finish();
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		// Toast.makeText(this, "openid = " + resp.openId,
		// Toast.LENGTH_SHORT).show();
		//
		// if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
		// Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code,
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// String result = "";
		//



		switch (resp.errCode) {
			// 发送成功
			case BaseResp.ErrCode.ERR_OK:

				LogUtils.e("type="+resp.getType());

				if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {

				}else{
					WeixinUtils.weiXin_sendAuth=(Resp)resp;
					com.lidroid.xutils.util.LogUtils.e(WeixinUtils.weiXin_sendAuth.toString());
					CommonObservable.getInstance().notifyObservers(CommonObserver.WeiXinObserver.class);
				}

				break;
			// 发送取消
			case BaseResp.ErrCode.ERR_USER_CANCEL:

				break;
			// 发送被拒绝
			case BaseResp.ErrCode.ERR_AUTH_DENIED:

				break;

			// 发送返回
			default:

				break;

		}
		finish();



	}

	

}