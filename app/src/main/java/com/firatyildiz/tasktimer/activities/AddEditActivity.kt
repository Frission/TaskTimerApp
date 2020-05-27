package com.firatyildiz.tasktimer.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.firatyildiz.tasktimer.AppDialog
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.model.entities.Tasks
import kotlinx.android.synthetic.main.activity_add_edit.*

class AddEditActivity : AppCompatActivity(), AddEditFragment.OnFragmentCloseButtonClicked,
    AppDialog.DialogEvents {
    private val TAG = "AddEditActivity"

    companion object {
        private val DIALOG_ID_CANCEL_EDIT: Int = 1
    }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "onOptionsItemSelected: home button pressed")
                val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)

                if (fragment is AddEditFragment && fragment.canClose()) {
                    return super.onOptionsItemSelected(item)
                } else {
                    showConfirmationDialog()
                    return true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    fun showConfirmationDialog() {
        val dialog = AppDialog()
        val args = Bundle()

        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT)
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDiag_message))
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDiag_positive_caption)
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDiag_negative_caption)

        dialog.arguments = args
        dialog.show(supportFragmentManager, null)
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: back button has been pressed")
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)

        if (fragment is AddEditFragment && fragment.canClose())
            super.onBackPressed()
        else {
            val dialog = AppDialog()
            val args = Bundle()

            args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT)
            args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDiag_message))
            args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDiag_positive_caption)
            args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDiag_negative_caption)

            dialog.arguments = args
            dialog.show(supportFragmentManager, null)
        }
    }

    override fun onFragmentCloseButtonClicked() {
        finish()
    }

    override fun onPositiveDialogResult(dialogId: Int, args: Bundle?) {

    }

    override fun onNegativeDialogResult(dialogId: Int, args: Bundle?) {
        finish()
    }

    override fun onDialogCancelled(dialogId: Int) {

    }
}
