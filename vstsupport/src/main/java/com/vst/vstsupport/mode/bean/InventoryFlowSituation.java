package com.vst.vstsupport.mode.bean;

import java.io.Serializable;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/8/1 9:46
 * Description:
 */
public class InventoryFlowSituation implements Serializable{
    public String secendLevelLine; //二级线
    public String fundDesc;//是否占用资金池
    public String solution;//清理时间
    public String reason;//清理策略

}
