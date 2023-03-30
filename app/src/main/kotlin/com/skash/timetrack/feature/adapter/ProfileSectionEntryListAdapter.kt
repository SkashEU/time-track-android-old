package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.menu.ProfileSectionEntry
import com.skash.timetrack.databinding.ListItemProfileSectionEntryBinding

class ProfileSectionEntryListAdapter(
    val onEntryClicked: (ProfileSectionEntry) -> Unit
) : ListAdapter<ProfileSectionEntry, ProfileSectionEntryViewHolder>(ProfileSectionEntryDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileSectionEntryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemProfileSectionEntryBinding.inflate(layoutInflater, parent, false)
        return ProfileSectionEntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileSectionEntryViewHolder, position: Int) {
        holder.bind(getItem(position), onEntryClicked)
    }
}

class ProfileSectionEntryViewHolder(
    private val binding: ListItemProfileSectionEntryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entry: ProfileSectionEntry, onEntryClicked: (ProfileSectionEntry) -> Unit) {
        binding.titleTextView.setText(entry.type.titleId)
        binding.valueTextView.text = entry.value

        binding.root.setOnClickListener {
            onEntryClicked(entry)
        }
    }
}

class ProfileSectionEntryDiffUtil : ItemCallback<ProfileSectionEntry>() {
    override fun areItemsTheSame(
        oldItem: ProfileSectionEntry,
        newItem: ProfileSectionEntry
    ): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(
        oldItem: ProfileSectionEntry,
        newItem: ProfileSectionEntry
    ): Boolean {
        return oldItem == newItem
    }

}