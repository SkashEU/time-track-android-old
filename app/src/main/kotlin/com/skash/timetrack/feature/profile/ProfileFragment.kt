package com.skash.timetrack.feature.profile

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.sharedprefs.clearAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.helper.sharedprefs.getSelectedWorkspace
import com.skash.timetrack.core.helper.sharedprefs.getSelfUser
import com.skash.timetrack.core.helper.sharedprefs.saveSelfUser
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.menu.ProfileSectionEntry
import com.skash.timetrack.core.model.User
import com.skash.timetrack.core.model.Workspace
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentProfileBinding
import com.skash.timetrack.feature.adapter.ProfileSectionListAdapter
import com.skash.timetrack.feature.auth.login.LoginActivity
import com.skash.timetrack.feature.profile.avatar.ManageAvatarBottomSheet
import com.skash.timetrack.feature.profile.avatar.ManageAvatarOption
import com.skash.timetrack.feature.profile.username.NameChangeFragment
import com.skash.timetrack.feature.workspace.WorkspaceSelectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BindableFragment<FragmentProfileBinding>(
    R.layout.fragment_profile
) {

    private val viewModel: ProfileViewModel by viewModels()

    private val adapter = ProfileSectionListAdapter(
        onEntryClicked = {
            viewModel.onProfileSectionClicked(it)
        }
    )

    private val manageAvatarBottomSheet = ManageAvatarBottomSheet(
        onOptionSelected = { option ->
            when (option) {
                ManageAvatarOption.REMOVE -> TODO()
                ManageAvatarOption.MODIFY -> photoPickContract.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
    )

    private val photoPickContract = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        if (uri == null) {
            return@registerForActivityResult
        }

        val inputStream = requireContext().contentResolver.openInputStream(uri)
            ?: return@registerForActivityResult

        val avatarBytes = inputStream.buffered().use { buffer ->
            buffer.readBytes()
        }

        viewModel.updateAvatar(avatarBytes)
    }

    override fun createBindingInstance(view: View): FragmentProfileBinding {
        return FragmentProfileBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeWorkspaceChanges()
        observeUsernameChanges()

        binding.recyclerView.adapter = adapter

        requireContext().getPrefs().getSelfUser()?.let { user ->
            binding.nameTextView.text = user.name
            loadUserAvatar(user)
        }

        requireContext().getPrefs().getSelectedWorkspace()?.let {
            binding.workspaceTitleTextView.text = it.title
        }

        viewModel.profileSectionLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.avatarUploadStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), null, onSuccess = {
                val user = requireContext().getPrefs().getSelfUser() ?: return@handle
                requireContext().getPrefs().saveSelfUser(user.copy(avatar = it))
                loadUserAvatar(user)
            })
        }

        viewModel.profileSelectionLiveData.observe(viewLifecycleOwner) {
            val navDirection = when (it) {
                ProfileSectionEntry.USERNAME -> ProfileFragmentDirections.navigateToNameChange()
                ProfileSectionEntry.EMAIL -> TODO()
                ProfileSectionEntry.PASSWORD -> TODO()
                ProfileSectionEntry.WORKSPACE -> ProfileFragmentDirections.navigateToWorkspaceSelection()
                ProfileSectionEntry.TWO_FACTOR_AUTH -> TODO()
                ProfileSectionEntry.BACKUP_CODES -> ProfileFragmentDirections.navigateToBackupCodes()
                ProfileSectionEntry.MY_INVITES -> TODO()
            }

            findNavController().navigate(
                navDirection
            )
        }
    }

    override fun bindActions() {
        binding.logoutButton.setOnClickListener {
            requireContext().getPrefs().clearAuthData()
            LoginActivity.launch(requireContext())
            requireActivity().finish()
        }

        binding.pfpImageView.setOnClickListener {
            manageAvatarBottomSheet.show(childFragmentManager, null)
        }
    }

    private fun observeWorkspaceChanges() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Workspace>(
            WorkspaceSelectionFragment.WORKSPACE_UPDATE
        )?.observe(viewLifecycleOwner) {
            binding.workspaceTitleTextView.text = it.title
        }
    }

    private fun observeUsernameChanges() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            NameChangeFragment.NAME_CHANGE
        )?.observe(viewLifecycleOwner) {
            binding.nameTextView.text = it
        }
    }

    private fun loadUserAvatar(user: User) {
        if (user.avatar == null) {
            return
        }

        Glide.with(requireContext())
            .load(user.avatar.avatarURL())
            .into(binding.pfpImageView)
    }
}