package com.levent.pia;

import android.app.Application;

import com.onesignal.OneSignal;

public class DemoApp extends Application {

    private static DemoApp instance;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(this))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public static DemoApp getInstance() {
        return instance;
    }


}
