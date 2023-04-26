package com.skash.timetrack.feature.workspace

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.dialog.FullscreenDialogFragment
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.model.Workspace
import com.skash.timetrack.databinding.FragmentWorkspaceSelectionBinding
import com.skash.timetrack.feature.adapter.OrganizationListAdapter
import com.skash.timetrack.feature.adapter.WorkspaceListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class WorkspaceSelectionFragment : FullscreenDialogFragment(
    R.layout.fragment_workspace_selection
) {

    private var _binding: FragmentWorkspaceSelectionBinding? = null
    private val binding get() = _binding!!

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

    private val subscriptions = CompositeDisposable()

    companion object {
        const val WORKSPACE_UPDATE = "workspace_update"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWorkspaceSelectionBinding.bind(view)

        bindActions()

        binding.workspaceRecyclerView.adapter = workspaceListAdapter
        binding.organizationRecyclerView.adapter = organizationListAdapter

        viewModel.saveStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = {
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

    private fun bindActions() {
        binding.saveButton.clicks()
            .subscribe {
                viewModel.saveSelectedOptions()
            }
            .addTo(subscriptions)

        binding.closeButton.clicks()
            .subscribe {
                dismiss()
            }
            .addTo(subscriptions)
    }

    private fun notifyCallerAboutChanges(workspace: Workspace) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            WORKSPACE_UPDATE,
            workspace
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        _binding = null
    }
}