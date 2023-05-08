package com.skash.timetrack.feature.auth.twofa

import android.view.View
import com.skash.timetrack.R
import com.skash.timetrack.core.util.BindableBottomSheet
import com.skash.timetrack.databinding.FragmentTwoFactorAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TwoFactorAuthBottomSheet(
    val onCodeEntered: (Int?) -> Unit
) : BindableBottomSheet<FragmentTwoFactorAuthBinding>(
    R.layout.fragment_two_factor_auth
) {

    override fun createBindingInstance(view: View): FragmentTwoFactorAuthBinding {
        return FragmentTwoFactorAuthBinding.bind(view)
    }

    override fun bindActions() {
        binding.loginButton.setOnClickListener {
            onCodeEntered(binding.codeEditText.text.toString().toIntOrNull())
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}