package com.vst.vstsupport.mode.bean;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/30
 * class description:请输入类描述
 */
public class ReceivableBean {
    private ArrayList<ReceivableItemBean> receivableFiveDay;
    private ArrayList<ReceivableItemBean> receivableList;
    private String times;

    public ArrayList<ReceivableItemBean> getReceivableFiveDay() {
        return receivableFiveDay;
    }

    public void setReceivableFiveDay(ArrayList<ReceivableItemBean> receivableFiveDay) {
        this.receivableFiveDay = receivableFiveDay;
    }

    public ArrayList<ReceivableItemBean> getReceivableList() {
        return receivableList;
    }

    public void setReceivableList(ArrayList<ReceivableItemBean> receivableList) {
        this.receivableList = receivableList;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
