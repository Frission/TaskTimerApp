package com.firatyildiz.tasktimer.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.firatyildiz.tasktimer.model.entities.Tasks
import com.firatyildiz.tasktimer.model.repositories.TasksRepository
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TasksRepository
    val tasks: LiveData<List<Tasks>>

    init {
        val taskDao = AppRoomDatabase.getDatabase(application)?.taskDao()!!
        repository =
            TasksRepository(taskDao)
        tasks = repository.allTasks
    }

    fun insert(task: Tasks) = viewModelScope.launch {
        repository.insert(task)
    }
}