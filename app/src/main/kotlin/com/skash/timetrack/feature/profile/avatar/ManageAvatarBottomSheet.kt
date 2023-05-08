package com.skash.timetrack.feature.profile.avatar

import android.view.View
import com.skash.timetrack.R
import com.skash.timetrack.core.util.BindableBottomSheet
import com.skash.timetrack.databinding.FragmentManageAvatarBinding

enum class ManageAvatarOption {
    REMOVE,
    MODIFY
}

class ManageAvatarBottomSheet(
    val onOptionSelected: (ManageAvatarOption) -> Unit
) : BindableBottomSheet<FragmentManageAvatarBinding>(R.layout.fragment_manage_avatar) {

    override fun createBindingInstance(view: View): FragmentManageAvatarBinding {
        return FragmentManageAvatarBinding.bind(view)
    }

    override fun bindActions() {
        binding.changeAvatarTextView.setOnClickListener {
            onOptionSelected(ManageAvatarOption.MODIFY)
            dismiss()
        }

        binding.removeAvatarTextView.setOnClickListener {
            onOptionSelected(ManageAvatarOption.REMOVE)
            dismiss()
        }
    }
}