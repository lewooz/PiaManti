package com.levent.pia;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    Context context2;
    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;
    String mail;

    ExampleNotificationOpenedHandler(Context context) {
        context2 = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {

        mAuth = FirebaseAuth.getInstance();
        currentOnlineUser = mAuth.getCurrentUser();
        if(currentOnlineUser!=null) {
            mail = currentOnlineUser.getEmail();
        }

        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

        if(currentOnlineUser!=null) {
            if (mail.equals("leventozkan89@gmail.com")) {
                Intent intent = new Intent(context2 , AdminOrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                context2.startActivity(intent);
            } else {

                Intent intent = new Intent(context2 , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                context2.startActivity(intent);
            }
        }else
        {
            Intent intent = new Intent(context2 , MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            context2.startActivity(intent);
        }}
}

