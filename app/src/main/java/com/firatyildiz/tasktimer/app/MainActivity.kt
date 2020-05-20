package com.firatyildiz.tasktimer.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.activities.AddEditActivity
import com.firatyildiz.tasktimer.activities.AddEditFragment
import com.firatyildiz.tasktimer.adapters.TaskRecyclerAdapter
import com.firatyildiz.tasktimer.model.entities.Tasks
import com.firatyildiz.tasktimer.model.viewmodel.TasksViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TaskRecyclerAdapter.OnTaskButtonClickListener,
    AddEditFragment.OnFragmentCloseButtonClicked {

    companion object {
        private val ADD_EDIT_FRAGMENT = "AddEditFragment"
    }

    private val TAG = "MainActivity"
    private lateinit var taskViewModel: TasksViewModel
    private lateinit var fragment: AddEditFragment

    // if we have enough space to show two fragments in one pane
    private var twoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        taskViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)

        if (findViewById<View>(R.id.task_details_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // res/values-land and res/values-sw600dp
            // If this view is present, then the app should be in two pane mode
            twoPane = true
        }
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
            fragment = AddEditFragment()

            val arguments: Bundle = Bundle()
            arguments.putSerializable(Tasks::class.java.simpleName, task)
            fragment.arguments = arguments

            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.task_details_container, fragment)
            fragmentTransaction.setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fragment_fade_exit
            )
            fragmentTransaction.commit()
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

    override fun onEditTaskClicked(task: Tasks) {
        taskEditRequest(task)
    }

    override fun onDeleteTaskClicked(task: Tasks) {
        taskViewModel.delete(task)
    }

    override fun onFragmentCloseButtonClicked() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.setCustomAnimations(
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit
        )
        fragmentTransaction.commit()
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
