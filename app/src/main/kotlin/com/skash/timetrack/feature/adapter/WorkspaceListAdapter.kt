package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.wrapper.WorkspaceSelectionWrapper
import com.skash.timetrack.databinding.ListItemWorkspaceBinding

class WorkspaceListAdapter(
    val onWorkspaceClicked: (WorkspaceSelectionWrapper) -> Unit
) : ListAdapter<WorkspaceSelectionWrapper, WorkspaceViewHolder>(WorkspaceDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkspaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkspaceBinding.inflate(layoutInflater, parent, false)
        return WorkspaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkspaceViewHolder, position: Int) {
        holder.bind(getItem(position), onWorkspaceClicked)
    }
}

class WorkspaceViewHolder(
    private val binding: ListItemWorkspaceBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wrapper: WorkspaceSelectionWrapper,
        onWorkspaceClicked: (WorkspaceSelectionWrapper) -> Unit
    ) {
        binding.titleTextView.text = wrapper.workspace.title
        binding.checkmarkImageView.isVisible = wrapper.isSelected

        binding.cardView.setOnClickListener {
            onWorkspaceClicked(wrapper)
        }
    }
}

class WorkspaceDiffUtil : ItemCallback<WorkspaceSelectionWrapper>() {
    override fun areItemsTheSame(
        oldItem: WorkspaceSelectionWrapper,
        newItem: WorkspaceSelectionWrapper
    ): Boolean {
        return oldItem.workspace.id == newItem.workspace.id
    }

    override fun areContentsTheSame(
        oldItem: WorkspaceSelectionWrapper,
        newItem: WorkspaceSelectionWrapper
    ): Boolean {
        return oldItem == newItem
    }
}