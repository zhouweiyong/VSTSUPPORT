package com.vstecs.android.funframework.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * Fragment's framework,the developer shouldn't extends it<br>
 * <b>创建时间</b> 2014-3-1 <br>
 * <b>最后修改时间</b> 2014-5-30<br>
 * 
 * @version 1.5
 */
public abstract class KJFrameFragment extends Fragment implements OnClickListener {

	private View view;

	protected abstract View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle);

	/**
	 * initialization widget, you should look like parentView.findviewbyid(id);
	 * call method
	 * 
	 * @param parentView
	 */
	protected void initWidget(View parentView) {}

	/** initialization data */
	protected void initData() {}

	/**
	 * initialization data. And this method run in background thread, so you
	 * shouldn't change ui
	 */
	protected void initThreadData() {}

	/** widget click method */
	protected void widgetClick(View v) {}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflaterView(inflater, container, savedInstanceState);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//new Thread(new Runnable() {
//			@Override
//			public void run() {
//				initThreadData();
//			}
//		}).start();
		initData();
		initWidget(view);
	}

}
