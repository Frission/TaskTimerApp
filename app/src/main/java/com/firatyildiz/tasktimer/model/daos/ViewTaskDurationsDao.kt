package com.firatyildiz.tasktimer.model.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.firatyildiz.tasktimer.DurationsContract
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations

@Dao
interface ViewTaskDurationsDao {

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME}")
    fun getAllTaskDurations(): LiveData<List<ViewTaskDurations>>
}