package com.nimok97.accountbook.data.datasource

import android.provider.BaseColumns

const val DB_NAME = "account_book.db"

object HisitoryDBStructure: BaseColumns {
    val TABLE_NAME = "history"
    val COLUMN_ID = BaseColumns._ID
    val COLUMN_TYPE = "type"
    val COLUMN_YEAR = "year"
    val COLUMN_MONTH = "month"
    val COLUMN_DAY_NUM = "day_num"
    val COLUMN_DAY_STR = "day_str"
    val COLUMN_AMOUNT = "amount"
    val COLUMN_CONTENT = "content"
    val COLUMN_METHOD_ID = "method_id"
    val COLUMN_CATEGORY_ID = "category_id"

    val CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY NOT NULL, " +
            "$COLUMN_TYPE INTEGER NOT NULL, " +
            "$COLUMN_YEAR INTEGER NOT NULL, " +
            "$COLUMN_MONTH INTEGER NOT NULL, " +
            "$COLUMN_DAY_NUM INTEGER NOT NULL, " +
            "$COLUMN_DAY_STR TEXT NOT NULL, " +
            "$COLUMN_AMOUNT INTEGER NOT NULL, " +
            "$COLUMN_CONTENT TEXT, " +
            "$COLUMN_METHOD_ID INTEGER NOT NULL, " +
            "$COLUMN_CATEGORY_ID INTEGER NOT NULL)"

    val DROP_TABLE_HISTORY = "DROP TABLE IF EXISTS $TABLE_NAME"
}

object CategoryDBStructure: BaseColumns {
    val TABLE_NAME = "category"
    val COLUMN_ID = BaseColumns._ID
    val COLUMN_TYPE = "type" // 0 = 수입 , 1 = 지출
    val COLUMN_CONTENT = "content"
    val COLUMN_COLOR = "color"

    val CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY NOT NULL, " +
            "$COLUMN_TYPE INTEGER NOT NULL, " +
            "$COLUMN_CONTENT TEXT NOT NULL, " +
            "$COLUMN_COLOR TEXT NOT NULL)"

    val DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS $TABLE_NAME"
}

object MethodDBStructure: BaseColumns {
    val TABLE_NAME = "method"
    val COLUMN_ID = BaseColumns._ID
    val COLUMN_CONTENT = "content"

    val CREATE_TABLE_METHOD = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY NOT NULL, " +
            "$COLUMN_CONTENT TEXT NOT NULL)"

    val DROP_TABLE_METHOD = "DROP TABLE IF EXISTS $TABLE_NAME"
}