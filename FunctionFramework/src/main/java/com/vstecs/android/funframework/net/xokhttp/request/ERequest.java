package com.vstecs.android.funframework.net.xokhttp.request;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vstecs.android.funframework.net.okhttp.OkHttpClient;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;

import java.lang.reflect.Type;

/**
 * Description:网络请求的基类
 * Created by zhouweiyong on 2016/1/7.
 */
public abstract class ERequest<T> implements IRequest{
    public String url;
    public AjaxParams ajaxParams;
    public OnNetSuccuss<T> onNetSuccuss;
    public OnNetError onError;
    public Type typeOfT;
    public OkHttpClient mOkHttpClient;
    public Handler mDelivery;
    public Gson mGson = new GsonBuilder().serializeNulls().create();
    public Object tag;

    public ERequest(String url, AjaxParams ajaxParams, OnNetSuccuss<T> onNetSuccuss, OnNetError onError, Type typeOfT) {
        this.url = url;
        this.ajaxParams = ajaxParams;
        this.onNetSuccuss = onNetSuccuss;
        this.onError = onError;
        this.typeOfT = typeOfT;
    }

}
