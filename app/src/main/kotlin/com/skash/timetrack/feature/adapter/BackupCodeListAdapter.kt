package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.BackupCode
import com.skash.timetrack.databinding.ListItemBackupCodeBinding

class BackupCodeListAdapter(
    val onBackupCodeClicked: (BackupCode) -> Unit
) : ListAdapter<BackupCode, BackupCodeViewHolder>(BackupCodeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupCodeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBackupCodeBinding.inflate(layoutInflater, parent, false)
        return BackupCodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BackupCodeViewHolder, position: Int) {
        holder.bind(getItem(position), onBackupCodeClicked)
    }
}

class BackupCodeViewHolder(
    private val binding: ListItemBackupCodeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(code: BackupCode, onBackupCodeClicked: (BackupCode) -> Unit) {
        binding.titleTextView.text = code.code

        binding.cardView.setOnClickListener {
            onBackupCodeClicked(code)
        }
    }
}

class BackupCodeDiffUtil : ItemCallback<BackupCode>() {
    override fun areItemsTheSame(oldItem: BackupCode, newItem: BackupCode): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: BackupCode, newItem: BackupCode): Boolean {
        return oldItem == newItem
    }
}