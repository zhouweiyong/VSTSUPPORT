package com.vst.vstsupport.control.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.vst.vstsupport.R;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkHttpManage;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.request.ERequest;
import com.vstecs.android.funframework.ui.fragment.BaseFragment;
import com.vstecs.android.uiframework.view.LoadingDialog;
import com.vstecs.android.uiframework.view.jazzyviewpage.ToastMsg;
import com.vstecs.android.uiframework.view.loadcontrol.VaryViewHelperController;

import java.util.HashSet;
import java.util.Set;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public abstract class BaseFra extends BaseFragment implements OnCancelListener, OnClickListener,OnNetError {
	/** 分页加载数据，每页数据量 */
	protected static final int PAGE_SIZE = 10;
	protected   BaseAct activity;
	/** 当前页，用于分页加载数据 */
	protected int CURRENT_PAGE = 1;
	protected LoadingDialog loadingDialog;
	private VaryViewHelperController mVaryViewHelperController = null;
	private Set<Object> tags = new HashSet<Object>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseAct) getActivity();
		if (isBindEventBusHere()) {
			EventBus.getDefault().register(this);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);



	}

	protected void initLoadingDialog(boolean isTrans){
		if (null == loadingDialog) {
			loadingDialog = new LoadingDialog(getActivity(), isTrans);
			loadingDialog.setOnCancelListener(this);
		}
	}

	public void showLoadingDialog(String parameter) {
		initLoadingDialog(true);//透明
		loadingDialog.setTitle(parameter);
		if(!loadingDialog.isShowing())
			loadingDialog.show();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	@Override
	public void onCancel(DialogInterface dialog) {}

	public void dismissLoadingDialog() {
		if (null != loadingDialog) {
			LoadingDialog.dismissDialog(loadingDialog);
		}
	}

	public void showToastMsg(String msg) {
		ToastMsg.showToastMsg(getActivity(), msg);
	}

	public void showToastMsg(int strId) {
		ToastMsg.showToastMsg(getActivity(), strId);
	}

	/**覆盖页 end******************************/

	/* 获取输入内容 */
	public String getInputStr(EditText et) {
		return et.getText().toString().trim();
	}

	/**
	 * @Description:关闭键盘事件
	 * @author: 刘成伟（wwwlllll@126.com）
	 * @Title: closeInput
	 * @param
	 * @return void
	 * @throws
	 * @date 2014-4-6 上午11:18:36
	 */
	public void closeInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * get loading target view
	 */
	protected abstract View getContainerTargetView();

	/**
	 * is bind eventBus
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
	 *
	 * @param eventCenter
	 */
	protected abstract void onEventComming(EventCenter eventCenter);

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isBindEventBusHere()) {
			EventBus.getDefault().unregister(this);
		}
	}

	protected void showLoadingView(){
		toggleShowLoading(true, null);
	}

	protected void showErrorView(){
		toggleShowError(true, null, null);
	}

	protected void showNONetView(OnClickListener onClickListener){
		toggleNetworkError(true, onClickListener);
	}

	protected void showNODataView(){
		toggleShowEmpty(true, null, null, R.mipmap.ic_error);
	}

	protected void stopAllView(){
		toggleShowLoading(false, null);
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
	protected void toggleShowEmpty(boolean toggle, String msg, OnClickListener onClickListener,int imageId) {
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
	protected void toggleShowEmpty(boolean toggle, String msg, OnClickListener onClickListener,int imageId,boolean isNeedMargin) {
		if (null == getVaryViewHelperController()) {
			throw new IllegalArgumentException("You must return a right target view for common_over_loading");
		}

		if (toggle) {
			mVaryViewHelperController.showEmpty(msg, onClickListener,imageId,isNeedMargin);
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
		if (null == getVaryViewHelperController()) {
			throw new IllegalArgumentException("You must return a right target view for common_over_loading");
		}

		if (toggle) {
			mVaryViewHelperController.showNetworkError(onClickListener);
		} else {
			mVaryViewHelperController.restore();
		}
	}

    protected VaryViewHelperController getVaryViewHelperController() {
        if (getContainerTargetView() != null && mVaryViewHelperController==null) {
            return mVaryViewHelperController = new VaryViewHelperController(getContainerTargetView());
        }
        return mVaryViewHelperController;
    }

	protected void executeRequest(ERequest eRequest){
		OkHttpManage.getInstance().execute(eRequest);
	}

	protected void executeRequest(ERequest eRequest,Object tag){
		OkHttpManage.getInstance().execute(eRequest,tag);
	}

	protected void cancelRequest(Object tag){
		OkHttpManage.getInstance().cancelRequest(tag);
	}

	protected void cancelAllRequest(){
		OkHttpManage.getInstance().cancelAllRequest();
	}

	@Override
	public void onError(OkhttpError okhttpError) {
		dismissLoadingDialog();
		if (CURRENT_PAGE > 1) {// 加载异常回退到当前页
			CURRENT_PAGE--;
		}
		String msg = "网络异常";
		if (!TextUtils.isEmpty(okhttpError.errMsg)){
			msg = okhttpError.errMsg;
		}
		showToastMsg(msg);
	}

	/**
	 * 嵌套viewpager时，第一次显示回调
	 * @author fanxing 创建于 Dec 3, 2014
	 */
	public interface OnInitShowListener{
		void onInitShow();
	}



	/**要在Fragment中监听按键需要实现该接口*/
	public interface FragmentKeyDown{
		boolean onFraKeyDown(int keyCode, KeyEvent event);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}
}
