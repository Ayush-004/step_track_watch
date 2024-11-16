package com.example.watch_step;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationScheduler {

    /**
     * Schedules periodic notifications by starting the NotificationService.
     *
     * @param context The application context.
     * @param isTest  If true, uses a short interval for testing (e.g., 10 seconds).
     *                If false, uses the production interval (e.g., 1 hour).
     */
    public static void scheduleNotifications(Context context, boolean isTest) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(NotificationService.ACTION_SEND_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long interval = isTest ? 10000 : 3600000; // 10 sec or 1 hour

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + interval,
                        pendingIntent
                );
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + interval,
                        pendingIntent
                );
            } else {
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + interval,
                        pendingIntent
                );
            }
        }
    }

    /**
     * Reschedules notifications. Useful after a device reboot.
     *
     * @param context The application context.
     * @param isTest  If true, uses a short interval for testing (e.g., 10 seconds).
     *                If false, uses the production interval (e.g., 1 hour).
     */
    public static void rescheduleNotifications(Context context, boolean isTest) {
        // Cancel existing alarms
        cancelNotifications(context);

        // Schedule again
        scheduleNotifications(context, isTest);
    }

    /**
     * Cancels scheduled notifications.
     *
     * @param context The application context.
     */
    public static void cancelNotifications(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(NotificationService.ACTION_SEND_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }
}