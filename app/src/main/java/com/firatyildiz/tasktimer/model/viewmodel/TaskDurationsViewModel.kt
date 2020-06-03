package com.firatyildiz.tasktimer.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.firatyildiz.tasktimer.model.AppRoomDatabase
import com.firatyildiz.tasktimer.model.repositories.ViewTaskDurationsRepository
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations

class TaskDurationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ViewTaskDurationsRepository
    var taskDurations: LiveData<List<ViewTaskDurations>>

    init {
        val vwTaskDurationsDao = AppRoomDatabase.getDatabase(application)?.viewTaskDurationsDao()!!
        repository = ViewTaskDurationsRepository(vwTaskDurationsDao)
        taskDurations = repository.allTaskDurations
    }
}