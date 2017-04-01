package com.vstecs.android.funframework.net.xokhttp;

import android.text.TextUtils;

import com.vstecs.android.funframework.net.okhttp.Request;
import com.vstecs.android.funframework.net.xokhttp.utils.L;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/8.
 */
public class OkhttpError {
    public Request request;//请求的信息
    public String errMsg;//自定义错误消息
    public Exception exception;//错误信息
    /**
     * 500：网络连接错误
     * 400：
     * 300：解析错误
     * 600:自定义错误，或其它未知错误，需要进一步明确
     */
    public int errorCode;//错误码

    public OkhttpError(Request request, Exception exception) {
        this.request = request;
        this.exception = exception;
        initCode();
    }

    public OkhttpError(Exception exception) {
        this.exception = exception;
        initCode();
    }

    public OkhttpError(String errMsg) {
        this.errMsg = errMsg;
    }

    public void initCode() {
        errMsg = "网络错误！";
        if (exception != null) {
            if (!TextUtils.isEmpty(exception.getMessage())) {
                errMsg = exception.getMessage();
            }
            if (errMsg.equals("Failed to connect")) {
                errorCode = 500;
            } else if (errMsg.equals("IllegalStateException")) {
                errorCode = 300;
            } else {
                errorCode = 600;
            }
        }
        L.i(String.format("ErrorMessage:%s", errMsg));

    }
}
