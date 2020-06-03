package com.firatyildiz.tasktimer.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.adapters.TaskRecyclerAdapter
import com.firatyildiz.tasktimer.model.AppRoomDatabase
import com.firatyildiz.tasktimer.model.entities.Tasks
import com.firatyildiz.tasktimer.model.entities.Timing
import com.firatyildiz.tasktimer.model.repositories.TimingRepository
import com.firatyildiz.tasktimer.model.viewmodel.TasksViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment(), TaskRecyclerAdapter.OnTaskButtonClickListener {
    private val TAG = "MainFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskViewModel: TasksViewModel
    private var adapter: TaskRecyclerAdapter? = null
    private var currentTiming: Timing? = null

    private lateinit var timingRepository: TimingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: create fragment")
        super.onCreate(savedInstanceState)
        retainInstance = true
        timingRepository =
            TimingRepository(AppRoomDatabase.getDatabase(requireContext())?.timingDao()!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity !is TaskRecyclerAdapter.OnTaskButtonClickListener)
            throw ClassCastException("${requireActivity()::class.simpleName} must implement TaskRecyclerAdapter.OnTaskButtonClickListener")

        setTimingText(currentTiming)
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

    override fun onTaskLongCLick(task: Tasks) {
//        if(activity is TaskRecyclerAdapter.OnTaskButtonClickListener)
//            (activity as? TaskRecyclerAdapter.OnTaskButtonClickListener)?.onTaskLongCLick(task)

        Toast.makeText(requireContext(), "Task ${task.name} clicked", Toast.LENGTH_SHORT).show()

        if (currentTiming != null) {
            if (task._id == currentTiming?.taskID) {
                // the current task was tapped a second time, stop timing
                currentTiming?.let {
                    saveTiming(it)
                    currentTiming = null
                    setTimingText(currentTiming)
                }
            } else {
                // a new task is being timed
                currentTiming?.let {
                    saveTiming(it)
                    currentTiming = Timing(task._id)
                    setTimingText(currentTiming)
                }
            }
        } else {
            // no task being timed, so start timing the new task
            currentTiming = Timing(task._id)
            setTimingText(currentTiming)
        }
    }

    private fun saveTiming(currentTiming: Timing) {
        Log.d(TAG, "saveTiming: saving timing")

        // If we have an open timing, set the duration and save
        currentTiming.setDuration()

        lifecycleScope.launch {
            timingRepository.update(currentTiming)
        }
    }

    private fun setTimingText(timing: Timing?) {
        val taskName = requireActivity().findViewById<TextView>(R.id.current_task)

        if (timing != null) {
            lifecycleScope.launch {
                taskName.text = "Timing " + taskViewModel.getTaskName(timing.taskID)
                Log.d(
                    TAG,
                    "setTimingText: task name is ${taskViewModel.getTaskName(timing.taskID)}"
                )
            }
        } else
            taskName.setText(R.string.no_task_message)
    }
}
