package com.vst.vstsupport.mode.bean;

import java.io.Serializable;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:39
 * Description:
 */
public class LimitDetailMsgBean implements Serializable{
//    {
//        "CREATE_DATE":"2016-07-27 15:59:22",
//            "READER_TIME":"2016-07-27 15:59:22",
//            "OBJECT_ID":"1351,江西省通信产业服务有限公司物流分公司,HPCDX",
//            "SOURCE_ACCOUNT":"jamie_li",
//            "COMMENTS_ID":"cda9eccba1fc401e9a087c370e0c964f",
//            "IS_READER":0,
//            "CONTENT":"款到了吗？",
//            "COMMENTS_TYPE":1,
//            "IS_DELETE":0,
//            "SEND_ACCOUNT":"colin_au"
//    }

    public String CREATE_DATE;//创建时间
    public String READER_TIME;//阅读时间
    public String OBJECT_ID;//唯一标识项
    public String SOURCE_ACCOUNT;//接收方账号
    public String COMMENTS_ID;//流水号
    public String IS_READER;//是否已读
    public String CONTENT;//评论详情
    public String COMMENTS_TYPE;//类型
    public String IS_DELETE;//是否删除
    public String SEND_ACCOUNT;//发送方账号
}
