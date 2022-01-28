package cn.finalteam.rxgalleryfinal.sample.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import cn.finalteam.rxgalleryfinal.sample.CompareActivity;
import cn.finalteam.rxgalleryfinal.sample.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.finalteam.rxgalleryfinal.sample.utils.Constant;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class uploadImage {
    public int type;
    private static final String TAG="Upload";

    public uploadImage(){
        type = -1;
    }
    public void uploadImage(String imagePath) {
        new NetworkTask().execute(imagePath);
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!"error".equals(result)) {
               // Log.i(TAG, "图片地址 " + Constant.BASE_URL + result);
                //Log.i(TAG, "图片类型 " + Constant.BASE_URL + result);
                Log.i(TAG, "图片类型 "  + result);

                /*
                Glide.with(mContext)
                        .load(Constant.BASE_URL + result)
                        .into(mPictureIv);*/
            }
        }
    }

    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constant.BASE_URL + "/uploadimage")
                .post(requestBody)
                .build();

        Log.d(TAG, "请求地址 " + Constant.BASE_URL + "/uploadimage");
        try{
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d(TAG, "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d(TAG, "响应体 " + resultValue);
                type = Integer.parseInt(resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
