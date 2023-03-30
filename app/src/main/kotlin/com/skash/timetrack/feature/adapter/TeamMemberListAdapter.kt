package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.Member
import com.skash.timetrack.databinding.ListItemTeamMemberBinding

class TeamMemberListAdapter : ListAdapter<Member, TeamMemberViewHolder>(TeamMemberDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemTeamMemberBinding.inflate(layoutInflater, parent, false)
        return TeamMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TeamMemberViewHolder(
    private val binding: ListItemTeamMemberBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: Member) {
        binding.nameTextView.text = member.name
        binding.roleTextView.setText(member.role.displayName())
    }
}

class TeamMemberDiffUtil : ItemCallback<Member>() {
    override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
        return oldItem == newItem
    }

}