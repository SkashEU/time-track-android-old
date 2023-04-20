package com.skash.timetrack.feature.auth.reset

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetBottomSheet(
    val onPasswordResetRequested: () -> Unit
) : BottomSheetDialogFragment(
    R.layout.fragment_reset_password
) {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PasswordResetViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentResetPasswordBinding.bind(view)

        bindActions()

        viewModel.emailValidationLiveData.observe(viewLifecycleOwner) { validationState ->
            validationState.handle(requireContext(), null, onSuccess = {
                onPasswordResetRequested()
                dismiss()
            })
        }
    }

    private fun bindActions() {
        binding.resetButton.setOnClickListener {
            viewModel.resetPassword(
                binding.emailEditText.text.toString()
            )
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}