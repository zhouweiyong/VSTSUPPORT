package com.vst.vstsupport.config;


/**
 * @description：Debug全局配置类
 */
public class Configuration {
	public static final String RELEASE_IM_SERVER_DOMAIN = "http://im.file.m.365sji.com:8080/hx_cfg.ini";
	public static final String RELEASETEST_IM_SERVER_DOMAIN = "http://im.file.m.365sji.com:8080/hx_cfg_test.ini";
	public static final String TEST_IM_SERVER_DOMAIN = "http://192.168.16.218:8080/hx_cfg.ini";
	// public static boolean IS_DEBUG_ENABLE = BuildConfig.DEBUG;
	public static boolean IS_DEBUG_ENABLE = true;// 正式环境和测试环境的切换，false:正式环境，true:测试环境
	public static boolean IS_LOG = true;// 发布时改为false,不打LOG
	public static String DEBUG_TAG = "YJ";
/**
	private static final String getIMServerDomain() {
		if (IS_DEBUG_ENABLE) {
			return RELEASE_IM_SERVER_DOMAIN;
		} else {
			return TEST_IM_SERVER_DOMAIN;    
		}

	}*/
	

}
