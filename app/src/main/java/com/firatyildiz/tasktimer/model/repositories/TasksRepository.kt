package com.firatyildiz.tasktimer.model.repositories

import androidx.lifecycle.LiveData
import com.firatyildiz.tasktimer.model.daos.TasksDao
import com.firatyildiz.tasktimer.model.entities.Tasks

class TasksRepository(private val tasksDao: TasksDao) {

    val allTasks: LiveData<List<Tasks>> = tasksDao.getAllTasks()

    fun getTaskByName(name: String): LiveData<List<Tasks>> {
        return tasksDao.getTaskByName(name)
    }

    suspend fun insert(task: Tasks?): Long? {
        return task?.let { tasksDao.insertTask(it) }
    }

    suspend fun update(task: Tasks?): Int? {
        return task?.let { tasksDao.updateTask(it) }
    }

    suspend fun delete(task: Tasks?): Int? {
        return task?.let { tasksDao.deleteTask(it) }
    }

    suspend fun deleteByName(name: String): Int? {
        return tasksDao.deleteTaskByName(name)
    }

    suspend fun deleteById(taskId: Int): Int? {
        return tasksDao.deleteTaskById(taskId)
    }
}