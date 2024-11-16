// ViewNotesActivity.java

package com.example.watch_step;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewNotesActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private ArrayList<Note> notesList;
    private NotesDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        recyclerView = findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesList = new ArrayList<>();
        dbHelper = new NotesDatabaseHelper(this);

        loadNotes();

        adapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadNotes(){
        Cursor cursor = dbHelper.getAllNotes();
        if(cursor != null && cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String noteText = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"));
                long notificationId = cursor.getLong(cursor.getColumnIndexOrThrow("notification_id"));

                notesList.add(new Note(id, noteText, timestamp, notificationId));
            } while(cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    public void onDeleteClick(int position) {
        Note note = notesList.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean success = dbHelper.deleteNote(note.getId());
                    if(success){
                        notesList.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}