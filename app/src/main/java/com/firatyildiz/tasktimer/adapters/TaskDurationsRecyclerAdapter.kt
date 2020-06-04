package com.firatyildiz.tasktimer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.model.views.ViewTaskDurations
import java.util.*

class TaskDurationsRecyclerAdapter(var context: Context) :
    RecyclerView.Adapter<TaskDurationsRecyclerAdapter.ViewHolder>() {

    private var durations: List<ViewTaskDurations>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_durations_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        durations?.let {
            holder.name?.text = it[position].Name
            holder.description?.text = it[position].Description
            holder.startDate?.text = it[position].StartDate
            holder.duration?.text = formatDuration(it[position].Duration)
        }
    }

    override fun getItemCount(): Int {
        return durations?.size ?: 0
    }

    private fun formatDuration(duration: Long?): String {
        // duration is in seconds, convert to hh:mm:ss
        val dur: Long = duration!!
        val hours = dur / 3600
        val remainder = dur - (hours * 3600)
        val minutes = remainder / 60
        val seconds = remainder - (minutes * 60)

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun setTaskDurations(durations: List<ViewTaskDurations>?) {
        this.durations = durations
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var description: TextView? = null
        var startDate: TextView? = null
        var duration: TextView? = null

        init {
            name = itemView.findViewById(R.id.td_name)
            description = itemView.findViewById(R.id.td_description)
            startDate = itemView.findViewById(R.id.td_start)
            duration = itemView.findViewById(R.id.td_duration)
        }
    }
}