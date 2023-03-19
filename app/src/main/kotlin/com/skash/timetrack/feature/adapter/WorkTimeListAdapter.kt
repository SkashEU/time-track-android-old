package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.WorkTime
import com.skash.timetrack.databinding.ListItemWorkTimeBinding

class WorkTimeListAdapter : ListAdapter<WorkTime, WorkTimeViewHolder>(WorkTimeDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkTimeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkTimeBinding.inflate(layoutInflater, parent, false)
        return WorkTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkTimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WorkTimeViewHolder(
    private val binding: ListItemWorkTimeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(workTime: WorkTime) {
        // TODO: Calculate Time dif etc
        binding.amountTextView.text = "00:05:22"
    }
}

class WorkTimeDiffUtil : DiffUtil.ItemCallback<WorkTime>() {
    override fun areItemsTheSame(oldItem: WorkTime, newItem: WorkTime): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WorkTime, newItem: WorkTime): Boolean {
        return oldItem == newItem
    }

}