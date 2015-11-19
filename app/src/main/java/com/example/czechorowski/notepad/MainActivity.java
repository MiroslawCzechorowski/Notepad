package com.example.czechorowski.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czechorowski.notepad.db.Database;
import com.example.czechorowski.notepad.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    private Button add;
    private ListView listView;

    //Update always on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        updateFiles();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start new intent which one you will add new note
        add=(Button)findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,AddNewFile.class);
                startActivity(intent);
            }
        });

        listView=(ListView)findViewById(R.id.listView);
        // DELETING FILES FROM NOTEPAD
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView toDelete= (TextView) view.findViewById(R.id.textViewData);
                final String fileDate =toDelete.getText().toString().trim();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure to delete this note?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper = new DatabaseHelper(MainActivity.this);
                        SQLiteDatabase sqlDB = helper.getWritableDatabase();
                        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                                Database.TABLE,
                                Database.Columns.DATE,
                                fileDate);
                        sqlDB.execSQL(sql);
                        updateFiles();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });
        //WATCHING FULL NOTE
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView clickedFile= (TextView) view.findViewById(R.id.textViewData);
                String fileDate =clickedFile.getText().toString().trim();
                Intent intent=new Intent(MainActivity.this, ViewFile.class);
                intent.putExtra("FILE", fileDate);
                startActivity(intent);

            }
        });
        updateFiles();
    }

    //Get notes from SQLite database
    private void updateFiles() {
        helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase sqlDB=helper.getReadableDatabase();

        Cursor cursor=sqlDB.query(Database.TABLE, new String[]{Database.Columns._ID, Database.Columns.TITLE, Database.Columns.DATA, Database.Columns.DATE},
                null, null, null, null, null, null);
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,R.layout.file_adapter,cursor,new String []{Database.Columns.DATE,Database.Columns.TITLE},
                new int[]{R.id.textViewData,R.id.textViewFile},0);
        listView.setAdapter(listAdapter);
    }

}
