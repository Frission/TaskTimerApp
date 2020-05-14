package com.firatyildiz.tasktimer

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Tasks (
    @ColumnInfo(name = TasksContract.Columns.TASKS_NAME) var name: String,
    @ColumnInfo(name = TasksContract.Columns.TASKS_DESCRIPTION) var description: String?,
    @ColumnInfo(name = TasksContract.Columns.TASKS_SORTORDER) var sortOrder: Int?
) {
    @PrimaryKey(autoGenerate = true) var _id: Int = 0
}

@Dao
interface TasksDao {
    @Query("SELECT * FROM ${TasksContract.TABLE_NAME}")
    fun getAllTasks(): LiveData<List<Tasks>>

    @Query("SELECT * FROM ${TasksContract.TABLE_NAME} WHERE ${TasksContract.Columns.TASKS_NAME} == (:name)")
    fun getTaskByName(name: String): LiveData<List<Tasks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Tasks): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(vararg task: Tasks): List<Long>?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: Tasks): Int?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTasks(vararg tasks: Tasks): Int?

    @Delete
    suspend fun deleteTask(task: Tasks): Int?

    @Delete
    suspend fun deleteTasks(vararg tasks: Tasks): Int?

    @Query("DELETE FROM ${TasksContract.TABLE_NAME} WHERE ${TasksContract.Columns.TASKS_NAME} == (:name)")
    suspend fun deleteTaskByName(name: String): Int?
}
