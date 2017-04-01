package com.vstecs.android.funframework.net.xokhttp.https;

import java.io.Serializable;

public class RequestResult<T> implements Serializable {
	public String url;
	public String msg;
	public String code;
	public String dateStr;
	public boolean success;
	public int currentPage;
	public int totalPage;
	public T rs;
	
}
