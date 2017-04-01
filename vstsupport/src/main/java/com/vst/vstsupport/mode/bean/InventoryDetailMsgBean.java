package com.vst.vstsupport.mode.bean;

import java.io.Serializable;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:39
 * Description:
 */
public class InventoryDetailMsgBean implements Serializable{
//    {
//        "sourceAccount":"zhang.yingming",  //接受账号
//            "sendAccount":"qi.luo",//发送账号
//            "content":"三星事业部",//评论消息
//            "createDate":"2016-07-27 11:23:20"//时间
//    }

    public String createDate;//创建时间
    public String sourceAccount;//接收方账号
    public String content;//评论详情
    public String sendAccount;//发送方账号
}
