package com.skash.timetrack.feature.timer.task

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.context.getTaskTimerStatus
import com.skash.timetrack.core.helper.context.moveTaskTimerToBackground
import com.skash.timetrack.core.helper.context.moveTaskTimerToForeground
import com.skash.timetrack.core.helper.context.startTaskTimer
import com.skash.timetrack.core.helper.context.stopTaskTimer
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.Task
import com.skash.timetrack.core.model.TimerStatus
import com.skash.timetrack.core.util.BindableBottomSheet
import com.skash.timetrack.databinding.FragmentProjectTimeBinding
import com.skash.timetrack.feature.broadcast.ElapsedTimeBroadcastReceiver
import com.skash.timetrack.feature.broadcast.TimerStatusBroadcastReceiver
import com.skash.timetrack.feature.service.TaskTimerService
import com.skash.timetrack.feature.service.TimerService
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TaskTimerBottomSheet(
    val onNewEntryCreated: (Task) -> Unit
) : BindableBottomSheet<FragmentProjectTimeBinding>(R.layout.fragment_project_time) {

    private val viewModel: TaskTimerViewModel by viewModels()

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

    private val dropdownAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.list_item_dropdown,
            mutableListOf<Project>()
        )
    }

    private val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (binding.projectInputLayout.editText as? AutoCompleteTextView)?.setAdapter(dropdownAdapter)

        setupView()

        viewModel.timerActionLiveData.observe(viewLifecycleOwner) { shouldStartTimer ->
            if (shouldStartTimer) {
                requireContext().startTaskTimer()
                return@observe
            }

            requireContext().stopTaskTimer()
        }

        viewModel.projectStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { projects ->
                dropdownAdapter.addAll(projects)
            })
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

        viewModel.taskTimeCreationStateLiveData.observe(viewLifecycleOwner) { creationState ->
            creationState.handle(requireContext(), loadingDialog, onSuccess = {
                onNewEntryCreated(it)
                updateTimer(0)
                dismiss()
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentProjectTimeBinding {
        return FragmentProjectTimeBinding.bind(view)
    }

    override fun bindActions() {
        binding.timerButton.setOnClickListener {
            viewModel.startOrStopTimer()
        }

        binding.projectMenu.setOnItemClickListener { _, _, position, _ ->
            val client = dropdownAdapter.getItem(position) ?: return@setOnItemClickListener
            viewModel.setProject(client)
        }
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()

        requireContext().getTaskTimerStatus()
        requireContext().moveTaskTimerToBackground()
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(
            requireContext()
        ).unregisterReceiver(timerStatusBroadcastReceiver)

        LocalBroadcastManager.getInstance(
            requireContext()
        ).unregisterReceiver(elapsedTimeBroadcastReceiver)

        requireContext().moveTaskTimerToForeground()
    }

    private fun registerBroadcastReceiver() {
        val statusFilter = IntentFilter()
        statusFilter.addAction(TaskTimerService.TIMER_STATUS)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(timerStatusBroadcastReceiver, statusFilter)

        // Receiving time values from service
        val timeFilter = IntentFilter()
        timeFilter.addAction(TaskTimerService.TIMER_TICK)
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