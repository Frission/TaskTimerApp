package com.firatyildiz.tasktimer.model.repositories

import androidx.lifecycle.LiveData
import com.firatyildiz.tasktimer.model.daos.TimingDao
import com.firatyildiz.tasktimer.model.entities.Timing

class TimingRepository(private val timingDao: TimingDao) {

    val allTimings: LiveData<List<Timing>> = timingDao.getAllTimings()

    suspend fun insert(timing: Timing): Long? {
        return timingDao.insertTiming(timing)
    }

    suspend fun update(timing: Timing): Int? {
        return timingDao.updateTiming(timing)
    }
}