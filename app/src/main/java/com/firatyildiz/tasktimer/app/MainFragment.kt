package com.firatyildiz.tasktimer.app

import android.os.Bundle
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
import com.firatyildiz.tasktimer.model.viewmodel.TasksViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    private val TAG = "MainFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskViewModel: TasksViewModel
    private lateinit var adapter: TaskRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = view.findViewById(R.id.taskRecyclerView)
        adapter = TaskRecyclerAdapter(
            requireContext(),
            activity as TaskRecyclerAdapter.OnTaskButtonClickListener
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel = ViewModelProvider(requireActivity()).get(TasksViewModel::class.java)
        taskViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let { adapter.setTasks(it) }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}
