package com.firatyildiz.tasktimer.debug

import com.firatyildiz.tasktimer.model.daos.TasksDao
import com.firatyildiz.tasktimer.model.daos.TimingDao
import com.firatyildiz.tasktimer.model.entities.Timing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import java.util.*
import kotlin.math.roundToInt


class TestData {
    companion object {
        @TestOnly
        fun generateTestData(
            timingDao: TimingDao,
            tasksDao: TasksDao,
            coroutineScope: CoroutineScope
        ) {
            val SECS_IN_DAY = 864
            val LOWER_BOUND = 50
            val UPPER_BOUND = 250
            val MAX_DURATION = SECS_IN_DAY / 6

            // get a list of task ID's from the database
            coroutineScope.launch {
                val taskIds = tasksDao.getTaskIds()

                if (taskIds != null) {
                    for (taskId in taskIds) {
                        // generate between 10 and 50 random timings for this task
                        var loopCount = LOWER_BOUND + getRandomInt(UPPER_BOUND - LOWER_BOUND)

                        for (i in LOWER_BOUND until UPPER_BOUND) {
                            val randomDate = randomDateTime()

                            // generate a random duration between 0 and 4 hours
                            val duration = getRandomInt(MAX_DURATION).toLong()

                            // create a new TestTiming object
                            val testTiming = TestTiming(taskId, randomDate, duration)

                            // add it to the database
                            saveCurrentTiming(timingDao, testTiming, coroutineScope)
                        }
                    }
                }
            }
        }

        @TestOnly
        private fun getRandomInt(max: Int): Int {
            return (Math.random() * max).roundToInt()
        }

        @TestOnly
        private fun randomDateTime(): Long {
            // Set the range of years, change as necessary
            val startYear = 2019
            val endYear = 2020

            val sec = getRandomInt(59)
            val min = getRandomInt(59)
            val hour = getRandomInt(23)
            val month = getRandomInt(11)
            val year = startYear + getRandomInt(endYear - startYear)

            val gc = GregorianCalendar(year, month, 1)
            val day = 1 + getRandomInt(gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) - 1)

            gc.set(year, month, day, hour, min, sec)

            return gc.timeInMillis
        }

        @TestOnly
        private fun saveCurrentTiming(
            timingDao: TimingDao,
            currentTiming: TestTiming,
            coroutineScope: CoroutineScope
        ) {
            // save the timing record
            val timing = Timing(currentTiming.taskId)
            timing.startTime = currentTiming.startTime
            timing.duration = currentTiming.duration

            coroutineScope.launch {
                timingDao.insertTiming(timing)
            }
        }
    }
}