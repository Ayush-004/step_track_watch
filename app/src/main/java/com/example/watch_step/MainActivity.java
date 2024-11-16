package com.example.watch_step;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvStepCount;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private float initialStepCount = 0;
    private float currentStepCount = 0;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    private NotificationService notificationService;
    private boolean isServiceBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
            notificationService = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the Foreground Service
        startForegroundService();

        // Initialize UI components
        tvStepCount = findViewById(R.id.tvStepCount);
        Button btnOpenNotes = findViewById(R.id.btnOpenNotes);
        Button btnViewNotes = findViewById(R.id.btnViewNotes);

        // Set button listeners
        btnOpenNotes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });

        btnViewNotes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewNotesActivity.class);
            startActivity(intent);
        });

        // Initialize SensorManager and step sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor == null) {
                Toast.makeText(this, "Step Counter sensor not available!", Toast.LENGTH_LONG).show();
                CSVLogger csvLogger = new CSVLogger(this);
                csvLogger.logEvent("Error", "Step Counter sensor not available.");
            }
        } else {
            Toast.makeText(this, "Sensor Manager not available!", Toast.LENGTH_LONG).show();
            CSVLogger csvLogger = new CSVLogger(this);
            csvLogger.logEvent("Error", "Sensor Manager not available.");
        }

        // Request necessary permissions
        requestPermissions();

        // Initialize SharedPreferences
        SharedPreferences prefs = getSharedPreferences("step_prefs", MODE_PRIVATE);
        initialStepCount = prefs.getFloat("initial_step_count", 0);

        // Bind to NotificationService to communicate updates
        Intent serviceIntent = new Intent(this, NotificationService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.setAction(NotificationService.ACTION_START_FOREGROUND_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void requestPermissions() {
        // Define the permissions to request
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.POST_NOTIFICATIONS
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION
            };
        }

        // Register the permissions callback
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean allGranted = true;
                    for (Boolean granted : result.values()) {
                        allGranted = allGranted && granted;
                    }
                    if (!allGranted) {
                        Toast.makeText(this, "Permissions not granted. App may not function correctly.", Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Launch the permission request
        requestPermissionLauncher.launch(permissions);

        // Schedule notifications using NotificationScheduler
        NotificationScheduler.scheduleNotifications(this, false); // Set to true for testing interval
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            if (initialStepCount == 0) {
                initialStepCount = event.values[0];
                SharedPreferences prefs = getSharedPreferences("step_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("initial_step_count", initialStepCount);
                editor.apply();
            }
            currentStepCount = event.values[0] - initialStepCount;
            tvStepCount.setText("Steps: " + (int) currentStepCount);

            // Update current step count in SharedPreferences
            SharedPreferencesHelper.setCurrentStepCount(this, currentStepCount);

            // Update the ForegroundService's notification
            if(isServiceBound && notificationService != null){
                notificationService.updateForegroundNotification(currentStepCount);
            }

        } catch (Exception e) {
            Log.e("MainActivity", "Error updating step count", e);
            CSVLogger csvLogger = new CSVLogger(this);
            csvLogger.logEvent("Error", "Error updating step count: " + e.getMessage());
            Toast.makeText(this, "Error updating step count.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
}