package com.liuguanghui.littlereader.entity;




public class CheckUpdateInfo {
    private String appName;
    private float newAppSize;
    private int newAppVersionCode;
    private String newAppVersionName;
    private String newAppUpdateDesc;
    private String newAppReleaseTime;
    private String newAppUrl;
    private int isForceUpdate;
    private int isUsePreDialog;

    public CheckUpdateInfo() {
    }

    public CheckUpdateInfo(String appName, float newAppSize, int newAppVersionCode, String newAppVersionName, String newAppUpdateDesc, String newAppReleaseTime, String newAppUrl, int isForceUpdate) {
        this.appName = appName;
        this.newAppSize = newAppSize;
        this.newAppVersionCode = newAppVersionCode;
        this.newAppVersionName = newAppVersionName;
        this.newAppUpdateDesc = newAppUpdateDesc;
        this.newAppReleaseTime = newAppReleaseTime;
        this.newAppUrl = newAppUrl;
        this.isForceUpdate = isForceUpdate;
    }

    public int getIsUsePreDialog() {
        return isUsePreDialog;
    }

    public void setIsUsePreDialog(int isUsePreDialog) {
        this.isUsePreDialog = isUsePreDialog;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public float getNewAppSize() {
        return newAppSize;
    }

    public void setNewAppSize(float newAppSize) {
        this.newAppSize = newAppSize;
    }

    public int getNewAppVersionCode() {
        return newAppVersionCode;
    }

    public void setNewAppVersionCode(int newAppVersionCode) {
        this.newAppVersionCode = newAppVersionCode;
    }

    public String getNewAppVersionName() {
        return newAppVersionName;
    }

    public void setNewAppVersionName(String newAppVersionName) {
        this.newAppVersionName = newAppVersionName;
    }

    public String getNewAppUpdateDesc() {
        return newAppUpdateDesc;
    }

    public void setNewAppUpdateDesc(String newAppUpdateDesc) {
        this.newAppUpdateDesc = newAppUpdateDesc;
    }

    public String getNewAppReleaseTime() {
        return newAppReleaseTime;
    }

    public void setNewAppReleaseTime(String newAppReleaseTime) {
        this.newAppReleaseTime = newAppReleaseTime;
    }

    public String getNewAppUrl() {
        return newAppUrl;
    }

    public void setNewAppUrl(String newAppUrl) {
        this.newAppUrl = newAppUrl;
    }

    public int getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }
}
