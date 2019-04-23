package com.example.goals.util;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.example.goals.DatabaseActivities.AddGoalActivity;
import com.example.goals.R;

import androidx.core.app.NotificationCompat;

public class OtherReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("OtherRreceiver", "entered receiver");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;

//***********************OPTION 1 ****************************
        if (Build.VERSION.SDK_INT >= 26) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "adf");

            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("ticker")
                    .setWhen(System.currentTimeMillis()+2000)
                    .setAutoCancel(true)
                    .setContentTitle("whats up")
                    .setContentText("goals and stuff yo");

            Notification notification = builder.build();


            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
            notificationManager.notify(id, notification);
            Log.i("OtherRreceiver", "entered iffffr");
        }


//***********************OPTION 2 ****************************************
//        if (Build.VERSION.SDK_INT >= 26) {
//            Log.i("receiver", "yes my phone is 26+");
//            NotificationChannel mChannel = null;
//            mChannel = new NotificationChannel("afd", context.getString(R.string.app_name), importance);
//            // Configure the notification channel.
//            mChannel.setDescription("asefdasdfasdfasdfasdfasdfasdf");
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//           notificationManager.createNotificationChannel(mChannel);
//
//        }
    }
}