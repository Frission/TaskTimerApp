package com.firatyildiz.tasktimer

import androidx.room.Database
import androidx.room.RoomDatabase

// database version is 2 because i forgot to make the _id auto generate
@Database(entities = arrayOf(Tasks::class), version = 2)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TasksDao
}