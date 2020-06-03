package com.firatyildiz.tasktimer.model.entities

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firatyildiz.tasktimer.TimingContract
import java.io.Serializable
import java.util.*

/*
 * Simple timing object
 * Sets its start time when created, and calculates how long since creation,
 * when setDuration is called.
 */
@Entity
data class Timing(@ColumnInfo(name = TimingContract.Columns.TIMING_TASK_ID) var taskID: Int) :
    Serializable {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
    @ColumnInfo(name = TimingContract.Columns.TIMING_START_TIME)
    var startTime: Long?
    @ColumnInfo(name = TimingContract.Columns.TIMING_DURATION)
    var duration: Long?

    init {
        val currentTime = Date()
        startTime = currentTime.time / 1000
        duration = 0
    }

    fun setDuration() {
        val currentTime = Date()
        duration = currentTime.time / 1000 - startTime!!
        Log.d(TAG, "setDuration: $taskID - Start Time: $startTime | Duration: $duration")
    }

    companion object {
        @JvmStatic
        private val serialVersionUID = 20200529L
        private val TAG = "Timing"
    }
}