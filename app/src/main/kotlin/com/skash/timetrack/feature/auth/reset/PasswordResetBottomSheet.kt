package com.skash.timetrack.feature.auth.reset

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.util.BindableBottomSheet
import com.skash.timetrack.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetBottomSheet(
    val onPasswordResetRequested: () -> Unit
) : BindableBottomSheet<FragmentResetPasswordBinding>(
    R.layout.fragment_reset_password
) {

    private val viewModel by viewModels<PasswordResetViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.emailValidationLiveData.observe(viewLifecycleOwner) { validationState ->
            validationState.handle(requireContext(), null, onSuccess = {
                onPasswordResetRequested()
                dismiss()
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentResetPasswordBinding {
        return FragmentResetPasswordBinding.bind(view)
    }

    override fun bindActions() {
        binding.resetButton.setOnClickListener {
            viewModel.resetPassword(
                binding.emailEditText.text.toString()
            )
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}