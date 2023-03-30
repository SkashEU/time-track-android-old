package com.skash.timetrack.feature.manage.project.manage

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.helper.state.loading.LoadingDialog
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.ProjectModifyWrapper
import com.skash.timetrack.databinding.FragmentManageProjectBinding
import com.skash.timetrack.feature.adapter.ProjectColorListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class ManageProjectFragment : BottomSheetDialogFragment(
    R.layout.fragment_manage_project
) {
    private var _binding: FragmentManageProjectBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ManageProjectViewModel by viewModels()

    private val adapter = ProjectColorListAdapter(onColorClicked = { color ->
        viewModel.markProjectColorAsSelected(color)
    })

    private val loadingDialog: LoadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    private val subscriptions = CompositeDisposable()

    companion object {
        const val PROJECT = "key_project"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentManageProjectBinding.bind(view)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.recyclerView.adapter = adapter

        bindActions()

        viewModel.projectStateLiveData.observe(viewLifecycleOwner) { projectState ->
            projectState.handle(requireContext(), loadingDialog, onSuccess = { project ->
                notifyCallerAboutProjectChange(project)
            })
        }

        viewModel.colorLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.projectLiveData.observe(viewLifecycleOwner) {
            binding.titleEditText.setText(it.title)
        }

        binding.titleEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.titleEditText.clearFocus()
            }
            false
        }
    }

    private fun bindActions() {
        binding.saveButton.clicks()
            .subscribe {
                viewModel.createOrUpdateProject(
                    binding.titleEditText.text.toString()
                )
            }.addTo(subscriptions)

        binding.closeButton.clicks()
            .subscribe {
                dismiss()
            }.addTo(subscriptions)
    }

    private fun notifyCallerAboutProjectChange(project: ProjectModifyWrapper) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            PROJECT, project
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        _binding = null
    }
}