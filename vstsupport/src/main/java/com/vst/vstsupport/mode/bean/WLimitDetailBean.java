package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:37
 * Description:
 */
public class WLimitDetailBean implements Serializable{
    public  WLimitBean overdueData;//超期数据，对象，同列表页面的说明
    public ArrayList<InventoryDetailMsgBean> overdueMessage;//超期底下的消息列表
    public long nowTime;
    public String sendFlag;//超期详情里面能否发送消息 1能 0 否 null:消息列表进入

}
