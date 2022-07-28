package com.nimok97.accountbook.data.datasource

import android.content.ContentValues
import android.util.Log
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.History

class AccountBookDataSource(private val dbHelper: DBHelper) {

    suspend fun addHistory(history: History): Result<Long> {
        runCatching {
            val db = dbHelper.writableDatabase
            val value = ContentValues().apply {
                put(HisitoryDBStructure.COLUMN_TYPE, history.type)
                put(HisitoryDBStructure.COLUMN_YEAR, history.year)
                put(HisitoryDBStructure.COLUMN_MONTH, history.month)
                put(HisitoryDBStructure.COLUMN_DAY_NUM, history.dayNum)
                put(HisitoryDBStructure.COLUMN_DAY_STR, history.dayStr)
                put(HisitoryDBStructure.COLUMN_AMOUNT, history.amount)
                put(HisitoryDBStructure.COLUMN_CONTENT, history.content)
                put(HisitoryDBStructure.COLUMN_CATEGORY_ID, history.categoryId)
                put(HisitoryDBStructure.COLUMN_METHOD_ID, history.methodId)
            }
            db.insert(HisitoryDBStructure.TABLE_NAME, null, value)
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun getHistoriesByYearAndMonth(year: Int, month: Int): Result<List<History>> {
        runCatching {
            val db = dbHelper.readableDatabase

            val columns = arrayOf(
                HisitoryDBStructure.COLUMN_ID,
                HisitoryDBStructure.COLUMN_TYPE,
                HisitoryDBStructure.COLUMN_YEAR,
                HisitoryDBStructure.COLUMN_MONTH,
                HisitoryDBStructure.COLUMN_DAY_NUM,
                HisitoryDBStructure.COLUMN_DAY_STR,
                HisitoryDBStructure.COLUMN_AMOUNT,
                HisitoryDBStructure.COLUMN_CONTENT,
                HisitoryDBStructure.COLUMN_METHOD_ID,
                HisitoryDBStructure.COLUMN_CATEGORY_ID
            )

            val selection =
                "${HisitoryDBStructure.COLUMN_YEAR} = ? AND ${HisitoryDBStructure.COLUMN_MONTH} = ?"
            val selectionArgs = arrayOf("$year", "$month")
            val orderBy = "${HisitoryDBStructure.COLUMN_DAY_NUM} DESC"

            val cursor = db.query(
                HisitoryDBStructure.TABLE_NAME,
                columns, selection, selectionArgs, null, null, orderBy
            )

            val histories = ArrayList<History>()
            while (cursor.moveToNext()) {
                histories.add(
                    History(
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_YEAR)),
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_MONTH)),
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_DAY_NUM)),
                        cursor.getString(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_DAY_STR)),
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_CONTENT)),
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_METHOD_ID)),
                        cursor.getInt(cursor.getColumnIndex(HisitoryDBStructure.COLUMN_CATEGORY_ID))
                    )
                )
                printLog("${histories.last()}")
            }
            cursor.close()
            histories
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun updateHistory(id: Int, newHistory: History) {

    }

    suspend fun addCategory(categoryDao: CategoryDao): Result<Long> {
        runCatching {
            val db = dbHelper.writableDatabase
            val value = ContentValues().apply {
                put(CategoryDBStructure.COLUMN_TYPE, categoryDao.type)
                put(CategoryDBStructure.COLUMN_CONTENT, categoryDao.content)
                put(CategoryDBStructure.COLUMN_COLOR, categoryDao.color)
            }
            db.insert(CategoryDBStructure.TABLE_NAME, null, value)
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun getAllCategory(): Result<List<Category>> {
        runCatching {
            val db = dbHelper.readableDatabase

            val columns = arrayOf(
                CategoryDBStructure.COLUMN_ID,
                CategoryDBStructure.COLUMN_TYPE,
                CategoryDBStructure.COLUMN_CONTENT,
                CategoryDBStructure.COLUMN_COLOR
            )

            val cursor = db.query(
                CategoryDBStructure.TABLE_NAME,
                columns, null, null, null, null, null
            )

            val categories = ArrayList<Category>()
            while (cursor.moveToNext()) {
                categories.add(
                    Category(
                        cursor.getInt(cursor.getColumnIndex(CategoryDBStructure.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(CategoryDBStructure.COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndex(CategoryDBStructure.COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(CategoryDBStructure.COLUMN_COLOR))
                    )
                )
                //printLog("${categories.last()}")
            }
            cursor.close()
            categories
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun getCategoryById() {

    }

    suspend fun updateCategory() {

    }

    suspend fun addMethod() {

    }

    suspend fun getAllMethod() {

    }

    suspend fun getMethodById() {

    }

    suspend fun updateMethod() {

    }

}