package com.vstecs.android.funframework.net.xokhttp.utils;

import android.util.Log;


/**
 * Description:
 * Created by zhouweiyong on 2016/1/8.
 */
public class L {
    private static final String TAG = "okhttp";
//    public static boolean isDebug = BuildConfig.DEBUG;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static boolean isDebug = true;
    private L()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void i(String msg)
    {
        if (isDebug)
            Log.i(TAG, msg);
    }
    public static void e(String msg)
    {
        if (isDebug)
            Log.e(TAG, msg);
    }
}
