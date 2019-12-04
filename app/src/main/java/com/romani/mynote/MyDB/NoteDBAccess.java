package com.romani.mynote.MyDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NoteDBAccess
{
    public final static String TABLE_NAME = "notes";
    public final static String noteID = "noteID";
    public final static String noteContent = "noteContent";
    public final static String noteDate = "noteDate";

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static NoteDBAccess instance;
    Cursor c = null;

    private NoteDBAccess(Context context)
    {
        this.openHelper = new NoteDBOpenHelper(context);
    }

    public static NoteDBAccess getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new NoteDBAccess(context);
        }
        return instance;
    }

    public void open()
    {
        this.db = openHelper.getWritableDatabase();
    }

    public void close()
    {
        if (db != null)
        {
            this.db.close();
        }
    }




    public long insertNote(Note note)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(noteContent , note.getNoteContent());
        contentValues.put(noteDate , note.getNoteDate());

        long result = db.insert(TABLE_NAME , null , contentValues);
        db.close();

        return result;
    }

    public long updatetNote(Note note)
    {
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

        Cursor cursor = db.query(TABLE_NAME, null ,  null, null ,null , null, null);

        while(cursor.moveToNext())
        {
            Note note = new Note();

            note.setNoteID(cursor.getString(0) );
            note.setNoteContent(cursor.getString(1));
            note.setNoteDate(cursor.getString(2) );

            notes.add(note);
        }

        cursor.close();

        db.close();

        return notes;
    }

    public ArrayList<Note> getNotes(String value)
    {
        ArrayList<Note> notes = new ArrayList<>();

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

        db.delete(TABLE_NAME, null , null);

    }

    public void deleteNote(String noteIDValue)
    {

        db.delete(TABLE_NAME, noteID + " = " + noteIDValue , null);

    }




}
