package com.example.aqasemian.workreminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private  static String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener theDateSetListener;
    private TimePickerDialog.OnTimeSetListener theTimeSetListener;

    Calendar calendar = Calendar.getInstance();
    EditText note;
    TextView txDate;
    TextView txTime;
    Button btSave;
    Button btCancel;
    String strDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        note = (EditText)findViewById(R.id.editTextNote);
        txDate = (TextView) findViewById(R.id.textViewDate);
        txTime = (TextView) findViewById(R.id.textViewTime);
        btSave = (Button)findViewById(R.id.buttonSave);
        btCancel = (Button)findViewById(R.id.buttonCancel);
        final RecordDatabase rdb = new RecordDatabase(this);
        //-----------------------------------------------------
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        strDate = (month + 1) + "/" + day + "/" + year;
        //-------------------------------------------------------
        rdb.openDatabase();
        Cursor c = rdb.getAllValues();
        while (c.moveToNext()){
            String theNote = c.getString(1);
            String theDate = c.getString(2);
            String theTime = c.getString(3);
            if(theDate.equals(strDate)){
                alrt1(theNote, theDate, theTime);
            }
        }

        rdb.closeDatabase();

        txTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calend = Calendar.getInstance();
                int hour = calend.get(Calendar.HOUR_OF_DAY);
                int minute = calend.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(MainActivity.this,theTimeSetListener, hour, minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        theTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                txTime.setText(hourOfDay + ":" + minute);
            }
        };
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        theDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        theDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                txDate.setText(date);
            }
        };
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(note.getText().toString().equals("") || txDate.getText().toString().equals("") || txTime.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "insert all demanded data please.", Toast.LENGTH_SHORT).show();
                }else {
                    rdb.openDatabase();
                    long addCondition = rdb.addRecord(note.getText().toString(), txDate.getText().toString(), txTime.getText().toString());
                    if(addCondition != 0){
                        note.setText("");
                        Toast.makeText(MainActivity.this, "all records added.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Failed no record added.", Toast.LENGTH_SHORT).show();
                    }

                    rdb.closeDatabase();
                }
            }
        });
    }
    private void alrt1(String noteHold, String dateHold, String timeHold){
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
        myBuilder.setMessage(noteHold+ " at " + dateHold + " on " + timeHold)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog myAlert1 = myBuilder.create();
        myAlert1.setTitle("Alert !!!");
        myAlert1.show();
    }
}
