package com.firatyildiz.tasktimer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.model.entities.Tasks
import kotlinx.android.synthetic.main.activity_add_edit.*

class AddEditActivity : AppCompatActivity(), AddEditFragment.OnFragmentCloseButtonClicked {

    private lateinit var fragment: AddEditFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fragment = AddEditFragment()

        val arguments: Bundle = Bundle()
        val task: Tasks? = intent.extras?.get(Tasks::class.java.simpleName) as Tasks?

        if (task != null) {
            arguments.putSerializable(Tasks::class.java.simpleName, task)
            fragment.arguments = arguments

            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_fragment_container, fragment)
            fragmentTransaction.setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fragment_fade_exit
            )
            fragmentTransaction.commit()
        }
    }

    override fun onFragmentCloseButtonClicked() {
        finish()
    }
}
