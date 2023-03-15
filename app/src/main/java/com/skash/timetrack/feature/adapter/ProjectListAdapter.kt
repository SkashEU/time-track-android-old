package com.skash.timetrack.feature.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.databinding.ListItemProjectBinding

class ProjectListAdapter(
    val onProjectSelected: (Project) -> Unit
) : ListAdapter<Project, ProjectViewHolder>(ProjectDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemProjectBinding.inflate(layoutInflater, parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(getItem(position), onProjectSelected)
    }
}

class ProjectViewHolder(
    private val binding: ListItemProjectBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(project: Project, onProjectSelected: (Project) -> Unit) {
        binding.titleTextView.text = project.title
        binding.colorDotImageView.setColorFilter(Color.parseColor(project.color))

        binding.root.setOnClickListener {
            onProjectSelected(project)
        }
    }
}

class ProjectDiffUtil : ItemCallback<Project>() {
    override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem == newItem
    }
}