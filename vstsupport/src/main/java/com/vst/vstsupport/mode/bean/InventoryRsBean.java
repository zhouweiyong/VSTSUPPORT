package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/29 11:24
 * Description:
 */
public class InventoryRsBean implements Serializable{
    public ArrayList<InventoryBean> inventoryList;
    public long nowTime;
    public int totalPage;
    public String secendLevelLineSring;
    public String operation;//问询还是跟进
    public ArrayList<BusinessDepartmentBean> businessDepartmentList;


//    "businessDepartmentList": [
//    {
//        "businessDepartmentId": "123840",
//            "businessDepartment": "集成业务事业部"
//    },
//    {
//        "businessDepartmentId": "136978",
//            "businessDepartment": "移动互联大客户部"
//    }
//    ],
}
