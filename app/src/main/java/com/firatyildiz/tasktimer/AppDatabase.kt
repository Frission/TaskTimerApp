package com.firatyildiz.tasktimer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 *   Basic database class for the application
 *
 *   The only class that should use this is AppProvider.
 *   This was implemented as part of the course I'm following, but I'll be using the Room database
 *    for db access inside the app.
 */
class AppDatabase private constructor(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    companion object {
        const val TAG = "AppDatabase"
        const val DATABASE_NAME = "TaskTimer.db"
        const val DATABASE_VERSION: Int = 3

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance != null)
                return instance
            else {
                synchronized(this) {
                    if (instance == null)
                        instance =
                            AppDatabase(context)

                    return instance
                }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val sSQL = "CREATE TABLE " + TasksContract.TABLE_NAME + " ("
            .plus(TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, ")
            .plus(TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, ")
            .plus(TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, ")
            .plus(TasksContract.Columns.TASKS_SORTORDER + " INTEGER);")

        db?.execSQL(sSQL)

        addTimingsTable(db)
        addDurationsView(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: upgrading database")
        when (oldVersion) {
            1 -> {
                addTimingsTable(db)
                addDurationsView(db)
            }
            2 -> {
                addTimingsTable(db)
                addDurationsView(db)
            }
            3 -> {
                addDurationsView(db)
            }
            else -> throw IllegalStateException("onUpgrade() with unknown newVersion: $newVersion")
        }
    }

    private fun addTimingsTable(db: SQLiteDatabase?) {
        var sSQL = "CREATE TABLE " + TimingContract.TABLE_NAME + " ("
            .plus(TimingContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, ")
            .plus(TimingContract.Columns.TIMING_TASK_ID + " INTEGER NOT NULL, ")
            .plus(TimingContract.Columns.TIMING_START_TIME + " INTEGER, ")
            .plus(TimingContract.Columns.TIMING_DURATION + " INTEGER);")

        db?.execSQL(sSQL)

        sSQL = "CREATE TRIGGER Remove_Task AFTER DELETE ON " + TasksContract.TABLE_NAME
            .plus(" FOR EACH ROW")
            .plus(" BEGIN")
            .plus(" DELETE FROM " + TimingContract.TABLE_NAME)
            .plus(" WHERE " + TimingContract.Columns.TIMING_TASK_ID + " == OLD." + TasksContract.Columns._ID + ";")
            .plus(" END;")

        db?.execSQL(sSQL)
    }

    private fun addDurationsView(db: SQLiteDatabase?) {
        val sSQL =
            "CREATE VIEW `${DurationsContract.TABLE_NAME}` AS SELECT ${TimingContract.TABLE_NAME}.${TimingContract.Columns._ID}, " +
                    "${TasksContract.TABLE_NAME}.${TasksContract.Columns.TASKS_NAME}, " +
                    "${TasksContract.TABLE_NAME}.${TasksContract.Columns.TASKS_DESCRIPTION}, " +
                    "${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_START_TIME}, " +
                    "DATE(${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_START_TIME}, 'unixepoch') AS ${DurationsContract.Columns.DURATIONS_START_DATE}, " +
                    "SUM(${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_DURATION}) AS ${DurationsContract.Columns.DURATIONS_DURATION} " +
                    "FROM ${TasksContract.TABLE_NAME} INNER JOIN ${TimingContract.TABLE_NAME} " +
                    "ON ${TasksContract.TABLE_NAME}.${TasksContract.Columns._ID} == ${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_TASK_ID} " +
                    "GROUP BY ${TasksContract.TABLE_NAME}.${TasksContract.Columns._ID}, ${DurationsContract.Columns.DURATIONS_START_DATE};"

        db?.execSQL(sSQL)
    }
}