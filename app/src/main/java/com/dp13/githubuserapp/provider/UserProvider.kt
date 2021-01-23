package com.dp13.githubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dp13.githubuserapp.db.UserContract.AUTHORITY
import com.dp13.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.dp13.githubuserapp.db.UserContract.UserColumns.Companion.TABLE_NAME
import com.dp13.githubuserapp.db.UserHelper

class UserProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private const val USERNAME = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper
        init {
            // content://com.dp13.githubuserapp/favorite_user
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

            // content://com.dp13.githubuserapp/favorite_user/id
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)

            // content://com.dp13.githubuserapp/favorite_user/username
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USERNAME)

        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USER_ID -> userHelper.queryById(uri.lastPathSegment.toString())
            USERNAME -> userHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(),contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}
