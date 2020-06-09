package com.z_martin.common.base.app;

/**
 * @description:
 * @project name: e-learningmaster
 * @author: xiaoming723@126.com
 * @createTime: 2020/3/7 11:30
 * @version: 1.0
 */
public class AppStatusManager {
    public int appStatus = AppStatus.STATUS_RECYCLE;
    public static AppStatusManager appStatusManager;
    private AppStatusManager(){}
    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }
    public int getAppStatus() {
        return appStatus;
    }
    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
