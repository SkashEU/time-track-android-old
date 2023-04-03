package com.skash.timetrack.feature.overview.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.databinding.FragmentOverviewTasksBinding
import com.skash.timetrack.feature.adapter.TaskGroupListAdapter
import com.skash.timetrack.feature.timer.task.TaskTimerBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksOverviewFragment : Fragment(R.layout.fragment_overview_tasks) {

    private var _binding: FragmentOverviewTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TasksOverviewViewModel by viewModels()

    private val adapter = TaskGroupListAdapter()

    private val trackingBottomSheet = TaskTimerBottomSheet()

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOverviewTasksBinding.bind(view)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        binding.timeTrackFab.setOnClickListener {
            trackingBottomSheet.show(childFragmentManager, null)
        }

        viewModel.taskGroupsLiveData.observe(viewLifecycleOwner) { state ->
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