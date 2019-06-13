package com.example.ircore.cloudanchor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Places.db";
    private static final  int DATABASE_VERSION = 1;

    public DataBaseManager(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql = "create table Places ("
                        +" id integer primary key autoincrement,"
                        +" idAnchor String, "
                        +" place int)";
        db.execSQL( strSql);
        Log.i("DATABASE","OnCreate invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSql = "drop table Places";
        db.execSQL(strSql);
        this.onCreate(db);
        Log.i("DATABASE","onUpgrade invoked");
    }

    public void insertAnchor (String ID, int place){
        String strSql = "insert into Places (idAnchor, place) values ('"+ID+ "', "+place+")";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","Ancrage inserted");
    }

    public List<Ancrage> readAnchors(int place){
        List<Ancrage> liste = new ArrayList<>();
        String strSql = "select * from Places where (place="+place+")";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Ancrage ancrage = new Ancrage(cursor.getString(1),cursor.getInt(2));
            liste.add(ancrage);
            cursor.moveToNext();
        }
        cursor.close();

        return liste;

    }
}



