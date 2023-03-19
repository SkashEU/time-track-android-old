package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.TaskGroup
import com.skash.timetrack.databinding.ListItemTaskGroupBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TaskGroupListAdapter : ListAdapter<TaskGroup, TaskGroupViewHolder>(TaskGroupDiffUtil()) {

    companion object {
        val dateFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskGroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemTaskGroupBinding.inflate(layoutInflater, parent, false)
        return TaskGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TaskGroupViewHolder(
    private val binding: ListItemTaskGroupBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = TaskListAdapter()

    fun bind(group: TaskGroup) {
        binding.dateTextView.text = TaskGroupListAdapter.dateFormat.format(group.date)
        binding.recyclerView.adapter = adapter
        adapter.submitList(group.tasks)
    }
}

class TaskGroupDiffUtil : ItemCallback<TaskGroup>() {
    override fun areItemsTheSame(oldItem: TaskGroup, newItem: TaskGroup): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: TaskGroup, newItem: TaskGroup): Boolean {
        return oldItem == newItem
    }
}