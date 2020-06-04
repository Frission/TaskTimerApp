package com.firatyildiz.tasktimer.model.repositories

import androidx.lifecycle.MutableLiveData
import com.firatyildiz.tasktimer.model.daos.ViewTaskDurationsDao
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ViewTaskDurationsRepository(
    var viewTaskDurationsDao: ViewTaskDurationsDao,
    val viewModelScope: CoroutineScope
) {

    enum class DurationSortOrder {
        Name, Description, StartDate, Duration, None
    }

    var allTaskDurations: MutableLiveData<List<ViewTaskDurations>> = MutableLiveData()

    init {
        viewModelScope.launch {
            allTaskDurations.value = viewTaskDurationsDao.getAllTaskDurations()
        }
    }

    fun updateTaskDurations() = viewModelScope.launch {
        allTaskDurations.value = viewTaskDurationsDao.getAllTaskDurations()
    }

    suspend fun filterAllTaskDurationsWithDate(date: String) = viewModelScope.launch {
        allTaskDurations.value = viewTaskDurationsDao.getAllTaskDurationsAtDate(date)
    }

    suspend fun filterAllTaskDurationsBetweenDates(startDate: String, endDate: String) =
        viewModelScope.launch {
            allTaskDurations.value =
                viewTaskDurationsDao.getAllTaskDurationsBetweenDates(startDate, endDate)
        }

    fun updateTaskDurationsSorted(sortOrder: DurationSortOrder) = viewModelScope.launch {
        when (sortOrder) {
            DurationSortOrder.None -> allTaskDurations.value =
                viewTaskDurationsDao.getAllTaskDurations()
            DurationSortOrder.Name -> allTaskDurations.value =
                viewTaskDurationsDao.getAllTaskDurationsSortedByName()
            DurationSortOrder.Description -> allTaskDurations.value =
                viewTaskDurationsDao.getAllTaskDurationsSortedByDescription()
            DurationSortOrder.StartDate -> allTaskDurations.value =
                viewTaskDurationsDao.getAllTaskDurationsSortedByStartDate()
            DurationSortOrder.Duration -> allTaskDurations.value =
                viewTaskDurationsDao.getAllTaskDurationsSortedByDuration()
        }
    }
}