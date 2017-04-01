package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * @description��
 * @author chenyuan
 * @date 2016-6-16 ����3:43:58
 */
public class MessageBean implements Serializable{


	private static final long serialVersionUID = 1L;

	public String time;//时间
	public String msg;//时间
	public String url;//时间
	public String name;//时间
	public String flag;//时间


	private ArrayList<MessageBean> datas=new ArrayList<MessageBean>();

	public ArrayList<MessageBean> getDatas(){
		for (int i = 0; i < 5; i++) {
			MessageBean bean =new MessageBean();
			bean.time="7-9 18:30";
			bean.msg="正在与客户沟通下个月回款";
			bean.url="http://img5.imgtn.bdimg.com/it/u=876917683,706781518&fm=11&gp=0.jpg";
			bean.name="jie.huang";
			bean.flag=""+i%2;
			datas.add(bean);
		}
		return datas;
	}
	
	
}
