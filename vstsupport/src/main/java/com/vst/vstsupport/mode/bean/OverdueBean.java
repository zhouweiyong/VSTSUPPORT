package com.vst.vstsupport.mode.bean;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/30
 * class description:跟进提醒
 */
public class OverdueBean {
    private ArrayList<OverdueListItemBean> overdueList;
    private String operation="follow";
    private String times;
    private long nowTime;

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public ArrayList<OverdueListItemBean> getOverdueList() {
        return overdueList;
    }

    public void setOverdueList(ArrayList<OverdueListItemBean> overdueList) {
        this.overdueList = overdueList;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
