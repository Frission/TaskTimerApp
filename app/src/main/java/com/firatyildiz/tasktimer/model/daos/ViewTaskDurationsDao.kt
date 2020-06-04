package com.firatyildiz.tasktimer.model.daos

import androidx.room.Dao
import androidx.room.Query
import com.firatyildiz.tasktimer.model.DurationsContract
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations

@Dao
interface ViewTaskDurationsDao {

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME}")
    suspend fun getAllTaskDurations(): List<ViewTaskDurations>

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME} ORDER BY ${DurationsContract.Columns.DURATIONS_NAME}")
    suspend fun getAllTaskDurationsSortedByName(): List<ViewTaskDurations>

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME} ORDER BY ${DurationsContract.Columns.DURATIONS_DESCRIPTION}")
    suspend fun getAllTaskDurationsSortedByDescription(): List<ViewTaskDurations>

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME} ORDER BY ${DurationsContract.Columns.DURATIONS_START_DATE}")
    suspend fun getAllTaskDurationsSortedByStartDate(): List<ViewTaskDurations>

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME} ORDER BY ${DurationsContract.Columns.DURATIONS_DURATION}")
    suspend fun getAllTaskDurationsSortedByDuration(): List<ViewTaskDurations>

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME} WHERE ${DurationsContract.Columns.DURATIONS_START_DATE} = (:date)")
    suspend fun getAllTaskDurationsAtDate(date: String): List<ViewTaskDurations>

    @Query("SELECT * FROM ${DurationsContract.TABLE_NAME} WHERE ${DurationsContract.Columns.DURATIONS_START_DATE} BETWEEN (:startDate) AND (:endDate)")
    suspend fun getAllTaskDurationsBetweenDates(
        startDate: String,
        endDate: String
    ): List<ViewTaskDurations>
}