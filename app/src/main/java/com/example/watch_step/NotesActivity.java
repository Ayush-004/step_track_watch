// NotesActivity.java
package com.example.watch_step;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotesActivity extends AppCompatActivity {

    private EditText etNote;
    private Button btnSaveNote;
    private NotesDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        etNote = findViewById(R.id.etNote);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        dbHelper = new NotesDatabaseHelper(this);

        btnSaveNote.setOnClickListener(v -> {
            String noteText = etNote.getText().toString().trim();
            if(noteText.isEmpty()){
                Toast.makeText(this, "Please enter a note.", Toast.LENGTH_SHORT).show();
                return;
            }

            long notificationId = 1; // You can associate with actual notification IDs if needed
            boolean success = dbHelper.insertNote(noteText, notificationId);
            if(success){
                Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT).show();
                etNote.setText("");
            }
            else{
                Toast.makeText(this, "Failed to save note.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
