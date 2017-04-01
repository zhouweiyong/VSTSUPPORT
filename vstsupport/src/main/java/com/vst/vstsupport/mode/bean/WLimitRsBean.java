package com.vst.vstsupport.mode.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/26 14:01
 * Description:
 */
public class WLimitRsBean implements Serializable{

    public ArrayList<WLimitBean> overDueList;
    public String operation;//问询还是跟进
    public long nowTime;//服务器时间
    public String limitTime;//截数日期

}
