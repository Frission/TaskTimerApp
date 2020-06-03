package com.firatyildiz.tasktimer.model.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.firatyildiz.tasktimer.TimingContract
import com.firatyildiz.tasktimer.model.entities.Timing

@Dao
interface TimingDao {

    @Query("SELECT * FROM ${TimingContract.TABLE_NAME}")
    fun getAllTimings(): LiveData<List<Timing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiming(timing: Timing): Long?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTiming(timing: Timing): Int?

    @Delete
    suspend fun deleteTiming(timing: Timing): Int?
}