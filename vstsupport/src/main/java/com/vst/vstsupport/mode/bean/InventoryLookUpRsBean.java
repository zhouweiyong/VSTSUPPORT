package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/29 11:24
 * Description:库存查看结果实体
 */
public class InventoryLookUpRsBean implements Serializable{
    public ArrayList<InventoryLookUpBean> inventoryList;
    public String secendLevelLineSring;//销售人员能查看的二级线 逗号分隔取值
}
