package com.odin.sms.demo;

import android.app.Application;

import com.odin.sms.OdinSMS;

public class OdinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化OdinSMSSDK
        OdinSMS.init(this, "985459861c2c4e7b8f4f2c7245e56448", "b250f2ba017048e88275154a62ed1bce");
        OdinSMS.setLogEnabled(true);
    }
}
