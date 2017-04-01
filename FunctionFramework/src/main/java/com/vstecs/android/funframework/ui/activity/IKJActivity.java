package com.vstecs.android.funframework.ui.activity;

import android.view.View;

/**
 * KJFrameActivity接口协议，实现此接口可使用KJActivityManager堆栈<br>
 * <b>创建时间</b> 2014-3-1 <br>
 * <b>最后修改时间</b> 2014-5-30
 * 
 * @version 1.0
 */
public interface IKJActivity {
	/** 初始化方法 */
	void initialize();

	/** 设置root界面 */
	void setRootView();

	/** 点击事件回调方法 */
	void widgetClick(View v);
}
