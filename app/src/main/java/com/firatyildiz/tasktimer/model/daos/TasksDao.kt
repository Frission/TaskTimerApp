package com.firatyildiz.tasktimer.model.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.firatyildiz.tasktimer.TasksContract
import com.firatyildiz.tasktimer.model.entities.Tasks

@Dao
interface TasksDao {
    @Query("SELECT * FROM ${TasksContract.TABLE_NAME}")
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
}
