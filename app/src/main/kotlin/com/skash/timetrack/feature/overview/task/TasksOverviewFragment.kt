package com.skash.timetrack.feature.overview.task

import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentOverviewTasksBinding
import com.skash.timetrack.feature.adapter.TaskGroupListAdapter
import com.skash.timetrack.feature.broadcast.ReloadBroadcastReceiver
import com.skash.timetrack.feature.service.ReloadService
import com.skash.timetrack.feature.timer.task.TaskTimerBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksOverviewFragment :
    BindableFragment<FragmentOverviewTasksBinding>(R.layout.fragment_overview_tasks) {

    private val viewModel: TasksOverviewViewModel by viewModels()

    private val adapter = TaskGroupListAdapter()

    private val trackingBottomSheet = TaskTimerBottomSheet(onNewEntryCreated = {
        viewModel.fetchTasks()
    })

    private val reloadBroadcastReceiver = ReloadBroadcastReceiver(onDataReloaded = {
        viewModel.fetchTasks()
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        binding.timeTrackFab.setOnClickListener {
            trackingBottomSheet.show(childFragmentManager, null)
        }

        viewModel.taskGroupsLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), null, onSuccess = { groups ->
                adapter.submitList(groups)
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentOverviewTasksBinding {
        return FragmentOverviewTasksBinding.bind(view)
    }

    override fun bindActions() {}

    override fun onResume() {
        super.onResume()
        viewModel.fetchTasks()

        val reloadFilter = IntentFilter()
        reloadFilter.addAction(ReloadService.RELOADED_ACTION)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(reloadBroadcastReceiver, reloadFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(
            requireContext()
        ).unregisterReceiver(reloadBroadcastReceiver)
    }
}