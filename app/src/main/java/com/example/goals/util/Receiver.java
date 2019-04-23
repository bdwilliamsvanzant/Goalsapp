package com.example.goals.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.goals.R;

import junit.framework.Test;

import androidx.core.app.NotificationCompat;

public class Receiver extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, Test.class);
        long[] pattern = {0, 300, 0};
        PendingIntent pi = PendingIntent.getActivity(this, 01234, intent, 0);
        Log.i("receiver", "entered receiver");
        NotificationChannel mChannel = null;

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "afd");
//               // .setSmallIcon(R.drawable.depressiontest)
//                .setContentTitle("Take Questionnaire")
//                .setContentText("Take questionnaire for Duke Mood Study.")
////                .setVibrate(pattern)
//                .setAutoCancel(true);


        //mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            Log.i("receiver", "yes my phone is 26+");
            mChannel = new NotificationChannel("afd", this.getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription("asefdasdfasdfasdfasdfasdfasdf");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);

        } else {
            mNotificationManager.notify(01234, mBuilder.build());
        }
    }


}