package com.skash.timetrack.feature.workspace

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.helper.sharedprefs.saveSelectedWorkspace
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.model.Workspace
import com.skash.timetrack.core.util.BindableFullscreenDialog
import com.skash.timetrack.databinding.FragmentWorkspaceSelectionBinding
import com.skash.timetrack.feature.adapter.OrganizationListAdapter
import com.skash.timetrack.feature.adapter.WorkspaceListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkspaceSelectionFragment : BindableFullscreenDialog<FragmentWorkspaceSelectionBinding>(
    R.layout.fragment_workspace_selection
) {

    private val viewModel: WorkspaceSelectionViewModel by viewModels()

    private val organizationListAdapter = OrganizationListAdapter(
        onOrganizationClicked = {
            viewModel.markOrganizationAsSelected(it)
            viewModel.fetchWorkspacesForOrganization(it.organization.id)
        }
    )

    private val workspaceListAdapter = WorkspaceListAdapter(
        onWorkspaceClicked = {
            viewModel.markWorkspaceAsSelected(it)
        }
    )

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    companion object {
        const val WORKSPACE_UPDATE = "workspace_update"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workspaceRecyclerView.adapter = workspaceListAdapter
        binding.organizationRecyclerView.adapter = organizationListAdapter

        viewModel.saveStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = {
                requireContext().getPrefs().saveSelectedWorkspace(it)
                notifyCallerAboutChanges(it)
                dismiss()
            })
        }

        viewModel.workspaceStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { workspaces ->
                workspaceListAdapter.submitList(workspaces)
            })
        }

        viewModel.organizationsStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { organizations ->
                organizationListAdapter.submitList(organizations)
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentWorkspaceSelectionBinding {
        return FragmentWorkspaceSelectionBinding.bind(view)
    }

    override fun bindActions() {
        binding.saveButton.setOnClickListener {
            viewModel.saveSelectedOptions()
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun notifyCallerAboutChanges(workspace: Workspace) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            WORKSPACE_UPDATE,
            workspace
        )
    }
}