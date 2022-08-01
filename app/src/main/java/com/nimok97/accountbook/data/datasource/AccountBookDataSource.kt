package com.nimok97.accountbook.data.datasource

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.model.Method

class AccountBookDataSource(private val dbHelper: DBHelper) {

    suspend fun addHistory(historyDao: HistoryDao): Result<Long> { // 내역 추가
        runCatching {
            val db = dbHelper.writableDatabase
            val value = ContentValues().apply {
                put(HisitoryDBStructure.COLUMN_TYPE, historyDao.type)
                put(HisitoryDBStructure.COLUMN_YEAR, historyDao.year)
                put(HisitoryDBStructure.COLUMN_MONTH, historyDao.month)
                put(HisitoryDBStructure.COLUMN_DAY_NUM, historyDao.dayNum)
                put(HisitoryDBStructure.COLUMN_DAY_STR, historyDao.dayStr)
                put(HisitoryDBStructure.COLUMN_AMOUNT, historyDao.amount)
                put(HisitoryDBStructure.COLUMN_CONTENT, historyDao.content)
                put(HisitoryDBStructure.COLUMN_CATEGORY_ID, historyDao.categoryId)
                put(HisitoryDBStructure.COLUMN_METHOD_ID, historyDao.methodId)
            }
            db.insert(HisitoryDBStructure.TABLE_NAME, null, value)
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun getHistoriesByYearAndMonth(
        year: Int,
        month: Int
    ): Result<List<History>> { // 연, 월 값으로 해당하는 달의 전체 내역 조회
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

    suspend fun updateHistory(id: Int, newHistory: History) { // 내역 정보 업데이트

    }

    suspend fun addCategory(categoryDao: CategoryDao): Result<Long> { // 수입/지출 카테고리 추가
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

    suspend fun getAllCategory(): Result<List<Category>> { // 모든 카테고리 조회
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

    suspend fun getCategoryById(categoryId: Int): Result<Category> {  // id 값으로 해당 카테고리 가져오기
        runCatching {
            var db = dbHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM " + CategoryDBStructure.TABLE_NAME +
                        " WHERE ${CategoryDBStructure.COLUMN_ID} == $categoryId ", null
            )
            var category = Category(-1, -1, "", "")
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(CategoryDBStructure.COLUMN_ID))
                val type = cursor.getInt(cursor.getColumnIndex(CategoryDBStructure.COLUMN_TYPE))
                val content =
                    cursor.getString(cursor.getColumnIndex(CategoryDBStructure.COLUMN_CONTENT))
                val color =
                    cursor.getString(cursor.getColumnIndex(CategoryDBStructure.COLUMN_COLOR))
                category = Category(id, type, content, color)
            }
            category
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun updateCategory() {

    }

    suspend fun addMethod(methodDao: MethodDao): Result<Long> { // 결제 수단 추가
        runCatching {
            val db = dbHelper.writableDatabase
            val value = ContentValues().apply {
                put(MethodDBStructure.COLUMN_CONTENT, methodDao.content)
            }
            db.insert(MethodDBStructure.TABLE_NAME, null, value)
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun getAllMethod(): Result<List<Method>> { // 모든 결제 수단 조회
        runCatching {
            val db = dbHelper.readableDatabase

            val columns = arrayOf(
                MethodDBStructure.COLUMN_ID,
                MethodDBStructure.COLUMN_CONTENT
            )

            val cursor = db.query(
                MethodDBStructure.TABLE_NAME,
                columns, null, null, null, null, null
            )

            val methods = ArrayList<Method>()
            while (cursor.moveToNext()) {
                methods.add(
                    Method(
                        cursor.getInt(cursor.getColumnIndex(MethodDBStructure.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(MethodDBStructure.COLUMN_CONTENT))
                    )
                )
            }
            cursor.close()
            methods
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun getMethodById(methodId: Int): Result<Method> { // id 값으로 해당 결제 수단 가져오기
        runCatching {
            var db = dbHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM " + MethodDBStructure.TABLE_NAME +
                        " WHERE ${MethodDBStructure.COLUMN_ID} == $methodId ", null
            )
            var method = Method(-1, "")
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(MethodDBStructure.COLUMN_ID))
                val content =
                    cursor.getString(cursor.getColumnIndex(MethodDBStructure.COLUMN_CONTENT))
                method = Method(id, content)
            }
            method
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun checkMethodExistenceByContent(content: String): Result<Boolean> { // 결제 수단 추가 시 중복체크 위함
        runCatching {
            var db = dbHelper.readableDatabase
            val columns = arrayOf(
                MethodDBStructure.COLUMN_CONTENT
            )
            val selection = "${MethodDBStructure.COLUMN_CONTENT} = ?"
            val selectionArgs = arrayOf(content)
//            val cursor = db.rawQuery(
//                "SELECT * FROM " + MethodDBStructure.TABLE_NAME +
//                        " WHERE ${MethodDBStructure.COLUMN_CONTENT} = $content ", null
//            )
            val cursor = db.query(
                MethodDBStructure.TABLE_NAME, columns, selection, selectionArgs,
                null, null, null
            )
            var count = 0
            while (cursor.moveToNext()) {
                count++
            }
            count != 0
        }.onSuccess {
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Throwable("db error"))
    }

    suspend fun updateMethod() {

    }

}