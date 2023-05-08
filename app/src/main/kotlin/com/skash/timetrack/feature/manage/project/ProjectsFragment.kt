package com.skash.timetrack.feature.manage.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.helper.state.loading.LoadingDialog
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.ProjectModifyWrapper
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentProjectsBinding
import com.skash.timetrack.feature.adapter.ProjectListAdapter
import com.skash.timetrack.feature.manage.ManageFragmentDirections
import com.skash.timetrack.feature.manage.project.manage.ManageProjectFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : BindableFragment<FragmentProjectsBinding>(R.layout.fragment_projects) {

    private val viewModel: ProjectsViewModel by viewModels()

    private val adapter = ProjectListAdapter(onProjectSelected = { project ->
        navigateToManageProject(project)
    })

    private val loadingDialog: LoadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeProjectChanges()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewModel.projectsLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { projects ->
                adapter.submitList(projects)
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentProjectsBinding {
        return FragmentProjectsBinding.bind(view)
    }

    override fun bindActions() {
        binding.addFab.setOnClickListener {
            navigateToManageProject(null)
        }
    }

    private fun observeProjectChanges() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ProjectModifyWrapper>(
            ManageProjectFragment.PROJECT
        )?.observe(viewLifecycleOwner) {
            viewModel.fetchProjects()
        }
    }

    private fun navigateToManageProject(project: Project?) {
        findNavController().navigate(
            ManageFragmentDirections.navigateToManageProject(
                project
            )
        )
    }
}