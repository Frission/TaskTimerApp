package com.firatyildiz.tasktimer.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

    enum class FragmentEditMode { EDIT, ADD }

    private lateinit var mode: FragmentEditMode

    private lateinit var nameTextView: EditText
    private lateinit var descriptionTextView: EditText
    private lateinit var sortOrderTextView: EditText
    private lateinit var saveButton: Button

    private var task: Tasks? = null
    private var tasksRepository: TasksRepository? = null

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

        var arguments = activity?.intent?.extras

        if (arguments != null) {
            task = arguments.getSerializable(Tasks::class.java.simpleName) as Tasks

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

    private var onSaveButtonClicked = View.OnClickListener {
        // Update the database if at least one field has changed

        val sortOrder: Int
        if (sortOrderTextView.text.isNullOrEmpty())
            sortOrder = 0
        else
            sortOrder = sortOrderTextView.text.toString().toInt()

        var edited = false
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
                    lifecycleScope.launch { tasksRepository?.update(task) }
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
