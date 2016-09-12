package cn.chono.yopper.api;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.base.App;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.task.RemoveAliasAsyncTask;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 *  Created by SQ on 16/7/12.
 */
public class RxResultHelper {

    public static <T> Observable.Transformer<ApiResp<T>, T> handleResult() {
        return apiResponseObservable -> apiResponseObservable.flatMap(new Func1<ApiResp<T>, Observable<T>>() {
            @Override
            public Observable<T> call(ApiResp<T> tApiResponse) {

                Logger.e("tApiResponse="+tApiResponse.toString());

                if (tApiResponse.isSuccess()) {
                    //表示成功
                    return createData(tApiResponse.getResp());

                } else if(TextUtils.equals("500", tApiResponse.getStatus())){

                    return Observable.error(new ServerException("连接失败，请稍后重试"));

                } else if(TextUtils.equals("401", tApiResponse.getStatus()) || TextUtils.equals("403", tApiResponse.getStatus())){

                    String authToken = LoginUser.getInstance().getAuthToken();


                    if (!TextUtils.isEmpty(authToken)) {

                        RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();
                        loginAsyncTask.execute(tApiResponse.getMsg());

                        return Observable.error(null);

                    }

                    return Observable.error(new ServerException("连接失败，请稍后重试"));

                }else{

                    return Observable.error(new ServerException(tApiResponse.getMsg()));
                }
            }
        });
    }


    public static <T> Observable<T> createData(T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {



                    subscriber.onError(e);


                }
            }
        });
    }
}

