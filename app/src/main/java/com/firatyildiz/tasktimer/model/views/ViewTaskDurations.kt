package com.firatyildiz.tasktimer.model.views

import androidx.room.DatabaseView
import com.firatyildiz.tasktimer.model.DurationsContract
import com.firatyildiz.tasktimer.model.TasksContract
import com.firatyildiz.tasktimer.model.TimingContract

@DatabaseView(
    "SELECT ${TimingContract.TABLE_NAME}.${TimingContract.Columns._ID}, " +
            "${TasksContract.TABLE_NAME}.${TasksContract.Columns.TASKS_NAME}, " +
            "${TasksContract.TABLE_NAME}.${TasksContract.Columns.TASKS_DESCRIPTION}, " +
            "${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_START_TIME}, " +
            "DATE(${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_START_TIME}, 'unixepoch') AS ${DurationsContract.Columns.DURATIONS_START_DATE}, " +
            "SUM(${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_DURATION}) AS ${DurationsContract.Columns.DURATIONS_DURATION} " +
            "FROM ${TasksContract.TABLE_NAME} INNER JOIN ${TimingContract.TABLE_NAME} " +
            "ON ${TasksContract.TABLE_NAME}.${TasksContract.Columns._ID} == ${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_TASK_ID} " +
            "GROUP BY ${TasksContract.TABLE_NAME}.${TasksContract.Columns._ID}, ${DurationsContract.Columns.DURATIONS_START_DATE}"
)
data class ViewTaskDurations(
    val _id: Int,
    val Name: String,
    val Description: String?,
    val StartTime: Long?,
    val StartDate: String?,
    val Duration: Long?
)