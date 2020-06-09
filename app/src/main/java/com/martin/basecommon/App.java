package com.martin.basecommon;

import com.z_martin.common.base.app.AppConfig;
import com.z_martin.common.base.app.BaseApp;

public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        AppConfig.init(MainActivity.class, MainActivity.class, "", "", 0, 10003, 10001);
    }
}
