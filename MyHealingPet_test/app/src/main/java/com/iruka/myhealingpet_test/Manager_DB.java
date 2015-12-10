package com.iruka.myhealingpet_test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sung TaeHun on 2015-11-20.
 */

// DB 관리 클래스
// DB를 생성하고 버전을 관리한다.
// DB에 대한 삽입, 수정, 삭제를 함수를 통해서 실행한다.
// DB에서 name을 입력하여 value를 가져올 수 있다.
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

