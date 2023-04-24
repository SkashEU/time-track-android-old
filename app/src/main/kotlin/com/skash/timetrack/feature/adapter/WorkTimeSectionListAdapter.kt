package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.WorkTimeSection
import com.skash.timetrack.core.time.formatElapsedTime
import com.skash.timetrack.core.time.secondsToHoursMinutesSeconds
import com.skash.timetrack.databinding.ListItemWorkTimeSectionBinding

class WorkTimeSectionListAdapter : ListAdapter<WorkTimeSection, WorkTimeSectionViewHolder>(
    WorkTimeSectionDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkTimeSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkTimeSectionBinding.inflate(layoutInflater, parent, false)
        return WorkTimeSectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkTimeSectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WorkTimeSectionViewHolder(
    private val binding: ListItemWorkTimeSectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = WorkTimeListAdapter()

    fun bind(section: WorkTimeSection) {
        binding.childRecyclerView.adapter = adapter
        binding.childBadgeCardView.isVisible = section.workTimes.isNotEmpty()
        binding.childCountTextView.text = section.workTimes.size.toString()
        binding.childRecyclerView.isVisible = false
        binding.amountTextView.text =
            section.time.secondsToHoursMinutesSeconds().let { (hours, minutes, seconds) ->
                formatElapsedTime(hours, minutes, seconds)
            }
        adapter.submitList(section.workTimes)

        binding.cardView.setOnClickListener {
            binding.childRecyclerView.isVisible = !binding.childRecyclerView.isVisible
        }
    }
}

class WorkTimeSectionDiffUtil : ItemCallback<WorkTimeSection>() {
    override fun areItemsTheSame(oldItem: WorkTimeSection, newItem: WorkTimeSection): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: WorkTimeSection, newItem: WorkTimeSection): Boolean {
        return oldItem == newItem
    }
}