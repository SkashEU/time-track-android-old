package com.skash.timetrack.feature.manage.team

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentTeamBinding
import com.skash.timetrack.feature.adapter.TeamMemberListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamFragment : BindableFragment<FragmentTeamBinding>(R.layout.fragment_team) {

    private val viewModel: TeamViewModel by viewModels()

    private val adapter = TeamMemberListAdapter()

    private val loadingDialog by lazy {
        DefaultLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewModel.teamMemberStateLiveData.observe(viewLifecycleOwner) { state ->
            state.handle(requireContext(), loadingDialog, onSuccess = { members ->
                adapter.submitList(members)
            })
        }
    }

    override fun createBindingInstance(view: View): FragmentTeamBinding {
        return FragmentTeamBinding.bind(view)
    }

    override fun bindActions() {}
}