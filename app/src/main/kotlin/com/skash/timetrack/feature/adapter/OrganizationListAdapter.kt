package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.wrapper.OrganizationSelectionWrapper
import com.skash.timetrack.databinding.ListItemOrganizationBinding

class OrganizationListAdapter(
    val onOrganizationClicked: (OrganizationSelectionWrapper) -> Unit
) : ListAdapter<OrganizationSelectionWrapper, OrganizationViewHolder>(OrganizationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemOrganizationBinding.inflate(layoutInflater, parent, false)
        return OrganizationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        holder.bind(getItem(position), onOrganizationClicked)
    }
}

class OrganizationViewHolder(
    private val binding: ListItemOrganizationBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        organization: OrganizationSelectionWrapper,
        onOrganizationClicked: (OrganizationSelectionWrapper) -> Unit
    ) {
        binding.titleTextView.text = organization.organization.name
        binding.checkmarkImageView.isVisible = organization.isSelected

        binding.cardView.setOnClickListener {
            onOrganizationClicked(organization)
        }
    }
}

class OrganizationDiffUtil : ItemCallback<OrganizationSelectionWrapper>() {
    override fun areItemsTheSame(
        oldItem: OrganizationSelectionWrapper,
        newItem: OrganizationSelectionWrapper
    ): Boolean {
        return oldItem.organization.id == newItem.organization.id
    }

    override fun areContentsTheSame(
        oldItem: OrganizationSelectionWrapper,
        newItem: OrganizationSelectionWrapper
    ): Boolean {
        return oldItem == newItem
    }
}