package cn.chono.yopper.entity.exception;

import android.text.TextUtils;

import com.google.gson.Gson;

import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.task.RemoveAliasAsyncTask;
import retrofit2.adapter.rxjava.HttpException;

/**
 *
 */
public class ErrorHanding {

    public ErrorHanding() {
    }

    public static String handleError(Throwable throwable) {
        String message;
        if (throwable instanceof ServerException) {
            message = throwable.getMessage();
        } else {

            message = "网络异常，请稍后重试";

        }
        return message;
    }

    public static ApiResp handle(Throwable throwable) {

        if (throwable instanceof HttpException) {

            HttpException error = (HttpException) throwable;


            try {


                String msg = error.response().errorBody().string();

                ApiResp apiResp = new Gson().fromJson(msg, ApiResp.class);

                if (apiResp == null) {

                    apiResp = new ApiResp();

                    apiResp.setMsg("网络异常，请稍后重试");

                    return apiResp;

                }


                if (TextUtils.equals("500", apiResp.getStatus())) {

                    apiResp.setMsg("网络异常，请稍后重试");

                    return apiResp;
                }


                if (TextUtils.equals("401", apiResp.getStatus()) || TextUtils.equals("403", apiResp.getStatus())) {

                    String authToken = LoginUser.getInstance().getAuthToken();


                    if (!TextUtils.isEmpty(authToken)) {

                        RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();

                        loginAsyncTask.execute(apiResp.getMsg());

                        return apiResp;

                    } else {

                        apiResp.setMsg("网络异常，请稍后重试");

                        return apiResp;

                    }


                } else {


                    return apiResp;

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            ApiResp apiResp = new ApiResp();

            apiResp.setMsg("连接失败，请稍后重试");

            return apiResp;


        }

        ApiResp apiResp = new ApiResp();

        apiResp.setMsg("连接失败，请稍后重试");

        return apiResp;
    }
}
