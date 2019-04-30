package com.example.goals.util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.example.goals.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class OtherReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("createNotification","entered");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "qwerty")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Expiring goals")
                .setContentText("you have a goal that expires today!!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}