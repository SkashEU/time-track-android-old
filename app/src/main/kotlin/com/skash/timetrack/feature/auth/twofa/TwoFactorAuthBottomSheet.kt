package com.skash.timetrack.feature.auth.twofa

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.databinding.FragmentTwoFactorAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class TwoFactorAuthBottomSheet(
    val onCodeEntered: (Int?) -> Unit
) : BottomSheetDialogFragment(
    R.layout.fragment_two_factor_auth
) {

    private var _binding: FragmentTwoFactorAuthBinding? = null
    private val binding get() = _binding!!

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    private val subscriptions = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTwoFactorAuthBinding.bind(view)
        bindActions()
    }

    private fun bindActions() {
        binding.loginButton.clicks()
            .subscribe {
                onCodeEntered(binding.codeEditText.text.toString().toIntOrNull())
            }
            .addTo(subscriptions)

        binding.cancelButton.clicks()
            .subscribe {
                dismiss()
            }
            .addTo(subscriptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        _binding = null
    }
}