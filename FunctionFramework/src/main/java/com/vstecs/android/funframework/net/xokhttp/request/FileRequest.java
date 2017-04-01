package com.vstecs.android.funframework.net.xokhttp.request;

import android.net.Uri;

import com.vstecs.android.funframework.net.okhttp.Call;
import com.vstecs.android.funframework.net.okhttp.Callback;
import com.vstecs.android.funframework.net.okhttp.Headers;
import com.vstecs.android.funframework.net.okhttp.MediaType;
import com.vstecs.android.funframework.net.okhttp.MultipartBody;
import com.vstecs.android.funframework.net.okhttp.Request;
import com.vstecs.android.funframework.net.okhttp.RequestBody;
import com.vstecs.android.funframework.net.okhttp.Response;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.params.type.FileTypeParam;
import com.vstecs.android.funframework.net.xokhttp.utils.FileUtils;
import com.vstecs.android.funframework.net.xokhttp.utils.L;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Description:上传文件的网络请求
 * 模拟form提交到服务器
 * 参数同样也是模拟form以post方式提交到服务器
 *
 * Created by zhouweiyong on 2016/1/14.
 */
public class FileRequest<T> extends ERequest {

    public FileRequest(String url, AjaxParams ajaxParams, OnNetSuccuss onNetSuccuss, OnNetError onError, Type typeOfT) {
        super(url, ajaxParams, onNetSuccuss, onError, typeOfT);
    }

    @Override
    public void netWork() {
        try {
            MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            Map<String, FileTypeParam> fileParams = ajaxParams.getFileParams();
            Set<String> keySet = fileParams.keySet();
            for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                String key = it.next();
                FileTypeParam fileTypeParam = fileParams.get(key);
                MediaType mediaType = null;

                mediaType = MediaType.parse(FileUtils.getMimeType(Uri.fromFile(fileTypeParam.file).toString()));

                RequestBody fileBody = RequestBody.create(mediaType, fileTypeParam.file);
                multiBuilder.addPart(Headers.of("Content-Disposition", String.format("form-data; name=%s;filename=%s", fileTypeParam.key, fileTypeParam.fileName)),fileBody);
            }

            Map<String,String> params = ajaxParams.getUrlParams();
            Set<String> paramKey = params.keySet();
            for (Iterator<String> it = paramKey.iterator();it.hasNext();){
                String key = it.next();
                String value = params.get(key);
                multiBuilder.addPart(Headers.of("Content-Disposition", String.format("form-data; name=%s", key)),RequestBody.create(null,value));
            }

            RequestBody requestBody =multiBuilder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .tag(tag)
                    .build();

            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onError.onError(new OkhttpError(e));
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
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            onNetSuccuss.onSuccess(result);
                        }
                    });
                }
            });
        } catch (Exception e) {
            onError.onError(new OkhttpError(e));
        }
    }
}
