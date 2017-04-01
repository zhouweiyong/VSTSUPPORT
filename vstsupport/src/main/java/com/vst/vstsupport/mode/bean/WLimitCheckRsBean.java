package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/26 14:01
 * Description:
 */
public class WLimitCheckRsBean implements Serializable{

    public ArrayList<WLimitCheckReceiveBean> receivableList;
    public ArrayList<WLimitCheckReceiveBean> receivableThreeDay;
    public String operation;//问询还是跟进
    public String limitTime;
    public ArrayList<BusinessDepartmentBean> businessDepartmentList;
    public long nowTime;//服务器时间

}
