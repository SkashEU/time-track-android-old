package com.skash.timetrack.feature.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.Task
import com.skash.timetrack.databinding.ListItemTaskBinding

class TaskListAdapter : ListAdapter<Task, TaskViewHolder>(TaskDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TaskViewHolder(
    private val binding: ListItemTaskBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(task: Task) {
        binding.taskDescription.text = task.description
        binding.colorDotImageView.isVisible = task.project != null

        task.project?.let { project ->
            binding.projectTitleTextView.text = project.title
            binding.colorDotImageView.setColorFilter(Color.parseColor(project.color))
        }
    }
}

class TaskDiffUtil : ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}