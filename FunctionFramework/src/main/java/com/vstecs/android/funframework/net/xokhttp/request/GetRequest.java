package com.vstecs.android.funframework.net.xokhttp.request;

import com.vstecs.android.funframework.net.okhttp.Call;
import com.vstecs.android.funframework.net.okhttp.Callback;
import com.vstecs.android.funframework.net.okhttp.Request;
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
 * Description:GET方式的网络请求
 * Created by zhouweiyong on 2016/1/6.
 */
public class GetRequest<T> extends ERequest{
    public GetRequest(String url, AjaxParams ajaxParams, OnNetSuccuss onNetSuccuss, OnNetError onError, Type typeOfT) {
        super(url, ajaxParams, onNetSuccuss, onError, typeOfT);
    }


    @Override
    public void netWork() {
        try {
            Map<String, String> params = ajaxParams.getUrlParams();
            Set<String> keySet = params.keySet();
            final JSONObject jsonObject = new JSONObject();
            StringBuilder stringBuilder = new StringBuilder();
            for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                String k = it.next();
                String v = params.get(k);
                stringBuilder.append(k).append("=").append(v).append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            String requestUrl = String.format("%s?%s", url, stringBuilder.toString());
            L.i(String.format("请求的URL:%s", requestUrl));
            Request request = new Request.Builder().url(requestUrl).tag(tag).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onError.onError(new OkhttpError(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String date = null;
                        String url = null;
                        if (response.headers() != null) date = response.header("Date", null);
                        if (response.request() != null) url = response.request().url().toString();
                        String json = response.body().string();
                        L.i(String.format("返回结果:%s", json));
                        final T result = mGson.fromJson(json, typeOfT);
                        if (result instanceof RequestResult) {
                            ((RequestResult) result).dateStr = date;
                            ((RequestResult) result).url = url;
                        }
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                onNetSuccuss.onSuccess(result);
                            }
                        });
                    } catch (Exception e) {
                        onError.onError(new OkhttpError(e));
                    }
                }
            });
        } catch (Exception e) {
            onError.onError(new OkhttpError(e));
        }
    }
}
