package com.skash.timetrack.feature.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.ProjectColor
import com.skash.timetrack.databinding.ListItemProjectColorBinding

class ProjectColorListAdapter(
    val onColorClicked: (ProjectColor) -> Unit
) : ListAdapter<ProjectColor, ProjectColorViewHolder>(ProjectColorDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectColorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemProjectColorBinding.inflate(layoutInflater, parent, false)
        return ProjectColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectColorViewHolder, position: Int) {
        holder.bind(getItem(position), onColorClicked)
    }
}

class ProjectColorViewHolder(
    private val binding: ListItemProjectColorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(color: ProjectColor, onColorClicked: (ProjectColor) -> Unit) {
        binding.colorDotImageView.setColorFilter(Color.parseColor(color.hex))
        binding.selectedImageView.isVisible = color.isSelected
        binding.root.setOnClickListener {
            onColorClicked(color)
        }
    }
}

class ProjectColorDiffUtil : ItemCallback<ProjectColor>() {
    override fun areItemsTheSame(oldItem: ProjectColor, newItem: ProjectColor): Boolean {
        return oldItem.hex == newItem.hex
    }

    override fun areContentsTheSame(oldItem: ProjectColor, newItem: ProjectColor): Boolean {
        return oldItem == newItem
    }
}