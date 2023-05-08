package com.skash.timetrack.feature.timer.worktime

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.context.getWorkTimerStatus
import com.skash.timetrack.core.helper.context.moveWorkTimeTimerToBackground
import com.skash.timetrack.core.helper.context.moveWorkTimeTimerToForeground
import com.skash.timetrack.core.helper.context.startWorkTimeTimer
import com.skash.timetrack.core.helper.context.stopWorkTimeTimer
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.model.TimerStatus
import com.skash.timetrack.core.model.WorkTime
import com.skash.timetrack.core.util.BindableBottomSheet
import com.skash.timetrack.databinding.FragmentWorktimeBinding
import com.skash.timetrack.feature.broadcast.ElapsedTimeBroadcastReceiver
import com.skash.timetrack.feature.broadcast.TimerStatusBroadcastReceiver
import com.skash.timetrack.feature.service.TimerService
import com.skash.timetrack.feature.service.WorkTimeTimerService
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class WorkTimeBottomSheet(
    val onNewEntryCreated: (WorkTime) -> Unit
) : BindableBottomSheet<FragmentWorktimeBinding>(R.layout.fragment_worktime) {

    private val viewModel: WorkTimeViewModel by viewModels()

    private val timerStatusBroadcastReceiver = TimerStatusBroadcastReceiver(
        onStateChanged = {
            viewModel.updateTimerStatus(it)
        }
    )

    private val elapsedTimeBroadcastReceiver = ElapsedTimeBroadcastReceiver(
        onTimeElapsed = {
            Log.d(javaClass.name, "Got new Time: $it")
            viewModel.updateTimerStatus(TimerStatus(true, it))
        }
    )

    private val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        viewModel.timerActionLiveData.observe(viewLifecycleOwner) { shouldStartTimer ->
            if (shouldStartTimer) {
                requireContext().startWorkTimeTimer()
                return@observe
            }

            requireContext().stopWorkTimeTimer()
        }

        viewModel.timerStatusLiveData.observe(viewLifecycleOwner) { status ->
            if (status.isTimerRunning && binding.timerButton.text != getText(R.string.title_timer_button_stop)) {
                binding.timerButton.setText(R.string.title_timer_button_stop)
            } else if (!status.isTimerRunning && binding.timerButton.text != getText(R.string.title_timer_button_start)) {
                binding.timerButton.setText(R.string.title_timer_button_start)
            }

            if (status.isFinished.not()) {
                updateTimer(status.elapsedTime)
                return@observe
            }

            viewModel.createWorkTime(
                status.elapsedTime
            )
        }

        viewModel.workTimeCreationStateLiveData.observe(viewLifecycleOwner) { creationState ->
            creationState.handle(requireContext(), loadingDialog, onSuccess = {
                onNewEntryCreated(it)
                updateTimer(0)
                dismiss()
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentWorktimeBinding {
        return FragmentWorktimeBinding.bind(view)
    }

    override fun bindActions() {
        binding.timerButton.setOnClickListener {
            viewModel.startOrStopTimer()
        }
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()

        requireContext().getWorkTimerStatus()
        requireContext().moveWorkTimeTimerToBackground()
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(
            requireContext()
        ).unregisterReceiver(timerStatusBroadcastReceiver)

        LocalBroadcastManager.getInstance(
            requireContext()
        ).unregisterReceiver(elapsedTimeBroadcastReceiver)

        requireContext().moveWorkTimeTimerToForeground()
    }

    private fun registerBroadcastReceiver() {
        val statusFilter = IntentFilter()
        statusFilter.addAction(WorkTimeTimerService.TIMER_STATUS)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(timerStatusBroadcastReceiver, statusFilter)

        // Receiving time values from service
        val timeFilter = IntentFilter()
        timeFilter.addAction(WorkTimeTimerService.TIMER_TICK)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(elapsedTimeBroadcastReceiver, timeFilter)
    }

    private fun updateTimer(elapsedTime: Int) {
        val hours: Int = (elapsedTime / 60) / 60
        val minutes: Int = (elapsedTime / 60)
        val seconds: Int = (elapsedTime % 60)
        binding.timeTextView.text = TimerService.formatElapsedTime(
            hours, minutes, seconds
        )
    }

    private fun setupView() {
        val today = Date()
        binding.dayTextView.text = dayFormatter.format(today)
        binding.dateTextView.text = dateFormatter.format(today)
    }
}