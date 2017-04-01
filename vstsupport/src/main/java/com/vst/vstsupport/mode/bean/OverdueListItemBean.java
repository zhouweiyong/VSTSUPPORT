package com.vst.vstsupport.mode.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/30
 * class description:跟进提醒item
 */
public class OverdueListItemBean implements Parcelable {
    public static final Parcelable.Creator<OverdueListItemBean> CREATOR = new Parcelable.Creator<OverdueListItemBean>() {
        @Override
        public OverdueListItemBean createFromParcel(Parcel source) {
            return new OverdueListItemBean(source);
        }

        @Override
        public OverdueListItemBean[] newArray(int size) {
            return new OverdueListItemBean[size];
        }
    };
    /**
     * day31to60 :
     * day1to15 :
     * day60up : 31260.185
     * customerName : 吉林省启明软件园企业孵化有限公司
     * secendLevelLine : DLBD
     * branchCompany : 北京1
     * saleName : 张英明
     * dealTime :
     * solution :
     * day16to30 :
     * totalOverdueDay : 424
     * totalOverdueMoney : 31260.185
     * businessDepartment : DELL企业级产品事业部
     * personId : 65012
     */


    private String day1to15;
    private String day16to30;
    private String day31to60;
    private String day60up;
    private String customerName;
    private String secendLevelLine;
    private String branchCompany;
    private String saleName;
    private String dealTime;
    private String solution;
    private String totalOverdueDay;
    private String totalOverdueMoney;
    private String businessDepartment;
    private int personId;

    public OverdueListItemBean() {
    }

    protected OverdueListItemBean(Parcel in) {
        this.day1to15 = in.readString();
        this.day16to30 = in.readString();
        this.day31to60 = in.readString();
        this.day60up = in.readString();
        this.customerName = in.readString();
        this.secendLevelLine = in.readString();
        this.branchCompany = in.readString();
        this.saleName = in.readString();
        this.dealTime = in.readString();
        this.solution = in.readString();
        this.totalOverdueDay = in.readString();
        this.totalOverdueMoney = in.readString();
        this.businessDepartment = in.readString();
        this.personId = in.readInt();
    }

    public double getDay1to15() {
        if (TextUtils.isEmpty(day1to15)){
            return 0;
        }else {
            return Double.parseDouble(day1to15);
        }
    }

    public void setDay1to15(double day1to15) {
        this.day1to15 = String.valueOf(day1to15);
    }

    public double getDay16to30() {
        if (TextUtils.isEmpty(day16to30)){
            return 0;
        }else {
            return Double.parseDouble(day16to30);
        }
    }

    public void setDay16to30(double day16to30) {
        this.day16to30 = String.valueOf(day16to30);
    }

    public double getDay31to60() {
        if (TextUtils.isEmpty(day31to60)){
            return 0;
        }else {
            return Double.parseDouble(day31to60);
        }
    }

    public void setDay31to60(double day31to60) {
        this.day31to60 = String.valueOf(day31to60);
    }

    public double getDay60up() {
        if (TextUtils.isEmpty(day60up)){
            return 0;
        }else {
            return Double.parseDouble(day60up);
        }
    }

    public void setDay60up(double day60up) {
        this.day60up = String.valueOf(day60up);
    }

    public double getTotalOverdueMoney() {
        if (TextUtils.isEmpty(totalOverdueMoney)){
            return 0;
        }else {
            return Double.parseDouble(totalOverdueMoney);
        }
    }

    public void setTotalOverdueMoney(double totalOverdueMoney) {
        this.totalOverdueMoney = String.valueOf(totalOverdueMoney);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSecendLevelLine() {
        return secendLevelLine;
    }

    public void setSecendLevelLine(String secendLevelLine) {
        this.secendLevelLine = secendLevelLine;
    }

    public String getBranchCompany() {
        return branchCompany;
    }

    public void setBranchCompany(String branchCompany) {
        this.branchCompany = branchCompany;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getTotalOverdueDay() {
        return totalOverdueDay;
    }

    public void setTotalOverdueDay(String totalOverdueDay) {
        this.totalOverdueDay = totalOverdueDay;
    }

    public String getBusinessDepartment() {
        return businessDepartment;
    }

    public void setBusinessDepartment(String businessDepartment) {
        this.businessDepartment = businessDepartment;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day1to15);
        dest.writeString(this.day16to30);
        dest.writeString(this.day31to60);
        dest.writeString(this.day60up);
        dest.writeString(this.customerName);
        dest.writeString(this.secendLevelLine);
        dest.writeString(this.branchCompany);
        dest.writeString(this.saleName);
        dest.writeString(this.dealTime);
        dest.writeString(this.solution);
        dest.writeString(this.totalOverdueDay);
        dest.writeString(this.totalOverdueMoney);
        dest.writeString(this.businessDepartment);
        dest.writeInt(this.personId);
    }
}
