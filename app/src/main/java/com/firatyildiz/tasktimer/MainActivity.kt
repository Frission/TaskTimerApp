package com.firatyildiz.tasktimer

import android.content.ContentResolver
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val appDatabase: AppDatabase? = AppDatabase.getInstance(this)
    private lateinit var sqliteDatabase: SQLiteDatabase
    private lateinit var roomDatabase: AppRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        roomDatabase =
            Room.databaseBuilder(applicationContext, AppRoomDatabase::class.java, "TaskTimer.db")
                .build()
        sqliteDatabase = appDatabase?.readableDatabase!!

        var projection: Array<String> =
            arrayOf(TasksContract.Columns.TASKS_NAME, TasksContract.Columns.TASKS_DESCRIPTION)
        var contentResolver: ContentResolver = getContentResolver()
        var cursor: Cursor? = contentResolver.query(
            TasksContract.CONTENT_URI,
            projection,
            null,
            null,
            TasksContract.Columns.TASKS_NAME
        )

        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    for (i in 0 until columnCount) {
                        Log.d(TAG, "onCreate: " + columnNames[i] + ": " + getString(i))
                    }

                    Log.d(TAG, "onCreate: =================")
                }
                close()
            }
        }

        testRoomDatabase()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun testRoomDatabase() {
        val tasks = roomDatabase.taskDao().getAllTasks()

        tasks.observe(this, Observer {
            for (tasks: Tasks in it) {
                Log.d(TAG, "testRoomDatabase: " + tasks.name + " | " + tasks.description)
            }
        })
    }
}
