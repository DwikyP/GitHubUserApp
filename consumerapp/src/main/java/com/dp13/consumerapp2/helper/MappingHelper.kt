package com.dp13.consumerapp2.helper

import android.database.Cursor
import com.dp13.consumerapp2.entity.User
import com.dp13.consumerapp2.db.UserContract

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {

        val userList = ArrayList<User>()
        notesCursor?.apply {
            while (moveToNext()) {
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
                val user = User()
                user.photo = avatar
                user.username = username
                userList.add(user)
            }
        }
        return userList
    }

    fun mapCursorToObject(notesCursor: Cursor?): User {
        var user = User()
        notesCursor?.apply {
            moveToFirst()
            val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL))
            val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
            user.photo = avatar
            user.username = username
        }
        return user
    }
}