package com.example.watch_step;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private ArrayList<Note> notesList;
    private OnNoteListener onNoteListener;

    public interface OnNoteListener{
        void onDeleteClick(int position);
    }

    public NotesAdapter(ArrayList<Note> notesList, OnNoteListener onNoteListener){
        this.notesList = notesList;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position){
        Note note = notesList.get(position);
        holder.tvNoteText.setText(note.getNoteText());
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(note.getTimestamp()));
        holder.tvTimestamp.setText(date);
    }

    @Override
    public int getItemCount(){
        return notesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView tvNoteText, tvTimestamp;
        Button btnDelete;

        public NoteViewHolder(@NonNull View itemView, OnNoteListener onNoteListener){
            super(itemView);
            tvNoteText = itemView.findViewById(R.id.tvNoteText);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(v -> {
                if(onNoteListener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onNoteListener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}