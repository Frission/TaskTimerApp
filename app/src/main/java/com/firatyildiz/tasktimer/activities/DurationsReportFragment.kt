package com.firatyildiz.tasktimer.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.adapters.TaskDurationsRecyclerAdapter
import com.firatyildiz.tasktimer.model.repositories.ViewTaskDurationsRepository
import com.firatyildiz.tasktimer.model.viewmodel.TaskDurationsViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DurationsReportFragment : Fragment(), View.OnClickListener {

    companion object {
        private val TAG = "DurationsReportFragment"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var durationsViewModel: TaskDurationsViewModel
    private var adapter: TaskDurationsRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_durations, container, false)

        recyclerView = view.findViewById(R.id.td_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TaskDurationsRecyclerAdapter(requireContext())
        recyclerView.adapter = adapter

        durationsViewModel =
            ViewModelProvider(requireActivity()).get(TaskDurationsViewModel::class.java)
        durationsViewModel.taskDurations.observe(viewLifecycleOwner, Observer {
            adapter?.setTaskDurations(it)
        })

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.td_name_heading).setOnClickListener(this)
        view.findViewById<TextView?>(R.id.td_description_heading)?.setOnClickListener(this)
        view.findViewById<TextView>(R.id.td_start_heading).setOnClickListener(this)
        view.findViewById<TextView>(R.id.td_duration_heading).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        Log.d(TAG, "onClick: sort button clicked")
        when (view?.id) {
            R.id.td_name_heading -> durationsViewModel.sortOrder =
                ViewTaskDurationsRepository.DurationSortOrder.Name
            R.id.td_description_heading -> durationsViewModel.sortOrder =
                ViewTaskDurationsRepository.DurationSortOrder.Description
            R.id.td_start_heading -> durationsViewModel.sortOrder =
                ViewTaskDurationsRepository.DurationSortOrder.StartDate
            R.id.td_duration_heading -> durationsViewModel.sortOrder =
                ViewTaskDurationsRepository.DurationSortOrder.Duration
        }

        durationsViewModel.updateTaskDurations()
    }


}
