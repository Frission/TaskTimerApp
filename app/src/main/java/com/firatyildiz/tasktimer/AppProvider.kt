package com.firatyildiz.tasktimer

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/**
 * Provider for the TaskTimer app.
 */
class AppProvider : ContentProvider() {

    private val TAG = "AppProvider"

    private var openHelper: AppDatabase? = null

    companion object {
        const val CONTENT_AUTHORITY = "com.firatyildiz.tasktimer.provider"
        val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

        val uriMatcher: UriMatcher =
            buildUriMatcher()

        private fun buildUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)

            matcher.addURI(
                CONTENT_AUTHORITY,
                TasksContract.TABLE_NAME,
                TASKS
            )
            matcher.addURI(
                CONTENT_AUTHORITY, TasksContract.TABLE_NAME + "/#",
                TASKS_ID
            )

//            matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS)
//            matcher.addURI(ContentProvider, TimingsContract.TABLE_NAME + "/#", TIMINGS_ID)
//
//            matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS)
//            matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME + "/#", TASK_DURATIONS_ID)

            return matcher

        }

        private const val TASKS = 100
        private const val TASKS_ID = 101

        private const val TIMINGS = 200
        private const val TIMINGS_ID = 201

//        private const val TASK_TIMINGS = 300
//        private const val TASK_TIMINGS_ID = 301

        private const val TASK_DURATIONS = 400
        private const val TASK_DURATIONS_ID = 401
    }


    override fun onCreate(): Boolean {
        openHelper = context?.let {
            AppDatabase.getInstance(
                it
            )
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val match = uriMatcher.match(uri)

        val queryBuilder = SQLiteQueryBuilder()

        when(match) {
            TASKS -> queryBuilder.tables =
                TasksContract.TABLE_NAME
            TASKS_ID -> {
                queryBuilder.tables =
                    TasksContract.TABLE_NAME
                val taskId: Long =
                    TasksContract.getTaskId(uri)
                queryBuilder.appendWhere(TasksContract.Columns._ID + " = " + taskId)
            }

//            TIMINGS -> queryBuilder.tables = TimingsContract.TABLE_NAME
//            TIMINGS_ID -> {
//                queryBuilder.tables = TimingsContract.TABLE_NAME
//                var timingId: Long = TimingsContract.getTimingId(uri)
//                queryBuilder.appendWhere(TimingsContract.Columns._ID + " = " + timingId)
//            }

//            TASK_DURATIONS -> queryBuilder.tables = DurationsContract.TABLE_NAME
//            TASK_DURATIONS_ID -> {
//                queryBuilder.tables = DurationsContract.TABLE_NAME
//                var durationId: Long = DurationsContract.getTimingId(uri)
//                queryBuilder.appendWhere(DurationsContract.Columns._ID + " = " + durationId)
//            }
            else -> throw IllegalStateException("Unknown URI: $uri")
        }

        val db = openHelper?.readableDatabase
        val cursor: Cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        return cursor
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert: inserting")

        val match = uriMatcher.match(uri)

        val db: SQLiteDatabase?
        val recordId: Long
        var returnUri: Uri? = null

        when(match)
        {
            TASKS -> {
                db = openHelper?.writableDatabase

                if(db != null) {
                    recordId = db.insert(TasksContract.TABLE_NAME, null, values)
                    if(recordId >= 0)
                        returnUri =
                            TasksContract.buildTaskUri(
                                recordId
                            )
                    else
                        throw SQLException("Failed to insert into " + uri.toString())
                }
                else
                    throw Exception("Database could not be opened.")
            }
            TIMINGS -> {
                db = openHelper?.writableDatabase

//                if(db != null) {
//                    recordId = db.insert(TimingsContract.TABLE_NAME, null, values)
//                    if(recordId >= 0)
//                        returnUri = TimingsContract.buildTaskUri(recordId)
//                    else
//                        throw SQLException("Failed to insert into " + uri.toString())
//                }
//                else
//                    throw Exception("Database could not be opened.")
            }
            else -> throw IllegalStateException("Unknown uri: " + uri)
        }

        return returnUri
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)

        val db: SQLiteDatabase?
        var count: Int = 0
        var selectionCriteria: String

        when(match) {
            TASKS -> {
                db = openHelper?.writableDatabase

                if(db != null)
                    count = db.update(TasksContract.TABLE_NAME, values, selection, selectionArgs)
                else
                    throw Exception("Database could not be opened.")
            }

            TASKS_ID -> {
                db = openHelper?.writableDatabase
                val taskId =
                    TasksContract.getTaskId(uri)
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId

                if(selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }

                if(db != null)
                    count = db.update(TasksContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
                else
                    throw Exception("Database could not be opened.")
            }

//            TIMINGS -> {
//                db = openHelper?.writableDatabase
//
//                if(db != null)
//                    count = db.update(TimingsContract.TABLE_NAME, values, selection, selectionArgs)
//                else
//                    throw Exception("Database could not be opened.")
//            }

//            TASKS_ID -> {
//                db = openHelper?.writableDatabase
//                val timingsId = TimingsContract.getTaskId(uri)
//                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId
//
//                if(selection != null && selection.isNotEmpty()) {
//                    selectionCriteria += " AND ($selection)"
//                }
//
//                if(db != null)
//                    count = db.update(TimingsContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
//                else
//                    throw Exception("Database could not be opened.")
//            }

            else -> throw IllegalStateException("Unknown uri: " + uri)
        }

        return count
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)

        val db: SQLiteDatabase?
        var count: Int = 0
        var selectionCriteria: String

        when(match) {
            TASKS -> {
                db = openHelper?.writableDatabase

                if(db != null)
                    count = db.delete(TasksContract.TABLE_NAME, selection, selectionArgs)
                else
                    throw Exception("Database could not be opened.")
            }

            TASKS_ID -> {
                db = openHelper?.writableDatabase
                val taskId =
                    TasksContract.getTaskId(uri)
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId

                if(selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }

                if(db != null)
                    count = db.delete(TasksContract.TABLE_NAME, selection, selectionArgs)
                else
                    throw Exception("Database could not be opened.")
            }

//            TIMINGS -> {
//                db = openHelper?.writableDatabase
//
//                if(db != null)
//                    count = db.delete(TimingsContract.TABLE_NAME, selection, selectionArgs)
//                else
//                    throw Exception("Database could not be opened.")
//            }
//
//            TASKS_ID -> {
//                db = openHelper?.writableDatabase
//                val timingsId = TimingsContract.getTaskId(uri)
//                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId
//
//                if(selection != null && selection.isNotEmpty()) {
//                    selectionCriteria += " AND ($selection)"
//                }
//
//                if(db != null)
//                    count = db.delete(TimingsContract.TABLE_NAME, selectionCriteria, selectionArgs)
//                else
//                    throw Exception("Database could not be opened.")
//            }

            else -> throw IllegalStateException("Unknown uri: " + uri)
        }

        return count
    }

    override fun getType(uri: Uri): String? {
        return null;
    }

}