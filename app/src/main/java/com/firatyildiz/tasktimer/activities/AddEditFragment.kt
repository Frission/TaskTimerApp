package com.firatyildiz.tasktimer.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.model.AppRoomDatabase
import com.firatyildiz.tasktimer.model.entities.Tasks
import com.firatyildiz.tasktimer.model.repositories.TasksRepository
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AddEditFragment : Fragment() {
    private val TAG = "AddEditFragment"
    enum class FragmentEditMode { EDIT, ADD }

    private lateinit var mode: FragmentEditMode

    private lateinit var nameTextView: EditText
    private lateinit var descriptionTextView: EditText
    private lateinit var sortOrderTextView: EditText
    private lateinit var saveButton: Button

    private var task: Tasks? = null
    private var tasksRepository: TasksRepository? = null
    private var edited: Boolean = false

    interface OnFragmentCloseButtonClicked {
        fun onFragmentCloseButtonClicked()
    }

    var closeButtonListener: OnFragmentCloseButtonClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_addedit, container, false)

        tasksRepository =
            TasksRepository(AppRoomDatabase.getDatabase(requireContext())?.taskDao()!!)

        nameTextView = view?.findViewById(R.id.add_edit_name)!!
        descriptionTextView = view.findViewById(R.id.add_edit_description)!!
        sortOrderTextView = view.findViewById(R.id.add_edit_sortorder)!!
        saveButton = view.findViewById(R.id.add_edit_save)!!

        val arguments = arguments

        if (arguments != null) {
            task = arguments.getSerializable(Tasks::class.java.simpleName) as? Tasks

            if (task != null) {
                nameTextView.setText(task?.name)
                descriptionTextView.setText(task?.description)
                sortOrderTextView.setText(task?.sortOrder.toString())
                mode = FragmentEditMode.EDIT
            } else {
                mode = FragmentEditMode.ADD
            }
        } else {
            mode = FragmentEditMode.ADD
        }

        saveButton.isEnabled = nameTextView.text.isNotEmpty()
        saveButton.setOnClickListener(onSaveButtonClicked)

        nameTextView.addTextChangedListener {
            saveButton.isEnabled = nameTextView.text.isNotEmpty()
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: called")
        closeButtonListener = findListenerActivity()
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: called")
        closeButtonListener = null
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        super.onDetach()
    }

    private var onSaveButtonClicked = View.OnClickListener {
        // Update the database if at least one field has changed
        Log.d(TAG, "onSaveButtonClicked: fragment closing")
        val sortOrder: Int
        if (sortOrderTextView.text.isNullOrEmpty())
            sortOrder = 0
        else
            sortOrder = sortOrderTextView.text.toString().toInt()

        when (mode) {
            FragmentEditMode.EDIT -> {
                if (nameTextView.text.toString() != task?.name) {
                    task?.name = nameTextView.text.toString()
                    edited = true
                }
                if (descriptionTextView.text.toString() != task?.description) {
                    task?.description = descriptionTextView.text.toString()
                    edited = true
                }
                if (sortOrder != task?.sortOrder) {
                    task?.sortOrder = sortOrder
                    edited = true
                }

                if (edited) {
                    lifecycleScope.launch { tasksRepository?.update(task!!) }
                }
            }

            FragmentEditMode.ADD -> {
                lifecycleScope.launch {
                    tasksRepository?.insert(
                        Tasks(
                            nameTextView.text.toString(),
                            descriptionTextView.text.toString(),
                            sortOrder
                        )
                    )
                }
            }
        }

        if (closeButtonListener == null)
            closeButtonListener = findListenerActivity()

        closeButtonListener?.onFragmentCloseButtonClicked()
    }

    // might return null
    private fun findListenerActivity(): OnFragmentCloseButtonClicked? {
        return if (context is Activity && context is OnFragmentCloseButtonClicked)
            context as? OnFragmentCloseButtonClicked
        else
            null
    }

    fun canClose(): Boolean {
        return false
        //return !edited
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
}
