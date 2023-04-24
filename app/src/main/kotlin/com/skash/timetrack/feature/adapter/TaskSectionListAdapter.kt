package com.skash.timetrack.feature.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.TaskSection
import com.skash.timetrack.core.time.formatElapsedTime
import com.skash.timetrack.core.time.secondsToHoursMinutesSeconds
import com.skash.timetrack.databinding.ListItemTaskSectionBinding

class TaskSectionListAdapter :
    ListAdapter<TaskSection, TaskSectionViewHolder>(TaskSectionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemTaskSectionBinding.inflate(layoutInflater, parent, false)
        return TaskSectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskSectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TaskSectionViewHolder(
    private val binding: ListItemTaskSectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = TaskListAdapter()

    fun bind(section: TaskSection) {
        binding.childRecyclerView.adapter = adapter
        binding.childBadgeCardView.isVisible = section.tasks.isNotEmpty()
        binding.childCountTextView.text = section.tasks.size.toString()
        binding.childRecyclerView.isVisible = false
        binding.projectTitleTextView.text = section.project?.title
        binding.colorDotImageView.setColorFilter(Color.parseColor(section.project?.color))
        binding.taskDescription.text = section.description
        binding.customerTitleTextView.text = "- ${section.project?.client?.title}"
        binding.amountTextView.text =
            section.time.secondsToHoursMinutesSeconds().let { (hours, minutes, seconds) ->
                formatElapsedTime(hours, minutes, seconds)
            }
        adapter.submitList(section.tasks)

        binding.cardView.setOnClickListener {
            binding.childRecyclerView.isVisible = !binding.childRecyclerView.isVisible
        }
    }
}

class TaskSectionDiffUtil : ItemCallback<TaskSection>() {
    override fun areItemsTheSame(oldItem: TaskSection, newItem: TaskSection): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: TaskSection, newItem: TaskSection): Boolean {
        return oldItem == newItem
    }
}