package com.skash.timetrack.feature.timer.task

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.context.getProjectTimerStatus
import com.skash.timetrack.core.helper.context.moveProjectTimerToBackground
import com.skash.timetrack.core.helper.context.moveProjectTimerToForeground
import com.skash.timetrack.core.helper.context.startProjectTimer
import com.skash.timetrack.core.helper.context.stopProjectTimer
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.model.TimerStatus
import com.skash.timetrack.databinding.FragmentProjectTimeBinding
import com.skash.timetrack.feature.broadcast.ElapsedTimeBroadcastReceiver
import com.skash.timetrack.feature.broadcast.TimerStatusBroadcastReceiver
import com.skash.timetrack.feature.service.ProjectTimerService
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TaskTimerBottomSheet : BottomSheetDialogFragment(R.layout.fragment_project_time) {

    private var _binding: FragmentProjectTimeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskTimerViewModel by viewModels()

    private val timerStatusBroadcastReceiver = TimerStatusBroadcastReceiver(
        onStateChanged = {
            viewModel.updateTimerStatus(it)
        }
    )

    private val elapsedTimeBroadcastReceiver = ElapsedTimeBroadcastReceiver(
        onTimeElapsed = {
            viewModel.updateTimerStatus(TimerStatus(true, it))
        }
    )

    private val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    private val subscriptions = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProjectTimeBinding.bind(view)

        bindActions()
        setupView()

        viewModel.timerActionLiveData.observe(viewLifecycleOwner) { shouldStartTimer ->
            if (shouldStartTimer) {
                requireContext().startProjectTimer()
                return@observe
            }

            requireContext().stopProjectTimer()
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

            viewModel.createProjectTime(
                binding.descriptionEditText.text.toString(),
                status.elapsedTime
            )
        }

        viewModel.projectTimeCreationStateLiveData.observe(viewLifecycleOwner) { creationState ->
            creationState.handle(requireContext(), loadingDialog, onSuccess = {
                Log.d(javaClass.name, "Saved Project Time")
                updateTimer(0)
            })
        }
    }

    private fun bindActions() {
        binding.timerButton.clicks()
            .subscribe {
                viewModel.startOrStopTimer()
            }
            .addTo(subscriptions)
    }

    override fun onResume() {
        super.onResume()
        requireContext().getProjectTimerStatus()

        registerBroadcastReceiver()
        requireContext().moveProjectTimerToBackground()
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(
            requireContext()
        ).unregisterReceiver(timerStatusBroadcastReceiver)

        LocalBroadcastManager.getInstance(
            requireContext()
        ).unregisterReceiver(elapsedTimeBroadcastReceiver)

        requireContext().moveProjectTimerToForeground()
    }

    private fun registerBroadcastReceiver() {
        val statusFilter = IntentFilter()
        statusFilter.addAction(ProjectTimerService.TIMER_STATUS)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(timerStatusBroadcastReceiver, statusFilter)

        // Receiving time values from service
        val timeFilter = IntentFilter()
        timeFilter.addAction(ProjectTimerService.TIMER_TICK)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(elapsedTimeBroadcastReceiver, timeFilter)
    }

    private fun updateTimer(elapsedTime: Int) {
        val hours: Int = (elapsedTime / 60) / 60
        val minutes: Int = (elapsedTime / 60)
        val seconds: Int = (elapsedTime % 60)
        binding.timeTextView.text = ProjectTimerService.formatElapsedTime(
            hours, minutes, seconds
        )
    }

    private fun setupView() {
        val today = Date()
        binding.dayTextView.text = dayFormatter.format(today)
        binding.dateTextView.text = dateFormatter.format(today)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        _binding = null
    }
}