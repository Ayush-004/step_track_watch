// NotesDatabaseHelper.java

package com.example.watch_step;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "NotesDatabaseHelper";
    private static final String DATABASE_NAME = "step_tracker.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_NOTIFICATION_ID = "notification_id";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOTE + " TEXT, " +
                    COLUMN_TIMESTAMP + " INTEGER, " +
                    COLUMN_NOTIFICATION_ID + " INTEGER" +
                    ")";

    public NotesDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e){
            Log.e(TAG, "Error creating table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
            onCreate(db);
        } catch (Exception e){
            Log.e(TAG, "Error upgrading table", e);
        }
    }

    public boolean insertNote(String note, long notificationId){
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE, note);
            values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
            values.put(COLUMN_NOTIFICATION_ID, notificationId);
            long result = db.insert(TABLE_NOTES, null, values);
            return result != -1;
        } catch (Exception e){
            Log.e(TAG, "Error inserting note", e);
            return false;
        } finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public Cursor getAllNotes(){
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            return db.query(TABLE_NOTES, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");
        } catch (Exception e){
            Log.e(TAG, "Error fetching notes", e);
            return null;
        }
    }

    public boolean deleteNote(int id){
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int result = db.delete(TABLE_NOTES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            return result > 0;
        } catch (Exception e){
            Log.e(TAG, "Error deleting note", e);
            return false;
        } finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }
}