package com.smartvision.creativeminds.Connection;

import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTool {

    //--------------------------------------------------------------------
    private Retrofit retrofit = null;

    public Retrofit getRetrofit(String baseURL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
//                    .client(Utils.getClient())
                    .addConverterFactory(GsonConverterFactory.create()).client(getClient())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }


    public ServerTool.ApiInterface getAPIBuilder(String baseURL) {
        ServerTool.ApiInterface apiService = getRetrofit(baseURL).create(ServerTool.ApiInterface.class);
//        RetrofitInterface retrofitInterface = new RestAdapter.Builder().setEndpoint(API.API_URL).build().create(RetrofitInterface.class);
        return apiService;
    }

    public abstract static class APICallBack<T> implements Callback<T> {
        private static final String TAG = "RetrofitTool";

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            final Gson gson = new Gson();
            Log.wtf(TAG, response.raw().toString());
            Log.d(TAG, gson.toJson(response.body()) + "");
            Log.d(TAG, call.request().url().toString());


            if (response.isSuccessful()) {// response.isSuccessful() is true if the response code is 2xx
                onSuccess(response.body());
            } else {

                int statusCode = response.code();
                ResponseBody errorBody = response.errorBody();
                onFailed(statusCode, errorBody);//return

            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.d("Excaption", t.getMessage() + "");
            onFailed(0, null);
            Log.d(TAG, call.request().url().toString());
//            connection failed
//            onFailed(-1);

        }

        public abstract void onSuccess(T response);

        public abstract void onFailed(int statusCode, ResponseBody responseBody);

    }

}