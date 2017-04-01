package com.vst.vstsupport.utils;


import android.content.SharedPreferences;

import com.vst.vstsupport.VstApplication;

/**
 * @description：PreferHelper
 */
public class PreferHelper {
	public static final String NAME = "vst_preference";
	/** 登录 */
	public static final String KEY_LOGIN_NAME = "key_login_name";
	public static final String KEY_LOGIN_PWD = "key_login_pwd";
	private static SharedPreferences sp;

	/**
	 * 字段区
	 */
	private static SharedPreferences.Editor editor;
	private static PreferHelper mInstance;
	

	private PreferHelper() {
		sp = VstApplication.getInstance().getSharedPreferences(NAME, 0);
		editor = sp.edit();
	}
	
	//增加了双重判断
	 public static PreferHelper getInstance() {
		if (null == mInstance) {
			synchronized(PreferHelper.class){
				if(null == mInstance)
				mInstance = new PreferHelper();
			}
		}
		return mInstance;
	}

	/**
	 * 储存值
	 * @param key
	 * @param value
	 */
	public void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获取值
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return sp.getString(key, "");
	}

	public int getInt(String key) {
		return sp.getInt(key, -1);
	}

	public long getLong(String key) {
		return sp.getLong(key, 1);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	/**
	 * @description：移除特定的
	 * @date 2014年11月5日 下午4:30:08
	 */
	public void remove(String name) {
		editor.remove(name);
		editor.commit();
	}

	public void saveLoginInfo(String userName,String pwd){
		editor.putString(KEY_LOGIN_NAME,userName);
		editor.putString(KEY_LOGIN_PWD,pwd);
		editor.commit();
	}

	public void clearLoginInfo(){
		editor.remove(KEY_LOGIN_PWD);
		editor.commit();
	}

}
