package com.firatyildiz.tasktimer.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.firatyildiz.tasktimer.model.daos.TasksDao
import com.firatyildiz.tasktimer.model.entities.Tasks

// database version is 2 because i forgot to make the _id auto generate
@Database(entities = arrayOf(Tasks::class), version = 2)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TasksDao

    companion object {
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(AppRoomDatabase::class) {
                    if (INSTANCE == null)
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppRoomDatabase::class.java,
                            "TaskTimer.db"
                        ).build()
                }
            }

            return INSTANCE
        }

        fun closeDatabase() {
            INSTANCE = null
        }
    }
}