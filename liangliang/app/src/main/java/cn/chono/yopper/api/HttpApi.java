package cn.chono.yopper.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.data.LikeListDto;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.AttamptRespDto;
import cn.chono.yopper.entity.DatingRequirementData;
import cn.chono.yopper.entity.DatingsInviteEntity;
import cn.chono.yopper.entity.GiftStatusWithDatingEntity;
import cn.chono.yopper.entity.HeadEntity;
import cn.chono.yopper.entity.PrivateAlbum;
import cn.chono.yopper.entity.Report;
import cn.chono.yopper.entity.VideoEntity;
import cn.chono.yopper.entity.WithDrawBody;
import cn.chono.yopper.entity.WithDrawRecordsEntity;
import cn.chono.yopper.entity.WithDrawResultEntity;
import cn.chono.yopper.entity.charm.CharmInfoEntity;
import cn.chono.yopper.entity.chatgift.AllGiftsEntity;
import cn.chono.yopper.entity.chatgift.GiftOrderResp;
import cn.chono.yopper.entity.chatgift.GiftOrdreReq;
import cn.chono.yopper.entity.chatgift.GiveGiftBody;
import cn.chono.yopper.entity.chatgift.GiveGiftRpBean;
import cn.chono.yopper.entity.chatgift.ReceiveGiftBody;
import cn.chono.yopper.entity.chatgift.ReceiveGiftResultEntity;
import cn.chono.yopper.entity.likeBean.CancelIlike;
import cn.chono.yopper.entity.likeBean.CheckKeyBean;
import cn.chono.yopper.entity.likeBean.IlikeBase;
import cn.chono.yopper.entity.likeBean.PrivateAlbumBody;
import cn.chono.yopper.entity.unlock.PrivateAlbumBean;
import cn.chono.yopper.entity.unlock.UnlockPeople;
import cn.chono.yopper.entity.unlock.UnlockVideoBean;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by SQ on 16/7/12.
 */
public interface HttpApi {

//    @GET("txapi/weixin/wxhot")
//    rx.Observable<ApiResponse<List<Popular>>> getPopular(@Query("page") int page, @Query("num") int num, @Query("word") String word);

    @GET("/api/v2/users/like?Start=0")
    rx.Observable<ApiResp<LikeListDto>> getLike();

    //我喜欢
    @GET("/api/v2/users/like/me?")
    rx.Observable<ApiResp<IlikeBase>> getIlike(@Query("start") int start);

    //喜欢我
    @GET("/api/v2/users/like/other?")
    rx.Observable<ApiResp<IlikeBase>> getLikeMe(@Query("start") int start);

    //相互喜欢
    @GET("/api/v2/users/like/each?")
    rx.Observable<ApiResp<IlikeBase>> getLikeEach(@Query("start") int start);

    //解锁的时候校验钥匙
    @GET("/api/v2/users/like/UserKey")
    rx.Observable<ApiResp<CheckKeyBean>> onCheckKey();

    //解锁喜欢我的人
    @PUT("/api/v2/users/like/unlock")
    rx.Observable<ApiResp<UnlockPeople>> putUnlockPeople(@QueryMap HashMap<String, Object> map);

//    //全部的礼物
//    @GET("/api/v2/gifts")
//    rx.Observable<ApiResp<GiftBean>> getGift();

    //全部的礼物
    @GET("/api/v2/gifts")
    rx.Observable<ApiResp<AllGiftsEntity>> getAllGifts();

    //赠送礼物
    @POST("api/v2/gifts/{id}/present")
    rx.Observable<ApiResp<GiveGiftRpBean>> giveGift(@Path("id") String id, @Body GiveGiftBody body);

    //赠送礼物快速下单
    @POST("api/v2/orders/appleQuickly")
    rx.Observable<ApiResp<GiftOrderResp>> giveOrder(@Body GiftOrdreReq rep);


    //签收礼物
    @PUT("api/v2/gifts/receive")
    rx.Observable<ApiResp<ReceiveGiftResultEntity>> receiveGift(@Body ReceiveGiftBody body);


    /**
     * 私密
     */
    //解锁私密相册
    @PUT("/api/v2/user/PrivateAlbum")
    rx.Observable<ApiResp<PrivateAlbumBean>> putUnlockAlbum(@Body PrivateAlbumBody body);

