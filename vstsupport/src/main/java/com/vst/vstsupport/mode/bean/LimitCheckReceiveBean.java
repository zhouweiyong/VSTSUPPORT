package com.vst.vstsupport.mode.bean;

import java.io.Serializable;

/** 
 * @description��
 * @author chenyuan
 * @date 2016-6-16 ����3:43:58
 */
public class LimitCheckReceiveBean implements Serializable{


	private static final long serialVersionUID = 1L;

//	{
//		"secendLevelLine":"HPTM",
//			"expireDay":"Jul 26, 2016 12:00:00 AM",
//			"money":3318,
//			"days":0,
//			"customerName":"山西元兴通商贸有限公司"
//	},

	public String secendLevelLine;//二级线
	public String customerName;//项目名称
	public String money;//应收余额
	public String expireDay;//应收到期日
	public int days;//逾期天数

	public String area;//区域
	public String saleName;//业务员
	public String businessDepartment;//事业部




}
