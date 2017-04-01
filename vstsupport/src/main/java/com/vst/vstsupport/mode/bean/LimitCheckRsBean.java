package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/26 14:01
 * Description:
 */
public class LimitCheckRsBean implements Serializable{

    public ArrayList<LimitCheckReceiveBean> receivableList;
    public ArrayList<LimitCheckReceiveBean> receivableFiveDay;
    public String operation;//问询还是跟进
    public ArrayList<BusinessDepartmentBean> businessDepartmentList;
    public long nowTime;//服务器时间
    public String secendLevelLineList;
    public String areaList;

}
