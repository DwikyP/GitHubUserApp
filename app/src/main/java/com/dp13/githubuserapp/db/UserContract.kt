package com.dp13.githubuserapp.db

import android.net.Uri
import android.provider.BaseColumns

object UserContract {
    const val AUTHORITY = "com.dp13.githubuserapp"
    const val SCHEME = "content"

    class UserColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}