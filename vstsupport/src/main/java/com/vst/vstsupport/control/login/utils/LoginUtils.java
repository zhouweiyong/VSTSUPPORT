package com.vst.vstsupport.control.login.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.message.jpush.JPushUtils;
import com.vst.vstsupport.mode.bean.UserBean;
import com.vst.vstsupport.utils.PreferHelper;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkHttpManage;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/22
 * class description:请输入类描述
 */
public class LoginUtils {
    private static LoginUtils instance;
    private boolean isAutoLogin = false;
    private BaseAct baseAct;


    public LoginUtils(boolean isAutoLogin, BaseAct baseAct) {
        this.isAutoLogin = isAutoLogin;
        this.baseAct = baseAct;
    }

    public static void logout() {
        VstApplication.getInstance().setUserBean(null);
        PreferHelper.getInstance().clearLoginInfo();
//        JPushUtils jPushUtils = new JPushUtils();
//        jPushUtils.setTag(null);
        MobclickAgent.onProfileSignOff();
        JPushInterface.stopPush(VstApplication.getInstance());
    }

    public static boolean isAutoLogin() {
        String userName = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_NAME);
        String password = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PWD);
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            return true;
        }
        return false;
    }

    public void setAutoLogin(boolean autoLogin) {
        isAutoLogin = autoLogin;
    }

    public void login(final String userName, final String password) {
        baseAct.showLoadingDialog("");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("account", userName);
        ajaxParams.putCommonTypeParam("password", password);

        PostRequest<RequestResult<UserBean>> request = new PostRequest<RequestResult<UserBean>>(UrlConstants.USERLOG, ajaxParams, new OnNetSuccuss<RequestResult<UserBean>>() {
            @Override
            public void onSuccess(RequestResult<UserBean> response) {
                if (response.success) {
                    UserBean userBean = response.rs;
                    VstApplication.getInstance().setUserBean(userBean);
                    if (isAutoLogin) {
                        PreferHelper.getInstance().saveLoginInfo(userName, password);
                    }else {
                        PreferHelper.getInstance().setString(PreferHelper.KEY_LOGIN_NAME,userName);
                    }
                    if (JPushInterface.isPushStopped(VstApplication.getInstance())){
                        JPushInterface.resumePush(VstApplication.getInstance());
                    }
                    JPushUtils jPushUtils = new JPushUtils();
                    jPushUtils.setTag(userBean.getAccount());
                    Log.i("vst", response.msg);
                    MobclickAgent.onProfileSignIn(userBean.getAccount());
                    baseAct.finish();
                    EventBus.getDefault().post(new EventCenter<String>(1, "success"));
                } else {
                    baseAct.showToastMsg(response.msg);
                }
                baseAct.dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                baseAct.showToastMsg(okhttpError.errMsg);
                Log.i("vst", okhttpError.errMsg);
                EventBus.getDefault().post(new EventCenter<String>(1, "fail"));
                baseAct.dismissLoadingDialog();
            }
        }, new TypeToken<RequestResult<UserBean>>() {
        }.getType());
        OkHttpManage.getInstance().execute(request);
    }

    public void autoLogin() {
        String userName = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_NAME);
        String password = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PWD);
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            login(userName, password);
        }
    }

}
