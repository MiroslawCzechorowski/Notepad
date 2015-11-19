package com.example.czechorowski.notepad;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.czechorowski.notepad.db.Database;
import com.example.czechorowski.notepad.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MiroslawCzechorowski on 19.11.2015.
 */
public class AddNewFile extends AppCompatActivity {
    private String title;
    private EditText editText;
    private Button ok;
    private Button cancel;
    private DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_file);

        editText=(EditText)findViewById(R.id.editText);
        ok=(Button)findViewById(R.id.okButton);
        //Add new note
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length()<2){
                Toast.makeText(getApplicationContext(),"Application wont save empty file",Toast.LENGTH_LONG).show();
                }else
                {
                    //Show alert dialog and ask for note name
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNewFile.this);
                    builder.setTitle("Save as...");

                    final EditText input = new EditText(AddNewFile.this);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            title = input.getText().toString();
                            helper = new DatabaseHelper(AddNewFile.this);
                            SQLiteDatabase sqlDB = helper.getWritableDatabase();
                            String text = editText.getText().toString().trim();
                            ContentValues values = new ContentValues();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strDate = sdf.format(new Date());

                            values.clear();
                            values.put(Database.Columns.TITLE, title);
                            values.put(Database.Columns.DATA,text);
                            values.put(Database.Columns.DATE,strDate);
                            sqlDB.insertWithOnConflict(Database.TABLE, null,values,SQLiteDatabase.CONFLICT_IGNORE);
                            onBackPressed();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            onBackPressed();
                        }
                    });
                    builder.show();
                }
            }
        });

        cancel=(Button)findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
