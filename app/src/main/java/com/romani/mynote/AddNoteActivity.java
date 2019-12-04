package com.romani.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.romani.mynote.MyDB.Note;
import com.romani.mynote.MyDB.NoteDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {
    TextView date_TXT;
    EditText myNote;
    NoteDB noteDB;
    Note note = new Note();
    String noteContent = "", noteID = "";
    ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        date_TXT = findViewById(R.id.date_TxtView);
        myNote = findViewById(R.id.myNote_EditTXT);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        date_TXT.setText(formattedDate);


        noteDB = new NoteDB(this);

        noteContent = getIntent().getStringExtra("noteContent");
        noteID = getIntent().getStringExtra("noteID");
        myNote.setText(noteContent);

        notes = noteDB.getNotes(noteContent);
    }

    public void setNewNote()
    {
        note.setNoteID(noteID);
        note.setNoteContent(myNote.getText().toString());
        note.setNoteDate(date_TXT.getText().toString());
    }


    public void backToMainActivity(View v) {
       startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
    }

    public void done(View v)
    {
        if (noteContent == null)
        {
            setNewNote();
            noteDB.insertNote(note);

            Toast.makeText(this, "Added: " + myNote.getText().toString(), Toast.LENGTH_LONG).show();
            Log.d("Added: ", myNote.getText().toString());
            startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
        }

        else if (noteContent.length() > 2)
        {
            setNewNote();
            noteDB.updatetNote(note);

            Toast.makeText(this, "Updated: " + myNote.getText().toString(), Toast.LENGTH_LONG).show();
            Log.d("Updated: ", myNote.getText().toString());
            startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
        } else
            {
            Toast.makeText(this, "No Changes Happen OR  Changes Affected", Toast.LENGTH_LONG).show();
            Log.d("No Changes Happen: ", "No Changes Affected");
            startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
        }

    }
}
