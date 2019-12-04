package com.romani.mynote.MyDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class NoteDB extends SQLiteOpenHelper
{
    public final static String DATABASE_NAME = "notesDB.db";
    public final static String TABLE_NAME = "notes";
    public final static String noteID = "noteID";
    public final static String noteContent = "noteContent";
    public final static String noteDate = "noteDate";


    public final static String SQL_Create_Table = "CREATE TABLE " + TABLE_NAME
            + " ( " + noteID + " INTEGER primary key autoincrement, " + noteContent + " TEXT, " + noteDate  + " TEXT " + " );";

    public NoteDB(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(SQL_Create_Table);
        Log.d("Table Created" , "table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(noteContent , note.getNoteContent());
        contentValues.put(noteDate , note.getNoteDate());

        long result = db.insert(TABLE_NAME , null , contentValues);
        db.close();

        return result;
    }

    public long updatetNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(noteContent , note.getNoteContent());
        contentValues.put(noteDate , note.getNoteDate());

        long result = db.update(TABLE_NAME , contentValues , noteID + " = "+  note.getNoteID(), null);
        db.close();

        return result;
    }

    public ArrayList<Note> getNotes()
    {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null ,  null, null ,null , null, null);

        while(cursor.moveToNext())
        {
            Note note = new Note();

            note.setNoteID(cursor.getString(0) );
            note.setNoteContent(cursor.getString(1));
            note.setNoteDate(cursor.getString(2) );

//            note.setNoteID((cursor.getString(0) == null) ? 0+"" : cursor.getString(0));
//            note.setNoteContent((cursor.getString(1) == null)? 0+"" : cursor.getString(1));
//            note.setNoteDate((cursor.getString(2) == null)? 0+"" : cursor.getString(2));

            notes.add(note);
        }

        cursor.close();

        db.close();

        return notes;
    }

    public ArrayList<Note> getNotes(String value)
    {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null ,  noteContent + " like '%%" + value +"%%'", null ,null , null, null);

        cursor.moveToFirst();

        while(cursor.moveToNext())
        {
            Note note = new Note();

            note.setNoteID(cursor.getString(0));
            note.setNoteContent((cursor.getString(1) == null)? 0+"" : cursor.getString(1));
            note.setNoteDate((cursor.getString(2) == null)? 0+"" : cursor.getString(2));

            notes.add(note);
        }

        cursor.close();

        db.close();

        return notes;
    }


    public void deleteALLNotes()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null , null);

    }

    public void deleteNote(String noteIDValue)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, noteID + " = " + noteIDValue , null);

    }

}
