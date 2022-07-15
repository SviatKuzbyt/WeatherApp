package com.sviatkuzbyt.weatherapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DataBase(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(
            "CREATE TABLE CITIES(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CITY TEXT)"
        )

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if (p1 < 2){
            p0?.execSQL("ALTER TABLE CITIES ADD COLUMN CITYEN TEXT")
        }
    }

    companion object{
        private const val DB_NAME = "DB_WEATHER_APP"
        private const val DB_VERSION = 2
    }

}