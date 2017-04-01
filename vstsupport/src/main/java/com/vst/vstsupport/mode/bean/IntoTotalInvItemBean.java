package com.vst.vstsupport.mode.bean;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/8/1
 * class description:消息列表总库存金额
 */
public class IntoTotalInvItemBean {
    /**
     * secendLevelLine : sas
     * ingInv : 26250
     * inInv : 300000
     * totalMoney : 674657
     * projectName : 移动办公优化
     */

    private String secendLevelLine;
    private String projectName;
    private double ingInv;
    private double inInv;
    private String totalMoney;

    public String getSecendLevelLine() {
        return secendLevelLine;
    }

    public void setSecendLevelLine(String secendLevelLine) {
        this.secendLevelLine = secendLevelLine;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getIngInv() {
        return ingInv;
    }

    public void setIngInv(double ingInv) {
        this.ingInv = ingInv;
    }

    public double getInInv() {
        return inInv;
    }

    public void setInInv(double inInv) {
        this.inInv = inInv;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }
}
