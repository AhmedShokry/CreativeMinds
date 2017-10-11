package com.smartvision.creativeminds.Connection;

import android.content.Context;
import android.util.Log;

import com.smartvision.creativeminds.models.JsonResponseModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class ServerTool {


    public interface ApiInterface {



        @GET(URLS.URL_GET_DATA)
        Call<List<JsonResponseModel>> getJsonData(@Query("page") int pageNumber, @Query("per_page")int perPage);

    }

    public static void getJsonData(final Context context,int pageNumber,int perPage, final APICallBack apiCallBack) {

        final RetrofitTool retrofitTool = new RetrofitTool();
        retrofitTool.getAPIBuilder(URLS.URL_BASE).getJsonData(pageNumber,perPage).enqueue(new RetrofitTool.APICallBack<List<JsonResponseModel>>() {


            @Override
            public void onSuccess(List<JsonResponseModel> response) {
                apiCallBack.onSuccess(response);

            }

            @Override
            public void onFailed(int statusCode, ResponseBody responseBody) {
                apiCallBack.onFailed(statusCode, responseBody);


            }
        });
    }




    private static <M> void makeRequestToken(final Context context, Call call, final APICallBack apiCallBack, final RetrofitTool retrofitTool) {
        call.enqueue(new RetrofitTool.APICallBack<M>() {
            @Override
            public void onSuccess(M response) {
                apiCallBack.onSuccess(response);


            }

            @Override
            public void onFailed(int statusCode, ResponseBody responseBody) {
                //Hide loading
                handleGeneralFailure(context, statusCode, responseBody, retrofitTool);
                apiCallBack.onFailed(statusCode, responseBody);

            }
        });
    }

    private static void handleGeneralFailure(Context context, int statusCode, ResponseBody responseBody, RetrofitTool retrofitTool) {
        Retrofit retrofit = retrofitTool.getRetrofit(URLS.URL_BASE);
        Log.d("statusCode", statusCode + "");

    }

    public static interface APICallBack<T> {
        void onSuccess(T response);

        void onFailed(int statusCode, ResponseBody responseBody);

    }

}
