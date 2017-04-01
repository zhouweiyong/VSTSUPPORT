package com.vst.vstsupport.config;

/**
 * 网络地址配置
 */
	public class UrlConstants {

//	public static String host = "http://192.168.3.100:8089";//李金环境
//	public static String host = "http://192.168.3.165:8080";
	public static String host = "http://219.137.243.44:8000";//发布环境
//	public static String host = "http://219.137.243.41:8000";

	public static String USERLOG =getUrl("/ecsapp/appUserModel/userLog");
	public static String GETMESSAGELIST =getUrl("/ecsapp/appMessageModel/getMessageList");
	public static String OVERDUELIST =getUrl("/ecsapp/appReceivableModel/OverdueList");
	public static String RECEIVABLELIST =getUrl("/ecsapp/appReceivableModel/receivableList");
	public static String OVERDUEFOLLW =getUrl("/ecsapp/appReceivableModel/overdueFollow");
	public static String INTOOVERDUEDETAIL =getUrl("/ecsapp/appReceivableModel/intoOverdueDetail");
	public static String INVENTORYLIST =getUrl("/ecsapp/appInventoryModel/inventoryList");
	public static String INVENTORYFOLLOW =getUrl("/ecsapp/appInventoryModel/inventoryFollow");
	public static String GETINVENTORYLIST =getUrl("/ecsapp/appInventoryModel/getInventoryList");
	public static String OVERDUEMESSAGELIST =getUrl("/ecsapp/appReceivableModel/overdueMessageList");
	public static String PUBLISHCOMMENTS =getUrl("/ecsapp/appMessageModel/publishComments");
	public static String RECEIVABLEMESSAGELIST =getUrl("/ecsapp/appReceivableModel/receivableMessageList");
	public static String INTO90INV =getUrl("/ecsapp/appInventoryModel/into90Inv");
	public static String INTOTOTALINV =getUrl("/ecsapp/appInventoryModel/intoTotalInv");
	public static String GETVERSIONINFO =getUrl("/ecsapp/appecsversion/getVersionInfo ");
	public static String INTOINVENTORYDETAIL =getUrl("/ecsapp/appInventoryModel/intoInventoryDetail");
	public static String GETSECONDLEVELLINE =getUrl("/ecsapp/appInventoryModel/getsecendLevelLine");
	public static String DELETECOMMENTS =getUrl("/ecsapp/appMessageModel/deleteComments");
	public static String READERCOMMENTS =getUrl("/ecsapp/appMessageModel/readerComments");


	public static String VSTOVERDUEFOLLOW =getUrl("/ecsapp/appReceivableVstModel/vstOverdueFollow");
	public static String VSTOVERDUELIST =getUrl("/ecsapp/appReceivableVstModel/vstOverdueList");
	public static String GETBRAND =getUrl("/ecsapp/appReceivableVstModel/getBrand");
	public static String INTOVSTOVERDUEDETAIL =getUrl("/ecsapp/appReceivableVstModel/intoVstOverdueDetail");
	public static String VSTRECEIVEABLELIST =getUrl("/ecsapp/appReceivableVstModel/vstReceivableList");

	private static String getUrl(String mode){
		return String.format("%s%s",host,mode);
	}
}
