package com.vst.vstsupport;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.vst.vstsupport.mode.bean.BusinessDepartmentBean;
import com.vst.vstsupport.mode.bean.SecendLevelLineBean;
import com.vst.vstsupport.mode.bean.UserBean;
import com.vstecs.android.funframework.utils.CrashHandler;

import java.util.ArrayList;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/14
 * class description:请输入类描述
 */
public class VstApplication extends Application{
    private static VstApplication instance;
    private UserBean userBean;
    private ArrayList<BusinessDepartmentBean> departmentBeans=new ArrayList<BusinessDepartmentBean>();//事业部结果集
    private ArrayList<SecendLevelLineBean> secendLevelLineList=new ArrayList<SecendLevelLineBean>();//根据事业部获取二级线

    public static VstApplication getInstance(){
        return instance;
    }

    public ArrayList<SecendLevelLineBean> getSecendLevelLineList() {
        return secendLevelLineList;
    }

    public void setSecendLevelLineList(ArrayList<SecendLevelLineBean> secendLevelLineList) {
        this.secendLevelLineList = secendLevelLineList;
    }

    public ArrayList<BusinessDepartmentBean> getDepartmentBeans() {
        return departmentBeans;
    }

    public void setDepartmentBeans(ArrayList<BusinessDepartmentBean> departmentBeans) {
        this.departmentBeans = departmentBeans;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    @Override
    public void onCreate() {
        super.onCreate();
           		// 初始化 JPush
        if ("com.vst.vstsupport".equals(getCurrProcName())){
            instance = this;
            CrashHandler.create(this);
            JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志

            //初始化
            JPushInterface.init(this);
            JPushInterface.setDebugMode(true);
            //设置通知栏上显示的消息数量，超过则会覆盖
            JPushInterface.setLatestNotificationNumber(this,1);
            //设置消息在通知栏的显示样式
            setJPushNotify();

            JPushInterface.stopPush(this);

            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
            MobclickAgent.openActivityDurationTrack(false);
            MobclickAgent.setCatchUncaughtExceptions(true);
        }
    }

    private String getCurrProcName() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    //设置消息在通知栏的显示样式
    private void setJPushNotify(){
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.ic_start;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        //设置默认样式
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }



}
