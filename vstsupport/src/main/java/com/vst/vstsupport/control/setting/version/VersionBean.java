package com.vst.vstsupport.control.setting.version;

import java.io.Serializable;

/**
 * @author samy
 * @description：获取软件版本信息
 * @date 2014年10月9日 下午4:46:27
 */
public class VersionBean implements Serializable {
    private static final long serialVersionUID = -5415506163755359079L;
    /**
     * @description：
     * @author samy
     * @date 2014年10月17日 下午5:33:06
     */
    /**
     * 应用名称
     */
    public String appName;
    /**
     * 包名
     */
    public String packageName;
    /**
     * 版本号
     */
    public String versionNo;
    /**
     * 版本名称
     */
    public String versionName;
    /**
     * 文件大小
     */
    public String fileSize;
    /**
     * 下载地址
     */
    public String downloadUrl;
    /**
     * 更新内容
     */
    public String updateInfo;
    /**
     * 是否强制升级（0：否，1：是）
     */
    public int isForceUpdate;
    public String createTime;
    public int appType;
    public String updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getVersionNo() {
        String tmp = this.versionNo.replace(".","");
        return Integer.parseInt(tmp);
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    // 给数据库看的；
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public int getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
