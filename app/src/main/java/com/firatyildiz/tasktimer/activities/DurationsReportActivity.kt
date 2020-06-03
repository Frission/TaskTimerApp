package com.firatyildiz.tasktimer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.model.viewmodel.TaskDurationsViewModel
import kotlinx.android.synthetic.main.activity_durations_report.*

class DurationsReportActivity : AppCompatActivity() {

    private lateinit var durationsViewModel: TaskDurationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_durations_report)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        durationsViewModel = ViewModelProvider(this).get(TaskDurationsViewModel::class.java)
    }

}
