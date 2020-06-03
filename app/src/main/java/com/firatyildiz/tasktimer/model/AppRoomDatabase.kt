package com.firatyildiz.tasktimer.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.firatyildiz.tasktimer.DurationsContract
import com.firatyildiz.tasktimer.TasksContract
import com.firatyildiz.tasktimer.TimingContract
import com.firatyildiz.tasktimer.model.daos.TasksDao
import com.firatyildiz.tasktimer.model.daos.TimingDao
import com.firatyildiz.tasktimer.model.daos.ViewTaskDurationsDao
import com.firatyildiz.tasktimer.model.entities.Tasks
import com.firatyildiz.tasktimer.model.entities.Timing
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations

@Database(
    entities = [Tasks::class, Timing::class],
    views = [ViewTaskDurations::class],
    version = 4
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TasksDao
    abstract fun timingDao(): TimingDao
    abstract fun viewTaskDurationsDao(): ViewTaskDurationsDao

    companion object {
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(AppRoomDatabase::class) {
                    if (INSTANCE == null)
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppRoomDatabase::class.java,
                            "TaskTimer.db"
                        ).addMigrations(object : Migration(1, 2) {
                            override fun migrate(database: SupportSQLiteDatabase) {
                                // do nothing, because i've only made changes in the code, not the database
                            }

                        })
                            .addMigrations(object : Migration(2, 3) {
                                override fun migrate(database: SupportSQLiteDatabase) {
                                    database.execSQL(
                                        "CREATE TABLE " + TimingContract.TABLE_NAME + " ("
                                            .plus(TimingContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, ")
                                            .plus(TimingContract.Columns.TIMING_TASK_ID + " INTEGER NOT NULL, ")
                                            .plus(TimingContract.Columns.TIMING_START_TIME + " INTEGER, ")
                                            .plus(TimingContract.Columns.TIMING_DURATION + " INTEGER);")
                                    )

                                    database.execSQL(
                                        "CREATE TRIGGER Remove_Task AFTER DELETE ON " + TasksContract.TABLE_NAME
                                            .plus(" FOR EACH ROW")
                                            .plus(" BEGIN")
                                            .plus(" DELETE FROM " + TimingContract.TABLE_NAME)
                                            .plus(" WHERE " + TimingContract.Columns.TIMING_TASK_ID + " == OLD." + TasksContract.Columns._ID + ";")
                                            .plus(" END;")
                                    )
                                }
                            })
                            .addMigrations(object : Migration(3, 4) {
                                override fun migrate(database: SupportSQLiteDatabase) {
                                    // what is this character??? -> `
                                    database.execSQL(
                                        "CREATE VIEW `${DurationsContract.TABLE_NAME}` AS SELECT ${TimingContract.TABLE_NAME}.${TimingContract.Columns._ID}, " +
                                                "${TasksContract.TABLE_NAME}.${TasksContract.Columns.TASKS_NAME}, " +
                                                "${TasksContract.TABLE_NAME}.${TasksContract.Columns.TASKS_DESCRIPTION}, " +
                                                "${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_START_TIME}, " +
                                                "DATE(${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_START_TIME}, 'unixepoch') AS ${DurationsContract.Columns.DURATIONS_START_DATE}, " +
                                                "SUM(${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_DURATION}) AS ${DurationsContract.Columns.DURATIONS_DURATION} " +
                                                "FROM ${TasksContract.TABLE_NAME} INNER JOIN ${TimingContract.TABLE_NAME} " +
                                                "ON ${TasksContract.TABLE_NAME}.${TasksContract.Columns._ID} == ${TimingContract.TABLE_NAME}.${TimingContract.Columns.TIMING_TASK_ID} " +
                                                "GROUP BY ${TasksContract.TABLE_NAME}.${TasksContract.Columns._ID}, ${DurationsContract.Columns.DURATIONS_START_DATE};"
                                    )
                                }

                            })
                            .build()
                }
            }

            return INSTANCE
        }

        fun closeDatabase() {
            INSTANCE = null
        }
    }
}