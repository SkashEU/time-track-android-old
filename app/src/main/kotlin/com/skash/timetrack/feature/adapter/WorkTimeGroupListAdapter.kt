package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.WorkTimeGroup
import com.skash.timetrack.databinding.ListItemWorkTimeGroupBinding
import java.text.SimpleDateFormat
import java.util.Locale

class WorkTimeGroupListAdapter :
    ListAdapter<WorkTimeGroup, WorkTimeGroupViewHolder>(WorkTimeGroupDiffUtil()) {

    companion object {
        val dateFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkTimeGroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkTimeGroupBinding.inflate(layoutInflater, parent, false)
        return WorkTimeGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkTimeGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WorkTimeGroupViewHolder(
    private val binding: ListItemWorkTimeGroupBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = WorkTimeSectionListAdapter()

    fun bind(group: WorkTimeGroup) {
        binding.dateTextView.text = WorkTimeGroupListAdapter.dateFormat.format(group.date)
        binding.recyclerView.adapter = adapter
        adapter.submitList(group.workTimes)
    }
}

class WorkTimeGroupDiffUtil : DiffUtil.ItemCallback<WorkTimeGroup>() {
    override fun areItemsTheSame(oldItem: WorkTimeGroup, newItem: WorkTimeGroup): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: WorkTimeGroup, newItem: WorkTimeGroup): Boolean {
        return oldItem == newItem
    }
}