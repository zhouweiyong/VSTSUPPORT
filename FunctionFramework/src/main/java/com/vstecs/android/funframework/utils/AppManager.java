package com.vstecs.android.funframework.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.vstecs.android.funframework.net.xokhttp.utils.L;

import java.util.List;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/8/17
 * class description:请输入类描述
 */
public class AppManager {

    /**
     * 判断应用是否在运行
     * @param pkgName
     * @param context
     * @return
     */
    public static  boolean isAppRunning(String pkgName,Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(pkgName) || info.baseActivity.getPackageName().equals(pkgName)) {
                isAppRunning = true;
                L.i(info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }
}
