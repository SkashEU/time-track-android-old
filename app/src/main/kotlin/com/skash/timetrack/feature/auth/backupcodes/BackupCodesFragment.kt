package com.skash.timetrack.feature.auth.backupcodes

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.dialog.FullscreenDialogFragment
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.model.BackupCode
import com.skash.timetrack.databinding.FragmentBackupCodesBinding
import com.skash.timetrack.feature.adapter.BackupCodeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class BackupCodesFragment : FullscreenDialogFragment(
    R.layout.fragment_backup_codes
) {

    private var _binding: FragmentBackupCodesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BackupCodesViewModel by viewModels()

    private val adapter = BackupCodeListAdapter(onBackupCodeClicked = {
        copyCodeToClipboard(it)
    })

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    private val subscriptions = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBackupCodesBinding.bind(view)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        bindActions()

        viewModel.backupCodeLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = {
                adapter.submitList(it)
            })
        }
    }

    private fun bindActions() {
        binding.closeButton.clicks()
            .subscribe {
                dismiss()
            }
            .addTo(subscriptions)
    }

    private fun copyCodeToClipboard(code: BackupCode) {
        val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Backup Code", code.code)
        clipboard?.setPrimaryClip(clip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        _binding = null
    }
}