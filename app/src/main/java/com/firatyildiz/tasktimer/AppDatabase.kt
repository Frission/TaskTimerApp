package com.firatyildiz.tasktimer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.IllegalStateException

/**
*   Basic database class for the application
*
*   The only class that should use this is AppProvider.
 */
class AppDatabase private constructor (context: Context?) :
     SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val TAG = "AppDatabase"
        const val DATABASE_NAME = "TaskTimer.db"
        const val DATABASE_VERSION: Int = 2

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(instance != null)
                return instance
            else {
                synchronized(this) {
                    if (instance == null)
                        instance = AppDatabase(context)

                    return instance
                }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var sSQL = "CREATE TABLE " + TasksContract.TABLE_NAME + " ("
            .plus(TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, ")
            .plus(TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, ")
            .plus(TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, ")
            .plus(TasksContract.Columns.TASKS_SORTORDER + " INTEGER);")

        db?.execSQL(sSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: upgrading database")
        when(oldVersion) {
            1 -> print("upgraded")
            else -> throw IllegalStateException("onUpgrade() with unknown newVersion: $newVersion")
        }
    }
}