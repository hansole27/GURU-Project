package com.example.guruproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {



    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 쿼리
        db!!.execSQL("""
            CREATE TABLE book (
                title TEXT, 
                author TEXT, 
                publisher TEXT, 
                start_date TEXT, 
                end_date TEXT,
                memo TEXT,
                image_url TEXT
            )
        """)
        Log.d("DBManager", "<성공> 새로운 테이블 생성 완료")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}