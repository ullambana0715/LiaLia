package cn.chono.yopper.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

public class SinaWeiBoUtil {

	//1119193594
	public static String app_id="428166326";



	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 *
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
	 * 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";


	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
	 * 选择赋予应用的功能。
	 *
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
	 * 使用权限，高级权限需要进行申请。
	 *
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 *
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE =
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";



	/** 微博微博分享接口实例 */
	private static IWeiboShareAPI  mWeiboShareAPI = null;

	/**
	 * 初始化新浪api
	 *
	 * @param context
	 */
	public static void initweibo(Context context) {
		try {
			// 如果是测试版使用测试版app_id
			//			 if (DEBUG == true)
			//			 APP_ID = APP_ID_TEST;
			// 通过WXAPIFactory工厂，获取IWXAPI的实例
			mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, app_id,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查用户手机端是否可使用weibo分享
	 *
	 * @return
	 */
	public static boolean isWeiboAvailable() {
		try {
			if (mWeiboShareAPI == null) {
				return false;
			}
			return mWeiboShareAPI.isWeiboAppInstalled();// && api.isWXAppSupportAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 注册新浪微博app，注册成功后该应用将显示在新浪的app列表中
	 *
	 * @return
	 */
	public static boolean registerWeibo() {
		try {
			if (mWeiboShareAPI == null || !isWeiboAvailable()) {
				return false;
			}
			return mWeiboShareAPI.registerApp();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 处理微博回调
	 *
	 * @param intent
	 * @return
	 */
	public static boolean handleIntent(Intent intent, IWeiboHandler.Response res) {
		try {
			if (mWeiboShareAPI == null || !isWeiboAvailable()) {
				return false;
			}
			return mWeiboShareAPI.handleWeiboResponse(intent,res);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。
	 */
	public static void sendMessage(Context mContext,boolean hasText,boolean hasWebpage,String appname,String description, String imageUrl, String webUrl) {

		sendMultiMessage(mContext,hasText,  hasWebpage,appname,description,imageUrl,webUrl);
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。
	 * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
	 * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
	 *
	 * @param hasText    分享的内容是否有文本
	 * @param hasWebpage 分享的内容是否有网页
	 */
	public static void sendMultiMessage(final Context mContext,boolean hasText,boolean hasWebpage,String appname,String description, String imageUrl, String webUrl) {

		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (hasText) {
			weiboMessage.textObject = getTextObj(appname, description,  imageUrl,  webUrl);
		}



		// 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
		if (hasWebpage) {
			weiboMessage.mediaObject = getWebpageObj(appname, description,  imageUrl,  webUrl);
		}



		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		// 3. 发送请求消息到微博，唤起微博分享界面

		AuthInfo authInfo = new AuthInfo(mContext, app_id, REDIRECT_URL, SCOPE);
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mContext);
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
		}
		mWeiboShareAPI.sendRequest((Activity) mContext, request, authInfo, token, new WeiboAuthListener() {

			@Override
			public void onWeiboException( WeiboException arg0 ) {

			}

			@Override
			public void onComplete( Bundle bundle ) {

				Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
				AccessTokenKeeper.writeAccessToken(mContext, newToken);
			}

			@Override
			public void onCancel() {

			}
		});

	}


	/**
	 * 创建文本消息对象。
	 *
	 * @return 文本消息对象。
	 */
	private static TextObject getTextObj(String appname,String description,  String imageUrl,String webUrl) {
		TextObject textObject = new TextObject();
		textObject.text =description+" "+webUrl;
		return textObject;
	}

	/**
	 * 创建多媒体（网页）消息对象。
	 *
	 * @return 多媒体（网页）消息对象。
	 */
	private static WebpageObject getWebpageObj(String appname,String description,  String imageUrl,String webUrl) {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = appname;
		mediaObject.description = description;

		mediaObject.actionUrl = webUrl;
		return mediaObject;
	}



}
