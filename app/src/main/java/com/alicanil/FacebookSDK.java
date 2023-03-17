package com.alicanil;

import android.app.Application;

import com.facebook.FacebookSdk;

public class FacebookSDK extends Application {

    // facebook sdk nin tanimlandigi ve applicationa initialize edilisi
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

    }
}
