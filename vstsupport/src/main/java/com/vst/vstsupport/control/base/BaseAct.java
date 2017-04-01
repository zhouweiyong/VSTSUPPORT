package com.vst.vstsupport.control.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.vst.vstsupport.R;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkHttpManage;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.request.ERequest;
import com.vstecs.android.funframework.netstatus.NetChangeObserver;
import com.vstecs.android.funframework.netstatus.NetStateReceiver;
import com.vstecs.android.funframework.netstatus.NetUtils;
import com.vstecs.android.funframework.ui.activity.BaseActivity;
import com.vstecs.android.uiframework.view.LoadingDialog;
import com.vstecs.android.uiframework.view.jazzyviewpage.ToastMsg;
import com.vstecs.android.uiframework.view.loadcontrol.VaryViewHelperController;

import java.util.HashSet;
import java.util.Set;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public abstract class BaseAct extends BaseActivity implements
        OnCancelListener, OnNetError {
    /**
     * 分页加载数据，每页数据量
     */
    public final int PAGE_SIZE = 5;
    /**
     * 当前页，用于分页加载数据
     */
    public int CURRENT_PAGE = 1;
    public LoadingDialog loadingDialog;
    protected LayoutInflater inflater;
    protected Context context;
    /**
     * network status
     */
    protected NetChangeObserver mNetChangeObserver = null;
    private VaryViewHelperController mVaryViewHelperController = null;
    private Set<Object> tags = new HashSet<Object>();

    public BaseAct() {
        setHiddenActionBar(true);
        // setBackListener(false);
        // setScreenOrientation(ScreenOrientation.VERTICAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getOverridePendingTransitionMode() != null) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }


        context = this;
        inflater = LayoutInflater.from(context);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }

        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };

        NetStateReceiver.registerObserver(mNetChangeObserver);
        NetStateReceiver.registerNetworkStateReceiver(this);
        super.onCreate(savedInstanceState);
    }

    protected void initLoadingDialog(boolean isTrans) {
        if (null == loadingDialog) {
            loadingDialog = new LoadingDialog(this, isTrans);
            loadingDialog.setOnCancelListener(this);
        }
    }

    public void showLoadingDialog(String parameter) {
        initLoadingDialog(true);//透明
        loadingDialog.setTitle(parameter);
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (null != loadingDialog) {
            LoadingDialog.dismissDialog(loadingDialog);
        }
    }

    public void showToastMsg(String msg) {
        ToastMsg.showToastMsg(this, msg);
    }

    public void showToastMsg(int strId) {
        ToastMsg.showToastMsg(this, strId);
    }

    protected void executeRequest(ERequest eRequest) {
        OkHttpManage.getInstance().execute(eRequest);
    }

    protected void executeRequest(ERequest eRequest, Object tag) {
        OkHttpManage.getInstance().execute(eRequest, tag);
    }

    protected void cancelRequest(Object tag) {
        OkHttpManage.getInstance().cancelRequest(tag);
    }

    protected void cancelAllRequest() {
        OkHttpManage.getInstance().cancelAllRequest();
    }


    @Override
    public void onError(OkhttpError okhttpError) {
        dismissLoadingDialog();
        if (CURRENT_PAGE > 1) {// 加载异常回退到当前页
            CURRENT_PAGE--;
        }
        String msg = "网络异常";
        if (!TextUtils.isEmpty(okhttpError.errMsg)) {
            msg = okhttpError.errMsg;
        }
        showToastMsg(msg);
    }


    /* 获取输入内容 */
    public String getInputStr(EditText et) {
        return et.getText().toString().trim();
    }


    //-----------------------带动画的跳转结束--------------------------------------------

    /**
     * loadingdialog取消监听
     */
    @Override
    public void onCancel(DialogInterface dialog) {

    }


    /** 覆盖页 end ******************************/

    /**
     * @Description: TODO:：关闭键盘事件
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (getOverridePendingTransitionMode() != null) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in_disappear, R.anim.scale_out_disappear);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in_disappear, R.anim.fade_out_disappear);
                    break;
            }
        }
    }

    protected abstract TransitionMode getOverridePendingTransitionMode();

    /**
     * is bind eventBus
     * 绑定eventBus
     *
     * @return
     */
    protected abstract boolean isBindEventBusHere();

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            onEventComming(eventCenter);
        }
    }

    /**
     * when event comming
     * 接收eventBus事件，如果isBindEventBusHere返回false则没有事件传递
     *
     * @param eventCenter
     */
    protected abstract void onEventComming(EventCenter eventCenter);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * get loading target view
     */
    protected abstract View getContainerTargetView();

    /**
     * network connected
     */
    protected abstract void onNetworkConnected(NetUtils.NetType type);

    /**
     * network disconnected
     */
    protected abstract void onNetworkDisConnected();

    protected void showLoadingView() {
        toggleShowLoading(true, null);
    }

    protected void showErrorView() {
        toggleShowError(true, null, null);
    }

    protected void showNONetView(OnClickListener onClickListener) {
        toggleNetworkError(true, onClickListener);
    }

    protected void showNODataView() {
        toggleShowEmpty(true, null, null, R.mipmap.ic_error);
    }

    protected void stopAllView() {
        toggleShowLoading(false, null);
    }

    protected VaryViewHelperController getVaryViewHelperController() {
        if (getContainerTargetView() != null && mVaryViewHelperController == null) {
            return mVaryViewHelperController = new VaryViewHelperController(getContainerTargetView());
        }
        return mVaryViewHelperController;
    }

    /**
     * toggle show common_over_loading
     *
     * @param toggle
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == getVaryViewHelperController()) {
            throw new IllegalArgumentException("You must return a right target view for common_over_loading");
        }
        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg, OnClickListener onClickListener, int imageId) {
        if (null == getVaryViewHelperController()) {
            throw new IllegalArgumentException("You must return a right target view for common_over_loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener, imageId);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener, int imageId, boolean isNeedMargin) {
        if (null == getVaryViewHelperController()) {
            throw new IllegalArgumentException("You must return a right target view for common_over_loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener, imageId, isNeedMargin);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show error
     *
     * @param toggle
     */
    protected void toggleShowError(boolean toggle, String msg, OnClickListener onClickListener) {
        if (null == getVaryViewHelperController()) {
            throw new IllegalArgumentException("You must return a right target view for common_over_loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show network error
     *
     * @param toggle
     */
    protected void toggleNetworkError(boolean toggle, OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for common_over_loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }
}
