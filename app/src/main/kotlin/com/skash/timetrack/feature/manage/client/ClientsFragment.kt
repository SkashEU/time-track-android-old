package com.skash.timetrack.feature.manage.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentClientsBinding
import com.skash.timetrack.feature.adapter.ClientListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientsFragment : BindableFragment<FragmentClientsBinding>(R.layout.fragment_clients) {

    private val viewModel: ClientsViewModel by viewModels()

    private val adapter = ClientListAdapter(onClientClicked = { client ->

    })

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewModel.clientsStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { clients ->
                adapter.submitList(clients)
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentClientsBinding {
        return FragmentClientsBinding.bind(view)
    }

    override fun bindActions() {}
}