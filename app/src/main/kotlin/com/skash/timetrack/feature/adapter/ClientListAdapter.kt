package com.skash.timetrack.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skash.timetrack.core.model.Client
import com.skash.timetrack.databinding.ListItemClientBinding

class ClientListAdapter(
    val onClientClicked: (Client) -> Unit
) : ListAdapter<Client, ClientViewHolder>(ClientDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemClientBinding.inflate(layoutInflater, parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(getItem(position), onClientClicked)
    }
}

class ClientViewHolder(
    private val binding: ListItemClientBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(client: Client, onClientClicked: (Client) -> Unit) {
        binding.titleTextView.text = client.title

        binding.root.setOnClickListener {
            onClientClicked(client)
        }
    }
}

class ClientDiffUtil : ItemCallback<Client>() {
    override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem == newItem
    }
}