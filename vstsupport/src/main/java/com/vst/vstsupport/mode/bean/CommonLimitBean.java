package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * @description��
 * @author chenyuan
 * @date 2016-6-16 ����3:43:58
 */
public class CommonLimitBean implements Serializable{


	private static final long serialVersionUID = 1L;

	public String dept;//部门
	public String unionSecCode;//二级线
	public String erpSet3;//产品
	public String day0to60;//0到60天在库金额
	public String day61to90;//61到90天在库金额
	public String day91to120;//91到120天在库金额
	public String day120up;//120天以上在库金额
	public String totleMey;//在库金额
	public String pmName;//产品经理
	public String pjtName;//项目名称
	public String olStkDay;//在库天数
	public String depType;//
	public boolean canEdit;
	public String limitTimeTotal;//超期总额
	public String lastLongTime;//最长逾期天数
	public String salesMan;//业务员
	public String pos;//职位
	public String location;//地理位置
	public String solute;//解决方案
	public String calucateTime;//清理时间



	private ArrayList<CommonLimitBean> datas=new ArrayList<CommonLimitBean>();

	public ArrayList<CommonLimitBean> getDatas(){
		for (int i = 0; i < 5; i++) {
			CommonLimitBean bean =new CommonLimitBean();
			bean.dept="外设产品事业部";
			bean.unionSecCode="ORA.SS";
			bean.location="沈阳";
			bean.solute="计划下个月汇款";
			bean.calucateTime="2016-6-6";
			bean.salesMan="罗凯";
			bean.lastLongTime="47";
			bean.limitTimeTotal="711600.00";
			bean.pjtName="GD北京迅捷科技有限公司";
			bean.day0to60="";
			bean.day61to90="427790.00";
			bean.day91to120="";
			bean.day120up="";
			bean.pos=""+i;
			bean.canEdit=true;
			datas.add(bean);
		}
		return datas;
	}
	
	
}
