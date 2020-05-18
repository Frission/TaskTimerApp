package com.firatyildiz.tasktimer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firatyildiz.tasktimer.R
import kotlinx.android.synthetic.main.activity_add_edit.*

class AddEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

}
