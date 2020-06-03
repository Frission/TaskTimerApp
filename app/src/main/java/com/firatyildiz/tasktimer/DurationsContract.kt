package com.firatyildiz.tasktimer

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import com.firatyildiz.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY
import com.firatyildiz.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY_URI

object DurationsContract {
    const val TABLE_NAME = "ViewTaskDurations"

    val CONTENT_URI: Uri = Uri.withAppendedPath(
        CONTENT_AUTHORITY_URI,
        TABLE_NAME
    )

    val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY_URI.$TABLE_NAME"
    val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    fun getDurationId(uri: Uri): Long {
        return ContentUris.parseId(uri)
    }

    object Columns {
        const val _ID = BaseColumns._ID
        const val DURATIONS_NAME = TasksContract.Columns.TASKS_NAME
        const val DURATIONS_DESCRIPTION = TasksContract.Columns.TASKS_DESCRIPTION
        const val DURATIONS_START_TIME = TimingContract.Columns.TIMING_START_TIME
        const val DURATIONS_START_DATE = "StartDate"
        const val DURATIONS_DURATION = TimingContract.Columns.TIMING_DURATION
    }
}

