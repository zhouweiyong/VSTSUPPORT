package com.vstecs.android.funframework.net.xokhttp;

import android.os.Handler;
import android.os.Looper;

import com.vstecs.android.funframework.BuildConfig;
import com.vstecs.android.funframework.net.okhttp.Cache;
import com.vstecs.android.funframework.net.okhttp.Call;
import com.vstecs.android.funframework.net.okhttp.OkHttpClient;
import com.vstecs.android.funframework.net.xokhttp.cookie.SimpleCookieJar;
import com.vstecs.android.funframework.net.xokhttp.https.HttpsUtils;
import com.vstecs.android.funframework.net.xokhttp.request.ERequest;
import com.vstecs.android.funframework.net.xokhttp.utils.L;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/6.
 */
public class OkHttpManage {

    private static OkHttpManage mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;


    private OkHttpManage() {
        mDelivery = new Handler(Looper.getMainLooper());

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        okHttpClientBuilder.connectTimeout(100,TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(50,TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(50,TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            /**
             * 忽略服务器的证书验证
             * 仅仅用于测试阶段，不建议用于发布后的产品中
             */
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

        mOkHttpClient = okHttpClientBuilder.build();


    }

    public static OkHttpManage getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManage.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManage();
                }
            }
        }
        return mInstance;
    }


    public void execute(ERequest eRequest) {
        eRequest.mOkHttpClient = mOkHttpClient;
        eRequest.mDelivery = mDelivery;
        eRequest.netWork();
    }

    public void execute(ERequest eRequest,Object tag) {
        eRequest.mOkHttpClient = mOkHttpClient;
        eRequest.mDelivery = mDelivery;
        eRequest.tag = tag;
        eRequest.netWork();
    }

    /**
     * 按照标记取消请求
     * @param tag
     */
    public void cancelRequest(Object tag){
        for (Call call : mOkHttpClient.dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }

    /**
     * 取消全部请求
     */
    public void cancelAllRequest(){
        mOkHttpClient.dispatcher().cancelAll();
    }

    /**
     * 设置https证书
     * @param certificates
     */
    public OkHttpManage setCertificates(InputStream... certificates)
    {
        mOkHttpClient = mOkHttpClient.newBuilder()
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null))
                .build();
        return this;
    }



    /**
     * 设置连接超时时间
     * @param timeout
     * @param units
     */
    public OkHttpManage setConnectTimeout(int timeout, TimeUnit units)
    {
        mOkHttpClient = mOkHttpClient.newBuilder()
                .connectTimeout(timeout, units)
                .build();
        return this;
    }

    /**
     * 设置缓存
     * @param cacheDirectory
     */
    public OkHttpManage setCache(File cacheDirectory){
        int cacheSize = 10*1024*1024;//10MB
        Cache cache = new Cache(cacheDirectory,cacheSize);
        mOkHttpClient =mOkHttpClient.newBuilder().cache(cache).build();
        return this;
    }

    public void isDebug(boolean isDebug) {
        L.isDebug = isDebug;
    }
}
