package com.example.watch_step;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent){
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            // Start the NotificationService as a foreground service
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.setAction(NotificationService.ACTION_START_FOREGROUND_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
            Log.d(TAG, "Foreground service started on boot");

            // Reschedule notifications
            NotificationScheduler.scheduleNotifications(context, false); // Set to true for testing interval
            Log.d(TAG, "Notifications rescheduled after boot");
        }
    }
}