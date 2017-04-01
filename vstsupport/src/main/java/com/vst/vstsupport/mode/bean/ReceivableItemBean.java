package com.vst.vstsupport.mode.bean;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/30
 * class description:请输入类描述
 */
public class ReceivableItemBean {
    /**
     * secendLevelLine : HPTM
     * expireDay : Aug 2, 2016 12:00:00 AM
     * money : 5367.6
     * days : -7
     * customerName : 北京美捷美科技有限公司
     */

    private String secendLevelLine;
    private String expireDay;
    private double money;
    private String days;
    private String customerName;

    public String getSecendLevelLine() {
        return secendLevelLine;
    }

    public void setSecendLevelLine(String secendLevelLine) {
        this.secendLevelLine = secendLevelLine;
    }

    public String getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(String expireDay) {
        this.expireDay = expireDay;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
