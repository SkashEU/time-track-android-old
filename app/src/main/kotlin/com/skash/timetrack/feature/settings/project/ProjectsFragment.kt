package com.skash.timetrack.feature.settings.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.helper.state.loading.LoadingDialog
import com.skash.timetrack.core.model.ProjectModifyWrapper
import com.skash.timetrack.databinding.FragmentProjectsBinding
import com.skash.timetrack.feature.adapter.ProjectListAdapter
import com.skash.timetrack.feature.settings.SettingsFragmentDirections
import com.skash.timetrack.feature.settings.project.manage.ManageProjectFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProjectsViewModel by viewModels()

    private val adapter = ProjectListAdapter(onProjectSelected = {
        findNavController().navigate(
            SettingsFragmentDirections.navigateToManageProject(it)
        )
    })

    private val loadingDialog: LoadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProjectsBinding.bind(view)

        bindActions()
        observeProjectChanges()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewModel.projectsLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { projects ->
                adapter.submitList(projects)
            })
        }
    }

    private fun bindActions() {
        binding.addFab.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.navigateToManageProject(null)
            )
        }
    }

    private fun observeProjectChanges() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ProjectModifyWrapper>(
            ManageProjectFragment.PROJECT
        )?.observe(viewLifecycleOwner) {
            viewModel.fetchProjects()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}