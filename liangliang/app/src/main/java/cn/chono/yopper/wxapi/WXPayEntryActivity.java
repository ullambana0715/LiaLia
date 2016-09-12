package cn.chono.yopper.wxapi;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.chono.yopper.utils.CommonObservable;
import cn.chono.yopper.utils.CommonObserver;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


	private IWXAPI api;
	
	private static int result=100; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WeixinUtils.handlePayIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		WeixinUtils.handlePayIntent(intent, this);
		
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = 1;
				CommonObservable.getInstance().notifyObservers(CommonObserver.WeiXinPayObserver.class);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = 2;
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = 3;
				break;
			default:
				result = 100;
				break;
			}
		}
		

		finish();
	}

}