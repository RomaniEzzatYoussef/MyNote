package com.romani.mynote.MyDB;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.romani.mynote.AddNoteActivity;
import com.romani.mynote.NoteDetailsActivity;
import com.romani.mynote.R;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter
{

    ArrayList<Note> notes;
    Context context;
    NoteDB noteDB;
    public NotesAdapter(Context context, ArrayList<Note> notes)
    {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_items, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(userView);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {
        final NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
        noteViewHolder.noteID.setText(notes.get(position).getNoteID());
        noteViewHolder.noteContent.setText(notes.get(position).getNoteContent());
        noteViewHolder.noteDate.setText(notes.get(position).getNoteDate());

        noteViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context , NoteDetailsActivity.class);
                intent.putExtra("noteID" , noteViewHolder.noteID.getText().toString());
                intent.putExtra("noteContent" , noteViewHolder.noteContent.getText().toString());
                intent.putExtra("noteDate" , noteViewHolder.noteDate.getText().toString());
                context.startActivity(intent);
            }
        });

        noteViewHolder.noteEditBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_options, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                Intent i = new Intent(context , AddNoteActivity.class);
                                i.putExtra("noteContent" , noteViewHolder.noteContent.getText().toString());
                                i.putExtra("noteID" , noteViewHolder.noteID.getText().toString());
                                context.startActivity(i);
                                return true;
                            case R.id.item_details:
                                Intent intent = new Intent(context , NoteDetailsActivity.class);
                                intent.putExtra("noteID" , noteViewHolder.noteID.getText().toString());
                                intent.putExtra("noteContent" , noteViewHolder.noteContent.getText().toString());
                                intent.putExtra("noteDate" , noteViewHolder.noteDate.getText().toString());
                                context.startActivity(intent);
                                return true;

                            case R.id.item_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                builder.setTitle("Confirm");
                                builder.setMessage("Are you sure you want to delete note '" + noteViewHolder.noteContent.getText().toString() +"' ?");

                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                                {

                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // Do nothing but close the dialog
                                        noteDB = new NoteDB(context);
                                        noteDB.deleteNote(noteViewHolder.noteID.getText().toString());
                                        notes.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context , "Note " + noteViewHolder.noteContent.getText().toString() + " Deleted" , Toast.LENGTH_LONG).show();
                                    }
                                });

                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                        // Do nothing
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();




            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout relativeLayout;
        Button noteEditBTN;
        TextView noteID , noteContent , noteDate;

        public NoteViewHolder(@NonNull View itemView)
        {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.item_layout);
            noteEditBTN = itemView.findViewById(R.id.edit_note_BTN);
            noteID = itemView.findViewById(R.id.note_id_TXTView);
            noteContent = itemView.findViewById(R.id.note_content_TXTView);
            noteDate = itemView.findViewById(R.id.date_text_view);
        }
    }
}
