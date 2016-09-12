package cn.chono.yopper.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

import cn.chono.yopper.activity.usercenter.SettingActivity;
import cn.chono.yopper.utils.ActivityUtil;

public class WeiBoEntryActivity extends Activity implements IWeiboHandler.Response{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		SinaWeiBoUtil.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		SinaWeiBoUtil.handleIntent(getIntent(), this);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {

//		switch (baseResp.errCode) {
//		case WBConstants.ErrorCode.ERR_OK:
//			DialogUtil.showDisCoverNetToast(ContextUtil.getContext(),"分享成功");
//			break;
//		case WBConstants.ErrorCode.ERR_CANCEL:
//			DialogUtil.showDisCoverNetToast(ContextUtil.getContext(),"取消分享");
//			break;
//		case WBConstants.ErrorCode.ERR_FAIL:
//			DialogUtil.showDisCoverNetToast(ContextUtil.getContext(),"分享失败");
//			break;
//		}

		Bundle bundle = new Bundle();
		//需要清理task 所以传入flagtype=2
		ActivityUtil.jump(WeiBoEntryActivity.this,SettingActivity.class, bundle, 1,0);
	}



}
