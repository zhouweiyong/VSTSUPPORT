package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * @description��
 * @author chenyuan
 * @date 2016-6-16 ����3:43:58
 */
public class CheckReceiveBean implements Serializable{


	private static final long serialVersionUID = 1L;

	public String unionSecCode;//二级线
	public String pjtName;//项目名称
	public String num;//应收余额
	public String receive_total;//应收到期日
	public String tainshu;//逾期天数
	public String inDbDays;//在库天数
	public String productNums;//产品数量
	public String inDbTime;//入库时间
	public String inventoryNums;//库存金额
	public String pos="1";//职位



	private ArrayList<CheckReceiveBean> datas=new ArrayList<CheckReceiveBean>();

	public ArrayList<CheckReceiveBean> getDatas(){
		for (int i = 0; i < 5; i++) {
			CheckReceiveBean bean =new CheckReceiveBean();
			bean.unionSecCode="ORA.SS";
			bean.pjtName="JWH武汉杰隆达外衣科技有限公司";
			bean.num="￥2978.36";
			bean.receive_total="2015-03-15";
			bean.tainshu="14天";
			bean.inDbDays="15天";
			bean.productNums="1211223";
			bean.inventoryNums="1211223.00";
			bean.inDbTime="2015-03-15";
			pos=i%2+"";
			datas.add(bean);
		}
		return datas;
	}
	
	
}
