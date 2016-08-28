package com.beautifish.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.beautifish.app.App;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpTool {

    private static final String TAG = HttpTool.class.getName();

    /**Get请求*/
    public static String httpGet(Context context, String url){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    //注册
    public static String httpPostRegister(Context context, String url, String name, String password, String phone, String wechat, String qq, String inviteCode){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("user_name", name)
                    .add("user_password", password)
                    .add("user_phone", phone)
                    .add("user_wechat", wechat)
                    .add("user_qq", qq)
                    .add("user_own", inviteCode)
                    .build();
            Request request = new Request.Builder()
                    //.addHeader("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (IOException e){
                result = null;
            }
        }
        return result;
    }

    /**登陆*/
    public static String httpPostLogin(Context context, String url, String name, String password){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("user_phone", name)
                    .add("user_password", password)
                    .build();
            Request request = new Request.Builder()
                    //.addHeader("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**充值*/
    public static String httpPostInMoney(Context context, String url, String fromUserId, String toUserId, String moneyAmount, String moneyID, String moneyMsgDirect, String takenMoney){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("fromUserId", fromUserId)
                    .add("toUserId", toUserId)
                    .add("moneyAmount", moneyAmount)
                    .add("moneyID", moneyID)
                    .add("moneyMsgDirect", moneyMsgDirect)
                    .add("takenMoney", takenMoney)
                    .build();
            Request request = new Request.Builder()
                    //.addHeader("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                App.log(TAG, "--------code--------->"+response.code());
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**充值历史记录*/
    public static String httpPostInMoneyHistory(Context context, String url, String fromUserId){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("fromUserId", fromUserId)
                    .build();
            Request request = new Request.Builder()
                    //.addHeader("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                App.log(TAG, "--------code--------->"+response.code());
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**打开红包*/
    public static String httpPostUpdateRedPacket(Context context, String url, String moneyID){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("moneyID", moneyID)
                    .build();
            Request request = new Request.Builder()
                    //.addHeader("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                App.log(TAG, "--------code--------->"+response.code());
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**提现*/
    public static String httpPostOutMoney(Context context, String url, String phone, String amount){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("w_phone", phone)
                    .add("w_price", amount)
                    .build();
            Request request = new Request.Builder()
                    //.addHeader("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                App.log(TAG, "--------code--------->"+response.code());
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**充值历史记录*/
    public static String httpPostOutMoneyHistory(Context context, String url, String w_phone){
        String result = null;
        if(isNetworkConnected(context)){
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("w_phone", w_phone)
                    .build();
            Request request = new Request.Builder()
                    //.addHeader("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                App.log(TAG, "--------code--------->"+response.code());
                if(response.code()==200){
                    result = response.body().string();
                }else {
                    result = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**检查网络是否可用*/
    private static boolean isNetworkConnected(Context context) {
        boolean flag = false;
        if(context != null){
            ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connManager.getActiveNetworkInfo();
            if(netInfo != null){
                if (netInfo.isConnectedOrConnecting()) {
                    flag = true;
                }else{
                    flag = false;
                }
            }
        }
        return flag;
    }

}
