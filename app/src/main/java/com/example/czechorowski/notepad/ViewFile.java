package com.example.czechorowski.notepad;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czechorowski.notepad.db.Database;
import com.example.czechorowski.notepad.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewFile extends AppCompatActivity {
    DatabaseHelper helper;
    EditText fileTitle;
    EditText fileData;
    Button saveButton;
    String _ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);
        //Get which one file user clicked
        final String file;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            file = null;
        } else {
            file = extras.getString("FILE");
        }
        fileTitle = (EditText) findViewById(R.id.editTextFileTitle);
        fileData = (EditText) findViewById(R.id.editTextFileData);

        //Get clicked note data from SQLite database
        helper = new DatabaseHelper(ViewFile.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT * from " + Database.TABLE + " where " + Database.Columns.DATE + " = '" + file + "'", null);
        //Set received data to UI
        if (cursor.moveToFirst()) {
            fileData.setText(cursor.getString(cursor.getColumnIndex(Database.Columns.DATA)));
            fileTitle.setText(cursor.getString(cursor.getColumnIndex(Database.Columns.TITLE)));
            _ID=cursor.getString(cursor.getColumnIndex(Database.Columns._ID));
        }else{
            Toast.makeText(getApplicationContext(),"An error occured. Try again",Toast.LENGTH_LONG).show();
        }

        //Save changes
        saveButton=(Button)findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(new Date());
                fileTitle=(EditText)findViewById(R.id.editTextFileTitle);
                fileData=(EditText)findViewById(R.id.editTextFileData);
                String newFileTitle=fileTitle.getText().toString().trim();
                String newFileData =fileData.getText().toString().trim();

                helper=new DatabaseHelper(ViewFile.this);
                String sql=String.format(String.format("UPDATE %s " +
                                "SET %s = '%s'," +
                                " %s = '%s'," +
                                " %s = '%s'"+
                                " WHERE %s = '%s';",
                        Database.TABLE, Database.Columns.TITLE, newFileTitle, Database.Columns.DATA,newFileData, Database.Columns.DATE, strDate,Database.Columns._ID,_ID));
                SQLiteDatabase sqlDb= helper.getWritableDatabase();
                sqlDb.execSQL(sql);
                onBackPressed();
            }
        });
    }
}
