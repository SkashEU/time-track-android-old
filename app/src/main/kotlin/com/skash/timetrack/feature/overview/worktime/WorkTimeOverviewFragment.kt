package com.skash.timetrack.feature.overview.worktime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.databinding.FragmentOverviewWorkTimeBinding
import com.skash.timetrack.feature.adapter.WorkTimeGroupListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkTimeOverviewFragment : Fragment(R.layout.fragment_overview_work_time) {

    private var _binding: FragmentOverviewWorkTimeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkTimeOverviewViewModel by viewModels()

    private val adapter = WorkTimeGroupListAdapter()

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOverviewWorkTimeBinding.bind(view)

        binding.recyclerView.adapter = adapter

        viewModel.workTimeGroupsLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { groups ->
                adapter.submitList(groups)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}