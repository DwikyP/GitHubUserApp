package com.dp13.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dp13.githubuserapp.db.UserContract.UserColumns
import com.dp13.githubuserapp.db.UserContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context):  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "dbgithubuserapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_USER_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumns._ID} INTEGER PRIMARY KEY," +
                " ${UserColumns.COLUMN_NAME_USERNAME} TEXT NOT NULL," +
                " ${UserColumns.COLUMN_NAME_AVATAR_URL} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}