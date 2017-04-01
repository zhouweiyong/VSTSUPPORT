package com.vst.vstsupport.mode.event;

import java.io.Serializable;

/** 让主页切换界面事件 */
public class CallMainPagerChangeEvent implements Serializable {
	private static final long serialVersionUID = -5582728308345869073L;
	public int changePageNo;

	public CallMainPagerChangeEvent(int changePageNo) {
		this.changePageNo = changePageNo;
	}
}
