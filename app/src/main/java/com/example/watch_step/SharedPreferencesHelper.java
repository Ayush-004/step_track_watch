// app/src/main/java/com/example/watch_step/SharedPreferencesHelper.java

package com.example.watch_step;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "step_prefs";
    private static final String KEY_INITIAL_STEP = "initial_step_count";
    private static final String KEY_CURRENT_STEP = "current_step_count";

    public static void setInitialStepCount(Context context, float stepCount) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putFloat(KEY_INITIAL_STEP, stepCount).apply();
    }

    public static float getInitialStepCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_INITIAL_STEP, 0);
    }

    public static void setCurrentStepCount(Context context, float stepCount) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putFloat(KEY_CURRENT_STEP, stepCount).apply();
    }

    public static float getCurrentStepCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_CURRENT_STEP, 0);
    }

    // Add methods for notification sent times if needed
}