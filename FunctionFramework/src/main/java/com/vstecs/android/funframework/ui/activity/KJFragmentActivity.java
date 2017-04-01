package com.vstecs.android.funframework.ui.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.vstecs.android.funframework.ui.KJActivityManager;
import com.vstecs.android.funframework.ui.ViewInject;
import com.vstecs.android.funframework.ui.fragment.BaseFragment;


/**
 * Application BaseActivity plus. For ease of use, your Activity should overload
 * changeFragment(Fragment frag).<br>
 * <b>说明</b> if you want include the Fragment,you should extends it for your
 * Activity <br>
 * <b>说明</b> else you should extends KJFrameActivity for your Activity<br>
 * <b>创建时间</b> 2014-5-14
 * 
 * @version 1.1
 */
public abstract class KJFragmentActivity extends BaseActivity {
	private boolean openBackListener = false;

	public KJFragmentActivity() {
		openBackListener = getBackListener();
		setBackListener(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (openBackListener && keyCode == KeyEvent.KEYCODE_BACK && getFragmentManager().getBackStackEntryCount() == 0 && KJActivityManager.create().getCount() < 2) {
			ViewInject.create().getExitDialog(this);
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 改变界面的fragment */
	protected void changeFragment(int resView, BaseFragment targetFragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(resView, targetFragment, targetFragment.getClass().getName());
		transaction.commit();
	}

	/**
	 * 你应该在这里调用changeFragment(R.id.content, addStack, targetFragment);
	 * 
	 * @param targetFragment
	 *            要改变的Activity
	 */
	public abstract void changeFragment(BaseFragment targetFragment);
}
