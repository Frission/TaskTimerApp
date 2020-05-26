package com.firatyildiz.tasktimer.model.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.firatyildiz.tasktimer.TasksContract
import com.firatyildiz.tasktimer.model.entities.Tasks

@Dao
interface TasksDao {
    @Query("SELECT * FROM ${TasksContract.TABLE_NAME} ORDER BY ${TasksContract.Columns.TASKS_SORTORDER} , ${TasksContract.Columns.TASKS_NAME} COLLATE NOCASE")
    fun getAllTasks(): LiveData<List<Tasks>>

    @Query("SELECT * FROM ${TasksContract.TABLE_NAME} WHERE ${TasksContract.Columns.TASKS_NAME} == (:name)")
    fun getTaskByName(name: String): LiveData<List<Tasks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Tasks): Long?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: Tasks): Int?

    @Delete
    suspend fun deleteTask(task: Tasks): Int?

    @Query("DELETE FROM ${TasksContract.TABLE_NAME} WHERE ${TasksContract.Columns.TASKS_NAME} == (:name)")
    suspend fun deleteTaskByName(name: String): Int?

    @Query("DELETE FROM ${TasksContract.TABLE_NAME} WHERE ${TasksContract.Columns._ID} == (:taskId)")
    suspend fun deleteTaskById(taskId: Int): Int?
}
