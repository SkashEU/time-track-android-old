package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.menu.ProfileSection
import com.skash.timetrack.core.menu.ProfileSectionEntry
import com.skash.timetrack.databinding.ListItemProfileSectionBinding

class ProfileSectionListAdapter(
    val onEntryClicked: (ProfileSectionEntry) -> Unit
) : ListAdapter<ProfileSection, ProfileSectionViewHolder>(ProfileSectionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemProfileSectionBinding.inflate(layoutInflater, parent, false)
        return ProfileSectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileSectionViewHolder, position: Int) {
        holder.bind(getItem(position), onEntryClicked)
    }
}

class ProfileSectionViewHolder(
    private val binding: ListItemProfileSectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(section: ProfileSection, onEntryClicked: (ProfileSectionEntry) -> Unit) {
        val adapter = ProfileSectionEntryListAdapter(onEntryClicked)
        binding.sectionTextView.setText(section.titleId)
        binding.recyclerView.adapter = adapter
        adapter.submitList(section.items)
    }

}

class ProfileSectionDiffUtil : ItemCallback<ProfileSection>() {
    override fun areItemsTheSame(oldItem: ProfileSection, newItem: ProfileSection): Boolean {
        return oldItem.titleId == newItem.titleId
    }

    override fun areContentsTheSame(oldItem: ProfileSection, newItem: ProfileSection): Boolean {
        return oldItem == newItem
    }
}