package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/26 14:01
 * Description:
 */
public class LimitRsBean implements Serializable{

    public String secendLevelLineList;
    public ArrayList<LimitBean> overdueList;
    public String operation;//问询还是跟进
    public String areaList;//搜索条件地区可选列表
    public long nowTime;//服务器时间
    public ArrayList<BusinessDepartmentBean> businessDepartmentList;
}
