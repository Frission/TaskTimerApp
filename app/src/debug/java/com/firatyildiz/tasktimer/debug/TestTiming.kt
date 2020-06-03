package com.firatyildiz.tasktimer.debug

data class TestTiming(
    var taskId: Int,
    var startTime: Long,
    var duration: Long
) {
    init {
        startTime /= 1000
    }
}