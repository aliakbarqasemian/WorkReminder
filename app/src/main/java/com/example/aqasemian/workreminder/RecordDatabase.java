package com.example.aqasemian.workreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by A.Qasemian on 12/17/2017.
 */

class RecordDatabase {
    static final String ROWID = "id";
    static final String TEXTHOLDER = "textHolder";
    static final String TIMEHOLDER = "timeHolder";
    static final String DATEHOLDER = "dateHolder";
    static final String TAG = "DBSpinner";

    static final String REMINDERDB = "reminderDB";
    static final String REMINERTB = "reminderTB";
    static final int DBV = '2';

    static final String CREATERTABLE = "CREATE TABLE reminderTB(id INTEGER PRIMARY KEY AUTOINCREMENT, textHolder TEXT NOT NULL, dateHolder TEXT NOT NULL, timeHolder TEXT NOT NULL);";

    final Context c2;
    SQLiteDatabase db2;
    RecordDatabase.DBHelper helper2;
    public RecordDatabase(Context ctx2){
        this.c2 = ctx2;
        helper2 = new RecordDatabase.DBHelper(c2);
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context){
            super(context, REMINDERDB, null, DBV);
        }

        @Override
        public void onCreate(SQLiteDatabase db2) {
            try {
                db2.execSQL(CREATERTABLE);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db2, int oldVersion2, int newVersion2) {
            Log.w(TAG, "Upgrading DB");
            db2.execSQL("DROP TABLE IF EXISTS reminderTB");
            onCreate(db2);
        }
    }
    public RecordDatabase openDatabase(){
        try {
            db2 = helper2.getWritableDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return this;
    }
    public void closeDatabase(){
        helper2.close();
    }
    public long addRecord(String reminderText, String reminderDate, String reminerTime){
        try {
            ContentValues cv2 = new ContentValues();
            cv2.put(TEXTHOLDER, reminderText);
            cv2.put(DATEHOLDER, reminderDate);
            cv2.put(TIMEHOLDER, reminerTime);
            return db2.insert(REMINERTB, ROWID, cv2);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public Cursor getAllValues(){
        String[] columns2 = {ROWID, TEXTHOLDER, DATEHOLDER, TIMEHOLDER};
        return db2.query(REMINERTB, columns2,null,null,null,null,null);

    }
}
