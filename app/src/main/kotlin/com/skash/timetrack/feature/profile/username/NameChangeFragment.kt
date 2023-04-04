package com.skash.timetrack.feature.profile.username

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.dialog.FullscreenDialogFragment
import com.skash.timetrack.core.helper.sharedprefs.getUserName
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.databinding.FragmentNameChangeBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class NameChangeFragment : FullscreenDialogFragment(R.layout.fragment_name_change) {

    private var _binding: FragmentNameChangeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NameChangeViewModel by viewModels()

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    private val subscriptions = CompositeDisposable()

    companion object {
        const val NAME_CHANGE = "name_change"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNameChangeBinding.bind(view)

        bindActions()

        binding.usernameEditText.setText(requireContext().getUserName())

        viewModel.usernameStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = {
                notifyCallerAboutUsernameChange()
                dismiss()
            })
        }
    }

    private fun bindActions() {
        binding.saveButton.clicks()
            .subscribe {
                viewModel.changeUsername(
                    binding.usernameEditText.text.toString()
                )
            }
            .addTo(subscriptions)

        binding.closeButton.clicks()
            .subscribe {
                dismiss()
            }
            .addTo(subscriptions)
    }

    private fun notifyCallerAboutUsernameChange() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            NAME_CHANGE,
            binding.usernameEditText.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        _binding = null
    }
}