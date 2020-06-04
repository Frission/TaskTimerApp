package com.firatyildiz.tasktimer.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firatyildiz.tasktimer.model.TasksContract
import java.io.Serializable

@Entity
data class Tasks(
    @ColumnInfo(name = TasksContract.Columns.TASKS_NAME) var name: String,
    @ColumnInfo(name = TasksContract.Columns.TASKS_DESCRIPTION) var description: String?,
    @ColumnInfo(name = TasksContract.Columns.TASKS_SORTORDER) var sortOrder: Int?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0

    companion object {
        @JvmStatic
        private val serialVersionUID = 20200529L
    }
}