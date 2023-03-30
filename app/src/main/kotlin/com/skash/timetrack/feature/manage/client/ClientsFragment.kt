package com.skash.timetrack.feature.manage.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.databinding.FragmentClientsBinding
import com.skash.timetrack.feature.adapter.ClientListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientsFragment : Fragment(R.layout.fragment_clients) {

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientsViewModel by viewModels()

    private val adapter = ClientListAdapter(onClientClicked = { client ->

    })

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentClientsBinding.bind(view)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewModel.clientsStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { clients ->
                adapter.submitList(clients)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}