package com.vstecs.android.funframework.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.vstecs.android.funframework.ui.IBroadcastReg;
import com.vstecs.android.funframework.ui.KJActivityManager;


/**
 * update log:
 * 
 * @1.5 abstract protocol: I_KJActivity
 * @1.6 add method initThreadData()
 * @1.7 add abstract protocol:I_SkipActivity
 */

/**
 * Activity's framework,the developer shouldn't extends it<br>
 * <b>创建时间</b> 2014-3-1 <br>
 * <b>最后修改时间</b> 2014-5-30<br>
 * 直接兼容2.3系统或v4包的话得直接继承这个类
 * 
 * @version 1.7
 */
public abstract class KJFrameActivity extends FragmentActivity implements OnClickListener, IBroadcastReg, IKJActivity, ISkipActivity {
	/**
	 * initialization data. And this method run in background thread, so you
	 * shouldn't change ui
	 */
	protected void initDataFromThread() {}

	/** initialization data */
	protected void initData(Intent intent) {}

	/** initialization widget */
	protected void initWidget() {}

	/** initialization */
	@Override
	public void initialize() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initDataFromThread();
			}
		}).start();
		Intent intent =getIntent();
		initData(intent);
		initWidget();
	}

	/** listened widget's click method */
	@Override
	public void widgetClick(View v) {}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	@Override
	public void registerBroadcast() {}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KJActivityManager.create().addActivity(this);
		
		if(checkLoginOrOther()){
			return;
		}
		
		setRootView(); // 必须放在annotate之前调用
		initialize();
		registerBroadcast();
	}
	
	/**
	 * 是否需要优先登录或者其他操作
	 * @return
	 * @author FAN 创建于Dec 2, 2014
	 */
	protected boolean checkLoginOrOther() {
		return false;
	}

	@Override
	public void unRegisterBroadcast() {}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterBroadcast();
		KJActivityManager.create().finishActivity(this);
	}
}
