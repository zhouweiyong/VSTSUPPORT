package com.vstecs.android.funframework.utils;

/**
 * KJLibrary's base exception class <br>
 * <b>创建时间</b> 2014-2-28
 * 
 * @version 1.0
 */
public class KJException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public KJException() {
		super();
	}

	public KJException(String msg) {
		super(msg);
	}

	public KJException(Throwable ex) {
		super(ex);
	}

	public KJException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
