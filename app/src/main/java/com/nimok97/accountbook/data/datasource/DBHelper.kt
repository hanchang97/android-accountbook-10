package com.nimok97.accountbook.data.datasource

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(HisitoryDBStructure.CREATE_TABLE_HISTORY)
        db.execSQL(CategoryDBStructure.CREATE_TABLE_CATEGORY)
        db.execSQL(MethodDBStructure.CREATE_TABLE_METHOD)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(HisitoryDBStructure.DROP_TABLE_HISTORY)
        db.execSQL(CategoryDBStructure.DROP_TABLE_CATEGORY)
        db.execSQL(MethodDBStructure.DROP_TABLE_METHOD)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}