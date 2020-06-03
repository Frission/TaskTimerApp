package com.firatyildiz.tasktimer.model.repositories

import androidx.lifecycle.LiveData
import com.firatyildiz.tasktimer.model.daos.TasksDao
import com.firatyildiz.tasktimer.model.entities.Tasks

class TasksRepository(private val tasksDao: TasksDao) {

    val allTasks: LiveData<List<Tasks>> = tasksDao.getAllTasks()

    fun getTaskByName(name: String): LiveData<List<Tasks>> {
        return tasksDao.getTaskByName(name)
    }

    suspend fun insert(task: Tasks): Long? {
        return tasksDao.insertTask(task)
    }

    suspend fun update(task: Tasks): Int? {
        return tasksDao.updateTask(task)
    }

    suspend fun delete(task: Tasks): Int? {
        return tasksDao.deleteTask(task)
    }

    suspend fun deleteByName(name: String): Int? {
        return tasksDao.deleteTaskByName(name)
    }

    suspend fun deleteById(taskId: Int): Int? {
        return tasksDao.deleteTaskById(taskId)
    }

    suspend fun getTaskName(taskId: Int): String {
        return tasksDao.getTaskName(taskId)
    }
}