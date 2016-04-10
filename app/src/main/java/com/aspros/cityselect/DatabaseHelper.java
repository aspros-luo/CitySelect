package com.aspros.cityselect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aspros on 16/4/9.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_COUNTRY = "CREATE TABLE  country  ( " +
            "id TEXT, " +
            "countryName TEXT " +
            ")";
    private static final String CREATE_PROVINCE = "CREATE TABLE  province  ( " +
            "id TEXT, " +
            "countryId TEXT, "+
            "provinceName TEXT" +
            ")";
    private static final String CREATE_CITY = "CREATE TABLE  city  ( " +
            "id TEXT," +
            "provinceId TEXT," +
            "cityName TEXT" +
            ")";
    private static final String CREATE_AREA = "CREATE TABLE  area  ( " +
            "id TEXT," +
            "provinceId TEXT," +
            "cityId TEXT," +
            "areaName TEXT" +
            ")";

    private Context mContext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COUNTRY);
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_AREA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
