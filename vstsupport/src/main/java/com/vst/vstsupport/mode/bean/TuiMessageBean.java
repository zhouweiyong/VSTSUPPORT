package com.vst.vstsupport.mode.bean;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/27
 * class description:请输入类描述
 */
public class TuiMessageBean {

    /**
     * returnCount : 10
     * identityType : 0
     * commentsType : 0
     * title : 即将到期
     * content : 您有10条即将到期的应收账款
     * createDateTime : 2016-07-26 18:20:35
     */

    private int returnCount;
    private int noReaderCount;
    private int identityType;
    private int commentsType;
    private String title;
    private String content;
    private String createDateTime;
    private String projectName;
    /**
     * secendLevelLineList : HPCDX
     * messageId : 3
     * personId : 1351
     * customerName : 江西省通信产业服务有限公司物流分公司
     */

    private String secendLevelLineList;
    private int messageId;
    private String personId;
    private String customerName;
    private String businessDepartment;
    private String totalmoney;

    private boolean showNumTag = true;
    private String secendLevelLine;
    private String projectSerial;
    private String customerId;
    private String brandId;
    private String brandName;
    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getProjectSerial() {
        return projectSerial;
    }

    public void setProjectSerial(String projectSerial) {
        this.projectSerial = projectSerial;
    }

    public int getNoReaderCount() {
        return noReaderCount;
    }

    public void setNoReaderCount(int noReaderCount) {
        this.noReaderCount = noReaderCount;
    }

    public String getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }

    public boolean isShowNumTag() {
        return showNumTag;
    }

    public void setShowNumTag(boolean showNumTag) {
        this.showNumTag = showNumTag;
    }

    public int getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(int returnCount) {
        this.returnCount = returnCount;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public int getCommentsType() {
        return commentsType;
    }

    public void setCommentsType(int commentsType) {
        this.commentsType = commentsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getSecendLevelLineList() {
        return secendLevelLineList;
    }

    public void setSecendLevelLineList(String secendLevelLineList) {
        this.secendLevelLineList = secendLevelLineList;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBusinessDepartment() {
        return businessDepartment;
    }

    public void setBusinessDepartment(String businessDepartment) {
        this.businessDepartment = businessDepartment;
    }
}
