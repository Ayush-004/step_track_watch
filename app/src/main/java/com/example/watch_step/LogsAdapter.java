// LogsAdapter.java

package com.example.watch_step;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogViewHolder> {

    private ArrayList<LogEntry> logsList;

    public LogsAdapter(ArrayList<LogEntry> logsList) {
        this.logsList = logsList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogEntry log = logsList.get(position);
        holder.tvEvent.setText(log.getEvent());
        holder.tvDetails.setText(log.getDetails());

        String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date(log.getTimestamp()));
        holder.tvTimestamp.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvEvent, tvDetails, tvTimestamp;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEvent = itemView.findViewById(R.id.tvEvent);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}