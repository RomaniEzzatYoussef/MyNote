package com.romani.mynote.MyDB;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class NoteDBOpenHelper extends SQLiteAssetHelper
{
    public final static String DATABASE_NAME = "notesDB.db";

    public NoteDBOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }
}
