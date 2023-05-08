package com.skash.timetrack.feature.auth.backupcodes

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.model.BackupCode
import com.skash.timetrack.core.util.BindableFullscreenDialog
import com.skash.timetrack.databinding.FragmentBackupCodesBinding
import com.skash.timetrack.feature.adapter.BackupCodeListAdapter
import com.skash.timetrack.feature.auth.confirmation.PasswordConfirmationBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class BackupCodesFragment : BindableFullscreenDialog<FragmentBackupCodesBinding>(
    R.layout.fragment_backup_codes
) {

    private val viewModel: BackupCodesViewModel by viewModels()

    private val adapter = BackupCodeListAdapter(
        onBackupCodeClicked = {
            copyCodeToClipboard(it)
        }
    )

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    private val passwordConfirmationBottomSheet = PasswordConfirmationBottomSheet(
        R.string.backup_codes_view_codes_title,
        onPasswordEntered = {
            viewModel.fetchBackupCodes(it)
        },
        onCancel = {
            dismiss()
        }
    )

    private val subscriptions = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordConfirmationBottomSheet.show(childFragmentManager, null)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        viewModel.backupCodeLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = {
                adapter.submitList(it)
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentBackupCodesBinding {
        return FragmentBackupCodesBinding.bind(view)
    }

    override fun bindActions() {
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun copyCodeToClipboard(code: BackupCode) {
        val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Backup Code", code.code)
        clipboard?.setPrimaryClip(clip)
    }
}