// app/src/main/java/com/example/watch_step/NotificationService.java

package com.example.watch_step;

import android.app.Notification;
import android.content.Context;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    private static final int NOTIFICATION_ID_FOREGROUND = 1001;
    private static final int NOTIFICATION_ID_SCHEDULED = 1002;
    public static final String ACTION_START_FOREGROUND_SERVICE = "START_FOREGROUND_SERVICE";
    public static final String ACTION_SEND_NOTIFICATION = "SEND_NOTIFICATION";
    public static final String ACTION_DISMISS = "DISMISS_NOTIFICATION";

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_START_FOREGROUND_SERVICE.equals(action)) {
                startForeground(NOTIFICATION_ID_FOREGROUND, createForegroundNotification());
                Log.d(TAG, "Foreground service started with notification");
            } else if (ACTION_SEND_NOTIFICATION.equals(action)) {
                sendScheduledNotification();
                // Reschedule the next notification
                NotificationScheduler.scheduleNotifications(this, false); // Set to true for testing
            } else if (ACTION_DISMISS.equals(action)) {
                dismissNotification();
            } else {
                Log.w(TAG, "Unknown action received: " + action);
            }
        }
        // If the service is killed, restart it with the last intent.
        return START_STICKY;
    }

    private Notification createForegroundNotification() {
        // Get the current step count
        float currentSteps = SharedPreferencesHelper.getCurrentStepCount(this);
        String message = getStepMessage(currentSteps);

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);

        // "Open Notes" Action
        Intent notesIntent = new Intent(this, NotesActivity.class);
        PendingIntent notesPendingIntent = PendingIntent.getActivity(
                this,
                0,
                notesIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // "Dismiss" Action
        Intent dismissIntent = new Intent(this, NotificationService.class);
        dismissIntent.setAction(ACTION_DISMISS);
        PendingIntent dismissPendingIntent = PendingIntent.getService(
                this,
                0,
                dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the foreground notification
        NotificationCompat.Builder builder = NotificationHelper.getNotificationBuilder(this, "Step Tracker", message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true) // Make it ongoing to indicate active service
                .addAction(R.drawable.ic_notes, "Open Notes", notesPendingIntent)
                .addAction(R.drawable.ic_dismiss, "Dismiss", dismissPendingIntent); // Changed action

        return builder.build();
    }

    // Helper method to get the notification message
    private String getStepMessage(float currentSteps) {
        if (currentSteps < 200) {
            return "You're quite inactive today. Let's get moving!";
        } else if (currentSteps > 1500) {
            return "Great job! You've reached over 1500 steps. Consider taking a rest.";
        } else {
            return "Keep it up! You're doing great.";
        }
    }

    /**
     * Sends a scheduled notification.
     */
    private void sendScheduledNotification() {
        // Get the current step count
        float currentSteps = SharedPreferencesHelper.getCurrentStepCount(this);

        String message;
        if (currentSteps < 200) {
            message = "You're quite inactive today. Let's get moving!";
        } else if (currentSteps > 1500) {
            message = "Great job! You've reached over 1500 steps. Consider taking a rest.";
        } else {
            message = "Keep it up! You're doing great.";
        }

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);

        // "OK" Action
        Intent okIntent = new Intent(this, NotesActivity.class);
        PendingIntent okPendingIntent = PendingIntent.getActivity(
                this,
                0,
                okIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // "Dismiss" Action
        Intent dismissIntent = new Intent(this, NotificationService.class);
        dismissIntent.setAction(ACTION_DISMISS);
        PendingIntent dismissPendingIntent = PendingIntent.getService(
                this,
                0,
                dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the scheduled notification
        NotificationCompat.Builder builder = NotificationHelper.getNotificationBuilder(this, "Step Tracker", message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_ok, "OK", okPendingIntent)
                .addAction(R.drawable.ic_dismiss, "Dismiss", dismissPendingIntent);

        // Send the notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager != null){
            manager.notify(NOTIFICATION_ID_SCHEDULED, builder.build());

            // Log notification sent time
            CSVLogger csvLogger = new CSVLogger(this);
            csvLogger.logEvent("Scheduled Notification Sent", "Steps: " + (int)currentSteps);
        }
    }

    /**
     * Updates the ongoing foreground notification with the latest step count.
     *
     * @param currentSteps The latest step count.
     */
    public void updateForegroundNotification(float currentSteps) {
        String message = getStepMessage(currentSteps);

        // Rebuild the foreground notification
        Notification notification = NotificationHelper.getNotificationBuilder(this, "Step Tracker", message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .addAction(R.drawable.ic_notes, "Open Notes",
                        PendingIntent.getActivity(
                                this,
                                0,
                                new Intent(this, NotesActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        ))
                .addAction(R.drawable.ic_dismiss, "Dismiss",
                        PendingIntent.getService(
                                this,
                                0,
                                new Intent(this, NotificationService.class).setAction(ACTION_DISMISS),
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        ))
                .build();

        // Update the foreground notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager != null){
            manager.notify(NOTIFICATION_ID_FOREGROUND, notification);
        }

        // Optionally, log the update
        CSVLogger csvLogger = new CSVLogger(this);
        csvLogger.logEvent("Foreground Notification Updated", "Steps: " + (int)currentSteps);
    }

    /**
     * Handles dismissal of the notification by stopping the service.
     */
    private void dismissNotification() {
        stopForeground(true); // Removes the foreground notification
        stopSelf(); // Stops the service
        Log.d(TAG, "Foreground service stopped upon dismissal");
        CSVLogger csvLogger = new CSVLogger(this);
        csvLogger.logEvent("Notification Dismissed", "Service stopped by user.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Foreground service destroyed");
        CSVLogger csvLogger = new CSVLogger(this);
        csvLogger.logEvent("Notification Service", "Service destroyed");
    }
}