    /**
     * 查看视频验证
     */
    @GET("/api/v2/video/{UserId}/{VideoId}")
    rx.Observable<ApiResp<UnlockVideoBean>> getUnlockVideo(@Path("UserId") int userId, @Path("VideoId") int videoId);


    /**
     * 获取用户资料,包括相册等
     *
     * @param UserId
     * @param Wish
     * @param Album
     * @param Bubble
     * @param ForDating
     * @param Verification
     * @param FaceRating
     * @param Appointment
     * @param Friends
     * @param NewAlbum
     * @param Dating
     * @param PrivateAlbum
     * @param GenenalVideo
     * @param Lat
     * @param Lng
     * @return
     */
    //api/v2/user/{UserId}/profile?Wish={Wish}&Album={Album}&Bump={Bump}&Bubble={Bubble}&ForDating={ForDating}&Verification={Verification}&FaceRating={FaceRating}&Appointment={Appointment}&Friends={Friends}&NewAlbum={NewAlbum}&Dating={Dating}&PrivateAlbum={PrivateAlbum}&GenenalVideo={GenenalVideo}&Lat={Lat}&Lng={Lng}
    @GET("api/v2/user/{userId}/profile")
    rx.Observable<ApiResp<UserDto>> getUserInfo(@Path("userId") int UserId

            , @Query("Wish") boolean Wish
            , @Query("Album") boolean Album
            , @Query("Bubble") boolean Bubble
            , @Query("ForDating") boolean ForDating
            , @Query("Verification") boolean Verification
            , @Query("FaceRating") boolean FaceRating
            , @Query("Appointment") boolean Appointment
            , @Query("Friends") boolean Friends
            , @Query("NewAlbum") boolean NewAlbum
            , @Query("Dating") boolean Dating
            , @Query("PrivateAlbum") Boolean PrivateAlbum
            , @Query("GenenalVideo") Boolean GenenalVideo
            , @Query("MyGiftSum") Boolean MyGiftSum
            , @Query("Lat") Double Lat
            , @Query("Lng") Double Lng);

    /**
     * 拉黑
     *
     * @param userId
     * @param blockId
     * @param Block
     * @return
     */
    @POST("api/v2/user/{userId}/block/{blockId}")
    rx.Observable<ApiResp<Boolean>> postBlockRequest(

            @Path("userId") int userId

            , @Path("blockId") int blockId

            , @Query("Block") boolean Block

    );

    /**
     * 举报
     *
     * @param map
     * @return
     */
    @POST("api/v2/report")
    rx.Observable<ApiResp<Report>> postReport(@QueryMap Map<String, Object> map);

    /**
     * 上传头像
     *
     * @param
     * @param params
     * @return
     */

    @Multipart
    @POST("api/v2/user/headimg")
    rx.Observable<ApiResp<HeadEntity>> uploadingHead(@QueryMap Map<String, Object> map, @PartMap Map<String, RequestBody> params

    );

    /**
     * 上传图片
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("api/v2/user/image")
    rx.Observable<ApiResp<String>> uploadingImage(@PartMap Map<String, RequestBody> params);


    /**
     * 更新相册或更新私密相册
     *
     * @param userId
     * @param imageType V3.1 相册类型 0表示一般相册，1表示私密相册
     * @param album
     * @return
     */
    @PUT("api/v2/user/{userId}/Album")
    @FormUrlEncoded
    rx.Observable<ApiResp<Boolean>> putAlbum(

            @Path("userId") int userId

            , @Field("imageType") int imageType

            , @Field("album") List<String> album

    );

    /**
     * 更新用户的头像
     *
     * @param userId
     * @param headImg
     * @return
     */
    @PUT("api/v2/user/{userId}/HeadImg")
    @FormUrlEncoded
    rx.Observable<ApiResp<Boolean>> putHeadImg(@Path("userId") int userId, @Field("headImg") String headImg);

    /**
     * 邀请上传
     *
     * @param map
     * @return
     */
    @POST("api/v2/user/invite")
    rx.Observable<ApiResp<AttamptRespDto>> attampt(@QueryMap Map<String, Object> map);


    /**
     * 发布邀约的前置条件
     *
     * @return
     */
    @GET("api/v3/datings/requirements")
    rx.Observable<ApiResp<DatingRequirementData>> getDatingRequirementData();

