package com.example.happybirthday.helpers

import android.content.Context
import android.database.Cursor
import android.net.Uri

class UriPathHelper {
    fun getPath(context: Context?, uri: Uri?): String? {
        if ("content".equals(uri?.scheme, ignoreCase = true)) {
            return context?.let { getDataColumn(it, uri) }
        } else if ("file".equals(uri?.scheme, ignoreCase = true)) {
            return uri?.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                uri?.let { context.contentResolver.query(it, projection, null, null, null) }
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}

