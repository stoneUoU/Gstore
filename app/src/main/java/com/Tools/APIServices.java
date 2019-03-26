package com.Tools;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class APIServices {

    //操作成功
    public static final int CODE_SUCCESS = 0;
    //系统错误
    public static final int CODE_SYS_ERROR = 9999;

    //会话超时
    public static final int CODE_TOKEN_EXPIRED = 1001;
    //用户凭证校验失败
    public static final int CODE_TOKEN_INVALID = 1002;
    //用户凭证和API不匹配
    public static final int CODE_TOKEN_NOT_RIGHT_MATCHED_API = 1003;
    //用户凭证不存在
    public static final int TOKEN_NOT_FOUND = 1004;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //    // 测试环境【4期】
    public static final String API_PATH = "https://app.shop.znrmny.com/api/ggshop/member/v5/";
    public static final String API_WEB = "http://10.10.0.62:13380/";
    public static final String API_IMAGE = "http://10.10.0.62:22005/";
    public static final String WEBSOCKET = "http://10.10.0.62:22009?token=";
    public static final String NETWORK_ERROR = "网络开小差了，请稍后再试";

    /**
     * get请求
     * @param realPath 真实地址
     * @param token token
     * @param callback 回掉
     */
    public static void sendGetRequest(String realPath, String token, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        if(null == token){
            request = new Request.Builder()
                    .get()
                    .url(realPath)
                    .build();
        }else{
            request = new Request.Builder()
                    .get()
                    .url(realPath)
                    .header("token", token)
                    .build();
        }
        client.newCall(request).enqueue(callback);
    }

    public static void sendPostRequest(String realPath, String token,
                                       JSONObject requestJSON, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        RequestBody requestBody = null;
        if(null != requestJSON){ //表示存在JSON数据，创建请求体
            requestBody = RequestBody.create(JSON, requestJSON.toString());
        }else{
            requestBody = RequestBody.create(JSON, new JSONObject().toString());
        }
        if(null == token){
            request = new Request.Builder()
                    .url(realPath)
                    .post(requestBody)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(realPath)
                    .post(requestBody)
                    .header("token", token)
                    .build();
        }
        client.newCall(request).enqueue(callback);
    }

    //图片上传
    public static void postImageUpload(File file, String realPath, String token, Callback callback){
        MediaType type=MediaType.parse("image/png");
        RequestBody fileBody=RequestBody.create(type,file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=image; filename=" + file.getName())
                        , fileBody)
                .build();
        Request request = new Request.Builder()
                .url(realPath)
                .addHeader("token", token)
                .header("Content-Type","text/html; charset=utf-8;")
                .post(body)
                .build();
        new OkHttpClient().newCall(request).enqueue(callback);
    }
}

