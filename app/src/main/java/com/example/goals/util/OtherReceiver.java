package com.example.goals.util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import 	android.graphics.Color;
import com.example.goals.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import static android.graphics.Color.argb;

public class OtherReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_home_black_24dp);


        Log.i("createNotification","entered");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "qwerty")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(argb(1,252,144,20))
                .setLargeIcon(largeIcon)
                .setContentTitle("Expiring goals")
                .setContentText("you have a goal that expires today!!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}