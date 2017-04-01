package com.vst.vstsupport.mode.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/8/12
 * class description:请输入类描述
 */
public class ReceiveMessageContent implements Parcelable {
    public static final Creator<ReceiveMessageContent> CREATOR = new Creator<ReceiveMessageContent>() {
        @Override
        public ReceiveMessageContent createFromParcel(Parcel source) {
            return new ReceiveMessageContent(source);
        }

        @Override
        public ReceiveMessageContent[] newArray(int size) {
            return new ReceiveMessageContent[size];
        }
    };
    /**
     * content : 回复jasamin.lu:
     * customerName : 杭州魁星科技有限公司
     * commentsType : 1
     * sourceAccount : jasamin.lu
     * account : ningdong.liu
     * personId : 21072
     * unionSecdCode : IPHOBD
     * productName :
     */
    private String content;
    private String customerName;
    private int commentsType;
    private String sourceAccount;
    private String account;
    private String personId;
    private String secendLevelLine;
    private String productName;
    private String nowTime;

    public ReceiveMessageContent() {
    }

    protected ReceiveMessageContent(Parcel in) {
        this.content = in.readString();
        this.customerName = in.readString();
        this.commentsType = in.readInt();
        this.sourceAccount = in.readString();
        this.account = in.readString();
        this.personId = in.readString();
        this.secendLevelLine = in.readString();
        this.productName = in.readString();
        this.nowTime = in.readString();
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCommentsType() {
        return commentsType;
    }

    public void setCommentsType(int commentsType) {
        this.commentsType = commentsType;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUnionSecdCode() {
        return secendLevelLine;
    }

    public void setUnionSecdCode(String unionSecdCode) {
        this.secendLevelLine = unionSecdCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.customerName);
        dest.writeInt(this.commentsType);
        dest.writeString(this.sourceAccount);
        dest.writeString(this.account);
        dest.writeString(this.personId);
        dest.writeString(this.secendLevelLine);
        dest.writeString(this.productName);
        dest.writeString(this.nowTime);
    }
}
