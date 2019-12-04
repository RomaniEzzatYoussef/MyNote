package com.romani.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NoteDetailsActivity extends AppCompatActivity
{

    TextView noteDate , noteContent;
    String noteID;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        noteDate = findViewById(R.id.noteDate_TxtView);
        noteContent = findViewById(R.id.noteContent_TXTView);

        noteDate.setText(getIntent().getStringExtra("noteDate"));
        noteContent.setText(getIntent().getStringExtra("noteContent"));
        noteID = getIntent().getStringExtra("noteID");
    }

    public void backToMainActivity(View v)
    {
        startActivity(new Intent(NoteDetailsActivity.this, MainActivity.class));
    }

    public void edit(View v)
    {
        Intent i = new Intent(this , AddNoteActivity.class);
        i.putExtra("noteContent" , noteContent.getText().toString());
        i.putExtra("noteID" , noteID);
        startActivity(i);
    }
}
