package com.firatyildiz.tasktimer.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.firatyildiz.tasktimer.model.AppRoomDatabase
import com.firatyildiz.tasktimer.model.repositories.ViewTaskDurationsRepository
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations
import kotlinx.coroutines.launch

class TaskDurationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ViewTaskDurationsRepository
    var taskDurations: LiveData<List<ViewTaskDurations>>

    var sortOrder = ViewTaskDurationsRepository.DurationSortOrder.None

    init {
        val vwTaskDurationsDao = AppRoomDatabase.getDatabase(application)?.viewTaskDurationsDao()!!
        repository = ViewTaskDurationsRepository(vwTaskDurationsDao, viewModelScope)
        taskDurations = repository.allTaskDurations
    }

    fun showTaskDurationsAtDate(date: String) = viewModelScope.launch {
        repository.filterAllTaskDurationsWithDate(date)
    }

    fun showTaskDurationsBetweenDates(startDate: String, endDate: String) = viewModelScope.launch {
        repository.filterAllTaskDurationsBetweenDates(startDate, endDate)
    }

    fun updateTaskDurations() {
        if (this.sortOrder == ViewTaskDurationsRepository.DurationSortOrder.None)
            repository.updateTaskDurations()
        else
            repository.updateTaskDurationsSorted(sortOrder)
    }
}