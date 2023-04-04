package com.skash.timetrack.feature.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skash.timetrack.R
import com.skash.timetrack.core.menu.ProfileSectionEntryType
import com.skash.timetrack.core.model.Workspace
import com.skash.timetrack.databinding.FragmentProfileBinding
import com.skash.timetrack.feature.adapter.ProfileSectionListAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProfileBinding.bind(view)

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