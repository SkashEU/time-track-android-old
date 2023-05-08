package com.skash.timetrack.feature.profile.username

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.sharedprefs.getUserName
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.util.BindableFullscreenDialog
import com.skash.timetrack.databinding.FragmentNameChangeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameChangeFragment :
    BindableFullscreenDialog<FragmentNameChangeBinding>(R.layout.fragment_name_change) {

    private val viewModel: NameChangeViewModel by viewModels()

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    companion object {
        const val NAME_CHANGE = "name_change"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.usernameEditText.setText(requireContext().getUserName())

        viewModel.usernameStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = {
                notifyCallerAboutUsernameChange()
                dismiss()
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentNameChangeBinding {
        return FragmentNameChangeBinding.bind(view)
    }

    override fun bindActions() {
        binding.saveButton.setOnClickListener {
            viewModel.changeUsername(
                binding.usernameEditText.text.toString()
            )
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun notifyCallerAboutUsernameChange() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            NAME_CHANGE,
            binding.usernameEditText.text.toString()
        )
    }
}