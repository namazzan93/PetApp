package com.iruka.myhealingpet_test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sung TaeHun on 2015-11-20.
 */
public class Manager_DB extends SQLiteOpenHelper {
    private static final String TB_Name = "Pet";
    private static final String DB_Name = "MyDB";
    public static final int DB_Version = 1;



    public Manager_DB(Context context){
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table Pet ( _id integer primary key autoincrement, name text unique, value integer);");
        //db.execSQL("insert into Pet values (null, 'level', 0);");
        //db.execSQL("insert into Pet values (null, 'heart', 0);");
        //db.execSQL("insert into Pet values (null, 'hungry', 100);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exist Pet;");
    }
    public void updateData(String name, int value){
        String sql = "update Pet set value = '" + value + "' where name = '" + name + "';";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public int selectValue(String name){
        String sql = "SELECT value FROM Pet where name = '"+ name + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(sql, null);
        result.moveToFirst();
        return result.getInt(0);
    }
}

