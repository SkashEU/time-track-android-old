package com.skash.timetrack.feature.profile

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.sharedprefs.clearAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.menu.ProfileSectionEntryType
import com.skash.timetrack.core.model.Workspace
import com.skash.timetrack.databinding.FragmentProfileBinding
import com.skash.timetrack.feature.adapter.ProfileSectionListAdapter
import com.skash.timetrack.feature.auth.login.LoginActivity
import com.skash.timetrack.feature.profile.avatar.ManageAvatarBottomSheet
import com.skash.timetrack.feature.profile.avatar.ManageAvatarOption
import com.skash.timetrack.feature.profile.username.NameChangeFragment
import com.skash.timetrack.feature.workspace.WorkspaceSelectionBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private val adapter = ProfileSectionListAdapter(onEntryClicked = {
        viewModel.onProfileSectionClicked(it.type)
    })

    private val manageAvatarBottomSheet = ManageAvatarBottomSheet(onOptionSelected = { option ->
        when (option) {
            ManageAvatarOption.REMOVE -> TODO()
            ManageAvatarOption.MODIFY -> photoPickContract.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    })

    private val photoPickContract =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            binding.pfpImageView.setImageURI(it)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProfileBinding.bind(view)

        bindActions()
        observeWorkspaceChanges()
        observeUsernameChanges()

        binding.recyclerView.adapter = adapter

        viewModel.profileSectionLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.profileSelectionLiveData.observe(viewLifecycleOwner) {
            val navDirection = when (it) {
                ProfileSectionEntryType.USERNAME -> ProfileFragmentDirections.navigateToNameChange()
                ProfileSectionEntryType.EMAIL -> TODO()
                ProfileSectionEntryType.PASSWORD -> TODO()
                ProfileSectionEntryType.WORKSPACE -> ProfileFragmentDirections.navigateToWorkspaceSelection()
                ProfileSectionEntryType.TWO_FACTOR_AUTH -> TODO()
                ProfileSectionEntryType.BACKUP_CODES -> ProfileFragmentDirections.navigateToBackupCodes()
            }

            findNavController().navigate(
                navDirection
            )
        }
    }

    private fun bindActions() {
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
            WorkspaceSelectionBottomSheet.WORKSPACE_UPDATE
        )?.observe(viewLifecycleOwner) {
            viewModel.fetchProfileSections()
        }
    }

    private fun observeUsernameChanges() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            NameChangeFragment.NAME_CHANGE
        )?.observe(viewLifecycleOwner) {
            viewModel.fetchProfileSections()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}