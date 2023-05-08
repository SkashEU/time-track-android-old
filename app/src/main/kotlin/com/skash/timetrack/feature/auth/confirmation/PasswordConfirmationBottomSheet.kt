package com.skash.timetrack.feature.auth.confirmation

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.skash.timetrack.R
import com.skash.timetrack.core.util.BindableBottomSheet
import com.skash.timetrack.databinding.FragmentPasswordConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordConfirmationBottomSheet(
    @StringRes
    val title: Int,
    val onPasswordEntered: (String) -> Unit,
    val onCancel: () -> Unit
) : BindableBottomSheet<FragmentPasswordConfirmationBinding>(
    R.layout.fragment_password_confirmation
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headlineTextView.setText(title)
        isCancelable = false
    }

    override fun createBindingInstance(view: View): FragmentPasswordConfirmationBinding {
        return FragmentPasswordConfirmationBinding.bind(view)
    }

    override fun bindActions() {
        binding.cancelButton.setOnClickListener {
            onCancel()
            dismiss()
        }

        binding.okButton.setOnClickListener {
            onPasswordEntered(
                binding.passwordEditText.text.toString()
            )
            dismiss()
        }
    }
}