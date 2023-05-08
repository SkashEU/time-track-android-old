package com.skash.timetrack.feature.overview.worktime

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentOverviewWorkTimeBinding
import com.skash.timetrack.feature.adapter.WorkTimeGroupListAdapter
import com.skash.timetrack.feature.broadcast.ReloadBroadcastReceiver
import com.skash.timetrack.feature.service.ReloadService
import com.skash.timetrack.feature.timer.worktime.WorkTimeBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkTimeOverviewFragment :
    BindableFragment<FragmentOverviewWorkTimeBinding>(R.layout.fragment_overview_work_time) {

    private val viewModel: WorkTimeOverviewViewModel by viewModels()

    private val adapter = WorkTimeGroupListAdapter()

    private val trackingBottomSheet = WorkTimeBottomSheet(onNewEntryCreated = {
        viewModel.fetchWorkTimes()
    })

    private val reloadBroadcastReceiver = ReloadBroadcastReceiver(onDataReloaded = {
        viewModel.fetchWorkTimes()
        Log.d(javaClass.name, "Reload triggered by receiver")
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        binding.timeTrackFab.setOnClickListener {
            trackingBottomSheet.show(childFragmentManager, null)
        }

        viewModel.workTimeGroupsLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), null, onSuccess = { groups ->
                adapter.submitList(groups)
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentOverviewWorkTimeBinding {
        return FragmentOverviewWorkTimeBinding.bind(view)
    }

    override fun bindActions() {}

    override fun onResume() {
        super.onResume()
        viewModel.fetchWorkTimes()

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