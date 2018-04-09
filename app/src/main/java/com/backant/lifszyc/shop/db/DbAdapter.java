package com.backant.lifszyc.shop.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.backant.lifszyc.shop.db.MySqliteOpenHelper;

public class DbAdapter {
    MySqliteOpenHelper myhelper;

    public DbAdapter(Context context) {
        myhelper = new MySqliteOpenHelper(context);
    }

    public String getData() {

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {MySqliteOpenHelper.UID,MySqliteOpenHelper.NAME};
        Cursor cursor = db.query(MySqliteOpenHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(MySqliteOpenHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.NAME));
            buffer.append(cid + "   " + name + " \n");
        }
        return buffer.toString();
    }
}
