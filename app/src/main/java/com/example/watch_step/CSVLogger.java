// CSVLogger.java

package com.example.watch_step;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVLogger {
    private static final String TAG = "CSVLogger";
    private static final String FILE_NAME = "step_tracker_logs.csv";
    private Context context;

    public CSVLogger(Context context){
        this.context = context;
    }

    public void logEvent(String event, String details){
        FileWriter writer = null;
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            boolean isNewFile = !file.exists();
            writer = new FileWriter(file, true);
            if(isNewFile){
                writer.append("Event,Details,Timestamp\n");
            }
            long timestamp = System.currentTimeMillis();
            writer.append("\"").append(event).append("\",")
                    .append("\"").append(details).append("\",")
                    .append(String.valueOf(timestamp)).append("\n");
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error writing to CSV", e);
            Toast.makeText(context, "Logging failed.", Toast.LENGTH_SHORT).show();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing FileWriter", e);
                }
            }
        }
    }

    public ArrayList<LogEntry> readLogs(){
        ArrayList<LogEntry> logEntries = new ArrayList<>();
        File file = new File(context.getFilesDir(), FILE_NAME);
        if(!file.exists()){
            return logEntries;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            boolean isHeader = true;
            while((line = reader.readLine()) != null){
                if(isHeader){
                    isHeader = false;
                    continue;
                }
                String[] parts = parseCSVLine(line);
                if(parts.length >= 3){
                    String event = parts[0];
                    String details = parts[1];
                    long timestamp = Long.parseLong(parts[2]);
                    logEntries.add(new LogEntry(event, details, timestamp));
                }
            }
        } catch (IOException e){
            Log.e(TAG, "Error reading CSV", e);
            Toast.makeText(context, "Failed to read logs.", Toast.LENGTH_SHORT).show();
        }

        return logEntries;
    }

    // Utility method to handle CSV parsing with quotes
    private String[] parseCSVLine(String line){
        ArrayList<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for(char c : line.toCharArray()){
            if(c == '\"'){
                inQuotes = !inQuotes;
            }
            else if(c == ',' && !inQuotes){
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            }
            else{
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }
}