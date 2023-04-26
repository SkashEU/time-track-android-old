package com.skash.timetrack.feature.profile.avatar

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skash.timetrack.R
import com.skash.timetrack.databinding.FragmentManageAvatarBinding

enum class ManageAvatarOption {
    REMOVE,
    MODIFY
}

class ManageAvatarBottomSheet(
    val onOptionSelected: (ManageAvatarOption) -> Unit
) : BottomSheetDialogFragment(R.layout.fragment_manage_avatar) {

    private var _binding: FragmentManageAvatarBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentManageAvatarBinding.bind(view)

        bindActions()
    }

    private fun bindActions() {
        binding.changeAvatarTextView.setOnClickListener {
            onOptionSelected(ManageAvatarOption.MODIFY)
            dismiss()
        }

        binding.removeAvatarTextView.setOnClickListener {
            onOptionSelected(ManageAvatarOption.REMOVE)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}