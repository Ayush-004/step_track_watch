// app/src/main/java/com/example/watch_step/NotificationHelper.java

package com.example.watch_step;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    public static final String CHANNEL_ID = "STEP_TRACKER_CHANNEL";
    private static final String CHANNEL_NAME = "Step Tracker Notifications";
    private static final String CHANNEL_DESC = "Notifications for step tracking";

    public static void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 1000});

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String title, String message) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }
}