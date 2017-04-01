package com.vst.vstsupport.mode.bean;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/8/2
 * class description:请输入类描述
 */
public class IntoTotalInvBean {
    private String times;
    private ArrayList<IntoTotalInvItemBean> totalInv;

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public ArrayList<IntoTotalInvItemBean> getTotalInv() {
        return totalInv;
    }

    public void setTotalInv(ArrayList<IntoTotalInvItemBean> totalInv) {
        this.totalInv = totalInv;
    }
}
