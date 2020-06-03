package com.firatyildiz.tasktimer.model.repositories

import androidx.lifecycle.LiveData
import com.firatyildiz.tasktimer.model.daos.ViewTaskDurationsDao
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations

class ViewTaskDurationsRepository(var viewTaskDurationsDao: ViewTaskDurationsDao) {

    var allTaskDurations: LiveData<List<ViewTaskDurations>>

    init {
        allTaskDurations = viewTaskDurationsDao.getAllTaskDurations()
    }
}