package com.vst.vstsupport.mode.bean;

import java.io.Serializable;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/26 10:25
 * Description:库存查看实体
 */
public class InventoryLookUpBean implements Serializable{

    public String date;//时间
    public String money;//金额
    public String days;//天数
    public String secendLevelLine;//二级线
    public String productName;// 	产品名字 (类型为流量的时候显示这个字段)
    public String productNum;// 	产品数量



//    {
//        "date":"2016-03-14 00:00:00",
//            "secendLevelLine":"HPVTC",
//            "money":21150,
//            "days":136,
//            "productName":"瘦客户机"
//    }

}
