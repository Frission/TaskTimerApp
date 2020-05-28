package com.firatyildiz.tasktimer.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.adapters.TaskRecyclerAdapter
import com.firatyildiz.tasktimer.model.entities.Tasks
import com.firatyildiz.tasktimer.model.viewmodel.TasksViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment(), TaskRecyclerAdapter.OnTaskButtonClickListener {
    private val TAG = "MainFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskViewModel: TasksViewModel
    private var adapter: TaskRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: create fragment")
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity !is TaskRecyclerAdapter.OnTaskButtonClickListener)
            throw ClassCastException("${requireActivity()::class.simpleName} must implement TaskRecyclerAdapter.OnTaskButtonClickListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: creating main fragment view")
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = view.findViewById(R.id.taskRecyclerView)

        // if adapter is null it means the fragment is being created for the first time
        if (adapter == null) {
            adapter = TaskRecyclerAdapter(
                requireContext(),
                this as TaskRecyclerAdapter.OnTaskButtonClickListener
            )
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel = ViewModelProvider(requireActivity()).get(TasksViewModel::class.java)
        taskViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let { t -> adapter?.setTasks(t) }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onEditTaskClicked(task: Tasks) {
        if (activity is TaskRecyclerAdapter.OnTaskButtonClickListener)
            (activity as? TaskRecyclerAdapter.OnTaskButtonClickListener)?.onEditTaskClicked(task)
    }

    override fun onDeleteTaskClicked(task: Tasks) {
        if (activity is TaskRecyclerAdapter.OnTaskButtonClickListener)
            (activity as? TaskRecyclerAdapter.OnTaskButtonClickListener)?.onDeleteTaskClicked(task)
    }


}
