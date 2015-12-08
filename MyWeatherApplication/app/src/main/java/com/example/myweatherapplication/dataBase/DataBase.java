package com.example.myweatherapplication.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myweatherapplication.utils.ParseJSON;

import org.json.JSONObject;

/**
 * Created by Влад on 22.11.2015.
 */
public class DataBase extends SQLiteOpenHelper{
    public final static String TABLE_NAME = "Weather";
    public final static String DATE_AND_TIME = "dateAndTime";
    public final static String WEATHER = "weather";
    public final static String CITY = "city";
    public final static String COUNTRY = "country";
    public final static String TEMP = "temp";
    public final static String WIND_SPEED = "speed";
    public final static String PRESSURE = "pressure";
    public final static String ICON = "icon";

    public DataBase(Context context){
        super(context, "DataBase", null, 1);
    }

    public void onCreate(SQLiteDatabase dBase)
    {
        dBase.execSQL("create table " + TABLE_NAME + " (" +
                " id integer primary key autoincrement, " + DATE_AND_TIME + " text, "
                + COUNTRY + " text, " + CITY  + " text, "+ WEATHER + " text, " + TEMP + " real, "
                + WIND_SPEED + " real, " + PRESSURE + " real, " + ICON + " text); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
