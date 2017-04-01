package com.vstecs.android.funframework.ui.fragment;

import android.os.Bundle;

import com.vstecs.android.funframework.utils.KJLoger;


/**
 * Application's base Fragment,you should inherit it for your Fragment<br>
 * <b>创建时间</b> 2014-5-28
 * 
 * @version 1.0
 */
public abstract class BaseFragment extends KJFrameFragment {

	/***************************************************************************
	 * print Fragment callback methods
	 ***************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KJLoger.state(this.getClass().getName(), "---------onCreateView ");
	}

	@Override
	public void onResume() {
		KJLoger.state(this.getClass().getName(), "---------onResume ");
		super.onResume();
	}

	@Override
	public void onPause() {
		KJLoger.state(this.getClass().getName(), "---------onPause ");
		super.onPause();
	}

	@Override
	public void onStop() {
		KJLoger.state(this.getClass().getName(), "---------onStop ");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		KJLoger.state(this.getClass().getName(), "---------onDestroy ");
		super.onDestroyView();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (getUserVisibleHint()){
			KJLoger.state(this.getClass().getName(), "---------setUserVisibleHint--visible ");
		}else {
			KJLoger.state(this.getClass().getName(), "---------setUserVisibleHint--hide ");
		}


		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		KJLoger.state(this.getClass().getName(), "---------onActivityCreated ");
		super.onActivityCreated(savedInstanceState);
	}
}
