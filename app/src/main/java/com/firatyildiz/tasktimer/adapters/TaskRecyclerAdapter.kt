package com.firatyildiz.tasktimer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.model.entities.Tasks

class TaskRecyclerAdapter(
    var context: Context,
    var taskButtonsListener: OnTaskButtonClickListener
) :
    RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var tasks = emptyList<Tasks>()
    private var taskToDelete: Int = -1
    private var deleting = false

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = inflater.inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = tasks[position]
        holder.taskTitle.text = current.name
        holder.taskDescription.text = current.description

        holder.taskEditButton.setOnClickListener {
            taskButtonsListener.onEditTaskClicked(current)
        }

        holder.taskDeleteButton.setOnClickListener {
            taskToDelete = position
            deleting = true
            taskButtonsListener.onDeleteTaskClicked(current)
        }

        // animate
        holder.layout.animation = AnimationUtils.loadAnimation(context, R.anim.task_item_anim)
    }

    fun setTasks(tasks: List<Tasks>) {
        this.tasks = tasks

        // if an item was deleted
        if (deleting) {
            if (taskToDelete != -1)
                notifyItemRemoved(taskToDelete)
            taskToDelete = -1
            deleting = false
        } else {
            notifyDataSetChanged()
        }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: View = itemView.findViewById(R.id.taskItemLayout)
        val taskTitle: TextView = itemView.findViewById(R.id.taskItemTitle)
        val taskDescription: TextView = itemView.findViewById(R.id.taskItemDescription)
        val taskDeleteButton: ImageButton = itemView.findViewById(R.id.taskItemDeleteButton)
        val taskEditButton: ImageButton = itemView.findViewById(R.id.taskItemEditButton)
    }

    interface OnTaskButtonClickListener {
        fun onEditTaskClicked(task: Tasks)
        fun onDeleteTaskClicked(task: Tasks)
    }
}

