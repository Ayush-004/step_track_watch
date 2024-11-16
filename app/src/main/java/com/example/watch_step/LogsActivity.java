// LogsActivity.java

package com.example.watch_step;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LogsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LogsAdapter adapter;
    private ArrayList<LogEntry> logsList;
    private CSVLogger csvLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        recyclerView = findViewById(R.id.recyclerViewLogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logsList = new ArrayList<>();
        csvLogger = new CSVLogger(this);

        loadLogs();

        adapter = new LogsAdapter(logsList);
        recyclerView.setAdapter(adapter);
    }

    private void loadLogs() {
        logsList.clear();
        ArrayList<LogEntry> entries = csvLogger.readLogs();
        if (entries != null) {
            logsList.addAll(entries);
        }
        if (logsList.isEmpty()) {
            logsList.add(new LogEntry("No Logs", "No log entries available.", System.currentTimeMillis()));
        }
    }
}