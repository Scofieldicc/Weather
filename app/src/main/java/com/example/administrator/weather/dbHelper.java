package com.example.administrator.weather;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "data.db";
    private static final int VERSION = 1;
    private static final String SWORD="SWORD";
    private dbHelper database;

    public dbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        // TODO Auto-generated constructor stub
    }

    //create the db
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.i(SWORD, "create a database");

        String sql="create table user(name varchar(20) not null ," +
                " password varchar(60) not null );";
        db.execSQL(sql);

        sql="create table client(name varchar(20) not null,"
                +"age varchar(10),detail varchar(100),"
                +"address varchar(50),phone varchar(12));";
        db.execSQL(sql);
    }

    //update a db
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        Log.i(SWORD, "update a db");
    }

}