    /**
     * 基于邀约的聊天判断
     *
     * @param targetUserId
     * @param datingId
     * @return
     */
    @POST("api/v3/datings/chat/attempt")
    @FormUrlEncoded
    rx.Observable<ApiResp<AttamptRespDto>> ChatDatingUserAttampt(@Field("targetUserId") int targetUserId, @Field("datingId") String datingId);

    /**
     * 邀约邀请用户
     *
     * @param dateId
     * @param targetUserId
     * @return
     */
    @POST("api/v3/datings/invite")
    @FormUrlEncoded
    rx.Observable<ApiResp<DatingsInviteEntity>> DatingsInvite(@Field("dateId") String dateId, @Field("targetUserId") String targetUserId);

    /**
     * 认证视频详情（我的视频详情，他人视频详情）
     *
     * @param UserId    当前视屏认证的用户Id
     * @param visitorId 当前查看者的用户Id
     * @return
     */
    @GET("api/v2/video/verification/user/{UserId}?VisitorId={visitorId}")
    rx.Observable<ApiResp<VideoEntity>> getVideoVerification(

            @Path("UserId") int UserId

            , @Path("visitorId") int visitorId

    );

    /**
     * 解锁私密相册
     *
     * @param userId
     * @param lookedUserId
     * @return
     */
    @PUT("api/v2/user/PrivateAlbum")
    @FormUrlEncoded
    rx.Observable<ApiResp<PrivateAlbum>> putPrivateAlbumVerification(@Field("userId") int userId, @Field("lookedUserId") int lookedUserId);

    /**
     * 修改个人资料基本信息
     *
     * @param userId
     * @param mag
     * @return
     */
    @PUT("api/v2/user/{userId}/profile")
    @FormUrlEncoded
    rx.Observable<ApiResp<Boolean>> putUserInfo(

            @Path("userId") int userId

            , @FieldMap Map<String, Object> mag

    );

    /**
     * 取消喜欢/添加
     *
     * @param userId
     * @param islike
     * @return
     */
    @POST("/api/v2/users/{userId}/like")
    @FormUrlEncoded
    rx.Observable<ApiResp<CancelIlike>> cancelLike(@Path("userId") int userId, @Field("islike") Boolean islike);


    @GET("/api/v2/users/{userId}/charm/exchange")
    rx.Observable<ApiResp<WithDrawRecordsEntity>> getWithDrawRecords(@Path("userId") int userId, @Query("Start") int Start);


    @POST("/api/v2/users/{userId}/charm/exchange")
    rx.Observable<ApiResp<WithDrawResultEntity>> withDrawCommit(@Path("userId") int userId, @Body WithDrawBody body);


    @GET("/api/v2/gifts/mine")
    rx.Observable<ApiResp<CharmInfoEntity>> getAttractInfo(@Query("Start") int Start);

    @GET
    rx.Observable<ApiResp<CharmInfoEntity>> loadMoreGift(@Url  String url);

    /**
     * 照片点赞（2.5.1）
     *
     * @param imagePath 照片路径
     * @param isCancel  是否取消
     * @return
     */
    @PUT("api/v2/user/photo")
    @FormUrlEncoded
    rx.Observable<ApiResp<Boolean>> putPraise(

            @Field("imagePath") String imagePath

            , @Field("isCancel") boolean isCancel


    );

    /**
     * 删除形象视频
     *
     * @param UserId
     * @param VideoId
     * @return
     */
    @DELETE("api/v2/video/{UserId}/{VideoId}")
    rx.Observable<ApiResp<Boolean>> deleteVideo(

            @Path("UserId") int UserId

            , @Path("VideoId") int VideoId
    );

    /**
     * 上传形象视频
     *
     * @param userId
     * @param videoUrl    视频链接
     * @param coverImgUrl 封面图片，原始图片大小同上传图片
     * @return
     */
    @POST("api/v2/video/{userId}")
    @FormUrlEncoded
    rx.Observable<ApiResp<GeneralVideos>> postGeneralVideos(

            @Path("userId") int userId

            , @Field("videoUrl") String videoUrl


            , @Field("coverImgUrl") String coverImgUrl
    );

    /**
     * 基于邀约带礼物打招呼前置条件
     *
     * @param DatingId     邀约Id
     * @param TargetUserId 接收用户Id
     * @return
     */
    @GET("api/v3/datings/chat/requirements?")
    rx.Observable<ApiResp<GiftStatusWithDatingEntity>> getGiftStatusWithDating(@Query("DatingId") String DatingId, @Query("TargetUserId") int TargetUserId);


}
