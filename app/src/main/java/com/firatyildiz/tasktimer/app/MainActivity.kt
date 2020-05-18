package com.firatyildiz.tasktimer.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.activities.AddEditActivity
import com.firatyildiz.tasktimer.model.entities.Tasks
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val ADD_EDIT_FRAGMENT = "AddEditFragment"
    }

    private val TAG = "MainActivity"

    // if we have enough space to show two fragments in one pane
    private var twoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
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
            R.id.menuMain_addTask -> {
                taskEditRequest(null)
                true
            }
            R.id.menuMain_showDurations -> true
            R.id.menuMain_settings -> true
            R.id.menuMain_about -> true
            R.id.menuMain_generate -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun taskEditRequest(task: Tasks?) {
        if (twoPane) {

        } else {
            var detailIntent = Intent(this, AddEditActivity::class.java)
            if (task != null) {
                detailIntent.putExtra(Tasks::class.java.simpleName, task)
                startActivity(detailIntent)
            } else {
                startActivity(detailIntent)
            }
        }
    }
}

//    private val appDatabase: AppDatabase? =
//        AppDatabase.getInstance(this)
//
//    private lateinit var sqliteDatabase: SQLiteDatabase

//    fun testRoomDatabase() {
//        val tasks = roomDatabase.taskDao().getAllTasks()
//
//        tasks.observe(this, Observer {
//            for (tasks: Tasks in it) {
//                Log.d(TAG, "testRoomDatabase: " + tasks.name + " | " + tasks.description)
//            }
//        })
//    }

//        roomDatabase =
//            Room.databaseBuilder(applicationContext, AppRoomDatabase::class.java, "TaskTimer.db")
//                .build()
//        sqliteDatabase = appDatabase?.readableDatabase!!
//
//        val projection: Array<String> =
//            arrayOf(
//                TasksContract.Columns._ID,
//                TasksContract.Columns.TASKS_NAME,
//                TasksContract.Columns.TASKS_DESCRIPTION,
//                TasksContract.Columns.TASKS_SORTORDER
//            )
//
//        val contentResolver: ContentResolver = getContentResolver()
//
//        val cursor: Cursor? = contentResolver.query(
//            TasksContract.CONTENT_URI,
//            projection,
//            null,
//            null,
//            TasksContract.Columns.TASKS_NAME
//        )
//
//        if (cursor != null) {
//            with(cursor) {
//                while (moveToNext()) {
//                    for (i in 0 until columnCount) {
//                        Log.d(TAG, "onCreate: " + columnNames[i] + ": " + getString(i))
//                    }
//
//                    Log.d(TAG, "onCreate: =================")
//                }
//                close()
//            }
//        }
