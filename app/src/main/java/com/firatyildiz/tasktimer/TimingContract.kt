package com.firatyildiz.tasktimer

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import com.firatyildiz.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY
import com.firatyildiz.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY_URI

object TimingContract {
    const val TABLE_NAME = "Timing"

    val CONTENT_URI: Uri = Uri.withAppendedPath(
        CONTENT_AUTHORITY_URI,
        TABLE_NAME
    )

    val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY_URI.$TABLE_NAME"
    val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    fun buildTimingUri(timingId: Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI, timingId)
    }

    fun getTimingId(uri: Uri): Long {
        return ContentUris.parseId(uri)
    }

    object Columns {
        const val _ID = BaseColumns._ID
        const val TIMING_TASK_ID = "TaskID"
        const val TIMING_START_TIME = "StartTime"
        const val TIMING_DURATION = "Duration"
    }
}

