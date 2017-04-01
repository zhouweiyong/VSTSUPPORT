package com.vstecs.android.funframework.net.xokhttp.request;

import com.vstecs.android.funframework.net.okhttp.Call;
import com.vstecs.android.funframework.net.okhttp.Callback;
import com.vstecs.android.funframework.net.okhttp.MediaType;
import com.vstecs.android.funframework.net.okhttp.Request;
import com.vstecs.android.funframework.net.okhttp.RequestBody;
import com.vstecs.android.funframework.net.okhttp.Response;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.utils.L;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Description:POST的网络请求
 * 以json方式提交
 * Created by zhouweiyong on 2016/1/6.
 */
public class PostRequest<T> extends ERequest {

    public PostRequest(String url, AjaxParams ajaxParams, OnNetSuccuss onNetSuccuss, OnNetError onError, Type typeOfT) {
        super(url, ajaxParams, onNetSuccuss, onError, typeOfT);
    }


    @Override
    public void netWork() {
        try {
            L.i(String.format("请求的URL:%s", url));
            Map<String, String> params = ajaxParams.getUrlParams();
            Set<String> keySet = params.keySet();
            final JSONObject jsonObject = new JSONObject();
            for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                String k = it.next();
                String v = params.get(k);
                jsonObject.put(k, v);
            }
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
            L.i(String.format("请求参数:%s", jsonObject.toString()));
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .tag(tag)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    netError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        String date = null;
                        String url = null;
                        if (response.headers() != null) date = response.header("Date", null);
                        if (response.request() != null) url = response.request().url().toString();
                        String json = response.body().string();
                        L.i(String.format("返回的信息:%s", json));
                        final T result = mGson.fromJson(json, typeOfT);
                        if (result instanceof RequestResult) {
                            ((RequestResult) result).dateStr = date;
                            ((RequestResult) result).url = url;
                        }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        netError(e);
                    }
                    mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                onNetSuccuss.onSuccess(result);
                            }
                        });
                }
            });
        } catch (Exception e) {
            netError(e);
        }
    }

    private void netError(final Exception e){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onError.onError(new OkhttpError(e));
            }
        });
    }
}
