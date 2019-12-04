package com.romani.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.romani.mynote.MyDB.Note;
import com.romani.mynote.MyDB.NoteDB;
import com.romani.mynote.MyDB.NotesAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextWatcher , LocationListener
{
    EditText searchView;
    RecyclerView recyclerView;
    ArrayList<Note> notes = new ArrayList<>();
    NoteDB noteDB;

    //Location
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int PERMISSION_REQ_CODE = 0;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.notes_recyclerView);
        searchView = findViewById(R.id.searchView_noteContent);


        noteDB = new NoteDB(this);
        bindRecycler();

        searchView.addTextChangedListener(this);

        String s = "";
        for (int i = 0; i < notes.size(); i++)
        {
            s = " id:" + notes.get(i).getNoteID();
        }

        Log.e("Notes ArrayList: " , s);




        //Location

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this , permissions , PERMISSION_REQ_CODE);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 0,0 , MainActivity.this);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQ_CODE)
        {
            if(ActivityCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this , permissions , PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

    }

    public void showAddNoteActivity(View v)
    {
        Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
        i.putExtra("noteID" , "true");
        startActivity(i);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        notes = noteDB.getNotes();
        bindRecycler();

    }

    public void bindRecycler() {
        NotesAdapter movieAdapter = new NotesAdapter(this, notes);
        recyclerView.setAdapter(movieAdapter);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager1);
    }

    public void deleteAll(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete all notes?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface dialog, int which)
            {
                // Do nothing but close the dialog
                noteDB.deleteALLNotes();
                notes = noteDB.getNotes();
                bindRecycler();
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
    }

    public void refresh(View v)
    {
        notes = noteDB.getNotes();
        bindRecycler();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        notes = noteDB.getNotes(searchView.getText().toString());
        bindRecycler();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        Toast.makeText(this , "Long: " + location.getLongitude() + " \nLat: " + location.getLatitude() , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
