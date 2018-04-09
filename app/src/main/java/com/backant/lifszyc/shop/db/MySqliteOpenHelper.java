package com.backant.lifszyc.shop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    public static final String db_name = "user.db";
    public String column_id = "id", column_name = "name";

    public static final String TABLE_NAME = "user";   // Table Name
    public static final int DATABASE_Version = 1;    // Database Version
    public static final String UID="id";     // Column I (Primary Key)
    public static final String NAME = "name";    //Column II
    public static final String MyPASSWORD= "Password";    // Column III
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+ MyPASSWORD+" VARCHAR(225));";
    public static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
    public Context context;



    private String create_query = "CREATE TABLE user (id integer primary key autoincrement, name VARCHAR(255) not null, surname VARCHAR(255) not null, email VARCHAR(255) not null, password VARCHAR(255) not null, activo integer not null, role integer not null);";


    public MySqliteOpenHelper(Context context) {
        super(context, db_name, null, DATABASE_Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.e("DB", "DB ON CREATE METHOD");
//            db.execSQL(DROP_TABLE);
//            onCreate(db);
            db.execSQL(create_query);
        } catch (Exception e) {
            Log.e("VERGAS", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB ON UPGRADE", "DB ON UPGRADE METHOD");
    }

}

